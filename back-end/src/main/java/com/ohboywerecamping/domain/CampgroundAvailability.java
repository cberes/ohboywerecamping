package com.ohboywerecamping.domain;

import java.util.List;

public class CampgroundAvailability {
    private String campgroundId;
    private List<CampsiteAvailability> campsites;

    public String getCampgroundId() {
        return campgroundId;
    }

    public void setCampgroundId(final String campgroundId) {
        this.campgroundId = campgroundId;
    }

    public List<CampsiteAvailability> getCampsites() {
        return campsites;
    }

    public void setCampsites(final List<CampsiteAvailability> campsites) {
        this.campsites = campsites;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CampgroundAvailability obj;

        private Builder() {
            this.obj = new CampgroundAvailability();
        }

        public Builder withCampgroundId(final String campgroundId) {
            obj.campgroundId = campgroundId;
            return this;
        }

        public Builder withCampsites(final List<CampsiteAvailability> campsites) {
            obj.campsites = campsites;
            return this;
        }

        public CampgroundAvailability build() {
            return obj;
        }
    }
}
