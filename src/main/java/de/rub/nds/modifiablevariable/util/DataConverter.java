/*
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License 2.0 http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.util;

import de.rub.nds.modifiablevariable.bytearray.ModifiableByteArray;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.List;

/**
 * A utility class for data conversions and manipulations.
 *
 * <p>This class provides methods for:
 *
 * <ul>
 *   <li>Converting between various numeric types and byte arrays
 *   <li>Converting between hexadecimal strings and byte arrays
 *   <li>Concatenating arrays
 *   <li>Manipulating byte arrays
 *   <li>BigInteger conversions
 * </ul>
 *
 * <p>All methods are static, and the class cannot be instantiated.
 */
public final class DataConverter {

    /** Private constructor to prevent instantiation of this utility class. */
    private DataConverter() {
        super();
    }

    /**
     * Converts a long value to an 8-byte array representing a 64-bit unsigned integer.
     *
     * <p>This method uses big-endian byte order (most significant byte first), which is the network
     * byte order and the standard for many protocols.
     *
     * @param value The long value to convert
     * @return A byte array of length 8 representing the value in big-endian order
     */
    public static byte[] longToUint64Bytes(long value) {
        byte[] result = new byte[8];
        result[0] = (byte) (value >>> 56);
        result[1] = (byte) (value >>> 48);
        result[2] = (byte) (value >>> 40);
        result[3] = (byte) (value >>> 32);
        result[4] = (byte) (value >>> 24);
        result[5] = (byte) (value >>> 16);
        result[6] = (byte) (value >>> 8);
        result[7] = (byte) value;
        return result;
    }

    /**
     * Converts a long value to a 6-byte array representing a 48-bit unsigned integer.
     *
     * <p>This method uses big-endian byte order (most significant byte first). The highest 16 bits
     * of the long value will be truncated.
     *
     * <p>48-bit values are sometimes used in networking protocols for timestamps or identifiers.
     *
     * @param value The long value to convert (highest 16 bits will be truncated)
     * @return A byte array of length 6 representing the value in big-endian order
     */
    public static byte[] longToUint48Bytes(long value) {
        byte[] output = new byte[6];
        output[0] = (byte) (value >>> 40);
        output[1] = (byte) (value >>> 32);
        output[2] = (byte) (value >>> 24);
        output[3] = (byte) (value >>> 16);
        output[4] = (byte) (value >>> 8);
        output[5] = (byte) value;
        return output;
    }

    /**
     * Converts a long value to a 4-byte array representing a 32-bit unsigned integer.
     *
     * <p>This method uses big-endian byte order (most significant byte first). Only the lowest 32
     * bits of the long value are used; higher bits are truncated.
     *
     * <p>This is commonly used for 32-bit protocol fields like sequence numbers, timestamps, and
     * message lengths.
     *
     * @param value The long value to convert (highest 32 bits will be truncated)
     * @return A byte array of length 4 representing the value in big-endian order
     */
    public static byte[] longToUint32Bytes(long value) {
        byte[] result = new byte[4];
        result[0] = (byte) (value >>> 24);
        result[1] = (byte) (value >>> 16);
        result[2] = (byte) (value >>> 8);
        result[3] = (byte) value;
        return result;
    }

    /**
     * Converts an 8-byte array representing a 64-bit unsigned integer to a long value.
     *
     * <p>This method uses big-endian byte order (most significant byte first), which is the network
     * byte order and the standard for many protocols.
     *
     * <p>This is the inverse operation of {@link #longToUint64Bytes(long)}.
     *
     * @param bytes The byte array of length 8 to convert
     * @return The long value represented by the byte array
     * @throws IllegalArgumentException if bytes is null or not exactly 8 bytes long
     */
    public static long uInt64BytesToLong(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        if (bytes.length != 8) {
            throw new IllegalArgumentException("Input byte array must be exactly 8 bytes long");
        }
        return (long) (bytes[0] & 0xFF) << 56
                | (long) (bytes[1] & 0xFF) << 48
                | (long) (bytes[2] & 0xFF) << 40
                | (long) (bytes[3] & 0xFF) << 32
                | (long) (bytes[4] & 0xFF) << 24
                | (long) (bytes[5] & 0xFF) << 16
                | (long) (bytes[6] & 0xFF) << 8
                | (long) (bytes[7] & 0xFF);
    }

