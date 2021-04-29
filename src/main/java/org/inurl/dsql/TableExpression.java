package org.inurl.dsql;

public interface TableExpression {

    <R> R accept(TableExpressionVisitor<R> visitor);

    default boolean isSubQuery() {
        return false;
    }
}
