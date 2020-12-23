package org.xblackcat.adventofcode.year2020;

import java.util.Collection;

/**
 * --- Day 23: Crab Cups ---
 * The small crab challenges you to a game! The crab is going to mix up some cups, and you have to predict where they'll end up.
 * <p>
 * The cups will be arranged in a circle and labeled clockwise (your puzzle input). For example, if your labeling were 32415, there would be five cups in the circle; going clockwise around the circle from the first cup, the cups would be labeled 3, 2, 4, 1, 5, and then back to 3 again.
 * <p>
 * Before the crab starts, it will designate the first cup in your list as the current cup. The crab is then going to do 100 moves.
 * <p>
 * Each move, the crab does the following actions:
 * <ul>
 * <li>The crab picks up the three cups that are immediately clockwise of the current cup. They are removed from the circle; cup spacing is adjusted as necessary to maintain the circle.</li>
 * <li>The crab selects a destination cup: the cup with a label equal to the current cup's label minus one. If this would select one of the cups that was just picked up, the crab will keep subtracting one until it finds a cup that wasn't just picked up. If at any point in this process the value goes below the lowest value on any cup's label, it wraps around to the highest value on any cup's label instead.</li>
 * <li>The crab places the cups it just picked up so that they are immediately clockwise of the destination cup. They keep the same order as when they were picked up.</li>
 * <li>The crab selects a new current cup: the cup which is immediately clockwise of the current cup.</li>
 * </ul>For example, suppose your cup labeling were 389125467. If the crab were to do merely 10 moves, the following changes would occur:
 * <pre>
 * -- move 1 --
 * cups: (3) 8  9  1  2  5  4  6  7
 * pick up: 8, 9, 1
 * destination: 2
 *
 * -- move 2 --
 * cups:  3 (2) 8  9  1  5  4  6  7
 * pick up: 8, 9, 1
 * destination: 7
 *
 * -- move 3 --
 * cups:  3  2 (5) 4  6  7  8  9  1
 * pick up: 4, 6, 7
 * destination: 3
 *
 * -- move 4 --
 * cups:  7  2  5 (8) 9  1  3  4  6
 * pick up: 9, 1, 3
 * destination: 7
 *
 * -- move 5 --
 * cups:  3  2  5  8 (4) 6  7  9  1
 * pick up: 6, 7, 9
 * destination: 3
 *
 * -- move 6 --
 * cups:  9  2  5  8  4 (1) 3  6  7
 * pick up: 3, 6, 7
 * destination: 9
 *
 * -- move 7 --
 * cups:  7  2  5  8  4  1 (9) 3  6
 * pick up: 3, 6, 7
 * destination: 8
 *
 * -- move 8 --
 * cups:  8  3  6  7  4  1  9 (2) 5
 * pick up: 5, 8, 3
 * destination: 1
 *
 * -- move 9 --
 * cups:  7  4  1  5  8  3  9  2 (6)
 * pick up: 7, 4, 1
 * destination: 5
 *
 * -- move 10 --
 * cups: (5) 7  4  1  8  3  9  2  6
 * pick up: 7, 4, 1
 * destination: 3
 *
 * -- final --
 * cups:  5 (8) 3  7  4  1  9  2  6</pre>
 * In the above example, the cups' values are the labels as they appear moving clockwise around the circle; the current cup is marked with ( ).
 * <p>
 * After the crab is done, what order will the cups be in? Starting after the cup labeled 1, collect the other cups' labels clockwise into a single string with no extra characters; each number except 1 should appear exactly once. In the above example, after 10 moves, the cups clockwise from 1 are labeled 9, 2, 6, 5, and so on, producing 92658374. If the crab were to complete all 100 moves, the order after cup 1 would be 67384529.
 * <p>
 * Using your labeling, simulate 100 moves. What are the labels on the cups after cup 1?
 * <p>
 *  --- Part Two ---
 * Due to what you can only assume is a mistranslation (you're not exactly fluent in Crab), you are quite surprised when the crab starts arranging many cups in a circle on your raft - one million (1000000) in total.
 * <p>
 * Your labeling is still correct for the first few cups; after that, the remaining cups are just numbered in an increasing fashion starting from the number after the highest number in your list and proceeding one by one until one million is reached. (For example, if your labeling were 54321, the cups would be numbered 5, 4, 3, 2, 1, and then start counting up from 6 until one million is reached.) In this way, every number from one through one million is used exactly once.
 * <p>
 * After discovering where you made the mistake in translating Crab Numbers, you realize the small crab isn't going to do merely 100 moves; the crab is going to do ten million (10000000) moves!
 * <p>
 * The crab is going to hide your stars - one each - under the two cups that will end up immediately clockwise of cup 1. You can have them if you predict what the labels on those cups will be when the crab is finished.
 * <p>
 * In the above example (389125467), this would be 934001 and then 159792; multiplying these together produces 149245887792.
 * <p>
 * Determine which two cups will end up immediately clockwise of cup 1. What do you get if you multiply their labels together?
 */
