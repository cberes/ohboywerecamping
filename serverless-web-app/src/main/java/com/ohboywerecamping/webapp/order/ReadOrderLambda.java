package com.ohboywerecamping.webapp.order;

import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.customer.CustomerComponent;
import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Order;
import com.ohboywerecamping.order.OrderComponent;
import com.ohboywerecamping.webapp.util.Cognito;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.forbidden;
import static com.ohboywerecamping.webapp.util.Responses.notFound;
import static com.ohboywerecamping.webapp.util.Responses.ok;

public class ReadOrderLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Response {
        private final Order order;

        public Response(final Order order) {
            this.order = order;
        }

        public Order getOrder() {
            return order;
        }
    }

    private final OrderComponent orders;
    private final CustomerComponent customers;

    public ReadOrderLambda(final OrderComponent orders, final CustomerComponent customers) {
        this.orders = orders;
        this.customers = customers;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        context.getLogger().log("Authenticated username is  " + Cognito.username(input).orElse(null));

        final String orderId = input.getPathParameters().get("orderId");

        final Optional<Order> order = orders.findOrderById(orderId);
        if (order.isEmpty()) {
            return notFound("{\"message\":\"Order not found: " + orderId + "\"}");
        }

        final Optional<Customer> customer = Cognito.username(input).flatMap(customers::findCustomerByEmail);
        if (customer.isEmpty() || !customer.get().getId().equals(order.get().getCustomer().getId())) {
            return forbidden("{\"message\":\"You are not allowed to access this order\"}");
        }

        return ok(JsonUtils.toJson(new Response(order.get())));
    }
}
