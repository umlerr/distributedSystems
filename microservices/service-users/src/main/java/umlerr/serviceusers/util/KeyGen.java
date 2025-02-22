//package umlerr.serviceuser.util;
//
//import java.security.SecureRandom;
//import java.util.Base64;
//import lombok.experimental.UtilityClass;
//
//@UtilityClass
//public class KeyGen {
//    public static void main(String[] args) {
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] key = new byte[64];  // 64 байта = 512 бит
//        secureRandom.nextBytes(key);
//        String secretKey = Base64.getEncoder().encodeToString(key);
//        System.out.println("Generated Secret Key: " + secretKey);
//    }
//}
