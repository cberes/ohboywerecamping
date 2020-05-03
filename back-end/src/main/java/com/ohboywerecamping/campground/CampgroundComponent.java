package com.ohboywerecamping.campground;

import java.util.Optional;

import com.ohboywerecamping.domain.Campground;

public interface CampgroundComponent {
    Optional<Campground> findCampgroundById(String id);

    Optional<Campground> findCampgroundByReferer(String referer);

    String save(Campground campground);
}
