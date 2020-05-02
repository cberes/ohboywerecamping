package com.ohboywerecamping.webapp;

import com.ohboywerecamping.webapp.availability.ReadAvailabilityLambda;
import com.ohboywerecamping.webapp.order.CreateOrderLambda;
import com.ohboywerecamping.webapp.order.ReadOrderLambda;
import com.ohboywerecamping.webapp.order.ReadOrderListLambda;
import com.ohboywerecamping.webapp.util.JsonUtils;

class Lambdas {
    private static final Lambdas instance = new Lambdas();

    private ReadAvailabilityLambda readAvailability;
    private CreateOrderLambda createOrder;
    private ReadOrderLambda readOrder;
    private ReadOrderListLambda readOrderList;

    private Lambdas() {
    }

    static ReadAvailabilityLambda readAvailability() {
        if (instance.readAvailability == null) {
            instance.readAvailability = new ReadAvailabilityLambda(Singletons.availabilityService());
        }
        return instance.readAvailability;
    }

    static CreateOrderLambda createOrder() {
        if (instance.createOrder == null) {
            instance.createOrder = new CreateOrderLambda(JsonUtils.jackson(), Singletons.orderComponent(), Singletons.customerComponent());
        }
        return instance.createOrder;
    }

    static ReadOrderLambda readOrder() {
        if (instance.readOrder == null) {
            instance.readOrder = new ReadOrderLambda(Singletons.orderComponent(), Singletons.customerComponent());
        }
        return instance.readOrder;
    }

    static ReadOrderListLambda readOrderList() {
        if (instance.readOrderList == null) {
            instance.readOrderList = new ReadOrderListLambda(Singletons.orderComponent(), Singletons.customerComponent());
        }
        return instance.readOrderList;
    }
}
