package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 4: Passport Processing ---
 * You arrive at the airport only to realize that you grabbed your North Pole Credentials instead of your passport. While these documents are extremely similar, North Pole Credentials aren't issued by a country and therefore aren't actually valid documentation for travel in most of the world.
 * <p>
 * It seems like you're not the only one having problems, though; a very long line has formed for the automatic passport scanners, and the delay could upset your travel itinerary.
 * <p>
 * Due to some questionable network security, you realize you might be able to solve both of these problems at the same time.
 * <p>
 * The automatic passport scanners are slow because they're having trouble detecting which passports have all required fields. The expected fields are as follows:
 * <p>
 * byr (Birth Year)
 * iyr (Issue Year)
 * eyr (Expiration Year)
 * hgt (Height)
 * hcl (Hair Color)
 * ecl (Eye Color)
 * pid (Passport ID)
 * cid (Country ID)
 * Passport data is validated in batch files (your puzzle input). Each passport is represented as a sequence of key:value pairs separated by spaces or newlines. Passports are separated by blank lines.
 * <p>
 * Here is an example batch file containing four passports:
 * <p>
 * ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
 * byr:1937 iyr:2017 cid:147 hgt:183cm
 * <p>
 * iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
 * hcl:#cfa07d byr:1929
 * <p>
 * hcl:#ae17e1 iyr:2013
 * eyr:2024
 * ecl:brn pid:760753108 byr:1931
 * hgt:179cm
 * <p>
 * hcl:#cfa07d eyr:2025 pid:166559648
 * iyr:2011 ecl:brn hgt:59in
 * The first passport is valid - all eight fields are present. The second passport is invalid - it is missing hgt (the Height field).
 * <p>
 * The third passport is interesting; the only missing field is cid, so it looks like data from North Pole Credentials, not a passport at all! Surely, nobody would mind if you made the system temporarily ignore missing cid fields. Treat this "passport" as valid.
 * <p>
 * The fourth passport is missing two fields, cid and byr. Missing cid is fine, but missing any other field is not, so this passport is invalid.
 * <p>
 * According to the above rules, your improved system would report 2 valid passports.
 * <p>
 * Count the number of valid passports - those that have all required fields. Treat cid as optional. In your batch file, how many passports are valid?
 * <p>
 * --- Part Two ---
 * The line is moving more quickly now, but you overhear airport security talking about how passports with invalid data are getting through. Better add some data validation, quick!
 * <p>
 * You can continue to ignore the cid field, but each other field has strict rules about what values are valid for automatic validation:
 * <p>
 * byr (Birth Year) - four digits; at least 1920 and at most 2002.
 * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
 * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
 * hgt (Height) - a number followed by either cm or in:
 * If cm, the number must be at least 150 and at most 193.
 * If in, the number must be at least 59 and at most 76.
 * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
 * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
 * pid (Passport ID) - a nine-digit number, including leading zeroes.
 * cid (Country ID) - ignored, missing or not.
 * Your job is to count the passports where all required fields are both present and valid according to the above rules. Here are some example values:
 * <p>
 * byr valid:   2002
 * byr invalid: 2003
 * <p>
 * hgt valid:   60in
 * hgt valid:   190cm
 * hgt invalid: 190in
 * hgt invalid: 190
 * <p>
 * hcl valid:   #123abc
 * hcl invalid: #123abz
 * hcl invalid: 123abc
 * <p>
 * ecl valid:   brn
 * ecl invalid: wat
 * <p>
 * pid valid:   000000001
 * pid invalid: 0123456789
 * Here are some invalid passports:
 * <p>
 * eyr:1972 cid:100
 * hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
 * <p>
 * iyr:2019
 * hcl:#602927 eyr:1967 hgt:170cm
 * ecl:grn pid:012533040 byr:1946
 * <p>
 * hcl:dab227 iyr:2012
 * ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
 * <p>
 * hgt:59cm ecl:zzz
 * eyr:2038 hcl:74454a iyr:2023
 * pid:3556412378 byr:2007
 * Here are some valid passports:
 * <p>
 * pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
 * hcl:#623a2f
 * <p>
 * eyr:2029 ecl:blu cid:129 byr:1989
 * iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
 * <p>
 * hcl:#888785
 * hgt:164cm byr:2001 iyr:2015 cid:88
 * pid:545766238 ecl:hzl
 * eyr:2022
 * <p>
 * iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
 * Count the number of valid passports - those that have all required fields and valid values. Continue to treat cid as optional. In your batch file, how many passports are valid?
 */
public class Day4 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d+)(cm|in)");
    private static final Pattern HAIR_COLOR_PATTERN = Pattern.compile("#[a-z0-9]{6}");
    private static final Set<String> REQUIRED_FIELD = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    private static final Set<String> EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    private static void part1() throws IOException {
        int valid = doCheck(Day4::weakCheck);

        System.out.println("Valid passports: " + valid);
    }

    private static boolean weakCheck(Map<String, String> it) {
        return it.keySet().containsAll(REQUIRED_FIELD);
    }

    private static boolean strongCheck(Map<String, String> passData) {
        if (!weakCheck(passData)) {
            return false;
        }

        if (!isNumberValid(passData.get("byr"), 1920, 2002)) {
            return false;
        }
        if (!isNumberValid(passData.get("iyr"), 2010, 2020)) {
            return false;
        }
        if (!isNumberValid(passData.get("eyr"), 2020, 2030)) {
            return false;
        }
        if (!isHeightValid(passData.get("hgt"))) {
            return false;
        }
        if (!HAIR_COLOR_PATTERN.matcher(passData.get("hcl")).matches()) {
            return false;
        }
        if (!EYE_COLORS.contains(passData.get("ecl"))) {
            return false;
        }

        String pid = passData.get("pid");
        if (pid.length() != 9) {
            return false;
        } else {
            try {
                Integer.parseInt(pid);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    private static boolean isHeightValid(String height) {
        final Matcher matcher = HEIGHT_PATTERN.matcher(height);
        if (!matcher.matches()) {
            return false;
        }

        final String value = matcher.group(1);
        return switch (matcher.group(2)) {
            case "cm" -> isNumberValid(value, 150, 193);
            case "in" -> isNumberValid(value, 59, 76);
            default -> false;
        };
    }

    private static boolean isNumberValid(String number, int lower, int upper) {
        try {
            final int value = Integer.parseInt(number);
            return value >= lower && value <= upper;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static void part2() throws IOException {

        int valid = doCheck(Day4::strongCheck);

        System.out.println("Valid passports: " + valid);
    }

    private static int doCheck(Predicate<Map<String, String>> isValid) throws IOException {
        int valid = 0;

        var pairPattern = Pattern.compile("(\\w+):(\\S+)");

        final Map<String, String> fields = new HashMap<>();
        try (
                final InputStream stream = Day4.class.getResourceAsStream("/year2020/day4.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    if (isValid.test(fields)) {
                        valid++;
                    }
                    fields.clear();
                } else {
                    var m = pairPattern.matcher(line);
                    while (m.find()) {
                        fields.put(m.group(1), m.group(2));
                    }
                }
            }
        }
        if (isValid.test(fields)) {
            valid++;
        }
        return valid;
    }
}
