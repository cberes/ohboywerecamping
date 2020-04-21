package com.ohboywerecamping.webapp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import static java.util.Collections.singletonMap;

public class RequestUnicorn implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public static class Request {
        @JsonProperty("PickupLocation")
        private Location pickupLocation;

        public Location getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(final Location pickupLocation) {
            this.pickupLocation = pickupLocation;
        }
    }

    public static class Response {
        @JsonProperty("RideId")
        private String rideId;
        @JsonProperty("Unicorn")
        private Unicorn unicorn;
        @JsonProperty("UnicornName")
        private String unicornName;
        @JsonProperty("Eta")
        private String eta;
        @JsonProperty("Ride")
        private String rider;

        public String getRideId() {
            return rideId;
        }

        public void setRideId(final String rideId) {
            this.rideId = rideId;
        }

        public Unicorn getUnicorn() {
            return unicorn;
        }

        public void setUnicorn(final Unicorn unicorn) {
            this.unicorn = unicorn;
        }

        public String getUnicornName() {
            return unicornName;
        }

        public void setUnicornName(final String unicornName) {
            this.unicornName = unicornName;
        }

        public String getEta() {
            return eta;
        }

        public void setEta(final String eta) {
            this.eta = eta;
        }

        public String getRider() {
            return rider;
        }

        public void setRider(final String rider) {
            this.rider = rider;
        }
    }

    private final List<Unicorn> fleet = List.of(
            new Unicorn("Bucephalus", "Golden", "Male"),
            new Unicorn("Shadowfax", "White", "Male"),
            new Unicorn("Rocinante", "Yellow", "Female"));


    private final Region region = Region.of(System.getenv("AWS_REGION"));
    private final DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();
    private final ObjectMapper jackson = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        final String rideId = randomId();
        final String username = Cognito.username(input).orElse(null);
        final Request request = request(input);
        context.getLogger().log("Finding unicorn for " + request.getPickupLocation().getLatitude() +
                ", " + request.getPickupLocation().getLongitude());
        final Unicorn unicorn = findUnicorn(request.getPickupLocation());
        recordRide(rideId, username, unicorn);

        final Response response = new Response();
        response.setRideId(rideId);
        response.setUnicorn(unicorn);
        response.setUnicornName(unicorn.getName());
        response.setEta("30 seconds");
        response.setRider(username);
        return response(response);
    }

    private Request request(final APIGatewayProxyRequestEvent input) {
        try {
            return jackson.readValue(input.getBody(), Request.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String randomId() {
        final byte[] buffer = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(buffer);
        } catch (NoSuchAlgorithmException e) {
            // I'll take that risk
            throw new RuntimeException(e);
        }
        return toUrlString(buffer);
    }

    private static String toUrlString(byte[] buffer) {
        return Base64.getEncoder().encodeToString(buffer)
                .replace('+', '-')
                .replace('/', '_')
                .replace("=", "");
    }

    private Unicorn findUnicorn(final Location pickupLocation) {
        return fleet.get((int) Math.floor(Math.random() * fleet.size()));
    }

    private void recordRide(final String rideId, final String username, final Unicorn unicorn) {
        final HashMap<String, AttributeValue> itemValues = new HashMap<>();

        itemValues.put("RideId", AttributeValue.builder().s(rideId).build());
        itemValues.put("User", AttributeValue.builder().s(username).build());
        itemValues.put("Unicorn", AttributeValue.builder().m(toAttributeMap(unicorn)).build());
        itemValues.put("UnicornName", AttributeValue.builder().s(unicorn.getName()).build());
        itemValues.put("RequestTime", AttributeValue.builder().s(ZonedDateTime.now(ZoneOffset.UTC).toString()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("Rides")
                .item(itemValues)
                .build();

        ddb.putItem(request);
    }

    private static Map<String, AttributeValue> toAttributeMap(final Unicorn unicorn) {
        final HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("Name", AttributeValue.builder().s(unicorn.getName()).build());
        itemValues.put("Color", AttributeValue.builder().s(unicorn.getColor()).build());
        itemValues.put("Gender", AttributeValue.builder().s(unicorn.getGender()).build());
        return itemValues;
    }

    private APIGatewayProxyResponseEvent response(final Response obj) {
        try {
            final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setBody(jackson.writeValueAsString(obj));
            response.setHeaders(singletonMap("Access-Control-Allow-Origin", "*"));
            response.setStatusCode(201);
            return response;
        } catch (IOException e) {
            // I'll take my chances
            throw new RuntimeException(e);
        }
    }
}
