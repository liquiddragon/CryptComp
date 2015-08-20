package compression;

import org.junit.Test;
import static org.junit.Assert.*;
import utility.CCList;

/**
 * Lempel-Ziv-Welch class related tests.
 */
public class LempelZivWelchTest {

    LempelZivWelch lz;

    /**
     * Compression method test.
     */
    @Test
    public void testCompress() {
        lz = new LempelZivWelch();

        String inputText = "ABBABBBABBA";
        int[] inputTable = stringToIntTable(inputText);

        CCList<Integer> expectedOutput = new CCList<>();
        expectedOutput.add(65);
        expectedOutput.add(66);
        expectedOutput.add(66);
        expectedOutput.add(256);
        expectedOutput.add(257);
        expectedOutput.add(259);
        expectedOutput.add(65);

        CCList<Integer> compressed = lz.compress(inputTable);

        verifyCCLists(expectedOutput, compressed);
    }

    /**
     * Decompression method test.
     */
    @Test
    public void testDecompress() {
        lz = new LempelZivWelch();

        String expected = "ABBABBBABBA";
        int[] expectedOutput = stringToIntTable(expected);

        CCList<Integer> input = new CCList<>();
        input.add(65);
        input.add(66);
        input.add(66);
        input.add(256);
        input.add(257);
        input.add(259);
        input.add(65);

        int[] decompressed = lz.decompress(input);

        verifyTables(expectedOutput, decompressed);
    }

    /**
     * Test compression followed by decompression.
     */
    @Test
    public void testCompressionDecompression() {
        lz = new LempelZivWelch();

        String input = "This is a test message that might or might not compress.";
        int[] inputTable = stringToIntTable(input);
        CCList<Integer> compressed = lz.compress(inputTable);
        int[] output = lz.decompress(compressed);

        verifyTables(inputTable, output);
    }

    /**
     * Decompression method test.
     */
    @Test
    public void testDecompressUnexpectedValue() {
        lz = new LempelZivWelch();

        String expected = "AAABAAAABAAAA";
        int[] expectedOutput = stringToIntTable(expected);

        CCList<Integer> input = new CCList<>();
        input.add(65);
        input.add(256);
        input.add(66);
        input.add(256);
        input.add(257);
        input.add(259);
        input.add(65);

        int[] decompressed = lz.decompress(input);

        verifyTables(expectedOutput, decompressed);
    }

    /**
     * Compression method test.
     */
    @Test
    public void testCompressEmptyString() {
        lz = new LempelZivWelch();

        String inputText = "";

        CCList<Integer> compressed = lz.compress(stringToIntTable(inputText));

        assertEquals(0, compressed.getSize());
    }

    /**
     * Helper method converting string to corresponding int table.
     *
     * @param inputText to be converted
     * @return text in int table
     */
    private int[] stringToIntTable(String inputText) {
        int[] inputTable = new int[inputText.length()];

        for (int i = 0; i < inputText.length(); i++) {
            inputTable[i] = inputText.charAt(i);
        }

        return inputTable;
    }

    /**
     * Helper method for verifying if two int tables are equal in size and in
     * contents.
     *
     * @param expected source being compared to
     * @param result that is compared
     */
    private void verifyTables(int[] expected, int[] result) {
        assertEquals(expected.length, result.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    /**
     * Helper method for verifying if two lists are equal in size and in
     * contents.
     *
     * @param expected source being compared to
     * @param result that is compared
     */
    private void verifyCCLists(CCList<Integer> expected, CCList<Integer> result) {
        assertEquals(expected.getSize(), result.getSize());

        for (int i = 0; i < result.getSize(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }
}
