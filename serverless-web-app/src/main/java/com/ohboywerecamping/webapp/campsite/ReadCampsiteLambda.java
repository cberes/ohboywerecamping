package com.ohboywerecamping.webapp.campsite;

import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.campsite.CampsiteComponent;
import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.*;

public class ReadCampsiteLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Response {
        private final Campsite campsite;

        public Response(final Campsite campsite) {
            this.campsite = campsite;
        }

        public Campsite getCampsite() {
            return campsite;
        }
    }

    private final CampsiteComponent campsites;

    public ReadCampsiteLambda(final CampsiteComponent campsites) {
        this.campsites = campsites;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        final String campsiteId = input.getPathParameters().get("campsiteId");
        // TODO we need to make sure that this campsite is owned by the campground that we're viewing

        final Optional<Campsite> campsite = campsites.findCampsiteById(campsiteId);
        if (campsite.isEmpty()) {
            return notFound("{\"message\":\"Campsite not found: " + campsiteId + "\"}");
        }

        return ok(JsonUtils.toJson(new Response(campsite.get())));
    }
}
