package org.xblackcat.adventofcode.year2020.day20;

import java.util.Arrays;

/**
 *
 */
public enum Transform {
    None {
        @Override
        public int getRight(Tile tile) {
            return tile.right();
        }

        @Override
        public int getBottom(Tile tile) {
            return tile.bottom();
        }

        @Override
        public char[][] transform(char[][] part) {
            return Arrays.stream(part).map(char[]::clone).toArray(char[][]::new);
        }
    },
    ClockWise {
        @Override
        public int getRight(Tile tile) {
            return tile.top();
        }

        @Override
        public int getBottom(Tile tile) {
            return flipTileSide(tile.right());
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[size - y][x];
                }
            }
            return chars;
        }
    },
    TurnOver {
        @Override
        public int getRight(Tile tile) {
            return flipTileSide(tile.left());
        }

        @Override
        public int getBottom(Tile tile) {
            return flipTileSide(tile.top());
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[size - x][size - y];
                }
            }
            return chars;
        }
    },
    CounterClockWise {
        @Override
        public int getRight(Tile tile) {
            return flipTileSide(tile.bottom());
        }

        @Override
        public int getBottom(Tile tile) {
            return tile.left();
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[y][size - x];
                }
            }
            return chars;
        }
    },
    FlipVertical {
        @Override
        public int getRight(Tile tile) {
            return flipTileSide(tile.right());
        }

        @Override
        public int getBottom(Tile tile) {
            return tile.top();
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[size - x][y];
                }
            }
            return chars;
        }
    },
    FlipHorizontal {
        @Override
        public int getRight(Tile tile) {
            return tile.left();
        }

        @Override
        public int getBottom(Tile tile) {
            return flipTileSide(tile.bottom());
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[x][size - y];
                }
            }
            return chars;
        }
    },
    ClockWiseFlip {
        @Override
        public int getRight(Tile tile) {
            return tile.bottom();
        }

        @Override
        public int getBottom(Tile tile) {
            return tile.right();
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[y][x];
                }
            }
            return chars;
        }
    },
    CounterClockWiseFlip {
        @Override
        public int getRight(Tile tile) {
            return flipTileSide(tile.top());
        }

        @Override
        public int getBottom(Tile tile) {
            return flipTileSide(tile.left());
        }

        @Override
        public char[][] transform(char[][] part) {
            final int size = part.length - 1;
            final char[][] chars = new char[size + 1][size + 1];
            for (int x = 0; x <= size; x++) {
                for (int y = 0; y <= size; y++) {
                    chars[x][y] = part[size - y][size - x];
                }
            }
            return chars;
        }
    },
    ;

    static int flipTileSide(int value) {
        return Integer.reverse(value) >>> 22;
    }

    public abstract int getRight(Tile tile);

    public abstract int getBottom(Tile tile);

    public abstract char[][] transform(char[][] part);
}
