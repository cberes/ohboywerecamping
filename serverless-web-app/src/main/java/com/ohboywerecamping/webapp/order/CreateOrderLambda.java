package com.ohboywerecamping.webapp.order;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohboywerecamping.customer.CustomerComponent;
import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Order;
import com.ohboywerecamping.order.OrderComponent;
import com.ohboywerecamping.webapp.util.Cognito;
import com.ohboywerecamping.webapp.Main;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.badRequest;
import static com.ohboywerecamping.webapp.util.Responses.created;

public class CreateOrderLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Request {
        private List<LocalDate> days;

        public List<LocalDate> getDays() {
            return days;
        }

        public void setDays(final List<LocalDate> days) {
            this.days = days;
        }
    }

    private final ObjectMapper jackson = Main.jackson();
    private final OrderComponent orders = Main.orderComponent();
    private final CustomerComponent customers = Main.customerComponent();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        context.getLogger().log("Authenticated username is  " + Cognito.username(input).orElse(null));

        final Request request;
        try {
            request = jackson.readValue(input.getBody(), Request.class);
        } catch (IOException e) {
            return badRequest(e.getMessage());
        }

        final String campsiteId = input.getPathParameters().get("campsiteId");

        final Customer customer = customers.findOrCreateCustomer(Cognito.username(input).get());

        final Order order = orders.create(customer, campsiteId, request.getDays());

        return created(JsonUtils.toJson(order));
    }
}
