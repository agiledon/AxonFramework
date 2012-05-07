package org.axonframework.domain.annotation.model;

import org.axonframework.eventhandling.annotation.EventHandler;

/**
 * @author Allard Buijze
 */
public class Contact {

    private final ContactId id;
    private String name;

    public Contact(ContactId id, String name) {
        this.id = id;
        this.name = name;
    }

    @EventHandler
    private void handle(ContactNameChangedEvent event) {
        if (event.getContactId().equals(id)) {
            this.name = event.getNewName();
        }
    }

    /* Getters for testing */

    public String getName() {
        return name;
    }
}
