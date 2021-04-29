package org.inurl.dsql.select.join;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.inurl.dsql.TableExpression;

public class JoinModel {
    private final List<JoinSpecification> joinSpecifications = new ArrayList<>();

    private JoinModel(List<JoinSpecification> joinSpecifications) {
        this.joinSpecifications.addAll(joinSpecifications);
    }

    public <R> Stream<R> mapJoinSpecifications(Function<JoinSpecification, R> mapper) {
        return joinSpecifications.stream().map(mapper);
    }

    public static JoinModel of(List<JoinSpecification> joinSpecifications) {
        return new JoinModel(joinSpecifications);
    }

    public boolean containsSubQueries() {
        return joinSpecifications.stream()
                .map(JoinSpecification::table)
                .anyMatch(TableExpression::isSubQuery);
    }
}
