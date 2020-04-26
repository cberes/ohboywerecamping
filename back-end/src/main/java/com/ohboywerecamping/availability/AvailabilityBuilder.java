package com.ohboywerecamping.availability;

import com.ohboywerecamping.reservation.ReservationRepository;
import com.ohboywerecamping.domain.CampgroundAvailability;
import com.ohboywerecamping.domain.CampsiteAvailability;
import com.ohboywerecamping.domain.DateAvailability;
import com.ohboywerecamping.domain.Reservation;
import com.ohboywerecamping.domain.Availability;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

class AvailabilityBuilder {
    private final LocalDate start;
    private final LocalDate end;
    private final Function<LocalDate, Availability> statusFunc;

    AvailabilityBuilder(final LocalDate start, final LocalDate end,
                        final Function<LocalDate, Availability> statusFunc) {
        this.start = start;
        this.end = end;
        this.statusFunc = statusFunc;
    }

    CampgroundAvailability findAvailability(final ReservationRepository reservations,
                                            final String campgroundId,
                                            final List<String> campsiteIds) {
        final var reservationsByCampsiteId = reservations.findByCampsitesBetweenDates(campsiteIds, start, end)
                .stream().collect(groupingBy(res -> res.getCampsite().getId()));
        final List<CampsiteAvailability> availability = buildAvailability(reservationsByCampsiteId, campsiteIds);
        return CampgroundAvailability.builder()
                .withCampgroundId(campgroundId)
                .withCampsites(availability)
                .build();
    }

    private List<CampsiteAvailability> buildAvailability(final Map<String, List<Reservation>> reservationsByCampsiteId,
                                                         final List<String> campsiteIds) {
        return campsiteIds.stream()
                .map(id -> buildAvailability(reservationsByCampsiteId.getOrDefault(id, emptyList()), id))
                .collect(toList());
    }

    private CampsiteAvailability buildAvailability(final List<Reservation> reservations, final String campsiteId) {
        final List<DateAvailability> dates = getAvailabilityDates(reservations);
        return CampsiteAvailability.builder()
                .withId(campsiteId)
                .withAvailability(dates)
                .build();
    }

    private List<DateAvailability> getAvailabilityDates(final List<Reservation> reservations) {
        final Map<LocalDate, Reservation> reservationsByDate = reservations.stream()
                .collect(toMap(Reservation::getDate, Function.identity()));
        return start.datesUntil(end)
                .map(date -> getAvailability(reservationsByDate.get(date), date))
                .collect(toList());
    }

    private DateAvailability getAvailability(final Reservation reservation, final LocalDate date) {
        final Availability availability = Optional.ofNullable(reservation)
                .map(res -> Availability.RESERVED)
                .orElse(statusFunc.apply(date));
        return DateAvailability.builder()
                .withDate(date)
                .withStatus(availability)
                .build();
    }
}
