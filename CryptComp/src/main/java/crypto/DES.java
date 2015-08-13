/**
 * Cryptography algorithm package.
 */
package crypto;

import java.util.Arrays;

/**
 * This class implements DES in ECB and CBC modes using PKCS5 padding.
 */
public class DES {

    private final int keyBits = 56;
    private final int rounds = 16;
    private final int blockSize = 8;
    private final int keySize = 8;
    private final int[] encryptKeys = new int[32];
    private final int[] decryptKeys = new int[32];
    private final int[] tempInts = new int[2];
    private byte[] copyOfInput = new byte[blockSize];
    private boolean skipDone = false;
    private OperationMode opMode;

    /**
     * DES operation mode.
     */
    public enum OperationMode {

        /**
         * Electronic codebook.
         */
        ECB,
        /**
         * Cipher blocking chaining.
         */
        CBC
    };

    /**
     * Constructs a DES object using provided key in String format for
     * encryption or decryption.
     *
     * @param keyStr key to be used
     */
    public DES(String keyStr) {
        opMode = OperationMode.CBC;
        byte[] key = strToByteKey(keyStr);
        setKey(key);
    }

    /**
     * Constructs a DES object using provided key in byte format for encryption
     * or decryption.
     *
     * @param key key to be used
     */
    public DES(byte[] key) {
        opMode = OperationMode.CBC;
        setKey(key);
    }

    /**
     * Encrypt provided bytes using key generated during class instantiation.
     *
     * @param clearText bytes to be encrypted
     * @return encrypted bytes
     */
    public byte[] encrypt(byte[] clearText) {
        initBlockCrypter();

        byte[] clearTextWithPadding = addPadding(clearText);

        int length = clearTextWithPadding.length;
        int count = length / 8;
        byte[] cipherText = new byte[length];

        for (int i = 0; i < count; i++) {
            encryptBlock(clearTextWithPadding, i * blockSize, cipherText, i * blockSize, opMode);
        }

        return cipherText;
    }

    /**
     * Decrypt provided bytes using key generated during class instantiation.
     *
     * @param cipherText encrypted bytes
     * @return decrypted bytes
     */
    public byte[] decrypt(byte[] cipherText) {
        initBlockCrypter();

        int length = cipherText.length;

        if (length % blockSize != 0) {
            return null;
        }

        byte[] clearText = new byte[length];

        int count = length / blockSize;
        for (int i = 0; i < count; i++) {
            decryptBlock(cipherText, i * blockSize, clearText, i * blockSize, opMode);
        }

        byte[] clearTextWithouPadding = removePadding(clearText);

        return clearTextWithouPadding;
    }

    /**
     * Encrypt a block of eight bytes.
     *
     * @param clearText clear bytes
     * @param clearOff offset to clear bytes being encrypted
     * @param cipherText encrypted bytes
     * @param cipherOff offset to encrypted bytes being produced
     * @param opMode operation mode of encrypt
     */
    public void encryptBlock(byte[] clearText, int clearOff, byte[] cipherText, int cipherOff, OperationMode opMode) {
        if (opMode == OperationMode.CBC) {
            for (int i = 0; i < blockSize; i++) {
                clearText[clearOff + i] ^= copyOfInput[i];
            }
        }

        squashBytesToInts(clearText, clearOff, tempInts, 0, 2);
        des(tempInts, tempInts, encryptKeys);
        spreadIntsToBytes(tempInts, 0, cipherText, cipherOff, 2);

        if (opMode == OperationMode.CBC) {
            copyOfInput = Arrays.copyOfRange(cipherText, cipherOff, cipherOff + blockSize);
        }
    }

