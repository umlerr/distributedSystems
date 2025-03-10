package umlerr.serviceauth.util;

import java.security.SecureRandom;
import java.util.Base64;

public class NonceGen {
    private static final SecureRandom random = new SecureRandom();
    public static String generateNonce() {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
