package org.axonframework.domain.annotation.model;

/**
 * @author Allard Buijze
 */
public class CreateCustomerCommand {

    private final CustomerId id;
    private final String name;

    public CreateCustomerCommand(CustomerId id, String name) {
        this.id = id;
        this.name = name;
    }

    public CustomerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
