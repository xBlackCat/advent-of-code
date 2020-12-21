package org.xblackcat.adventofcode.year2020.day20;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 */
public record PlacedTile(Tile tile, Transform transform) {
    public int getRightSide() {
        return transform.getRight(tile);
    }

    public int getBottomSide() {
        return transform.getBottom(tile);
    }

    public char[][] getPart() {
        return transform.transform(tile.imagePart());
    }

    @Override
    public String toString() {
        return Arrays.stream(getPart()).map(String::new).collect(Collectors.joining("\n"));
    }
}
