package com.github.mono83.commons.events.simple;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ClassBasedAsyncEventDispatcher extends Abstract {
    private final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    private final Executor executor;

    public ClassBasedAsyncEventDispatcher(final Executor executor) {
        this.executor = executor == null
                ? Runnable::run
                : executor;

        Thread thread = new Thread(this::read, "ClassBasedAsyncEventDispatcher");
        thread.setDaemon(true);
        thread.start();
    }

    public ClassBasedAsyncEventDispatcher() {
        this(null);
    }

    private void read() {
        boolean running = true;
        do {
            try {
                final Object taken = queue.take();
                executor.execute(() -> dispatch(taken));
            } catch (InterruptedException e) {
                running = false;
            }
        } while (running);
    }

    @Override
    public void accept(final Object event) {
        if (event != null) {
            queue.add(event);
        }
    }
}
