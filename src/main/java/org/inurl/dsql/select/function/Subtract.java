package org.inurl.dsql.select.function;

import java.util.Arrays;
import java.util.List;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.BindableColumn;

public class Subtract<T> extends OperatorFunction<T> {

    private Subtract(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                     List<BasicColumn> subsequentColumns) {
        super("-", firstColumn, secondColumn, subsequentColumns);
    }

    @Override
    protected Subtract<T> copy() {
        return new Subtract<>(column, secondColumn, subsequentColumns);
    }

    public static <T> Subtract<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            BasicColumn... subsequentColumns) {
        return of(firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }

    public static <T> Subtract<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                     List<BasicColumn> subsequentColumns) {
        return new Subtract<>(firstColumn, secondColumn, subsequentColumns);
    }
}
