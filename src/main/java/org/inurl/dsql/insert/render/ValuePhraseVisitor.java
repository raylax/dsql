package org.inurl.dsql.insert.render;

import java.util.Optional;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.util.InsertMappingVisitor;
import org.inurl.dsql.util.PropertyMapping;
import org.inurl.dsql.util.StringConstantMapping;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;
import org.inurl.dsql.util.PropertyWhenPresentMapping;

public class ValuePhraseVisitor extends InsertMappingVisitor<Optional<FieldAndValue>> {

    protected final RenderingStrategy renderingStrategy;

    public ValuePhraseVisitor(RenderingStrategy renderingStrategy) {
        this.renderingStrategy = renderingStrategy;
    }

    @Override
    public Optional<FieldAndValue> visit(NullMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase("null")
                .buildOptional();
    }

    @Override
    public Optional<FieldAndValue> visit(ConstantMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase(mapping.constant())
                .buildOptional();
    }

    @Override
    public Optional<FieldAndValue> visit(StringConstantMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase("'" + mapping.constant() + "'")
                .buildOptional();
    }

    @Override
    public Optional<FieldAndValue> visit(PropertyMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase(mapping.mapColumn(c -> calculateJdbcPlaceholder(c, mapping.property())))
                .buildOptional();
    }

    @Override
    public Optional<FieldAndValue> visit(PropertyWhenPresentMapping mapping) {
        if (mapping.shouldRender()) {
            return visit((PropertyMapping) mapping);
        } else {
            return Optional.empty();
        }
    }

    private String calculateJdbcPlaceholder(SqlColumn<?> column, String parameterName) {
        return column.renderingStrategy().orElse(renderingStrategy)
                .getFormattedJdbcPlaceholder(column, "record", parameterName);
    }
}
