package com.ohboywerecamping.domain;

import java.util.List;

public class CampsiteAvailability {
    private long id;
    private List<DateAvailability> availability;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public List<DateAvailability> getAvailability() {
        return availability;
    }

    public void setAvailability(final List<DateAvailability> availability) {
        this.availability = availability;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CampsiteAvailability obj;

        private Builder() {
            this.obj = new CampsiteAvailability();
        }

        public Builder withId(final long id) {
            obj.id = id;
            return this;
        }

        public Builder withAvailability(final List<DateAvailability> availability) {
            obj.availability = availability;
            return this;
        }

        public CampsiteAvailability build() {
            return obj;
        }
    }
}
