package com.github.mono83.commons.reference;

import java.time.Instant;

public interface InstantCreateTimeReference {
    /**
     * @return Time this entity was created.
     */
    Instant getCreatedAt();

    /**
     * @return Time this entity was modified.
     */
    default Instant getModifiedAt() {
        return getCreatedAt();
    }
}
