package org.inurl.dsql.insert.render;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.util.StringConstantMapping;
import org.inurl.dsql.util.ValueMapping;
import org.inurl.dsql.util.ValueOrNullMapping;
import org.inurl.dsql.util.ValueWhenPresentMapping;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.GeneralInsertMappingVisitor;
import org.inurl.dsql.util.NullMapping;

public class GeneralInsertValuePhraseVisitor extends GeneralInsertMappingVisitor<Optional<FieldAndValueAndParameters>> {

    private final RenderingStrategy renderingStrategy;
    private final AtomicInteger sequence = new AtomicInteger(1);

    public GeneralInsertValuePhraseVisitor(RenderingStrategy renderingStrategy) {
        this.renderingStrategy = renderingStrategy;
    }

    @Override
    public Optional<FieldAndValueAndParameters> visit(NullMapping mapping) {
        return buildNullFragment(mapping);
    }

    @Override
    public Optional<FieldAndValueAndParameters> visit(ConstantMapping mapping) {
        return FieldAndValueAndParameters.withFieldName(mapping.columnName())
                .withValuePhrase(mapping.constant())
                .buildOptional();
    }

    @Override
    public Optional<FieldAndValueAndParameters> visit(StringConstantMapping mapping) {
        return FieldAndValueAndParameters.withFieldName(mapping.columnName())
                .withValuePhrase("'" + mapping.constant() + "'")
                .buildOptional();
    }

    @Override
    public <T> Optional<FieldAndValueAndParameters> visit(ValueMapping<T> mapping) {
        return buildValueFragment(mapping, mapping.value());
    }

    @Override
    public <T> Optional<FieldAndValueAndParameters> visit(ValueOrNullMapping<T> mapping) {
        return mapping.value().map(v -> buildValueFragment(mapping, v))
                .orElseGet(() -> buildNullFragment(mapping));
    }

    @Override
    public <T> Optional<FieldAndValueAndParameters> visit(ValueWhenPresentMapping<T> mapping) {
        return mapping.value().flatMap(v -> buildValueFragment(mapping, v));
    }

    private Optional<FieldAndValueAndParameters> buildValueFragment(AbstractColumnMapping mapping,
            Object value) {
        return buildFragment(mapping, value);
    }

    private Optional<FieldAndValueAndParameters> buildNullFragment(AbstractColumnMapping mapping) {
        return FieldAndValueAndParameters.withFieldName(mapping.columnName())
                .withValuePhrase("null")
                .buildOptional();
    }

    private Optional<FieldAndValueAndParameters> buildFragment(AbstractColumnMapping mapping, Object value) {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);

        String jdbcPlaceholder = mapping.mapColumn(c -> calculateJdbcPlaceholder(c, mapKey));

        return FieldAndValueAndParameters.withFieldName(mapping.columnName())
                .withValuePhrase(jdbcPlaceholder)
                .withParameter(mapKey, value)
                .buildOptional();
    }

    private String calculateJdbcPlaceholder(SqlColumn<?> column, String parameterName) {
        return column.renderingStrategy().orElse(renderingStrategy)
                .getFormattedJdbcPlaceholder(column, RenderingStrategy.DEFAULT_PARAMETER_PREFIX, parameterName);
    }
}
