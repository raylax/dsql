package org.inurl.dsql.select.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.inurl.dsql.render.TableAliasCalculator;
import org.inurl.dsql.BasicColumn;
import org.inurl.dsql.BindableColumn;

public class OperatorFunction<T> extends AbstractUniTypeFunction<T, OperatorFunction<T>> {

    protected final BasicColumn secondColumn;
    protected final List<BasicColumn> subsequentColumns = new ArrayList<>();
    private final String operator;

    protected OperatorFunction(String operator, BindableColumn<T> firstColumn, BasicColumn secondColumn,
            List<BasicColumn> subsequentColumns) {
        super(firstColumn);
        this.secondColumn = Objects.requireNonNull(secondColumn);
        this.subsequentColumns.addAll(subsequentColumns);
        this.operator = Objects.requireNonNull(operator);
    }

    @Override
    protected OperatorFunction<T> copy() {
        return new OperatorFunction<>(operator, column, secondColumn, subsequentColumns);
    }

    @Override
    public String renderWithTableAlias(TableAliasCalculator tableAliasCalculator) {
        String paddedOperator = " " + operator + " ";

        // note - the cast below is added for a type inference bug in the Java9 compiler.
        return Stream.of(Stream.of((BasicColumn) column), Stream.of(secondColumn), subsequentColumns.stream())
                .flatMap(Function.identity())
                .map(column -> column.renderWithTableAlias(tableAliasCalculator))
                .collect(Collectors.joining(paddedOperator, "(", ")"));
    }

    public static <T> OperatorFunction<T> of(String operator, BindableColumn<T> firstColumn, BasicColumn secondColumn,
            BasicColumn... subsequentColumns) {
        return of(operator, firstColumn, secondColumn, Arrays.asList(subsequentColumns));
    }

    public static <T> OperatorFunction<T> of(String operator, BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                             List<BasicColumn> subsequentColumns) {
        return new OperatorFunction<>(operator, firstColumn, secondColumn, subsequentColumns);
    }
}
