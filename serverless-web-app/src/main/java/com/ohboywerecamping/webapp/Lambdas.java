package com.ohboywerecamping.webapp;

import com.ohboywerecamping.webapp.availability.ReadAvailabilityLambda;
import com.ohboywerecamping.webapp.order.CreateOrderLambda;
import com.ohboywerecamping.webapp.order.ReadOrderLambda;
import com.ohboywerecamping.webapp.order.ReadOrderListLambda;
import com.ohboywerecamping.webapp.util.JsonUtils;

public final class Lambdas {
    public static class LiveReadAvailabilityLambda extends ReadAvailabilityLambda {
        public LiveReadAvailabilityLambda() {
            super(Singletons.availabilityService());
        }
    }

    public static class LiveCreateOrderLambda extends CreateOrderLambda {
        public LiveCreateOrderLambda() {
            super(JsonUtils.jackson(), Singletons.orderComponent(), Singletons.customerComponent());
        }
    }

    public static class LiveReadOrderLambda extends ReadOrderLambda {
        public LiveReadOrderLambda() {
            super(Singletons.orderComponent(), Singletons.customerComponent());
        }
    }

    public static class LiveReadOrderListLambda extends ReadOrderListLambda {
        public LiveReadOrderListLambda() {
            super(Singletons.orderComponent(), Singletons.customerComponent());
        }
    }

    private Lambdas() {
        throw new UnsupportedOperationException("cannot instantiate " + getClass());
    }
}
