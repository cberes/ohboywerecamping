package com.ohboywerecamping.domain;

import java.time.LocalDate;

public class DateAvailability {
    private LocalDate date;
    private Availability status;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public Availability getStatus() {
        return status;
    }

    public void setStatus(final Availability status) {
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DateAvailability obj;

        private Builder() {
            this.obj = new DateAvailability();
        }

        public Builder withDate(final LocalDate date) {
            obj.date = date;
            return this;
        }

        public Builder withStatus(final Availability status) {
            obj.status = status;
            return this;
        }

        public DateAvailability build() {
            return obj;
        }
    }
}
