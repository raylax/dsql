package org.inurl.dsql;

import java.util.function.Function;

@FunctionalInterface
public interface Callback {
    void call();

    static Callback exceptionThrowingCallback(String message) {
        return exceptionThrowingCallback(message, RuntimeException::new);
    }

    static Callback exceptionThrowingCallback(String message,
            Function<String, ? extends RuntimeException> exceptionBuilder) {
        return () -> {
            throw exceptionBuilder.apply(message);
        };
    }
}
