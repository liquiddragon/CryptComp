package crypto;

import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;

public class DESTest {

    private DES des;

    public DESTest() {
    }

    /**
     * DES string constructor test.
     */
    @Test
    public void testDESStringConstructor() {
        String key = "The key";
        des = new DES(key);

        Field encryptKeysField = obtainDESClassPrivateField("encryptKeys");
        Field decryptKeysField = obtainDESClassPrivateField("decryptKeys");

        try {
            encryptKeysField.setAccessible(true);
            decryptKeysField.setAccessible(true);

            int[] eKeys = (int[]) encryptKeysField.get(des);
            int[] dKeys = (int[]) decryptKeysField.get(des);

            int count = 0;
            for (int i = 0; i < eKeys.length; i++) {
                if (eKeys[i] > 0) {
                    count++;
                }
            }
            assertEquals(32, count);

            count = 0;
            for (int i = 0; i < dKeys.length; i++) {
                if (dKeys[i] > 0) {
                    count++;
                }
            }
            assertEquals(32, count);
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument: " + e.toString());
        } catch (SecurityException | IllegalAccessException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * DES byte[] constructor test.
     */
    @Test
    public void testDESByteArrayConstructor() {
        String key = "Password";
        des = new DES(key.getBytes());

        Field encryptKeysField = obtainDESClassPrivateField("encryptKeys");
        Field decryptKeysField = obtainDESClassPrivateField("decryptKeys");

        try {
            encryptKeysField.setAccessible(true);
            decryptKeysField.setAccessible(true);

            int[] eKeys = (int[]) encryptKeysField.get(des);
            int[] dKeys = (int[]) decryptKeysField.get(des);

            int count = 0;
            for (int i = 0; i < eKeys.length; i++) {
                if (eKeys[i] > 0) {
                    count++;
                }
            }
            assertEquals(32, count);

            count = 0;
            for (int i = 0; i < dKeys.length; i++) {
                if (dKeys[i] > 0) {
                    count++;
                }
            }
            assertEquals(32, count);
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument: " + e.toString());
        } catch (SecurityException | IllegalAccessException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * DES encrypt and decrypt with short message.
     */
    @Test
    public void testDESEncDecShortMessage() {
        String key = "TheLongerKey";
        des = new DES(key);

        byte[] clearText = "Test".getBytes();
        byte[] cipherText = des.encrypt(clearText);
        byte[] clearedText = des.decrypt(cipherText);

        assertArrayEquals(clearText, clearedText);
    }

    /**
     * DES encrypt and decrypt with long message.
     */
    @Test
    public void testDESEncDecLongMessage() {
        String key = "TheDoubleLongKey";
        des = new DES(key);

        byte[] clearText = "This is a longer test message.:)".getBytes();
        byte[] cipherText = des.encrypt(clearText);
        byte[] clearedText = des.decrypt(cipherText);

        assertArrayEquals(clearText, clearedText);
    }

    /**
     * DES decrypt without message.
     */
    @Test
    public void testDESDecNoMsg() {
        String key = "The key";
        des = new DES(key);

        byte[] clearText = des.decrypt("Dummy".getBytes());
        assertNull(clearText);
    }

    /**
     * Helper method to obtaining access to private field in DES class for
     * testing purposes.
     *
     * @param fieldName Name of field for which access is wanted
     * @return Reference to DES class private field, if successful, or null, if
     * unsuccessful
     */
    private Field obtainDESClassPrivateField(String fieldName) {
        Class desClass = DES.class;
        Field field = null;

        try {
            field = desClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            System.out.println("Missing field: " + e.toString());
        } catch (SecurityException e) {
            System.out.println(e.toString());
        }
        return field;
    }
}
