package org.inurl.dsql.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.update.render.UpdateStatementProvider;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.render.RenderingStrategy;
import org.inurl.dsql.update.render.UpdateRenderer;
import org.inurl.dsql.util.AbstractColumnMapping;
import org.inurl.dsql.where.WhereModel;

public class UpdateModel {
    private final SqlTable table;
    private final WhereModel whereModel;
    private final List<AbstractColumnMapping> columnMappings;

    private UpdateModel(Builder builder) {
        table = Objects.requireNonNull(builder.table);
        whereModel = builder.whereModel;
        columnMappings = Objects.requireNonNull(builder.columnMappings);
    }

    public SqlTable table() {
        return table;
    }

    public Optional<WhereModel> whereModel() {
        return Optional.ofNullable(whereModel);
    }

    public <R> Stream<R> mapColumnMappings(Function<AbstractColumnMapping, R> mapper) {
        return columnMappings.stream().map(mapper);
    }

    @NotNull
    public UpdateStatementProvider render(RenderingStrategy renderingStrategy) {
        return UpdateRenderer.withUpdateModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static Builder withTable(SqlTable table) {
        return new Builder().withTable(table);
    }

    public static class Builder {
        private SqlTable table;
        private WhereModel whereModel;
        private final List<AbstractColumnMapping> columnMappings = new ArrayList<>();

        public Builder withTable(SqlTable table) {
            this.table = table;
            return this;
        }

        public Builder withColumnMappings(List<AbstractColumnMapping> columnMappings) {
            this.columnMappings.addAll(columnMappings);
            return this;
        }

        public Builder withWhereModel(WhereModel whereModel) {
            this.whereModel = whereModel;
            return this;
        }

        public UpdateModel build() {
            return new UpdateModel(this);
        }
    }
}
