package org.xblackcat.adventofcode.year2020.day20;

import org.xblackcat.adventofcode.year2020.Day17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * --- Day 20: Jurassic Jigsaw ---
 * The high-speed train leaves the forest and quickly carries you south. You can even see a desert in the distance! Since you have some spare time, you might as well see if there was anything interesting in the image the Mythical Information Bureau satellite captured.
 * <p>
 * After decoding the satellite messages, you discover that the data actually contains many small images created by the satellite's camera array. The camera array consists of many cameras; rather than produce a single square image, they produce many smaller square image tiles that need to be reassembled back into a single image.
 * <p>
 * Each camera in the camera array returns a single monochrome image tile with a random unique ID number. The tiles (your puzzle input) arrived in a random order.
 * <p>
 * Worse yet, the camera array appears to be malfunctioning: each image tile has been rotated and flipped to a random orientation. Your first task is to reassemble the original image by orienting the tiles so they fit together.
 * <p>
 * To show how the tiles should be reassembled, each tile's image data includes a border that should line up exactly with its adjacent tiles. All tiles have this border, and the border lines up exactly when the tiles are both oriented correctly. Tiles at the edge of the image also have this border, but the outermost edges won't line up with any other tiles.
 * <p>
 * For example, suppose you have the following nine tiles:
 * <pre>
 * Tile 2311:
 * ..##.#..#.
 * ##..#.....
 * #...##..#.
 * ####.#...#
 * ##.##.###.
 * ##...#.###
 * .#.#.#..##
 * ..#....#..
 * ###...#.#.
 * ..###..###
 *
 * Tile 1951:
 * #.##...##.
 * #.####...#
 * .....#..##
 * #...######
 * .##.#....#
 * .###.#####
 * ###.##.##.
 * .###....#.
 * ..#.#..#.#
 * #...##.#..
 *
 * Tile 1171:
 * ####...##.
 * #..##.#..#
 * ##.#..#.#.
 * .###.####.
 * ..###.####
 * .##....##.
 * .#...####.
 * #.##.####.
 * ####..#...
 * .....##...
 *
 * Tile 1427:
 * ###.##.#..
 * .#..#.##..
 * .#.##.#..#
 * #.#.#.##.#
 * ....#...##
 * ...##..##.
 * ...#.#####
 * .#.####.#.
 * ..#..###.#
 * ..##.#..#.
 *
 * Tile 1489:
 * ##.#.#....
 * ..##...#..
 * .##..##...
 * ..#...#...
 * #####...#.
 * #..#.#.#.#
 * ...#.#.#..
 * ##.#...##.
 * ..##.##.##
 * ###.##.#..
 *
 * Tile 2473:
 * #....####.
 * #..#.##...
 * #.##..#...
 * ######.#.#
 * .#...#.#.#
 * .#########
 * .###.#..#.
 * ########.#
 * ##...##.#.
 * ..###.#.#.
 *
 * Tile 2971:
 * ..#.#....#
 * #...###...
 * #.#.###...
 * ##.##..#..
 * .#####..##
 * .#..####.#
 * #..#.#..#.
 * ..####.###
 * ..#.#.###.
 * ...#.#.#.#
 *
 * Tile 2729:
 * ...#.#.#.#
 * ####.#....
 * ..#.#.....
 * ....#..#.#
 * .##..##.#.
 * .#.####...
 * ####.#.#..
 * ##.####...
 * ##..#.##..
 * #.##...##.
 *
 * Tile 3079:
 * #.#.#####.
 * .#..######
 * ..#.......
 * ######....
 * ####.#..#.
 * .#...#.##.
 * #.#####.##
 * ..#.###...
 * ..#.......
 * ..#.###...</pre>
 * By rotating, flipping, and rearranging them, you can find a square arrangement that causes all adjacent borders to line up:
 * <pre>
 * #...##.#.. ..###..### #.#.#####.
 * ..#.#..#.# ###...#.#. .#..######
 * .###....#. ..#....#.. ..#.......
 * ###.##.##. .#.#.#..## ######....
 * .###.##### ##...#.### ####.#..#.
 * .##.#....# ##.##.###. .#...#.##.
 * #...###### ####.#...# #.#####.##
 * .....#..## #...##..#. ..#.###...
 * #.####...# ##..#..... ..#.......
 * #.##...##. ..##.#..#. ..#.###...
 *
 * #.##...##. ..##.#..#. ..#.###...
 * ##..#.##.. ..#..###.# ##.##....#
 * ##.####... .#.####.#. ..#.###..#
 * ####.#.#.. ...#.##### ###.#..###
 * .#.####... ...##..##. .######.##
 * .##..##.#. ....#...## #.#.#.#...
 * ....#..#.# #.#.#.##.# #.###.###.
 * ..#.#..... .#.##.#..# #.###.##..
 * ####.#.... .#..#.##.. .######...
 * ...#.#.#.# ###.##.#.. .##...####
 *
 * ...#.#.#.# ###.##.#.. .##...####
 * ..#.#.###. ..##.##.## #..#.##..#
 * ..####.### ##.#...##. .#.#..#.##
 * #..#.#..#. ...#.#.#.. .####.###.
 * .#..####.# #..#.#.#.# ####.###..
 * .#####..## #####...#. .##....##.
 * ##.##..#.. ..#...#... .####...#.
 * #.#.###... .##..##... .####.##.#
 * #...###... ..##...#.. ...#..####
 * ..#.#....# ##.#.#.... ...##.....</pre>
 * For reference, the IDs of the above tiles are:
 * <pre>
 * 1951    2311    3079
 * 2729    1427    2473
 * 2971    1489    1171</pre>
 * To check that you've assembled the image correctly, multiply the IDs of the four corner tiles together. If you do this with the assembled tiles from the example above, you get 1951 * 3079 * 2971 * 1171 = 20899048083289.
 * <p>
 * Assemble the tiles into an image. What do you get if you multiply together the IDs of the four corner tiles?
 * <p>
 * --- Part Two ---
 * Now, you're ready to check the image for sea monsters.
 * <p>
 * The borders of each tile are not part of the actual image; start by removing them.
 * <p>
 * In the example above, the tiles become:
 * <pre>
 * .#.#..#. ##...#.# #..#####
 * ###....# .#....#. .#......
 * ##.##.## #.#.#..# #####...
 * ###.#### #...#.## ###.#..#
 * ##.#.... #.##.### #...#.##
 * ...##### ###.#... .#####.#
 * ....#..# ...##..# .#.###..
 * .####... #..#.... .#......
 *
 * #..#.##. .#..###. #.##....
 * #.####.. #.####.# .#.###..
 * ###.#.#. ..#.#### ##.#..##
 * #.####.. ..##..## ######.#
 * ##..##.# ...#...# .#.#.#..
 * ...#..#. .#.#.##. .###.###
 * .#.#.... #.##.#.. .###.##.
 * ###.#... #..#.##. ######..
 *
 * .#.#.### .##.##.# ..#.##..
 * .####.## #.#...## #.#..#.#
 * ..#.#..# ..#.#.#. ####.###
 * #..####. ..#.#.#. ###.###.
 * #####..# ####...# ##....##
 * #.##..#. .#...#.. ####...#
 * .#.###.. ##..##.. ####.##.
 * ...###.. .##...#. ..#..###</pre>
 * Remove the gaps to form the actual image:
 * <pre>
 * .#.#..#.##...#.##..#####
 * ###....#.#....#..#......
 * ##.##.###.#.#..######...
 * ###.#####...#.#####.#..#
 * ##.#....#.##.####...#.##
 * ...########.#....#####.#
 * ....#..#...##..#.#.###..
 * .####...#..#.....#......
 * #..#.##..#..###.#.##....
 * #.####..#.####.#.#.###..
 * ###.#.#...#.######.#..##
 * #.####....##..########.#
 * ##..##.#...#...#.#.#.#..
 * ...#..#..#.#.##..###.###
 * .#.#....#.##.#...###.##.
 * ###.#...#..#.##.######..
 * .#.#.###.##.##.#..#.##..
 * .####.###.#...###.#..#.#
 * ..#.#..#..#.#.#.####.###
 * #..####...#.#.#.###.###.
 * #####..#####...###....##
 * #.##..#..#...#..####...#
 * .#.###..##..##..####.##.
 * ...###...##...#...#..###</pre>
 * Now, you're ready to search for sea monsters! Because your image is monochrome, a sea monster will look like this:
 * <pre>
 *                   #
 * #    ##    ##    ###
 *  #  #  #  #  #  #   </pre>
 * When looking for this pattern in the image, the spaces can be anything; only the # need to match. Also, you might need to rotate or flip your image before it's oriented correctly to find sea monsters. In the above image, after flipping and rotating it to the appropriate orientation, there are two sea monsters (marked with O):
 * <pre>
 * .####...#####..#...###..
 * #####..#..#.#.####..#.#.
 * .#.#...#.###...#.##.O#..
 * #.O.##.OO#.#.OO.##.OOO##
 * ..#O.#O#.O##O..O.#O##.##
 * ...#.#..##.##...#..#..##
 * #.##.#..#.#..#..##.#.#..
 * .###.##.....#...###.#...
 * #.####.#.#....##.#..#.#.
 * ##...#..#....#..#...####
 * ..#.##...###..#.#####..#
 * ....#.##.#.#####....#...
 * ..##.##.###.....#.##..#.
 * #...#...###..####....##.
 * .#.##...#.##.#.#.###...#
 * #.###.#..####...##..#...
 * #.###...#.##...#.##O###.
 * .O##.#OO.###OO##..OOO##.
 * ..O#.O..O..O.#O##O##.###
 * #.#..##.########..#..##.
 * #.#####..#.#...##..#....
 * #....##..#.#########..##
 * #...#.....#..##...###.##
 * #..###....##.#...##.##.#</pre>
 * Determine how rough the waters are in the sea monsters' habitat by counting the number of # that are not part of a sea monster. In the above example, the habitat's water roughness is 273.
 * <p>
 * How many # are not part of a sea monster?
 */
