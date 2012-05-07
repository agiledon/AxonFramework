package org.axonframework.domain.annotation;

import org.axonframework.eventsourcing.IncompatibleAggregateException;

import java.lang.reflect.Field;

/**
 * @author Allard Buijze
 */
public class AggregateFieldProperty<T> implements AggregateProperty<T> {

    private final Field field;
    private final Class<T> expectedType;

    public AggregateFieldProperty(Field field, Class<T> expectedType) {
        this.field = field;
        this.expectedType = expectedType;
    }

    @Override
    public T get(Object owner) {
        try {
            return expectedType.cast(field.get(owner));
        } catch (IllegalAccessException e) {
            throw new IncompatibleAggregateException("Aggregate field not accessible for read.", e);
        }
    }

    @Override
    public void set(Object owner, T value) {
        try {
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Aggregate field not accessible for write.", e);
        }
    }
}
