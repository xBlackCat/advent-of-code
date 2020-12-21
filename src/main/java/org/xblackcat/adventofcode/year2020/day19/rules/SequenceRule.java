package org.xblackcat.adventofcode.year2020.day19.rules;

import java.util.Arrays;
import java.util.stream.Collectors;

record SequenceRule(int id, Rule... rules) implements Rule {
    public SequenceRule(Rule... rules) {
        this(-1, rules);
    }

    @Override
    public int test(
            String text,
            int offset,
            boolean canBeLast
    ) {
        int totalAdvance = 0;
        for (Rule r : rules) {
            int advance = r.test(text, offset + totalAdvance, canBeLast);
            if (advance == 0) {
                return 0;
            }
            totalAdvance += advance;
        }
        return totalAdvance;
    }

    @Override
    public Rule pack() {
        return null;
    }

    @Override
    public String toString() {
        if (id >= 0) {
            return id + ": " + Arrays.stream(rules).map(String::valueOf).collect(Collectors.joining(" "));
        } else {
            // Internal rule
            return Arrays.stream(rules).map(String::valueOf).collect(Collectors.joining(" "));
        }
    }
}