public class Day20 {

    public static void main(String[] args) throws IOException {
        // Part 1
        List<Tile> tiles = loadTiles("/year2020/day20.txt");

        int size = (int) Math.sqrt(tiles.size());

        final PlacedTile[][] puzzle = new PlacedTile[size][size];
        Set<Tile> usedTiles = new HashSet<>();

        if (!fillTiles(tiles, puzzle, usedTiles, 0, size)) {
            System.out.println("Nope...");
            return;
        }
        System.out.println("Result: " +
                                   (puzzle[0][0].tile().number() *
                                           puzzle[0][size - 1].tile().number() *
                                           puzzle[size - 1][0].tile().number() *
                                           puzzle[size - 1][size - 1].tile().number()));

        final Image image = Image.from(puzzle);
        final SeaMonster monster = new SeaMonster();
        for (Transform t : Transform.values()) {
            final Image searchImage = new Image(t.transform(image.raw()));

            final int amount = monster.eatImage(searchImage);
            if (amount > 0) {
                System.out.println(t);
                System.out.println("Monsters: " + amount);

                int water = 0;
                for (var row : searchImage.raw()) {
                    for (char cell : row) {
                        if (cell == '#') {
                            water++;
                        }
                    }
                }

                System.out.println("Water: " + water);
            }
        }
    }