public class Day23 {
    public static void main(String[] args) {
        part1();
//        part2();
    }

    private static void part1() {
        System.out.println(playCrabGame("389125467", 10));
        System.out.println(playCrabGame("389125467", 100));
        System.out.println(playCrabGame("253149867", 100));
    }

    private static void part2() {
        System.out.println(playMillionCrabGame("389125467"));
        System.out.println(playMillionCrabGame("253149867"));
    }

    private static String playCrabGame(String text, int moves) {
        int[] buffer = text.chars().map(c -> c - '0').toArray();
        playCrabGame(moves, buffer);
        return toString(buffer);

    }

    private static String playMillionCrabGame(String text) {
        int[] buffer = new int[1_000_000];
        char[] charArray = text.toCharArray();
        int i = 0;
        for (char c : charArray) {
            buffer[i++] = c - '0';
        }

        for (; i < buffer.length; i++) {
            buffer[i] = i + 1;
        }
        playCrabGame(10_000_000, buffer);

        if (buffer[buffer.length - 1] == 1) {
            return String.valueOf((long) buffer[0] * buffer[1]);
        } else if (buffer[buffer.length - 2] == 1) {
            return String.valueOf((long) buffer[0] * buffer[buffer.length - 1]);
        }

        for (i = buffer.length - 3; i >= 0; i--) {
            if (buffer[i] == 1) {
                return String.valueOf((long) buffer[i + 1] * buffer[i + 2]);
            }
        }
        return "!!!";
    }

    private static void playCrabGame(int moves, int[] buffer) {
        long timeline = System.currentTimeMillis();
        for (int i = 1; i <= moves; i++) {
            int currentCup = buffer[0];
            int a = buffer[1];
            int b = buffer[2];
            int c = buffer[3];

            final int length = buffer.length;

            int nextCup = decrement(currentCup, length);
            while (nextCup == a || nextCup == b || nextCup == c) {
                nextCup = decrement(nextCup, length);
            }
            int j = 0;
            while (j < length - 4) {
                buffer[j] = buffer[j + 4];
                if (buffer[j] == nextCup) {
                    break;
                }
                j++;
            }
            j++;
            buffer[j] = a;
            j++;
            buffer[j] = b;
            j++;
            buffer[j] = c;
            j++;
            if (j < length - 1) {
                System.arraycopy(buffer, j + 1, buffer, j, length - j - 1);
            }
            buffer[length - 1] = currentCup;
            if ((i & 0xFFFF) == 0) {
                System.out.println("Move: " + i + " " + (System.currentTimeMillis() - timeline) + " ms");
            }
        }
    }

    private static String toString(int[] buffer) {
        StringBuilder tail = new StringBuilder();
        StringBuilder head = new StringBuilder();

        StringBuilder current = tail;
        for (int e : buffer) {
            if (e == 1) {
                current = head;
            } else {
                current.append(e);
            }
        }

        head.append(tail);

        return head.toString();
    }

    private static String toString(Collection<Integer> buffer) {
        StringBuilder tail = new StringBuilder();
        StringBuilder head = new StringBuilder();

        StringBuilder current = tail;
        for (int e : buffer) {
            if (e == 1) {
                current = head;
            } else {
                current.append(e);
            }
        }

        head.append(tail);

        return head.toString();
    }

    private static int decrement(int v, int bound) {
        if (v == 1) {
            return bound;
        } else {
            return v - 1;
        }
    }

}
