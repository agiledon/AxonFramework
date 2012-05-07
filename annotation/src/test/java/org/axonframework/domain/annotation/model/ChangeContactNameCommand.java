package org.axonframework.domain.annotation.model;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * @author Allard Buijze
 */
public class ChangeContactNameCommand {

    @TargetAggregateIdentifier
    private final CustomerId customerId;
    private final ContactId contactId;
    private final String newName;

    public ChangeContactNameCommand(CustomerId customerId, ContactId contactId, String newName) {
        this.customerId = customerId;
        this.contactId = contactId;
        this.newName = newName;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public ContactId getContactId() {
        return contactId;
    }

    public String getNewName() {
        return newName;
    }
}
