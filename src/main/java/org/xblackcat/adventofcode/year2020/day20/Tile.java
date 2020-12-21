package org.xblackcat.adventofcode.year2020.day20;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public record Tile(long number, int top, int right, int left, int bottom, char[][] imagePart) {
    public static Tile of(List<String> chunk) {
        String title = chunk.get(0);
        String no = title.substring(5, title.length() - 1);

        int top = Integer.parseInt(chunk.get(1).replace('.', '0').replace('#', '1'), 2);
        int bottom = Integer.parseInt(chunk.get(10).replace('.', '0').replace('#', '1'), 2);
        int left = 0;
        int right = 0;
        for (int i = 1; i < chunk.size(); i++) {
            String line = chunk.get(i);
            left <<= 1;
            right <<= 1;
            if (line.charAt(0) == '#') {
                left |= 1;
            }
            if (line.charAt(line.length() - 1) == '#') {
                right |= 1;
            }
        }

        return new Tile(
                Long.parseLong(no),
                top,
                right,
                left,
                bottom,
                chunk.stream().skip(2).limit(8).map(s -> s.substring(1, s.length() - 1).toCharArray()).toArray(char[][]::new)
        );
    }

    public Set<Transform> canBePlacedRightOf(int rightSide) {
        Set<Transform> possible = EnumSet.noneOf(Transform.class);
        if (rightSide == left) {
            possible.add(Transform.None);
        }
        if (rightSide == right) {
            possible.add(Transform.FlipHorizontal);
        }
        if (rightSide == top) {
            possible.add(Transform.CounterClockWiseFlip);
        }
        if (rightSide == bottom) {
            possible.add(Transform.ClockWise);
        }
        if (rightSide == Transform.flipTileSide(left)) {
            possible.add(Transform.FlipVertical);
        }
        if (rightSide == Transform.flipTileSide(right)) {
            possible.add(Transform.TurnOver);
        }
        if (rightSide == Transform.flipTileSide(top)) {
            possible.add(Transform.CounterClockWise);
        }
        if (rightSide == Transform.flipTileSide(bottom)) {
            possible.add(Transform.CounterClockWiseFlip);
        }

        return possible;
    }

    public Set<Transform> canBePlacedBottomOf(int bottomSide) {
        Set<Transform> possible = EnumSet.noneOf(Transform.class);
        if (bottomSide == left) {
            possible.add(Transform.ClockWiseFlip);
        }
        if (bottomSide == right) {
            possible.add(Transform.CounterClockWise);
        }
        if (bottomSide == top) {
            possible.add(Transform.None);
        }
        if (bottomSide == bottom) {
            possible.add(Transform.FlipVertical);
        }
        if (bottomSide == Transform.flipTileSide(left)) {
            possible.add(Transform.ClockWise);
        }
        if (bottomSide == Transform.flipTileSide(right)) {
            possible.add(Transform.CounterClockWiseFlip);
        }
        if (bottomSide == Transform.flipTileSide(top)) {
            possible.add(Transform.FlipHorizontal);
        }
        if (bottomSide == Transform.flipTileSide(bottom)) {
            possible.add(Transform.TurnOver);
        }

        return possible;
    }
}
