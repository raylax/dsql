package org.inurl.dsql;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.inurl.dsql.delete.DeleteDSL;
import org.inurl.dsql.delete.DeleteModel;
import org.inurl.dsql.insert.BatchInsertDSL;
import org.inurl.dsql.insert.GeneralInsertDSL;
import org.inurl.dsql.insert.InsertDSL;
import org.inurl.dsql.insert.InsertSelectDSL;
import org.inurl.dsql.insert.MultiRowInsertDSL;
import org.inurl.dsql.select.ColumnSortSpecification;
import org.inurl.dsql.select.QueryExpressionDSL;
import org.inurl.dsql.select.SelectDSL;
import org.inurl.dsql.select.SelectModel;
import org.inurl.dsql.select.SimpleSortSpecification;
import org.inurl.dsql.select.aggregate.Avg;
import org.inurl.dsql.select.aggregate.CountAll;
import org.inurl.dsql.select.aggregate.CountDistinct;
import org.inurl.dsql.select.aggregate.Max;
import org.inurl.dsql.select.aggregate.Min;
import org.inurl.dsql.select.aggregate.Sum;
import org.inurl.dsql.select.function.Concatenate;
import org.inurl.dsql.select.function.Divide;
import org.inurl.dsql.select.function.Lower;
import org.inurl.dsql.select.function.Multiply;
import org.inurl.dsql.select.function.OperatorFunction;
import org.inurl.dsql.select.function.Substring;
import org.inurl.dsql.select.function.Upper;
import org.inurl.dsql.select.join.JoinCondition;
import org.inurl.dsql.select.join.JoinCriterion;
import org.inurl.dsql.update.UpdateDSL;
import org.inurl.dsql.update.UpdateModel;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.where.WhereDSL;
import org.inurl.dsql.select.CountDSL;
import org.inurl.dsql.select.aggregate.Count;
import org.inurl.dsql.select.function.Add;
import org.inurl.dsql.select.function.Subtract;
import org.inurl.dsql.select.join.EqualTo;
import org.inurl.dsql.where.condition.IsBetween;
import org.inurl.dsql.where.condition.IsEqualTo;
import org.inurl.dsql.where.condition.IsEqualToColumn;
import org.inurl.dsql.where.condition.IsEqualToWithSubselect;
import org.inurl.dsql.where.condition.IsGreaterThan;
import org.inurl.dsql.where.condition.IsGreaterThanColumn;
import org.inurl.dsql.where.condition.IsGreaterThanOrEqualTo;
import org.inurl.dsql.where.condition.IsGreaterThanOrEqualToColumn;
import org.inurl.dsql.where.condition.IsGreaterThanOrEqualToWithSubselect;
import org.inurl.dsql.where.condition.IsGreaterThanWithSubselect;
import org.inurl.dsql.where.condition.IsIn;
import org.inurl.dsql.where.condition.IsInCaseInsensitive;
import org.inurl.dsql.where.condition.IsInWithSubselect;
import org.inurl.dsql.where.condition.IsLessThan;
import org.inurl.dsql.where.condition.IsLessThanColumn;
import org.inurl.dsql.where.condition.IsLessThanOrEqualTo;
import org.inurl.dsql.where.condition.IsLessThanOrEqualToColumn;
import org.inurl.dsql.where.condition.IsLessThanOrEqualToWithSubselect;
import org.inurl.dsql.where.condition.IsLessThanWithSubselect;
import org.inurl.dsql.where.condition.IsLike;
import org.inurl.dsql.where.condition.IsLikeCaseInsensitive;
import org.inurl.dsql.where.condition.IsNotBetween;
import org.inurl.dsql.where.condition.IsNotEqualTo;
import org.inurl.dsql.where.condition.IsNotEqualToColumn;
import org.inurl.dsql.where.condition.IsNotEqualToWithSubselect;
import org.inurl.dsql.where.condition.IsNotIn;
import org.inurl.dsql.where.condition.IsNotInCaseInsensitive;
import org.inurl.dsql.where.condition.IsNotInWithSubselect;
import org.inurl.dsql.where.condition.IsNotLike;
import org.inurl.dsql.where.condition.IsNotLikeCaseInsensitive;
import org.inurl.dsql.where.condition.IsNotNull;
import org.inurl.dsql.where.condition.IsNull;

public interface SqlBuilder {

    // statements

