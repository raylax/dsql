package org.inurl.dsql.select.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.TableExpressionVisitor;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.select.SubQuery;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.StringUtilities;

public class TableExpressionRenderer implements TableExpressionVisitor<FragmentAndParameters> {
    private final TableAliasCalculator tableAliasCalculator;
    private final RenderingStrategy renderingStrategy;
    private final AtomicInteger sequence;

    private TableExpressionRenderer(Builder builder) {
        tableAliasCalculator = Objects.requireNonNull(builder.tableAliasCalculator);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
        sequence = Objects.requireNonNull(builder.sequence);
    }

    @Override
    public FragmentAndParameters visit(SqlTable table) {
        return FragmentAndParameters.withFragment(
                tableAliasCalculator.aliasForTable(table)
                        .map(a -> table.tableNameAtRuntime() + StringUtilities.spaceBefore(a))
                        .orElseGet(table::tableNameAtRuntime))
                .build();
    }

    @Override
    public FragmentAndParameters visit(SubQuery subQuery) {
        SelectStatementProvider selectStatement = new SelectRenderer.Builder()
                .withSelectModel(subQuery.selectModel())
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build()
                .render();

        String fragment = "(" + selectStatement.getSelectStatement() + ")";

        fragment = applyAlias(fragment, subQuery);

        return FragmentAndParameters.withFragment(fragment)
                .withParameters(selectStatement.getParameters())
                .build();
    }

    private String applyAlias(String fragment, SubQuery subQuery) {
        return subQuery.alias()
                .map(a -> fragment + StringUtilities.spaceBefore(a))
                .orElse(fragment);
    }

    public static class Builder {
        private TableAliasCalculator tableAliasCalculator;
        private RenderingStrategy renderingStrategy;
        private AtomicInteger sequence;

        public Builder withTableAliasCalculator(TableAliasCalculator tableAliasCalculator) {
            this.tableAliasCalculator = tableAliasCalculator;
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

        public TableExpressionRenderer build() {
            return new TableExpressionRenderer(this);
        }
    }
}
