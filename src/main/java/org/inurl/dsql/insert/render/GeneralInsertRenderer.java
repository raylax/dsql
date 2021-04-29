package org.inurl.dsql.insert.render;

import static org.inurl.dsql.util.StringUtilities.spaceBefore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.inurl.dsql.insert.GeneralInsertModel;
import org.inurl.dsql.render.RenderingStrategy;

public class GeneralInsertRenderer {

    private final GeneralInsertModel model;
    private final RenderingStrategy renderingStrategy;

    private GeneralInsertRenderer(Builder builder) {
        model = Objects.requireNonNull(builder.model);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }

    public GeneralInsertStatementProvider render() {
        GeneralInsertValuePhraseVisitor visitor = new GeneralInsertValuePhraseVisitor(renderingStrategy);
        List<Optional<FieldAndValueAndParameters>> fieldsAndValues = model.mapColumnMappings(m -> m.accept(visitor))
                .collect(Collectors.toList());

        return DefaultGeneralInsertStatementProvider.withInsertStatement(calculateInsertStatement(fieldsAndValues))
                .withParameters(calculateParameters(fieldsAndValues))
                .build();
    }

    private String calculateInsertStatement(List<Optional<FieldAndValueAndParameters>> fieldsAndValues) {
        return "insert into"
                + spaceBefore(model.table().tableNameAtRuntime())
                + spaceBefore(calculateColumnsPhrase(fieldsAndValues))
                + spaceBefore(calculateValuesPhrase(fieldsAndValues));
    }

    private String calculateColumnsPhrase(List<Optional<FieldAndValueAndParameters>> fieldsAndValues) {
        return fieldsAndValues.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FieldAndValueAndParameters::fieldName)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private String calculateValuesPhrase(List<Optional<FieldAndValueAndParameters>> fieldsAndValues) {
        return fieldsAndValues.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FieldAndValueAndParameters::valuePhrase)
                .collect(Collectors.joining(", ", "values (", ")"));
    }

    private Map<String, Object> calculateParameters(List<Optional<FieldAndValueAndParameters>> fieldsAndValues) {
        return fieldsAndValues.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FieldAndValueAndParameters::parameters)
                .collect(HashMap::new, HashMap::putAll, HashMap::putAll);
    }

    public static Builder withInsertModel(GeneralInsertModel model) {
        return new Builder().withInsertModel(model);
    }

    public static class Builder {
        private GeneralInsertModel model;
        private RenderingStrategy renderingStrategy;

        public Builder withInsertModel(GeneralInsertModel model) {
            this.model = model;
            return this;
        }

        public Builder withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }

        public GeneralInsertRenderer build() {
            return new GeneralInsertRenderer(this);
        }
    }
}
