package com.github.mono83.commons.reference;

public interface LongIdReference {
    /**
     * @return Identifier of entity
     */
    long getId();

    /**
     * Base implementation of interface.
     */
    class Base implements LongIdReference {
        private final long id;

        public Base(final long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(id);
        }

        @Override
        public String toString() {
            return "{" + getClass().getSimpleName() + " " + getId() + "}";
        }
    }
}
