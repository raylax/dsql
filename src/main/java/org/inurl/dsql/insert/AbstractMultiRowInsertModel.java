package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.util.AbstractColumnMapping;

public abstract class AbstractMultiRowInsertModel<T> {
    private final SqlTable table;
    private final List<T> records;
    private final List<AbstractColumnMapping> columnMappings;

    protected AbstractMultiRowInsertModel(AbstractBuilder<T, ?> builder) {
        table = Objects.requireNonNull(builder.table);
        records = Collections.unmodifiableList(Objects.requireNonNull(builder.records));
        columnMappings = Objects.requireNonNull(builder.columnMappings);
    }

    public <R> Stream<R> mapColumnMappings(Function<AbstractColumnMapping, R> mapper) {
        return columnMappings.stream().map(mapper);
    }

    public List<T> records() {
        return records;
    }

    public SqlTable table() {
        return table;
    }

    public int recordCount() {
        return records.size();
    }

    public abstract static class AbstractBuilder<T, S extends AbstractBuilder<T, S>> {
        private SqlTable table;
        private final List<T> records = new ArrayList<>();
        private final List<AbstractColumnMapping> columnMappings = new ArrayList<>();

        public S withTable(SqlTable table) {
            this.table = table;
            return getThis();
        }

        public S withRecords(Collection<T> records) {
            this.records.addAll(records);
            return getThis();
        }

        public S withColumnMappings(List<AbstractColumnMapping> columnMappings) {
            this.columnMappings.addAll(columnMappings);
            return getThis();
        }

        protected abstract S getThis();
    }
}