    /**
     * Converts a 4-byte array representing a 32-bit unsigned integer to a long value.
     *
     * <p>This method uses big-endian byte order (most significant byte first), which is the network
     * byte order and the standard for many protocols.
     *
     * <p>This is the inverse operation of {@link #longToUint32Bytes(long)}.
     *
     * @param bytes The byte array of length 4 to convert
     * @return The long value represented by the byte array
     * @throws IllegalArgumentException if bytes is null or not exactly 4 bytes long
     */
    public static long uInt32BytesToLong(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Input byte array must be exactly 4 bytes long");
        }
        return (long) (bytes[0] & 0xFF) << 24
                | (bytes[1] & 0xFF) << 16
                | (bytes[2] & 0xFF) << 8
                | bytes[3] & 0xFF;
    }

    /**
     * Takes an integer value and stores its last bytes into a byte array of a given size.
     *
     * @param value integer value
     * @param size byte size of the new integer byte array
     * @return Byte array representing the integer. If the array size is larger, 00 bytes are
     *     prepended. If the number are larger, MSBs are omitted.
     */
    public static byte[] intToBytes(int value, int size) {
        if (size < 1) {
            throw new IllegalArgumentException("The array must be at least of size 1");
        }
        byte[] result = new byte[size];
        int shift = 0;
        int finalPosition = size > Integer.BYTES ? size - Integer.BYTES : 0;
        for (int i = size - 1; i >= finalPosition; i--) {
            result[i] = (byte) ((value >>> shift) & 0xFF);
            shift += 8;
        }

        return result;
    }

    /**
     * Takes a long value and stores its last bytes into a byte array
     *
     * @param value long value
     * @param size byte size of the new integer byte array
     * @return Byte array representing the long. If the array size is larger, 00 bytes are
     *     prepended. If the number are larger, MSBs are omitted.
     */
    public static byte[] longToBytes(long value, int size) {
        if (size < 1) {
            throw new IllegalArgumentException("The array must be at least of size 1");
        }
        byte[] result = new byte[size];
        int shift = 0;
        int finalPosition = size > Long.BYTES ? size - Long.BYTES : 0;
        for (int i = size - 1; i >= finalPosition; i--) {
            result[i] = (byte) ((value >>> shift) & 0xFF);
            shift += 8;
        }

        return result;
    }

    /**
     * Converts multiple bytes into one int value
     *
     * @param value byte array
     * @return integer
     * @throws IllegalArgumentException if value is null or exceeds 4 bytes
     */
    public static int bytesToInt(byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        if (value.length > 4) {
            throw new IllegalArgumentException("Input byte array length must not exceed 4 bytes");
        }
        int result = 0;
        int shift = 0;
        for (int i = value.length - 1; i >= 0; i--) {
            result += (value[i] & 0xFF) << shift;
            shift += 8;
        }
        return result;
    }

    /**
     * Converts multiple bytes into one long value
     *
     * @param value byte array
     * @return long
     * @throws IllegalArgumentException if value is null or exceeds 8 bytes
     */
    public static long bytesToLong(byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        if (value.length > 8) {
            throw new IllegalArgumentException("Input byte array length must not exceed 8 bytes");
        }
        long result = 0;
        int shift = 0;
        for (int i = value.length - 1; i >= 0; i--) {
            result += (long) (value[i] & 0xFF) << shift;
            shift += 8;
        }
        return result;
    }

    /**
     * Converts a byte array to a hexadecimal string representation.
     *
     * <p>This method automatically determines whether to use pretty-printing based on the array
     * length. If the array has more than 15 bytes, pretty-printing is enabled for better
     * readability.
     *
     * <p>If the input array is null, an IllegalArgumentException is thrown.
     *
     * @param array The byte array to convert
     * @return A string representation of the bytes in hexadecimal format
     * @throws IllegalArgumentException if array is null
     */
    public static String bytesToHexString(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        boolean usePrettyPrinting = array.length > 15;
        return bytesToHexString(array, usePrettyPrinting);
    }

    /**
     * Converts a byte array to a hexadecimal string representation with optional pretty-printing.
     *
     * <p>When pretty-printing is enabled, the output includes newlines and spacing to improve
     * readability of longer byte sequences.
     *
     * <p>If the input array is null, an IllegalArgumentException is thrown.
     *
     * @param array The byte array to convert
     * @param usePrettyPrinting Whether to use pretty-printing formatting
     * @return A string representation of the bytes in hexadecimal format
     * @throws IllegalArgumentException if array is null
     */
    public static String bytesToHexString(byte[] array, boolean usePrettyPrinting) {
        if (array == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        return bytesToHexString(array, usePrettyPrinting, true);
    }

    /**
     * Converts a byte array to a hexadecimal string representation with detailed formatting
     * options.
     *
     * <p>This method provides full control over the formatting:
     *
     * <ul>
     *   <li>When pretty-printing is enabled, bytes are grouped with spaces
     *   <li>Every 8 bytes get an additional space
     *   <li>Every 16 bytes get a new line
     *   <li>The optional initial new line can be controlled separately
     * </ul>
     *
     * <p>If the input array is null, an IllegalArgumentException is thrown.
     *
     * @param array The byte array to convert
     * @param usePrettyPrinting Whether to use pretty-printing formatting
     * @param initialNewLine Whether to begin with a new line (only applies if pretty-printing is
     *     enabled)
     * @return A string representation of the bytes in hexadecimal format
     * @throws IllegalArgumentException if array is null
     */
    public static String bytesToHexString(
            byte[] array, boolean usePrettyPrinting, boolean initialNewLine) {
        if (array == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        StringBuilder result = new StringBuilder();
        if (initialNewLine && usePrettyPrinting) {
            result.append("\n");
        }
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                if (usePrettyPrinting && i % 16 == 0) {
                    result.append("\n");
                } else {
                    if (usePrettyPrinting && i % 8 == 0) {
                        result.append(" ");
                    }
                    result.append(" ");
                }
            }
            byte b = array[i];
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    /**
     * Converts a ModifiableByteArray to a hexadecimal string representation.
     *
     * <p>This method automatically determines whether to use pretty-printing based on the array
     * length. If the array has more than 15 bytes, pretty-printing is enabled for better
     * readability.
     *
     * @param array The ModifiableByteArray to convert
     * @return A string representation of the bytes in hexadecimal format
     * @throws IllegalArgumentException if array is null or its value is null
     */
    public static String bytesToHexString(ModifiableByteArray array) {
        if (array == null) {
            throw new IllegalArgumentException("Input ModifiableByteArray must not be null");
        }
        byte[] value = array.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Value of ModifiableByteArray must not be null");
        }
        return bytesToHexString(value);
    }

    /**
     * Converts a ModifiableByteArray to a hexadecimal string representation with optional
     * pretty-printing.
     *
     * <p>When pretty-printing is enabled, the output includes newlines and spacing to improve
     * readability of longer byte sequences.
     *
     * @param array The ModifiableByteArray to convert
     * @param usePrettyPrinting Whether to use pretty-printing formatting
     * @return A string representation of the bytes in hexadecimal format
     * @throws IllegalArgumentException if array is null or its value is null
     */
    public static String bytesToHexString(ModifiableByteArray array, boolean usePrettyPrinting) {
        if (array == null) {
            throw new IllegalArgumentException("Input ModifiableByteArray must not be null");
        }
        byte[] value = array.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Value of ModifiableByteArray must not be null");
        }
        return bytesToHexString(value, usePrettyPrinting, true);
    }

    /**
     * Converts a ModifiableByteArray to a hexadecimal string representation with detailed
     * formatting options.
     *
     * <p>This method provides full control over the formatting:
     *
     * <ul>
     *   <li>When pretty-printing is enabled, bytes are grouped with spaces
     *   <li>Every 8 bytes get an additional space
     *   <li>Every 16 bytes get a new line
     *   <li>The optional initial new line can be controlled separately
     * </ul>
     *
     * @param array The ModifiableByteArray to convert
     * @param usePrettyPrinting Whether to use pretty-printing formatting
     * @param initialNewLine Whether to begin with a new line (only applies if pretty-printing is
     *     enabled)
     * @return A string representation of the bytes in hexadecimal format
     * @throws IllegalArgumentException if array is null or its value is null
     */
    public static String bytesToHexString(
            ModifiableByteArray array, boolean usePrettyPrinting, boolean initialNewLine) {
        if (array == null) {
            throw new IllegalArgumentException("Input ModifiableByteArray must not be null");
        }
        byte[] value = array.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Value of ModifiableByteArray must not be null");
        }
        return bytesToHexString(value, usePrettyPrinting, initialNewLine);
    }

    /**
     * Like bytesToHexString() without any formatting.
     *
     * @param array byte array
     * @return hex string
     * @throws IllegalArgumentException if array is null
     */
    public static String bytesToRawHexString(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Input byte array must not be null");
        }
        StringBuilder result = new StringBuilder();
        for (byte b : array) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    /**
     * Concatenates multiple arrays of the same type into a single array.
     *
     * <p>This generic method works with arrays of any reference type. It creates a new array that
     * contains all elements from the input arrays in the order they are provided.
     *
     * <p>If any of the input arrays is null, an IllegalArgumentException will be thrown.
     *
     * @param <T> The component type of the arrays
     * @param arrays The arrays to concatenate
     * @return A new array containing all elements from the input arrays
     * @throws IllegalArgumentException if arrays is null or empty, or if any input array is null
     */
    @SafeVarargs
    public static <T> T[] concatenate(T[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            throw new IllegalArgumentException(
                    "The minimal number of parameters for this function is one");
        }
        int length = 0;
        for (T[] a : arrays) {
            if (a == null) {
                throw new IllegalArgumentException("Input arrays must not be null");
            }
            length += a.length;
        }
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length);
        int currentOffset = 0;
        for (T[] a : arrays) {
            System.arraycopy(a, 0, result, currentOffset, a.length);
            currentOffset += a.length;
        }
        return result;
    }

    /**
     * Concatenates multiple byte arrays into a single byte array.
     *
     * <p>This method creates a new byte array that contains all elements from the input arrays in
     * the order they are provided.
     *
     * <p>If any of the input arrays is null, an IllegalArgumentException will be thrown.
     *
     * @param arrays The byte arrays to concatenate
     * @return A new byte array containing all elements from the input arrays
     * @throws IllegalArgumentException if arrays is null or empty, or if any input array is null
     */
    public static byte[] concatenate(byte[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            throw new IllegalArgumentException(
                    "The minimal number of parameters for this function is one");
        }
        int length = 0;
        for (byte[] a : arrays) {
            if (a == null) {
                throw new IllegalArgumentException("Input arrays must not be null");
            }
            length += a.length;
        }
        byte[] result = new byte[length];
        int currentOffset = 0;
        for (byte[] a : arrays) {
            System.arraycopy(a, 0, result, currentOffset, a.length);
            currentOffset += a.length;
        }
        return result;
    }

    /**
     * Concatenates two byte arrays, using only a specified number of bytes from the second array.
     *
     * <p>This method is similar to {@link #concatenate(byte[]...)}, but allows you to limit how
     * many bytes are taken from the second array. This is useful when you need to append only a
     * portion of one array to another.
     *
     * @param array1 The first byte array (used in its entirety)
     * @param array2 The second byte array (used partially)
     * @param numberOfArray2Bytes The number of bytes to use from array2
     * @return A new byte array containing array1 followed by the specified portion of array2
     * @throws IllegalArgumentException if array1 or array2 is null, or if numberOfArray2Bytes is
     *     negative or greater than array2.length
     */
    public static byte[] concatenate(byte[] array1, byte[] array2, int numberOfArray2Bytes) {
        if (array1 == null) {
            throw new IllegalArgumentException("First array must not be null");
        }
        if (array2 == null) {
            throw new IllegalArgumentException("Second array must not be null");
        }
        if (numberOfArray2Bytes < 0 || numberOfArray2Bytes > array2.length) {
            throw new IllegalArgumentException(
                    "Number of bytes from second array must be between 0 and array2.length");
        }
        int length = array1.length + numberOfArray2Bytes;
        byte[] result = new byte[length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, numberOfArray2Bytes);
        return result;
    }

    /**
     * Replaces all zero bytes in an array with non-zero values.
     *
     * <p>This method modifies the input array in-place by replacing any bytes with value 0x00 with
     * the value 0x01. This can be useful in cryptographic contexts where zero bytes might cause
     * special behaviors that need to be avoided.
     *
     * @param array The byte array to modify (modified in-place)
     * @throws IllegalArgumentException if array is null
     */
    public static void makeArrayNonZero(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Input array must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array[i] = 1;
            }
        }
    }

    /**
     * Converts a BigInteger to a byte array with specific output size padding.
     *
     * <p>This method is particularly useful for cryptographic operations that require values to be
     * padded to specific block sizes.
     *
     * <p>The resulting array will have a length that is a multiple of the specified size. If the
     * BigInteger's byte representation is not of the specified size, the array will be padded with
     * leading zeros.
     *
     * <p>Special cases:
     *
     * <ul>
     *   <li>If the output size is 0, an empty array is returned
     *   <li>If the value is zero, an array of zeros with length equal to the block size is returned
     * </ul>
     *
     * @param value The BigInteger to convert
     * @param expectedLength The expected length to align the output to
     * @param removeSignByte Whether to remove the sign byte (if present) before padding
     * @return A byte array representation of the BigInteger, padded to be a multiple of the block
     *     size
     * @throws IllegalArgumentException if value is null
     */
    public static byte[] bigIntegerToByteArray(
            BigInteger value, int expectedLength, boolean removeSignByte) {
        if (value == null) {
            throw new IllegalArgumentException("Input BigInteger must not be null");
        }
        if (expectedLength == 0) {
            return new byte[0];
        } else if (value.equals(BigInteger.ZERO)) {
            return new byte[expectedLength];
        }
        byte[] array = value.toByteArray();
        int remainder = array.length % expectedLength;
        byte[] result = array;
        byte[] tmp;

        if (removeSignByte && result[0] == 0x0) {
            tmp = new byte[result.length - 1];
            System.arraycopy(result, 1, tmp, 0, tmp.length);
            result = tmp;
            remainder = tmp.length % expectedLength;
        }

        if (remainder > 0) {
            // add zeros to fit size
            tmp = new byte[result.length + expectedLength - remainder];
            System.arraycopy(result, 0, tmp, expectedLength - remainder, result.length);
            result = tmp;
        }

        return result;
    }

    /**
     * Takes a BigInteger value and returns its (unsigned) byte array representation, if necessary
     * the sign byte is removed.
     *
     * @param value big integer to be converted
     * @return big integer represented in bytes
     * @throws IllegalArgumentException if value is null
     */
    public static byte[] bigIntegerToByteArray(BigInteger value) {
        if (value == null) {
            throw new IllegalArgumentException("Input BigInteger must not be null");
        }
        if (value.equals(BigInteger.ZERO)) {
            return new byte[0];
        }
        byte[] result = value.toByteArray();

        if (result.length > 1 && result[0] == 0x0) {
            byte[] tmp = new byte[result.length - 1];
            System.arraycopy(result, 1, tmp, 0, tmp.length);
            result = tmp;
        }
        return result;
    }

    /**
     * Converts a list of BigIntegers to an array
     *
     * @param list list of big integers
     * @return array of big integers
     * @throws IllegalArgumentException if list is null
     */
    public static BigInteger[] convertListToArray(List<BigInteger> list) {
        if (list == null) {
            throw new IllegalArgumentException("Input list must not be null");
        }
        BigInteger[] result = new BigInteger[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * Converts a string with an even number of hexadecimal characters to a byte array.
     *
     * @param input hex string
     * @return byte array
     */
    public static byte[] hexStringToByteArray(String input) {
        if (input == null || input.length() % 2 != 0) {
            throw new IllegalArgumentException(
                    "The input must not be null and "
                            + "shall have an even number of hexadecimal characters. Found: "
                            + input);
        }
        byte[] output = new byte[input.length() / 2];
        for (int i = 0; i < output.length; i++) {
            output[i] =
                    (byte)
                            ((Character.digit(input.charAt(i * 2), 16) << 4)
                                    + Character.digit(input.charAt(i * 2 + 1), 16));
        }
        return output;
    }

    /**
     * Converts a BigInteger into a byte array of given size. If the BigInteger doesn't fit into the
     * byte array, bits of the BigInteger will simply be truncated, starting with the most
     * significant bit. If the array is larger than the BigInteger, prepending bytes in the array
     * will be 0x00.
     *
     * @param input big integer
     * @param outputSizeInBytes output size
     * @return big integer converted to a byte array
     */
    public static byte[] bigIntegerToNullPaddedByteArray(BigInteger input, int outputSizeInBytes) {
        if (input == null) {
            throw new IllegalArgumentException("'input' must not be null.");
        }
        byte[] output = new byte[outputSizeInBytes];

        int numByteBlocks = input.bitLength() / 8;
        int remainingBits;

        if (numByteBlocks < output.length) {
            remainingBits = input.bitLength() % 8;
        } else {
            remainingBits = 0;
            numByteBlocks = output.length;
        }

        int written = remainingBits > 0 ? numByteBlocks + 1 : numByteBlocks;
        // byte i of the result from the end is input.shiftRight(i * 8).byteValue(), which is byte i
        // from the end of the two's complement encoding (at least bitLength / 8 + 1 bytes long)
        byte[] encoded = input.toByteArray();
        System.arraycopy(
                encoded, encoded.length - written, output, output.length - written, written);
        return output;
    }

    /**
     * Reverses the order of a byte array: So, [0x00,0x01,0x02,0x03] will be returned as
     * [0x03,0x02,0x01,0x00]
     *
     * @param array the byte array to reverse
     * @return byte array with reversed byte order
     * @throws IllegalArgumentException if array is null
     */
    public static byte[] reverseByteOrder(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Input array must not be null");
        }
        int length = array.length;
        byte[] temp = new byte[length];
        int counter = length - 1;
        for (int i = 0; i < length; i++) {
            temp[i] = array[counter--];
        }
        return temp;
    }

    /**
     * Returns the starting index of the innerArray inside the outerArray if present. Returns null
     * if the innerArray is not present in the outerArray
     *
     * @param outerArray Outer byte array to search for inner array
     * @param innerArray byte array searched for
     * @return StartIndex of innerArray in outerArray or null if not present.
     * @throws IllegalArgumentException if outerArray or innerArray is null, or if innerArray is
     *     empty
     */
    public static Integer indexOf(byte[] outerArray, byte[] innerArray) {
        if (outerArray == null) {
            throw new IllegalArgumentException("Outer array must not be null");
        }
        if (innerArray == null) {
            throw new IllegalArgumentException("Inner array must not be null");
        }
        if (innerArray.length == 0) {
            throw new IllegalArgumentException("Inner array must not be empty");
        }
        if (innerArray.length > outerArray.length) {
            return null; // Inner array can't be found in a smaller outer array
        }

        for (int i = 0; i < outerArray.length - innerArray.length + 1; ++i) {
            boolean found = true;
            for (int j = 0; j < innerArray.length; ++j) {
                if (outerArray[i + j] != innerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return null;
    }

    /**
     * Converts a signed byte to an unsigned int.
     *
     * <p>In Java, bytes are signed 8-bit values ranging from -128 to 127. This method converts a
     * byte to an unsigned int value (0-255) by masking with 0xFF.
     *
     * <p>This is particularly useful when dealing with network protocols or file formats where
     * bytes are often interpreted as unsigned values.
     *
     * @param b The byte to convert
     * @return The unsigned int value (0-255) corresponding to the byte
     */
    public static int byteToUnsignedInt(byte b) {
        return b & 0xff;
    }
}
