/*
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License 2.0 http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.math.BigInteger;
import java.util.Random;
import org.junit.jupiter.api.Test;

/** bigIntegerToNullPaddedByteArray must equal the byte-wise shift implementation it replaced. */
class DataConverterNullPaddedTest {

    private static byte[] reference(BigInteger input, int outputSizeInBytes) {
        byte[] output = new byte[outputSizeInBytes];
        int numByteBlocks = input.bitLength() / 8;
        int remainingBits;
        if (numByteBlocks < output.length) {
            remainingBits = input.bitLength() % 8;
        } else {
            remainingBits = 0;
            numByteBlocks = output.length;
        }
        int i;
        for (i = 0; i < numByteBlocks; i++) {
            output[output.length - 1 - i] = input.shiftRight(i * 8).byteValue();
        }
        if (remainingBits > 0) {
            output[output.length - 1 - i] = input.shiftRight(i * 8).byteValue();
        }
        return output;
    }

    private static void check(BigInteger input, int size) {
        assertArrayEquals(
                reference(input, size),
                DataConverter.bigIntegerToNullPaddedByteArray(input, size),
                input + " size " + size);
    }

    @Test
    void testEqualsShiftImplementation() {
        Random random = new Random(1);
        for (int n = 0; n < 200000; n++) {
            int bits = random.nextInt(600);
            BigInteger value = new BigInteger(bits, random);
            if (random.nextInt(4) == 0) {
                // exact powers of two and all-ones values hit the byte boundaries
                value =
                        random.nextBoolean()
                                ? BigInteger.ONE.shiftLeft(bits)
                                : BigInteger.ONE.shiftLeft(bits).subtract(BigInteger.ONE);
            }
            if (random.nextInt(3) == 0) {
                value = value.negate();
            }
            int bytes = (bits + 7) / 8;
            int size = Math.max(0, bytes + random.nextInt(5) - 2);
            if (random.nextInt(10) == 0) {
                size = random.nextInt(80);
            }
            check(value, size);
        }
    }

    @Test
    void testSmallValuesAndSizes() {
        for (int v = -70000; v <= 70000; v += (v > -300 && v < 300) ? 1 : 97) {
            for (int size = 0; size <= 5; size++) {
                check(BigInteger.valueOf(v), size);
            }
        }
    }
}