    /**
     * Renders as select count(distinct column) from table...
     *
     * @param column the column to count
     * @return the next step in the DSL
     */
    static CountDSL.FromGatherer<SelectModel> countDistinctColumn(BasicColumn column) {
        return CountDSL.countDistinct(column);
    }

    /**
     * Renders as select count(column) from table...
     *
     * @param column the column to count
     * @return the next step in the DSL
     */
    static CountDSL.FromGatherer<SelectModel> countColumn(BasicColumn column) {
        return CountDSL.count(column);
    }

    /**
     * Renders as select count(*) from table...
     *
     * @param table the table to count
     * @return the next step in the DSL
     */
    static CountDSL<SelectModel> countFrom(SqlTable table) {
        return CountDSL.countFrom(table);
    }

    static DeleteDSL<DeleteModel> deleteFrom(SqlTable table) {
        return DeleteDSL.deleteFrom(table);
    }

    static <T> InsertDSL.IntoGatherer<T> insert(T record) {
        return InsertDSL.insert(record);
    }

    /**
     * Insert a Batch of records. The model object is structured to support bulk inserts with JDBC batch support.
     *
     * @param records records to insert
     * @param <T> the type of record to insert
     * @return the next step in the DSL
     */
    @SafeVarargs
    static <T> BatchInsertDSL.IntoGatherer<T> insertBatch(T...records) {
        return BatchInsertDSL.insert(records);
    }

    /**
     * Insert a Batch of records. The model object is structured to support bulk inserts with JDBC batch support.
     *
     * @param records records to insert
     * @param <T> the type of record to insert
     * @return the next step in the DSL
     */
    static <T> BatchInsertDSL.IntoGatherer<T> insertBatch(Collection<T> records) {
        return BatchInsertDSL.insert(records);
    }

    /**
     * Insert multiple records in a single statement. The model object is structured as a single insert statement with
     * multiple values clauses. This statement is suitable for use with a small number of records. It is not suitable
     * for large bulk inserts as it is possible to exceed the limit of parameter markers in a prepared statement.
     *
     * <p>For large bulk inserts, see {@link SqlBuilder#insertBatch(Object[])}
     *
     * @param records records to insert
     * @param <T> the type of record to insert
     * @return the next step in the DSL
     */
    @SafeVarargs
    static <T> MultiRowInsertDSL.IntoGatherer<T> insertMultiple(T...records) {
        return MultiRowInsertDSL.insert(records);
    }

    /**
     * Insert multiple records in a single statement. The model object is structured as a single insert statement with
     * multiple values clauses. This statement is suitable for use with a small number of records. It is not suitable
     * for large bulk inserts as it is possible to exceed the limit of parameter markers in a prepared statement.
     *
     * <p>For large bulk inserts, see {@link SqlBuilder#insertBatch(Collection)}
     *
     * @param records records to insert
     * @param <T> the type of record to insert
     * @return the next step in the DSL
     */
    static <T> MultiRowInsertDSL.IntoGatherer<T> insertMultiple(Collection<T> records) {
        return MultiRowInsertDSL.insert(records);
    }

    static InsertIntoNextStep insertInto(SqlTable table) {
        return new InsertIntoNextStep(table);
    }

    static QueryExpressionDSL.FromGatherer<SelectModel> select(BasicColumn...selectList) {
        return SelectDSL.select(selectList);
    }

    static QueryExpressionDSL.FromGatherer<SelectModel> select(Collection<BasicColumn> selectList) {
        return SelectDSL.select(selectList);
    }

    static QueryExpressionDSL.FromGatherer<SelectModel> selectDistinct(BasicColumn...selectList) {
        return SelectDSL.selectDistinct(selectList);
    }

    static QueryExpressionDSL.FromGatherer<SelectModel> selectDistinct(Collection<BasicColumn> selectList) {
        return SelectDSL.selectDistinct(selectList);
    }

    static UpdateDSL<UpdateModel> update(SqlTable table) {
        return UpdateDSL.update(table);
    }

    static WhereDSL where() {
        return WhereDSL.where();
    }

    static <T> WhereDSL where(BindableColumn<T> column, VisitableCondition<T> condition) {
        return WhereDSL.where().where(column, condition);
    }

    static <T> WhereDSL where(BindableColumn<T> column, VisitableCondition<T> condition,
            SqlCriterion... subCriteria) {
        return WhereDSL.where().where(column, condition, subCriteria);
    }

