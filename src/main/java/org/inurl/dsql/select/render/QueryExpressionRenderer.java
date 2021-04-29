package org.inurl.dsql.select.render;

import static org.inurl.dsql.util.StringUtilities.spaceAfter;
import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.TableExpression;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.GroupByModel;
import org.inurl.dsql.select.QueryExpressionModel;
import org.inurl.dsql.select.join.JoinModel;
import org.inurl.dsql.util.CustomCollectors;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.where.WhereModel;
import org.inurl.dsql.where.render.WhereClauseProvider;
import org.inurl.dsql.where.render.WhereRenderer;

public class QueryExpressionRenderer {
    private final QueryExpressionModel queryExpression;
    private final RenderingStrategy renderingStrategy;
    private final AtomicInteger sequence;
    private final TableExpressionRenderer tableExpressionRenderer;

    private QueryExpressionRenderer(Builder builder) {
        queryExpression = Objects.requireNonNull(builder.queryExpression);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
        sequence = Objects.requireNonNull(builder.sequence);
        tableExpressionRenderer = new TableExpressionRenderer.Builder()
                .withTableAliasCalculator(queryExpression.tableAliasCalculator())
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build();
    }

    public FragmentAndParameters render() {
        FragmentAndParameters answer = calculateQueryExpressionStart();
        answer = addJoinClause(answer);
        answer = addWhereClause(answer);
        answer = addGroupByClause(answer);
        return answer;
    }

    private FragmentAndParameters calculateQueryExpressionStart() {
        String start = spaceAfter(queryExpression.connector())
                + "select "
                + (queryExpression.isDistinct() ? "distinct " : "")
                + calculateColumnList()
                + " from ";

        FragmentAndParameters renderedTable = renderTableExpression(queryExpression.table());
        start += renderedTable.fragment();

        return FragmentAndParameters.withFragment(start)
                .withParameters(renderedTable.parameters())
                .build();
    }

    private String calculateColumnList() {
        return queryExpression.mapColumns(this::applyTableAndColumnAlias)
                .collect(Collectors.joining(", "));
    }

    private String applyTableAndColumnAlias(BasicColumn selectListItem) {
        return selectListItem.renderWithTableAndColumnAlias(queryExpression.tableAliasCalculator());
    }

    private FragmentAndParameters renderTableExpression(TableExpression table) {
        return table.accept(tableExpressionRenderer);
    }

    private FragmentAndParameters addJoinClause(FragmentAndParameters partial) {
        return queryExpression.joinModel()
                .map(this::renderJoin)
                .map(fp -> partial.add(spaceBefore(fp.fragment()), fp.parameters()))
                .orElse(partial);
    }

    private FragmentAndParameters renderJoin(JoinModel joinModel) {
        return JoinRenderer.withJoinModel(joinModel)
                .withQueryExpression(queryExpression)
                .withTableExpressionRenderer(tableExpressionRenderer)
                .build()
                .render();
    }

    private FragmentAndParameters addWhereClause(FragmentAndParameters partial) {
        return queryExpression.whereModel()
                .flatMap(this::renderWhereClause)
                .map(wc -> partial.add(spaceBefore(wc.getWhereClause()), wc.getParameters()))
                .orElse(partial);
    }

    private Optional<WhereClauseProvider> renderWhereClause(WhereModel whereModel) {
        return WhereRenderer.withWhereModel(whereModel)
                .withRenderingStrategy(renderingStrategy)
                .withTableAliasCalculator(queryExpression.tableAliasCalculator())
                .withSequence(sequence)
                .build()
                .render();
    }

    private FragmentAndParameters addGroupByClause(FragmentAndParameters partial) {
        return queryExpression.groupByModel()
                .map(this::renderGroupBy)
                .map(s -> partial.add(spaceBefore(s)))
                .orElse(partial);
    }

    private String renderGroupBy(GroupByModel groupByModel) {
        return groupByModel.mapColumns(this::applyTableAlias)
                .collect(CustomCollectors.joining(", ", "group by ", ""));
    }

    private String applyTableAlias(BasicColumn column) {
        return column.renderWithTableAlias(queryExpression.tableAliasCalculator());
    }

    public static Builder withQueryExpression(QueryExpressionModel model) {
        return new Builder().withQueryExpression(model);
    }

    public static class Builder {
        private QueryExpressionModel queryExpression;
        private RenderingStrategy renderingStrategy;
        private AtomicInteger sequence;

        public Builder withQueryExpression(QueryExpressionModel queryExpression) {
            this.queryExpression = queryExpression;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public Builder withSequence(AtomicInteger sequence) {
            this.sequence = sequence;
            return this;
        }

        public QueryExpressionRenderer build() {
            return new QueryExpressionRenderer(this);
        }
    }
}
