package com.github.mono83.commons.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Defines baseline cache functionality.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public interface BaselineCache<K, V> {
    /**
     * Returns value from cache, associated with key.
     *
     * @param key Cache key.
     * @return Cache value, optional.
     */
    Optional<V> get(K key);

    /**
     * Returns value from cache.
     * If there is no values in cache, will generate it using provided generator and store to cache.
     *
     * @param key       Cache key.
     * @param generator Function to generate value.
     * @return Value from cache.
     */
    V computeIfAbsent(K key, Function<K, V> generator);

    /**
     * Returns value from cache.
     * If there is no values in cache, will generate it using provided generator and store to cache.
     *
     * @param key       Cache key.
     * @param generator Function to generate value.
     * @return Value from cache.
     */
    V computeIfAbsent(K key, Supplier<V> generator);

    /**
     * Returns multiple values from cache.
     * If there is no values in cache, will generate it using provided generator and store to cache.
     *
     * @param keys      Cache keys.
     * @param generator Function to generate values.
     * @return Values from cache.
     */
    Map<K, V> computeIfAbsent(Iterable<K> keys, Function<Set<K>, Map<K, V>> generator);

    /**
     * Returns multiple values from cache.
     * If there is no values in cache, will generate it using provided generator and store to cache.
     *
     * @param keys      Cache keys.
     * @param generator Function to generate values.
     * @return Values from cache.
     */
    default Map<K, V> computeIfAbsent(K[] keys, Function<Set<K>, Map<K, V>> generator) {
        return computeIfAbsent(List.of(keys), generator);
    }
}
