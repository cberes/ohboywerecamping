package com.ohboywerecamping.domain;

public enum Availability {
    RESERVED, AVAILABLE, FIRST_COME_FIRST_SERVE, IN_PERSON_ONLY;

    public boolean isReservationAllowed() {
        return this == AVAILABLE;
    }
}
