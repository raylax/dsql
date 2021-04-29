package org.inurl.dsql.select.function;

import java.util.Arrays;
import java.util.List;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.BindableColumn;

public class Divide<T> extends OperatorFunction<T> {

    private Divide(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        super("/", firstColumn, secondColumn, subsequentColumns);
    }

    @Override
    protected Divide<T> copy() {
        return new Divide<>(column, secondColumn, subsequentColumns);
    }

    public static <T> Divide<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                   BasicColumn... subsequentColumns) {
        return of(firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }

    public static <T> Divide<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        return new Divide<>(firstColumn, secondColumn, subsequentColumns);
    }
}
