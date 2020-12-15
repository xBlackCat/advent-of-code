package org.xblackcat.adventofcode.year2020;

import gnu.trove.TCollections;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.TLongLongMap;
import gnu.trove.map.hash.TLongLongHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 14: Docking Data ---
 * As your ferry approaches the sea port, the captain asks for your help again. The computer system that runs this port isn't compatible with the docking program on the ferry, so the docking parameters aren't being correctly initialized in the docking program's memory.
 * <p>
 * After a brief inspection, you discover that the sea port's computer system uses a strange bitmask system in its initialization program. Although you don't have the correct decoder chip handy, you can emulate it in software!
 * <p>
 * The initialization program (your puzzle input) can either update the bitmask or write a value to memory. Values and memory addresses are both 36-bit unsigned integers. For example, ignoring bitmasks for a moment, a line like mem[8] = 11 would write the value 11 to memory address 8.
 * <p>
 * The bitmask is always given as a string of 36 bits, written with the most significant bit (representing 2^35) on the left and the least significant bit (2^0, that is, the 1s bit) on the right. The current bitmask is applied to values immediately before they are written to memory: a 0 or 1 overwrites the corresponding bit in the value, while an X leaves the bit in the value unchanged.
 * <p>
 * For example, consider the following program:
 * <pre>
 * mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * mem[8] = 11
 * mem[7] = 101
 * mem[8] = 0</pre>
 * This program starts by specifying a bitmask (mask = ....). The mask it specifies will overwrite two bits in every written value: the 2s bit is overwritten with 0, and the 64s bit is overwritten with 1.
 * <p>
 * The program then attempts to write the value 11 to memory address 8. By expanding everything out to individual bits, the mask is applied as follows:
 * <pre>
 * value:  000000000000000000000000000000001011  (decimal 11)
 * mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * result: 000000000000000000000000000001001001  (decimal 73)</pre>
 * So, because of the mask, the value 73 is written to memory address 8 instead. Then, the program tries to write 101 to address 7:
 * <pre>
 * value:  000000000000000000000000000001100101  (decimal 101)
 * mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * result: 000000000000000000000000000001100101  (decimal 101)</pre>
 * This time, the mask has no effect, as the bits it overwrote were already the values the mask tried to set. Finally, the program tries to write 0 to address 8:
 * <pre>
 * value:  000000000000000000000000000000000000  (decimal 0)
 * mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * result: 000000000000000000000000000001000000  (decimal 64)</pre>
 * 64 is written to address 8 instead, overwriting the value that was there previously.
 * <p>
 * To initialize your ferry's docking program, you need the sum of all values left in memory after the initialization program completes. (The entire 36-bit address space begins initialized to the value 0 at every address.) In the above example, only two values in memory are not zero - 101 (at address 7) and 64 (at address 8) - producing a sum of 165.
 * <p>
 * Execute the initialization program. What is the sum of all values left in memory after it completes?
 * <p>
 * <p>
 * --- Part Two ---
 * For some reason, the sea port's computer system still can't communicate with your ferry's docking program. It must be using version 2 of the decoder chip!
 * <p>
 * A version 2 decoder chip doesn't modify the values being written at all. Instead, it acts as a memory address decoder. Immediately before a value is written to memory, each bit in the bitmask modifies the corresponding bit of the destination memory address in the following way:
 * <p>
 * If the bitmask bit is 0, the corresponding memory address bit is unchanged.
 * If the bitmask bit is 1, the corresponding memory address bit is overwritten with 1.
 * If the bitmask bit is X, the corresponding memory address bit is floating.
 * A floating bit is not connected to anything and instead fluctuates unpredictably. In practice, this means the floating bits will take on all possible values, potentially causing many memory addresses to be written all at once!
 * <p>
 * For example, consider the following program:
 * <pre>
 * mask = 000000000000000000000000000000X1001X
 * mem[42] = 100
 * mask = 00000000000000000000000000000000X0XX
 * mem[26] = 1</pre>
 * When this program goes to write to memory address 42, it first applies the bitmask:
 * <pre>
 * address: 000000000000000000000000000000101010  (decimal 42)
 * mask:    000000000000000000000000000000X1001X
 * result:  000000000000000000000000000000X1101X</pre>
 * After applying the mask, four bits are overwritten, three of which are different, and two of which are floating. Floating bits take on every possible combination of values; with two floating bits, four actual memory addresses are written:
 * <pre>
 * 000000000000000000000000000000011010  (decimal 26)
 * 000000000000000000000000000000011011  (decimal 27)
 * 000000000000000000000000000000111010  (decimal 58)
 * 000000000000000000000000000000111011  (decimal 59)</pre>
 * Next, the program is about to write to memory address 26 with a different bitmask:
 * <pre>
 * address: 000000000000000000000000000000011010  (decimal 26)
 * mask:    00000000000000000000000000000000X0XX
 * result:  00000000000000000000000000000001X0XX</pre>
 * This results in an address with three floating bits, causing writes to eight memory addresses:
 * <pre>
 * 000000000000000000000000000000010000  (decimal 16)
 * 000000000000000000000000000000010001  (decimal 17)
 * 000000000000000000000000000000010010  (decimal 18)
 * 000000000000000000000000000000010011  (decimal 19)
 * 000000000000000000000000000000011000  (decimal 24)
 * 000000000000000000000000000000011001  (decimal 25)
 * 000000000000000000000000000000011010  (decimal 26)
 * 000000000000000000000000000000011011  (decimal 27)</pre>
 * The entire 36-bit address space still begins initialized to the value 0 at every address, and you still need the sum of all values left in memory at the end of the program. In this example, the sum is 208.
 * <p>
 * Execute the initialization program using an emulator for a version 2 decoder chip. What is the sum of all values left in memory after it completes?
 */
