package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * --- Day 24: Lobby Layout ---
 * <p>
 * Your raft makes it to the tropical island; it turns out that the small crab was an excellent navigator. You make your way to the resort.
 * <p>
 * As you enter the lobby, you discover a small problem: the floor is being renovated. You can't even reach the check-in desk until they've finished installing the new tile floor.
 * <p>
 * The tiles are all hexagonal; they need to be arranged in a hex grid with a very specific color pattern. Not in the mood to wait, you offer to help figure out the pattern.
 * <p>
 * The tiles are all white on one side and black on the other. They start with the white side facing up. The lobby is large enough to fit whatever pattern might need to appear there.
 * <p>
 * A member of the renovation crew gives you a list of the tiles that need to be flipped over (your puzzle input). Each line in the list identifies a single tile that needs to be flipped by giving a series of steps starting from a reference tile in the very center of the room. (Every line starts from the same reference tile.)
 * <p>
 * Because the tiles are hexagonal, every tile has six neighbors: east, southeast, southwest, west, northwest, and northeast. These directions are given in your list, respectively, as e, se, sw, w, nw, and ne. A tile is identified by a series of these directions with no delimiters; for example, esenee identifies the tile you land on if you start at the reference tile and then move one tile east, one tile southeast, one tile northeast, and one tile east.
 * <p>
 * Each time a tile is identified, it flips from white to black or from black to white. Tiles might be flipped more than once. For example, a line like esew flips a tile immediately adjacent to the reference tile, and a line like nwwswee flips the reference tile itself.
 * <p>
 * Here is a larger example:
 * <pre>
 * sesenwnenenewseeswwswswwnenewsewsw
 * neeenesenwnwwswnenewnwwsewnenwseswesw
 * seswneswswsenwwnwse
 * nwnwneseeswswnenewneswwnewseswneseene
 * swweswneswnenwsewnwneneseenw
 * eesenwseswswnenwswnwnwsewwnwsene
 * sewnenenenesenwsewnenwwwse
 * wenwwweseeeweswwwnwwe
 * wsweesenenewnwwnwsenewsenwwsesesenwne
 * neeswseenwwswnwswswnw
 * nenwswwsewswnenenewsenwsenwnesesenew
 * enewnwewneswsewnwswenweswnenwsenwsw
 * sweneswneswneneenwnewenewwneswswnese
 * swwesenesewenwneswnwwneseswwne
 * enesenwswwswneneswsenwnewswseenwsese
 * wnwnesenesenenwwnenwsewesewsesesew
 * nenewswnwewswnenesenwnesewesw
 * eneswnwswnwsenenwnwnwwseeswneewsenese
 * neswnwewnwnwseenwseesewsenwsweewe
 * wseweeenwnesenwwwswnew</pre>
 * In the above example, 10 tiles are flipped once (to black), and 5 more are flipped twice (to black, then back to white). After all of these instructions have been followed, a total of 10 tiles are black.
 * <p>
 * Go through the renovation crew's list and determine which tiles they need to flip. After all of the instructions have been followed, how many tiles are left with the black side up?
 * <p>
 * --- Part Two ---
 * <p>
 * The tile floor in the lobby is meant to be a living art exhibit. Every day, the tiles are all flipped according to the following rules:
 *
 * <ul><li>Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.</li>
 * <li>Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.</li>
 * </ul>Here, tiles immediately adjacent means the six tiles directly touching the tile in question.
 * <p>
 * The rules are applied simultaneously to every tile; put another way, it is first determined which tiles need to be flipped, then they are all flipped at the same time.
 * <p>
 * In the above example, the number of black tiles that are facing up after the given number of days has passed is as follows:
 * <pre>
 * Day 1: 15
 * Day 2: 12
 * Day 3: 25
 * Day 4: 14
 * Day 5: 23
 * Day 6: 28
 * Day 7: 41
 * Day 8: 37
 * Day 9: 49
 * Day 10: 37
 *
 * Day 20: 132
 * Day 30: 259
 * Day 40: 406
 * Day 50: 566
 * Day 60: 788
 * Day 70: 1106
 * Day 80: 1373
 * Day 90: 1844
 * Day 100: 2208</pre>
 * After executing this process a total of 100 times, there would be 2208 black tiles facing up.
 * <p>
 * How many tiles will be black after 100 days?
 */
