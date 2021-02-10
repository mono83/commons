package com.github.mono83.commons.events.simple;

public class ClassBasedEventDispatcher extends Abstract {
    @Override
    public void accept(final Object event) {
        dispatch(event);
    }
}
