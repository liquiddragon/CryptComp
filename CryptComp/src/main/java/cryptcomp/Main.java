/**
 * Driver package for created cryptography and compression classes.
 */
package cryptcomp;

import crypto.DES;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main class for simple CLI.
 */
public class Main { 

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
            }
            key = cmdParser.getOption(CmdLineParser.CCOptions.KEY.getOption());
            if (key == null) {
                System.out.println("Key is missing");
                cmdParser.showUsage();
            }
        }

        String inFile = null;
        String outFile = null;
        if (cmdParser.hasOption(CmdLineParser.CCOptions.INFILE.getOption())) {
            inFile = cmdParser.getOption(CmdLineParser.CCOptions.INFILE.getOption());

            if (inFile == null) {
                System.out.println("Input file name is missing");
                cmdParser.showUsage();
            }
        }

        if (cmdParser.hasOption(CmdLineParser.CCOptions.OUTFILE.getOption())) {
            outFile = cmdParser.getOption(CmdLineParser.CCOptions.OUTFILE.getOption());

            if (outFile == null) {
                System.out.println("Output file name is missing");
                cmdParser.showUsage();
            }
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        if (inFile == null) {
            bis = new BufferedInputStream(System.in);
        } else {
            try {
                bis = new BufferedInputStream(new FileInputStream(inFile));
            } catch (FileNotFoundException fnfe) {
                System.out.println("There is an issue with input. Check following: " + fnfe.getMessage());
                cmdParser.showUsage();
            }
        }

        if (outFile == null) {
            bos = new BufferedOutputStream(System.out);
        } else {
            try {
                bos = new BufferedOutputStream(new FileOutputStream(outFile, false));
            } catch (FileNotFoundException fnfe) {
                System.out.println("There is an issue with output. Check following: " + fnfe.getMessage());
                cmdParser.showUsage();
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                }
            }
        }

        if (cmdParser.hasOption(CmdLineParser.CCOptions.ENC.getOption())
                && cmdParser.getOption(CmdLineParser.CCOptions.ENC.getOption()).equalsIgnoreCase(CmdLineParser.CCCryptoAlgo.DES.getAlgo())) {
            encryptDES(key, bis, bos);
        } else if (cmdParser.hasOption(CmdLineParser.CCOptions.DEC.getOption())
                && cmdParser.getOption(CmdLineParser.CCOptions.DEC.getOption()).equalsIgnoreCase(CmdLineParser.CCCryptoAlgo.DES.getAlgo())) {
            decryptDES(key, bis, bos);
        }

        if (bis != null) {
            try {
                bis.close();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    /**
     * Encrypt input stream to output stream using given key.
     * 
     * @param key for encryption
     * @param bis input stream being encrypted
     * @param bos output stream where encrypted stream is written
     */
    private static void encryptDES(String key, BufferedInputStream bis, BufferedOutputStream bos) {
        CryptoParams cp = new CryptoParams();
        int blockSize = 8;
        cp.setBlockSize(blockSize);
        cp.setEndOfFile(false);
        DES des = new DES(key);

        do {
            cp.setTextBuffer(new byte[blockSize]);
            readBlock(cp, bis, des);
            byte[] cipherText = new byte[blockSize];
            des.encryptBlock(cp.getTextBuffer(), 0, cipherText, 0, DES.OperationMode.ECB);
            writeBlock(cipherText, bos);
        } while (cp.isEndOfFile() == false);
    }

    /**
     * Decrypt input stream to output stream using given key.
     * 
     * @param key for decryption
     * @param bis input stream being decrypted
     * @param bos output stream where decrypted stream is written
     */
    private static void decryptDES(String key, BufferedInputStream bis, BufferedOutputStream bos) {
        CryptoParams cp = new CryptoParams();
        int blockSize = 8;
        cp.setBlockSize(blockSize);
        cp.setEndOfFile(false);
        DES des = new DES(key);

        do {
            cp.setTextBuffer(new byte[blockSize]);
            readBlock(cp, bis, des);
            if (cp.isEndOfFile() == false || (cp.isEndOfFile() == true && cp.getReadCount() > 0)) {
                byte[] clearText = new byte[blockSize];
                des.decryptBlock(cp.getTextBuffer(), 0, clearText, 0, DES.OperationMode.ECB);
                writeBlock(des.removePadding(clearText), bos);
            }
        } while (cp.isEndOfFile() == false);
    }

    private static void readBlock(CryptoParams cp, BufferedInputStream bis, DES des) {
        boolean EOF = false;
        int count = 0;
        int input = -1;
        byte[] buffer = cp.getTextBuffer();

        do {
            try {
                input = bis.read();
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

    private static void writeBlock(byte[] buffer, BufferedOutputStream bos) {
        try {
            bos.write(buffer, 0, buffer.length);
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }
}
