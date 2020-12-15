package org.xblackcat.adventofcode.year2020;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * --- Day 1: Report Repair ---
 * <p>
 * After saving Christmas five years in a row, you've decided to take a vacation at a nice resort on a tropical island. Surely, Christmas will go on without you.
 * <p>
 * The tropical island has its own currency and is entirely cash-only. The gold coins used there have a little picture of a starfish; the locals just call them stars. None of the currency exchanges seem to have heard of them, but somehow, you'll need to find fifty of these coins by the time you arrive so you can pay the deposit on your room.
 * <p>
 * To save your vacation, you need to get all fifty stars by December 25th.
 * <p>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p>
 * Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input); apparently, something isn't quite adding up.
 * <p>
 * Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.
 * <p>
 * For example, suppose your expense report contained the following:
 * <p>
 * 1721
 * 979
 * 366
 * 299
 * 675
 * 1456
 * In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together produces 1721 * 299 = 514579, so the correct answer is 514579.
 * <p>
 * Of course, your expense report is much larger. Find the two entries that sum to 2020; what do you get if you multiply them together?
 * <p>
 * --- Part Two ---
 * The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they had left over from a past vacation. They offer you a second one if you can find three numbers in your expense report that meet the same criteria.
 * <p>
 * Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them together produces the answer, 241861950.
 * <p>
 * In your expense report, what is the product of the three entries that sum to 2020?
 */
public class Day1 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        TIntSet values = new TIntHashSet();

        try (
                final InputStream stream = Day1.class.getResourceAsStream("/year2020/day1.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                int value = Integer.parseInt(line);
                if (values.contains(2020 - value)) {
                    System.out.println("Found pair: " + value + " * " + (2020 - value) + " = " + (value * (2020 - value)));
                    return;
                }

                values.add(value);
            }
        }

        System.out.println("Pair not found");
    }

    private static void part2() throws IOException {
        TIntList valueList = new TIntArrayList();

        try (
                final InputStream stream = Day1.class.getResourceAsStream("/year2020/day1.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                valueList.add(Integer.parseInt(line));
            }
        }

        valueList.sort();
        final int[] values = valueList.toArray();
        for (int i = 0; i < values.length; i++) {
            final int a = values[i];
            for (int j = i + 1; j < values.length; j++) {
                final int b = values[j];
                for (int k = j + 1; k < values.length; k++) {
                    final int c = values[k];
                    int sum = a + b + c;
                    if (sum == 2020) {
                        System.out.println("Found triple: " + a + " * " + b + " * " + c + " = " + (a * b * c));
                        return;
                    } else if (sum > 2020) {
                        break;
                    }
                }
            }
        }

        System.out.println("Triples not found");
    }
}
