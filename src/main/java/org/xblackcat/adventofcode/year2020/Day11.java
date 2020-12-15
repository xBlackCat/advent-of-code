package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * --- Day 11: Seating System ---
 * Your plane lands with plenty of time to spare. The final leg of your journey is a ferry that goes directly to the tropical island where you can finally start your vacation. As you reach the waiting area to board the ferry, you realize you're so early, nobody else has even arrived yet!
 * <p>
 * By modeling the process people use to choose (or abandon) their seat in the waiting area, you're pretty sure you can predict the best place to sit. You make a quick map of the seat layout (your puzzle input).
 * <p>
 * The seat layout fits neatly on a grid. Each position is either floor (.), an empty seat (L), or an occupied seat (#). For example, the initial seat layout might look like this:
 *
 * <pre>
 * L.LL.LL.LL
 * LLLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLLL
 * L.LLLLLL.L
 * L.LLLLL.LL</pre>
 * Now, you just need to model the people who will be arriving shortly. Fortunately, people are entirely predictable and always follow a simple set of rules. All decisions are based on the number of occupied seats adjacent to a given seat (one of the eight positions immediately up, down, left, right, or diagonal from the seat). The following rules are applied to every seat simultaneously:
 *
 * <ul><li>If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.</li>
 * <li>If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.</li>
 * <li>Otherwise, the seat's state does not change.</li>
 * <li>Floor (.) never changes; seats don't move, and nobody sits on the floor.</li></ul>
 * <p>
 * After one round of these rules, every seat in the example layout becomes occupied:
 *
 * <pre>#.##.##.##
 * #######.##
 * #.#.#..#..
 * ####.##.##
 * #.##.##.##
 * #.#####.##
 * ..#.#.....
 * ##########
 * #.######.#
 * #.#####.##</pre>
 * After a second round, the seats with four or more occupied adjacent seats become empty again:
 *
 * <pre>
 * #.LL.L#.##
 * #LLLLLL.L#
 * L.L.L..L..
 * #LLL.LL.L#
 * #.LL.LL.LL
 * #.LLLL#.##
 * ..L.L.....
 * #LLLLLLLL#
 * #.LLLLLL.L
 * #.#LLLL.##</pre>
 * This process continues for three more rounds:
 *
 * <pre>
 * #.##.L#.##
 * #L###LL.L#
 * L.#.#..#..
 * #L##.##.L#
 * #.##.LL.LL
 * #.###L#.##
 * ..#.#.....
 * #L######L#
 * #.LL###L.L
 * #.#L###.##</pre>
 * <pre>
 * #.#L.L#.##
 * #LLL#LL.L#
 * L.L.L..#..
 * #LLL.##.L#
 * #.LL.LL.LL
 * #.LL#L#.##
 * ..L.L.....
 * #L#LLLL#L#
 * #.LLLLLL.L
 * #.#L#L#.##
 * </pre>
 * <pre>
 * #.#L.L#.##
 * #LLL#LL.L#
 * L.#.L..#..
 * #L##.##.L#
 * #.#L.LL.LL
 * #.#L#L#.##
 * ..L.L.....
 * #L#L##L#L#
 * #.LLLLLL.L
 * #.#L#L#.##</pre>
 * At this point, something interesting happens: the chaos stabilizes and further applications of these rules cause no seats to change state! Once people stop moving around, you count 37 occupied seats.
 * <p>
 * Simulate your seating area by applying the seating rules repeatedly until no seats change state. How many seats end up occupied?
 * <p>
 * --- Part Two ---
 * As soon as people start to arrive, you realize your mistake. People don't just care about adjacent seats - they care about the first seat they can see in each of those eight directions!
 * <p>
 * Now, instead of considering just the eight immediately adjacent seats, consider the first seat in each of those eight directions. For example, the empty seat below would see eight occupied seats:
 * <pre>
 * .......#.
 * ...#.....
 * .#.......
 * .........
 * ..#L....#
 * ....#....
 * .........
 * #........
 * ...#.....</pre>
 * The leftmost empty seat below would only see one empty seat, but cannot see any of the occupied ones:
 *
 * <pre>
 * .............
 * .L.L.#.#.#.#.
 * .............</pre>
 * The empty seat below would see no occupied seats:
 *
 * <pre>
 * .##.##.
 * #.#.#.#
 * ##...##
 * ...L...
 * ##...##
 * #.#.#.#
 * .##.##.</pre>
 * Also, people seem to be more tolerant than you expected: it now takes five or more visible occupied seats for an occupied seat to become empty (rather than four or more from the previous rules). The other rules still apply: empty seats that see no occupied seats become occupied, seats matching no rule don't change, and floor never changes.
 * <p>
 * Given the same starting layout as above, these new rules cause the seating area to shift around as follows:
 *
 * <pre>
 * L.LL.LL.LL
 * LLLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLLL
 * L.LLLLLL.L
 * L.LLLLL.LL</pre>
 * <pre>
 * #.##.##.##
 * #######.##
 * #.#.#..#..
 * ####.##.##
 * #.##.##.##
 * #.#####.##
 * ..#.#.....
 * ##########
 * #.######.#
 * #.#####.##</pre>
 * <pre>
 * #.LL.LL.L#
 * #LLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLL#
 * #.LLLLLL.L
 * #.LLLLL.L#</pre>
 * <pre>
 * #.L#.##.L#
 * #L#####.LL
 * L.#.#..#..
 * ##L#.##.##
 * #.##.#L.##
 * #.#####.#L
 * ..#.#.....
 * LLL####LL#
 * #.L#####.L
 * #.L####.L#</pre>
 * <pre>
 * #.L#.L#.L#
 * #LLLLLL.LL
 * L.L.L..#..
 * ##LL.LL.L#
 * L.LL.LL.L#
 * #.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLL#
 * #.LLLLL#.L
 * #.L#LL#.L#</pre>
 * <pre>
 * #.L#.L#.L#
 * #LLLLLL.LL
 * L.L.L..#..
 * ##L#.#L.L#
 * L.L#.#L.L#
 * #.L####.LL
 * ..#.#.....
 * LLL###LLL#
 * #.LLLLL#.L
 * #.L#LL#.L#</pre>
 * <pre>
 * #.L#.L#.L#
 * #LLLLLL.LL
 * L.L.L..#..
 * ##L#.#L.L#
 * L.L#.LL.L#
 * #.LLLL#.LL
 * ..#.L.....
 * LLL###LLL#
 * #.LLLLL#.L
 * #.L#LL#.L#</pre>
 * Again, at this point, people stop shifting around and the seating area reaches equilibrium. Once this occurs, you count 26 occupied seats.
 * <p>
 * Given the new visibility method and the rule change for occupied seats becoming empty, once equilibrium is reached, how many seats end up occupied?
 */
public class Day11 {
    private static final int EMPTY_SPACE = 0b00;
    private static final int EMPTY_SEAT = 0b10;
    private static final int BUSY_SEAT = 0b11;

    private static final int SEAT_MASK = 0b11;

    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }

    private static void part1() throws IOException {
        final int[][] map = readMap("/year2020/day11.txt");

        int iterations = 0;
        while (playLife(map)) {
            iterations++;
        }

        int occupied = Arrays.stream(map).mapToInt(row -> (int) Arrays.stream(row).filter(seat -> seat == BUSY_SEAT).count()).sum();

        System.out.println("Occupied: " + occupied);
        System.out.println("Iterations: " + iterations);
    }

    private static void part2() throws IOException {
        final int[][] map = readMap("/year2020/day11.txt");

        int iterations = 0;
        do {
            iterations++;
//            System.out.println("--------------------------------");
//            System.out.println("Iteration #" + iterations);
//            printMap(map);
        } while (playSeatsLife(map));

        int occupied = Arrays.stream(map).mapToInt(row -> (int) Arrays.stream(row).filter(seat -> seat == BUSY_SEAT).count()).sum();

        System.out.println("Occupied: " + occupied);
        System.out.println("Iterations: " + iterations);
    }

    private static void printMap(int[][] map) {
        for (int[] row : map) {
            for (int seat : row) {
                char render = switch (seat) {
                    case EMPTY_SEAT -> 'L';
                    case BUSY_SEAT -> '#';
                    default -> '.';
                };

                System.out.print(render);
            }
            System.out.println();
        }
    }

    private static boolean playLife(int[][] map) {
        boolean changed = false;
        for (int r = 0; r < map.length; r++) {
            int[] row = map[r];
            boolean topEdge = r == 0;
            boolean bottomEdge = r == map.length - 1;
            for (int c = 0; c < row.length; c++) {
                boolean leftEdge = c == 0;
                boolean rightEdge = c == row.length - 1;

                int neighbourhoods = 0;
                if (!leftEdge) {
                    if (!topEdge) {
                        neighbourhoods += map[r - 1][c - 1] & 0x1;
                    }
                    neighbourhoods += map[r][c - 1] & 0x1;
                    if (!bottomEdge) {
                        neighbourhoods += map[r + 1][c - 1] & 0x1;
                    }
                }
                if (!topEdge) {
                    neighbourhoods += map[r - 1][c] & 0x1;
                }
                if (!bottomEdge) {
                    neighbourhoods += map[r + 1][c] & 0x1;
                }
                if (!rightEdge) {
                    if (!topEdge) {
                        neighbourhoods += map[r - 1][c + 1] & 0x1;
                    }
                    neighbourhoods += map[r][c + 1] & 0x1;
                    if (!bottomEdge) {
                        neighbourhoods += map[r + 1][c + 1] & 0x1;
                    }
                }

                map[r][c] |= neighbourhoods << 2;
            }
        }
        for (int[] row : map) {
            for (int i = 0, rowLength = row.length; i < rowLength; i++) {
                int seat = row[i];
                int currentState = seat & SEAT_MASK;
                int neighbourhoods = seat >> 2;
                final int newState;
                if (currentState == EMPTY_SEAT && neighbourhoods == 0) {
                    newState = BUSY_SEAT;
                } else if (currentState == BUSY_SEAT && neighbourhoods >= 4) {
                    newState = EMPTY_SEAT;
                } else {
                    newState = currentState;
                }

                row[i] = newState;
                changed |= currentState != newState;
            }
        }
        return changed;
    }

    private static int lookForNeighbourhood(int[][] map, int r, int c, int dx, int dy) {
        int rIdx = r;
        int cIdx = c;
        while (true) {
            rIdx += dx;
            cIdx += dy;
            if (cIdx < 0 || rIdx < 0 || rIdx >= map.length) {
                return 0;
            }

            int[] row = map[rIdx];
            if (cIdx >= row.length) {
                return 0;
            }

            int seat = row[cIdx] & SEAT_MASK;
            if (seat == BUSY_SEAT) {
                return 1;
            } else if (seat == EMPTY_SEAT) {
                return 0;
            }
        }
    }

    private static boolean playSeatsLife(int[][] map) {
        boolean changed = false;
        for (int r = 0; r < map.length; r++) {
            int[] row = map[r];
            for (int c = 0; c < row.length; c++) {
                int neighbourhoods = 0;

                neighbourhoods += lookForNeighbourhood(map, r, c, -1, -1);
                neighbourhoods += lookForNeighbourhood(map, r, c, 0, -1);
                neighbourhoods += lookForNeighbourhood(map, r, c, +1, -1);
                neighbourhoods += lookForNeighbourhood(map, r, c, -1, 0);
                neighbourhoods += lookForNeighbourhood(map, r, c, +1, 0);
                neighbourhoods += lookForNeighbourhood(map, r, c, -1, +1);
                neighbourhoods += lookForNeighbourhood(map, r, c, 0, +1);
                neighbourhoods += lookForNeighbourhood(map, r, c, +1, +1);

                map[r][c] |= neighbourhoods << 2;
            }
        }
        for (int[] row : map) {
            for (int i = 0, rowLength = row.length; i < rowLength; i++) {
                int seat = row[i];
                int currentState = seat & SEAT_MASK;
                int neighbourhoods = seat >> 2;
                final int newState;
                if (currentState == EMPTY_SEAT && neighbourhoods == 0) {
                    newState = BUSY_SEAT;
                } else if (currentState == BUSY_SEAT && neighbourhoods >= 5) {
                    newState = EMPTY_SEAT;
                } else {
                    newState = currentState;
                }

                row[i] = newState;
                changed |= currentState != newState;
            }
        }
        return changed;
    }

    private static int[][] readMap(String name) throws IOException {
        try (
                final InputStream stream = Day11.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            return reader.lines().map(Day11::parseLine).toArray(int[][]::new);
        }
    }

    private static int[] parseLine(String line) {
        return line.chars().map(p -> switch (p) {
            case 'L' -> EMPTY_SEAT;
            case '#' -> BUSY_SEAT;
            default -> EMPTY_SPACE;
        }).toArray();
    }
}
