package org.inurl.dsql.where.render;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.inurl.dsql.ColumnAndConditionCriterion;
import org.inurl.dsql.ExistsCriterion;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.FragmentCollector;
import org.inurl.dsql.ExistsPredicate;
import org.inurl.dsql.SqlCriterion;
import org.inurl.dsql.SqlCriterionVisitor;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.render.SelectRenderer;

/**
 * Renders a {@link SqlCriterion} to a {@link RenderedCriterion}. The process is complex because all conditions
 * may or may not be a candidate for rendering. For example, "isEqualWhenPresent" will not render when the value
 * is null. It is also complex because SqlCriterion may or may not include sub-criteria.
 *
 * <p>Rendering is a recursive process. The renderer will recurse into each sub-criteria - which may also
 * contain further sub-criteria - until all possible sub-criteria are rendered into a single fragment. So, for example,
 * the fragment may end up looking like:
 *
 * <pre>
 *     col1 = ? and (col2 = ? or (col3 = ? and col4 = ?))
 * </pre>
 *
 * <p>It is also possible that the end result will be empty if all criteria and sub-criteria are not valid for
 * rendering.
 *
 * @author Jeff Butler
 */
public class CriterionRenderer implements SqlCriterionVisitor<Optional<RenderedCriterion>> {
    private final AtomicInteger sequence;
    private final RenderingStrategy renderingStrategy;
    private final TableAliasCalculator tableAliasCalculator;
    private final String parameterName;

    private CriterionRenderer(Builder builder) {
        sequence = Objects.requireNonNull(builder.sequence);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
        tableAliasCalculator = Objects.requireNonNull(builder.tableAliasCalculator);
        parameterName = builder.parameterName;
    }

    @Override
    public <T> Optional<RenderedCriterion> visit(ColumnAndConditionCriterion<T> criterion) {
        if (criterion.condition().shouldRender()) {
            return renderWithInitialCondition(renderCondition(criterion), criterion);
        } else {
            criterion.condition().renderingSkipped();
            return renderWithoutInitialCondition(criterion);
        }
    }

    @Override
    public Optional<RenderedCriterion> visit(ExistsCriterion criterion) {
        ExistsPredicate existsPredicate = criterion.existsPredicate();

        SelectStatementProvider selectStatement = SelectRenderer
                .withSelectModel(existsPredicate.selectModelBuilder().build())
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build()
                .render();

        String fragment = existsPredicate.operator() + " ("
                + selectStatement.getSelectStatement() + ")";

        FragmentAndParameters initialCondition = FragmentAndParameters
                .withFragment(fragment)
                .withParameters(selectStatement.getParameters())
                .build();

        return renderWithInitialCondition(initialCondition, criterion);
    }

    private <T> FragmentAndParameters renderCondition(ColumnAndConditionCriterion<T> criterion) {
        WhereConditionVisitor<T> visitor = WhereConditionVisitor.withColumn(criterion.column())
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .withTableAliasCalculator(tableAliasCalculator)
                .withParameterName(parameterName)
                .build();
        return criterion.condition().accept(visitor);
    }

    private List<RenderedCriterion> renderSubCriteria(SqlCriterion criterion) {
        return criterion.mapSubCriteria(c -> c.accept(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<RenderedCriterion> renderWithoutInitialCondition(SqlCriterion criterion) {
        List<RenderedCriterion> subCriteria = renderSubCriteria(criterion);
        if (subCriteria.isEmpty()) {
            return Optional.empty();
        }

        return calculateRenderedCriterion(subCriteria, criterion);
    }

    private Optional<RenderedCriterion> renderWithInitialCondition(FragmentAndParameters initialCondition,
            SqlCriterion criterion) {
        List<RenderedCriterion> subCriteria = renderSubCriteria(criterion);
        if (subCriteria.isEmpty()) {
            return calculateRenderedCriterion(initialCondition, criterion);
        }

        return calculateRenderedCriterion(initialCondition, criterion, subCriteria);
    }

    private Optional<RenderedCriterion> calculateRenderedCriterion(FragmentAndParameters initialCondition,
            SqlCriterion criterion) {
        return fromFragmentAndParameters(initialCondition, criterion);
    }

    private Optional<RenderedCriterion> calculateRenderedCriterion(List<RenderedCriterion> subCriteria,
            SqlCriterion criterion) {
        return calculateRenderedCriterion(subCriteria.get(0).fragmentAndParameters(),
                criterion,
                subCriteria.subList(1, subCriteria.size()));
    }

    private Optional<RenderedCriterion> calculateRenderedCriterion(FragmentAndParameters initialCondition,
            SqlCriterion criterion,
            List<RenderedCriterion> subCriteria) {
        FragmentCollector fc = subCriteria.stream()
                .map(RenderedCriterion::fragmentAndParametersWithConnector)
                .collect(FragmentCollector.collect(initialCondition));
        return fromFragmentAndParameters(FragmentAndParameters.withFragment(calculateFragment(fc))
                .withParameters(fc.parameters())
                .build(), criterion);
    }

    private String calculateFragment(FragmentCollector collector) {
        if (collector.hasMultipleFragments()) {
            return collector.fragments()
                    .collect(Collectors.joining(" ", "(", ")"));
        } else {
            return collector.fragments().findFirst().orElse("");
        }
    }

    private Optional<RenderedCriterion> fromFragmentAndParameters(FragmentAndParameters fragmentAndParameters,
            SqlCriterion criterion) {
        RenderedCriterion.Builder builder = new RenderedCriterion.Builder()
                .withFragmentAndParameters(fragmentAndParameters);

        criterion.connector().ifPresent(builder::withConnector);

        return Optional.of(builder.build());
    }

    public static class Builder {
        private AtomicInteger sequence;
        private RenderingStrategy renderingStrategy;
        private TableAliasCalculator tableAliasCalculator;
        private String parameterName;

        public Builder withSequence(AtomicInteger sequence) {
            this.sequence = sequence;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public Builder withTableAliasCalculator(TableAliasCalculator tableAliasCalculator) {
            this.tableAliasCalculator = tableAliasCalculator;
            return this;
        }

        public Builder withParameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public CriterionRenderer build() {
            return new CriterionRenderer(this);
        }
    }
}
