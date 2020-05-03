package com.ohboywerecamping.campground;

import java.net.URI;
import java.util.Optional;

import com.ohboywerecamping.domain.Campground;

public class CampgroundComponentImpl implements CampgroundComponent {
    private final CampgroundRepository campgrounds;

    public CampgroundComponentImpl(final CampgroundRepository campgrounds) {
        this.campgrounds = campgrounds;
    }

    @Override
    public Optional<Campground> findCampgroundById(final String id) {
        return campgrounds.findById(id);
    }

    @Override
    public Optional<Campground> findCampgroundByReferer(final String referer) {
        return Optional.ofNullable(referer)
                .map(URI::create)
                .map(URI::getHost)
                .flatMap(campgrounds::findByHostname);
    }

    @Override
    public String save(final Campground campground) {
        return campgrounds.save(campground);
    }
}
