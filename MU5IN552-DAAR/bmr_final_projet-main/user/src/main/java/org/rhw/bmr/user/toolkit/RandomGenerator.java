package org.rhw.bmr.user.toolkit;


import java.security.SecureRandom;

/**
 * Group ID is generated randomly
 */
public final class RandomGenerator {
    private final static String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }


    public static String generateRandomString() {
        return generateRandomString(6);
    }

}
