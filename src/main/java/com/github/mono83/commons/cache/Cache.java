package com.github.mono83.commons.cache;

import java.util.Optional;

public interface Cache<K, V> extends BaselineCache<K, V> {
    /**
     * Places value to cache.
     *
     * @param key   Cache key.
     * @param value Cache value.
     * @return Previous value if present and this operation is possible.
     */
    Optional<V> put(K key, V value);

    /**
     * Invalidates cache entry by key.
     *
     * @param key Cache key.
     * @return Previous value if present and this operation is possible.
     */
    Optional<V> invalidate(K key);
}
