package org.xblackcat.adventofcode.year2020;

import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * --- Day 9: Encoding Error ---
 * With your neighbor happily enjoying their video game, you turn your attention to an open data port on the little screen in the seat in front of you.
 * <p>
 * Though the port is non-standard, you manage to connect it to your computer through the clever use of several paperclips. Upon connection, the port outputs a series of numbers (your puzzle input).
 * <p>
 * The data appears to be encrypted with the eXchange-Masking Addition System (XMAS) which, conveniently for you, is an old cypher with an important weakness.
 * <p>
 * XMAS starts by transmitting a preamble of 25 numbers. After that, each number you receive should be the sum of any two of the 25 immediately previous numbers. The two numbers will have different values, and there might be more than one such pair.
 * <p>
 * For example, suppose your preamble consists of the numbers 1 through 25 in a random order. To be valid, the next number must be the sum of two of those numbers:
 * <p>
 * 26 would be a valid next number, as it could be 1 plus 25 (or many other pairs, like 2 and 24).
 * 49 would be a valid next number, as it is the sum of 24 and 25.
 * 100 would not be valid; no two of the previous 25 numbers sum to 100.
 * 50 would also not be valid; although 25 appears in the previous 25 numbers, the two numbers in the pair must be different.
 * Suppose the 26th number is 45, and the first number (no longer an option, as it is more than 25 numbers ago) was 20. Now, for the next number to be valid, there needs to be some pair of numbers among 1-19, 21-25, or 45 that add up to it:
 *
 * <ul>
 *     <li>26 would still be a valid next number, as 1 and 25 are still within the previous 25 numbers.</li>
 * <li>65 would not be valid, as no two of the available numbers sum to it.</li>
 * <li>64 and 66 would both be valid, as they are the result of 19+45 and 21+45 respectively.</li>
 * <li>Here is a larger example which only considers the previous 5 numbers (and has a preamble of length 5):</li>
 * </ul>
 * <pre>35
 * 20
 * 15
 * 25
 * 47
 * 40
 * 62
 * 55
 * 65
 * 95
 * 102
 * 117
 * 150
 * 182
 * 127
 * 219
 * 299
 * 277
 * 309
 * 576</pre>
 * In this example, after the 5-number preamble, almost every number is the sum of two of the previous 5 numbers; the only number that does not follow this rule is 127.
 * <p>
 * The first step of attacking the weakness in the XMAS data is to find the first number in the list (after the preamble) which is not the sum of two of the 25 numbers before it. What is the first number that does not have this property?
 * <p>
 * --- Part Two ---
 * The final step in breaking the XMAS encryption relies on the invalid number you just found: you must find a contiguous set of at least two numbers in your list which sum to the invalid number from step 1.
 * <p>
 * Again consider the above example:
 *
 * <pre>35
 * 20
 * 15
 * 25
 * 47
 * 40
 * 62
 * 55
 * 65
 * 95
 * 102
 * 117
 * 150
 * 182
 * 127
 * 219
 * 299
 * 277
 * 309
 * 576</pre>
 * In this list, adding up all of the numbers from 15 through 40 produces the invalid number from step 1, 127. (Of course, the contiguous set of numbers in your actual list might be much longer.)
 * <p>
 * To find the encryption weakness, add together the smallest and largest number in this contiguous range; in this example, these are 15 and 47, producing 62.
 * <p>
 * What is the encryption weakness in your XMAS-encrypted list of numbers?
 */
public class Day9 {
    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }


    private static void part1() throws IOException {
        var value = validate("/year2020/day9.txt", 5);
        System.out.println("Invalid value: " + value);
    }

    private static void part2() throws IOException {
        final String name = "/year2020/day9.txt";
        var value = validate(name, 25);
        if (value != null) {
            long[] buffer;
            try (
                    final InputStream stream = Day8.class.getResourceAsStream(name);
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
            ) {
                buffer = reader.lines().mapToLong(Long::parseLong).toArray();
            }


            long sum = 0;
            int lower = 0;
            int upper = 0;

            int limit = buffer.length;
            while (upper < limit && lower < limit) {
                if (sum < value) {
                    sum += buffer[upper++];
                } else if (sum > value) {
                    sum -= buffer[lower++];
                } else {
                    break;
                }
            }

            final long[] range = Arrays.copyOfRange(buffer, lower, upper);
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (long l : range) {
                if (l < min) {
                    min = l;
                }
                if (l > max) {
                    max = l;
                }
            }

            if (sum == value) {
                System.out.println("Found range [" + min + ", " + max + "]. Result: " + (min + max));
            }
        }
    }

    private static Long validate(String name, int amount) throws IOException {
        long[] buffer = new long[amount];
        Arrays.fill(buffer, Long.MIN_VALUE);
        try (
                final InputStream stream = Day8.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                long value = Long.parseLong(line);

                if (buffer[buffer.length - 1] != Long.MIN_VALUE) {
                    if (!checkValue(buffer, value)) {
                        return value;
                    }
                }

                System.arraycopy(buffer, 0, buffer, 1, buffer.length - 1);
                buffer[0] = value;
            }
        }
        return null;
    }

    private static boolean checkValue(long[] buffer, long value) {
        TLongSet testSet = new TLongHashSet();
        for (long b : buffer) {
            testSet.add(b);
            if (testSet.contains(value - b)) {
                return true;
            }
        }

        return false;
    }

}