    static WhereDSL where(ExistsPredicate existsPredicate) {
        return WhereDSL.where().where(existsPredicate);
    }

    static WhereDSL where(ExistsPredicate existsPredicate, SqlCriterion... subCriteria) {
        return WhereDSL.where().where(existsPredicate, subCriteria);
    }

    // where condition connectors
    static <T> SqlCriterion or(BindableColumn<T> column, VisitableCondition<T> condition) {
        return ColumnAndConditionCriterion.withColumn(column)
                .withConnector("or")
                .withCondition(condition)
                .build();
    }

    static <T> SqlCriterion or(BindableColumn<T> column, VisitableCondition<T> condition,
            SqlCriterion...subCriteria) {
        return ColumnAndConditionCriterion.withColumn(column)
                .withConnector("or")
                .withCondition(condition)
                .withSubCriteria(Arrays.asList(subCriteria))
                .build();
    }

    static SqlCriterion or(ExistsPredicate existsPredicate) {
        return new ExistsCriterion.Builder()
                .withConnector("or")
                .withExistsPredicate(existsPredicate)
                .build();
    }

    static SqlCriterion or(ExistsPredicate existsPredicate, SqlCriterion...subCriteria) {
        return new ExistsCriterion.Builder()
                .withConnector("or")
                .withExistsPredicate(existsPredicate)
                .withSubCriteria(Arrays.asList(subCriteria))
                .build();
    }

    static <T> SqlCriterion and(BindableColumn<T> column, VisitableCondition<T> condition) {
        return ColumnAndConditionCriterion.withColumn(column)
                .withConnector("and")
                .withCondition(condition)
                .build();
    }

    static <T> SqlCriterion and(BindableColumn<T> column, VisitableCondition<T> condition,
            SqlCriterion...subCriteria) {
        return ColumnAndConditionCriterion.withColumn(column)
                .withConnector("and")
                .withCondition(condition)
                .withSubCriteria(Arrays.asList(subCriteria))
                .build();
    }

    static SqlCriterion and(ExistsPredicate existsPredicate) {
        return new ExistsCriterion.Builder()
                .withConnector("and")
                .withExistsPredicate(existsPredicate)
                .build();
    }

    static SqlCriterion and(ExistsPredicate existsPredicate, SqlCriterion...subCriteria) {
        return new ExistsCriterion.Builder()
                .withConnector("and")
                .withExistsPredicate(existsPredicate)
                .withSubCriteria(Arrays.asList(subCriteria))
                .build();
    }

    // join support
    static JoinCriterion and(BasicColumn joinColumn, JoinCondition joinCondition) {
        return new JoinCriterion.Builder()
                .withConnector("and")
                .withJoinColumn(joinColumn)
                .withJoinCondition(joinCondition)
                .build();
    }

    static JoinCriterion on(BasicColumn joinColumn, JoinCondition joinCondition) {
        return new JoinCriterion.Builder()
                .withConnector("on")
                .withJoinColumn(joinColumn)
                .withJoinCondition(joinCondition)
                .build();
    }

    static EqualTo equalTo(BasicColumn column) {
        return new EqualTo(column);
    }

    // aggregate support
    static CountAll count() {
        return new CountAll();
    }

    static Count count(BasicColumn column) {
        return Count.of(column);
    }

    static CountDistinct countDistinct(BasicColumn column) {
        return CountDistinct.of(column);
    }

    static <T> Max<T> max(BindableColumn<T> column) {
        return Max.of(column);
    }

    static <T> Min<T> min(BindableColumn<T> column) {
        return Min.of(column);
    }

    static <T> Avg<T> avg(BindableColumn<T> column) {
        return Avg.of(column);
    }

    static <T> Sum<T> sum(BindableColumn<T> column) {
        return Sum.of(column);
    }

    // constants
    static <T> Constant<T> constant(String constant) {
        return Constant.of(constant);
    }

    static StringConstant stringConstant(String constant) {
        return StringConstant.of(constant);
    }

    // functions
    static <T> Add<T> add(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            BasicColumn... subsequentColumns) {
        return Add.of(firstColumn, secondColumn, subsequentColumns);
    }

    static <T> Divide<T> divide(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                BasicColumn... subsequentColumns) {
        return Divide.of(firstColumn, secondColumn, subsequentColumns);
    }

