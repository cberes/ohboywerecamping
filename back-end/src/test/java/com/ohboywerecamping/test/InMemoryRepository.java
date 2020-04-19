package com.ohboywerecamping.test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.ohboywerecamping.common.Repository;

import static java.util.stream.IntStream.range;

public class InMemoryRepository<V, K> implements Repository<V, K> {
    private final Map<K, V> elements;

    public InMemoryRepository(final Function<Integer, K> idGenerator,
                              final BiConsumer<V, K> idSetter,
                              final List<V> elements) {
        this(new LinkedHashMap<>());
        range(0, elements.size()).forEach(i -> {
            idSetter.accept(elements.get(i), idGenerator.apply(i));
            this.elements.put(idGenerator.apply(i), elements.get(i));
        });
    }

    public InMemoryRepository(final Map<K, V> elements) {
        this.elements = elements;
    }

    @Override
    public List<V> findAll() {
        return new ArrayList<>(elements.values());
    }

    @Override
    public Optional<V> findById(final K id) {
        return Optional.ofNullable(elements.get(id));
    }
}
