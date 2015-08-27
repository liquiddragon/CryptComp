/**
 * Driver package for created cryptography and compression classes.
 */
package cryptcomp;

import compression.BitPacker;
import compression.LempelZivWelch;
import crypto.DES;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import utility.CCList;

/**
 * Main class for simple CLI.
 */
public class Main {

    private static final int WORD_LIMIT = 65536;

    /**
     * Standard starting function for the application.
     *
     * @param args command line
     */
    public static void main(String[] args) {
        String key = null;

        /* Quick and dirty command line interface for en/decrypt class. This
         * has not been tested much and it might or might not work as expected.
         */
        CmdLineParser cmdParser = new CmdLineParser();
        if (cmdParser.parseCmdLine(args) == false) {
            cmdParser.showUsage();
            return;
        }

        if (cmdParser.hasOption(CmdLineParser.CCOptions.ENC.getOption())
                && cmdParser.getOption(CmdLineParser.CCOptions.ENC.getOption()).equalsIgnoreCase(CmdLineParser.CCCryptoAlgo.DES.getAlgo())
                || cmdParser.hasOption(CmdLineParser.CCOptions.DEC.getOption())
                && cmdParser.getOption(CmdLineParser.CCOptions.DEC.getOption()).equalsIgnoreCase(CmdLineParser.CCCryptoAlgo.DES.getAlgo())) {

            if (!cmdParser.hasOption(CmdLineParser.CCOptions.KEY.getOption())) {
                System.out.println("Key option is missing");
                cmdParser.showUsage();
                System.exit(1);
            }
            key = cmdParser.getOption(CmdLineParser.CCOptions.KEY.getOption());
            if (key == null) {
                System.out.println("Key is missing");
                cmdParser.showUsage();
                System.exit(1);
            }
        }

        String inFile = null;
        String outFile = null;
        if (cmdParser.hasOption(CmdLineParser.CCOptions.INFILE.getOption())) {
            inFile = cmdParser.getOption(CmdLineParser.CCOptions.INFILE.getOption());

            if (inFile == null) {
                System.out.println("Input file name is missing");
                cmdParser.showUsage();
                System.exit(1);
            }
        }

        if (cmdParser.hasOption(CmdLineParser.CCOptions.OUTFILE.getOption())) {
            outFile = cmdParser.getOption(CmdLineParser.CCOptions.OUTFILE.getOption());

            if (outFile == null) {
                System.out.println("Output file name is missing");
                cmdParser.showUsage();
                System.exit(1);
            }
        }

        DataInputStream dis = null;
        DataOutputStream dos = null;

        if (inFile == null) {
            dis = new DataInputStream(new BufferedInputStream(System.in));
        } else {
            try {
                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(inFile)));
            } catch (FileNotFoundException fnfe) {
                System.out.println("There is an issue with input. Check following: " + fnfe.getMessage());
                cmdParser.showUsage();
                System.exit(1);
            }
        }

        if (outFile == null) {
            dos = new DataOutputStream(new BufferedOutputStream(System.out));
        } else {
            try {
                dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile, false)));
            } catch (FileNotFoundException fnfe) {
                System.out.println("There is an issue with output. Check following: " + fnfe.getMessage());
                cmdParser.showUsage();
                try {
                    dis.close();
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
                System.exit(1);
            }
        }

        if (cmdParser.hasOption(CmdLineParser.CCOptions.ENC.getOption())
                && cmdParser.getOption(CmdLineParser.CCOptions.ENC.getOption()).equalsIgnoreCase(CmdLineParser.CCCryptoAlgo.DES.getAlgo())) {
            encryptDES(key, dis, dos);
        } else if (cmdParser.hasOption(CmdLineParser.CCOptions.DEC.getOption())
                && cmdParser.getOption(CmdLineParser.CCOptions.DEC.getOption()).equalsIgnoreCase(CmdLineParser.CCCryptoAlgo.DES.getAlgo())) {
            decryptDES(key, dis, dos);
        } else if (cmdParser.hasOption(CmdLineParser.CCOptions.PACK.getOption())) {
            boolean bitPacking = true;
            if (cmdParser.hasOption(CmdLineParser.CCOptions.NOBIT.getOption())) {
                bitPacking = false;
            }
            int dictSize = 12;
            if (cmdParser.hasOption(CmdLineParser.CCOptions.DICTSIZE.getOption())) {
                int value = Integer.parseInt(cmdParser.getOption(CmdLineParser.CCOptions.DICTSIZE.getOption()));
                if (value >= 9 && value <= 16) {
                    dictSize = value;
                }
            }
            pack(dis, dos, bitPacking, dictSize);
        } else if (cmdParser.hasOption(CmdLineParser.CCOptions.UNPACK.getOption())) {
            boolean bitPacking = true;
            if (cmdParser.hasOption(CmdLineParser.CCOptions.NOBIT.getOption())) {
                bitPacking = false;
            }
            int dictSize = 12;
            if (cmdParser.hasOption(CmdLineParser.CCOptions.DICTSIZE.getOption())) {
                int value = Integer.parseInt(cmdParser.getOption(CmdLineParser.CCOptions.DICTSIZE.getOption()));
                if (value >= 9 && value <= 16) {
                    dictSize = value;
                }
            }
            unpack(dis, dos, bitPacking, dictSize);
        }

        if (dis != null) {
            try {
                dis.close();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    /**
     * Encrypt input stream to output stream using given key.
     *
     * @param key for encryption
     * @param dis input stream being encrypted
     * @param dos output stream where encrypted stream is written
     */
    private static void encryptDES(String key, DataInputStream dis, DataOutputStream dos) {
        CryptoParams cp = new CryptoParams();
        int blockSize = 8;
        cp.setBlockSize(blockSize);
        cp.setEndOfFile(false);
        DES des = new DES(key);

        long alku = System.currentTimeMillis();
        do {
            cp.setTextBuffer(new byte[blockSize]);
            readBlock(cp, dis, des);
            byte[] cipherText = new byte[blockSize];
            des.encryptBlock(cp.getTextBuffer(), 0, cipherText, 0, DES.OperationMode.ECB);
            writeBlock(cipherText, dos);
        } while (cp.isEndOfFile() == false);
        long loppu = System.currentTimeMillis();
        
        System.out.println("Aika: " + (loppu-alku) + " ms");
    }

    /**
     * Decrypt input stream to output stream using given key.
     *
     * @param key for decryption
     * @param dis input stream being decrypted
     * @param dos output stream where decrypted stream is written
     */
    private static void decryptDES(String key, DataInputStream dis, DataOutputStream dos) {
        CryptoParams cp = new CryptoParams();
        int blockSize = 8;
        cp.setBlockSize(blockSize);
        cp.setEndOfFile(false);
        DES des = new DES(key);

        long alku = System.currentTimeMillis();
        do {
            cp.setTextBuffer(new byte[blockSize]);
            readBlock(cp, dis, des);
            if (cp.isEndOfFile() == false || (cp.isEndOfFile() == true && cp.getReadCount() > 0)) {
                byte[] clearText = new byte[blockSize];
                des.decryptBlock(cp.getTextBuffer(), 0, clearText, 0, DES.OperationMode.ECB);
                writeBlock(des.removePadding(clearText), dos);
            }
        } while (cp.isEndOfFile() == false);
        long loppu = System.currentTimeMillis();
        
        System.out.println("Aika: " + (loppu-alku) + " ms");
    }

    /**
     * Read a block from input.
     *
     * @param cp info block containing input information
     * @param dis buffered input stream
     * @param des crypto provider
     */
    private static void readBlock(CryptoParams cp, DataInputStream dis, DES des) {
        boolean EOF = false;
        int count = 0;
        int input = -1;
        byte[] buffer = cp.getTextBuffer();

        do {
            try {
                input = dis.read();
                if (input > -1) {
                    buffer[count] = (byte) input;
                    count++;
                } else {
                    EOF = true;
                }
            } catch (IOException ioe) {
                System.out.println("Error: " + ioe.getMessage());
            }
        } while (input != -1 && count < cp.getBlockSize());

        if (count < cp.getBlockSize()) {
            byte[] tmpBuffer = Arrays.copyOf(buffer, count);
            cp.setTextBuffer(des.addPadding(tmpBuffer));
        }

        cp.setReadCount(count);
        if (EOF) {
            cp.setEndOfFile(true);
        }
    }

    /**
     * Write a block to output.
     *
     * @param buffer to be written
     * @param dos buffered output stream
     */
    private static void writeBlock(byte[] buffer, DataOutputStream dos) {
        try {
            dos.write(buffer, 0, buffer.length);
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    /**
     * Pack input stream into output stream.
     *
     * @param dis buffered input stream
     * @param dos buffered output stream
     * @param bitPacking true if bit packing is to be used, false otherwise
     * @param dictSize dictionary size when compressing data
     */
    private static void pack(DataInputStream dis, DataOutputStream dos, boolean bitPacking, int dictSize) {
        try {
            int size = dis.available();
            byte[] input = new byte[size];
            CCList<Integer> packed;
            LempelZivWelch lzw = new LempelZivWelch();
            lzw.setDictionarySize(dictSize);

            // Read file to be packed
            dis.read(input, 0, size);
            int[] inputBuffer = convertByteToInt(input);

            packed = lzw.compress(inputBuffer);

            // Prepare for optional bit packing
            int[] finalPacked;
            int writeValue = 0;
            int highValue = lzw.getHighestKeyValue();
            BitPacker bp = new BitPacker();

            if (bitPacking) {
                bp.maxValue(highValue);
                finalPacked = bp.pack(CCList.convertCCListIntToArray(packed));
                writeValue = bp.packedCount();
            } else {
                finalPacked = CCList.convertCCListIntToArray(packed);
                writeValue = finalPacked.length;
            }

            // Write file header
            CompressHeader cHeader = new CompressHeader(highValue, bitPacking, bp.packedCount());
            cHeader.writeHeader(dos);

            // Write file
            int index = 0;
            while (index < writeValue) {
                if (highValue < WORD_LIMIT) {
                    dos.writeShort(finalPacked[index++]);
                } else {
                    dos.writeInt(finalPacked[index++]);
                }
            }

        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    /**
     * Unpack input stream into output stream.
     *
     * @param dis buffered input stream
     * @param dos buffered output stream
     * @param bitPacking true if bit packing is to be used, false otherwise
     * @param dictSize dictionary size when decompressing data
     */
    private static void unpack(DataInputStream dis, DataOutputStream dos, boolean bitPacking, int dictSize) {
        try {
            CCList<Integer> packed = new CCList<>();
            LempelZivWelch lzw = new LempelZivWelch();
            lzw.setDictionarySize(dictSize);

            CompressHeader cHeader = new CompressHeader(WORD_LIMIT, bitPacking, 0);
            cHeader.readHeader(dis);
            int maxValue = cHeader.getHighestValue();

            do {
                int in;
                if (maxValue < WORD_LIMIT) {
                    in = dis.readShort();

                } else {
                    in = dis.readInt();
                }
                packed.add(in & 0xffff);
            } while (dis.available() > 0);

            if (bitPacking) {
                BitPacker bp = new BitPacker();
                bp.maxValue(cHeader.getHighestValue());
                int[] unbitted = bp.unpack(CCList.convertCCListIntToArray(packed), cHeader.getPackedCount());
                packed = CCList.convertArrayToCCListInt(unbitted);
            }

            int[] unpacked = lzw.decompress(packed);

            int unpackCount = countUsage(unpacked);

            if (unpacked != null) {
                int index = 0;
                while (index < unpackCount) {
                    dos.write(unpacked[index++]);
                }
            }
        } catch (EOFException eof) {
            System.out.println("End of file: " + eof.getMessage());
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    /**
     * Convert byte array to integer array.
     *
     * @param input byte array
     * @return integer array
     */
    private static int[] convertByteToInt(byte[] input) {
        int[] output = new int[input.length];
        int count = 0;

        while (count < input.length) {
            output[count] = Byte.toUnsignedInt(input[count++]);
        }

        return output;
    }

    /**
     * Count used bytes from the table.
     *
     * @param table containing unused bytes as zeros at the end
     * @return count of used bytes
     */
    private static int countUsage(int[] table) {
        int index = table.length - 1;

        while (index > 0 && table[index] == 0) {
            index--;
        }

        return index + 1;
    }
}
