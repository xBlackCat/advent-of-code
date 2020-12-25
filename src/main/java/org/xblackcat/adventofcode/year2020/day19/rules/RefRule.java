package org.xblackcat.adventofcode.year2020.day19.rules;

import gnu.trove.map.TIntObjectMap;

import java.util.Set;

/**
 *
 */
public record RefRule(int id, int refId, TIntObjectMap<Rule> ruleCache) implements Rule {
    public RefRule(int refId, TIntObjectMap<Rule> ruleCache) {
        this(-1, refId, ruleCache);
    }

    @Override
    public int test(String text, int offset) {
        final Rule rule = getRule();
        if (rule == null) {
            throw new IllegalStateException("Referencing unknown rule #" + refId);
        }
        return rule.test(text, offset);
    }

    @Override
    public Set<String> getMatchSuffixes(String text) {
        final Rule rule = getRule();
        if (rule == null) {
            throw new IllegalStateException("Referencing unknown rule #" + refId);
        }
        return rule.getMatchSuffixes(text);
    }

    @Override
    public Rule pack() {
        return getRule();
    }

    private Rule getRule() {
        return ruleCache.get(refId);
    }

    @Override
    public String toString() {
        return "<" + refId + ">";
    }
}
