package org.xblackcat.adventofcode.year2020.day19.rules;

import java.util.Collections;
import java.util.Set;

record Match(int id, String value) implements Rule {
    static Rule of(int id, String ruleText) {
        if (ruleText.startsWith("\"") && ruleText.endsWith("\"")) {
            return new Match(id, ruleText.substring(1, ruleText.length() - 1));
        } else {
            return null;
        }
    }

    @Override
    public int test(String text, int offset) {
        if (text.regionMatches(offset, value, 0, value.length())) {
            return value.length();
        } else {
            return 0;
        }
    }

    @Override
    public Set<String> getMatchSuffixes(String text) {
        if (text.regionMatches(0, value, 0, value.length())) {
            return Set.of(text.substring(value.length()));
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Rule pack() {
        return this;
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }

}

