package com.ohboywerecamping.webapp.campsite;

import java.util.List;
import java.util.Optional;

import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.domain.Campground;
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
                    campsite.setCampground(new Campground());
                    campsite.getCampground().setId(id);
                    campsite.setId(Integer.toString(i));
                    return campsite;
                }).collect(toList());
            case "2":
                return range(3, 5).mapToObj(i -> {
                    Campsite campsite = new Campsite();
                    campsite.setCampground(new Campground());
                    campsite.getCampground().setId(id);
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
        return range(1, 3).boxed()
                .map(Object::toString)
                .map(this::findByCampgroundId)
                .flatMap(List::stream)
                .collect(toList());
    }

    @Override
    public Optional<Campsite> findById(final String id) {
        return findAll().stream()
                .filter(campsite -> campsite.getId().equals(id))
                .findFirst();
    }

    @Override
    public String save(final Campsite value) {
        return null;
    }

    @Override
    public void delete(final Campsite value) {
    }
}
