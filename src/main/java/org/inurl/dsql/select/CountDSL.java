package org.inurl.dsql.select;

import java.util.Objects;
import java.util.function.Function;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.SqlBuilder;
import org.inurl.dsql.SqlTable;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.where.AbstractWhereDSL;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.where.WhereModel;

/**
 * DSL for building count queries. Count queries are specializations of select queries. They have joins and where
 * clauses, but not the other parts of a select (group by, order by, etc.) Count queries always return
 * a long. If these restrictions are not acceptable, then use the Select DSL for an unrestricted select statement.
 *
 * @param <R> the type of model built by this Builder. Typically SelectModel.
 *
 * @author Jeff Butler
 */
public class CountDSL<R> extends AbstractQueryExpressionDSL<CountDSL<R>.CountWhereBuilder, CountDSL<R>>
        implements Buildable<R> {

    private final Function<SelectModel, R> adapterFunction;
    private final CountWhereBuilder whereBuilder = new CountWhereBuilder();
    private final BasicColumn countColumn;

    private CountDSL(BasicColumn countColumn, SqlTable table, Function<SelectModel, R> adapterFunction) {
        super(table);
        this.countColumn = Objects.requireNonNull(countColumn);
        this.adapterFunction = Objects.requireNonNull(adapterFunction);
    }

    @Override
    public CountWhereBuilder where() {
        return whereBuilder;
    }

    @NotNull
    @Override
    public R build() {
        return adapterFunction.apply(buildModel());
    }

    private SelectModel buildModel() {
        QueryExpressionModel.Builder b = new QueryExpressionModel.Builder()
                .withSelectColumn(countColumn)
                .withTable(table())
                .withTableAliases(tableAliases)
                .withWhereModel(whereBuilder.buildWhereModel());

        buildJoinModel().ifPresent(b::withJoinModel);

        return new SelectModel.Builder()
                .withQueryExpression(b.build())
                .build();
    }

    public static CountDSL<SelectModel> countFrom(SqlTable table) {
        return countFrom(Function.identity(), table);
    }

    public static <R> CountDSL<R> countFrom(Function<SelectModel, R> adapterFunction, SqlTable table) {
        return new CountDSL<>(SqlBuilder.count(), table, adapterFunction);
    }

    public static FromGatherer<SelectModel> count(BasicColumn column) {
        return count(Function.identity(), column);
    }

    public static <R> FromGatherer<R> count(Function<SelectModel, R> adapterFunction, BasicColumn column) {
        return new FromGatherer<>(adapterFunction, SqlBuilder.count(column));
    }

    public static FromGatherer<SelectModel> countDistinct(BasicColumn column) {
        return countDistinct(Function.identity(), column);
    }

    public static <R> FromGatherer<R> countDistinct(Function<SelectModel, R> adapterFunction, BasicColumn column) {
        return new FromGatherer<>(adapterFunction, SqlBuilder.countDistinct(column));
    }

    @Override
    protected CountDSL<R> getThis() {
        return this;
    }

    public static class FromGatherer<R> {
        private final BasicColumn column;
        private final Function<SelectModel, R> adapterFunction;

        public FromGatherer(Function<SelectModel, R> adapterFunction, BasicColumn column) {
            this.adapterFunction = adapterFunction;
            this.column = column;
        }

        public CountDSL<R> from(SqlTable table) {
            return new CountDSL<>(column, table, adapterFunction);
        }
    }

    public class CountWhereBuilder extends AbstractWhereDSL<CountWhereBuilder>
            implements Buildable<R> {
        private CountWhereBuilder() {}

        @NotNull
        @Override
        public R build() {
            return CountDSL.this.build();
        }

        @Override
        protected CountWhereBuilder getThis() {
            return this;
        }

        protected WhereModel buildWhereModel() {
            return internalBuild();
        }
    }
}
