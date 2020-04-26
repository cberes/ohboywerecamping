package com.ohboywerecamping.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

public class Customer {
    private String id;

    private boolean active = true;

    private ZonedDateTime joined = ZonedDateTime.now(ZoneOffset.UTC);

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String data;

    private Set<Reservation> reservations;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public ZonedDateTime getJoined() {
        return joined;
    }

    public void setJoined(final ZonedDateTime joined) {
        if (joined == null) {
            throw new IllegalArgumentException("cannot be null");
        }
        this.joined = joined;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(final Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
