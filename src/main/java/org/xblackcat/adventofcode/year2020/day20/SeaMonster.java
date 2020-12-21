package org.xblackcat.adventofcode.year2020.day20;


import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SeaMonster {
    private static final char[][] SEA_MONSTER = """
                              #\s
            #    ##    ##    ###
             #  #  #  #  #  #  \s
            """.lines().map(String::toCharArray).toArray(char[][]::new);

    private static final Pixel[] SEA_MONSTER_POSITIONS;

    static {
        List<Pixel> pixels = new ArrayList<>();
        for (int j = 0, sea_monsterLength = SEA_MONSTER.length; j < sea_monsterLength; j++) {
            char[] row = SEA_MONSTER[j];
            for (int i = 0, rowLength = row.length; i < rowLength; i++) {
                char cell = row[i];
                if (cell == '#') {
                    pixels.add(new Pixel(i, j));
                }
            }
        }
        SEA_MONSTER_POSITIONS = pixels.toArray(new Pixel[0]);
    }

    public SeaMonster() {
    }

    public int eatImage(Image img) {
        // Search for monster
        int amount = 0;
        char[][] raw = img.raw();
        for (int i = 0, rawLength = raw.length - SEA_MONSTER[0].length; i < rawLength; i++) {
            char[] column = raw[i];
            for (int j = 0, columnLength = column.length - SEA_MONSTER.length; j < columnLength; j++) {
                boolean found = true;
                for (Pixel p : SEA_MONSTER_POSITIONS) {
                    if (raw[i + p.dx][j + p.dy] != '#') {
                        found = false;
                        break;
                    }
                }

                if (!found) {
                    continue;
                }

                amount++;
                for (Pixel p : SEA_MONSTER_POSITIONS) {
                    raw[i + p.dx][j + p.dy] = '.';
                }
            }
        }
        return amount;
    }


    private record Pixel(int dx, int dy) {
    }
}
