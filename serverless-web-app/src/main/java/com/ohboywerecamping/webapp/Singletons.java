package com.ohboywerecamping.webapp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohboywerecamping.area.AreaRepository;
import com.ohboywerecamping.availability.AvailabilityService;
import com.ohboywerecamping.campground.CampgroundRepository;
import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.customer.CustomerComponent;
import com.ohboywerecamping.customer.CustomerComponentImpl;
import com.ohboywerecamping.customer.CustomerRepository;
import com.ohboywerecamping.order.OrderComponent;
import com.ohboywerecamping.order.OrderComponentImpl;
import com.ohboywerecamping.order.OrderRepository;
import com.ohboywerecamping.reservation.ReservationRepository;
import com.ohboywerecamping.webapp.campsite.FakeCampsiteRepository;
import com.ohboywerecamping.webapp.customer.DynamoCustomerRepository;
import com.ohboywerecamping.webapp.order.DynamoOrderRepository;
import com.ohboywerecamping.webapp.reservation.DynamoReservationRepository;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public final class Singletons {
    private static final Singletons instance = new Singletons();

    private ObjectMapper jackson;
    private DynamoDbClient ddb;
    private AreaRepository areas;
    private CampgroundRepository campgrounds;
    private CampsiteRepository campsites;
    private CustomerRepository customers;
    private OrderRepository orders;
    private ReservationRepository reservations;
    private AvailabilityService availabilityService;
    private CustomerComponent customerComponent;
    private OrderComponent orderComponent;

    private Singletons() {
    }

    public static ObjectMapper jackson() {
        if (instance.jackson == null) {
            instance.jackson = new ObjectMapper();
            instance.jackson.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            instance.jackson.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            instance.jackson.registerModule(new JavaTimeModule());
        }
        return instance.jackson;
    }


    public static DynamoDbClient dynamo() {
        if (instance.ddb == null) {
            instance.ddb = DynamoDbClient.builder().region(AwsUtils.region()).build();
        }
        return instance.ddb;
    }

    public static CampsiteRepository campsites() {
        if (instance.campsites == null) {
            instance.campsites = new FakeCampsiteRepository();
        }
        return instance.campsites;
    }

    public static CustomerRepository customers() {
        if (instance.customers == null) {
            instance.customers = new DynamoCustomerRepository(dynamo());
        }
        return instance.customers;
    }

    public static OrderRepository orders() {
        if (instance.orders == null) {
            instance.orders = new DynamoOrderRepository(dynamo());
        }
        return instance.orders;
    }

    public static ReservationRepository reservations() {
        if (instance.reservations == null) {
            instance.reservations = new DynamoReservationRepository(dynamo());
        }
        return instance.reservations;
    }

    public static AvailabilityService availabilityService() {
        if (instance.availabilityService == null) {
            instance.availabilityService = new AvailabilityService(null, campsites(), reservations());
        }
        return instance.availabilityService;
    }

    public static CustomerComponent customerComponent() {
        if (instance.customerComponent == null) {
            instance.customerComponent = new CustomerComponentImpl(customers());
        }
        return instance.customerComponent;
    }

    public static OrderComponent orderComponent() {
        if (instance.orderComponent == null) {
            instance.orderComponent = new OrderComponentImpl(customers(), orders(), campsites(), reservations());
        }
        return instance.orderComponent;
    }
}
