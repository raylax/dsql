package org.inurl.dsql.insert;

import java.util.Collection;

import org.inurl.dsql.insert.render.BatchInsert;
import org.inurl.dsql.insert.render.BatchInsertRenderer;
import org.inurl.dsql.render.RenderingStrategy;
import org.jetbrains.annotations.NotNull;

public class BatchInsertModel<T> extends AbstractMultiRowInsertModel<T> {

    private BatchInsertModel(Builder<T> builder) {
        super(builder);
    }

    @NotNull
    public BatchInsert<T> render(RenderingStrategy renderingStrategy) {
        return BatchInsertRenderer.withBatchInsertModel(this)
                .withRenderingStrategy(renderingStrategy)
                .build()
                .render();
    }

    public static <T> Builder<T> withRecords(Collection<T> records) {
        return new Builder<T>().withRecords(records);
    }

    public static class Builder<T> extends AbstractBuilder<T, Builder<T>> {
        @Override
        protected Builder<T> getThis() {
            return this;
        }

        public BatchInsertModel<T> build() {
            return new BatchInsertModel<>(this);
        }
    }
}
