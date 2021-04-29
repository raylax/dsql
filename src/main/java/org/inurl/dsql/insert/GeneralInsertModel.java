package org.inurl.dsql.insert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.insert.render.GeneralInsertRenderer;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.insert.render.GeneralInsertStatementProvider;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.util.AbstractColumnMapping;

public class GeneralInsertModel {

    private final SqlTable table;
    private final List<AbstractColumnMapping> insertMappings;

    private GeneralInsertModel(Builder builder) {
        table = Objects.requireNonNull(builder.table);
        insertMappings = builder.insertMappings;
    }

    public <R> Stream<R> mapColumnMappings(Function<AbstractColumnMapping, R> mapper) {
        return insertMappings.stream().map(mapper);
    }

    public SqlTable table() {
        return table;
    }

    @NotNull
    public GeneralInsertStatementProvider render(RenderingStrategy renderingStrategy) {
        return GeneralInsertRenderer.withInsertModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static class Builder {
        private SqlTable table;
        private final List<AbstractColumnMapping> insertMappings = new ArrayList<>();

        public Builder withTable(SqlTable table) {
            this.table = table;
            return this;
        }

        public Builder withInsertMappings(List<AbstractColumnMapping> insertMappings) {
            this.insertMappings.addAll(insertMappings);
            return this;
        }

        public GeneralInsertModel build() {
            return new GeneralInsertModel(this);
        }
    }
}
