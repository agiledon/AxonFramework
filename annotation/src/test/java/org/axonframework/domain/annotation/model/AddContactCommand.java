package org.axonframework.domain.annotation.model;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * @author Allard Buijze
 */
public class AddContactCommand {

    @TargetAggregateIdentifier
    private final CustomerId customerId;
    private final ContactId contactId;
    private final String name;

    public AddContactCommand(CustomerId customerId, ContactId contactId, String name) {
        this.customerId = customerId;
        this.contactId = contactId;
        this.name = name;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public ContactId getContactId() {
        return contactId;
    }
}
