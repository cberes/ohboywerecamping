package com.ohboywerecamping.webapp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiFunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohboywerecamping.webapp.util.Responses;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public final class Lambda {
    public static class LambdaError {
        public final String errorMessage;
        public final String errorType;

        public LambdaError(final Exception e) {
            this(e.getMessage(), e.getClass().getName());
        }

        public LambdaError(final String errorMessage, final String errorType) {
            this.errorMessage = errorMessage;
            this.errorType = errorType;
        }
    }

    private static final ObjectMapper jackson = Singletons.jackson();

    private Lambda() {
        throw new UnsupportedOperationException("cannot instantiate " + getClass());
    }

    public static <E> void handleEvents(final BiFunction<E, Context, ?> handler,
                                        final Class<E> type) throws IOException {
        handleEvents(handler, type, System.getenv("AWS_LAMBDA_RUNTIME_API"));
    }

    public static <E> void handleEvents(final BiFunction<E, Context, ?> handler,
                                        final Class<E> type,
                                        final String runtimeApiHost) throws IOException {
        final String nextEventUrl = eventUrl(runtimeApiHost);
        final URL url = new URL(nextEventUrl);
        while (true) {
            handleEvent(handler, type, runtimeApiHost, url);
        }
    }

    private static <E> void handleEvent(final BiFunction<E, Context, ?> handler,
                                        final Class<E> type,
                                        final String runtimeApiHost,
                                        final URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setUseCaches(false);

        final E event = jackson.readValue(connection.getInputStream(), type);
        final long deadlineMs = connection.getHeaderFieldLong("Lambda-Runtime-Deadline-Ms", 0L);
        final String requestId = connection.getHeaderField("Lambda-Runtime-Aws-Request-Id");
        final String functionArn = connection.getHeaderField("Lambda-Runtime-Invoked-Function-Arn");
        final Context context = new DefaultContext(requestId, functionArn, deadlineMs);
        handleEvent(handler, event, context, runtimeApiHost);
    }

    private static <E> void handleEvent(final BiFunction<E, Context, ?> handler,
                                        final E input,
                                        final Context context,
                                        final String runtimeApiHost) throws IOException {
        try {
            final Object response = handler.apply(input, context);
            handleRequest(context.getAwsRequestId(), response, runtimeApiHost);
        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
            e.printStackTrace();
            handleRequest(context.getAwsRequestId(), serverError(e), runtimeApiHost);
            handleError(context.getAwsRequestId(), e, runtimeApiHost);
        }
    }

    private static Object serverError(final Exception e) throws IOException {
        return Responses.serverError(jackson.writeValueAsString(new LambdaError(e)));
    }

    private static void handleRequest(final String requestId, final Object response, final String runtimeApiHost) throws IOException {
        final String responseBody = jackson.writeValueAsString(response);
        sendResponse(responseUrl(runtimeApiHost, requestId), responseBody, emptyMap());
    }

    private static void handleError(final String requestId, Exception e, final String runtimeApiHost) throws IOException {
        final String responseBody = jackson.writeValueAsString(new LambdaError(e));
        sendResponse(invocationErrorUrl(runtimeApiHost, requestId), responseBody,
                singletonMap("Lambda-Runtime-Function-Error-Type", "Unhandled"));
    }

    private static void sendResponse(final String url, final String responseBody, final Map<String, String> headers) throws IOException {
        final byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setRequestProperty("Content-Length", Integer.toString(responseBytes.length));
        connection.setRequestProperty("Content-Type", "application/json");
        headers.forEach(connection::setRequestProperty);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        try (OutputStream requestStream = connection.getOutputStream()) {
            requestStream.write(responseBytes, 0, responseBytes.length);
        }

        connection.getInputStream();
    }

    private static String eventUrl(final String host) {
        return String.format("http://%s/2018-06-01/runtime/invocation/next", host);
    }

    private static String responseUrl(final String host, final String requestId) {
        return String.format("http://%s/2018-06-01/runtime/invocation/%s/response", host, requestId);
    }

    private static String invocationErrorUrl(final String host, final String requestId) {
        return String.format("http://%s/2018-06-01/runtime/invocation/%s/error", host, requestId);
    }
}
