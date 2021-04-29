package org.inurl.dsql.render;

import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.BindableColumn;

public abstract class RenderingStrategy {
    public static final String DEFAULT_PARAMETER_PREFIX = "parameters";

    public static String formatParameterMapKey(AtomicInteger sequence) {
        return "p" + sequence.getAndIncrement();
    }

    public abstract String getFormattedJdbcPlaceholder(BindableColumn<?> column, String prefix, String parameterName);

    public abstract String getFormattedJdbcPlaceholder(String prefix, String parameterName);

    public String getMultiRowFormattedJdbcPlaceholder(BindableColumn<?> column, String prefix, String parameterName) {
        return getFormattedJdbcPlaceholder(column, prefix, parameterName);
    }
}
