package umlerr.serviceusers.util;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyGen {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Key: " + encodedKey);
    }
}
