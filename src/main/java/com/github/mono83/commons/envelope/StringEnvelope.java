package com.github.mono83.commons.envelope;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class StringEnvelope extends BaseEnvelope {
    private final String data;

    public StringEnvelope(
            final Integer code,
            final String type,
            final String address,
            final Map<String, Object> headers,
            final String data
    ) {
        super(code, type, address, headers);
        this.data = Objects.requireNonNull(data, "data");
    }

    @Override
    public Optional<Integer> getSize() {
        return Optional.of(data.length());
    }

    @Override
    public InputStream getData() {
        return new ByteArrayInputStream(getDataBytes());
    }

    @Override
    public byte[] getDataBytes() {
        return data.getBytes();
    }

    @Override
    public String getDataString() {
        return data;
    }
}
