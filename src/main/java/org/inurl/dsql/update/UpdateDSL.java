package org.inurl.dsql.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.ColumnToColumnMapping;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;
import org.inurl.dsql.util.StringConstantMapping;
import org.inurl.dsql.util.ValueMapping;
import org.inurl.dsql.util.ValueOrNullMapping;
import org.inurl.dsql.util.ValueWhenPresentMapping;
import org.inurl.dsql.where.AbstractWhereSupport;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.util.SelectMapping;
import org.inurl.dsql.where.AbstractWhereDSL;
import org.inurl.dsql.where.WhereModel;

public class UpdateDSL<R> extends AbstractWhereSupport<UpdateDSL<R>.UpdateWhereBuilder> implements Buildable<R> {

    private final Function<UpdateModel, R> adapterFunction;
    private final List<AbstractColumnMapping> columnMappings = new ArrayList<>();
    private final SqlTable table;
    private final UpdateWhereBuilder whereBuilder = new UpdateWhereBuilder();

    private UpdateDSL(SqlTable table, Function<UpdateModel, R> adapterFunction) {
        this.table = Objects.requireNonNull(table);
        this.adapterFunction = Objects.requireNonNull(adapterFunction);
    }

    public <T> SetClauseFinisher<T> set(SqlColumn<T> column) {
        return new SetClauseFinisher<>(column);
    }

    @Override
    public UpdateWhereBuilder where() {
        return whereBuilder;
    }

    /**
     * WARNING! Calling this method could result in an update statement that updates
     * all rows in a table.
     *
     * @return the update model
     */
    @NotNull
    @Override
    public R build() {
        UpdateModel updateModel = UpdateModel.withTable(table)
                .withColumnMappings(columnMappings)
                .withWhereModel(whereBuilder.buildWhereModel())
                .build();
        return adapterFunction.apply(updateModel);
    }

    public static <R> UpdateDSL<R> update(Function<UpdateModel, R> adapterFunction, SqlTable table) {
        return new UpdateDSL<>(table, adapterFunction);
    }

    public static UpdateDSL<UpdateModel> update(SqlTable table) {
        return update(Function.identity(), table);
    }

    public class SetClauseFinisher<T> {

        private final SqlColumn<T> column;

        public SetClauseFinisher(SqlColumn<T> column) {
            this.column = column;
        }

        public UpdateDSL<R> equalToNull() {
            columnMappings.add(NullMapping.of(column));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalToConstant(String constant) {
            columnMappings.add(ConstantMapping.of(column, constant));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalToStringConstant(String constant) {
            columnMappings.add(StringConstantMapping.of(column, constant));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalTo(T value) {
            return equalTo(() -> value);
        }

        public UpdateDSL<R> equalTo(Supplier<T> valueSupplier) {
            columnMappings.add(ValueMapping.of(column, valueSupplier));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalTo(Buildable<SelectModel> buildable) {
            columnMappings.add(SelectMapping.of(column, buildable));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalTo(BasicColumn rightColumn) {
            columnMappings.add(ColumnToColumnMapping.of(column, rightColumn));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalToOrNull(T value) {
            return equalToOrNull(() -> value);
        }

        public UpdateDSL<R> equalToOrNull(Supplier<T> valueSupplier) {
            columnMappings.add(ValueOrNullMapping.of(column, valueSupplier));
            return UpdateDSL.this;
        }

        public UpdateDSL<R> equalToWhenPresent(T value) {
            return equalToWhenPresent(() -> value);
        }

        public UpdateDSL<R> equalToWhenPresent(Supplier<T> valueSupplier) {
            columnMappings.add(ValueWhenPresentMapping.of(column, valueSupplier));
            return UpdateDSL.this;
        }
    }

    public class UpdateWhereBuilder extends AbstractWhereDSL<UpdateWhereBuilder> implements Buildable<R> {

        private UpdateWhereBuilder() {}

        @NotNull
        @Override
        public R build() {
            return UpdateDSL.this.build();
        }

        @Override
        protected UpdateWhereBuilder getThis() {
            return this;
        }

        protected WhereModel buildWhereModel() {
            return internalBuild();
        }
    }
}
