package compression;

import compression.BitPacker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Bit packer library tests.
 */
public class BitPackerTest {

    // Verbose debug printing support by setting this to true.
    private final boolean debugPrinting = false;

    BitPacker bp = new BitPacker();

    /**
     * Packing and unpacking tests with varying bit count.
     */
    @Test
    public void testPackUnpackWithMultipleBitAmounts() {
        int[] biggestNumber = new int[]{250, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000};

        for (int i = 0; i < biggestNumber.length; i++) {
            BitPacker bp = new BitPacker();
            bp.maxValue(biggestNumber[i]);

            System.out.print("Bits in use: " + bp.getBitCount() + ".  ");
            if (debugPrinting) {
                System.out.println("\n");
            }

            String testInput = "This is a test. This is a test. This is a test. This is a test.";
            if (debugPrinting) {
                System.out.println("Input: " + testInput + "\n");
            }

            int[] input = new int[testInput.length()];

            for (int j = 0; j < testInput.length(); j++) {
                input[j] = testInput.charAt(j);
            }

            if (debugPrinting) {
                printTableWithMessage(input, "Input as hex:");
            }

            int[] packed = bp.pack(input);

            if (debugPrinting) {
                printTableWithMessage(packed, "Packed as hex:");
            }

            int[] unpacked = bp.unpack(packed, bp.packedCount());

            if (debugPrinting) {
                printTableWithMessage(unpacked, "Output as hex:");
            }

            if (debugPrinting) {
                StringBuilder clearText = new StringBuilder();
                for (int j = 0; j < input.length; j++) {
                    clearText.append((char) unpacked[j]);
                }
                System.out.println("Output: " + clearText.toString() + "\n");
            }

            boolean testOk = true;
            for (int j = 0; j < input.length; j++) {
                assertEquals(input[j], unpacked[j]);
                if (debugPrinting) {
                    if (input[j] != unpacked[j]) {
                        System.out.println("Bug! @ " + j);
                        testOk = false;
                    }
                }
            }
            if (testOk) {
                System.out.println("Test passed!\n");
            }

            if (debugPrinting) {
                System.out.println(new String(new char[80]).replace("\0", "-"));
            }
        }
    }

    /**
     * Check that class counted bit value is correct.
     */
    @Test
    public void testEnsureCorrectBitCountUsage() {
        BitPacker bp = new BitPacker();
        bp.maxValue(250);
        assertEquals(8, bp.getBitCount());
    }

    /**
     * Print given int table elements as individual binary strings preceeded
     * with given message.
     *
     * @param input values to be printed
     * @param message to be dispalyed
     */
    private void printTableWithMessage(int[] input, String message) {
        System.out.println(message);
        for (int j = 0; j < input.length; j++) {
            System.out.print(Integer.toBinaryString(input[j]) + " ");
        }
        System.out.println("\n");
    }

}
