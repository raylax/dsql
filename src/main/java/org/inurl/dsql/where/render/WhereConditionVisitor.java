package org.inurl.dsql.where.render;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.AbstractColumnComparisonCondition;
import org.inurl.dsql.AbstractListValueCondition;
import org.inurl.dsql.AbstractSingleValueCondition;
import org.inurl.dsql.AbstractSubselectCondition;
import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.ConditionVisitor;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.FragmentCollector;
import org.inurl.dsql.AbstractNoValueCondition;
import org.inurl.dsql.AbstractTwoValueCondition;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.render.SelectRenderer;

public class WhereConditionVisitor<T> implements ConditionVisitor<T, FragmentAndParameters> {

    private final RenderingStrategy renderingStrategy;
    private final AtomicInteger sequence;
    private final BindableColumn<T> column;
    private final TableAliasCalculator tableAliasCalculator;
    private final String parameterPrefix;

    private WhereConditionVisitor(Builder<T> builder) {
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
        sequence = Objects.requireNonNull(builder.sequence);
        column = Objects.requireNonNull(builder.column);
        tableAliasCalculator = Objects.requireNonNull(builder.tableAliasCalculator);
        parameterPrefix = Objects.requireNonNull(builder.parameterPrefix);
    }

    @Override
    public FragmentAndParameters visit(AbstractListValueCondition<T> condition) {
        FragmentCollector fc = condition.mapValues(this::toFragmentAndParameters)
                .collect(FragmentCollector.collect());

        return FragmentAndParameters.withFragment(condition.renderCondition(columnName(), fc.fragments()))
                .withParameters(fc.parameters())
                .build();
    }

    @Override
    public FragmentAndParameters visit(AbstractNoValueCondition<T> condition) {
        return FragmentAndParameters.withFragment(condition.renderCondition(columnName()))
                .build();
    }

    @Override
    public FragmentAndParameters visit(AbstractSingleValueCondition<T> condition) {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);
        String fragment = condition.renderCondition(columnName(),
                getFormattedJdbcPlaceholder(mapKey));

        return FragmentAndParameters.withFragment(fragment)
                .withParameter(mapKey, convertValue(condition.value()))
                .build();
    }

    @Override
    public FragmentAndParameters visit(AbstractTwoValueCondition<T> condition) {
        String mapKey1 = RenderingStrategy.formatParameterMapKey(sequence);
        String mapKey2 = RenderingStrategy.formatParameterMapKey(sequence);
        String fragment = condition.renderCondition(columnName(),
                getFormattedJdbcPlaceholder(mapKey1),
                getFormattedJdbcPlaceholder(mapKey2));

        return FragmentAndParameters.withFragment(fragment)
                .withParameter(mapKey1, convertValue(condition.value1()))
                .withParameter(mapKey2, convertValue(condition.value2()))
                .build();
    }


    @Override
    public FragmentAndParameters visit(AbstractSubselectCondition<T> condition) {
        SelectStatementProvider selectStatement = SelectRenderer.withSelectModel(condition.selectModel())
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build()
                .render();

        String fragment = condition.renderCondition(columnName(), selectStatement.getSelectStatement());

        return FragmentAndParameters.withFragment(fragment)
                .withParameters(selectStatement.getParameters())
                .build();
    }

    @Override
    public FragmentAndParameters visit(AbstractColumnComparisonCondition<T> condition) {
        String fragment = condition.renderCondition(columnName(), tableAliasCalculator);
        return FragmentAndParameters.withFragment(fragment).build();
    }

    private Object convertValue(T value) {
        return column.convertParameterType(value);
    }

    private FragmentAndParameters toFragmentAndParameters(T value) {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);

        return FragmentAndParameters.withFragment(getFormattedJdbcPlaceholder(mapKey))
                .withParameter(mapKey, convertValue(value))
                .build();
    }

    private String getFormattedJdbcPlaceholder(String mapKey) {
        return column.renderingStrategy().orElse(renderingStrategy)
                .getFormattedJdbcPlaceholder(column, parameterPrefix, mapKey);
    }

    private String columnName() {
        return column.renderWithTableAlias(tableAliasCalculator);
    }

    public static <T> Builder<T> withColumn(BindableColumn<T> column) {
        return new Builder<T>().withColumn(column);
    }

    public static class Builder<T> {
        private RenderingStrategy renderingStrategy;
        private AtomicInteger sequence;
        private BindableColumn<T> column;
        private TableAliasCalculator tableAliasCalculator;
        private String parameterPrefix = RenderingStrategy.DEFAULT_PARAMETER_PREFIX;

        public Builder<T> withSequence(AtomicInteger sequence) {
            this.sequence = sequence;
            return this;
        }

        public Builder<T> withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public Builder<T> withColumn(BindableColumn<T> column) {
            this.column = column;
            return this;
        }

        public Builder<T> withTableAliasCalculator(TableAliasCalculator tableAliasCalculator) {
            this.tableAliasCalculator = tableAliasCalculator;
            return this;
        }

        public Builder<T> withParameterName(String parameterName) {
            if (parameterName != null) {
                parameterPrefix = parameterName + "." + RenderingStrategy.DEFAULT_PARAMETER_PREFIX;
            }
            return this;
        }

        public WhereConditionVisitor<T> build() {
            return new WhereConditionVisitor<>(this);
        }
    }
}
