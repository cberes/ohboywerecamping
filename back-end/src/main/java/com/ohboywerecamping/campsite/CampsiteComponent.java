package com.ohboywerecamping.campsite;

import java.util.List;
import java.util.Optional;

import com.ohboywerecamping.domain.Campsite;

public interface CampsiteComponent {
    List<Campsite> findCampsitesByCampgroundId(String campgroundId);

    List<Campsite> findCampsitesByAreaId(String areaId);

    Optional<Campsite> findCampsiteById(String id);

    String save(Campsite campsite);
}
