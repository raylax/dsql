package org.inurl.dsql.insert.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultMultiRowInsertStatementProvider<T> implements MultiRowInsertStatementProvider<T> {

    private final List<T> records;
    private final String insertStatement;

    private DefaultMultiRowInsertStatementProvider(Builder<T> builder) {
        insertStatement = Objects.requireNonNull(builder.insertStatement);
        records = Collections.unmodifiableList(builder.records);
    }

    @Override
    public String getInsertStatement() {
        return insertStatement;
    }

    @Override
    public List<T> getRecords() {
        return records;
    }

    public static class Builder<T> {
        private final List<T> records = new ArrayList<>();
        private String insertStatement;

        public Builder<T> withRecords(List<T> records) {
            this.records.addAll(records);
            return this;
        }

        public Builder<T> withInsertStatement(String insertStatement) {
            this.insertStatement = insertStatement;
            return this;
        }

        public DefaultMultiRowInsertStatementProvider<T> build() {
            return new DefaultMultiRowInsertStatementProvider<>(this);
        }
    }
}
