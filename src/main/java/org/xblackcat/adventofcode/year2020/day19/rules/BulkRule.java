package org.xblackcat.adventofcode.year2020.day19.rules;

import java.util.*;
import java.util.stream.Collectors;

record BulkRule(int id, Rule[]... rules) implements Rule {
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

    @Override
    public Set<String> getMatchSuffixes(String text) {
        Set<String> result = new HashSet<>();
        for (Rule[] sequences : rules) {
            Set<String> suffixes = Set.of(text);
            for (Rule r : sequences) {
                suffixes = suffixes.stream().map(r::getMatchSuffixes).collect(HashSet::new, Set::addAll, Set::addAll);
            }

            result.addAll(suffixes);
        }

        return result;
    }

    @Override
    public int test(String text, int offset) {
        for (Rule[] sequences : rules) {
            int advance = matchSequence(text, offset, sequences);
            if (advance > 0) {
                return advance;
            }
        }
        return 0;
    }

    private int matchSequence(String text, int offset, Rule... sequences) {
        int totalAdvance = 0;
        for (Rule r : sequences) {
            boolean lastRule = r == sequences[sequences.length - 1];
            int advance = r.test(text, offset + totalAdvance);
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

