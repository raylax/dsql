package org.inurl.dsql.select.function;

import java.util.Arrays;
import java.util.List;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.BindableColumn;

public class Multiply<T> extends OperatorFunction<T> {

    private Multiply(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                     List<BasicColumn> subsequentColumns) {
        super("*", firstColumn, secondColumn, subsequentColumns);
    }

    @Override
    protected Multiply<T> copy() {
        return new Multiply<>(column, secondColumn, subsequentColumns);
    }

    public static <T> Multiply<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                     BasicColumn... subsequentColumns) {
        return of(firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }

    public static <T> Multiply<T> of(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        return new Multiply<>(firstColumn, secondColumn, subsequentColumns);
    }
}
