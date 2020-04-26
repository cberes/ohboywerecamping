package com.ohboywerecamping.availability;

import com.ohboywerecamping.area.AreaRepository;
import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.reservation.ReservationRepository;
import com.ohboywerecamping.domain.CampgroundAvailability;
import com.ohboywerecamping.domain.Area;
import com.ohboywerecamping.domain.Campground;
import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.domain.Availability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AvailabilityService {
    private static final int MIN_DAYS_TO_RESERVE = 2;
    private static final Availability STATUS_TOO_LATE = Availability.IN_PERSON_ONLY;

    private final AreaRepository areaRepository;
    private final CampsiteRepository campsiteRepository;
    private final ReservationRepository reservationRepository;

    public AvailabilityService(final AreaRepository areaRepository,
                               final CampsiteRepository campsiteRepository,
                               final ReservationRepository reservationRepository) {
        this.areaRepository = areaRepository;
        this.campsiteRepository = campsiteRepository;
        this.reservationRepository = reservationRepository;
    }

    public CampgroundAvailability findByCampgroundId(final String id, final LocalDate start, final LocalDate end) {
        final List<Campsite> campsites = campsiteRepository.findByCampgroundId(id);
        final List<String> campsiteIds = campsiteIds(campsites.stream());
        return findAvailability(id, campsiteIds, start, end);
    }

    private static List<String> campsiteIds(final Stream<Campsite> campsites) {
        return campsites.map(Campsite::getId).collect(toList());
    }

    private CampgroundAvailability findAvailability(final String campgroundId, final List<String> campsiteIds,
                                                    final LocalDate start, final LocalDate end) {
        final LocalDate minDateToReserve = LocalDate.now().plusDays(MIN_DAYS_TO_RESERVE);
        final AvailabilityFunction func = new AvailabilityFunction(minDateToReserve, STATUS_TOO_LATE);
        return new AvailabilityBuilder(start, end, func)
                .findAvailability(reservationRepository, campgroundId, campsiteIds);
    }

    public CampgroundAvailability findByAreaId(final String id, final LocalDate start, final LocalDate end) {
        final String campgroundId = areaRepository.findById(id)
                .map(Area::getCampground)
                .map(Campground::getId)
                .orElse(null);
        final List<Campsite> campsites = campsiteRepository.findByAreaId(id);
        final List<String> campsiteIds = campsiteIds(campsites.stream());
        return findAvailability(campgroundId, campsiteIds, start, end);
    }

    public CampgroundAvailability findByCampsiteId(final String id, final LocalDate start, final LocalDate end) {
        final Optional<Campsite> campsite = campsiteRepository.findById(id);
        final String campgroundId = campsite.map(Campsite::getCampground).map(Campground::getId).orElse(null);
        final List<String> campsiteIds = campsiteIds(campsite.stream());
        return findAvailability(campgroundId, campsiteIds, start, end);
    }
}
