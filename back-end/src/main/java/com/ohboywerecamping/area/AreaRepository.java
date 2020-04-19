package com.ohboywerecamping.area;

import java.util.List;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Area;

public interface AreaRepository extends Repository<Area, Long> {
    List<Area> findByCampgroundId(long id);
}
