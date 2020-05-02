package com.ohboywerecamping.webapp;

import java.util.Map;
import java.util.function.Supplier;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.webapp.availability.ReadAvailabilityLambda;
import com.ohboywerecamping.webapp.order.CreateOrderLambda;
import com.ohboywerecamping.webapp.order.ReadOrderLambda;
import com.ohboywerecamping.webapp.order.ReadOrderListLambda;

public final class Main {
    public static void main(String[] args) throws Exception {
        run(System.getenv("_HANDLER"));
    }

    static void run(final String handlerName) throws Exception {
        final Map<String, Supplier<RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>>> handlers =
                Map.of(
                        "read_availability", ReadAvailabilityLambda::new,
                        "create_order", CreateOrderLambda::new,
                        "read_order", ReadOrderLambda::new,
                        "read_order_list", ReadOrderListLambda::new);

        final var handler = handlers.get(handlerName);
        if (handler != null) {
            Lambda.handleEvents(handler.get()::handleRequest, APIGatewayProxyRequestEvent.class);
        }
    }
}
