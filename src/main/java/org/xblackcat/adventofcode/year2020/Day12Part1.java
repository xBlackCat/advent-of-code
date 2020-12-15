package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * --- Day 12: Rain Risk ---
 * Your ferry made decent progress toward the island, but the storm came in faster than anyone expected. The ferry needs to take evasive actions!
 * <p>
 * Unfortunately, the ship's navigation computer seems to be malfunctioning; rather than giving a route directly to safety, it produced extremely circuitous instructions. When the captain uses the PA system to ask if anyone can help, you quickly volunteer.
 * <p>
 * The navigation instructions (your puzzle input) consists of a sequence of single-character actions paired with integer input values. After staring at them for a few minutes, you work out what they probably mean:
 *
 * <ul>
 * <li>Action N means to move north by the given value.</li>
 * <li>Action S means to move south by the given value.</li>
 * <li>Action E means to move east by the given value.</li>
 * <li>Action W means to move west by the given value.</li>
 * <li>Action L means to turn left the given number of degrees.</li>
 * <li>Action R means to turn right the given number of degrees.</li>
 * <li>Action F means to move forward by the given value in the direction the ship is currently facing.</li>
 * <li>The ship starts by facing east.</li> Only the L and R actions change the direction the ship is facing. (That is, if the ship is facing east and the next instruction is N10, the ship would move north 10 units, but would still move east if the following action were F.)
 * </ul>
 * For example:
 * <pre>
 * F10
 * N3
 * F7
 * R90
 * F11</pre>
 * These instructions would be handled as follows:
 *
 * <ul>
 *     <li>F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.</li>
 * <li>N3 would move the ship 3 units north to east 10, north 3.</li>
 * <li>F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.</li>
 * <li>R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.</li>
 * <li>F11 would move the ship 11 units south to east 17, south 8.</li>
 * </ul>At the end of these instructions, the ship's Manhattan distance (sum of the absolute values of its east/west position and its north/south position) from its starting position is 17 + 8 = 25.
 * <p>
 * Figure out where the navigation instructions lead. What is the Manhattan distance between that location and the ship's starting position?
 * <p>
 */
public class Day12Part1 {
    public static void main(String[] args) throws IOException {
        final Plane plane = new Plane();

        try (
                final InputStream stream = Day12Part1.class.getResourceAsStream("/year2020/day12.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            reader.lines().map(Day12Part1::parseInstruction).forEach(plane::apply);
        }

        System.out.println(plane);
        System.out.println("Manhattan distance: " + plane.getManhattanDistance());
    }

    private static Instruction parseInstruction(String s) {
        return new Instruction(Action.find(s), Integer.parseInt(s.substring(1)));
    }

    private interface Action {
        String name();

        default int getEastOffset(Direction currentDirection, int actionValue) {
            return 0;
        }

        default int getNorthOffset(Direction currentDirection, int actionValue) {
            return 0;
        }

        default Direction getNewDirection(Direction currentDirection, int actionValue) {
            return currentDirection;
        }

        default boolean matches(String action) {
            return action.charAt(0) == name().charAt(0);
        }

        static Action find(String s) {
            Action a = null;
            for (Action possible : Direction.values()) {
                if (possible.matches(s)) {
                    a = possible;
                    break;
                }
            }
            if (a == null) {
                for (Action possible : RelativeAction.values()) {
                    if (possible.matches(s)) {
                        a = possible;
                        break;
                    }
                }
            }

            if (a == null) {
                throw new IllegalArgumentException("Invalid instruction: " + s);
            }
            return a;
        }
    }

    private static class Plane {
        private int north = 0;
        private int east = 0;
        private Direction direction = Direction.East;

        public void apply(Instruction i) {
            north += i.getNorthOffset(direction);
            east += i.getEastOffset(direction);
            direction = i.getNewDirection(direction);
        }

        public int getManhattanDistance() {
            return Math.abs(north) + Math.abs(east);
        }

        @Override
        public String toString() {
            return "Plane directed to " + direction + ". Position: north = " + north + ", east = " + east;
        }
    }

    private enum Direction implements Action {
        North {
            @Override
            public int getNorthOffset(Direction currentDirection, int actionValue) {
                return actionValue;
            }
        },
        East {
            @Override
            public int getEastOffset(Direction currentDirection, int actionValue) {
                return actionValue;
            }
        },
        South {
            @Override
            public int getNorthOffset(Direction currentDirection, int actionValue) {
                return -actionValue;
            }
        },
        West {
            @Override
            public int getEastOffset(Direction currentDirection, int actionValue) {
                return -actionValue;
            }
        }
    }

    private enum RelativeAction implements Action {
        LeftTurn {
            @Override
            public Direction getNewDirection(Direction currentDirection, int actionValue) {
                final Direction[] directions = Direction.values();
                final int totalDirection = directions.length;
                final int idx = (currentDirection.ordinal() + totalDirection - (actionValue / 90) % totalDirection) % totalDirection;
                return directions[idx];
            }
        },
        RightTurn {
            @Override
            public Direction getNewDirection(Direction currentDirection, int actionValue) {
                final Direction[] directions = Direction.values();
                return directions[(currentDirection.ordinal() + actionValue / 90) % directions.length];
            }
        },
        Forward {
            @Override
            public int getEastOffset(Direction currentDirection, int actionValue) {
                return currentDirection.getEastOffset(currentDirection, actionValue);
            }

            @Override
            public int getNorthOffset(Direction currentDirection, int actionValue) {
                return currentDirection.getNorthOffset(currentDirection, actionValue);
            }
        },
    }

    private record Instruction(Action action, int value) {
        int getEastOffset(Direction currentDirection) {
            return action.getEastOffset(currentDirection, value);
        }

        int getNorthOffset(Direction currentDirection) {
            return action.getNorthOffset(currentDirection, value);
        }

        Direction getNewDirection(Direction currentDirection) {
            return action.getNewDirection(currentDirection, value);
        }

    }
}
