package org.xblackcat.adventofcode.year2020.day19.rules;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class RuleProcessor {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\d+|\\||\"[^\"]+\"");

    private final TIntObjectMap<Rule> ruleCache = new TIntObjectHashMap<>();
    private final Rule mainRule = new RefRule(0, ruleCache);

    public void addRule(int ruleId, String ruleText) {
        Rule simpleRule = Match.of(ruleId, ruleText);
        if (simpleRule != null) {
            ruleCache.put(ruleId, simpleRule);
            return;
        }

        final Matcher matcher = TOKEN_PATTERN.matcher(ruleText);
        List<Rule[]> bulk = new ArrayList<>();
        List<Rule> row = new ArrayList<>();
        while (matcher.find()) {
            String token = matcher.group();
            if (Objects.equals(token, "|")) {
                if (!row.isEmpty()) {
                    bulk.add(row.toArray(Rule[]::new));
                    row.clear();
                }
                continue;
            }
            Rule r;
            try {
                int refIdx = Integer.parseInt(token);
                r = new RefRule(refIdx, ruleCache);
            } catch (NumberFormatException e) {
                // Possibly literal rule
                r = Match.of(ruleId, token);
            }
            if (r == null) {
                // Go on next round
                bulk.clear();
                row.clear();
                break;
            }
            row.add(r);
        }

        if (!row.isEmpty()) {
            bulk.add(row.toArray(Rule[]::new));
        }

        if (!bulk.isEmpty()) {
            ruleCache.remove(ruleId);

            Rule newRule = new BulkRule(ruleId, bulk.toArray(Rule[][]::new));

            ruleCache.put(ruleId, newRule);
        }
    }

    public void packRules() {
        System.out.println("Before pack: ");
        ruleCache.forEachValue(r -> {
            System.out.println(r);
            return true;
        });
        final AtomicBoolean changed = new AtomicBoolean();
        do {
            changed.set(false);

            ruleCache.transformValues(r -> {
                Rule packed = r.pack();
                if (r.canBePacked() && packed != r) {
                    changed.set(true);
                    return packed;
                } else {
                    return r;
                }
            });
        } while (changed.get());
        System.out.println("After pack: ");
        ruleCache.forEachValue(r -> {
            System.out.println(r);
            return true;
        });
    }

    public boolean matches(String line) {
        final int test = mainRule.test(line, 0, true);
        boolean matches = test == line.length();
        System.out.println(line + "\n-> " + matches + "(" + test + "/" + line.length() + ")");
        return matches;
    }
}
