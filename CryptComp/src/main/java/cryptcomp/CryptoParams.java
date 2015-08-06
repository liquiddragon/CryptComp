package cryptcomp;

/**
 * Command line usage parameters for en/decrypt use.
 */
public class CryptoParams {

    private boolean endOfFile;
    private byte[] textBuffer;
    private int blockSize;
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
