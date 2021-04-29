package org.inurl.dsql.render;

import org.inurl.dsql.BindableColumn;

public class MyBatis3RenderingStrategy extends RenderingStrategy {
    @Override
    public String getFormattedJdbcPlaceholder(String prefix, String parameterName) {
        return "#{"
                + prefix
                + "."
                + parameterName
                + "}";
    }

    @Override
    public String getFormattedJdbcPlaceholder(BindableColumn<?> column, String prefix, String parameterName) {
        return "#{"
                + prefix
                + "."
                + parameterName
                + renderJdbcType(column)
                + renderTypeHandler(column)
                + "}";
    }

    private String renderTypeHandler(BindableColumn<?> column) {
        return column.typeHandler()
                .map(th -> ",typeHandler=" + th)
                .orElse("");
    }

    private String renderJdbcType(BindableColumn<?> column) {
        return column.jdbcType()
                .map(jt -> ",jdbcType=" + jt.getName())
                .orElse("");
    }
}
