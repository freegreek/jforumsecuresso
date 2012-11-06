package uk.co.smartkey.jforumsecuresso;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * SecurityTools
 */
public class SecurityTools {

    private static SecurityTools INSTANCE = new SecurityTools();

    /**
     * The chars that will be used in the textual representation of the encoded bytes
     */
    private final String DIGITS = "0123456789abcdef";

    public static final String FORUM_COOKIE_NAME = "jforum-secure-sso";

    /**
     * Cipher for encrypting
     */
    private Cipher cipher;

    /**
     * Key for encrypting
     */
    private SecretKeySpec skeySpec;

    public SecurityTools() {
        try {
            //prepare an AES cipher
            cipher = Cipher.getInstance("AES");

            //load the users password from the config file
            Properties config = new Properties();
            config.load(getClass().getResourceAsStream("/META-INF/jforumsecuresso.properties"));
            String securityPassword = config.getProperty("security.password");

            //if the user has not set the password length correctly, just pad it out to 16 chars
            while (securityPassword.length() < 16) {
                securityPassword += "-";
            }

            //prepare a key for use with the cipher
            byte[] keyBytes = securityPassword.getBytes();
            assert keyBytes.length >= 16;
            skeySpec = new SecretKeySpec(keyBytes, 0, 16, "AES");

        } catch (Exception e) {
            //TODO: log this
            e.printStackTrace();
        }
    }

    public static SecurityTools getInstance() {
        return INSTANCE;
    }

    public synchronized String encryptString(String stringToEncrypt) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            //encryptString the string
            byte[] encrypted = cipher.doFinal(stringToEncrypt.getBytes());
            String encryptedString = bytesToHex(encrypted);
            return encryptedString;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String decryptString(String stringToDecrypt) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            //decryptString the string
            byte[] decrypted = cipher.doFinal(hexToBytes(stringToDecrypt));
            String decryptedString = new String(decrypted);
            return decryptedString;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encryptCookieValues(String email, String screenName) {
        String cookieString = String.format("%1s:::%2s", email, screenName);
        String encryptedCookieString = encryptString(cookieString);
        return encryptedCookieString;
    }

    public String[] decryptCookieValues(String encryptedCookie) {
        try {
            String[] emailAndScreenName = new String[2];
            String decryptedCookie = decryptString(encryptedCookie);
            StringTokenizer tokenizer = new StringTokenizer(decryptedCookie, ":::");
            emailAndScreenName[0] = tokenizer.nextToken();
            emailAndScreenName[1] = tokenizer.nextToken();
            return emailAndScreenName;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Used to convert the encrypted bytes into chars to send over the web
     */
    private String bytesToHex(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            int v = b & 0xff;
            builder.append(DIGITS.charAt(v >> 4));
            builder.append(DIGITS.charAt(v & 0xf));
        }
        return builder.toString();
    }

    /**
     * Used to convert an encoded string into a byte array for de-encryping
     */
    private byte[] hexToBytes(String string) {
        byte[] data = new byte[string.length() / 2];
        for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
            char charA = string.charAt(dataIndex * 2);
            char charB = string.charAt(dataIndex * 2 + 1);
            int i = (byte) 0xff;
            i = i & ((byte) DIGITS.indexOf(charA));
            i = i << 4;
            i = i | ((byte) DIGITS.indexOf(charB));
            data[dataIndex] = (byte) i;
        }
        return data;
    }

}
