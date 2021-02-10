package com.github.mono83.commons.events.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

abstract class Abstract implements Consumer<Object> {
    protected final ArrayList<Consumer<Object>> empty = new ArrayList<>();
    protected final HashMap<Class<?>, ArrayList<Consumer<Object>>> consumers = new HashMap<>();

    /**
     * Registers new event consumer.
     * This method in not thread safe and all registrations should be
     * made during dependency injection phase.
     *
     * @param clazz    Event class to listen.
     * @param consumer Event consumer.
     */
    public void register(final Class<?> clazz, final Consumer<Object> consumer) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(consumer, "consumer");
        this.consumers.computeIfAbsent(clazz, $ -> new ArrayList<>()).add(consumer);
    }

    /**
     * Sends event.
     *
     * @param event Event to send.
     */
    protected void dispatch(final Object event) {
        if (event == null) {
            return;
        }
        for (Consumer<Object> consumer : this.consumers.getOrDefault(event.getClass(), empty)) {
            consumer.accept(event);
        }
        for (Consumer<Object> consumer : this.consumers.getOrDefault(Object.class, empty)) {
            consumer.accept(event);
        }
    }
}
