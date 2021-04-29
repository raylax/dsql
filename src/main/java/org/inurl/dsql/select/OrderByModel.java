package org.inurl.dsql.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.SortSpecification;

public class OrderByModel {
    private final List<SortSpecification> columns = new ArrayList<>();

    private OrderByModel(Collection<SortSpecification> columns) {
        this.columns.addAll(columns);
    }

    public <R> Stream<R> mapColumns(Function<SortSpecification, R> mapper) {
        return columns.stream().map(mapper);
    }

    public static OrderByModel of(Collection<SortSpecification> columns) {
        return new OrderByModel(columns);
    }
}
