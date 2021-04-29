package org.inurl.dsql.delete;

import java.util.Objects;
import java.util.function.Function;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.where.AbstractWhereDSL;
import org.inurl.dsql.where.AbstractWhereSupport;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.where.WhereModel;

public class DeleteDSL<R> extends AbstractWhereSupport<DeleteDSL<R>.DeleteWhereBuilder> implements Buildable<R> {

    private final Function<DeleteModel, R> adapterFunction;
    private final SqlTable table;
    private final DeleteWhereBuilder whereBuilder = new DeleteWhereBuilder();

    private DeleteDSL(SqlTable table, Function<DeleteModel, R> adapterFunction) {
        this.table = Objects.requireNonNull(table);
        this.adapterFunction = Objects.requireNonNull(adapterFunction);
    }

    @Override
    public DeleteWhereBuilder where() {
        return whereBuilder;
    }

    /**
     * WARNING! Calling this method could result in an delete statement that deletes
     * all rows in a table.
     *
     * @return the model class
     */
    @NotNull
    @Override
    public R build() {
        DeleteModel deleteModel = DeleteModel.withTable(table)
                .withWhereModel(whereBuilder.buildWhereModel())
                .build();
        return adapterFunction.apply(deleteModel);
    }

    public static <R> DeleteDSL<R> deleteFrom(Function<DeleteModel, R> adapterFunction, SqlTable table) {
        return new DeleteDSL<>(table, adapterFunction);
    }

    public static DeleteDSL<DeleteModel> deleteFrom(SqlTable table) {
        return deleteFrom(Function.identity(), table);
    }

    public class DeleteWhereBuilder extends AbstractWhereDSL<DeleteWhereBuilder> implements Buildable<R> {

        private DeleteWhereBuilder() {}

        @NotNull
        @Override
        public R build() {
            return DeleteDSL.this.build();
        }

        @Override
        protected DeleteWhereBuilder getThis() {
            return this;
        }

        protected WhereModel buildWhereModel() {
            return internalBuild();
        }
    }
}