    /**
     * Decrypt a block of eight bytes.
     *
     * @param cipherText encrypted bytes
     * @param cipherOff offset to encrypted bytes being decrypted
     * @param clearText decrypted bytes
     * @param clearOff offset to decrypted bytes being produced
     * @param opMode operation mode of decrypt
     */
    public void decryptBlock(byte[] cipherText, int cipherOff, byte[] clearText, int clearOff, OperationMode opMode) {
        byte[] backup = new byte[blockSize];

        if (opMode == OperationMode.CBC) {
            if (skipDone == true) {
                backup = Arrays.copyOf(copyOfInput, copyOfInput.length);
            }
            copyOfInput = Arrays.copyOfRange(cipherText, cipherOff, cipherOff + blockSize);
        }

        squashBytesToInts(cipherText, cipherOff, tempInts, 0, 2);
        des(tempInts, tempInts, decryptKeys);
        spreadIntsToBytes(tempInts, 0, clearText, clearOff, 2);

        if (opMode == OperationMode.CBC && skipDone == true) {
            for (int i = 0; i < blockSize; i++) {
                clearText[clearOff + i] ^= backup[i];
            }
        } else {
            skipDone = true;
        }
    }

    /**
     * Add padding to input below block size according to PKCS5.
     *
     * @param input for which padding might be required
     * @return input with possible padding
     */
    public byte[] addPadding(byte[] input) {
        int paddingLen = 0;

        if (input.length % blockSize != 0) {
            paddingLen = blockSize - (input.length % blockSize);
        }

        byte[] inputWithPadding = Arrays.copyOf(input, input.length + paddingLen);

        if (paddingLen > 0) {
            for (int i = 0; i < paddingLen; i++) {
                inputWithPadding[input.length + i] = (byte) paddingLen;
            }
        }

        return inputWithPadding;
    }

    /**
     * Remove padding according to PKCS5 from input if present.
     *
     * @param input containing possible padding
     * @return input with padding, if any, removed
     */
    public byte[] removePadding(byte[] input) {
        int count = input[input.length - 1];
        int strip = 0;

        if (count > 0 && count <= blockSize) {
            strip = count;
        }
        byte[] inputWithoutPadding = Arrays.copyOf(input, input.length - strip);

        return inputWithoutPadding;
    }

    /**
     * Initialise block en/decrypt for operation.
     */
    public void initBlockCrypter() {
        Arrays.fill(copyOfInput, (byte) 0);
        skipDone = false;
    }

    /**
     * Set en/decrypt operation mode.
     *
     * @param opMode desired operation mode
     */
    public void setOperationMode(OperationMode opMode) {
        this.opMode = opMode;
    }

    /**
     * Convert key string into series of eight bytes.
     *
     * @param keyStr key to be transformed
     * @return key in bytes
     */
    private byte[] strToByteKey(String keyStr) {
        byte[] key = new byte[keySize];

        for (int i = 0; i < key.length; ++i) {
            key[i] = 0;
        }

        for (int i = 0, j = 0; i < keyStr.length(); ++i, j = (j + 1) % key.length) {
            key[j] ^= (byte) keyStr.charAt(i);
        }

        return key;
    }

    /**
     * Set provided key to be used for requested operations.
     *
     * @param key key to be used
     */
    private void setKey(byte[] key) {

        // Set parity bit
        for (int i = 0; i < keySize; ++i) {
            if (Integer.bitCount(key[i]) % 2 == 0) {
                key[i] ^= 0x80;
            }
        }

        deskey(key, true, encryptKeys);
        deskey(key, false, decryptKeys);
    }

