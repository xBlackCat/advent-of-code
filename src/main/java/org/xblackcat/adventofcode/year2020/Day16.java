package org.xblackcat.adventofcode.year2020;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * --- Day 16: Ticket Translation ---
 * As you're walking to yet another connecting flight, you realize that one of the legs of your re-routed trip coming up is on a high-speed train. However, the train ticket you were given is in a language you don't understand. You should probably figure out what it says before you get to the train station after the next flight.
 * <p>
 * Unfortunately, you can't actually read the words on the ticket. You can, however, read the numbers, and so you figure out the fields these tickets must have and the valid ranges for values in those fields.
 * <p>
 * You collect the rules for ticket fields, the numbers on your ticket, and the numbers on other nearby tickets for the same train service (via the airport security cameras) together into a single document you can reference (your puzzle input).
 * <p>
 * The rules for ticket fields specify a list of fields that exist somewhere on the ticket and the valid ranges of values for each field. For example, a rule like class: 1-3 or 5-7 means that one of the fields in every ticket is named class and can be any value in the ranges 1-3 or 5-7 (inclusive, such that 3 and 5 are both valid in this field, but 4 is not).
 * <p>
 * Each ticket is represented by a single line of comma-separated values. The values are the numbers on the ticket in the order they appear; every ticket has the same format. For example, consider this ticket:
 *
 * <pre>
 * .--------------------------------------------------------.
 * | ????: 101    ?????: 102   ??????????: 103     ???: 104 |
 * |                                                        |
 * | ??: 301  ??: 302             ???????: 303      ??????? |
 * | ??: 401  ??: 402           ???? ????: 403    ????????? |
 * '--------------------------------------------------------'
 * </pre>Here, ? represents text in a language you don't understand. This ticket might be represented as 101,102,103,104,301,302,303,401,402,403; of course, the actual train tickets you're looking at are much more complicated. In any case, you've extracted just the numbers in such a way that the first number is always the same specific field, the second number is always a different specific field, and so on - you just don't know what each position actually means!
 * <p>
 * Start by determining which tickets are completely invalid; these are tickets that contain values which aren't valid for any field. Ignore your ticket for now.
 * <p>
 * For example, suppose you have the following notes:
 * <pre>
 * class: 1-3 or 5-7
 * row: 6-11 or 33-44
 * seat: 13-40 or 45-50
 *
 * your ticket:
 * 7,1,14
 *
 * nearby tickets:
 * 7,3,47
 * 40,4,50
 * 55,2,20
 * 38,6,12</pre>
 * It doesn't matter which position corresponds to which field; you can identify invalid nearby tickets by considering only whether tickets contain values that are not valid for any field. In this example, the values on the first nearby ticket are all valid for at least one field. This is not true of the other three nearby tickets: the values 4, 55, and 12 are are not valid for any field. Adding together all of the invalid values produces your ticket scanning error rate: 4 + 55 + 12 = 71.
 * <p>
 * Consider the validity of the nearby tickets you scanned. What is your ticket scanning error rate?
 * <p>
 * --- Part Two ---
 * Now that you've identified which tickets contain invalid values, discard those tickets entirely. Use the remaining valid tickets to determine which field is which.
 * <p>
 * Using the valid ranges for each field, determine what order the fields appear on the tickets. The order is consistent between all tickets: if seat is the third field, it is the third field on every ticket, including your ticket.
 * <p>
 * For example, suppose you have the following notes:
 * <pre>
 * class: 0-1 or 4-19
 * row: 0-5 or 8-19
 * seat: 0-13 or 16-19
 *
 * your ticket:
 * 11,12,13
 *
 * nearby tickets:
 * 3,9,18
 * 15,1,5
 * 5,14,9</pre>
 * Based on the nearby tickets in the above example, the first position must be row, the second position must be class, and the third position must be seat; you can conclude that in your ticket, class is 12, row is 11, and seat is 13.
 * <p>
 * Once you work out which field is which, look for the six fields on your ticket that start with the word departure. What do you get if you multiply those six values together?
 */
