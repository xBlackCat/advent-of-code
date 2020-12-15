package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 7: Handy Haversacks ---
 * You land at the regional airport in time for your next flight. In fact, it looks like you'll even have time to grab some food: all flights are currently delayed due to issues in luggage processing.
 * <p>
 * Due to recent aviation regulations, many rules (your puzzle input) are being enforced about bags and their contents; bags must be color-coded and must contain specific quantities of other color-coded bags. Apparently, nobody responsible for these regulations considered how long they would take to enforce!
 * <p>
 * For example, consider the following rules:
 *
 * <pre>light red bags contain 1 bright white bag, 2 muted yellow bags.
 * dark orange bags contain 3 bright white bags, 4 muted yellow bags.
 * bright white bags contain 1 shiny gold bag.
 * muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
 * shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
 * dark olive bags contain 3 faded blue bags, 4 dotted black bags.
 * vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
 * faded blue bags contain no other bags.
 * dotted black bags contain no other bags.</pre>
 * These rules specify the required contents for 9 bag types. In this example, every faded blue bag is empty, every vibrant plum bag contains 11 bags (5 faded blue and 6 dotted black), and so on.
 * <p>
 * You have a shiny gold bag. If you wanted to carry it in at least one other bag, how many different bag colors would be valid for the outermost bag? (In other words: how many colors can, eventually, contain at least one shiny gold bag?)
 * <p>
 * In the above rules, the following options would be available to you:
 *
 * <ul>
 *     <li>A bright white bag, which can hold your shiny gold bag directly.</li>
 * <li>A muted yellow bag, which can hold your shiny gold bag directly, plus some other bags.</li>
 * <li>A dark orange bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny gold bag.</li>
 * <li>A light red bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny gold bag.</li></ul>
 * So, in this example, the number of bag colors that can eventually contain at least one shiny gold bag is 4.
 * <p>
 * How many bag colors can eventually contain at least one shiny gold bag? (The list of rules is quite long; make sure you get all of it.)
 * <p>
 * --- Part Two ---
 * It's getting pretty expensive to fly these days - not because of ticket prices, but because of the ridiculous number of bags you need to buy!
 * <p>
 * Consider again your shiny gold bag and the rules from the above example:
 * <ul>
 * <li>faded blue bags contain 0 other bags.</li>
 * <li>dotted black bags contain 0 other bags.</li>
 * <li>vibrant plum bags contain 11 other bags: 5 faded blue bags and 6 dotted black bags.</li>
 * <li>dark olive bags contain 7 other bags: 3 faded blue bags and 4 dotted black bags.</li>
 * </ul>
 * So, a single shiny gold bag must contain 1 dark olive bag (and the 7 bags within it) plus 2 vibrant plum bags (and the 11 bags within each of those): 1 + 1*7 + 2 + 2*11 = 32 bags!
 * <p>
 * Of course, the actual rules have a small chance of going several levels deeper than this example; be sure to count all of the bags, even if the nesting becomes topologically impractical!
 * <p>
 * Here's another example:
 * <pre>
 * shiny gold bags contain 2 dark red bags.
 * dark red bags contain 2 dark orange bags.
 * dark orange bags contain 2 dark yellow bags.
 * dark yellow bags contain 2 dark green bags.
 * dark green bags contain 2 dark blue bags.
 * dark blue bags contain 2 dark violet bags.
 * dark violet bags contain no other bags.
 * </pre>
 * In this example, a single shiny gold bag must contain 126 other bags.
 * <p>
 * How many individual bags are required inside your single shiny gold bag?
 */
public class Day7 {
    private static final Pattern MAJOR = Pattern.compile("(\\w+ \\w+) bags contain (.+)");
    private static final Pattern MINOR = Pattern.compile("(\\d+) (\\w+ \\w+) bag");

    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }

    private static void part1() throws IOException {
        final List<BagRule> rules = readRules("/year2020/day7.txt");

        Map<String, Set<String>> rulesTree = new HashMap<>();
        for (var rule : rules) {
            for (var bag : rule.bags) {
                rulesTree.computeIfAbsent(bag.color, k -> new HashSet<>()).add(rule.container);
            }
        }

        Set<String> visited = new HashSet<>();
        contains(rulesTree, visited, "shiny gold");
        System.out.println(visited.size());
    }

    private static void part2() throws IOException {
        final List<BagRule> rules = readRules("/year2020/day7.txt");

        Map<String, BagAmount[]> rulesTree = new HashMap<>();
        for (var r : rules) {
            rulesTree.put(r.container, r.bags);
        }

        System.out.println(amount(rulesTree, "shiny gold"));
    }

    private static long amount(Map<String, BagAmount[]> rulesTree, String color) {
        var rule = rulesTree.get(color);
        if (rule == null) {
            return 0;
        }
        long amount = 0;
        for (var r : rule) {
            amount += r.amount * (amount(rulesTree, r.color) + 1);
        }
        return amount;
    }

    private static void contains(Map<String, Set<String>> rulesTree, Set<String> visited, String color) {
        final Set<String> set = rulesTree.get(color);
        if (set == null) {
            return;
        }
        for (var c : set) {
            if (visited.add(c)) {
                contains(rulesTree, visited, c);
            }
        }
    }

    private static List<BagRule> readRules(String name) throws IOException {
        List<BagRule> rules = new ArrayList<>();
        try (
                final InputStream stream = Day7.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                rules.add(parseRule(line));
            }
        }
        return rules;
    }

    private static BagRule parseRule(String rule) {
        final Matcher major = MAJOR.matcher(rule);
        if (!major.matches()) {
            throw new RuntimeException("!!!");
        }
        String container = major.group(1);
        final Matcher minor = MINOR.matcher(major.group(2));
        List<BagAmount> rules = new ArrayList<>();
        while (minor.find()) {
            rules.add(new BagAmount(minor.group(2), Integer.parseInt(minor.group(1))));
        }
        return new BagRule(container, rules.toArray(new BagAmount[0]));
    }

    private static record BagAmount(String color, int amount) {
    }


    private static record BagRule(String container, BagAmount[] bags) {
    }
}
