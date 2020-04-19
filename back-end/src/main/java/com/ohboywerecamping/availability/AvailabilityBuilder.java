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
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
                                            final long campgroundId,
                                            final List<Long> campsiteIds) {
        final Map<Long, List<Reservation>> reservationsByCampsiteId = getReservationsByCampsiteId(reservations);
        final List<CampsiteAvailability> availability = buildAvailability(reservationsByCampsiteId, campsiteIds);
        return CampgroundAvailability.builder()
                .withCampgroundId(campgroundId)
                .withCampsites(availability)
                .build();
    }

    private Map<Long, List<Reservation>> getReservationsByCampsiteId(final ReservationRepository reservations) {
        // TODO query for reservations by campsite and date
        return reservations.findAll().stream()
                .collect(groupingBy(res -> res.getCampsite().getId()));
    }

    private List<CampsiteAvailability> buildAvailability(final Map<Long, List<Reservation>> reservationsByCampsiteId,
                                                         final List<Long> campsiteIds) {
        return campsiteIds.stream()
                .map(id -> buildAvailability(reservationsByCampsiteId.getOrDefault(id, emptyList()), id))
                .collect(toList());
    }

    private CampsiteAvailability buildAvailability(final List<Reservation> reservations, final long campsiteId) {
        final List<DateAvailability> dates = getAvailabilityDates(reservations);
        return CampsiteAvailability.builder()
                .withId(campsiteId)
                .withAvailability(dates)
                .build();
    }

    private List<DateAvailability> getAvailabilityDates(final List<Reservation> reservations) {
        // XXX: the mapping function is stateful; it will modify the reservations list
        reservations.sort(Comparator.comparing(Reservation::getStarting));
        return start.datesUntil(end)
                .map(date -> getAvailability(reservations, date))
                .collect(toList());
    }

    private DateAvailability getAvailability(final List<Reservation> reservations, final LocalDate date) {
        final Optional<Reservation> reservation = findNextReservation(reservations, date);
        final Availability availability = reservation.isPresent() ? Availability.RESERVED : statusFunc.apply(date);
        return DateAvailability.builder()
                .withDate(date)
                .withStatus(availability)
                .build();
    }

    private static Optional<Reservation> findNextReservation(final List<Reservation> reservations,
                                                             final LocalDate date) {
        removePastDates(reservations, date);
        return reservations.isEmpty() || date.isBefore(reservations.get(0).getStarting())
                ? Optional.empty() : Optional.of(reservations.get(0));
    }

    private static void removePastDates(final List<Reservation> reservations, final LocalDate date) {
        while (!reservations.isEmpty() && isBefore(reservations.get(0), date)) {
            reservations.remove(0);
        }
    }

    private static boolean isBefore(final Reservation reservation, final LocalDate date) {
        return reservation.getEnding().isBefore(date) || reservation.getEnding().isEqual(date);
    }
}
