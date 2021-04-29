package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.insert.render.InsertRenderer;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.insert.render.InsertStatementProvider;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.util.AbstractColumnMapping;

public class InsertModel<T> {
    private final SqlTable table;
    private final T record;
    private final List<AbstractColumnMapping> columnMappings;

    private InsertModel(Builder<T> builder) {
        table = Objects.requireNonNull(builder.table);
        record = Objects.requireNonNull(builder.record);
        columnMappings = Objects.requireNonNull(builder.columnMappings);
    }

    public <R> Stream<R> mapColumnMappings(Function<AbstractColumnMapping, R> mapper) {
        return columnMappings.stream().map(mapper);
    }

    public T record() {
        return record;
    }

    public SqlTable table() {
        return table;
    }

    @NotNull
    public InsertStatementProvider<T> render(RenderingStrategy renderingStrategy) {
        return InsertRenderer.withInsertModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static <T> Builder<T> withRecord(T record) {
        return new Builder<T>().withRecord(record);
    }

    public static class Builder<T> {
        private SqlTable table;
        private T record;
        private final List<AbstractColumnMapping> columnMappings = new ArrayList<>();

        public Builder<T> withTable(SqlTable table) {
            this.table = table;
            return this;
        }

        public Builder<T> withRecord(T record) {
            this.record = record;
            return this;
        }

        public Builder<T> withColumnMappings(List<AbstractColumnMapping> columnMappings) {
            this.columnMappings.addAll(columnMappings);
            return this;
        }

        public InsertModel<T> build() {
            return new InsertModel<>(this);
        }
    }
}
