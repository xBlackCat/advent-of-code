package org.xblackcat.adventofcode.year2020.day20;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 */
public record Image(char[][] raw) {
    public static Image from(PlacedTile[][] tiles) {
        final int sizeTiles = tiles.length;
        char[][] image = new char[sizeTiles << 3][sizeTiles << 3];
        int x = 0;
        int y = 0;
        for (PlacedTile[] column : tiles) {
            y = 0;
            for (PlacedTile cell : column) {
                final char[][] part = cell.getPart();
                for (int i = 0; i < 8; i++) {
                    System.arraycopy(part[i], 0, image[x + i], y, 8);
                }
                y += 8;
            }
            x += 8;
        }
        return new Image(image);
    }

    @Override
    public String toString() {
        return Arrays.stream(raw).map(String::new).collect(Collectors.joining("\n"));
    }
}
