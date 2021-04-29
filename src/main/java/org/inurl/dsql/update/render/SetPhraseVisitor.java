package org.inurl.dsql.update.render;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.select.render.SelectStatementProvider;
import org.inurl.dsql.util.ColumnToColumnMapping;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.util.StringConstantMapping;
import org.inurl.dsql.util.UpdateMappingVisitor;
import org.inurl.dsql.util.ValueMapping;
import org.inurl.dsql.util.ValueOrNullMapping;
import org.inurl.dsql.util.ValueWhenPresentMapping;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.select.render.SelectRenderer;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;
import org.inurl.dsql.util.SelectMapping;

public class SetPhraseVisitor extends UpdateMappingVisitor<Optional<FragmentAndParameters>> {

    private final AtomicInteger sequence;
    private final RenderingStrategy renderingStrategy;

    public SetPhraseVisitor(AtomicInteger sequence, RenderingStrategy renderingStrategy) {
        this.sequence = Objects.requireNonNull(sequence);
        this.renderingStrategy = Objects.requireNonNull(renderingStrategy);
    }

    @Override
    public Optional<FragmentAndParameters> visit(NullMapping mapping) {
        return FragmentAndParameters.withFragment(mapping.columnName() + " = null")
                .buildOptional();
    }

    @Override
    public Optional<FragmentAndParameters> visit(ConstantMapping mapping) {
        String fragment = mapping.columnName() + " = " + mapping.constant();
        return FragmentAndParameters.withFragment(fragment)
                .buildOptional();
    }

    @Override
    public Optional<FragmentAndParameters> visit(StringConstantMapping mapping) {
        String fragment = mapping.columnName()
                + " = '"
                + mapping.constant()
                + "'";

        return FragmentAndParameters.withFragment(fragment)
                .buildOptional();
    }

    @Override
    public <T> Optional<FragmentAndParameters> visit(ValueMapping<T> mapping) {
        return buildFragment(mapping, mapping.value());
    }

    @Override
    public <T> Optional<FragmentAndParameters> visit(ValueOrNullMapping<T> mapping) {
        return mapping.value()
                .map(v -> buildFragment(mapping, v))
                .orElseGet(() -> FragmentAndParameters
                        .withFragment(mapping.columnName() + " = null")
                        .buildOptional()
                );
    }

    @Override
    public <T> Optional<FragmentAndParameters> visit(ValueWhenPresentMapping<T> mapping) {
        return mapping.value().flatMap(v -> buildFragment(mapping, v));
    }

    @Override
    public Optional<FragmentAndParameters> visit(SelectMapping mapping) {
        SelectStatementProvider selectStatement = SelectRenderer.withSelectModel(mapping.selectModel())
                .withRenderingStrategy(renderingStrategy)
                .withSequence(sequence)
                .build()
                .render();

        String fragment = mapping.columnName()
                + " = ("
                + selectStatement.getSelectStatement()
                + ")";

        return FragmentAndParameters.withFragment(fragment)
                .withParameters(selectStatement.getParameters())
                .buildOptional();
    }

    @Override
    public Optional<FragmentAndParameters> visit(ColumnToColumnMapping mapping) {
        String setPhrase = mapping.columnName()
                + " = " 
                + mapping.rightColumn().renderWithTableAlias(TableAliasCalculator.empty());

        return FragmentAndParameters.withFragment(setPhrase)
                .buildOptional();
    }

    private <T> Optional<FragmentAndParameters> buildFragment(AbstractColumnMapping mapping, T value) {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);

        String jdbcPlaceholder = mapping.mapColumn(c -> calculateJdbcPlaceholder(c, mapKey));
        String setPhrase = mapping.columnName()
                + " = " 
                + jdbcPlaceholder;

        return FragmentAndParameters.withFragment(setPhrase)
                .withParameter(mapKey, value)
                .buildOptional();
    }

    private String calculateJdbcPlaceholder(SqlColumn<?> column, String parameterName) {
        return column.renderingStrategy().orElse(renderingStrategy)
                .getFormattedJdbcPlaceholder(column, RenderingStrategy.DEFAULT_PARAMETER_PREFIX, parameterName);
    }
}
