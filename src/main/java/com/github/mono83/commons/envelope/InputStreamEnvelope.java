package com.github.mono83.commons.envelope;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InputStreamEnvelope extends BaseEnvelope {
    private final Integer size;
    private final InputStream stream;

    public InputStreamEnvelope(
            final Integer code,
            final String type,
            final String address,
            final Map<String, Object> headers,
            final InputStream stream,
            final Integer size
    ) {
        super(code, type, address, headers);
        this.stream = Objects.requireNonNull(stream, "stream");
        this.size = size == null || size == 0
                ? (stream instanceof ByteArrayInputStream ? ((ByteArrayInputStream) stream).available() : null)
                : size;
    }

    @Override
    public Optional<Integer> getSize() {
        return Optional.ofNullable(size);
    }

    @Override
    public InputStream getData() {
        return stream;
    }
}
