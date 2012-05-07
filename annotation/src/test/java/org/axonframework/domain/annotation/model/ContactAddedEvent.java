package org.axonframework.domain.annotation.model;

/**
 * @author Allard Buijze
 */
public class ContactAddedEvent {

    private final CustomerId customerId;
    private final ContactId contactId;
    private final String name;

    public ContactAddedEvent(CustomerId customerId, ContactId contactId, String name) {
        this.customerId = customerId;
        this.contactId = contactId;
        this.name = name;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public ContactId getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }
}
