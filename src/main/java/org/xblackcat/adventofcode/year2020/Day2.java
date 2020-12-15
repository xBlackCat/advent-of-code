package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 2: Password Philosophy ---
 * Your flight departs in a few days from the coastal airport; the easiest way down to the coast from here is via toboggan.
 * <p>
 * The shopkeeper at the North Pole Toboggan Rental Shop is having a bad day. "Something's wrong with our computers; we can't log in!" You ask if you can take a look.
 * <p>
 * Their password database seems to be a little corrupted: some of the passwords wouldn't have been allowed by the Official Toboggan Corporate Policy that was in effect when they were chosen.
 * <p>
 * To try to debug the problem, they have created a list (your puzzle input) of passwords (according to the corrupted database) and the corporate policy when that password was set.
 * <p>
 * For example, suppose you have the following list:
 * <p>
 * 1-3 a: abcde
 * 1-3 b: cdefg
 * 2-9 c: ccccccccc
 * Each line gives the password policy and then the password. The password policy indicates the lowest and highest number of times a given letter must appear for the password to be valid. For example, 1-3 a means that the password must contain a at least 1 time and at most 3 times.
 * <p>
 * In the above example, 2 passwords are valid. The middle password, cdefg, is not; it contains no instances of b, but needs at least 1. The first and third passwords are valid: they contain one a or nine c, both within the limits of their respective policies.
 * <p>
 * How many passwords are valid according to their policies?
 * <p>
 * --- Part Two ---
 * While it appears you validated the passwords correctly, they don't seem to be what the Official Toboggan Corporate Authentication System is expecting.
 * <p>
 * The shopkeeper suddenly realizes that he just accidentally explained the password policy rules from his old job at the sled rental place down the street! The Official Toboggan Corporate Policy actually works a little differently.
 * <p>
 * Each policy actually describes two positions in the password, where 1 means the first character, 2 means the second character, and so on. (Be careful; Toboggan Corporate Policies have no concept of "index zero"!) Exactly one of these positions must contain the given letter. Other occurrences of the letter are irrelevant for the purposes of policy enforcement.
 * <p>
 * Given the same example list from above:
 * <p>
 * 1-3 a: abcde is valid: position 1 contains a and position 3 does not.
 * <p>
 * 1-3 b: cdefg is invalid: neither position 1 nor position 3 contains b.
 * <p>
 * 2-9 c: ccccccccc is invalid: both position 2 and position 9 contain c.
 * <p>
 * How many passwords are valid according to the new interpretation of the policies?
 */
public class Day2 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        int valid = 0;

        final var pattern = Pattern.compile("(\\d+)-(\\d+) (\\w): (\\w+)");

        try (
                final var stream = Day1.class.getResourceAsStream("/year2020/day2.txt");
                final var reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    int from = Integer.parseInt(matcher.group(1));
                    int to = Integer.parseInt(matcher.group(2));
                    char passChar = matcher.group(3).charAt(0);

                    int found = 0;
                    for (var c : matcher.group(4).toCharArray()) {
                        if (c == passChar) {
                            found++;
                            if (found > to) {
                                break;
                            }
                        }
                    }

                    if (found >= from && found <= to) {
                        valid++;
                    }
                } else {
                    System.out.println("!!! Failed to match line: " + line);
                }
            }
        }

        System.out.println("Valid passwords: " + valid);
    }

    private static void part2() throws IOException {
        int valid = 0;

        final var pattern = Pattern.compile("(\\d+)-(\\d+) (\\w): (\\w+)");

        try (
                final var stream = Day1.class.getResourceAsStream("/year2020/day2.txt");
                final var reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    int from = Integer.parseInt(matcher.group(1));
                    int to = Integer.parseInt(matcher.group(2));
                    char passChar = matcher.group(3).charAt(0);

                    final String pass = matcher.group(4);

                    if (
                            (passChar == pass.charAt(from - 1)) !=
                                    (passChar == pass.charAt(to - 1))
                    ) {
                        valid++;
                    }
                } else {
                    System.out.println("!!! Failed to match line: " + line);
                }
            }
        }

        System.out.println("Valid passwords: " + valid);
    }

}
