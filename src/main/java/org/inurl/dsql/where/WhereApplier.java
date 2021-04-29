package org.inurl.dsql.where;

import java.util.function.Consumer;

@FunctionalInterface
public interface WhereApplier extends Consumer<AbstractWhereDSL<?>> {
    /**
     * Return a composed where applier that performs this operation followed
     * by the after operation.
     *
     * @param after the operation to perform after this operation
     * @return a composed where applier that performs this operation followed
     *     by the after operation.
     */
    default WhereApplier andThen(WhereApplier after) {
        return t -> {
            accept(t);
            after.accept(t);
        };
    }
}
