package com.ohboywerecamping.webapp.campsite;

import java.util.List;
import java.util.Optional;

import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.domain.Campsite;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class FakeCampsiteRepository implements CampsiteRepository {
    @Override
    public List<Campsite> findByCampgroundId(final String id) {
        switch (id) {
            case "1":
                return range(1, 3).mapToObj(i -> {
                    Campsite campsite = new Campsite();
                    campsite.setId(Integer.toString(i));
                    return campsite;
                }).collect(toList());
            case "2":
                return range(3, 5).mapToObj(i -> {
                    Campsite campsite = new Campsite();
                    campsite.setId(Integer.toString(i));
                    return campsite;
                }).collect(toList());
            default:
                return emptyList();
        }
    }

    @Override
    public List<Campsite> findByAreaId(final String id) {
        return emptyList();
    }

    @Override
    public List<Campsite> findAll() {
        return emptyList();
    }

    @Override
    public Optional<Campsite> findById(final String id) {
        return Optional.empty();
    }

    @Override
    public String save(final Campsite value) {
        return null;
    }

    @Override
    public void delete(final Campsite value) {
    }
}
