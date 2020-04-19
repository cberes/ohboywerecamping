package com.ohboywerecamping.domain;

import java.util.Set;

public class Area {
    private long id;

    private Campground campground;

    private boolean active = true;

    private String name;

    private String description;

    private Set<Campsite> campsites;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Campground getCampground() {
        return campground;
    }

    public void setCampground(final Campground campground) {
        this.campground = campground;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<Campsite> getCampsites() {
        return campsites;
    }

    public void setCampsites(final Set<Campsite> campsites) {
        this.campsites = campsites;
    }
}
