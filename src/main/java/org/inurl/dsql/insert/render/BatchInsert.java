package org.inurl.dsql.insert.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BatchInsert<T> {
    private final String insertStatement;
    private final List<T> records;

    private BatchInsert(Builder<T> builder) {
        insertStatement = Objects.requireNonNull(builder.insertStatement);
        records = Collections.unmodifiableList(Objects.requireNonNull(builder.records));
    }

    /**
     * Returns a list of InsertStatement objects.  This is useful for MyBatis batch support.
     *
     * @return a List of InsertStatements
     */
    public List<InsertStatementProvider<T>> insertStatements() {
        return records.stream()
                .map(this::toInsertStatement)
                .collect(Collectors.toList());
    }

    private InsertStatementProvider<T> toInsertStatement(T record) {
        return DefaultInsertStatementProvider.withRecord(record)
                .withInsertStatement(insertStatement)
                .build();
    }

    /**
     * Returns the generated SQL for this batch.  This is useful for Spring JDBC batch support.
     *
     * @return the generated INSERT statement
     */
    public String getInsertStatementSQL() {
        return insertStatement;
    }

    public List<T> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public static <T> Builder<T> withRecords(List<T> records) {
        return new Builder<T>().withRecords(records);
    }

    public static class Builder<T> {
        private String insertStatement;
        private final List<T> records = new ArrayList<>();

        public Builder<T> withInsertStatement(String insertStatement) {
            this.insertStatement = insertStatement;
            return this;
        }

        public Builder<T> withRecords(List<T> records) {
            this.records.addAll(records);
            return this;
        }

        public BatchInsert<T> build() {
            return new BatchInsert<>(this);
        }
    }
}
