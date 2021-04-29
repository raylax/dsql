package org.inurl.dsql.insert.render;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.util.MultiRowInsertMappingVisitor;
import org.inurl.dsql.util.PropertyMapping;
import org.inurl.dsql.util.StringConstantMapping;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.util.ConstantMapping;
import org.inurl.dsql.util.NullMapping;

public abstract class AbstractMultiRowValuePhraseVisitor extends MultiRowInsertMappingVisitor<FieldAndValue> {

    protected final RenderingStrategy renderingStrategy;
    protected final String prefix;

    protected AbstractMultiRowValuePhraseVisitor(RenderingStrategy renderingStrategy, String prefix) {
        this.renderingStrategy = renderingStrategy;
        this.prefix = prefix;
    }

    @Override
    public FieldAndValue visit(NullMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase("null")
                .build();
    }

    @Override
    public FieldAndValue visit(ConstantMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase(mapping.constant())
                .build();
    }

    @Override
    public FieldAndValue visit(StringConstantMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase("'" + mapping.constant() + "'")
                .build();
    }

    @Override
    public FieldAndValue visit(PropertyMapping mapping) {
        return FieldAndValue.withFieldName(mapping.columnName())
                .withValuePhrase(mapping.mapColumn(c -> calculateJdbcPlaceholder(c, mapping.property())))
                .build();
    }

    abstract String calculateJdbcPlaceholder(SqlColumn<?> column, String parameterName);
}
