package org.axonframework.domain.annotation.model;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class ContactId {

    private final String id;

    public ContactId() {
        this(UUID.randomUUID().toString());
    }

    public ContactId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactId contactId = (ContactId) o;

        if (!id.equals(contactId.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
