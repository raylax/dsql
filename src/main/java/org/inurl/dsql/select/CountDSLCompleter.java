package org.inurl.dsql.select;

import java.util.function.Function;
import java.util.function.ToLongFunction;

import org.inurl.dsql.util.Buildable;
import org.inurl.dsql.util.mybatis3.MyBatis3Utils;

/**
 * Represents a function that can be used to create a general count method. When using this function,
 * you can create a method that does not require a user to call the build() and render() methods - making
 * client code look a bit cleaner.
 *
 * <p>This function is intended to by used in conjunction with a utility method like
 * {@link MyBatis3Utils#countFrom(ToLongFunction, CountDSL, CountDSLCompleter)}
 *
 * <p>For example, you can create mapper interface methods like this:
 *
 * <pre>
 * &#64;SelectProvider(type=SqlProviderAdapter.class, method="select")
 * long count(SelectStatementProvider selectStatement);
 *
 * default long count(CountDSLCompleter completer) {
 *     return MyBatis3Utils.count(this::count, person, completer);
 * }
 * </pre>
 *
 * <p>And then call the simplified default method like this:
 *
 * <pre>
 * long rows = mapper.count(c -&gt;
 *         c.where(occupation, isNull()));
 * </pre>
 *
 * <p>You can implement a "count all" with the following code:
 *
 * <pre>
 * long rows = mapper.count(c -&gt; c);
 * </pre>
 *
 * <p>Or
 *
 * <pre>
 * long rows = mapper.count(CountDSLCompleter.allRows());
 * </pre>
 *
 * @author Jeff Butler
 */
@FunctionalInterface
public interface CountDSLCompleter extends
        Function<CountDSL<SelectModel>, Buildable<SelectModel>> {

    /**
     * Returns a completer that can be used to count every row in a table.
     *
     * @return the completer that will count every row in a table
     */
    static CountDSLCompleter allRows() {
        return c -> c;
    }
}
