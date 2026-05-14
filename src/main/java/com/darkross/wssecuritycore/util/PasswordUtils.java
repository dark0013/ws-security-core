package com.darkross.wssecuritycore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int PASSWORD_LENGTH = 12;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Genera una contraseña aleatoria de longitud fija
     */
    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    /**
     * Hashea una contraseña usando MD5
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear contraseña con MD5", e);
        }
    }

    /**
     * Genera una contraseña aleatoria y la hashea con MD5
     */
    public static PasswordResult generateAndHashPassword() {
        String rawPassword = generateRandomPassword();
        String hashedPassword = hashPassword(rawPassword);
        return new PasswordResult(rawPassword, hashedPassword);
    }

    /**
     * Clase para devolver tanto la contraseña original como la hasheada
     */
    public static class PasswordResult {
        private final String rawPassword;
        private final String hashedPassword;

        public PasswordResult(String rawPassword, String hashedPassword) {
            this.rawPassword = rawPassword;
            this.hashedPassword = hashedPassword;
        }

        public String getRawPassword() {
            return rawPassword;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }
    }
}
