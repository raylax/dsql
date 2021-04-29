package org.inurl.dsql;

public interface ConditionVisitor<T, R> {
    R visit(AbstractListValueCondition<T> condition);

    R visit(AbstractNoValueCondition<T> condition);

    R visit(AbstractSingleValueCondition<T> condition);

    R visit(AbstractTwoValueCondition<T> condition);

    R visit(AbstractSubselectCondition<T> condition);

    R visit(AbstractColumnComparisonCondition<T> condition);
}
