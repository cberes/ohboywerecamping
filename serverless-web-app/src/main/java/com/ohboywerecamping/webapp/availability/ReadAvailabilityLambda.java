package com.ohboywerecamping.webapp.availability;

import java.time.LocalDate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.availability.AvailabilityService;
import com.ohboywerecamping.domain.CampgroundAvailability;
import com.ohboywerecamping.webapp.Singletons;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.badRequest;
import static com.ohboywerecamping.webapp.util.Responses.ok;

public class ReadAvailabilityLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Response {
        private final CampgroundAvailability availability;

        public Response(final CampgroundAvailability availability) {
            this.availability = availability;
        }

        public CampgroundAvailability getAvailability() {
            return availability;
        }
    }

    private static final String AREA = "areaId";
    private static final String CAMPGROUND = "campgroundId";
    private static final String CAMPSITE = "campsiteId";

    private final AvailabilityService service = Singletons.availabilityService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        final LocalDate start = LocalDate.parse(input.getQueryStringParameters().get("start"));
        final LocalDate end = LocalDate.parse(input.getQueryStringParameters().get("end"));

        final CampgroundAvailability availability;
        if (input.getPathParameters().containsKey(CAMPGROUND)) {
            availability = service.findByCampgroundId(input.getPathParameters().get(CAMPGROUND), start, end);
        } else if (input.getPathParameters().containsKey(AREA)) {
            availability = service.findByAreaId(input.getPathParameters().get(AREA), start, end);
        } else if (input.getPathParameters().containsKey(CAMPSITE)) {
            availability = service.findByCampsiteId(input.getPathParameters().get(CAMPSITE), start, end);
        } else {
            return badRequest("missing path parameter");
        }

        return ok(JsonUtils.toJson(new Response(availability)));
    }
}
