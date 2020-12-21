package org.xblackcat.adventofcode.year2020.day19.rules;

sealed interface Rule permits BulkRule, Match, OrRule, RefRule, SequenceRule {
    /**
     * Returns 0 if rule can't be applied to specified position in the text
     *
     * @param text      text to check
     * @param offset    position in text to apply the rule
     * @param canBeLast
     * @return 0 if rule can't be applied. If rule is matched, returns amount of symbols to match the rule
     */
    int test(String text, int offset, boolean canBeLast);

    Rule pack();

    default boolean canBePacked() {
        return false;
    }
}

