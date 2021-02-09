package com.github.mono83.commons.reference;

import java.util.*;

public class LongIdReferencedRepositoryMapImpl<T extends LongIdReference> implements LongIdReferencedRepository<T> {
    private final HashMap<Long, T> values;

    public LongIdReferencedRepositoryMapImpl(final Map<Long, T> source) {
        this.values = new HashMap<>(source);
    }

    @Override
    public Collection<T> get(final long[] ids) {
        if (ids == null || ids.length == 0) {
            return Collections.emptyList();
        }

        ArrayList<T> response = new ArrayList<>(ids.length);
        for (long id : ids) {
            T value = values.get(id);
            if (value != null) {
                response.add(value);
            }
        }
        return response;
    }
}
