package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * --- Day 12: Rain Risk ---
 * --- Part Two ---
 * Before you can give the destination to the captain, you realize that the actual action meanings were printed on the back of the instructions the whole time.
 * <p>
 * Almost all of the actions indicate how to move a waypoint which is relative to the ship's position:
 * <ul>
 * <li>Action N means to move the waypoint north by the given value.</li>
 * <li>Action S means to move the waypoint south by the given value.</li>
 * <li>Action E means to move the waypoint east by the given value.</li>
 * <li>Action W means to move the waypoint west by the given value.</li>
 * <li>Action L means to rotate the waypoint around the ship left (counter-clockwise) the given number of degrees.</li>
 * <li>Action R means to rotate the waypoint around the ship right (clockwise) the given number of degrees.</li>
 * <li>Action F means to move forward to the waypoint a number of times equal to the given value.</li>
 * </ul>The waypoint starts 10 units east and 1 unit north relative to the ship. The waypoint is relative to the ship; that is, if the ship moves, the waypoint moves with it.
 * <p>
 * For example, using the same instructions as above:
 *
 * <ul>
 *     <li>F10 moves the ship to the waypoint 10 times (a total of 100 units east and 10 units north), leaving the ship at east 100, north 10. The waypoint stays 10 units east and 1 unit north of the ship.</li>
 * <li>N3 moves the waypoint 3 units north to 10 units east and 4 units north of the ship. The ship remains at east 100, north 10.</li>
 * <li>F7 moves the ship to the waypoint 7 times (a total of 70 units east and 28 units north), leaving the ship at east 170, north 38. The waypoint stays 10 units east and 4 units north of the ship.</li>
 * <li>R90 rotates the waypoint around the ship clockwise 90 degrees, moving it to 4 units east and 10 units south of the ship. The ship remains at east 170, north 38.</li>
 * <li>F11 moves the ship to the waypoint 11 times (a total of 44 units east and 110 units south), leaving the ship at east 214, south 72. The waypoint stays 4 units east and 10 units south of the ship.</li>
 * </ul>After these operations, the ship's Manhattan distance from its starting position is 214 + 72 = 286.
 * <p>
 * Figure out where the navigation instructions actually lead. What is the Manhattan distance between that location and the ship's starting position?
 */
public class Day12Part2 {
    public static void main(String[] args) throws IOException {
        final Plane plane = new Plane();

        try (
                final InputStream stream = Day12Part2.class.getResourceAsStream("/year2020/day12.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            reader.lines().map(Day12Part2::parseInstruction).forEach(plane::apply);
        }

        System.out.println(plane);
        System.out.println("Manhattan distance: " + plane.getManhattanDistance());
    }

    private static Instruction parseInstruction(String s) {
        return new Instruction(Action.find(s), Integer.parseInt(s.substring(1)));
    }

    private interface Action {
        String name();

        default Point newPosition(
                Point currentPosition,
                Point wayPoint,
                Direction currentDirection,
                int actionValue
        ) {
            return currentPosition;
        }

        default Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
            return currentWayPoint;
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
        private Point position = new Point(0, 0);
        private Point wayPoint = new Point(1, 10);
        private Direction direction = Direction.East;

        public void apply(Instruction i) {
            position = i.newPosition(position, wayPoint, direction);
            wayPoint = i.newWayPoint(wayPoint, direction);
            direction = i.getNewDirection(direction);
        }

        public int getManhattanDistance() {
            return position.getManhattanDistance();
        }

        @Override
        public String toString() {
            return "Plane directed to " + direction + ". Position: " + position;
        }
    }

    private enum Direction implements Action {
        North {
            @Override
            public Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
                return currentWayPoint.add(actionValue, 0);
            }
        },
        East {
            @Override
            public Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
                return currentWayPoint.add(0, actionValue);
            }
        },
        South {
            @Override
            public Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
                return currentWayPoint.add(-actionValue, 0);
            }
        },
        West {
            @Override
            public Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
                return currentWayPoint.add(0, -actionValue);
            }
        };

        public static int size = values().length;
    }

    private enum RelativeAction implements Action {
        LeftTurn {
            @Override
            public Direction getNewDirection(Direction currentDirection, int actionValue) {
                final Direction[] directions = Direction.values();
                final int idx = (currentDirection.ordinal() + Direction.size - (actionValue / 90) % Direction.size) % Direction.size;
                return directions[idx];
            }

            @Override
            public Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
                final int turns = (actionValue / 90) % Direction.values().length;
                return switch (turns) {
                    case 1 -> currentWayPoint.turnLeft();
                    case 2 -> currentWayPoint.turnOver();
                    case 3 -> currentWayPoint.turnRight();
                    default -> currentWayPoint;
                };
            }
        },
        RightTurn {
            @Override
            public Direction getNewDirection(Direction currentDirection, int actionValue) {
                final Direction[] directions = Direction.values();
                return directions[(currentDirection.ordinal() + actionValue / 90) % Direction.size];
            }

            @Override
            public Point newWayPoint(Point currentWayPoint, Direction currentDirection, int actionValue) {
                final int turns = (actionValue / 90) % Direction.values().length;
                return switch (turns) {
                    case 1 -> currentWayPoint.turnRight();
                    case 2 -> currentWayPoint.turnOver();
                    case 3 -> currentWayPoint.turnLeft();
                    default -> currentWayPoint;
                };
            }
        },
        Forward {
            @Override
            public Point newPosition(
                    Point currentPosition,
                    Point wayPoint,
                    Direction currentDirection,
                    int actionValue
            ) {
                return currentPosition.add(wayPoint.north * actionValue, wayPoint.east * actionValue);
            }
        },
    }

    private record Instruction(Action action, int value) {
        Point newPosition(Point currentPosition, Point wayPoint, Direction currentDirection) {
            return action.newPosition(currentPosition, wayPoint, currentDirection, value);
        }

        Point newWayPoint(Point currentWayPoint, Direction currentDirection) {
            return action.newWayPoint(currentWayPoint, currentDirection, value);
        }

        Direction getNewDirection(Direction currentDirection) {
            return action.getNewDirection(currentDirection, value);
        }

    }

    private record Point(int north, int east) {
        public Point add(int n, int e) {
            if (n == 0 && e == 0) {
                return this;
            }
            return new Point(north + n, east + e);
        }

        public Point turnLeft() {
            return new Point(east, -north);
        }

        public Point turnRight() {
            return new Point(-east, north);
        }

        public Point turnOver() {
            return new Point(-north, -east);
        }

        public int getManhattanDistance() {
            return Math.abs(north) + Math.abs(east);
        }
    }
}
