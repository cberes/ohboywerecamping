package com.ohboywerecamping.domain;

import java.time.LocalDate;

public class Reservation {
    private String id;

    private Order order;

    private Campsite campsite;

    private LocalDate date;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(final Campsite campsite) {
        this.campsite = campsite;
    }

    /**
     * Returns the date when the reservation is valid (usually this night and the following morning).
     * @return start date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date when the reservation is valid (usually this night and the following morning).
     * @param date start date
     */
    public void setDate(final LocalDate date) {
        this.date = date;
    }
}
