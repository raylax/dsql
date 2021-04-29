package org.inurl.dsql.util;

import java.util.function.BiPredicate;

public class Predicates {
    private Predicates() {}

    public static <T> BiPredicate<T, T> bothPresent() {
        return (v1, v2) -> v1 != null && v2 != null;
    }
}
