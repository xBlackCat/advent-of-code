package org.xblackcat.adventofcode.year2020.day19.rules;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

record OrRule(int id, Rule... rules) implements Rule {
    @Override
    public int test(
            String text,
            int offset,
            boolean canBeLast
    ) {
        for (Rule r : rules) {
            int advance = r.test(text, offset, canBeLast);
            if (advance > 0 && advance + offset <= text.length()) {
                return advance;
            }
        }
        return 0;
    }

    @Override
    public Rule pack() {
        return null;
    }

    @Override
    public String toString() {
        return id + ": " + Arrays.stream(rules).map(Objects::toString).collect(Collectors.joining("|"));
    }
}

