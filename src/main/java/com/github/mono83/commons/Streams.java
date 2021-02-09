package com.github.mono83.commons;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Various stream helpers.
 */
public class Streams {
    private Streams() {
    }

    /**
     * Obtains non-parallel stream from given iterable source.
     *
     * @param source Source for stream.
     * @return Stream.
     */
    public static <T> Stream<T> toStream(final Iterable<T> source) {
        return source == null
                ? Stream.empty()
                : StreamSupport.stream(source.spliterator(), false);
    }

    /**
     * Retrieves all data from collection as array.
     *
     * @param source Source data.
     * @return Array with values.
     */
    public static long[] toArray(final Collection<Long> source) {
        return source == null
                ? new long[0]
                : source.stream().mapToLong($ -> $).toArray();
    }

    /**
     * Retrieves all data from collection as array.
     *
     * @param source Source data.
     * @return Array with values.
     */
    public static long[] toArray(final Iterable<Long> source) {
        return source == null
                ? new long[0]
                : toStream(source).mapToLong($ -> $).toArray();
    }
}
