package org.axonframework.domain.annotation.model;

import org.axonframework.domain.annotation.AggregateRoot;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;

import static org.axonframework.domain.annotation.Aggregate.apply;

/**
 * @author Allard Buijze
 */
@AggregateRoot("test")
public class Customer {

    @Id // JPA Annotation for testing
    private CustomerId id;

    private List<Contact> contacts = new ArrayList<Contact>();
    private String name;

    /**
     * Default constructor for JPA and Axon.
     */
    @SuppressWarnings("UnusedDeclaration")
    protected Customer() {
    }

    @CommandHandler
    public Customer(CreateCustomerCommand command) {
        apply(this, new CustomerCreatedEvent(command.getId(), command.getName()));
    }

    @CommandHandler
    public void addContact(AddContactCommand command) {
        apply(this, new ContactAddedEvent(id, command.getContactId(), command.getName()));
    }

    @CommandHandler
    public void changeContact(ChangeContactNameCommand command) {
        apply(this, new ContactNameChangedEvent(id, command.getContactId(), command.getNewName()));
    }

    @EventHandler
    private void handleCreated(CustomerCreatedEvent event) {
        this.id = event.getCustomerId();
        this.name = event.getName();
    }

    @EventHandler
    private void handleContactAdded(ContactAddedEvent event) {
        contacts.add(new Contact(event.getContactId(), event.getName()));
    }

    /* Getters for test purposes */

    public CustomerId getId() {
        return id;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public String getName() {
        return name;
    }
}
