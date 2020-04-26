package com.ohboywerecamping.webapp.order;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.webapp.Cognito;

import static java.util.Collections.singletonMap;

public class CreateOrderLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        context.getLogger().log("Authenticated username is  " + Cognito.username(input).orElse(null));

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody("{}");
        response.setHeaders(singletonMap("Access-Control-Allow-Origin", "*"));
        response.setStatusCode(201);
        return response;
    }
}
