package org.axonframework.domain.annotation;

import org.axonframework.domain.AbstractAggregateRoot;
import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.eventhandling.annotation.AnnotationEventHandlerInvoker;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;

/**
 * @author Allard Buijze
 */
public class EventSourcedAggregateGuard<T> extends AbstractAggregateRoot implements EventSourcedAggregateRoot {

    private static final long serialVersionUID = -2689775300916970418L;

    private final AnnotationEventHandlerInvoker invoker;
    private final T aggregateRoot;
    private final Object identifier;
    private final AggregateConfiguration configuration;

    public EventSourcedAggregateGuard(T aggregateRoot, AggregateConfiguration configuration) {
        this.aggregateRoot = aggregateRoot;
        this.configuration = configuration;
        this.identifier = configuration.getIdentifier(aggregateRoot);
        invoker = new AnnotationEventHandlerInvoker(aggregateRoot);
    }

    @Override
    public void markDeleted() {
        super.markDeleted();
    }

    public void apply(Object event) {
        DomainEventMessage<?> eventMessage = registerEvent(event);
        invokeRecursively(eventMessage);
    }

    @Override
    public void initializeState(Object aggregateIdentifier, DomainEventStream domainEventStream) {
        configuration.getIdentifierProperty().set(aggregateRoot, aggregateIdentifier);
        DomainEventMessage lastEvent = null;
        while (domainEventStream.hasNext()) {
            lastEvent = domainEventStream.next();
            invokeRecursively(lastEvent);
        }
        if (lastEvent != null) {
            initializeEventStream(lastEvent.getSequenceNumber());
        }
    }

    private void invokeRecursively(DomainEventMessage<?> eventMessage) {
        invoker.invokeEventHandlerMethod(eventMessage);
    }

    @Override
    public Object getIdentifier() {
        return identifier;
    }

    public T getAggregateRoot() {
        return aggregateRoot;
    }

    /**
     * When serializing, we actually want to serialize the aggregate itself
     */
    protected Object writeReplace() {
        // TODO: Serialization of this object needs to be smarter (writeObject, readObject)
        return aggregateRoot;
    }
}
