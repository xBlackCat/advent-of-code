package org.xblackcat.adventofcode.year2020;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.Arrays;

/**
 * --- Day 15: Rambunctious Recitation ---
 * You catch the airport shuttle and try to book a new flight to your vacation island. Due to the storm, all direct flights have been cancelled, but a route is available to get around the storm. You take it.
 * <p>
 * While you wait for your flight, you decide to check in with the Elves back at the North Pole. They're playing a memory game and are ever so excited to explain the rules!
 * <p>
 * In this game, the players take turns saying numbers. They begin by taking turns reading from a list of starting numbers (your puzzle input). Then, each turn consists of considering the most recently spoken number:
 * <ul>
 * <li>If that was the first time the number has been spoken, the current player says 0.</li>
 * <li>Otherwise, the number had been spoken before; the current player announces how many turns apart the number is from when it was previously spoken.</li>
 * </ul>So, after the starting numbers, each turn results in that player speaking aloud either 0 (if the last number is new) or an age (if the last number is a repeat).
 * <p>
 * For example, suppose the starting numbers are 0,3,6:
 * <ul>
 * <li>Turn 1: The 1st number spoken is a starting number, 0.</li>
 * <li>Turn 2: The 2nd number spoken is a starting number, 3.</li>
 * <li>Turn 3: The 3rd number spoken is a starting number, 6.</li>
 * <li>Turn 4: Now, consider the last number spoken, 6. Since that was the first time the number had been spoken, the 4th number spoken is 0.</li>
 * <li>Turn 5: Next, again consider the last number spoken, 0. Since it had been spoken before, the next number to speak is the difference between the turn number when it was last spoken (the previous turn, 4) and the turn number of the time it was most recently spoken before then (turn 1). Thus, the 5th number spoken is 4 - 1, 3.</li>
 * <li>Turn 6: The last number spoken, 3 had also been spoken before, most recently on turns 5 and 2. So, the 6th number spoken is 5 - 2, 3.</li>
 * <li>Turn 7: Since 3 was just spoken twice in a row, and the last two turns are 1 turn apart, the 7th number spoken is 1.</li>
 * <li>Turn 8: Since 1 is new, the 8th number spoken is 0.</li>
 * <li>Turn 9: 0 was last spoken on turns 8 and 4, so the 9th number spoken is the difference between them, 4.</li>
 * <li>Turn 10: 4 is new, so the 10th number spoken is 0.</li>
 * </ul>(The game ends when the Elves get sick of playing or dinner is ready, whichever comes first.)
 * <p>
 * Their question for you is: what will be the 2020th number spoken? In the example above, the 2020th number spoken will be 436.
 * <p>
 * Here are a few more examples:
 * <ul>
 * <li>Given the starting numbers 1,3,2, the 2020th number spoken is 1.</li>
 * <li>Given the starting numbers 2,1,3, the 2020th number spoken is 10.</li>
 * <li>Given the starting numbers 1,2,3, the 2020th number spoken is 27.</li>
 * <li>Given the starting numbers 2,3,1, the 2020th number spoken is 78.</li>
 * <li>Given the starting numbers 3,2,1, the 2020th number spoken is 438.</li>
 * <li>Given the starting numbers 3,1,2, the 2020th number spoken is 1836.</li>
 * </ul> Given your starting numbers, what will be the 2020th number spoken?
 * <p>
 * --- Part Two ---
 * Impressed, the Elves issue you a challenge: determine the 30000000th number spoken. For example, given the same starting numbers as above:
 * <ul>
 * <li>Given 0,3,6, the 30000000th number spoken is 175594.</li>
 * <li>Given 1,3,2, the 30000000th number spoken is 2578.</li>
 * <li>Given 2,1,3, the 30000000th number spoken is 3544142.</li>
 * <li>Given 1,2,3, the 30000000th number spoken is 261214.</li>
 * <li>Given 2,3,1, the 30000000th number spoken is 6895259.</li>
 * <li>Given 3,2,1, the 30000000th number spoken is 18.</li>
 * <li>Given 3,1,2, the 30000000th number spoken is 362.</ul></li>
 * Given your starting numbers, what will be the 30000000th number spoken?
 */
public class Day15 {
    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        doGame(2020, 0, 3, 6);
        doGame(2020, 1, 3, 2);
        doGame(2020, 2, 1, 3);
        doGame(2020, 1, 2, 3);
        doGame(2020, 2, 3, 1);
        doGame(2020, 3, 2, 1);
        doGame(2020, 3, 1, 2);
        doGame(2020, 15, 5, 1, 4, 7, 0);
    }

    private static void part2() {
        doGame(30000000, 0, 3, 6);
        doGame(30000000, 1, 3, 2);
        doGame(30000000, 2, 1, 3);
        doGame(30000000, 1, 2, 3);
        doGame(30000000, 2, 3, 1);
        doGame(30000000, 3, 2, 1);
        doGame(30000000, 3, 1, 2);
        doGame(30000000, 15, 5, 1, 4, 7, 0);
    }

    private static void doGame(long rounds, long... startNumbers) {
        final long value = playGame(rounds, startNumbers);
        System.out.println("Input values: " + Arrays.toString(startNumbers) + ". " + rounds + "th value is " + value);
    }

    private static long playGame(long rounds, long... startNumbers) {
        TLongObjectMap<LongBuf> playField = new TLongObjectHashMap<>();

        for (int i = 0, startNumbersLength = startNumbers.length; i < startNumbersLength; i++) {
            long n = startNumbers[i];
            final LongBuf buf = new LongBuf(i + 1);
            playField.put(n, buf);
        }

        long lastNumber = startNumbers[startNumbers.length - 1];

        for (long round = startNumbers.length + 1; round <= rounds; round++) {
            if (playField.containsKey(lastNumber)) {
                LongBuf buf = playField.get(lastNumber);
                if (buf.isFilled()) {
                    lastNumber = buf.getDiff();
                } else {
                    lastNumber = 0;
                }
            } else {
                lastNumber = 0;
            }

            final LongBuf curBuf = playField.get(lastNumber);
            if (curBuf == null) {
                playField.put(lastNumber, new LongBuf(round));
            } else {
                curBuf.add(round);
            }
        }
        return lastNumber;
    }

    private static class LongBuf {
        private final long[] buf = new long[2];
        private int size = 0;

        public LongBuf(long firstValue) {
            buf[0] = firstValue;
            size = 1;
        }

        public LongBuf() {
        }

        public boolean add(long v) {
            if (size == 0) {
                buf[0] = v;
                size = 1;
            } else {
                buf[1] = buf[0];
                buf[0] = v;

                if (size == 2) {
                    return true;
                }
                size = 2;
            }
            return false;
        }

        public long getDiff() {
            if (size < 2) {
                throw new RuntimeException("Oh no...");
            }

            return buf[0] - buf[1];
        }

        public boolean isFilled() {
            return size == 2;
        }

        @Override
        public String toString() {
            return switch (size) {
                case 0 -> "[]";
                case 1 -> "[" + buf[0] + "]";
                default -> Arrays.toString(buf);
            };
        }
    }
}
