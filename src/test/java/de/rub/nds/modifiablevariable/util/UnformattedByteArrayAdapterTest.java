/*
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License 2.0 http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnformattedByteArrayAdapterTest {

    private UnformattedByteArrayAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UnformattedByteArrayAdapter();
    }

    @Test
    void testMarshal() throws Exception {
        byte[] input = new byte[] {0x01, 0x02, 0x03, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        String expected = "01 02 03 AB CD EF";

        assertEquals(expected, adapter.marshal(input));
    }

    @Test
    void testUnmarshalCompactFormat() throws Exception {
        String input = "0102ABCDEF";
        byte[] expected = new byte[] {0x01, 0x02, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

        assertArrayEquals(expected, adapter.unmarshal(input));
    }

    @Test
    void testUnmarshalWithSpaces() throws Exception {
        String input = "01 02 AB CD EF";
        byte[] expected = new byte[] {0x01, 0x02, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

        assertArrayEquals(expected, adapter.unmarshal(input));
    }

    @Test
    void testUnmarshalWithNewlinesAndSpaces() throws Exception {
        String input = "01 02\nAB CD\r\nEF";
        byte[] expected = new byte[] {0x01, 0x02, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

        assertArrayEquals(expected, adapter.unmarshal(input));
    }

    @Test
    void testMarshalEmptyArray() throws Exception {
        byte[] input = new byte[0];
        String expected = "";

        assertEquals(expected, adapter.marshal(input));
    }

    @Test
    void testUnmarshalEmptyString() throws Exception {
        String input = "";
        byte[] expected = new byte[0];

        assertArrayEquals(expected, adapter.unmarshal(input));
    }

    @Test
    void testUnmarshalOnlyWhitespace() throws Exception {
        String input = " \n\t\r ";
        byte[] expected = new byte[0];

        assertArrayEquals(expected, adapter.unmarshal(input));
    }

    @Test
    void testUnmarshalStripsWhitespaceLikeRegex() {
        java.util.Random random = new java.util.Random(7);
        char[] alphabet = {
            '0', 'a', 'F', ' ', '\t', '\n', '\u000B', '\f', '\r', '\u00A0', '\u2028'
        };
        for (int n = 0; n < 2000; n++) {
            StringBuilder raw = new StringBuilder();
            int length = random.nextInt(40);
            for (int i = 0; i < length; i++) {
                char c = alphabet[random.nextInt(alphabet.length)];
                raw.append(c);
            }
            String expectedInput = raw.toString().replaceAll("\\s", "");
            byte[] expected;
            try {
                expected = DataConverter.hexStringToByteArray(expectedInput);
            } catch (RuntimeException e) {
                expected = null;
            }
            byte[] actual;
            try {
                actual = adapter.unmarshal(raw.toString());
            } catch (RuntimeException e) {
                actual = null;
            }
            assertArrayEquals(expected, actual, raw.toString());
        }
    }
}