    static <T> Multiply<T> multiply(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                    BasicColumn... subsequentColumns) {
        return Multiply.of(firstColumn, secondColumn, subsequentColumns);
    }

    static <T> Subtract<T> subtract(BindableColumn<T> firstColumn, BasicColumn secondColumn,
            BasicColumn... subsequentColumns) {
        return Subtract.of(firstColumn, secondColumn, subsequentColumns);
    }

    static <T> Concatenate<T> concatenate(BindableColumn<T> firstColumn, BasicColumn secondColumn,
                                          BasicColumn... subsequentColumns) {
        return Concatenate.concatenate(firstColumn, secondColumn, subsequentColumns);
    }

    static <T> OperatorFunction<T> applyOperator(String operator, BindableColumn<T> firstColumn,
                                                 BasicColumn secondColumn, BasicColumn... subsequentColumns) {
        return OperatorFunction.of(operator, firstColumn, secondColumn, subsequentColumns);
    }

    static <T> Lower<T> lower(BindableColumn<T> column) {
        return Lower.of(column);
    }

    static <T> Substring<T> substring(BindableColumn<T> column, int offset, int length) {
        return Substring.of(column, offset, length);
    }

    static <T> Upper<T> upper(BindableColumn<T> column) {
        return Upper.of(column);
    }

    // conditions for all data types
    static ExistsPredicate exists(Buildable<SelectModel> selectModelBuilder) {
        return ExistsPredicate.exists(selectModelBuilder);
    }

    static ExistsPredicate notExists(Buildable<SelectModel> selectModelBuilder) {
        return ExistsPredicate.notExists(selectModelBuilder);
    }

    static <T> IsNull<T> isNull() {
        return new IsNull<>();
    }

    static <T> IsNotNull<T> isNotNull() {
        return new IsNotNull<>();
    }

    static <T> IsEqualTo<T> isEqualTo(T value) {
        return IsEqualTo.of(value);
    }

    static <T> IsEqualTo<T> isEqualTo(Supplier<T> valueSupplier) {
        return isEqualTo(valueSupplier.get());
    }

    static <T> IsEqualToWithSubselect<T> isEqualTo(Buildable<SelectModel> selectModelBuilder) {
        return IsEqualToWithSubselect.of(selectModelBuilder);
    }

    static <T> IsEqualToColumn<T> isEqualTo(BasicColumn column) {
        return IsEqualToColumn.of(column);
    }

    static <T> IsEqualTo<T> isEqualToWhenPresent(T value) {
        return IsEqualTo.of(value).filter(Objects::nonNull);
    }

    static <T> IsEqualTo<T> isEqualToWhenPresent(Supplier<T> valueSupplier) {
        return isEqualToWhenPresent(valueSupplier.get());
    }

    static <T> IsNotEqualTo<T> isNotEqualTo(T value) {
        return IsNotEqualTo.of(value);
    }

    static <T> IsNotEqualTo<T> isNotEqualTo(Supplier<T> valueSupplier) {
        return isNotEqualTo(valueSupplier.get());
    }

    static <T> IsNotEqualToWithSubselect<T> isNotEqualTo(Buildable<SelectModel> selectModelBuilder) {
        return IsNotEqualToWithSubselect.of(selectModelBuilder);
    }

    static <T> IsNotEqualToColumn<T> isNotEqualTo(BasicColumn column) {
        return IsNotEqualToColumn.of(column);
    }

    static <T> IsNotEqualTo<T> isNotEqualToWhenPresent(T value) {
        return IsNotEqualTo.of(value).filter(Objects::nonNull);
    }

    static <T> IsNotEqualTo<T> isNotEqualToWhenPresent(Supplier<T> valueSupplier) {
        return isNotEqualToWhenPresent(valueSupplier.get());
    }

    static <T> IsGreaterThan<T> isGreaterThan(T value) {
        return IsGreaterThan.of(value);
    }

    static <T> IsGreaterThan<T> isGreaterThan(Supplier<T> valueSupplier) {
        return isGreaterThan(valueSupplier.get());
    }

    static <T> IsGreaterThanWithSubselect<T> isGreaterThan(Buildable<SelectModel> selectModelBuilder) {
        return IsGreaterThanWithSubselect.of(selectModelBuilder);
    }

