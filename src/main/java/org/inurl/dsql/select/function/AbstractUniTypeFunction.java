package org.inurl.dsql.select.function;

import java.sql.JDBCType;
import java.util.Optional;

import org.inurl.dsql.BindableColumn;

/**
 * Represents a function that does not change the underlying data type.
 *
 * @author Jeff Butler
 *
 * @param <T> The type of the underlying column
 * @param <U> the specific subtype that implements the function
 */
public abstract class AbstractUniTypeFunction<T, U extends AbstractUniTypeFunction<T, U>>
        extends AbstractTypeConvertingFunction<T, T, U> {

    protected AbstractUniTypeFunction(BindableColumn<T> column) {
        super(column);
    }

    @Override
    public Optional<JDBCType> jdbcType() {
        return column.jdbcType();
    }

    @Override
    public Optional<String> typeHandler() {
        return column.typeHandler();
    }
}
