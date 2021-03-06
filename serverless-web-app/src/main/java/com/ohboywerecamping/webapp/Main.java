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
                "read_availability", Lambdas.LiveReadAvailabilityLambda::new,
                "read_campground", Lambdas.LiveReadCampgroundLambda::new,
                "read_campsite", Lambdas.LiveReadCampsiteLambda::new,
                "read_campsite_list", Lambdas.LiveReadCampsiteListLambda::new,
                "create_order", Lambdas.LiveCreateOrderLambda::new,
                "read_order", Lambdas.LiveReadOrderLambda::new,
                "read_order_list", Lambdas.LiveReadOrderListLambda::new);
    }
}