    /**
     * Create sub-keys used during DES.
     *
     * @param keyBlock original key
     * @param encrypting flag indicating whether keys are for encrypting (true)
     * or decrypting (false)
     * @param KnL generated sub-keys
     */
    private void deskey(byte[] keyBlock, boolean encrypting, int[] KnL) {
        int i, j, l, m, n;
        int[] pc1m = new int[keyBits];
        int[] pcr = new int[keyBits];
        int[] kn = new int[32];

        // Initial key transformation
        for (j = 0; j < keyBits; ++j) {
            l = permutedChoiceOne[j];
            m = l & 07;
            pc1m[j] = ((keyBlock[l >>> 3] & bitsInByte[m]) != 0) ? 1 : 0;
        }

        // Prepara sub-keys for each round of DES
        for (i = 0; i < rounds; ++i) {
            if (encrypting == true) {
                m = i << 1;
            } else {
                m = (15 - i) << 1;
            }
            n = m + 1;
            kn[m] = kn[n] = 0;

            // First half
            for (j = 0; j < (keyBits / 2); ++j) {
                l = j + originalKeyRotation[i];
                if (l < (keyBits / 2)) {
                    pcr[j] = pc1m[l];
                } else {
                    pcr[j] = pc1m[l - (keyBits / 2)];
                }
            }

            // Second half
            for (j = (keyBits / 2); j < keyBits; ++j) {
                l = j + originalKeyRotation[i];
                if (l < keyBits) {
                    pcr[j] = pc1m[l];
                } else {
                    pcr[j] = pc1m[l - (keyBits / 2)];
                }
            }

            // Compression permutation
            for (j = 0; j < 24; ++j) {
                if (pcr[permutedChoiceTwo[j]] != 0) {
                    kn[m] |= bigByteMask[j];
                }
                if (pcr[permutedChoiceTwo[j + 24]] != 0) {
                    kn[n] |= bigByteMask[j];
                }
            }
        }

        cookey(kn, KnL);
    }

    /**
     * Combine generated sub-keys into whole keys.
     *
     * @param rawKey raw sub-key
     * @param cookedKeys cooked sub-key
     */
    private void cookey(int[] rawKey, int cookedKeys[]) {
        int rawOne, rawTwo;
        int rawIndex, cookedKeyIndex;
        int i;

        for (i = 0, rawIndex = 0, cookedKeyIndex = 0; i < rounds; ++i) {
            rawOne = rawKey[rawIndex++];
            rawTwo = rawKey[rawIndex++];

            cookedKeys[cookedKeyIndex] = (rawOne & 0x00fc0000) << 6;
            cookedKeys[cookedKeyIndex] |= (rawOne & 0x00000fc0) << 10;
            cookedKeys[cookedKeyIndex] |= (rawTwo & 0x00fc0000) >>> 10;
            cookedKeys[cookedKeyIndex] |= (rawTwo & 0x00000fc0) >>> 6;
            ++cookedKeyIndex;

            cookedKeys[cookedKeyIndex] = (rawOne & 0x0003f000) << 12;
            cookedKeys[cookedKeyIndex] |= (rawOne & 0x0000003f) << 16;
            cookedKeys[cookedKeyIndex] |= (rawTwo & 0x0003f000) >>> 4;
            cookedKeys[cookedKeyIndex] |= (rawTwo & 0x0000003f);
            ++cookedKeyIndex;
        }
    }

    /**
     * Transform bytes into integers for DES.
     *
     * @param inBytes bytes to be processed
     * @param inOff offset to bytes being processed
     * @param outInts output of processed bytes
     * @param outOff offset to output
     * @param intLen block length being processed
     */
    private void squashBytesToInts(byte[] inBytes, int inOff, int[] outInts, int outOff, int intLen) {
        for (int i = 0; i < intLen; ++i) {
            outInts[outOff + i]
                    = ((inBytes[inOff + i * 4] & 0xff) << 24)
                    | ((inBytes[inOff + i * 4 + 1] & 0xff) << 16)
                    | ((inBytes[inOff + i * 4 + 2] & 0xff) << 8)
                    | (inBytes[inOff + i * 4 + 3] & 0xff);
        }
    }

    /**
     * Transform integers into bytes after DES processing.
     *
     * @param inInts integers to be processed
     * @param inOff offset to integers being processed
     * @param outBytes output of processed integers
     * @param outOff offset to output
     * @param intLen block length being processed
     */
    private void spreadIntsToBytes(int[] inInts, int inOff, byte[] outBytes, int outOff, int intLen) {
        for (int i = 0; i < intLen; ++i) {
            outBytes[outOff + i * 4] = (byte) (inInts[inOff + i] >>> 24);
            outBytes[outOff + i * 4 + 1] = (byte) (inInts[inOff + i] >>> 16);
            outBytes[outOff + i * 4 + 2] = (byte) (inInts[inOff + i] >>> 8);
            outBytes[outOff + i * 4 + 3] = (byte) inInts[inOff + i];
        }
    }

