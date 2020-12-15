package org.xblackcat.adventofcode.year2020;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * --- Day 5: Binary Boarding ---
 * You board your plane only to discover a new problem: you dropped your boarding pass! You aren't sure which seat is yours, and all of the flight attendants are busy with the flood of people that suddenly made it through passport control.
 * <p>
 * You write a quick program to use your phone's camera to scan all of the nearby boarding passes (your puzzle input); perhaps you can find your seat through process of elimination.
 * <p>
 * Instead of zones or groups, this airline uses binary space partitioning to seat people. A seat might be specified like FBFBBFFRLR, where F means "front", B means "back", L means "left", and R means "right".
 * <p>
 * The first 7 characters will either be F or B; these specify exactly one of the 128 rows on the plane (numbered 0 through 127). Each letter tells you which half of a region the given seat is in. Start with the whole list of rows; the first letter indicates whether the seat is in the front (0 through 63) or the back (64 through 127). The next letter indicates which half of that region the seat is in, and so on until you're left with exactly one row.
 * <p>
 * For example, consider just the first seven characters of FBFBBFFRLR:
 * <p>
 * Start by considering the whole range, rows 0 through 127.
 * F means to take the lower half, keeping rows 0 through 63.
 * B means to take the upper half, keeping rows 32 through 63.
 * F means to take the lower half, keeping rows 32 through 47.
 * B means to take the upper half, keeping rows 40 through 47.
 * B keeps rows 44 through 47.
 * F keeps rows 44 through 45.
 * The final F keeps the lower of the two, row 44.
 * The last three characters will be either L or R; these specify exactly one of the 8 columns of seats on the plane (numbered 0 through 7). The same process as above proceeds again, this time with only three steps. L means to keep the lower half, while R means to keep the upper half.
 * <p>
 * For example, consider just the last 3 characters of FBFBBFFRLR:
 * <p>
 * Start by considering the whole range, columns 0 through 7.
 * R means to take the upper half, keeping columns 4 through 7.
 * L means to take the lower half, keeping columns 4 through 5.
 * The final R keeps the upper of the two, column 5.
 * So, decoding FBFBBFFRLR reveals that it is the seat at row 44, column 5.
 * <p>
 * Every seat also has a unique seat ID: multiply the row by 8, then add the column. In this example, the seat has ID 44 * 8 + 5 = 357.
 * <p>
 * Here are some other boarding passes:
 * <p>
 * BFFFBBFRRR: row 70, column 7, seat ID 567.
 * FFFBBBFRRR: row 14, column 7, seat ID 119.
 * BBFFBBFRLL: row 102, column 4, seat ID 820.
 * As a sanity check, look through your list of boarding passes. What is the highest seat ID on a boarding pass?
 * <p>
 * --- Part Two ---
 * Ding! The "fasten seat belt" signs have turned on. Time to find your seat.
 * <p>
 * It's a completely full flight, so your seat should be the only missing boarding pass in your list. However, there's a catch: some of the seats at the very front and back of the plane don't exist on this aircraft, so they'll be missing from your list as well.
 * <p>
 * Your seat wasn't at the very front or back, though; the seats with IDs +1 and -1 from yours will be in your list.
 * <p>
 * What is the ID of your seat?
 */
public class Day5 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        int max = 0;
        try (
                final InputStream stream = Day5.class.getResourceAsStream("/year2020/day5.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                max = Math.max(max, parse(line));
            }
        }

        System.out.println(max);
    }

    private static void part2() throws IOException {
        int max = 0;
        TIntSet seats = new TIntHashSet();
        try (
                final InputStream stream = Day5.class.getResourceAsStream("/year2020/day5.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                final int seat = parse(line);
                seats.add(seat);
                max = Math.max(max, seat);
            }
        }

        for (int i = 0; i < max; i++) {
            if (!seats.contains(i) && seats.contains(i - 1) && seats.contains(i + 1)) {
                System.out.println("Possible seat: " + i);
            }
        }

    }

    private static int parse(String ticket) {
        var chars = ticket.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (chars[i] == 'R' || chars[i] == 'B') ? '1' : '0';
        }
        return Integer.parseInt(String.valueOf(chars), 2);
    }
}
