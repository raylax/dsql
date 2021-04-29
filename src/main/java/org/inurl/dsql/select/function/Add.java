package org.inurl.dsql.select.function;

import java.util.Arrays;
import java.util.List;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.BindableColumn;

public class Add<T> extends OperatorFunction<T> {

    private Add(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                List<BasicColumn> subsequentColumns) {
        super("+", firstColumn, secondColumn, subsequentColumns);
    }

    @Override
    protected Add<T> copy() {
        return new Add<>(column, secondColumn, subsequentColumns);
    }

    public static <T> Add<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                BasicColumn... subsequentColumns) {
        return of(firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }

    public static <T> Add<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        return new Add<>(firstColumn, secondColumn, subsequentColumns);
    }
}
