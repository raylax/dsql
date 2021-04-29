package org.inurl.dsql.render;

import org.inurl.dsql.BindableColumn;

public class SpringNamedParameterRenderingStrategy extends RenderingStrategy {

    @Override
    public String getFormattedJdbcPlaceholder(BindableColumn<?> column, String prefix, String parameterName) {
        return getFormattedJdbcPlaceholder(prefix, parameterName);
    }

    @Override
    public String getFormattedJdbcPlaceholder(String prefix, String parameterName) {
        return ":" + parameterName;
    }

    @Override
    public String getMultiRowFormattedJdbcPlaceholder(BindableColumn<?> column, String prefix, String parameterName) {
        return ":" + prefix + "." + parameterName;
    }
}
