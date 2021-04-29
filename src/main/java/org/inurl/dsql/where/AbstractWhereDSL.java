package org.inurl.dsql.where;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.ColumnAndConditionCriterion;
import org.inurl.dsql.ExistsCriterion;
import org.inurl.dsql.ExistsPredicate;
import org.jetbrains.annotations.NotNull;
import org.inurl.dsql.SqlCriterion;
import org.inurl.dsql.VisitableCondition;

public abstract class AbstractWhereDSL<T extends AbstractWhereDSL<T>> {
    private final List<SqlCriterion> criteria = new ArrayList<>();

    @NotNull
    public <S> T where(BindableColumn<S> column, VisitableCondition<S> condition) {
        criteria.add(ColumnAndConditionCriterion.withColumn(column)
                .withCondition(condition)
                .build());
        return getThis();
    }

    @NotNull
    public <S> T where(BindableColumn<S> column, VisitableCondition<S> condition, SqlCriterion...subCriteria) {
        return where(column, condition, Arrays.asList(subCriteria));
    }

    @NotNull
    public <S> T where(BindableColumn<S> column, VisitableCondition<S> condition, List<SqlCriterion> subCriteria) {
        criteria.add(ColumnAndConditionCriterion.withColumn(column)
                .withCondition(condition)
                .withSubCriteria(subCriteria)
                .build());
        return getThis();
    }

    @NotNull
    public T where(ExistsPredicate existsPredicate) {
        criteria.add(new ExistsCriterion.Builder()
                .withExistsPredicate(existsPredicate)
                .build());
        return getThis();
    }

    @NotNull
    public T where(ExistsPredicate existsPredicate, SqlCriterion...subCriteria) {
        return where(existsPredicate, Arrays.asList(subCriteria));
    }

    @NotNull
    public T where(ExistsPredicate existsPredicate, List<SqlCriterion> subCriteria) {
        criteria.add(new ExistsCriterion.Builder()
                .withExistsPredicate(existsPredicate)
                .withSubCriteria(subCriteria)
                .build());
        return getThis();
    }

    @NotNull
    public T applyWhere(WhereApplier whereApplier) {
        whereApplier.accept(this);
        return getThis();
    }

    @NotNull
    public <S> T and(BindableColumn<S> column, VisitableCondition<S> condition) {
        criteria.add(ColumnAndConditionCriterion.withColumn(column)
                .withConnector("and")
                .withCondition(condition)
                .build());
        return getThis();
    }

    @NotNull
    public <S> T and(BindableColumn<S> column, VisitableCondition<S> condition, SqlCriterion...subCriteria) {
        return and(column, condition, Arrays.asList(subCriteria));
    }

    @NotNull
    public <S> T and(BindableColumn<S> column, VisitableCondition<S> condition, List<SqlCriterion> subCriteria) {
        criteria.add(ColumnAndConditionCriterion.withColumn(column)
                .withConnector("and")
                .withCondition(condition)
                .withSubCriteria(subCriteria)
                .build());
        return getThis();
    }

    @NotNull
    public T and(ExistsPredicate existsPredicate) {
        criteria.add(new ExistsCriterion.Builder()
                .withConnector("and")
                .withExistsPredicate(existsPredicate)
                .build());
        return getThis();
    }

    @NotNull
    public T and(ExistsPredicate existsPredicate, SqlCriterion...subCriteria) {
        return and(existsPredicate, Arrays.asList(subCriteria));
    }

    @NotNull
    public T and(ExistsPredicate existsPredicate, List<SqlCriterion> subCriteria) {
        criteria.add(new ExistsCriterion.Builder()
                .withConnector("and")
                .withExistsPredicate(existsPredicate)
                .withSubCriteria(subCriteria)
                .build());
        return getThis();
    }

    @NotNull
    public <S> T or(BindableColumn<S> column, VisitableCondition<S> condition) {
        criteria.add(ColumnAndConditionCriterion.withColumn(column)
                .withConnector("or")
                .withCondition(condition)
                .build());
        return getThis();
    }

    @NotNull
    public <S> T or(BindableColumn<S> column, VisitableCondition<S> condition, SqlCriterion...subCriteria) {
        return or(column, condition, Arrays.asList(subCriteria));
    }

    @NotNull
    public <S> T or(BindableColumn<S> column, VisitableCondition<S> condition, List<SqlCriterion> subCriteria) {
        criteria.add(ColumnAndConditionCriterion.withColumn(column)
                .withConnector("or")
                .withCondition(condition)
                .withSubCriteria(subCriteria)
                .build());
        return getThis();
    }

    @NotNull
    public T or(ExistsPredicate existsPredicate) {
        criteria.add(new ExistsCriterion.Builder()
                .withConnector("or")
                .withExistsPredicate(existsPredicate)
                .build());
        return getThis();
    }

    @NotNull
    public T or(ExistsPredicate existsPredicate, SqlCriterion...subCriteria) {
        return or(existsPredicate, Arrays.asList(subCriteria));
    }

    @NotNull
    public T or(ExistsPredicate existsPredicate, List<SqlCriterion> subCriteria) {
        criteria.add(new ExistsCriterion.Builder()
                .withConnector("or")
                .withExistsPredicate(existsPredicate)
                .withSubCriteria(subCriteria)
                .build());
        return getThis();
    }

    protected WhereModel internalBuild() {
        return WhereModel.of(criteria);
    }

    protected abstract T getThis();
}
