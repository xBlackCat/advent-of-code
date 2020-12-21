package org.xblackcat.adventofcode.year2020.day19.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

record BulkRule(int id, Rule[]... rules) implements Rule {
    @Override
    public int test(
            String text,
            int offset,
            boolean canBeLast
    ) {
        for (Rule[] sequences : rules) {
            int advance = matchSequence(text, offset, canBeLast, sequences);
            if (advance > 0) {
                return advance;
            }
        }
        return 0;
    }

    @Override
    public Rule pack() {
        List<Rule[]> packedRules = new ArrayList<>(rules.length);
        boolean changed = false;
        for (Rule[] sequences : rules) {
            Rule simple = compact(sequences);
            if (simple == null) {
                for (int i = 0, sequencesLength = sequences.length; i < sequencesLength; i++) {
                    Rule r = sequences[i];
                    Rule p = r.pack();
                    if (p instanceof Match) {
                        sequences[i] = p;
                    }
                }
                packedRules.add(sequences);
            } else {
                changed = true;
                packedRules.add(new Rule[]{simple});
            }
        }
        if (changed) {
            return new BulkRule(id, packedRules.toArray(Rule[][]::new));
        } else {
            return this;
        }
    }

    @Override
    public boolean canBePacked() {

        return true;
    }

    private Rule compact(Rule[] sequences) {
        if (sequences.length == 1 && sequences[0] instanceof Match) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (Rule r : sequences) {
            Rule p = r.pack();
            if (p instanceof Match m) {
                builder.append(m.value());
            } else {
                return null;
            }
        }
        return new Match(-1, builder.toString());
    }

    private int matchSequence(String text, int offset, boolean canBeLast, Rule... sequences) {
        int totalAdvance = 0;
        for (Rule r : sequences) {
            boolean lastRule = r == sequences[sequences.length - 1];
            int advance = r.test(text, offset + totalAdvance, canBeLast && lastRule);
            if (advance == 0) {
                return 0;
            }
            totalAdvance += advance;
        }
        return totalAdvance;
    }

    @Override
    public String toString() {
        return Arrays.stream(rules)
                .map(s -> Arrays.stream(s).map(Object::toString).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("|"));
    }
}

