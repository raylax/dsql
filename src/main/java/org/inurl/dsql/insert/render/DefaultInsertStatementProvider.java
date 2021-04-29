package org.inurl.dsql.insert.render;

import java.util.Objects;

public class DefaultInsertStatementProvider<T> implements InsertStatementProvider<T> {
    private final String insertStatement;
    private final T record;

    private DefaultInsertStatementProvider(Builder<T> builder) {
        insertStatement = Objects.requireNonNull(builder.insertStatement);
        record = Objects.requireNonNull(builder.record);
    }

    @Override
    public T getRecord() {
        return record;
    }

    @Override
    public String getInsertStatement() {
        return insertStatement;
    }

    public static <T> Builder<T> withRecord(T record) {
        return new Builder<T>().withRecord(record);
    }

    public static class Builder<T> {
        private String insertStatement;
        private T record;

        public Builder<T> withInsertStatement(String insertStatement) {
            this.insertStatement = insertStatement;
            return this;
        }

        public Builder<T> withRecord(T record) {
            this.record = record;
            return this;
        }

        public DefaultInsertStatementProvider<T> build() {
            return new DefaultInsertStatementProvider<>(this);
        }
    }
}
