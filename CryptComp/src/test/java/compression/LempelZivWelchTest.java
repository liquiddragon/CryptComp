package compression;

import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;
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
        CCList<Integer> expectedOutput = new CCList<>();
        expectedOutput.add(65);
        expectedOutput.add(66);
        expectedOutput.add(66);
        expectedOutput.add(256);
        expectedOutput.add(257);
        expectedOutput.add(259);
        expectedOutput.add(65);

        CCList<Integer> compressed = lz.compress(inputText);

        for (int i = 0; i < expectedOutput.getSize(); i++) {
            assertEquals(expectedOutput.get(i), compressed.get(i));
        }
    }

    /**
     * Decompression method test.
     */
    @Test
    public void testDecompress() {
        lz = new LempelZivWelch();

        String expectedOutput = "ABBABBBABBA";
        CCList<Integer> input = new CCList<>();
        input.add(65);
        input.add(66);
        input.add(66);
        input.add(256);
        input.add(257);
        input.add(259);
        input.add(65);

        String decompressed = lz.decompress(input);

        assertEquals(expectedOutput, decompressed);
    }

    /**
     * Test compression followed by decompression.
     */
    @Test
    public void testCompressionDecompression() {
        lz = new LempelZivWelch();

        String input = "This is a test message that might or might not compress.";
        CCList<Integer> compressed = lz.compress(input);
        String output = lz.decompress(compressed);

        assertEquals(input, output);
    }

    /**
     * Decompression method test.
     */
    @Test
    public void testDecompressUnexpectedValue() {
        lz = new LempelZivWelch();

        String expectedOutput = "AAABAAAABAAAA";
        CCList<Integer> input = new CCList<>();
        input.add(65);
        input.add(256);
        input.add(66);
        input.add(256);
        input.add(257);
        input.add(259);
        input.add(65);

        String decompressed = lz.decompress(input);

        assertEquals(expectedOutput, decompressed);
    }

    /**
     * Compression method test.
     */
    @Test
    public void testCompressEmptyString() {
        lz = new LempelZivWelch();

        String inputText = "";

        CCList<Integer> compressed = lz.compress(inputText);

        assertEquals(0, compressed.getSize());
    }
}
