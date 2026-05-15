package com.darkross.wssecuritycore.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class PasswordUtils {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    private static final int PASSWORD_LENGTH = 12;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Encoder BCrypt
     */
    private static final PasswordEncoder PASSWORD_ENCODER =
            new BCryptPasswordEncoder();

    /**
     * Genera una contraseña aleatoria de longitud fija
     */
    public static String generateRandomPassword() {

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(
                    CHARACTERS.charAt(
                            RANDOM.nextInt(CHARACTERS.length())
                    )
            );
        }

        return password.toString();
    }

    /**
     * Hashea una contraseña usando BCrypt
     */
    public static String hashPassword(String password) {

        return PASSWORD_ENCODER.encode(password);
    }

    /**
     * Genera una contraseña aleatoria y la hashea
     */
    public static PasswordResult generateAndHashPassword() {

        String rawPassword = generateRandomPassword();

        String hashedPassword = hashPassword(rawPassword);

        return new PasswordResult(rawPassword, hashedPassword);
    }

    /**
     * Verifica si una contraseña coincide con el hash
     */
    public static boolean matchesPassword(
            String rawPassword,
            String hashedPassword
    ) {

        return PASSWORD_ENCODER.matches(
                rawPassword,
                hashedPassword
        );
    }

    /**
     * Clase para devolver tanto la contraseña original
     * como la hasheada
     */
    public static class PasswordResult {

        private final String rawPassword;

        private final String hashedPassword;

        public PasswordResult(
                String rawPassword,
                String hashedPassword
        ) {

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