    private static boolean fillTiles(List<Tile> tiles, PlacedTile[][] image, Set<Tile> usedTiles, int pos, int size) {
        if (usedTiles.size() == tiles.size()) {
            return true;
        }
        for (Tile tile : tiles) {
            if (usedTiles.contains(tile)) {
                continue;
            }
            final int x = pos % size;
            final int y = pos / size;
            Set<Transform> transforms = getPossibleTransformsFor(image, tile, x, y);
            if (transforms.isEmpty()) {
                continue;
            }
            usedTiles.add(tile);
            for (Transform transform : transforms) {
                image[x][y] = new PlacedTile(tile, transform);
                if (fillTiles(tiles, image, usedTiles, pos + 1, size)) {
                    return true;
                }
            }
            usedTiles.remove(tile);
        }
        return false;
    }


    private static Set<Transform> getPossibleTransformsFor(
            PlacedTile[][] image,
            Tile tile,
            int x,
            int y
    ) {
        final EnumSet<Transform> transforms = EnumSet.allOf(Transform.class);
        if (x > 0) {
            transforms.retainAll(tile.canBePlacedBottomOf(image[x - 1][y].getBottomSide()));
        }
        if (y > 0) {
            transforms.retainAll(tile.canBePlacedRightOf(image[x][y - 1].getRightSide()));
        }

        return transforms;
    }

    private static List<Tile> loadTiles(String name) throws IOException {
        List<Tile> tiles = new ArrayList<>();
        try (
                final InputStream stream = Day17.class.getResourceAsStream(name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            List<String> chunk = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    tiles.add(Tile.of(chunk));
                    chunk.clear();
                } else {
                    chunk.add(line);
                }
            }

            tiles.add(Tile.of(chunk));
        }
        return tiles;
    }
}
