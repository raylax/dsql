package org.inurl.dsql.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Buildable<T> {
    @NotNull
    T build();
}