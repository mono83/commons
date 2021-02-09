package com.github.mono83.commons.reference;

import com.github.mono83.commons.cache.BaselineCache;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class LongIdReferencedRepositoryCachingAdapter<T extends LongIdReference> implements LongIdReferencedRepository<T> {
    private final LongIdReferencedRepository<T> source;
    private final BaselineCache<Long, T> cache;

    public LongIdReferencedRepositoryCachingAdapter(final LongIdReferencedRepository<T> source, final BaselineCache<Long, T> cache) {
        this.source = Objects.requireNonNull(source, "source");
        this.cache = Objects.requireNonNull(cache, "cache");
    }

    @Override
    public Collection<T> get(final long[] ids) {
        return cache.computeIfAbsent(
                LongStream.of(ids).boxed().collect(Collectors.toList()),
                source::getMap
        ).values();
    }
}
