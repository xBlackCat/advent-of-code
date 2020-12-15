package org.xblackcat.adventofcode.year2020;

import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * --- Day 6: Custom Customs ---
 * As your flight approaches the regional airport where you'll switch to a much larger plane, customs declaration forms are distributed to the passengers.
 * <p>
 * The form asks a series of 26 yes-or-no questions marked a through z. All you need to do is identify the questions for which anyone in your group answers "yes". Since your group is just you, this doesn't take very long.
 * <p>
 * However, the person sitting next to you seems to be experiencing a language barrier and asks if you can help. For each of the people in their group, you write down the questions for which they answer "yes", one per line. For example:
 * <pre>
 * abcx
 * abcy
 * abcz
 * </pre>
 * In this group, there are 6 questions to which anyone answered "yes": a, b, c, x, y, and z. (Duplicate answers to the same question don't count extra; each question counts at most once.)
 * <p>
 * Another group asks for your help, then another, and eventually you've collected answers from every group on the plane (your puzzle input). Each group's answers are separated by a blank line, and within each group, each person's answers are on a single line. For example:
 * <pre>
 * abc
 *
 * a
 * b
 * c
 *
 * ab
 * ac
 *
 * a
 * a
 * a
 * a
 *
 * b
 * </pre>
 * This list represents answers from five groups:
 * <p>
 * The first group contains one person who answered "yes" to 3 questions: a, b, and c.
 * The second group contains three people; combined, they answered "yes" to 3 questions: a, b, and c.
 * The third group contains two people; combined, they answered "yes" to 3 questions: a, b, and c.
 * The fourth group contains four people; combined, they answered "yes" to only 1 question, a.
 * The last group contains one person who answered "yes" to only 1 question, b.
 * In this example, the sum of these counts is 3 + 3 + 3 + 1 + 1 = 11.
 * <p>
 * For each group, count the number of questions to which anyone answered "yes". What is the sum of those counts?
 * <p>
 * --- Part Two ---
 * As you finish the last group's customs declaration, you notice that you misread one word in the instructions:
 * <p>
 * You don't need to identify the questions to which anyone answered "yes"; you need to identify the questions to which everyone answered "yes"!
 * <p>
 * Using the same example as above:
 * <pre>
 * abc
 *
 * a
 * b
 * c
 *
 * ab
 * ac
 *
 * a
 * a
 * a
 * a
 *
 * b
 * </pre>
 * This list represents answers from five groups:
 *
 * <ul>
 * <li> In the first group, everyone (all 1 person) answered "yes" to 3 questions: a, b, and c.</li>
 * <li>In the second group, there is no question to which everyone answered "yes".</li>
 * <li>In the third group, everyone answered yes to only 1 question, a. Since some people did not answer "yes" to b or c, they don't count.</li>
 * <li>In the fourth group, everyone answered yes to only 1 question, a.</li>
 * <li>In the fifth group, everyone (all 1 person) answered "yes" to 1 question, b.</li>
 * </ul>
 * In this example, the sum of these counts is 3 + 0 + 1 + 1 + 1 = 6.
 * <p>
 * For each group, count the number of questions to which everyone answered "yes". What is the sum of those counts?
 */
public class Day6 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        final TCharSet answers = new TCharHashSet();
        int sum = 0;
        try (
                final InputStream stream = Day6.class.getResourceAsStream("/year2020/day6.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    sum += answers.size();
                    answers.clear();
                } else {
                    answers.addAll(line.toCharArray());
                }
            }
        }
        sum += answers.size();

        System.out.println(sum);
    }

    private static void part2() throws IOException {
        final TCharSet answers = new TCharHashSet();
        boolean fill = true;
        int sum = 0;
        try (
                final InputStream stream = Day6.class.getResourceAsStream("/year2020/day6.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    sum += answers.size();
                    answers.clear();
                    fill = true;
                } else if (fill) {
                    answers.addAll(line.toCharArray());
                    fill = false;
                } else {
                    answers.retainAll(line.toCharArray());
                }
            }
        }
        sum += answers.size();

        System.out.println(sum);

    }
}
