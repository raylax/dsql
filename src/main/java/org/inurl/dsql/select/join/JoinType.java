package org.inurl.dsql.select.join;

import java.util.Optional;

public enum JoinType {
    INNER(),
    LEFT("left"),
    RIGHT("right"),
    FULL("full");

    private final String shortType;

    JoinType() {
        shortType = null;
    }

    JoinType(String shortType) {
        this.shortType = shortType;
    }

    public Optional<String> shortType() {
        return Optional.ofNullable(shortType);
    }
}
