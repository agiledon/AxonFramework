package org.axonframework.domain.annotation.model;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class CustomerId {

    private String id;

    public CustomerId() {
        id = UUID.randomUUID().toString();
    }

    public CustomerId(String id) {
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

        CustomerId customerId = (CustomerId) o;

        if (!id.equals(customerId.id)) {
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