public class Day16 {

    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }

    private static void part1() throws IOException {
        final InputData<Integer> inputData = loadData(Integer::parseInt, "/year2020/day16.txt");

        final Region.Builder<Integer> builder = new Region.Builder<>();
        for (Rule<Integer> r : inputData.rules) {
            builder.append(r.restrictions);
        }

        var wholeRegion = builder.build();

        int sum = inputData.nearbyTickets
                .stream()
                .flatMap(t -> t.values.stream())
                .filter(v -> !wholeRegion.contains(v))
                .mapToInt(Integer::intValue).sum();

        System.out.println("Ticket scanning error rate is: " + sum);
    }

    private static void part2() throws IOException {
        final InputData<Integer> inputData = loadData(Integer::parseInt, "/year2020/day16.txt");

        final Region.Builder<Integer> builder = new Region.Builder<>();
        for (Rule<Integer> r : inputData.rules) {
            builder.append(r.restrictions);
        }

        var wholeRegion = builder.build();

        inputData.nearbyTickets.removeIf(ticket -> ticket.values.stream().anyMatch(v -> !wholeRegion.contains(v)));
        TIntObjectMap<Set<Rule<Integer>>> positionRules = new TIntObjectHashMap<>();

        IntStream.range(0, inputData.yourTicket.values.size()).forEach(pos -> positionRules.put(pos, new HashSet<>(inputData.rules)));

        for (var t : inputData.nearbyTickets) {
            List<Integer> values = t.values;
            for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {
                int v = values.get(i);

                var possibleRules = positionRules.get(i);

                possibleRules.removeIf(r -> !r.restrictions.contains(v));
            }
        }

        System.out.println("\nPosition rules (before): " + positionRules);

        AtomicBoolean changed = new AtomicBoolean();
        do {
            changed.set(false);
            for (int position : positionRules.keys()) {
                final Set<Rule<Integer>> ruleSet = positionRules.remove(position);
                if (ruleSet.size() == 1) {
                    var singleRule = ruleSet.iterator().next();

                    positionRules.forEachValue(v -> {
                        if (v.remove(singleRule)) {
                            changed.set(true);
                        }
                        return true;
                    });
                }
                positionRules.put(position, ruleSet);
            }
        } while (changed.get());

        System.out.println("\nPosition rules (after): " + positionRules);

        positionRules.retainEntries((k, v) -> {
            if (v.size() > 1) {
                System.out.println("!!!: " + v);
            }
            return v.stream().anyMatch(r -> r.name.contains("departure"));
        });

        AtomicLong product = new AtomicLong(1);
        positionRules.forEachKey(k -> {
            product.getAndUpdate(v -> v * inputData.yourTicket.values.get(k));
            return true;
        });
        System.out.println("\nPosition rules: " + positionRules);
        System.out.println("\nProduct: " + product.get());
    }


    private static final Pattern RULE_PATTERN = Pattern.compile("([^:]+):(.*)");
    private static final Pattern REGION_PATTERN = Pattern.compile("(\\d+)-(\\d+)");

    private static <T extends Comparable<T>> InputData<T> loadData(Function<String, T> converter, String name) throws IOException {
        try (
                final InputStream stream = Day12Part2.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            AtomicReference<State> state = new AtomicReference<>(State.Rules);

            List<Rule<T>> rules = new ArrayList<>();
            AtomicReference<Ticket<T>> yourTicket = new AtomicReference<>();
            List<Ticket<T>> nearbyTickets = new ArrayList<>();

            reader.lines()
                    .forEach(l -> {
                        if (l.isBlank()) {
                            state.set(State.None);
                            return;
                        }
                        switch (state.get()) {
                            case None:
                                switch (l) {
                                    case "your ticket:" -> state.set(State.YourTicket);
                                    case "nearby tickets:" -> state.set(State.NearbyTicket);
                                }
                                break;
                            case Rules: {
                                final Matcher ruleMatcher = RULE_PATTERN.matcher(l);
                                if (ruleMatcher.matches()) {
                                    String ruleName = ruleMatcher.group(1);
                                    String regions = ruleMatcher.group(2);

                                    Region.Builder<T> builder = new Region.Builder<>();
                                    final Matcher regionMatcher = REGION_PATTERN.matcher(regions);
                                    while (regionMatcher.find()) {
                                        builder.append(Region.of(
                                                converter.apply(regionMatcher.group(1)),
                                                converter.apply(regionMatcher.group(2))
                                        ));
                                    }

                                    rules.add(new Rule<>(ruleName, builder.build()));
                                }
                                break;
                            }
                            case YourTicket: {
                                Ticket<T> ticket = new Ticket<>(Arrays.stream(l.split(",")).map(converter).collect(Collectors.toList()));
                                yourTicket.set(ticket);
                                break;
                            }
                            case NearbyTicket: {
                                Ticket<T> ticket = new Ticket<>(Arrays.stream(l.split(",")).map(converter).collect(Collectors.toList()));
                                nearbyTickets.add(ticket);
                                break;
                            }
                        }
                    });

            return new InputData<>(rules, yourTicket.get(), nearbyTickets);
        }
    }

    private enum State {
        None,
        Rules,
        YourTicket,
        NearbyTicket,
    }

    private record Rule<T extends Comparable<T>>(String name, Region<T> restrictions) {
    }

    private record Ticket<T extends Comparable<T>>(List<T> values) {
    }

    private record InputData<T extends Comparable<T>>(List<Rule<T>> rules, Ticket<T> yourTicket, List<Ticket<T>> nearbyTickets) {
    }

    private interface Region<T extends Comparable<T>> {
        static <T extends Comparable<T>> Region<T> of(T lower, T upper) {
            return new SimpleRegion<>(lower, upper);
        }

        static <T extends Comparable<T>> Region<T> of(T dot) {
            return new Dot<>(dot);
        }

        default boolean contains(Region<T> another) {
            return lower().compareTo(another.lower()) <= 0 && upper().compareTo(another.upper()) >= 0;
        }

        default boolean contains(T dot) {
            return lower().compareTo(dot) <= 0 && upper().compareTo(dot) >= 0;
        }

        T lower();

        T upper();

        class Builder<T extends Comparable<T>> {
            private List<Region<T>> regionList = new ArrayList<>();

            public Builder<T> append(Region<T> region) {
                if (region instanceof CompoundRegion<T> compoundRegion) {
                    regionList.addAll(compoundRegion.regions);
                } else {
                    regionList.add(region);
                }
                return this;
            }

            public Builder<T> append(List<Region<T>> regions) {
                regionList.addAll(regions);
                return this;
            }

            public Region<T> build() {
                if (regionList.isEmpty()) {
                    return new SimpleRegion<>(null, null);
                }
                if (regionList.size() == 1) {
                    return regionList.get(0);
                }

                regionList.sort(Comparator.<Region<T>>nullsFirst(Comparator.comparing(Region::lower))
                                        .thenComparing(Comparator.<Region<T>>nullsFirst(Comparator.comparing(Region::upper)).reversed()));

                List<Region<T>> merged = new ArrayList<>();
                Region<T> lastRegion = null;
                for (Region<T> r : regionList) {
                    if (lastRegion == null) {
                        lastRegion = r;
                        continue;
                    }
                    if (lastRegion.contains(r)) {
                        continue;
                    }
                    if (lastRegion.upper().compareTo(r.lower()) > 0) {
                        // merge
                        lastRegion = new SimpleRegion<>(lastRegion.lower(), r.upper());
                        continue;
                    }

                    merged.add(lastRegion);
                    lastRegion = r;
                }
                merged.add(lastRegion);

                return new CompoundRegion<>(merged);
            }
        }

    }

    private record SimpleRegion<T extends Comparable<T>>(T lower, T upper) implements Region<T> {
        @Override
        public String toString() {
            return "[" + lower + ", " + upper + "]";
        }
    }

    private record Dot<T extends Comparable<T>>(T value) implements Region<T> {
        @Override
        public T lower() {
            return value();
        }

        @Override
        public T upper() {
            return value();
        }

        @Override
        public String toString() {
            return "[" + value + "]";
        }
    }

    private static class CompoundRegion<T extends Comparable<T>> implements Region<T> {
        private final T lower;
        private final T upper;

        private final List<Region<T>> regions;

        private CompoundRegion(List<Region<T>> regions) {
            this.lower = regions.get(0).lower();
            this.upper = regions.get(regions.size() - 1).upper();
            this.regions = regions;
        }

        @Override
        public T lower() {
            return lower;
        }

        @Override
        public T upper() {
            return upper;
        }

        @Override
        public boolean contains(Region<T> another) {
            if (another instanceof CompoundRegion) {
                throw new IllegalArgumentException("Not implemented");
            }

            for (Region<T> region : regions) {
                if (region.contains(another)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean contains(T dot) {
            for (Region<T> region : regions) {
                if (region.contains(dot)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return regions.stream().map(Object::toString).collect(Collectors.joining("∪"));
        }
    }
}
