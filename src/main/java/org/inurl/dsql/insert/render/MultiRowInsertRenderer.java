package org.inurl.dsql.insert.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.inurl.dsql.insert.MultiRowInsertModel;
import org.inurl.dsql.render.RenderingStrategy;

public class MultiRowInsertRenderer<T> {

    private final MultiRowInsertModel<T> model;
    private final RenderingStrategy renderingStrategy;

    private MultiRowInsertRenderer(Builder<T> builder) {
        model = Objects.requireNonNull(builder.model);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public MultiRowInsertStatementProvider<T> render() {
        // the prefix is a generic format that will be resolved below with String.format(...)
        MultiRowValuePhraseVisitor visitor =
                new MultiRowValuePhraseVisitor(renderingStrategy, "records[%s]");
        List<FieldAndValue> fieldsAndValues = model
                .mapColumnMappings(m -> m.accept(visitor))
                .collect(Collectors.toList());

        return new DefaultMultiRowInsertStatementProvider.Builder<T>().withRecords(model.records())
                .withInsertStatement(calculateInsertStatement(fieldsAndValues))
                .build();
    }

    private String calculateInsertStatement(List<FieldAndValue> fieldsAndValues) {
        return "insert into"
                + spaceBefore(model.table().tableNameAtRuntime())
                + spaceBefore(calculateColumnsPhrase(fieldsAndValues))
                + spaceBefore(calculateMultiRowInsertValuesPhrase(fieldsAndValues, model.recordCount()));
    }

    private String calculateColumnsPhrase(List<FieldAndValue> fieldsAndValues) {
        return fieldsAndValues.stream()
                .map(FieldAndValue::fieldName)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private String calculateMultiRowInsertValuesPhrase(List<FieldAndValue> fieldsAndValues, int rowCount) {
        return IntStream.range(0, rowCount)
                .mapToObj(i -> toSingleRowOfValues(fieldsAndValues, i))
                .collect(Collectors.joining(", ", "values ", ""));
    }

    private String toSingleRowOfValues(List<FieldAndValue> fieldsAndValues, int row) {
        return fieldsAndValues.stream()
                .map(FieldAndValue::valuePhrase)
                .map(s -> String.format(s, row))
                .collect(Collectors.joining(", ", "(", ")"));
    }

    public static <T> Builder<T> withMultiRowInsertModel(MultiRowInsertModel<T> model) {
        return new Builder<T>().withMultiRowInsertModel(model);
    }

    public static class Builder<T> {
        private MultiRowInsertModel<T> model;
        private RenderingStrategy renderingStrategy;

        public Builder<T> withMultiRowInsertModel(MultiRowInsertModel<T> model) {
            this.model = model;
            return this;
        }

        public Builder<T> withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public MultiRowInsertRenderer<T> build() {
            return new MultiRowInsertRenderer<>(this);
        }
    }
}