    /**
     * The DES, or the Feistel function. For further information see e.g.
     * http://www.wikipedia.org/wiki/Data_Encryption_Standard
     *
     * @param inInts input
     * @param outInts output
     * @param keys sub-keys
     */
    private void des(int[] inInts, int[] outInts, int[] keys) {
        int fval, work, right, leftt;
        int round;
        int keysi = 0;

        leftt = inInts[0];
        right = inInts[1];

        work = ((leftt >>> 4) ^ right) & 0x0f0f0f0f;
        right ^= work;
        leftt ^= (work << 4);

        work = ((leftt >>> 16) ^ right) & 0x0000ffff;
        right ^= work;
        leftt ^= (work << 16);

        work = ((right >>> 2) ^ leftt) & 0x33333333;
        leftt ^= work;
        right ^= (work << 2);

        work = ((right >>> 8) ^ leftt) & 0x00ff00ff;
        leftt ^= work;
        right ^= (work << 8);
        right = (right << 1) | ((right >>> 31) & 1);

        work = (leftt ^ right) & 0xaaaaaaaa;
        leftt ^= work;
        right ^= work;
        leftt = (leftt << 1) | ((leftt >>> 31) & 1);

        for (round = 0; round < 8; ++round) {
            work = (right << 28) | (right >>> 4);
            work ^= keys[keysi++];

            fval = SBox7[work & 0x0000003f];
            fval |= SBox5[(work >>> 8) & 0x0000003f];
            fval |= SBox3[(work >>> 16) & 0x0000003f];
            fval |= SBox1[(work >>> 24) & 0x0000003f];
            work = right ^ keys[keysi++];

            fval |= SBox8[work & 0x0000003f];
            fval |= SBox6[(work >>> 8) & 0x0000003f];
            fval |= SBox4[(work >>> 16) & 0x0000003f];
            fval |= SBox2[(work >>> 24) & 0x0000003f];
            leftt ^= fval;
            work = (leftt << 28) | (leftt >>> 4);
            work ^= keys[keysi++];

            fval = SBox7[work & 0x0000003f];
            fval |= SBox5[(work >>> 8) & 0x0000003f];
            fval |= SBox3[(work >>> 16) & 0x0000003f];
            fval |= SBox1[(work >>> 24) & 0x0000003f];
            work = leftt ^ keys[keysi++];

            fval |= SBox8[work & 0x0000003f];
            fval |= SBox6[(work >>> 8) & 0x0000003f];
            fval |= SBox4[(work >>> 16) & 0x0000003f];
            fval |= SBox2[(work >>> 24) & 0x0000003f];
            right ^= fval;
        }

        right = (right << 31) | (right >>> 1);
        work = (leftt ^ right) & 0xaaaaaaaa;
        leftt ^= work;
        right ^= work;
        leftt = (leftt << 31) | (leftt >>> 1);
        work = ((leftt >>> 8) ^ right) & 0x00ff00ff;
        right ^= work;
        leftt ^= (work << 8);
        work = ((leftt >>> 2) ^ right) & 0x33333333;
        right ^= work;
        leftt ^= (work << 2);
        work = ((right >>> 16) ^ leftt) & 0x0000ffff;
        leftt ^= work;
        right ^= (work << 16);
        work = ((right >>> 4) ^ leftt) & 0x0f0f0f0f;
        leftt ^= work;
        right ^= (work << 4);
        outInts[0] = right;
        outInts[1] = leftt;
    }

