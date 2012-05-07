package org.axonframework.domain.annotation;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.domain.SimpleDomainEventStream;
import org.axonframework.domain.annotation.model.ContactAddedEvent;
import org.axonframework.domain.annotation.model.ContactId;
import org.axonframework.domain.annotation.model.ContactNameChangedEvent;
import org.axonframework.domain.annotation.model.CreateCustomerCommand;
import org.axonframework.domain.annotation.model.Customer;
import org.axonframework.domain.annotation.model.CustomerCreatedEvent;
import org.axonframework.domain.annotation.model.CustomerId;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.CurrentUnitOfWork;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Allard Buijze
 */
public class CustomerTest {

    private CommandBus commandBus;
    private Repository<Customer> repository;
    private EventStore eventStore;
    private CustomerId customerId;
    private EventBus eventBus;

    @Before
    public void setUp() {
        customerId = new CustomerId();
        commandBus = new SimpleCommandBus();
        eventStore = new InMemoryEventStore();
        eventBus = new SimpleEventBus();
        repository = new AnnotationAggregateRepository<Customer>(Customer.class, eventStore, eventBus);
        DefaultUnitOfWork.startAndGet();
    }

    @After
    public void tearDown() {
        while (CurrentUnitOfWork.isStarted()) {
            CurrentUnitOfWork.get().rollback();
        }
    }

    @Test
    public void testLoadAggregate() {
        new Customer(new CreateCustomerCommand(new CustomerId(), "henk"));
        ContactId contactId = new ContactId();
        eventStore.appendEvents("test", new SimpleDomainEventStream(
                createMessages(customerId,
                               new CustomerCreatedEvent(customerId, "tester"),
                               new ContactAddedEvent(customerId, contactId, "Piet"),
                               new ContactNameChangedEvent(customerId, contactId, "Klaas"))));

        Customer customer = repository.load(customerId);
        assertEquals("tester", customer.getName());
        assertEquals(customerId, customer.getId());
        assertEquals(1, customer.getContacts().size());
        assertEquals("Klaas", customer.getContacts().get(0).getName());
    }

    private List<DomainEventMessage> createMessages(CustomerId customer, Object... payloads) {
        List<DomainEventMessage> messages = new ArrayList<DomainEventMessage>();
        long seq = 0;
        for (Object payload : payloads) {
            messages.add(new GenericDomainEventMessage<Object>(customer, seq++, payload));
        }
        return messages;
    }

    private class InMemoryEventStore implements EventStore {

        private final Map<Object, List<DomainEventMessage>> storedEvents = new HashMap<Object, List<DomainEventMessage>>();

        @Override
        public void appendEvents(String type, DomainEventStream events) {
            if (events.hasNext()) {
                if (!storedEvents.containsKey(type + "/" + events.peek().getAggregateIdentifier())) {
                    storedEvents.put(type + "/" + events.peek().getAggregateIdentifier(),
                                     new ArrayList<DomainEventMessage>());
                }
            }
            while (events.hasNext()) {
                DomainEventMessage<?> message = events.next();
                storedEvents.get(type + "/" + message.getAggregateIdentifier()).add(message);
            }
        }

        @Override
        public DomainEventStream readEvents(String type, Object identifier) {
            List<DomainEventMessage> events = storedEvents.get(type + "/" + identifier);
            if (events == null || events.isEmpty()) {
                throw new AggregateNotFoundException(identifier, "Not found!");
            }
            return new SimpleDomainEventStream(events);
        }
    }
}
