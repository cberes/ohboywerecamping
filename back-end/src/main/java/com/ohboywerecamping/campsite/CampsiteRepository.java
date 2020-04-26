package com.ohboywerecamping.campsite;

import java.util.List;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Campsite;

public interface CampsiteRepository extends Repository<Campsite, String> {
    List<Campsite> findByCampgroundId(String id);

    List<Campsite> findByAreaId(String id);
}
