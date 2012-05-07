package org.axonframework.domain.annotation.model;

/**
 * @author Allard Buijze
 */
public class CustomerCreatedEvent {

    private final CustomerId customerId;
    private final String name;

    public CustomerCreatedEvent(CustomerId customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}
