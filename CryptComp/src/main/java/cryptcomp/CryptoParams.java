package cryptcomp;

/**
 * Command line usage parameters for en/decrypt use. Default getter and setter
 * methods for values.
 */
public class CryptoParams {

    /**
     * Whether end of file has been reached.
     */
    private boolean endOfFile;
    /**
     * Buffer holding 'text' being handled.
     */
    private byte[] textBuffer;
    /**
     * Block size for cryptography.
     */
    private int blockSize;
    /**
     * Number of bytes being handled.
     */
    private int readCount;

    public boolean isEndOfFile() {
        return endOfFile;
    }

    public void setEndOfFile(boolean endOfFile) {
        this.endOfFile = endOfFile;
    }

    public byte[] getTextBuffer() {
        return textBuffer;
    }

    public void setTextBuffer(byte[] textBuffer) {
        this.textBuffer = textBuffer;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
