package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;
import org.inurl.dsql.util.StringConstantMapping;
import org.inurl.dsql.util.ValueMapping;
import org.inurl.dsql.util.ValueOrNullMapping;
import org.inurl.dsql.util.ValueWhenPresentMapping;
import org.jetbrains.annotations.NotNull;

public class GeneralInsertDSL implements Buildable<GeneralInsertModel> {
    private final List<AbstractColumnMapping> insertMappings = new ArrayList<>();
    private final SqlTable table;

    private GeneralInsertDSL(SqlTable table) {
        this.table = Objects.requireNonNull(table);
    }

    public <T> SetClauseFinisher<T> set(SqlColumn<T> column) {
        return new SetClauseFinisher<>(column);
    }

    @NotNull
    @Override
    public GeneralInsertModel build() {
        return new GeneralInsertModel.Builder()
                .withTable(table)
                .withInsertMappings(insertMappings)
                .build();
    }

    public static GeneralInsertDSL insertInto(SqlTable table) {
        return new GeneralInsertDSL(table);
    }

    public class SetClauseFinisher<T> {

        private final SqlColumn<T> column;

        public SetClauseFinisher(SqlColumn<T> column) {
            this.column = column;
        }

        public GeneralInsertDSL toNull() {
            insertMappings.add(NullMapping.of(column));
            return GeneralInsertDSL.this;
        }

        public GeneralInsertDSL toConstant(String constant) {
            insertMappings.add(ConstantMapping.of(column, constant));
            return GeneralInsertDSL.this;
        }

        public GeneralInsertDSL toStringConstant(String constant) {
            insertMappings.add(StringConstantMapping.of(column, constant));
            return GeneralInsertDSL.this;
        }

        public GeneralInsertDSL toValue(T value) {
            return toValue(() -> value);
        }

        public GeneralInsertDSL toValue(Supplier<T> valueSupplier) {
            insertMappings.add(ValueMapping.of(column, valueSupplier));
            return GeneralInsertDSL.this;
        }

        public GeneralInsertDSL toValueOrNull(T value) {
            return toValueOrNull(() -> value);
        }

        public GeneralInsertDSL toValueOrNull(Supplier<T> valueSupplier) {
            insertMappings.add(ValueOrNullMapping.of(column, valueSupplier));
            return GeneralInsertDSL.this;
        }

        public GeneralInsertDSL toValueWhenPresent(T value) {
            return toValueWhenPresent(() -> value);
        }

        public GeneralInsertDSL toValueWhenPresent(Supplier<T> valueSupplier) {
            insertMappings.add(ValueWhenPresentMapping.of(column, valueSupplier));
            return GeneralInsertDSL.this;
        }
    }
}
