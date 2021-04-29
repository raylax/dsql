package org.inurl.dsql.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.BasicColumn;

public class GroupByModel {
    private final List<BasicColumn> columns = new ArrayList<>();

    private GroupByModel(Collection<BasicColumn> columns) {
        this.columns.addAll(columns);
    }

    public <R> Stream<R> mapColumns(Function<BasicColumn, R> mapper) {
        return columns.stream().map(mapper);
    }

    public static GroupByModel of(Collection<BasicColumn> columns) {
        return new GroupByModel(columns);
    }
}
