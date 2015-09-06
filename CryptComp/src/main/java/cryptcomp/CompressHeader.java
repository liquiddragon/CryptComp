package cryptcomp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Compressed file header.
 */
public class CompressHeader {

    /**
     * Mark for binary packed file.
     */
    private final int BPACKED = 55;
    /**
     * Mark for file without binary packing.
     */
    private final int BUNPACKED = 00;
    /**
     * Byte indicating whether binary packing has been done or not.
     */
    private byte bitPacked;
    /**
     * Number of packed elements.
     */
    private int packedCount;
    /**
     * Highest value used in dictionary.
     */
    private int highestValue;

    /**
     * Construct compress header object.
     *
     * @param highest
     * @param bitPacking
     * @param packCount
     */
    public CompressHeader(int highest, boolean bitPacking, int packCount) {
        if (bitPacking) {
            bitPacked = BPACKED;
        } else {
            bitPacked = BUNPACKED;
        }
        packedCount = packCount;

        highestValue = highest;
    }

    /**
     * Write compress header into given stream.
     *
     * @param dos stream where header is written
     */
    public void writeHeader(DataOutputStream dos) {
        try {
            dos.writeByte(bitPacked);
            dos.writeInt(packedCount);
            dos.writeInt(highestValue);
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    /**
     * Read compress header from given stream.
     *
     * @param dis stream where header is read
     * @return true if read was successful, otherwise false
     */
    public boolean readHeader(DataInputStream dis) {
        boolean result = true;

        try {
            bitPacked = dis.readByte();
            packedCount = dis.readInt();
            highestValue = dis.readInt();
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
            result = false;
        }

        return result;
    }

    /**
     * Indicates whether stream was bit packed or not.
     *
     * @return true if stream was bit packed, otherwise false
     */
    public boolean isBitPacked() {
        boolean result = false;

        if (bitPacked == BPACKED) {
            result = true;
        }

        return result;
    }

    /**
     * Retrieve count of packed entries.
     *
     * @return packed count
     */
    public int getPackedCount() {
        return packedCount;
    }

    /**
     * Retrieve highest used value in the stream.
     *
     * @return highest used valued
     */
    public int getHighestValue() {
        return highestValue;
    }
}
