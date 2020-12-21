package org.xblackcat.adventofcode.year2020.day19.rules;

record Match(int id, String value) implements Rule {
    static Rule of(int id, String ruleText) {
        if (ruleText.startsWith("\"") && ruleText.endsWith("\"")) {
            return new Match(id, ruleText.substring(1, ruleText.length() - 1));
        } else {
            return null;
        }
    }

    @Override
    public int test(String text, int offset, boolean canBeLast) {
        boolean completeMatch = text.equals(text.substring(0, offset) + value);
        if (completeMatch)
            if (!canBeLast) {
                System.out.println(" =-> " + text.substring(0, offset) + "[" + value + "]<*> !!!");
                return 0;
            } else {
                System.out.println(" =-> " + text.substring(0, offset) + "[" + value + "]");
                return value.length();
            }

        if (text.regionMatches(offset, value, 0, value.length())) {
            System.out.println(" =-> " + text.substring(0, offset) + "[" + value + "]" + (canBeLast ? "" : "<*>"));
            return value.length();
        } else {
            System.out.println(" =-> " + text.substring(0, offset) + "[!" + value + "]" + (canBeLast ? "" : "<*>"));
            return 0;
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

