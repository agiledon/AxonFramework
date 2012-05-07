package org.axonframework.domain.annotation;

/**
 * @author Allard Buijze
 */
public interface AggregateProperty<T> {

    T get(Object owner);

    void set(Object owner, T value);
}
