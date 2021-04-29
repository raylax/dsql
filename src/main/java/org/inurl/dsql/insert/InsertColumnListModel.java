package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.SqlColumn;

public class InsertColumnListModel {
    private final List<SqlColumn<?>> columns = new ArrayList<>();

    private InsertColumnListModel(List<SqlColumn<?>> columns) {
        this.columns.addAll(columns);
    }

    public <R> Stream<R> mapColumns(Function<SqlColumn<?>, R> mapper) {
        return columns.stream().map(mapper);
    }

    public static InsertColumnListModel of(List<SqlColumn<?>> columns) {
        return new InsertColumnListModel(columns);
    }
}
