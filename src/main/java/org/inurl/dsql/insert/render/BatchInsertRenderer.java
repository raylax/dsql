package org.inurl.dsql.insert.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.inurl.dsql.insert.BatchInsertModel;
import org.inurl.dsql.render.RenderingStrategy;

public class BatchInsertRenderer<T> {

    private final BatchInsertModel<T> model;
    private final RenderingStrategy renderingStrategy;

    private BatchInsertRenderer(Builder<T> builder) {
        model = Objects.requireNonNull(builder.model);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public BatchInsert<T> render() {
        BatchValuePhraseVisitor visitor = new BatchValuePhraseVisitor(renderingStrategy, "record");
        List<FieldAndValue> fieldsAndValues = model
                .mapColumnMappings(m -> m.accept(visitor))
                .collect(Collectors.toList());

        return BatchInsert.withRecords(model.records())
                .withInsertStatement(calculateInsertStatement(fieldsAndValues))
                .build();
    }

    private String calculateInsertStatement(List<FieldAndValue> fieldsAndValues) {
        return "insert into"
                + spaceBefore(model.table().tableNameAtRuntime())
                + spaceBefore(calculateColumnsPhrase(fieldsAndValues))
                + spaceBefore(calculateValuesPhrase(fieldsAndValues));
    }

    private String calculateColumnsPhrase(List<FieldAndValue> fieldsAndValues) {
        return fieldsAndValues.stream()
                .map(FieldAndValue::fieldName)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private String calculateValuesPhrase(List<FieldAndValue> fieldsAndValues) {
        return fieldsAndValues.stream()
                .map(FieldAndValue::valuePhrase)
                .collect(Collectors.joining(", ", "values (", ")"));
    }

    public static <T> Builder<T> withBatchInsertModel(BatchInsertModel<T> model) {
        return new Builder<T>().withBatchInsertModel(model);
    }

    public static class Builder<T> {
        private BatchInsertModel<T> model;
        private RenderingStrategy renderingStrategy;

        public Builder<T> withBatchInsertModel(BatchInsertModel<T> model) {
            this.model = model;
            return this;
        }

        public Builder<T> withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public BatchInsertRenderer<T> build() {
            return new BatchInsertRenderer<>(this);
        }
    }
}
