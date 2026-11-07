/*
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License 2.0 http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML adapter for converting between byte arrays and their hexadecimal string representations.
 *
 * <p>This adapter is used with JAXB annotations to customize how byte arrays are serialized to and
 * deserialized from XML. It provides a compact, unformatted hexadecimal representation without any
 * whitespace, spaces, or line breaks.
 *
 * <p>When unmarshaling (reading XML), the adapter removes all whitespace from the input string
 * before converting it to a byte array, making it more tolerant of different formatting styles in
 * the source XML.
 *
 * <p>When marshaling (writing XML), the adapter produces a continuous hexadecimal string without
 * any formatting characters, resulting in more compact XML.
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * public class MyClass {
 *     @XmlElement
 *     @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
 *     private byte[] data;
 * }
 * }</pre>
 *
 * <p>With this adapter, a byte array like {@code {0x01, 0x02, 0x03}} would be serialized as the
 * string {@code "010203"} in XML rather than the default format.
 */
public class UnformattedByteArrayAdapter extends XmlAdapter<String, byte[]> {

    /**
     * Creates a new adapter for converting between byte arrays and their hexadecimal
     * representations.
     *
     * <p>This constructor initializes the adapter with no special configuration, as all
     * functionality is provided by the marshal and unmarshal methods.
     */
    public UnformattedByteArrayAdapter() {
        super();
    }

    /**
     * Converts a hexadecimal string representation to a byte array.
     *
     * <p>This method removes all whitespace from the input string before converting it to a byte
     * array, making it tolerant of different formatting styles in the XML.
     *
     * @param value The hexadecimal string to convert
     * @return The corresponding byte array
     */
    @Override
    public byte[] unmarshal(String value) {
        return DataConverter.hexStringToByteArray(removeWhitespace(value));
    }

    /**
     * Converts a byte array to its hexadecimal string representation.
     *
     * <p>This method produces a compact representation without any whitespace or formatting
     * characters.
     *
     * @param value The byte array to convert
     * @return The hexadecimal string representation
     */
    @Override
    public String marshal(byte[] value) {
        return DataConverter.bytesToHexString(value, false, false);
    }

    /** Same as {@code value.replaceAll("\\s", "")}, without compiling a pattern per call. */
    private static String removeWhitespace(String value) {
        StringBuilder builder = null;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            boolean whitespace =
                    c == ' ' || c == '\t' || c == '\n' || c == '\u000B' || c == '\f' || c == '\r';
            if (whitespace) {
                if (builder == null) {
                    builder = new StringBuilder(value.length());
                    builder.append(value, 0, i);
                }
            } else if (builder != null) {
                builder.append(c);
            }
        }
        return builder == null ? value : builder.toString();
    }
}
