package com.ohboywerecamping.campground;

import java.util.Optional;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Campground;

public interface CampgroundRepository extends Repository<Campground, String> {
    Optional<Campground> findByHostname(final String hostname);
}
