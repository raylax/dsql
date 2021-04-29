package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.util.PropertyMapping;
import org.inurl.dsql.util.StringConstantMapping;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;
import org.inurl.dsql.util.PropertyWhenPresentMapping;

public class InsertDSL<T> implements Buildable<InsertModel<T>> {

    private final T record;
    private final SqlTable table;
    private final List<AbstractColumnMapping> columnMappings = new ArrayList<>();

    private InsertDSL(T record, SqlTable table) {
        this.record = record;
        this.table = table;
    }

    public <F> ColumnMappingFinisher<F> map(SqlColumn<F> column) {
        return new ColumnMappingFinisher<>(column);
    }

    @NotNull
    @Override
    public InsertModel<T> build() {
        return InsertModel.withRecord(record)
                .withTable(table)
                .withColumnMappings(columnMappings)
                .build();
    }

    public static <T> IntoGatherer<T> insert(T record) {
        return new IntoGatherer<>(record);
    }

    public static class IntoGatherer<T> {
        private final T record;

        private IntoGatherer(T record) {
            this.record = record;
        }

        public InsertDSL<T> into(SqlTable table) {
            return new InsertDSL<>(record, table);
        }
    }

    public class ColumnMappingFinisher<F> {
        private final SqlColumn<F> column;

        public ColumnMappingFinisher(SqlColumn<F> column) {
            this.column = column;
        }

        public InsertDSL<T> toProperty(String property) {
            columnMappings.add(PropertyMapping.of(column, property));
            return InsertDSL.this;
        }

        public InsertDSL<T> toPropertyWhenPresent(String property, Supplier<?> valueSupplier) {
            columnMappings.add(PropertyWhenPresentMapping.of(column, property, valueSupplier));
            return InsertDSL.this;
        }

        public InsertDSL<T> toNull() {
            columnMappings.add(NullMapping.of(column));
            return InsertDSL.this;
        }

        public InsertDSL<T> toConstant(String constant) {
            columnMappings.add(ConstantMapping.of(column, constant));
            return InsertDSL.this;
        }

        public InsertDSL<T> toStringConstant(String constant) {
            columnMappings.add(StringConstantMapping.of(column, constant));
            return InsertDSL.this;
        }
    }
}
