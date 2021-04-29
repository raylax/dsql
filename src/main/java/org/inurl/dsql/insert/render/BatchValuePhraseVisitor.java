package org.inurl.dsql.insert.render;

import org.inurl.dsql.SqlColumn;
import org.inurl.dsql.render.RenderingStrategy;

public class BatchValuePhraseVisitor extends AbstractMultiRowValuePhraseVisitor {

    public BatchValuePhraseVisitor(RenderingStrategy renderingStrategy, String prefix) {
        super(renderingStrategy, prefix);
    }

    @Override
    String calculateJdbcPlaceholder(SqlColumn<?> column, String parameterName) {
        return column.renderingStrategy().orElse(renderingStrategy)
                .getFormattedJdbcPlaceholder(column, prefix, parameterName);
    }
}
