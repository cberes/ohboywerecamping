package com.ohboywerecamping.domain;

import java.util.Set;

public class Campground {
    private String id;

    private boolean active = true;

    private String hostname;

    private String name;

    private String description;

    private Set<Area> areas;

    private Set<Campsite> campsites;

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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
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

    public Set<Area> getAreas() {
        return areas;
    }

    public void setAreas(final Set<Area> areas) {
        this.areas = areas;
    }

    public Set<Campsite> getCampsites() {
        return campsites;
    }

    public void setCampsites(final Set<Campsite> campsites) {
        this.campsites = campsites;
    }
}
