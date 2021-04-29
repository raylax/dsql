package org.inurl.dsql.where;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.inurl.dsql.ExistsPredicate;
import org.inurl.dsql.BindableColumn;
import org.inurl.dsql.SqlCriterion;
import org.inurl.dsql.VisitableCondition;

/**
 * Base class for DSLs that support where clauses - which is every DSL except Insert.
 * The purpose of the class is to provide an implementation of the {@link AbstractWhereDSL}
 * that is customized for a particular DSL, and to add the initiating common "where"
 * methods.
 *
 * @param <W> the implementation of the Where DSL customized for a particular SQL statement.
 */
public abstract class AbstractWhereSupport<W extends AbstractWhereDSL<?>> {

    public abstract W where();

    public <T> W where(BindableColumn<T> column, VisitableCondition<T> condition, SqlCriterion...subCriteria) {
        return where(column, condition, Arrays.asList(subCriteria));
    }

    public <T> W where(BindableColumn<T> column, VisitableCondition<T> condition, List<SqlCriterion> subCriteria) {
        return apply(w -> w.where(column, condition, subCriteria));
    }

    public W where(ExistsPredicate existsPredicate) {
        return apply(w -> w.where(existsPredicate));
    }

    public W where(ExistsPredicate existsPredicate, SqlCriterion...subCriteria) {
        return apply(w -> w.where(existsPredicate, subCriteria));
    }

    public W applyWhere(WhereApplier whereApplier) {
        return apply(w -> w.applyWhere(whereApplier));
    }

    private W apply(Consumer<W> block) {
        W dsl = where();
        block.accept(dsl);
        return dsl;
    }
}
