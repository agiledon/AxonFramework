package org.axonframework.domain.annotation;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Allard Buijze
 */
public abstract class Aggregate {

    private static Map<Object, Reference<EventSourcedAggregateGuard>> managedAggregates = new WeakHashMap<Object, Reference<EventSourcedAggregateGuard>>();

    public static void apply(Object aggregateRoot, Object event) {
        EventSourcedAggregateGuard eventSourcedAggregate = getGuardFor(aggregateRoot);
        eventSourcedAggregate.apply(event);
    }

    public static <T> EventSourcedAggregateGuard<T> getGuardFor(T aggregateRoot) {
        Class<?> aggregateType = aggregateRoot.getClass();
        EventSourcedAggregateGuard<T> guard = null;
        while (guard == null) {
            if (!managedAggregates.containsKey(aggregateRoot)) {
                guard = new EventSourcedAggregateGuard<T>(aggregateRoot,
                                                          AggregateConfiguration.forAggregate(aggregateType));
                managedAggregates.put(aggregateRoot, new WeakReference<EventSourcedAggregateGuard>(guard));
            } else {
                guard = managedAggregates.get(aggregateRoot).get();
            }
        }
        return guard;
    }
}
