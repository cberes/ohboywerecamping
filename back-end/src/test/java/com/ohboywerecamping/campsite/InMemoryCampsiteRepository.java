package com.ohboywerecamping.campsite;

import java.util.List;
import java.util.stream.Stream;

import com.ohboywerecamping.domain.*;
import com.ohboywerecamping.test.InMemoryRepository;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class InMemoryCampsiteRepository extends InMemoryRepository<Campsite, Long> implements CampsiteRepository {
    private InMemoryCampsiteRepository() {
        super(i -> (long) i, Campsite::setId, emptyList());
    }

    public InMemoryCampsiteRepository(final List<Area> areas) {
        super(i -> (long) i, Campsite::setId, range(0, areas.size()).boxed()
                .flatMap(i -> Stream.of(campsite1(areas.get(i), i), campsite2(areas.get(i), i)))
                .collect(toList()));
    }

    @Override
    public List<Campsite> findByCampgroundId(final long id) {
        return findAll().stream()
                .filter(it -> it.getCampground().getId() == id)
                .collect(toList());
    }

    @Override
    public List<Campsite> findByAreaId(final long id) {
        return findAll().stream()
                .filter(it -> it.getArea().getId() == id)
                .collect(toList());
    }

    private static Campsite campsite1(final Area area, final int offset) {
        return new Campsite.Builder()
                .withCampground(area.getCampground())
                .withArea(area)
                .withName("Site " + (char) ('A' + offset))
                .withDescription("Located at the top of Mt. Very High, where the air is very thin.")
                .withNotes(null)
                .withType(SiteType.CAMPSITE)
                .withAccess(Access.DRIVE_IN)
                .withSize(20)
                .withMaxOccupancy(6)
                .withMaxVehicles(1)
                .withPetsAllowed(2)
                .withElectric(Electric.UNKNOWN_AMP)
                .withWater(Water.YES)
                .withSewer(Sewer.NO)
                .build();
    }

    private static Campsite campsite2(final Area area, final int offset) {
        return new Campsite.Builder()
                .withCampground(area.getCampground())
                .withArea(area)
                .withName("Site " + (char) ('A' + offset + 1))
                .withDescription("Offers beautiful views of the lakeshore cliffs. Stay dry!")
                .withNotes("Infested by ants")
                .withType(SiteType.CAMPSITE)
                .withAccess(Access.DRIVE_IN)
                .withSize(40)
                .withMaxOccupancy(6)
                .withMaxVehicles(2)
                .withPetsAllowed(2)
                .withElectric(Electric.UNKNOWN_AMP)
                .withWater(Water.YES)
                .withSewer(Sewer.NO)
                .build();
    }

    public static InMemoryCampsiteRepository empty() {
        return new InMemoryCampsiteRepository();
    }
}
