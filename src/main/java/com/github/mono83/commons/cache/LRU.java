package com.github.mono83.commons.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO unit
public class LRU<K, V> implements Cache<K, V> {
    private final Object lock = new Object();
    private final boolean strict;
    private final Predicate<Node<V>> isValid;
    private final LinkedHashMap<K, Node<V>> data;

    /**
     * Created new LRU instance that will expire entities.
     *
     * @param ttl    Entity TTL.
     * @param size   Cache size.
     * @param strict If true, all value generator will lock whole cache until value is generated.
     * @return Cache instance.
     */
    public static <K, V> Cache<K, V> TTL(final Duration ttl, final int size, final boolean strict) {
        Objects.requireNonNull(ttl, "ttl");
        return new LRU<K, V>(
                null,
                (v, created) -> created.plus(ttl).isAfter(Instant.now()),
                size,
                strict
        );
    }

    /**
     * Main constructor.
     *
     * @param source  Optional initial cache data.
     * @param decider Predicate that can decide is cache entry alive or not.
     * @param size    Cache size.
     * @param strict  If true, all value generator will lock whole cache until value is generated.
     */
    public LRU(
            final Map<K, V> source,
            final BiPredicate<V, Instant> decider,
            final int size,
            final boolean strict
    ) {
        Objects.requireNonNull(decider, "decider");
        if (size < 1) {
            throw new IllegalArgumentException("Illegal LRU size " + size);
        }
        this.isValid = $ -> decider.test($.value, $.created);
        this.strict = strict;
        this.data = new LinkedHashMap<>(size + 1, 1.0f, true) {
            // (an anonymous inner class)
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(final Map.Entry<K, Node<V>> eldest) {
                return size() > size;
            }
        };
        if (source != null && source.size() > 0) {
            for (Map.Entry<K, V> e : source.entrySet()) {
                this.data.put(e.getKey(), new Node<>(e.getValue()));
            }
        }
    }

    @Override
    public Optional<V> get(final K key) {
        synchronized (lock) {
            return Optional.ofNullable(data.get(key)).filter(isValid).map(Node::getValue);
        }
    }

    @Override
    public Optional<V> put(final K key, final V value) {
        synchronized (lock) {
            return Optional.ofNullable(data.put(key, new Node<>(value))).filter(isValid).map(Node::getValue);
        }
    }

    @Override
    public Optional<V> invalidate(final K key) {
        synchronized (lock) {
            return Optional.ofNullable(data.remove(key)).filter(isValid).map(Node::getValue);
        }
    }

    @Override
    public V computeIfAbsent(final K key, final Function<K, V> generator) {
        if (strict) {
            // Strict mode - full lock
            synchronized (lock) {
                return data.compute(key, (k, previous) -> {
                    if (previous != null && isValid.test(previous)) {
                        return previous;
                    }

                    return new Node<>(generator.apply(k));
                }).getValue();
            }
        } else {
            return get(key).orElseGet(() -> {
                V generated = generator.apply(key);
                put(key, generated);
                return generated;
            });
        }
    }

    @Override
    public V computeIfAbsent(final K key, final Supplier<V> generator) {
        if (strict) {
            // Strict mode - full lock
            synchronized (lock) {
                return data.compute(key, (k, previous) -> {
                    if (previous != null && isValid.test(previous)) {
                        return previous;
                    }

                    return new Node<>(generator.get());
                }).getValue();
            }
        } else {
            return get(key).orElseGet(() -> {
                V generated = generator.get();
                put(key, generated);
                return generated;
            });
        }
    }

    @Override
    public Map<K, V> computeIfAbsent(
            final Iterable<K> keys,
            final Function<Set<K>, Map<K, V>> generator
    ) {
        HashSet<K> keySet = new HashSet<>();
        for (K key : keys) {
            keySet.add(key);
        }
        if (keySet.size() == 0) {
            return Collections.emptyMap();
        }

        HashMap<K, V> result = new HashMap<>(keySet.size());
        HashSet<K> missing = new HashSet<>();
        if (strict) {
            synchronized (lock) {
                // Reading present
                for (K key : keySet) {
                    Node<V> v = this.data.get(key);
                    if (v == null || !isValid.test(v)) {
                        missing.add(key);
                    } else {
                        result.put(key, v.value);
                    }
                }
                // Generating missing
                if (missing.size() > 0) {
                    for (Map.Entry<K, V> e : generator.apply(missing).entrySet()) {
                        this.data.put(e.getKey(), new Node<>(e.getValue()));
                        result.put(e.getKey(), e.getValue());
                    }
                }
            }
        } else {
            // Reading present
            synchronized (lock) {
                for (K key : keySet) {
                    Node<V> v = this.data.get(key);
                    if (v == null || !isValid.test(v)) {
                        missing.add(key);
                    } else {
                        result.put(key, v.value);
                    }
                }
            }
            // Generating missing
            if (missing.size() > 0) {
                Map<K, V> generated = generator.apply(missing);
                result.putAll(generated);
                synchronized (lock) {
                    for (Map.Entry<K, V> e : generated.entrySet()) {
                        this.data.put(e.getKey(), new Node<>(e.getValue()));
                    }
                }
            }
        }
        return result;
    }

    private static class Node<V> {
        private final V value;
        private final Instant created;

        private Node(final V value) {
            this.value = Objects.requireNonNull(value, "value");
            this.created = Instant.now();
        }

        public V getValue() {
            return value;
        }
    }
}
