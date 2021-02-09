package com.github.mono83.commons.reference;

import java.time.Instant;

public interface InstantTimeReference {
    /**
     * @return Entity time.
     */
    Instant getTime();

    /**
     * @return Entity time in epoch seconds.
     */
    default long getTimeUnixSeconds() {
        return getTime().getEpochSecond();
    }
}