    private static byte[] bitsInByte = {
        (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
        (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    private static int[] bigByteMask = {
        0x800000, 0x400000, 0x200000, 0x100000,
        0x080000, 0x040000, 0x020000, 0x010000,
        0x008000, 0x004000, 0x002000, 0x001000,
        0x000800, 0x000400, 0x000200, 0x000100,
        0x000080, 0x000040, 0x000020, 0x000010,
        0x000008, 0x000004, 0x000002, 0x000001
    };

    // A.k.a. key permutation
    private static byte[] permutedChoiceOne = {
        (byte) 56, (byte) 48, (byte) 40, (byte) 32, (byte) 24, (byte) 16, (byte) 8,
        (byte) 0, (byte) 57, (byte) 49, (byte) 41, (byte) 33, (byte) 25, (byte) 17,
        (byte) 9, (byte) 1, (byte) 58, (byte) 50, (byte) 42, (byte) 34, (byte) 26,
        (byte) 18, (byte) 10, (byte) 2, (byte) 59, (byte) 51, (byte) 43, (byte) 35,
        (byte) 62, (byte) 54, (byte) 46, (byte) 38, (byte) 30, (byte) 22, (byte) 14,
        (byte) 6, (byte) 61, (byte) 53, (byte) 45, (byte) 37, (byte) 29, (byte) 21,
        (byte) 13, (byte) 5, (byte) 60, (byte) 52, (byte) 44, (byte) 36, (byte) 28,
        (byte) 20, (byte) 12, (byte) 4, (byte) 27, (byte) 19, (byte) 11, (byte) 3
    };

    // A.k.a compression permutation
    private static byte[] permutedChoiceTwo = {
        (byte) 13, (byte) 16, (byte) 10, (byte) 23, (byte) 0, (byte) 4,
        (byte) 2, (byte) 27, (byte) 14, (byte) 5, (byte) 20, (byte) 9,
        (byte) 22, (byte) 18, (byte) 11, (byte) 3, (byte) 25, (byte) 7,
        (byte) 15, (byte) 6, (byte) 26, (byte) 19, (byte) 12, (byte) 1,
        (byte) 40, (byte) 51, (byte) 30, (byte) 36, (byte) 46, (byte) 54,
        (byte) 29, (byte) 39, (byte) 50, (byte) 44, (byte) 32, (byte) 47,
        (byte) 43, (byte) 48, (byte) 38, (byte) 55, (byte) 33, (byte) 52,
        (byte) 45, (byte) 41, (byte) 49, (byte) 35, (byte) 28, (byte) 31};

    private static int[] originalKeyRotation = {
        1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28
    };

    // S-boxes
    private static int[] SBox1 = {
        0x01010400, 0x00000000, 0x00010000, 0x01010404,
        0x01010004, 0x00010404, 0x00000004, 0x00010000,
        0x00000400, 0x01010400, 0x01010404, 0x00000400,
        0x01000404, 0x01010004, 0x01000000, 0x00000004,
        0x00000404, 0x01000400, 0x01000400, 0x00010400,
        0x00010400, 0x01010000, 0x01010000, 0x01000404,
        0x00010004, 0x01000004, 0x01000004, 0x00010004,
        0x00000000, 0x00000404, 0x00010404, 0x01000000,
        0x00010000, 0x01010404, 0x00000004, 0x01010000,
        0x01010400, 0x01000000, 0x01000000, 0x00000400,
        0x01010004, 0x00010000, 0x00010400, 0x01000004,
        0x00000400, 0x00000004, 0x01000404, 0x00010404,
        0x01010404, 0x00010004, 0x01010000, 0x01000404,
        0x01000004, 0x00000404, 0x00010404, 0x01010400,
        0x00000404, 0x01000400, 0x01000400, 0x00000000,
        0x00010004, 0x00010400, 0x00000000, 0x01010004
    };

    private static int[] SBox2 = {
        0x80108020, 0x80008000, 0x00008000, 0x00108020,
        0x00100000, 0x00000020, 0x80100020, 0x80008020,
        0x80000020, 0x80108020, 0x80108000, 0x80000000,
        0x80008000, 0x00100000, 0x00000020, 0x80100020,
        0x00108000, 0x00100020, 0x80008020, 0x00000000,
        0x80000000, 0x00008000, 0x00108020, 0x80100000,
        0x00100020, 0x80000020, 0x00000000, 0x00108000,
        0x00008020, 0x80108000, 0x80100000, 0x00008020,
        0x00000000, 0x00108020, 0x80100020, 0x00100000,
        0x80008020, 0x80100000, 0x80108000, 0x00008000,
        0x80100000, 0x80008000, 0x00000020, 0x80108020,
        0x00108020, 0x00000020, 0x00008000, 0x80000000,
        0x00008020, 0x80108000, 0x00100000, 0x80000020,
        0x00100020, 0x80008020, 0x80000020, 0x00100020,
        0x00108000, 0x00000000, 0x80008000, 0x00008020,
        0x80000000, 0x80100020, 0x80108020, 0x00108000
    };

    private static int[] SBox3 = {
        0x00000208, 0x08020200, 0x00000000, 0x08020008,
        0x08000200, 0x00000000, 0x00020208, 0x08000200,
        0x00020008, 0x08000008, 0x08000008, 0x00020000,
        0x08020208, 0x00020008, 0x08020000, 0x00000208,
        0x08000000, 0x00000008, 0x08020200, 0x00000200,
        0x00020200, 0x08020000, 0x08020008, 0x00020208,
        0x08000208, 0x00020200, 0x00020000, 0x08000208,
        0x00000008, 0x08020208, 0x00000200, 0x08000000,
        0x08020200, 0x08000000, 0x00020008, 0x00000208,
        0x00020000, 0x08020200, 0x08000200, 0x00000000,
        0x00000200, 0x00020008, 0x08020208, 0x08000200,
        0x08000008, 0x00000200, 0x00000000, 0x08020008,
        0x08000208, 0x00020000, 0x08000000, 0x08020208,
        0x00000008, 0x00020208, 0x00020200, 0x08000008,
        0x08020000, 0x08000208, 0x00000208, 0x08020000,
        0x00020208, 0x00000008, 0x08020008, 0x00020200
    };

    private static int[] SBox4 = {
        0x00802001, 0x00002081, 0x00002081, 0x00000080,
        0x00802080, 0x00800081, 0x00800001, 0x00002001,
        0x00000000, 0x00802000, 0x00802000, 0x00802081,
        0x00000081, 0x00000000, 0x00800080, 0x00800001,
        0x00000001, 0x00002000, 0x00800000, 0x00802001,
        0x00000080, 0x00800000, 0x00002001, 0x00002080,
        0x00800081, 0x00000001, 0x00002080, 0x00800080,
        0x00002000, 0x00802080, 0x00802081, 0x00000081,
        0x00800080, 0x00800001, 0x00802000, 0x00802081,
        0x00000081, 0x00000000, 0x00000000, 0x00802000,
        0x00002080, 0x00800080, 0x00800081, 0x00000001,
        0x00802001, 0x00002081, 0x00002081, 0x00000080,
        0x00802081, 0x00000081, 0x00000001, 0x00002000,
        0x00800001, 0x00002001, 0x00802080, 0x00800081,
        0x00002001, 0x00002080, 0x00800000, 0x00802001,
        0x00000080, 0x00800000, 0x00002000, 0x00802080
    };

    private static int[] SBox5 = {
        0x00000100, 0x02080100, 0x02080000, 0x42000100,
        0x00080000, 0x00000100, 0x40000000, 0x02080000,
        0x40080100, 0x00080000, 0x02000100, 0x40080100,
        0x42000100, 0x42080000, 0x00080100, 0x40000000,
        0x02000000, 0x40080000, 0x40080000, 0x00000000,
        0x40000100, 0x42080100, 0x42080100, 0x02000100,
        0x42080000, 0x40000100, 0x00000000, 0x42000000,
        0x02080100, 0x02000000, 0x42000000, 0x00080100,
        0x00080000, 0x42000100, 0x00000100, 0x02000000,
        0x40000000, 0x02080000, 0x42000100, 0x40080100,
        0x02000100, 0x40000000, 0x42080000, 0x02080100,
        0x40080100, 0x00000100, 0x02000000, 0x42080000,
        0x42080100, 0x00080100, 0x42000000, 0x42080100,
        0x02080000, 0x00000000, 0x40080000, 0x42000000,
        0x00080100, 0x02000100, 0x40000100, 0x00080000,
        0x00000000, 0x40080000, 0x02080100, 0x40000100
    };

    private static int[] SBox6 = {
        0x20000010, 0x20400000, 0x00004000, 0x20404010,
        0x20400000, 0x00000010, 0x20404010, 0x00400000,
        0x20004000, 0x00404010, 0x00400000, 0x20000010,
        0x00400010, 0x20004000, 0x20000000, 0x00004010,
        0x00000000, 0x00400010, 0x20004010, 0x00004000,
        0x00404000, 0x20004010, 0x00000010, 0x20400010,
        0x20400010, 0x00000000, 0x00404010, 0x20404000,
        0x00004010, 0x00404000, 0x20404000, 0x20000000,
        0x20004000, 0x00000010, 0x20400010, 0x00404000,
        0x20404010, 0x00400000, 0x00004010, 0x20000010,
        0x00400000, 0x20004000, 0x20000000, 0x00004010,
        0x20000010, 0x20404010, 0x00404000, 0x20400000,
        0x00404010, 0x20404000, 0x00000000, 0x20400010,
        0x00000010, 0x00004000, 0x20400000, 0x00404010,
        0x00004000, 0x00400010, 0x20004010, 0x00000000,
        0x20404000, 0x20000000, 0x00400010, 0x20004010
    };

    private static int[] SBox7 = {
        0x00200000, 0x04200002, 0x04000802, 0x00000000,
        0x00000800, 0x04000802, 0x00200802, 0x04200800,
        0x04200802, 0x00200000, 0x00000000, 0x04000002,
        0x00000002, 0x04000000, 0x04200002, 0x00000802,
        0x04000800, 0x00200802, 0x00200002, 0x04000800,
        0x04000002, 0x04200000, 0x04200800, 0x00200002,
        0x04200000, 0x00000800, 0x00000802, 0x04200802,
        0x00200800, 0x00000002, 0x04000000, 0x00200800,
        0x04000000, 0x00200800, 0x00200000, 0x04000802,
        0x04000802, 0x04200002, 0x04200002, 0x00000002,
        0x00200002, 0x04000000, 0x04000800, 0x00200000,
        0x04200800, 0x00000802, 0x00200802, 0x04200800,
        0x00000802, 0x04000002, 0x04200802, 0x04200000,
        0x00200800, 0x00000000, 0x00000002, 0x04200802,
        0x00000000, 0x00200802, 0x04200000, 0x00000800,
        0x04000002, 0x04000800, 0x00000800, 0x00200002
    };

    private static int[] SBox8 = {
        0x10001040, 0x00001000, 0x00040000, 0x10041040,
        0x10000000, 0x10001040, 0x00000040, 0x10000000,
        0x00040040, 0x10040000, 0x10041040, 0x00041000,
        0x10041000, 0x00041040, 0x00001000, 0x00000040,
        0x10040000, 0x10000040, 0x10001000, 0x00001040,
        0x00041000, 0x00040040, 0x10040040, 0x10041000,
        0x00001040, 0x00000000, 0x00000000, 0x10040040,
        0x10000040, 0x10001000, 0x00041040, 0x00040000,
        0x00041040, 0x00040000, 0x10041000, 0x00001000,
        0x00000040, 0x10040040, 0x00001000, 0x00041040,
        0x10001000, 0x00000040, 0x10000040, 0x10040000,
        0x10040040, 0x10000000, 0x00040000, 0x10001040,
        0x00000000, 0x10041040, 0x00040040, 0x10000040,
        0x10040000, 0x10001000, 0x10001040, 0x00000000,
        0x10041040, 0x00041000, 0x00041000, 0x00001040,
        0x00001040, 0x00040040, 0x10000000, 0x10041000
    };
}
