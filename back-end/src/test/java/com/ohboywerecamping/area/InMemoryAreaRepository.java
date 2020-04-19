package com.ohboywerecamping.area;

import java.util.List;

import com.ohboywerecamping.domain.Area;
import com.ohboywerecamping.domain.Campground;
import com.ohboywerecamping.test.InMemoryRepository;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class InMemoryAreaRepository extends InMemoryRepository<Area, Long> implements AreaRepository {
    private InMemoryAreaRepository() {
        super(i -> (long) i, Area::setId, emptyList());
    }

    public InMemoryAreaRepository(final List<Campground> campgrounds) {
        super(i -> (long) i, Area::setId, campgrounds.stream()
                .map(campground -> buildArea(campground, "Entire campground"))
                .collect(toList()));
    }

    @Override
    public List<Area> findByCampgroundId(final long id) {
        return findAll().stream()
                .filter(it -> it.getCampground().getId() == id)
                .collect(toList());
    }

    private static Area buildArea(final Campground campground, final String description) {
        final Area item = new Area();
        item.setCampground(campground);
        item.setName(campground.getName());
        item.setDescription(description);
        return item;
    }

    public static InMemoryAreaRepository empty() {
        return new InMemoryAreaRepository();
    }
}