    static <T> IsGreaterThanColumn<T> isGreaterThan(BasicColumn column) {
        return IsGreaterThanColumn.of(column);
    }

    static <T> IsGreaterThan<T> isGreaterThanWhenPresent(T value) {
        return IsGreaterThan.of(value).filter(Objects::nonNull);
    }

    static <T> IsGreaterThan<T> isGreaterThanWhenPresent(Supplier<T> valueSupplier) {
        return isGreaterThanWhenPresent(valueSupplier.get());
    }

    static <T> IsGreaterThanOrEqualTo<T> isGreaterThanOrEqualTo(T value) {
        return IsGreaterThanOrEqualTo.of(value);
    }

    static <T> IsGreaterThanOrEqualTo<T> isGreaterThanOrEqualTo(Supplier<T> valueSupplier) {
        return isGreaterThanOrEqualTo(valueSupplier.get());
    }

    static <T> IsGreaterThanOrEqualToWithSubselect<T> isGreaterThanOrEqualTo(
            Buildable<SelectModel> selectModelBuilder) {
        return IsGreaterThanOrEqualToWithSubselect.of(selectModelBuilder);
    }

    static <T> IsGreaterThanOrEqualToColumn<T> isGreaterThanOrEqualTo(BasicColumn column) {
        return IsGreaterThanOrEqualToColumn.of(column);
    }

    static <T> IsGreaterThanOrEqualTo<T> isGreaterThanOrEqualToWhenPresent(T value) {
        return IsGreaterThanOrEqualTo.of(value).filter(Objects::nonNull);
    }

    static <T> IsGreaterThanOrEqualTo<T> isGreaterThanOrEqualToWhenPresent(Supplier<T> valueSupplier) {
        return isGreaterThanOrEqualToWhenPresent(valueSupplier.get());
    }

    static <T> IsLessThan<T> isLessThan(T value) {
        return IsLessThan.of(value);
    }

    static <T> IsLessThan<T> isLessThan(Supplier<T> valueSupplier) {
        return isLessThan(valueSupplier.get());
    }

    static <T> IsLessThanWithSubselect<T> isLessThan(Buildable<SelectModel> selectModelBuilder) {
        return IsLessThanWithSubselect.of(selectModelBuilder);
    }

    static <T> IsLessThanColumn<T> isLessThan(BasicColumn column) {
        return IsLessThanColumn.of(column);
    }

    static <T> IsLessThan<T> isLessThanWhenPresent(T value) {
        return IsLessThan.of(value).filter(Objects::nonNull);
    }

    static <T> IsLessThan<T> isLessThanWhenPresent(Supplier<T> valueSupplier) {
        return isLessThanWhenPresent(valueSupplier.get());
    }

    static <T> IsLessThanOrEqualTo<T> isLessThanOrEqualTo(T value) {
        return IsLessThanOrEqualTo.of(value);
    }

    static <T> IsLessThanOrEqualTo<T> isLessThanOrEqualTo(Supplier<T> valueSupplier) {
        return isLessThanOrEqualTo(valueSupplier.get());
    }

    static <T> IsLessThanOrEqualToWithSubselect<T> isLessThanOrEqualTo(Buildable<SelectModel> selectModelBuilder) {
        return IsLessThanOrEqualToWithSubselect.of(selectModelBuilder);
    }

    static <T> IsLessThanOrEqualToColumn<T> isLessThanOrEqualTo(BasicColumn column) {
        return IsLessThanOrEqualToColumn.of(column);
    }

    static <T> IsLessThanOrEqualTo<T> isLessThanOrEqualToWhenPresent(T value) {
        return IsLessThanOrEqualTo.of(value).filter(Objects::nonNull);
    }

    static <T> IsLessThanOrEqualTo<T> isLessThanOrEqualToWhenPresent(Supplier<T> valueSupplier) {
        return isLessThanOrEqualToWhenPresent(valueSupplier.get());
    }

    @SafeVarargs
    static <T> IsIn<T> isIn(T...values) {
        return IsIn.of(values);
    }

    static <T> IsIn<T> isIn(Collection<T> values) {
        return IsIn.of(values);
    }

    static <T> IsInWithSubselect<T> isIn(Buildable<SelectModel> selectModelBuilder) {
        return IsInWithSubselect.of(selectModelBuilder);
    }

    @SafeVarargs
    static <T> IsIn<T> isInWhenPresent(T...values) {
        return IsIn.of(values).filter(Objects::nonNull);
    }

