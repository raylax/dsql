package org.inurl.dsql.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.inurl.dsql.SqlTable;
import org.inurl.dsql.TableExpression;
import org.inurl.dsql.select.join.JoinCriterion;
import org.inurl.dsql.select.join.JoinModel;
import org.inurl.dsql.select.join.JoinSpecification;
import org.inurl.dsql.select.join.JoinType;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.where.AbstractWhereDSL;
import org.inurl.dsql.where.AbstractWhereSupport;

public abstract class AbstractQueryExpressionDSL<W extends AbstractWhereDSL<?>,
            T extends AbstractQueryExpressionDSL<W, T>>
        extends AbstractWhereSupport<W> {

    private final List<JoinSpecification.Builder> joinSpecificationBuilders = new ArrayList<>();
    protected final Map<SqlTable, String> tableAliases = new HashMap<>();
    private final TableExpression table;

    protected AbstractQueryExpressionDSL(TableExpression table) {
        this.table = Objects.requireNonNull(table);
    }

    public TableExpression table() {
        return table;
    }

    public T join(SqlTable joinTable, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.INNER, Arrays.asList(andJoinCriteria));
        return getThis();
    }

    public T join(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return join(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T join(SqlTable joinTable, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.INNER, andJoinCriteria);
        return getThis();
    }

    public T join(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return join(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T join(Buildable<SelectModel> subQuery, String tableAlias, JoinCriterion onJoinCriterion,
                  List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(buildSubQuery(subQuery, tableAlias), onJoinCriterion, JoinType.INNER,
                andJoinCriteria);
        return getThis();
    }

    public T leftJoin(SqlTable joinTable, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.LEFT, Arrays.asList(andJoinCriteria));
        return getThis();
    }

    public T leftJoin(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return leftJoin(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T leftJoin(SqlTable joinTable, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.LEFT, andJoinCriteria);
        return getThis();
    }

    public T leftJoin(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return leftJoin(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T leftJoin(Buildable<SelectModel> subQuery, String tableAlias, JoinCriterion onJoinCriterion,
                      List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(buildSubQuery(subQuery, tableAlias), onJoinCriterion, JoinType.LEFT,
                andJoinCriteria);
        return getThis();
    }

    public T rightJoin(SqlTable joinTable, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.RIGHT, Arrays.asList(andJoinCriteria));
        return getThis();
    }

    public T rightJoin(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return rightJoin(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T rightJoin(SqlTable joinTable, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.RIGHT, andJoinCriteria);
        return getThis();
    }

    public T rightJoin(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return rightJoin(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T rightJoin(Buildable<SelectModel> subQuery, String tableAlias, JoinCriterion onJoinCriterion,
                      List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(buildSubQuery(subQuery, tableAlias), onJoinCriterion, JoinType.RIGHT,
                andJoinCriteria);
        return getThis();
    }

    public T fullJoin(SqlTable joinTable, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.FULL, Arrays.asList(andJoinCriteria));
        return getThis();
    }

    public T fullJoin(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            JoinCriterion...andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return fullJoin(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T fullJoin(SqlTable joinTable, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(joinTable, onJoinCriterion, JoinType.FULL, andJoinCriteria);
        return getThis();
    }

    public T fullJoin(SqlTable joinTable, String tableAlias, JoinCriterion onJoinCriterion,
            List<JoinCriterion> andJoinCriteria) {
        tableAliases.put(joinTable, tableAlias);
        return fullJoin(joinTable, onJoinCriterion, andJoinCriteria);
    }

    public T fullJoin(Buildable<SelectModel> subQuery, String tableAlias, JoinCriterion onJoinCriterion,
                  List<JoinCriterion> andJoinCriteria) {
        addJoinSpecificationBuilder(buildSubQuery(subQuery, tableAlias), onJoinCriterion, JoinType.FULL,
                andJoinCriteria);
        return getThis();
    }

    private void addJoinSpecificationBuilder(TableExpression joinTable, JoinCriterion onJoinCriterion,
            JoinType joinType, List<JoinCriterion> andJoinCriteria) {
        joinSpecificationBuilders.add(new JoinSpecification.Builder()
                .withJoinTable(joinTable)
                .withJoinType(joinType)
                .withJoinCriterion(onJoinCriterion)
                .withJoinCriteria(andJoinCriteria));
    }

    protected void addJoinSpecificationBuilder(JoinSpecification.Builder builder) {
        joinSpecificationBuilders.add(builder);
    }

    protected Optional<JoinModel> buildJoinModel() {
        if (joinSpecificationBuilders.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(JoinModel.of(joinSpecificationBuilders.stream()
                .map(JoinSpecification.Builder::build)
                .collect(Collectors.toList())));
    }

    protected static SubQuery buildSubQuery(Buildable<SelectModel> selectModel) {
        return new SubQuery.Builder()
                .withSelectModel(selectModel.build())
                .build();
    }

    protected static SubQuery buildSubQuery(Buildable<SelectModel> selectModel, String alias) {
        return new SubQuery.Builder()
                .withSelectModel(selectModel.build())
                .withAlias(alias)
                .build();
    }

    protected abstract T getThis();
}
