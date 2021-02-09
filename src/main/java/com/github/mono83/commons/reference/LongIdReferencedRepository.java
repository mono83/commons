package com.github.mono83.commons.reference;

import com.github.mono83.commons.Streams;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface LongIdReferencedRepository<T extends LongIdReference> {
    /**
     * Returns entities matched by identifiers.
     *
     * @param ids Identifiers.
     * @return Found entities.
     */
    Collection<T> get(long[] ids);

    /**
     * Returns entities matched by identifiers.
     *
     * @param ids Identifiers.
     * @return Found entities.
     */
    default Collection<T> get(final Iterable<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }

        return this.get(Streams.toArray(ids));
    }

    /**
     * Returns single entity by identifier.
     *
     * @param id Identifier.
     * @return Found entity.
     */
    default Optional<T> get(final long id) {
        Collection<T> collection = this.get(new long[]{id});
        if (collection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(collection.iterator().next());
    }

    /**
     * Returns entities matched by identifiers.
     *
     * @param ids Identifiers.
     * @return Found entities.
     */
    default Map<Long, T> getMap(final long[] ids) {
        return this.get(ids).stream().collect(Collectors.toMap(
                LongIdReference::getId,
                Function.identity()
        ));
    }

    /**
     * Returns entities matched by identifiers.
     *
     * @param ids Identifiers.
     * @return Found entities.
     */
    default Map<Long, T> getMap(final Iterable<Long> ids) {
        return this.getMap(Streams.toArray(ids));
    }
}
