package com.ohboywerecamping.webapp.campsite;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.campsite.CampsiteComponent;
import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.*;

public class ReadCampsiteListLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Response {
        private final List<Campsite> campsites;

        public Response(final List<Campsite> campsites) {
            this.campsites = campsites;
        }

        public List<Campsite> getCampsites() {
            return campsites;
        }
    }
    private static final String AREA = "areaId";
    private static final String CAMPGROUND = "campgroundId";

    private final CampsiteComponent campsites;

    public ReadCampsiteListLambda(final CampsiteComponent campsites) {
        this.campsites = campsites;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        final List<Campsite> campsiteList;
        if (input.getPathParameters().containsKey(CAMPGROUND)) {
            campsiteList = campsites.findCampsitesByCampgroundId(input.getPathParameters().get(CAMPGROUND));
        } else if (input.getPathParameters().containsKey(AREA)) {
            campsiteList = campsites.findCampsitesByAreaId(input.getPathParameters().get(AREA));
        } else {
            return badRequest("missing path parameter");
        }

        return ok(JsonUtils.toJson(new Response(campsiteList)));
    }
}
