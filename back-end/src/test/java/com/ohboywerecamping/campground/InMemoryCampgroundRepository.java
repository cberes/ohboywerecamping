package com.ohboywerecamping.campground;

import java.util.List;

import com.ohboywerecamping.domain.Campground;
import com.ohboywerecamping.test.InMemoryRepository;

import static java.util.Collections.emptyList;

public class InMemoryCampgroundRepository
        extends InMemoryRepository<Campground, String> implements CampgroundRepository {
    public InMemoryCampgroundRepository() {
        this(List.of(
                buildCampground("Campground X",
                "Campground X is a really fun place. It lets you get away from the toil of "
                        + "everyday life. The toil of everyday life where too many things at work "
                        + "are misspelled. Located in the deepest, darkest depths of scenic Lake "
                        + "Ontario."),
                buildCampground("Campground Y",
                "We have the best campsites at Campground Y. Reallly tremendous campsites. "
                        + "And it's surrounded by a 'uge wall that our neighbors paid for. It's "
                        + "completely surrounded. A tremendous, impenetrable wall.")));
    }

    private InMemoryCampgroundRepository(final List<Campground> campgrounds) {
        super(Object::toString, Campground::setId, campgrounds);
    }

    private static Campground buildCampground(final String name, final String description) {
        final Campground item = new Campground();
        item.setName(name);
        item.setDescription(description);
        return item;
    }

    public static InMemoryCampgroundRepository empty() {
        return new InMemoryCampgroundRepository(emptyList());
    }
}