public class Day14 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        final AComputer comp = new Computer();
        loadProgram(comp);

        System.out.println("Sum of values: " + new TLongArrayList(comp.memory().valueCollection()).sum());
    }

    private static void part2() throws IOException {
        final var comp = new Computer2();
        loadProgram(comp);

        System.out.println("Sum of values: " + new TLongArrayList(comp.memory().valueCollection()).sum());
    }

    private static void loadProgram(AComputer comp) throws IOException {
        try (
                final InputStream stream = Day12Part2.class.getResourceAsStream("/year2020/day14.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            reader.lines().forEach(comp::applyInstruction);
        }
    }

    private static abstract class AComputer {
        protected static final Pattern MASK_INSTRUCTION = Pattern.compile("mask = ([X10]+)");
        protected static final Pattern SET_INSTRUCTION = Pattern.compile("mem\\[(\\d+)] = (\\d+)");

        protected TLongLongMap mem = new TLongLongHashMap();
        protected long maskAnd = -1;
        protected long maskOr = 0;

        public abstract void applyInstruction(String line);

        final public TLongLongMap memory() {
            return TCollections.unmodifiableMap(mem);
        }
    }

    private static class Computer extends AComputer {
        public void applyInstruction(String line) {
            {
                final Matcher matcher = MASK_INSTRUCTION.matcher(line);
                if (matcher.matches()) {
                    final String mask = matcher.group(1);
                    maskAnd = prepareMask(mask, false, false, true);
                    maskOr = prepareMask(mask, false, true, false);
                    return;
                }
            }
            final Matcher matcher = SET_INSTRUCTION.matcher(line);
            if (matcher.matches()) {
                int pos = Integer.parseInt(matcher.group(1));
                long value = Long.parseLong(matcher.group(2));

                mem.put(pos, (value & maskAnd) | maskOr);
                return;
            }

            throw new IllegalStateException("Can't parse instruction: " + line);
        }
    }

    private static class Computer2 extends AComputer {
        private long[] floatingMasks = new long[0];

        public void applyInstruction(String line) {
            {
                final Matcher matcher = MASK_INSTRUCTION.matcher(line);
                if (matcher.matches()) {
                    final String mask = matcher.group(1);

                    maskAnd = prepareMask(mask, true, false, false);
                    maskOr = prepareMask(mask, false, true, false);
                    long maskFloating = prepareMask(mask, false, false, true);
                    int floatingBitsCount = Long.bitCount(maskFloating);
                    floatingMasks = new long[1 << floatingBitsCount];
                    Arrays.fill(floatingMasks, maskFloating);

                    int bitSetIdx = 0;
                    for (int idx = 0; idx < 64; idx++) {
                        final long maskBit = maskFloating & (1L << idx);
                        if (maskBit != 0) {
                            final int maskIdxBit = 1 << bitSetIdx;
                            for (int i = 0; i < floatingMasks.length; i++) {
                                if ((i & maskIdxBit) == 0) {
                                    floatingMasks[i] = floatingMasks[i] & (~maskBit);
                                }
                            }
                            bitSetIdx++;
                        }
                    }

                    return;
                }
            }
            final Matcher matcher = SET_INSTRUCTION.matcher(line);
            if (matcher.matches()) {
                long basePos = (Integer.parseInt(matcher.group(1)) & maskAnd) | maskOr;
                long value = Long.parseLong(matcher.group(2));

                for (long maskFloating : floatingMasks) {
                    mem.put(maskFloating | basePos, value);
                }

                return;
            }

            throw new IllegalStateException("Can't parse instruction: " + line);
        }
    }

    private static long prepareMask(String mask, boolean setIfZero, boolean setIfOne, boolean setIfX) {
        final int onZero = setIfZero ? '1' : '0';
        final int onOne = setIfOne ? '1' : '0';
        final int onX = setIfX ? '1' : '0';
        final String preparedMask = mask.codePoints()
                .map(c1 -> switch (c1) {
                    case '0' -> onZero;
                    case 'X' -> onX;
                    case '1' -> onOne;
                    default -> c1;
                })
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return Long.parseLong(preparedMask, 2);
    }
}
