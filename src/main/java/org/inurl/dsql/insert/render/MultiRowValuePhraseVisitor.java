package org.inurl.dsql.insert.render;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.render.RenderingStrategy;

public class MultiRowValuePhraseVisitor extends AbstractMultiRowValuePhraseVisitor {

    public MultiRowValuePhraseVisitor(RenderingStrategy renderingStrategy, String prefix) {
        super(renderingStrategy, prefix);
    }

    @Override
    String calculateJdbcPlaceholder(SqlColumn<?> column, String parameterName) {
        return column.renderingStrategy().orElse(renderingStrategy)
                .getMultiRowFormattedJdbcPlaceholder(column, prefix, parameterName);
    }
}
