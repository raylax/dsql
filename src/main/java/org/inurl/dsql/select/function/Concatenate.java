package org.inurl.dsql.select.function;

import java.util.Arrays;
import java.util.List;

import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.BindableColumn;

public class Concatenate<T> extends OperatorFunction<T> {

    protected Concatenate(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        super("||", firstColumn, secondColumn, subsequentColumns);
    }

    @Override
    protected Concatenate<T> copy() {
        return new Concatenate<>(column, secondColumn, subsequentColumns);
    }

    public static <T> Concatenate<T> concatenate(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            BasicColumn... subsequentColumns) {
        return new Concatenate<>(firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }
}
