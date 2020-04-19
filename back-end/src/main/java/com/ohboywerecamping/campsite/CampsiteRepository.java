package com.ohboywerecamping.campsite;

import java.util.List;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Campsite;

public interface CampsiteRepository extends Repository<Campsite, Long> {
    List<Campsite> findByCampgroundId(long id);

    List<Campsite> findByAreaId(long id);
}
