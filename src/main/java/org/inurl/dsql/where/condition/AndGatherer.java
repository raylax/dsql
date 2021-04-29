package org.inurl.dsql.where.condition;

import java.util.function.Supplier;

/**
 * Utility class supporting the "and" part of a between condition. This class supports builders,
 * so it is mutable.
 *
 * @author Jeff Butler
 *
 * @param <T> the type of field for the between condition
 * @param <R> the type of condition being built
 */
public abstract class AndGatherer<T, R> {
    protected final T value1;
    protected T value2;

    protected AndGatherer(T value1) {
        this.value1 = value1;
    }

    public R and(T value2) {
        this.value2 = value2;
        return build();
    }

    public R and(Supplier<T> valueSupplier2) {
        return and(valueSupplier2.get());
    }

    protected abstract R build();
}