    static <T> IsIn<T> isInWhenPresent(Collection<T> values) {
        return values == null ? IsIn.empty() : IsIn.of(values).filter(Objects::nonNull);
    }

    @SafeVarargs
    static <T> IsNotIn<T> isNotIn(T...values) {
        return IsNotIn.of(values);
    }

    static <T> IsNotIn<T> isNotIn(Collection<T> values) {
        return IsNotIn.of(values);
    }

    static <T> IsNotInWithSubselect<T> isNotIn(Buildable<SelectModel> selectModelBuilder) {
        return IsNotInWithSubselect.of(selectModelBuilder);
    }

    @SafeVarargs
    static <T> IsNotIn<T> isNotInWhenPresent(T...values) {
        return IsNotIn.of(values).filter(Objects::nonNull);
    }

    static <T> IsNotIn<T> isNotInWhenPresent(Collection<T> values) {
        return values == null ? IsNotIn.empty() : IsNotIn.of(values).filter(Objects::nonNull);
    }

    static <T> IsBetween.Builder<T> isBetween(T value1) {
        return IsBetween.isBetween(value1);
    }

    static <T> IsBetween.Builder<T> isBetween(Supplier<T> valueSupplier1) {
        return isBetween(valueSupplier1.get());
    }

    static <T> IsBetween.WhenPresentBuilder<T> isBetweenWhenPresent(T value1) {
        return IsBetween.isBetweenWhenPresent(value1);
    }

    static <T> IsBetween.WhenPresentBuilder<T> isBetweenWhenPresent(Supplier<T> valueSupplier1) {
        return isBetweenWhenPresent(valueSupplier1.get());
    }

    static <T> IsNotBetween.Builder<T> isNotBetween(T value1) {
        return IsNotBetween.isNotBetween(value1);
    }

    static <T> IsNotBetween.Builder<T> isNotBetween(Supplier<T> valueSupplier1) {
        return isNotBetween(valueSupplier1.get());
    }

    static <T> IsNotBetween.WhenPresentBuilder<T> isNotBetweenWhenPresent(T value1) {
        return IsNotBetween.isNotBetweenWhenPresent(value1);
    }

    static <T> IsNotBetween.WhenPresentBuilder<T> isNotBetweenWhenPresent(Supplier<T> valueSupplier1) {
        return isNotBetweenWhenPresent(valueSupplier1.get());
    }

    // for string columns, but generic for columns with type handlers
    static <T> IsLike<T> isLike(T value) {
        return IsLike.of(value);
    }

    static <T> IsLike<T> isLike(Supplier<T> valueSupplier) {
        return isLike(valueSupplier.get());
    }

    static <T> IsLike<T> isLikeWhenPresent(T value) {
        return IsLike.of(value).filter(Objects::nonNull);
    }

    static <T> IsLike<T> isLikeWhenPresent(Supplier<T> valueSupplier) {
        return isLikeWhenPresent(valueSupplier.get());
    }

    static <T> IsNotLike<T> isNotLike(T value) {
        return IsNotLike.of(value);
    }

    static <T> IsNotLike<T> isNotLike(Supplier<T> valueSupplier) {
        return isNotLike(valueSupplier.get());
    }

    static <T> IsNotLike<T> isNotLikeWhenPresent(T value) {
        return IsNotLike.of(value).filter(Objects::nonNull);
    }

    static <T> IsNotLike<T> isNotLikeWhenPresent(Supplier<T> valueSupplier) {
        return isNotLikeWhenPresent(valueSupplier.get());
    }

    // shortcuts for booleans
    static IsEqualTo<Boolean> isTrue() {
        return isEqualTo(Boolean.TRUE);
    }

    static IsEqualTo<Boolean> isFalse() {
        return isEqualTo(Boolean.FALSE);
    }

    // conditions for strings only
    static IsLikeCaseInsensitive isLikeCaseInsensitive(String value) {
        return IsLikeCaseInsensitive.of(value);
    }

    static IsLikeCaseInsensitive isLikeCaseInsensitive(Supplier<String> valueSupplier) {
        return isLikeCaseInsensitive(valueSupplier.get());
    }

    static IsLikeCaseInsensitive isLikeCaseInsensitiveWhenPresent(String value) {
        return IsLikeCaseInsensitive.of(value).filter(Objects::nonNull);
    }

