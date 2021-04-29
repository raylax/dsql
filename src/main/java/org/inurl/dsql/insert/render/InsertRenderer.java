package org.inurl.dsql.insert.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.inurl.dsql.insert.InsertModel;
import org.inurl.dsql.render.RenderingStrategy;

public class InsertRenderer<T> {

    private final InsertModel<T> model;
    private final RenderingStrategy renderingStrategy;

    private InsertRenderer(Builder<T> builder) {
        model = Objects.requireNonNull(builder.model);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public InsertStatementProvider<T> render() {
        ValuePhraseVisitor visitor = new ValuePhraseVisitor(renderingStrategy);

        List<Optional<FieldAndValue>> fieldsAndValues = model.mapColumnMappings(m -> m.accept(visitor))
                .collect(Collectors.toList());

        return DefaultInsertStatementProvider.withRecord(model.record())
                .withInsertStatement(calculateInsertStatement(fieldsAndValues))
                .build();
    }

    private String calculateInsertStatement(List<Optional<FieldAndValue>> fieldsAndValues) {
        return "insert into"
                + spaceBefore(model.table().tableNameAtRuntime())
                + spaceBefore(calculateColumnsPhrase(fieldsAndValues))
                + spaceBefore(calculateValuesPhrase(fieldsAndValues));
    }

    private String calculateColumnsPhrase(List<Optional<FieldAndValue>> fieldsAndValues) {
        return fieldsAndValues.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FieldAndValue::fieldName)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private String calculateValuesPhrase(List<Optional<FieldAndValue>> fieldsAndValues) {
        return fieldsAndValues.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FieldAndValue::valuePhrase)
                .collect(Collectors.joining(", ", "values (", ")"));
    }

    public static <T> Builder<T> withInsertModel(InsertModel<T> model) {
        return new Builder<T>().withInsertModel(model);
    }

    public static class Builder<T> {
        private InsertModel<T> model;
        private RenderingStrategy renderingStrategy;

        public Builder<T> withInsertModel(InsertModel<T> model) {
            this.model = model;
            return this;
        }

        public Builder<T> withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public InsertRenderer<T> build() {
            return new InsertRenderer<>(this);
        }
    }
}
