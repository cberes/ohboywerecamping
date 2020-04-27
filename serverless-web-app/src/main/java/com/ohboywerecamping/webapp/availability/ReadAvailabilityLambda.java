package com.ohboywerecamping.webapp.availability;

import java.time.LocalDate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.availability.AvailabilityService;
import com.ohboywerecamping.domain.CampgroundAvailability;
import com.ohboywerecamping.webapp.Main;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.ok;

public class ReadAvailabilityLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AvailabilityService service = Main.availabilityService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        final String campsiteId = input.getPathParameters().get("campsiteId");
        final LocalDate start = LocalDate.parse(input.getQueryStringParameters().get("start"));
        final LocalDate end = LocalDate.parse(input.getQueryStringParameters().get("end"));

        final CampgroundAvailability availability = service.findByCampsiteId(campsiteId, start, end);

        return ok(JsonUtils.toJson(availability));
    }
}
