package com.ohboywerecamping.webapp;

import java.util.Map;
import java.util.function.Supplier;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public final class Main {
    public static void main(String[] args) throws Exception {
        run(System.getenv("_HANDLER"));
    }

    static void run(final String handlerName) throws Exception {
        final var handler = handlers().get(handlerName);
        if (handler == null) {
            System.err.println("Unknown handler: " + handlerName);
        } else {
            Lambda.handleEvents(handler.get()::handleRequest, APIGatewayProxyRequestEvent.class);
        }
    }

    private static Map<String, Supplier<RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>>> handlers() {
        return Map.of(
                "read_availability", Lambdas::readAvailability,
                "create_order", Lambdas::createOrder,
                "read_order", Lambdas::readOrder,
                "read_order_list", Lambdas::readOrderList);
    }
}
