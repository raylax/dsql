package org.inurl.dsql.insert;

import java.util.Collection;

import org.inurl.dsql.insert.render.MultiRowInsertRenderer;
import org.inurl.dsql.insert.render.MultiRowInsertStatementProvider;
import org.inurl.dsql.render.RenderingStrategy;
import org.jetbrains.annotations.NotNull;

public class MultiRowInsertModel<T> extends AbstractMultiRowInsertModel<T> {

    private MultiRowInsertModel(Builder<T> builder) {
        super(builder);
    }

    @NotNull
    public MultiRowInsertStatementProvider<T> render(RenderingStrategy renderingStrategy) {
        return MultiRowInsertRenderer.withMultiRowInsertModel(this)
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

        public MultiRowInsertModel<T> build() {
            return new MultiRowInsertModel<>(this);
        }
    }
}
