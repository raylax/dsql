package org.inurl.dsql;

import java.sql.JDBCType;
import java.util.Optional;

import org.inurl.dsql.render.RenderingStrategy;

/**
 * Describes additional attributes of columns that are necessary for binding the column as a JDBC parameter.
 * Columns in where clauses are typically bound.
 *
 * @author Jeff Butler
 *
 * @param <T> - the Java type that corresponds to this column
*/
public interface BindableColumn<T> extends BasicColumn {

    /**
     * Override the base method definition to make it more specific to this interface.
     */
    @Override
    BindableColumn<T> as(String alias);

    default Optional<JDBCType> jdbcType() {
        return Optional.empty();
    }

    default Optional<String> typeHandler() {
        return Optional.empty();
    }

    default Optional<RenderingStrategy> renderingStrategy() {
        return Optional.empty();
    }

    default Object convertParameterType(T value) {
        return value;
    }
}
