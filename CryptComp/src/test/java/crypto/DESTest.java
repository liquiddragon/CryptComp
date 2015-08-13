package crypto;

import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DES class related tests.
 */
public class DESTest {

    private DES des;

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
     * DES encrypt and decrypt with short message using CBC.
     */
    @Test
    public void testDESCBCEncDecShortMessage() {
        String key = "TheLongerKey";
        des = new DES(key);

        byte[] clearText = "Test".getBytes();
        byte[] cipherText = des.encrypt(clearText);
        byte[] clearedText = des.decrypt(cipherText);

        assertArrayEquals(clearText, clearedText);
    }

    /**
     * DES encrypt and decrypt with long message using CBC.
     */
    @Test
    public void testDESCBCEncDecLongMessage() {
        String key = "TheDoubleLongKey";
        des = new DES(key);

        byte[] clearText = "This is a longer test message.:)".getBytes();
        byte[] cipherText = des.encrypt(clearText);
        byte[] clearedText = des.decrypt(cipherText);
        assertArrayEquals(clearText, clearedText);
    }

    /**
     * DES encrypt and decrypt with message using ECB.
     */
    @Test
    public void testDESECBEncDecLongMessage() {
        String key = "SurpriseKey!";
        des = new DES(key);
        des.setOperationMode(DES.OperationMode.ECB);

        byte[] clearText = "This is a repeating test message. This is a repeating test message".getBytes();
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
     * Test custom implementation against Java provided.
     */
    @Test
    public void testAgainstJavaDES() {
        String password = "password";
        String input = "This is a secret message.";
        byte[] passwordInBytes = password.getBytes();
        byte[] inputInBytes = input.getBytes();
        byte[] ivBytes = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        // Create encrypted and decrypted text using custom implementation of DES
        des = new DES(passwordInBytes);
        byte[] cipherText = des.encrypt(inputInBytes);
        byte[] clearText = des.decrypt(cipherText);

        // Create same with Java provided utilities
        SecretKeySpec key = new SecretKeySpec(passwordInBytes, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encr = new byte[32];
            int ctLen = cipher.update(inputInBytes, 0, inputInBytes.length, encr, 0);
            ctLen += cipher.doFinal(encr, ctLen);

            // Verify encrypted text
            assertArrayEquals(encr, cipherText);

            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decrypt = new byte[25];
            int ptLen = cipher.update(encr, 0, ctLen, decrypt, 0);
            ptLen += cipher.doFinal(decrypt, ptLen);

            // Verify decrypted text
            assertArrayEquals(decrypt, clearText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
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