    static IsLikeCaseInsensitive isLikeCaseInsensitiveWhenPresent(Supplier<String> valueSupplier) {
        return isLikeCaseInsensitiveWhenPresent(valueSupplier.get());
    }

    static IsNotLikeCaseInsensitive isNotLikeCaseInsensitive(String value) {
        return IsNotLikeCaseInsensitive.of(value);
    }

    static IsNotLikeCaseInsensitive isNotLikeCaseInsensitive(Supplier<String> valueSupplier) {
        return isNotLikeCaseInsensitive(valueSupplier.get());
    }

    static IsNotLikeCaseInsensitive isNotLikeCaseInsensitiveWhenPresent(String value) {
        return IsNotLikeCaseInsensitive.of(value).filter(Objects::nonNull);
    }

    static IsNotLikeCaseInsensitive isNotLikeCaseInsensitiveWhenPresent(Supplier<String> valueSupplier) {
        return isNotLikeCaseInsensitiveWhenPresent(valueSupplier.get());
    }

    static IsInCaseInsensitive isInCaseInsensitive(String...values) {
        return IsInCaseInsensitive.of(values);
    }

    static IsInCaseInsensitive isInCaseInsensitive(Collection<String> values) {
        return IsInCaseInsensitive.of(values);
    }

    static IsInCaseInsensitive isInCaseInsensitiveWhenPresent(String...values) {
        return IsInCaseInsensitive.of(values).filter(Objects::nonNull);
    }

    static IsInCaseInsensitive isInCaseInsensitiveWhenPresent(Collection<String> values) {
        return values == null ? IsInCaseInsensitive.empty() : IsInCaseInsensitive.of(values).filter(Objects::nonNull);
    }

    static IsNotInCaseInsensitive isNotInCaseInsensitive(String...values) {
        return IsNotInCaseInsensitive.of(values);
    }

    static IsNotInCaseInsensitive isNotInCaseInsensitive(Collection<String> values) {
        return IsNotInCaseInsensitive.of(values);
    }

    static IsNotInCaseInsensitive isNotInCaseInsensitiveWhenPresent(String...values) {
        return IsNotInCaseInsensitive.of(values).filter(Objects::nonNull);
    }

    static IsNotInCaseInsensitive isNotInCaseInsensitiveWhenPresent(Collection<String> values) {
        return values == null ? IsNotInCaseInsensitive.empty() :
                IsNotInCaseInsensitive.of(values).filter(Objects::nonNull);
    }

    // order by support

    /**
     * Creates a sort specification based on a String. This is useful when a column has been
     * aliased in the select list. For example:
     *
     * <pre>
     *     select(foo.as("bar"))
     *     .from(baz)
     *     .orderBy(sortColumn("bar"))
     * </pre>
     *
     * @param name the string to use as a sort specification
     * @return a sort specification
     */
    static SortSpecification sortColumn(String name) {
        return SimpleSortSpecification.of(name);
    }

    /**
     * Creates a sort specification based on a column and a table alias. This can be useful in a join
     * where the desired sort order is based on a column not in the select list. This will likely
     * fail in union queries depending on database support.
     *
     * @param tableAlias the table alias
     * @param column the column
     * @return a sort specification
     */
    static SortSpecification sortColumn(String tableAlias, SqlColumn<?> column) {
        return new ColumnSortSpecification(tableAlias, column);
    }

    class InsertIntoNextStep {

        private final SqlTable table;

        private InsertIntoNextStep(SqlTable table) {
            this.table = Objects.requireNonNull(table);
        }

        public InsertSelectDSL withSelectStatement(Buildable<SelectModel> selectModelBuilder) {
            return InsertSelectDSL.insertInto(table)
                    .withSelectStatement(selectModelBuilder);
        }

        public InsertSelectDSL.SelectGatherer withColumnList(SqlColumn<?>...columns) {
            return InsertSelectDSL.insertInto(table)
                    .withColumnList(columns);
        }

        public InsertSelectDSL.SelectGatherer withColumnList(List<SqlColumn<?>> columns) {
            return InsertSelectDSL.insertInto(table)
                    .withColumnList(columns);
        }

        public <T> GeneralInsertDSL.SetClauseFinisher<T> set(SqlColumn<T> column) {
            return GeneralInsertDSL.insertInto(table)
                    .set(column);
        }
    }
}
