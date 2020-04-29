package com.ohboywerecamping.webapp.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.customer.CustomerComponent;
import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Order;
import com.ohboywerecamping.order.OrderComponent;
import com.ohboywerecamping.webapp.Main;
import com.ohboywerecamping.webapp.util.Cognito;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.*;

public class ReadOrderListLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Response {
        private final List<Order> orders;

        public Response(final List<Order> orders) {
            this.orders = orders;
        }

        public List<Order> getOrders() {
            return orders;
        }
    }

    private final OrderComponent orders = Main.orderComponent();
    private final CustomerComponent customers = Main.customerComponent();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        context.getLogger().log("Authenticated username is  " + Cognito.username(input).orElse(null));

        final Optional<Customer> customer = Cognito.username(input).flatMap(customers::findCustomerByEmail);
        final List<Order> found = orders.findOrdersByCustomerAfterDate(customer.get().getId(), startDate(input));
        return ok(JsonUtils.toJson(new Response(found)));
    }

    private static LocalDate startDate(final APIGatewayProxyRequestEvent input) {
        return Optional.ofNullable(input.getQueryStringParameters())
                .map(params -> params.get("start"))
                .filter(s -> !s.isBlank())
                .map(LocalDate::parse)
                .orElse(LocalDate.MIN);
    }
}
