package compression;

/**
 * Bit packer that packs and unpacks integers into or from fixed size bit
 * arrays.
 */
public class BitPacker {

    private int bitCount;
    private final int maxBits = 16;
    private int packedCount;

    /**
     * Maximum value of the integers being packed or unpacked.
     *
     * @param number value of the highest number in table
     */
    public void maxValue(int number) {
        int value = 1;

        while (value < number) {
            value <<= 1;
            bitCount++;
        }
    }

    /**
     * Get number of bits being used.
     *
     * @return bit count
     */
    public int getBitCount() {
        return bitCount;
    }

    /**
     * Pack bits into words.
     *
     * @param original input requiring packing
     * @return packed input
     */
    public int[] pack(int[] original) {
        int[] packed = new int[original.length];

        int orgIndex = 0;
        int packIndex = 0;
        int usedBits = 0;
        int mask = 0;
        int leftShifter = 0;
        int rightShifter = 0;

        while (orgIndex < original.length) {
            // Handle value that fits entirely to given field
            if (usedBits + bitCount <= maxBits) {
                packed[packIndex] |= original[orgIndex] << leftShifter;
                usedBits += bitCount;
                leftShifter += bitCount;

                // Even bit value cases
                if (usedBits == maxBits) {
                    usedBits = 0;
                    leftShifter = 0;
                    packIndex++;
                }
                // Handled partial fit
            } else {
                mask = (1 << (maxBits - usedBits)) - 1;
                packed[packIndex] |= ((original[orgIndex] & mask) << leftShifter);
                usedBits += (maxBits - leftShifter);

                packIndex++;
                rightShifter = (maxBits - leftShifter);
                packed[packIndex] = original[orgIndex] >>> rightShifter;
                usedBits = (bitCount - rightShifter);
                leftShifter = (bitCount - rightShifter);
            }
            orgIndex++;
        }

        packedCount = packIndex;

        return packed;
    }

    /**
     * Number of entries in packed input after packing has been done.
     *
     * @return packed entries count
     */
    public int packedCount() {
        return packedCount;
    }

    /**
     * Unpack words back to bits.
     *
     * @param packed input
     * @param packedAmount count of entries that input contains
     * @return unpacked input
     */
    public int[] unpack(int[] packed, int packedAmount) {
        int[] unpacked = new int[packed.length << 1];

        int packedIndex = 0;
        int unpackIndex = 0;
        int mask = 0;
        int remainingBits = maxBits;
        int leftShifter = 0;
        int rightShifter = 0;

        while (packedIndex < packedAmount) {
            // Handle value that is contained entirely in single field
            if (remainingBits >= bitCount) {
                mask = (1 << bitCount) - 1;
                unpacked[unpackIndex] = (packed[packedIndex] >>> rightShifter) & mask;
                remainingBits -= bitCount;
                rightShifter = (maxBits - remainingBits);

                // Handle value that is split over two fields
            } else {
                unpacked[unpackIndex] = packed[packedIndex] >>> rightShifter;
                packedIndex++;
                if (packedIndex >= packed.length) {
                    break;
                }
                leftShifter = remainingBits;
                remainingBits = maxBits - (bitCount - (maxBits - rightShifter));
                mask = (1 << (bitCount - (maxBits - rightShifter))) - 1;
                unpacked[unpackIndex] |= ((packed[packedIndex] & mask) << leftShifter);
                rightShifter = (bitCount - leftShifter);
            }
            unpackIndex++;
        }

        return unpacked;
    }
}
