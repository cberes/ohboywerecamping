package com.ohboywerecamping.webapp.campground;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ohboywerecamping.campground.CampgroundComponent;
import com.ohboywerecamping.domain.Campground;
import com.ohboywerecamping.webapp.util.JsonUtils;

import static com.ohboywerecamping.webapp.util.Responses.*;

public class ReadCampgroundLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Response {
        private final Campground campground;

        public Response(final Campground campground) {
            this.campground = campground;
        }

        public Campground getCampground() {
            return campground;
        }
    }

    private static class CampgroundNotFoundException extends Exception {
        public CampgroundNotFoundException(final String message) {
            super(message);
        }
    }

    private final CampgroundComponent campgrounds;

    public ReadCampgroundLambda(final CampgroundComponent campgrounds) {
        this.campgrounds = campgrounds;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        context.getLogger().log("Received event in " + getClass().getSimpleName());

        final String campgroundId = input.getPathParameters().get("campgroundId");
        final String referer = input.getHeaders().get("Referer");

        try {
            final Campground campground = findCampground(campgroundId, referer);
            return ok(JsonUtils.toJson(new Response(campground)));
        } catch (CampgroundNotFoundException e) {
            return notFound("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private Campground findCampground(final String id, final String referer) throws CampgroundNotFoundException {
        if (id == null) {
            return findByReferer(referer);
        } else {
            return findById(id);
        }
    }

    private Campground findById(final String id) throws CampgroundNotFoundException {
        return campgrounds.findCampgroundById(id)
                .orElseThrow(() -> new CampgroundNotFoundException("Campground was not found: " + id));
    }

    private Campground findByReferer(final String referer) throws CampgroundNotFoundException {
        return campgrounds.findCampgroundByReferer(referer)
                .orElseThrow(() -> new CampgroundNotFoundException("Campground not found for referer " + referer));
    }
}
