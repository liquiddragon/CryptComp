package compression;

import utility.CCList;
import utility.CCTrie;

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
     * @param uncompressed input in clear
     * @return list of integers forming compress output
     */
    public CCList<Integer> compress(int[] uncompressed) {
        CCList<Integer> result = new CCList<>();

        CCTrie dictionary = createBaseDictionary();

        CCList<Integer> w = new CCList<>();
        for (int c : uncompressed) {
            CCList<Integer> wc = new CCList<>(w);
            wc.add(c);

            if (dictionary.contains(convertCCListIntToArray(wc))) {
                w = wc;
            } else {
                result.add(dictionary.get(convertCCListIntToArray(w)));
                dictionary.add(convertCCListIntToArray(wc));
                w.removeAll();
                w.add(c);
            }
        }

        if (w.getSize() > 0) {
            result.add(dictionary.get(convertCCListIntToArray(w)));
        }

        return result;
    }

    /**
     * Decompress given input using ASCII values as default dictionary.
     *
     * @param compressed list of integers forming compressed input
     * @return decompressed output
     */
    public int[] decompress(CCList<Integer> compressed) {
        int dictionarySize = initialDictionarySize;
        CCTrie dictionary = createBaseDictionary();

        CCList<Integer> w = new CCList<>();
        w.add(compressed.get(0));
        CCList<Integer> result = new CCList<>();
        result.add(compressed.get(0));
        compressed.remove(0);

        for (int k : compressed) {
            CCList<Integer> entry = new CCList<>();

            if (dictionary.containsReverse(k)) {
                entry = dictionary.getReverse(k);
            } else if (k == dictionarySize) {
                entry = new CCList<>(w);
                entry.add(w.get(0));
            }

            result.add(entry);
            CCList<Integer> temp = new CCList<>(w);
            temp.add(entry.get(0));
            dictionary.add(convertCCListIntToArray(temp));
            w = entry;
        }

        return convertCCListIntToArray(result);
    }

    /**
     * Create base dictionary for compression and decompression use.
     *
     * @return base dictionary
     */
    private CCTrie createBaseDictionary() {
        CCTrie dictionary = new CCTrie();

        for (int i = 0; i < initialDictionarySize; i++) {
            dictionary.add(new int[]{i});
        }

        return dictionary;
    }

    /**
     * Helper method converting list collection into int table
     *
     * @param list collection to be converted
     * @return table containing the collection
     */
    private int[] convertCCListIntToArray(CCList<Integer> list) {
        int[] returnArray = new int[list.getSize()];

        for (int i = 0; i < list.getSize(); i++) {
            returnArray[i] = list.get(i);
        }

        return returnArray;
    }
}
