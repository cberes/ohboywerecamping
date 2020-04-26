package com.ohboywerecamping.reservation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import com.ohboywerecamping.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findByOrder(String orderId);

    List<Reservation> findByCampsiteBetweenDates(String campsiteId, LocalDate start, LocalDate end);

    List<Reservation> findByCampsitesBetweenDates(Collection<String> campsiteIds, LocalDate start, LocalDate end);

    void store(Reservation reservation);

    void delete(Reservation reservation);
}
