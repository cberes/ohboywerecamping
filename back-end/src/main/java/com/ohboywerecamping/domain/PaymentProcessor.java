package com.ohboywerecamping.domain;

import java.util.Set;

public class PaymentProcessor {
    private long id;

    private boolean active = true;

    private String name;

    private Set<Payment> payments;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(final Set<Payment> payments) {
        this.payments = payments;
    }
}
