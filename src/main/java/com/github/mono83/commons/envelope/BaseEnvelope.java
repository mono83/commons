package com.github.mono83.commons.envelope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseEnvelope implements Envelope {
    private final Integer code;
    private final String type;
    private final String address;
    private final Map<String, Object> headers;

    protected BaseEnvelope(
            final Integer code,
            final String type,
            final String address,
            final Map<String, Object> headers
    ) {
        this.code = code;
        this.type = type;
        this.address = address;
        this.headers = headers == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(headers));
    }

    @Override
    public Optional<Integer> getCode() {
        return Optional.ofNullable(code);
    }

    @Override
    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }

    @Override
    public Map<String, Object> getHeaders() {
        return headers;
    }

}
