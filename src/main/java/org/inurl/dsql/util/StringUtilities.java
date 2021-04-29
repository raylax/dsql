package org.inurl.dsql.util;

import java.util.Optional;

public interface StringUtilities {

    static String spaceAfter(Optional<String> in) {
        return in.map(StringUtilities::spaceAfter)
                .orElse("");
    }

    static String spaceAfter(String in) {
        return in + " ";
    }

    static String spaceBefore(Optional<String> in) {
        return in.map(StringUtilities::spaceBefore)
                .orElse("");
    }

    static String spaceBefore(String in) {
        return " " + in;
    }

    static String safelyUpperCase(String s) {
        return s == null ? null : s.toUpperCase();
    }

    static String toCamelCase(String inputString) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;

        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            } else {
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
            }
        }

        return sb.toString();
    }
}
