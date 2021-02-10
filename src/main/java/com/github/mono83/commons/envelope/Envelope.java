package com.github.mono83.commons.envelope;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public interface Envelope {
    /**
     * @return Response code.
     */
    Optional<Integer> getCode();

    /**
     * @return Contents size.
     */
    Optional<Integer> getSize();

    /**
     * @return Envelope type (i.e. HTTP method)
     */
    Optional<String> getType();

    /**
     * @return Target address.
     */
    Optional<String> getAddress();

    /**
     * @return Headers map.
     */
    Map<String, Object> getHeaders();

    /**
     * @return Envelope contents.
     */
    InputStream getData();

    /**
     * @return Envelope contents as byte array.
     */
    default byte[] getDataBytes() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            getData().transferTo(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Envelope contents as string.
     */
    default String getDataString(final Charset charset) {
        return new String(getDataBytes(), charset);
    }

    /**
     * @return Envelope contents as string.
     */
    default String getDataString() {
        return getDataString(StandardCharsets.UTF_8);
    }
}
