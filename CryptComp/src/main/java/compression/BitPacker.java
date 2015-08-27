package compression;

/**
 * Bit packer that packs and unpacks integers into or from fixed size bit
 * arrays.
 */
public class BitPacker {

    /**
     * Maximum bit count supported.
     */
    private final int maxBits = 16;
    /**
     * Actual number of bits used.
     */
    private int actualBitCount;
    /**
     * Number of entries packed output contains.
     */
    private int packedCount;

    /**
     * Construct BitPacker object.
     */
    public BitPacker() {
        packedCount = 0;
    }

    /**
     * Maximum value of the integers being packed or unpacked.
     *
     * @param number value of the highest number in table
     */
    public void maxValue(int number) {
        int value = 1;

        while (value <= number) {
            value <<= 1;
            actualBitCount++;
        }
    }

    /**
     * Get number of bits being used.
     *
     * @return bit count
     */
    public int getBitCount() {
        return actualBitCount;
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
            if (usedBits + actualBitCount <= maxBits) {
                packed[packIndex] |= original[orgIndex] << leftShifter;
                usedBits += actualBitCount;
                leftShifter += actualBitCount;

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
                usedBits = (actualBitCount - rightShifter);
                leftShifter = (actualBitCount - rightShifter);
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
     * @param packedCount count of entries that input contains
     * @return unpacked input
     */
    public int[] unpack(int[] packed, int packedCount) {
        int[] unpacked = new int[packed.length << 1];

        int packedIndex = 0;
        int unpackIndex = 0;
        int mask = 0;
        int remainingBits = maxBits;
        int leftShifter = 0;
        int rightShifter = 0;

        while (packedIndex < packedCount) {
            // Handle value that is contained entirely in single field
            if (remainingBits >= actualBitCount) {
                mask = (1 << actualBitCount) - 1;
                unpacked[unpackIndex] = (packed[packedIndex] >>> rightShifter) & mask;
                remainingBits -= actualBitCount;
                rightShifter = (maxBits - remainingBits);

                // Handle value that is split over two fields
            } else {
                unpacked[unpackIndex] = packed[packedIndex] >>> rightShifter;
                packedIndex++;
                if (packedIndex >= packed.length) {
                    break;
                }
                leftShifter = remainingBits;
                remainingBits = maxBits - (actualBitCount - (maxBits - rightShifter));
                mask = (1 << (actualBitCount - (maxBits - rightShifter))) - 1;
                unpacked[unpackIndex] |= ((packed[packedIndex] & mask) << leftShifter);
                rightShifter = (actualBitCount - leftShifter);
            }
            unpackIndex++;
        }

        return unpacked;
    }
}
