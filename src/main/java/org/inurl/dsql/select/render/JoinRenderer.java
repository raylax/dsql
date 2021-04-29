package org.inurl.dsql.select.render;

import static org.inurl.dsql.util.StringUtilities.spaceAfter;
import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.Objects;
import java.util.stream.Collectors;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.select.QueryExpressionModel;
import org.inurl.dsql.select.join.JoinCriterion;
import org.inurl.dsql.select.join.JoinSpecification;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.FragmentCollector;
import org.inurl.dsql.util.StringUtilities;
import org.inurl.dsql.select.join.JoinModel;

public class JoinRenderer {
    private final JoinModel joinModel;
    private final QueryExpressionModel queryExpression;
    private final TableExpressionRenderer tableExpressionRenderer;

    private JoinRenderer(Builder builder) {
        joinModel = Objects.requireNonNull(builder.joinModel);
        queryExpression = Objects.requireNonNull(builder.queryExpression);
        tableExpressionRenderer = Objects.requireNonNull(builder.tableExpressionRenderer);
    }

    public FragmentAndParameters render() {
        FragmentCollector fc = joinModel.mapJoinSpecifications(this::renderJoinSpecification)
                .collect(FragmentCollector.collect());

        return FragmentAndParameters.withFragment(fc.fragments().collect(Collectors.joining(" ")))
                .withParameters(fc.parameters())
                .build();
    }

    private FragmentAndParameters renderJoinSpecification(JoinSpecification joinSpecification) {
        FragmentAndParameters renderedTable = joinSpecification.table().accept(tableExpressionRenderer);

        String fragment = StringUtilities.spaceAfter(joinSpecification.joinType().shortType())
                + "join"
                + StringUtilities.spaceBefore(renderedTable.fragment())
                + StringUtilities.spaceBefore(renderConditions(joinSpecification));

        return FragmentAndParameters.withFragment(fragment)
                .withParameters(renderedTable.parameters())
                .build();
    }

    private String renderConditions(JoinSpecification joinSpecification) {
        return joinSpecification.mapJoinCriteria(this::renderCriterion)
                .collect(Collectors.joining(" "));
    }

    private String renderCriterion(JoinCriterion joinCriterion) {
        return joinCriterion.connector()
                + StringUtilities.spaceBefore(applyTableAlias(joinCriterion.leftColumn()))
                + StringUtilities.spaceBefore(joinCriterion.operator())
                + StringUtilities.spaceBefore(applyTableAlias(joinCriterion.rightColumn()));
    }

    private String applyTableAlias(BasicColumn column) {
        return column.renderWithTableAlias(queryExpression.tableAliasCalculator());
    }

    public static Builder withJoinModel(JoinModel joinModel) {
        return new Builder().withJoinModel(joinModel);
    }

    public static class Builder {
        private JoinModel joinModel;
        private QueryExpressionModel queryExpression;
        private TableExpressionRenderer tableExpressionRenderer;

        public Builder withJoinModel(JoinModel joinModel) {
            this.joinModel = joinModel;
            return this;
        }

        public Builder withQueryExpression(QueryExpressionModel queryExpression) {
            this.queryExpression = queryExpression;
            return this;
        }

        public Builder withTableExpressionRenderer(TableExpressionRenderer tableExpressionRenderer) {
            this.tableExpressionRenderer = tableExpressionRenderer;
            return this;
        }

        public JoinRenderer build() {
            return new JoinRenderer(this);
        }
    }
}
