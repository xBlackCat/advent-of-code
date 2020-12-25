package org.xblackcat.adventofcode.year2020.day19.rules;

import java.util.Set;

sealed interface Rule permits BulkRule, Match, RefRule {
    /**
     * Returns 0 if rule can't be applied to specified position in the text
     *
     * @param text   text to check
     * @param offset position in text to apply the rule
     * @return 0 if rule can't be applied. If rule is matched, returns amount of symbols to match the rule
     */
    int test(String text, int offset);

    Set<String> getMatchSuffixes(String text);

    Rule pack();

    default boolean canBePacked() {
        return false;
    }
}

