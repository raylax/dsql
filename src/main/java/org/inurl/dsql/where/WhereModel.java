package org.inurl.dsql.where;

import org.inurl.dsql.SqlCriterion;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.where.render.WhereClauseProvider;
import org.inurl.dsql.where.render.WhereRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class WhereModel {
    private static final WhereClauseProvider EMPTY_WHERE_CLAUSE =
            new WhereClauseProvider.Builder().withWhereClause("").build();

    private final List<SqlCriterion> criteria = new ArrayList<>();

    private WhereModel(List<SqlCriterion> criteria) {
        this.criteria.addAll(criteria);
    }

    public <R> Stream<R> mapCriteria(Function<SqlCriterion, R> mapper) {
        return criteria.stream().map(mapper);
    }

    /**
     * Renders a where clause without table aliases.
     *
     * @param renderingStrategy rendering strategy
     * @return rendered where clause
     */
    public WhereClauseProvider render(RenderingStrategy renderingStrategy) {
        return WhereRenderer.withWhereModel(this)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(new AtomicInteger(1))
                .withTableAliasCalculator(TableAliasCalculator.empty())
                .build()
                .render()
                .orElse(EMPTY_WHERE_CLAUSE);
    }

    public WhereClauseProvider render(RenderingStrategy renderingStrategy,
            TableAliasCalculator tableAliasCalculator) {
        return WhereRenderer.withWhereModel(this)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(new AtomicInteger(1))
                .withTableAliasCalculator(tableAliasCalculator)
                .build()
                .render()
                .orElse(EMPTY_WHERE_CLAUSE);
    }

    public WhereClauseProvider render(RenderingStrategy renderingStrategy,
            String parameterName) {
        return WhereRenderer.withWhereModel(this)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(new AtomicInteger(1))
                .withTableAliasCalculator(TableAliasCalculator.empty())
                .withParameterName(parameterName)
                .build()
                .render()
                .orElse(EMPTY_WHERE_CLAUSE);
    }

    public WhereClauseProvider render(RenderingStrategy renderingStrategy,
            TableAliasCalculator tableAliasCalculator, String parameterName) {
        return WhereRenderer.withWhereModel(this)
                .withRenderingStrategy(renderingStrategy)
                .withSequence(new AtomicInteger(1))
                .withTableAliasCalculator(tableAliasCalculator)
                .withParameterName(parameterName)
                .build()
                .render()
                .orElse(EMPTY_WHERE_CLAUSE);
    }

    public static WhereModel of(List<SqlCriterion> criteria) {
        return new WhereModel(criteria);
    }
}
