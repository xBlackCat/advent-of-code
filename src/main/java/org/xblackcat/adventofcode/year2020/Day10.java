package org.xblackcat.adventofcode.year2020;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * --- Day 10: Adapter Array ---
 * Patched into the aircraft's data port, you discover weather forecasts of a massive tropical storm. Before you can figure out whether it will impact your vacation plans, however, your device suddenly turns off!
 * <p>
 * Its battery is dead.
 * <p>
 * You'll need to plug it in. There's only one problem: the charging outlet near your seat produces the wrong number of jolts. Always prepared, you make a list of all of the joltage adapters in your bag.
 * <p>
 * Each of your joltage adapters is rated for a specific output joltage (your puzzle input). Any given adapter can take an input 1, 2, or 3 jolts lower than its rating and still produce its rated output joltage.
 * <p>
 * In addition, your device has a built-in joltage adapter rated for 3 jolts higher than the highest-rated adapter in your bag. (If your adapter list were 3, 9, and 6, your device's built-in adapter would be rated for 12 jolts.)
 * <p>
 * Treat the charging outlet near your seat as having an effective joltage rating of 0.
 * <p>
 * Since you have some time to kill, you might as well test all of your adapters. Wouldn't want to get to your resort and realize you can't even charge your device!
 * <p>
 * If you use every adapter in your bag at once, what is the distribution of joltage differences between the charging outlet, the adapters, and your device?
 * <p>
 * For example, suppose that in your bag, you have adapters with the following joltage ratings:
 *
 * <pre>16
 * 10
 * 15
 * 5
 * 1
 * 11
 * 7
 * 19
 * 6
 * 12
 * 4</pre>
 * With these adapters, your device's built-in joltage adapter would be rated for 19 + 3 = 22 jolts, 3 higher than the highest-rated adapter.
 * <p>
 * Because adapters can only connect to a source 1-3 jolts lower than its rating, in order to use every adapter, you'd need to choose them like this:
 *
 * <ul>
 *     <li>The charging outlet has an effective rating of 0 jolts, so the only adapters that could connect to it directly would need to have a joltage rating of 1, 2, or 3 jolts. Of these, only one you have is an adapter rated 1 jolt (difference of 1).</li>
 * <li>From your 1-jolt rated adapter, the only choice is your 4-jolt rated adapter (difference of 3).</li>
 * <li>From the 4-jolt rated adapter, the adapters rated 5, 6, or 7 are valid choices. However, in order to not skip any adapters, you have to pick the adapter rated 5 jolts (difference of 1).</li>
 * <li>Similarly, the next choices would need to be the adapter rated 6 and then the adapter rated 7 (with difference of 1 and 1).</li>
 * <li>The only adapter that works with the 7-jolt rated adapter is the one rated 10 jolts (difference of 3).</li>
 * <li>From 10, the choices are 11 or 12; choose 11 (difference of 1) and then 12 (difference of 1).</li>
 * <li>After 12, only valid adapter has a rating of 15 (difference of 3), then 16 (difference of 1), then 19 (difference of 3).</li>
 * <li>Finally, your device's built-in adapter is always 3 higher than the highest adapter, so its rating is 22 jolts (always a difference of 3).</li>
 * <li>In this example, when using every adapter, there are 7 differences of 1 jolt and 5 differences of 3 jolts.</li>
 * </ul>
 * Here is a larger example:
 *
 * <pre>28
 * 33
 * 18
 * 42
 * 31
 * 14
 * 46
 * 20
 * 48
 * 47
 * 24
 * 23
 * 49
 * 45
 * 19
 * 38
 * 39
 * 11
 * 1
 * 32
 * 25
 * 35
 * 8
 * 17
 * 7
 * 9
 * 4
 * 2
 * 34
 * 10
 * 3</pre>
 * In this larger example, in a chain that uses all of the adapters, there are 22 differences of 1 jolt and 10 differences of 3 jolts.
 * <p>
 * Find a chain that uses all of your adapters to connect the charging outlet to your device's built-in adapter and count the joltage differences between the charging outlet, the adapters, and your device. What is the number of 1-jolt differences multiplied by the number of 3-jolt differences?
 * <p>
 * --- Part Two ---
 * To completely determine whether you have enough adapters, you'll need to figure out how many different ways they can be arranged. Every arrangement needs to connect the charging outlet to your device. The previous rules about when adapters can successfully connect still apply.
 * <p>
 * The first example above (the one that starts with 16, 10, 15) supports the following arrangements:
 *
 * <pre>(0), 1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19, (22)
 * (0), 1, 4, 5, 6, 7, 10, 12, 15, 16, 19, (22)
 * (0), 1, 4, 5, 7, 10, 11, 12, 15, 16, 19, (22)
 * (0), 1, 4, 5, 7, 10, 12, 15, 16, 19, (22)
 * (0), 1, 4, 6, 7, 10, 11, 12, 15, 16, 19, (22)
 * (0), 1, 4, 6, 7, 10, 12, 15, 16, 19, (22)
 * (0), 1, 4, 7, 10, 11, 12, 15, 16, 19, (22)
 * (0), 1, 4, 7, 10, 12, 15, 16, 19, (22)</pre>
 * (The charging outlet and your device's built-in adapter are shown in parentheses.) Given the adapters from the first example, the total number of arrangements that connect the charging outlet to your device is 8.
 * <p>
 * The second example above (the one that starts with 28, 33, 18) has many arrangements. Here are a few:
 *
 * <pre>(0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
 * 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 48, 49, (52)
 *
 * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
 * 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 49, (52)
 *
 * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
 * 32, 33, 34, 35, 38, 39, 42, 45, 46, 48, 49, (52)
 *
 * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
 * 32, 33, 34, 35, 38, 39, 42, 45, 46, 49, (52)
 *
 * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
 * 32, 33, 34, 35, 38, 39, 42, 45, 47, 48, 49, (52)
 *
 * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
 * 46, 48, 49, (52)
 *
 * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
 * 46, 49, (52)
 *
 * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
 * 47, 48, 49, (52)
 *
 * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
 * 47, 49, (52)
 *
 * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
 * 48, 49, (52)</pre>
 * In total, this set of adapters can connect the charging outlet to your device in 19208 distinct arrangements.
 * <p>
 * You glance back down at your bag and try to remember why you brought so many adapters; there must be more than a trillion valid ways to arrange them! Surely, there must be an efficient way to count the arrangements.
 * <p>
 * What is the total number of distinct ways you can arrange the adapters to connect the charging outlet to your device?
 */
public class Day10 {
    public static void main(String[] args) throws IOException {
        // Part 1
        final TLongSet charges = getCharges("/year2020/day10.txt");

        int[] diff = new int[3];
        diff[2] = 1; // count last outlet diff

        TIntList sequences = new TIntArrayList();
        TIntList diffs = new TIntArrayList();
        int currentSequence = 0;
        boolean inSequence = false;
        long value = 0;
        while (!charges.isEmpty()) {
            boolean found = false;
            for (int i = 1; i <= 3; i++) {
                if (charges.contains(value + i)) {
                    if (i == 3) {
                        if (inSequence) {
                            inSequence = false;
                            if (currentSequence > 1) {
                                sequences.add(currentSequence);
                            }
                            currentSequence = 0;
                        }
                    } else if (i == 1) {
                        currentSequence++;
                        inSequence = true;
                    }
                    diff[i - 1]++;
                    diffs.add(i);
                    value += i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No next value from " + value);
                break;
            }
        }

        if (currentSequence > 1) {
            sequences.add(currentSequence);
        }

        final long deviceValue = value + 3;
        System.out.println("Charges counts are: " + Arrays.toString(diff) + ", max charge value = " + deviceValue);
        System.out.println("Product: " + (diff[0] * diff[2]));

        System.out.println("Diffs: " + diffs.toString());
        System.out.println("Sequence of ones: " + Arrays.toString(sequences.toArray()));

        // Part 2
        // I'm too lazy so I just hardcode amount for combinations of 2, 3 and 4 ones
        long result = Arrays.stream(sequences.toArray()).mapToLong(it -> switch (it) {
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 7;
            default -> 1; // Just in case - should be never happened
        }).reduce((left, right) -> left * right).orElseThrow();
        System.out.println("Amount of variants: " + result);
    }

    private static TLongSet getCharges(String name) throws IOException {
        final TLongSet charges;
        try (
                final InputStream stream = Day8.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            charges = new TLongHashSet(reader.lines().mapToLong(Long::parseLong).toArray());
        }
        return charges;
    }
}
