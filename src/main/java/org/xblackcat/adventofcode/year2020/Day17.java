package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * --- Day 17: Conway Cubes ---
 * As your flight slowly drifts through the sky, the Elves at the Mythical Information Bureau at the North Pole contact you. They'd like some help debugging a malfunctioning experimental energy source aboard one of their super-secret imaging satellites.
 * <p>
 * The experimental energy source is based on cutting-edge technology: a set of Conway Cubes contained in a pocket dimension! When you hear it's having problems, you can't help but agree to take a look.
 * <p>
 * The pocket dimension contains an infinite 3-dimensional grid. At every integer 3-dimensional coordinate (x,y,z), there exists a single cube which is either active or inactive.
 * <p>
 * In the initial state of the pocket dimension, almost all cubes start inactive. The only exception to this is a small flat region of cubes (your puzzle input); the cubes in this region start in the specified active (#) or inactive (.) state.
 * <p>
 * The energy source then proceeds to boot up by executing six cycles.
 * <p>
 * Each cube only ever considers its neighbors: any of the 26 other cubes where any of their coordinates differ by at most 1. For example, given the cube at x=1,y=2,z=3, its neighbors include the cube at x=2,y=2,z=2, the cube at x=0,y=2,z=3, and so on.
 * <p>
 * During a cycle, all cubes simultaneously change their state according to the following rules:
 * <ul>
 * <li>If a cube is active and exactly 2 or 3 of its neighbors are also active, the cube remains active. Otherwise, the cube becomes inactive.</li>
 * <li>If a cube is inactive but exactly 3 of its neighbors are active, the cube becomes active. Otherwise, the cube remains inactive.</li>
 * </ul> The engineers responsible for this experimental energy source would like you to simulate the pocket dimension and determine what the configuration of cubes should be at the end of the six-cycle boot process.
 * <p>
 * For example, consider the following initial state:
 * <pre>
 * .#.
 * ..#
 * ###</pre>
 * Even though the pocket dimension is 3-dimensional, this initial state represents a small 2-dimensional slice of it. (In particular, this initial state defines a 3x3x1 region of the 3-dimensional space.)
 * <p>
 * Simulating a few cycles from this initial state produces the following configurations, where the result of each cycle is shown layer-by-layer at each given z coordinate:
 * <pre>
 * Before any cycles:
 *
 * z=0
 * .#.
 * ..#
 * ###
 *
 *
 * After 1 cycle:
 *
 * z=-1
 * #..
 * ..#
 * .#.
 *
 * z=0
 * #.#
 * .##
 * .#.
 *
 * z=1
 * #..
 * ..#
 * .#.
 *
 *
 * After 2 cycles:
 *
 * z=-2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-1
 * ..#..
 * .#..#
 * ....#
 * .#...
 * .....
 *
 * z=0
 * ##...
 * ##...
 * #....
 * ....#
 * .###.
 *
 * z=1
 * ..#..
 * .#..#
 * ....#
 * .#...
 * .....
 *
 * z=2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 *
 * After 3 cycles:
 *
 * z=-2
 * .......
 * .......
 * ..##...
 * ..###..
 * .......
 * .......
 * .......
 *
 * z=-1
 * ..#....
 * ...#...
 * #......
 * .....##
 * .#...#.
 * ..#.#..
 * ...#...
 *
 * z=0
 * ...#...
 * .......
 * #......
 * .......
 * .....##
 * .##.#..
 * ...#...
 *
 * z=1
 * ..#....
 * ...#...
 * #......
 * .....##
 * .#...#.
 * ..#.#..
 * ...#...
 *
 * z=2
 * .......
 * .......
 * ..##...
 * ..###..
 * .......
 * .......
 * .......
 * </pre>After the full six-cycle boot process completes, 112 cubes are left in the active state.
 * <p>
 * Starting with your given initial configuration, simulate six cycles. How many cubes are left in the active state after the sixth cycle?
 * <p>
 * --- Part Two ---
 * For some reason, your simulated results don't match what the experimental energy source engineers expected. Apparently, the pocket dimension actually has four spatial dimensions, not three.
 * <p>
 * The pocket dimension contains an infinite 4-dimensional grid. At every integer 4-dimensional coordinate (x,y,z,w), there exists a single cube (really, a hypercube) which is still either active or inactive.
 * <p>
 * Each cube only ever considers its neighbors: any of the 80 other cubes where any of their coordinates differ by at most 1. For example, given the cube at x=1,y=2,z=3,w=4, its neighbors include the cube at x=2,y=2,z=3,w=3, the cube at x=0,y=2,z=3,w=4, and so on.
 * <p>
 * The initial state of the pocket dimension still consists of a small flat region of cubes. Furthermore, the same rules for cycle updating still apply: during each cycle, consider the number of active neighbors of each cube.
 * <p>
 * For example, consider the same initial state as in the example above. Even though the pocket dimension is 4-dimensional, this initial state represents a small 2-dimensional slice of it. (In particular, this initial state defines a 3x3x1x1 region of the 4-dimensional space.)
 * <p>
 * Simulating a few cycles from this initial state produces the following configurations, where the result of each cycle is shown layer-by-layer at each given z and w coordinate:
 * <pre>
 * Before any cycles:
 *
 * z=0, w=0
 * .#.
 * ..#
 * ###
 *
 *
 * After 1 cycle:
 *
 * z=-1, w=-1
 * #..
 * ..#
 * .#.
 *
 * z=0, w=-1
 * #..
 * ..#
 * .#.
 *
 * z=1, w=-1
 * #..
 * ..#
 * .#.
 *
 * z=-1, w=0
 * #..
 * ..#
 * .#.
 *
 * z=0, w=0
 * #.#
 * .##
 * .#.
 *
 * z=1, w=0
 * #..
 * ..#
 * .#.
 *
 * z=-1, w=1
 * #..
 * ..#
 * .#.
 *
 * z=0, w=1
 * #..
 * ..#
 * .#.
 *
 * z=1, w=1
 * #..
 * ..#
 * .#.
 *
 *
 * After 2 cycles:
 *
 * z=-2, w=-2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-1, w=-2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=-2
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=1, w=-2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=-2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-2, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-1, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=1, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-2, w=0
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=-1, w=0
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=0
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=1, w=0
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=0
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=-2, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-1, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=1, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-2, w=2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-1, w=2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=2
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=1, w=2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=2
 * .....
 * .....
 * ..#..
 * .....
 * .....</pre>
 * After the full six-cycle boot process completes, 848 cubes are left in the active state.
 * <p>
 * Starting with your given initial configuration, simulate six cycles in a 4-dimensional space. How many cubes are left in the active state after the sixth cycle?
 */
public class Day17 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        int iterations = 6;
        final int[][] slice = readMap("/year2020/day17.txt");

        final int[][][] map = expand3D(slice, iterations);

        for (int i = 0; i < iterations; i++) {
            playLife3D(map);
        }

        int occupied = Arrays.stream(map)
                .mapToInt(s ->
                                  Arrays.stream(s)
                                          .mapToInt(r ->
                                                            (int) Arrays.stream(r)
                                                                    .filter(seat -> (seat & 1) == 1)
                                                                    .count()
                                          ).sum()
                ).sum();

        System.out.println("Occupied: " + occupied);
        System.out.println("Iterations: " + iterations);
    }

    private static void part2() throws IOException {
        int iterations = 6;
        final int[][] slice = readMap("/year2020/day17.txt");

        final int[][][][] space4D = expand4D(slice, iterations);

        for (int i = 0; i < iterations; i++) {
            playLife4D(space4D);
        }

        int occupied = Arrays.stream(space4D)
                .mapToInt(s ->
                                  Arrays.stream(s)
                                          .mapToInt(m ->
                                                            Arrays.stream(m)
                                                                    .mapToInt(r ->
                                                                                      (int) Arrays.stream(r)
                                                                                              .filter(seat -> (seat & 1) == 1)
                                                                                              .count()
                                                                    ).sum()
                                          ).sum()
                ).sum();

        System.out.println("Occupied: " + occupied);
        System.out.println("Iterations: " + iterations);
    }

    private static int[][][] expand3D(int[][] centerSlice, int iterations) {
        int width = centerSlice.length;
        int height = centerSlice[0].length;
        int depth = 1;

        final int grow = iterations + 1;
        final int[][][] map3D = new int[depth + (grow << 1)][width + (grow << 1)][height + (grow << 1)];
        for (int i = 0; i < width; i++) {
            System.arraycopy(centerSlice[i], 0, map3D[grow][i + grow], grow, height);
        }

        return map3D;
    }

    private static int[][][][] expand4D(int[][] centerSlice, int iterations) {
        int width = centerSlice.length;
        int height = centerSlice[0].length;
        int depth = 1;
        int hyperDepth = 1;

        final int grow = iterations + 1;
        final int[][][][] map4D = new int[hyperDepth + (grow << 1)][depth + (grow << 1)][width + (grow << 1)][height + (grow << 1)];
        for (int i = 0; i < width; i++) {
            System.arraycopy(centerSlice[i], 0, map4D[grow][grow][i + grow], grow, height);
        }

        return map4D;

    }

    private static boolean playLife3D(int[][][] map) {
        boolean changed = false;
        for (int s = 1; s < map.length - 1; s++) {
            int[][] slice = map[s];
            for (int c = 1; c < slice.length - 1; c++) {
                int[] row = slice[c];
                for (int r = 1; r < row.length - 1; r++) {
                    int neighbourhoods = 0;
                    neighbourhoods += map[s - 1][c - 1][r - 1] & 0x1;
                    neighbourhoods += map[s - 1][c][r - 1] & 0x1;
                    neighbourhoods += map[s - 1][c + 1][r - 1] & 0x1;
                    neighbourhoods += map[s - 1][c - 1][r] & 0x1;
                    neighbourhoods += map[s - 1][c][r] & 0x1;
                    neighbourhoods += map[s - 1][c + 1][r] & 0x1;
                    neighbourhoods += map[s - 1][c - 1][r + 1] & 0x1;
                    neighbourhoods += map[s - 1][c][r + 1] & 0x1;
                    neighbourhoods += map[s - 1][c + 1][r + 1] & 0x1;

                    neighbourhoods += map[s][c - 1][r - 1] & 0x1;
                    neighbourhoods += map[s][c][r - 1] & 0x1;
                    neighbourhoods += map[s][c + 1][r - 1] & 0x1;
                    neighbourhoods += map[s][c - 1][r] & 0x1;
                    neighbourhoods += map[s][c + 1][r] & 0x1;
                    neighbourhoods += map[s][c - 1][r + 1] & 0x1;
                    neighbourhoods += map[s][c][r + 1] & 0x1;
                    neighbourhoods += map[s][c + 1][r + 1] & 0x1;

                    neighbourhoods += map[s + 1][c - 1][r - 1] & 0x1;
                    neighbourhoods += map[s + 1][c][r - 1] & 0x1;
                    neighbourhoods += map[s + 1][c + 1][r - 1] & 0x1;
                    neighbourhoods += map[s + 1][c - 1][r] & 0x1;
                    neighbourhoods += map[s + 1][c][r] & 0x1;
                    neighbourhoods += map[s + 1][c + 1][r] & 0x1;
                    neighbourhoods += map[s + 1][c - 1][r + 1] & 0x1;
                    neighbourhoods += map[s + 1][c][r + 1] & 0x1;
                    neighbourhoods += map[s + 1][c + 1][r + 1] & 0x1;

                    map[s][c][r] |= neighbourhoods << 1;
                }
            }
        }
        for (int[][] slice : map) {
            for (int[] row : slice) {
                for (int i = 0, rowLength = row.length; i < rowLength; i++) {
                    int cell = row[i];
                    boolean currentActive = (cell & 1) == 1;
                    boolean newActiveState = cell == 5 || cell == 6 || cell == 7;

                    row[i] = newActiveState ? 1 : 0;
                    changed |= currentActive != newActiveState;
                }
            }
        }
        return changed;
    }

    private static boolean playLife4D(int[][][][] space) {
        boolean changed = false;
        for (int h = 1; h < space.length - 1; h++) {
            final int[][][] map = space[h];
            for (int s = 1; s < map.length - 1; s++) {
                int[][] slice = map[s];
                for (int c = 1; c < slice.length - 1; c++) {
                    int[] row = slice[c];
                    for (int r = 1; r < row.length - 1; r++) {
                        int neighbourhoods = 0;
                        // Hyper deep lower
                        neighbourhoods += space[h - 1][s - 1][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c - 1][r] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c][r] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c + 1][r] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c][r + 1] & 0x1;
                        neighbourhoods += space[h - 1][s - 1][c + 1][r + 1] & 0x1;

                        neighbourhoods += space[h - 1][s][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s][c][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s][c - 1][r] & 0x1;
                        neighbourhoods += space[h - 1][s][c][r] & 0x1;
                        neighbourhoods += space[h - 1][s][c + 1][r] & 0x1;
                        neighbourhoods += space[h - 1][s][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h - 1][s][c][r + 1] & 0x1;
                        neighbourhoods += space[h - 1][s][c + 1][r + 1] & 0x1;

                        neighbourhoods += space[h - 1][s + 1][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c - 1][r] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c][r] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c + 1][r] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c][r + 1] & 0x1;
                        neighbourhoods += space[h - 1][s + 1][c + 1][r + 1] & 0x1;

                        // Hyper depp this
                        neighbourhoods += space[h][s - 1][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h][s - 1][c][r - 1] & 0x1;
                        neighbourhoods += space[h][s - 1][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h][s - 1][c - 1][r] & 0x1;
                        neighbourhoods += space[h][s - 1][c][r] & 0x1;
                        neighbourhoods += space[h][s - 1][c + 1][r] & 0x1;
                        neighbourhoods += space[h][s - 1][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h][s - 1][c][r + 1] & 0x1;
                        neighbourhoods += space[h][s - 1][c + 1][r + 1] & 0x1;

                        neighbourhoods += space[h][s][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h][s][c][r - 1] & 0x1;
                        neighbourhoods += space[h][s][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h][s][c - 1][r] & 0x1;
                        neighbourhoods += space[h][s][c + 1][r] & 0x1;
                        neighbourhoods += space[h][s][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h][s][c][r + 1] & 0x1;
                        neighbourhoods += space[h][s][c + 1][r + 1] & 0x1;

                        neighbourhoods += space[h][s + 1][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h][s + 1][c][r - 1] & 0x1;
                        neighbourhoods += space[h][s + 1][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h][s + 1][c - 1][r] & 0x1;
                        neighbourhoods += space[h][s + 1][c][r] & 0x1;
                        neighbourhoods += space[h][s + 1][c + 1][r] & 0x1;
                        neighbourhoods += space[h][s + 1][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h][s + 1][c][r + 1] & 0x1;
                        neighbourhoods += space[h][s + 1][c + 1][r + 1] & 0x1;

                        // Hyper deep upper
                        neighbourhoods += space[h + 1][s - 1][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c - 1][r] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c][r] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c + 1][r] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c][r + 1] & 0x1;
                        neighbourhoods += space[h + 1][s - 1][c + 1][r + 1] & 0x1;

                        neighbourhoods += space[h + 1][s][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s][c][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s][c - 1][r] & 0x1;
                        neighbourhoods += space[h + 1][s][c][r] & 0x1;
                        neighbourhoods += space[h + 1][s][c + 1][r] & 0x1;
                        neighbourhoods += space[h + 1][s][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h + 1][s][c][r + 1] & 0x1;
                        neighbourhoods += space[h + 1][s][c + 1][r + 1] & 0x1;

                        neighbourhoods += space[h + 1][s + 1][c - 1][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c + 1][r - 1] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c - 1][r] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c][r] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c + 1][r] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c - 1][r + 1] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c][r + 1] & 0x1;
                        neighbourhoods += space[h + 1][s + 1][c + 1][r + 1] & 0x1;

                        space[h][s][c][r] |= neighbourhoods << 1;
                    }
                }
            }
        }
        for (int[][][] map : space) {
            for (int[][] slice : map) {
                for (int[] row : slice) {
                    for (int i = 0, rowLength = row.length; i < rowLength; i++) {
                        int cell = row[i];
                        boolean currentActive = (cell & 1) == 1;
                        boolean newActiveState = cell == 5 || cell == 6 || cell == 7;

                        row[i] = newActiveState ? 1 : 0;
                        changed |= currentActive != newActiveState;
                    }
                }
            }
        }
        return changed;
    }

    private static int[][] readMap(String name) throws IOException {
        try (
                final InputStream stream = Day17.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            return reader.lines().map(Day17::parseLine).toArray(int[][]::new);
        }
    }

    private static int[] parseLine(String line) {
        return line.chars().map(p -> p == '#' ? 1 : 0).toArray();
    }
}
