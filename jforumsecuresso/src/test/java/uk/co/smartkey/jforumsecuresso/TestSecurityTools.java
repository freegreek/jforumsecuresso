package uk.co.smartkey.jforumsecuresso;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TestSecurityTools
 */
public class TestSecurityTools {

    @Test
    public void testEncryption() {
        String plainText = "This is just an example";

        String encrypted = SecurityTools.getInstance().encryptString(plainText);
        String decrypted = SecurityTools.getInstance().decryptString(encrypted);

        assertEquals("the decrypted string does not match the origial value", plainText, decrypted);
    }

    @Test
    public void testCookieEncoding() {
        String email = "fred@fred.com";
        String screenName = "fred jones";

        String encryptedCookie = SecurityTools.getInstance().encryptCookieValues(email, screenName);
        String[] emailAndScreenName = SecurityTools.getInstance().decryptCookieValues(encryptedCookie);

        assertEquals(email, emailAndScreenName[0]);
        assertEquals(screenName, emailAndScreenName[1]);
    }

    @Test
    public void testCookieIsURLEncoded() {
        String email = "fred@fred.com";
        String screenName = "fred jones";

        String cookieValue = SecurityTools.getInstance().encryptCookieValues(email, screenName);

        assertTrue("cookie must be URL encoded", cookieValue.indexOf(' ') == -1);
    }

    @Test
    public void testIllegalCookieFormat() {
        String[] emailAndScreenName = SecurityTools.getInstance().decryptCookieValues("");
        assertNull(emailAndScreenName);
    }
}
