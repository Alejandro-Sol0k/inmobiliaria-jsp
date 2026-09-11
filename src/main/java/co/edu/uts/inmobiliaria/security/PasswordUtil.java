package co.edu.uts.inmobiliaria.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 24;

    private PasswordUtil() {
    }

    public static String newSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String password, String salt) throws GeneralSecurityException {
        PBEKeySpec specification = new PBEKeySpec(password.toCharArray(),
                salt.getBytes(StandardCharsets.UTF_8), ITERATIONS, KEY_LENGTH);
        byte[] derivedKey;
        try {
            derivedKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(specification).getEncoded();
        } finally {
            specification.clearPassword();
        }
        return Base64.getEncoder().encodeToString(derivedKey);
    }

    public static boolean matches(String password, String salt, String expectedHash)
            throws GeneralSecurityException {
        byte[] actual = hash(password, salt).getBytes(StandardCharsets.UTF_8);
        byte[] expected = expectedHash.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(actual, expected);
    }
}
