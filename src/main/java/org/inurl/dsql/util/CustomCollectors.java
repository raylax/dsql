package org.inurl.dsql.util;

import java.util.StringJoiner;
import java.util.stream.Collector;

public interface CustomCollectors {

    /**
     * Returns a {@code Collector} similar to the standard JDK joining collector, except that
     * this collector returns an empty string if there are no elements to collect.
     *
     * @param delimiter the delimiter to be used between each element
     * @param  prefix the sequence of characters to be used at the beginning
     *                of the joined result
     * @param  suffix the sequence of characters to be used at the end
     *                of the joined result
     * @return A {@code Collector} which concatenates CharSequence elements,
     *     separated by the specified delimiter, in encounter order
     */
    static Collector<CharSequence, StringJoiner, String> joining(CharSequence delimiter, CharSequence prefix,
            CharSequence suffix) {
        return Collector.of(() -> {
            StringJoiner sj = new StringJoiner(delimiter, prefix, suffix);
            sj.setEmptyValue("");
            return sj;
        }, StringJoiner::add, StringJoiner::merge, StringJoiner::toString);
    }
}
