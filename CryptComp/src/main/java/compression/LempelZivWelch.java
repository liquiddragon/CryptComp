package compression;

import utility.CCList;
import utility.CCMap;

/**
 * Simple Lempel-Ziv-Welch (LZW) compression algorithm. See
 * <a href="https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch">Wikipedia
 * Lempel-Ziv-Welch</a>
 */
public class LempelZivWelch {

    private final int initialDictionarySize = 256;

    /**
     * Compress given input using ASCII values as default dictionary.
     *
     * @param uncompressed input
     * @return list of integers forming compress output
     */
    public CCList<Integer> compress(String uncompressed) {
        int dictionarySize = initialDictionarySize;
        CCMap<String, Integer> dictionary = new CCMap<>();
        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put("" + (char) i, i);
        }

        String w = "";
        CCList<Integer> result = new CCList<>();

        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc)) {
                w = wc;
            } else {
                result.add(dictionary.get(w));
                dictionary.put(wc, dictionarySize++);
                w = "" + c;
            }
        }
        if (!w.equals("")) {
            result.add(dictionary.get(w));
        }

        return result;
    }

    /**
     * Decompress given input using ASCII values as default dictionary.
     *
     * @param compressed list of integers forming compressed input
     * @return string forming decompressed output
     */
    public String decompress(CCList<Integer> compressed) {
        int dictionarySize = initialDictionarySize;
        CCMap<Integer, String> dictionary = new CCMap<>();

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(i, "" + (char) i);
        }

        String w = "" + (char) (int) compressed.remove(0);
        StringBuilder result = new StringBuilder(w);
        for (int k : compressed) {
            String entry = "";
            if (dictionary.containsKey(k)) {
                entry = dictionary.get(k);
            } else if (k == dictionarySize) {
                entry = w + w.charAt(0);
            }

            result.append(entry);

            dictionary.put(dictionarySize++, w + entry.charAt(0));

            w = entry;
        }

        return result.toString();
    }
}
