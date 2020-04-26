package com.ohboywerecamping.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class Order {
    private String id;

    private Customer customer;

    private Payment payment;

    private ZonedDateTime created = ZonedDateTime.now(ZoneOffset.UTC);

    private List<Reservation> reservations;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(final Payment payment) {
        this.payment = payment;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(final ZonedDateTime created) {
        if (created == null) {
            throw new IllegalArgumentException("cannot be null");
        }
        this.created = created;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(final List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
