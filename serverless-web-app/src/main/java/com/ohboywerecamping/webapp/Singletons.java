package com.ohboywerecamping.webapp;

import com.ohboywerecamping.area.AreaRepository;
import com.ohboywerecamping.availability.AvailabilityService;
import com.ohboywerecamping.campground.CampgroundComponent;
import com.ohboywerecamping.campground.CampgroundComponentImpl;
import com.ohboywerecamping.campground.CampgroundRepository;
import com.ohboywerecamping.campsite.CampsiteComponent;
import com.ohboywerecamping.campsite.CampsiteComponentImpl;
import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.customer.CustomerComponent;
import com.ohboywerecamping.customer.CustomerComponentImpl;
import com.ohboywerecamping.customer.CustomerRepository;
import com.ohboywerecamping.order.OrderComponent;
import com.ohboywerecamping.order.OrderComponentImpl;
import com.ohboywerecamping.order.OrderRepository;
import com.ohboywerecamping.reservation.ReservationRepository;
import com.ohboywerecamping.webapp.campground.DynamoCampgroundRepository;
import com.ohboywerecamping.webapp.campsite.DynamoCampsiteRepository;
import com.ohboywerecamping.webapp.customer.DynamoCustomerRepository;
import com.ohboywerecamping.webapp.order.DynamoOrderRepository;
import com.ohboywerecamping.webapp.reservation.DynamoReservationRepository;
import com.ohboywerecamping.webapp.util.AwsUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

final class Singletons {
    private static final Singletons instance = new Singletons();

    private DynamoDbClient ddb;
    private AreaRepository areas;
    private CampgroundRepository campgrounds;
    private CampsiteRepository campsites;
    private CustomerRepository customers;
    private OrderRepository orders;
    private ReservationRepository reservations;
    private AvailabilityService availabilityService;
    private CampgroundComponent campgroundComponent;
    private CampsiteComponent campsiteComponent;
    private CustomerComponent customerComponent;
    private OrderComponent orderComponent;

    private Singletons() {
    }

    static DynamoDbClient dynamo() {
        if (instance.ddb == null) {
            instance.ddb = DynamoDbClient.builder().region(AwsUtils.region()).build();
        }
        return instance.ddb;
    }

    static CampgroundRepository campgrounds() {
        if (instance.campgrounds == null) {
            instance.campgrounds = new DynamoCampgroundRepository(dynamo());
        }
        return instance.campgrounds;
    }

    static CampsiteRepository campsites() {
        if (instance.campsites == null) {
            instance.campsites = new DynamoCampsiteRepository(dynamo());
        }
        return instance.campsites;
    }

    static CustomerRepository customers() {
        if (instance.customers == null) {
            instance.customers = new DynamoCustomerRepository(dynamo());
        }
        return instance.customers;
    }

    static OrderRepository orders() {
        if (instance.orders == null) {
            instance.orders = new DynamoOrderRepository(dynamo());
        }
        return instance.orders;
    }

    static ReservationRepository reservations() {
        if (instance.reservations == null) {
            instance.reservations = new DynamoReservationRepository(dynamo());
        }
        return instance.reservations;
    }

    static AvailabilityService availabilityService() {
        if (instance.availabilityService == null) {
            instance.availabilityService = new AvailabilityService(null, campsites(), reservations());
        }
        return instance.availabilityService;
    }

    static CampgroundComponent campgroundComponent() {
        if (instance.campgroundComponent == null) {
            instance.campgroundComponent = new CampgroundComponentImpl(campgrounds());
        }
        return instance.campgroundComponent;
    }

    static CampsiteComponent campsiteComponent() {
        if (instance.campsiteComponent == null) {
            instance.campsiteComponent = new CampsiteComponentImpl(campsites());
        }
        return instance.campsiteComponent;
    }

    static CustomerComponent customerComponent() {
        if (instance.customerComponent == null) {
            instance.customerComponent = new CustomerComponentImpl(customers());
        }
        return instance.customerComponent;
    }

    static OrderComponent orderComponent() {
        if (instance.orderComponent == null) {
            instance.orderComponent = new OrderComponentImpl(customers(), orders(), campsites(), reservations());
        }
        return instance.orderComponent;
    }
}
