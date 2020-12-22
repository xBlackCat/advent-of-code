package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * --- Day 21: Allergen Assessment ---
 * You reach the train's last stop and the closest you can get to your vacation island without getting wet. There aren't even any boats here, but nothing can stop you now: you build a raft. You just need a few days' worth of food for your journey.
 * <p>
 * You don't speak the local language, so you can't read any ingredients lists. However, sometimes, allergens are listed in a language you do understand. You should be able to use this information to determine which ingredient contains which allergen and work out which foods are safe to take with you on your trip.
 * <p>
 * You start by compiling a list of foods (your puzzle input), one food per line. Each line includes that food's ingredients list followed by some or all of the allergens the food contains.
 * <p>
 * Each allergen is found in exactly one ingredient. Each ingredient contains zero or one allergen. Allergens aren't always marked; when they're listed (as in (contains nuts, shellfish) after an ingredients list), the ingredient that contains each listed allergen will be somewhere in the corresponding ingredients list. However, even if an allergen isn't listed, the ingredient that contains that allergen could still be present: maybe they forgot to label it, or maybe it was labeled in a language you don't know.
 * <p>
 * For example, consider the following list of foods:
 * <pre>
 * mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
 * trh fvjkl sbzzf mxmxvkd (contains dairy)
 * sqjhc fvjkl (contains soy)
 * sqjhc mxmxvkd sbzzf (contains fish)</pre>
 * The first food in the list has four ingredients (written in a language you don't understand): mxmxvkd, kfcds, sqjhc, and nhms. While the food might contain other allergens, a few allergens the food definitely contains are listed afterward: dairy and fish.
 * <p>
 * The first step is to determine which ingredients can't possibly contain any of the allergens in any food in your list. In the above example, none of the ingredients kfcds, nhms, sbzzf, or trh can contain an allergen. Counting the number of times any of these ingredients appear in any ingredients list produces 5: they all appear once each except sbzzf, which appears twice.
 * <p>
 * Determine which ingredients cannot possibly contain any of the allergens in your list. How many times do any of those ingredients appear?
 * <p>
 * --- Part Two ---
 * Now that you've isolated the inert ingredients, you should have enough information to figure out which ingredient contains which allergen.
 * <p>
 * In the above example:
 * <pre>
 * mxmxvkd contains dairy.
 * sqjhc contains fish.
 * fvjkl contains soy.</pre>
 * Arrange the ingredients alphabetically by their allergen and separate them by commas to produce your canonical dangerous ingredient list. (There should not be any spaces in your canonical dangerous ingredient list.) In the above example, this would be mxmxvkd,sqjhc,fvjkl.
 * <p>
 * Time to stock your raft with supplies. What is your canonical dangerous ingredient list?
 */
public class Day21 {
    private static final Pattern MEAL_PARSER = Pattern.compile("([\\w ]+)\\(contains ([^)]+)\\)");
    private static final Pattern WORD_PARSER = Pattern.compile("\\w+");

    public static void main(String[] args) throws IOException {
        final List<Meal> menu = readMenu("/year2020/day21.txt");

        final Set<String> allAllergens = menu.stream().map(Meal::allergens).collect(HashSet::new, HashSet::addAll, HashSet::addAll);

        // allergen -> set of possible ingredients
        final Map<String, Set<String>> allergicIngredients = new HashMap<>();
        for (String a : allAllergens) {
            final Set<String> possibleIngredients = new HashSet<>();
            for (Meal m : menu) {
                if (m.allergens.contains(a)) {
                    if (possibleIngredients.isEmpty()) {
                        possibleIngredients.addAll(m.ingredients);
                    } else {
                        possibleIngredients.retainAll(m.ingredients);
                    }
                }
            }

            if (!possibleIngredients.isEmpty()) {
                allergicIngredients.put(a, possibleIngredients);
            }
        }

        Set<String> ingredientsWithAllergens = allergicIngredients.values().stream().collect(
                HashSet::new,
                HashSet::addAll,
                HashSet::addAll
        );

        final int ingredientsWithoutAllergens = menu.stream().map(Meal::ingredients).mapToInt(s -> {
            final Set<String> set = new HashSet<>(s);
            set.removeAll(ingredientsWithAllergens);
            return set.size();
        }).sum();

        System.out.println("Ingredients: " + ingredientsWithoutAllergens);
        boolean changed;
        do {
            changed = false;
            for (String a : allAllergens) {
                final Set<String> ingredients = allergicIngredients.remove(a);
                if (ingredients.size() == 1) {
                    for (Set<String> v : allergicIngredients.values()) {
                        changed |= v.removeAll(ingredients);
                    }
                }
                allergicIngredients.put(a, ingredients);
            }
        } while (changed);

        // Part 2
        System.out.println("Allergic map: ");
        allergicIngredients.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(System.out::println);

        final String list = allergicIngredients.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(v -> v.iterator().next())
                .collect(Collectors.joining(","));

        System.out.println("Result: " + list);
    }

    private static List<Meal> readMenu(String name) throws IOException {
        final List<Meal> menu;
        try (
                final InputStream stream = Day17.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            menu = reader.lines().map(Day21::parseLine).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return menu;
    }

    private static Meal parseLine(String line) {
        final Matcher mm = MEAL_PARSER.matcher(line);
        if (!mm.matches()) {
            return null;
        }

        return new Meal(
                getWords(mm.group(1)),
                getWords(mm.group(2))
        );
    }

    private static Set<String> getWords(String line) {
        Set<String> words = new HashSet<>();
        Matcher im = WORD_PARSER.matcher(line);
        while (im.find()) {
            words.add(im.group());
        }
        return words;
    }

    private record Meal(Set<String> ingredients, Set<String> allergens) {
    }
}
