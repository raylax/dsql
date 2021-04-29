package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.util.PropertyMapping;
import org.inurl.dsql.util.StringConstantMapping;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;

public class BatchInsertDSL<T> implements Buildable<BatchInsertModel<T>> {

    private final Collection<T> records;
    private final SqlTable table;
    private final List<AbstractColumnMapping> columnMappings = new ArrayList<>();

    private BatchInsertDSL(Collection<T> records, SqlTable table) {
        this.records = records;
        this.table = table;
    }

    public <F> ColumnMappingFinisher<F> map(SqlColumn<F> column) {
        return new ColumnMappingFinisher<>(column);
    }

    @NotNull
    @Override
    public BatchInsertModel<T> build() {
        return BatchInsertModel.withRecords(records)
                .withTable(table)
                .withColumnMappings(columnMappings)
                .build();
    }

    @SafeVarargs
    public static <T> IntoGatherer<T> insert(T...records) {
        return BatchInsertDSL.insert(Arrays.asList(records));
    }

    public static <T> IntoGatherer<T> insert(Collection<T> records) {
        return new IntoGatherer<>(records);
    }

    public static class IntoGatherer<T> {
        private final Collection<T> records;

        private IntoGatherer(Collection<T> records) {
            this.records = records;
        }

        public BatchInsertDSL<T> into(SqlTable table) {
            return new BatchInsertDSL<>(records, table);
        }
    }

    public class ColumnMappingFinisher<F> {
        private final SqlColumn<F> column;

        public ColumnMappingFinisher(SqlColumn<F> column) {
            this.column = column;
        }

        public BatchInsertDSL<T> toProperty(String property) {
            columnMappings.add(PropertyMapping.of(column, property));
            return BatchInsertDSL.this;
        }

        public BatchInsertDSL<T> toNull() {
            columnMappings.add(NullMapping.of(column));
            return BatchInsertDSL.this;
        }

        public BatchInsertDSL<T> toConstant(String constant) {
            columnMappings.add(ConstantMapping.of(column, constant));
            return BatchInsertDSL.this;
        }

        public BatchInsertDSL<T> toStringConstant(String constant) {
            columnMappings.add(StringConstantMapping.of(column, constant));
            return BatchInsertDSL.this;
        }
    }
}
