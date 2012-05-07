package org.axonframework.domain.annotation;

import org.axonframework.eventsourcing.IncompatibleAggregateException;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.lang.String.format;
import static org.axonframework.common.ReflectionUtils.ensureAccessible;
import static org.axonframework.common.ReflectionUtils.fieldsOf;

/**
 * @author Allard Buijze
 */
public class AggregateConfiguration {

    private static ConcurrentMap<Class, AggregateConfiguration> configurations =
            new ConcurrentSkipListMap<Class, AggregateConfiguration>(new ClassNameComparator());


    public static AggregateConfiguration forAggregate(Class<?> aggregateType) {
        if (!configurations.containsKey(aggregateType)) {
            configurations.putIfAbsent(aggregateType, new AggregateConfiguration(aggregateType));
        }
        return configurations.get(aggregateType);
    }



    private final Field identifierField;
    private final Class<?> aggregateType;

    private AggregateConfiguration(Class<?> aggregateType) {
        identifierField = locateIdentifierField(aggregateType);
        this.aggregateType = aggregateType;
    }

    private Field locateIdentifierField(Class<?> aggregateType) {
        for (Field candidate : fieldsOf(aggregateType)) {
            if (containsIdentifierAnnotation(candidate.getAnnotations())) {
                ensureAccessible(candidate);
                return candidate;
            }
        }
        throw new IncompatibleAggregateException(format("The aggregate class [%s] does not specify an Identifier. "
                                                                + "Ensure that the field containing the aggregate "
                                                                + "identifier is annotated with @AggregateIdentifier.",
                                                        aggregateType.getSimpleName()));
    }

    private static boolean containsIdentifierAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof AggregateIdentifier) {
                return true;
            } else if (annotation.toString().startsWith("@javax.persistence.Id(")) {
                return true;
            }
        }
        return false;
    }

    public Object getIdentifier(Object aggregateRoot) {
        try {
            return identifierField.get(aggregateRoot);
        } catch (IllegalAccessException e) {
            throw new IncompatibleAggregateException("Whoops");
        }
    }

    public AggregateProperty<Object> getIdentifierProperty() {
        return new AggregateFieldProperty<Object>(identifierField, Object.class);
    }

    public String getTypeIdentifier() {
        AggregateRoot annotation = aggregateType.getAnnotation(AggregateRoot.class);
        if (annotation != null && !"".equals(annotation.value())) {
            return annotation.value();
        }
        return aggregateType.getSimpleName();
    }

    private static class ClassNameComparator implements Comparator<Class> {
        @Override
        public int compare(Class o1, Class o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
