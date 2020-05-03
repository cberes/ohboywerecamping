package com.ohboywerecamping.campsite;

import java.util.List;
import java.util.Optional;

import com.ohboywerecamping.domain.Campsite;

public class CampsiteComponentImpl implements CampsiteComponent {
    private final CampsiteRepository campsites;

    public CampsiteComponentImpl(final CampsiteRepository campsites) {
        this.campsites = campsites;
    }

    @Override
    public List<Campsite> findCampsitesByCampgroundId(final String campgroundId) {
        return campsites.findByCampgroundId(campgroundId);
    }

    @Override
    public List<Campsite> findCampsitesByAreaId(final String areaId) {
        return campsites.findByAreaId(areaId);
    }

    @Override
    public Optional<Campsite> findCampsiteById(final String id) {
        return campsites.findById(id);
    }

    @Override
    public String save(final Campsite campsite) {
        return campsites.save(campsite);
    }
}