public class Day24 {
    public static void main(String[] args) throws IOException {
        Map<Hex, Long> hexes;
        try (
                final InputStream stream = Day17.class.getResourceAsStream("/year2020/day24.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            Direction.Builder db = new Direction.Builder();
            hexes = reader.lines().map(l -> {
                final Hex.Builder hb = new Hex.Builder();
                l.chars().mapToObj(db::consume).filter(Objects::nonNull).forEach(hb::apply);
                return hb.build();
            }).collect(Collectors.groupingBy(o -> o, HashMap::new, Collectors.counting()));
        }

        final List<Hex> blackTiles = hexes.entrySet().stream()
                .filter(e -> (e.getValue() & 1) == 1).map(Map.Entry::getKey).collect(Collectors.toList());
        System.out.println("Black tiles: " + blackTiles.size());

        // Part two

        int days = 100;

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Hex h : blackTiles) {
            if (h.x > maxX) {
                maxX = h.x;
            }
            if (h.x < minX) {
                minX = h.x;
            }
            if (h.y > maxY) {
                maxY = h.y;
            }
            if (h.y < minY) {
                minY = h.y;
            }
        }

        int padding = 1 + days;

        int playgroundWidth = padding * 2 + maxX - minX;
        int playgroundHeight = padding * 2 + maxY - minY;
        int[][] playground = new int[playgroundHeight][playgroundWidth];

        for (Hex h : blackTiles) {
            playground[padding + h.y][padding + h.x] = 1;
        }

        for (int i = 0; i < days; i++) {
            for (int y = 1; y < playgroundWidth - 1; y++) {
                for (int x = 1; x < playgroundHeight - 1; x++) {
                    int neighbour = 0;
                    // East
                    neighbour += playground[y][x + 1] & 1;
                    // West
                    neighbour += playground[y][x - 1] & 1;
                    // SouthEast
                    neighbour += playground[y - 1][x] & 1;
                    // SouthWest
                    neighbour += playground[y - 1][x - 1] & 1;
                    // NorthEast
                    neighbour += playground[y + 1][x + 1] & 1;
                    // NorthWest
                    neighbour += playground[y + 1][x] & 1;

                    playground[y][x] |= neighbour << 1;
                }
            }

            for (int y = 1; y < playgroundWidth - 1; y++) {
                for (int x = 1; x < playgroundHeight - 1; x++) {
                    final int hex = playground[y][x];
                    playground[y][x] = (hex == 3 || hex == 4 || hex == 5) ? 1 : 0;
                }
            }
        }

        final int sum = Arrays.stream(playground).mapToInt(row -> Arrays.stream(row).sum()).sum();
        System.out.println("Black tiles at the end of " + days + " day: " + sum);
    }

    private enum Direction {
        East,
        West,
        SouthEast,
        SouthWest,
        NorthEast,
        NorthWest;

        public static class Builder {
            private Boolean state;

            Direction consume(int c) {
                if (state == null) {
                    if (c == 'e') {
                        return East;
                    } else if (c == 'w') {
                        return West;
                    } else if (c == 's') {
                        state = false;
                        return null;
                    } else if (c == 'n') {
                        state = true;
                        return null;
                    } else {
                        throw new IllegalArgumentException("Unknown direction: " + Character.toString(c));
                    }
                }

                try {
                    if (c == 'e') {
                        return state ? NorthEast : SouthEast;
                    } else if (c == 'w') {
                        return state ? NorthWest : SouthWest;
                    } else {
                        throw new IllegalArgumentException("Unknown direction: " + Character.toString(c));
                    }
                } finally {
                    state = null;
                }
            }
        }
    }

    private record Hex(int x, int y) {
        static class Builder {
            private int x;
            private int y;

            Hex build() {
                return new Hex(x, y);
            }

            void apply(Direction d) {
                switch (d) {
                    case East -> x++;
                    case West -> x--;
                    case SouthEast -> y--;
                    case SouthWest -> {
                        y--;
                        x--;
                    }
                    case NorthEast -> {
                        x++;
                        y++;
                    }
                    case NorthWest -> y++;
                }
            }
        }
    }
}
