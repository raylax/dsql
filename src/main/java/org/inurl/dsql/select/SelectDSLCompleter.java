package org.inurl.dsql.select;

import java.util.function.Function;

import org.inurl.dsql.SortSpecification;
import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.mybatis3.MyBatis3Utils;

/**
 * Represents a function that can be used to create a general select method. When using this function,
 * you can create a method that does not require a user to call the build() and render() methods - making
 * client code look a bit cleaner.
 *
 * <p>This function is intended to by used in conjunction with utility methods like the select methods in
 * {@link MyBatis3Utils}.
 *
 * <p>For example, you can create mapper interface methods like this:
 *
 * <pre>
 * &#64;SelectProvider(type=SqlProviderAdapter.class, method="select")
 * List&lt;PersonRecord&gt; selectMany(SelectStatementProvider selectStatement);
 *
 * BasicColumn[] selectList =
 *     BasicColumn.columnList(id, firstName, lastName, birthDate, employed, occupation, addressId);
 *
 * default List&lt;PersonRecord&gt; select(SelectDSLCompleter completer) {
 *      return MyBatis3Utils.select(this::selectMany, selectList, person, completer);
 * }
 * </pre>
 *
 * <p>And then call the simplified default method like this:
 *
 * <pre>
 * List&lt;PersonRecord&gt; rows = mapper.select(c -&gt;
 *         c.where(occupation, isNull()));
 * </pre>
 *
 * <p>You can implement a "select all" with the following code:
 *
 * <pre>
 * List&lt;PersonRecord&gt; rows = mapper.select(c -&gt; c);
 * </pre>
 *
 * <p>Or
 *
 * <pre>
 * List&lt;PersonRecord&gt; rows = mapper.select(SelectDSLCompleter.allRows());
 * </pre>
 *
 * <p>There is also a utility method to support selecting all rows in a specified order:
 *
 * <pre>
 * List&lt;PersonRecord&gt; rows = mapper.select(SelectDSLCompleter.allRowsOrderedBy(lastName, firstName));
 * </pre>
 *
 * @author Jeff Butler
 */
@FunctionalInterface
public interface SelectDSLCompleter extends
        Function<QueryExpressionDSL<SelectModel>, Buildable<SelectModel>> {

    /**
     * Returns a completer that can be used to select every row in a table.
     *
     * @return the completer that will select every row in a table
     */
    static SelectDSLCompleter allRows() {
        return c -> c;
    }

    /**
     * Returns a completer that can be used to select every row in a table with specified order.
     *
     * @param columns list of sort specifications for an order by clause
     * @return the completer that will select every row in a table with specified order
     */
    static SelectDSLCompleter allRowsOrderedBy(SortSpecification...columns) {
        return c -> c.orderBy(columns);
    }
}
