package srcs.securite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PasswordStore {
    private final String nomAlgoHash;
    static Map<String, String> passwordStore = new HashMap<>();


    public PasswordStore(String nomAlgoHash) {
        this.nomAlgoHash = nomAlgoHash;
    }

    public void storePassword(String user, String passwd) throws NoSuchAlgorithmException {
        byte[] password = toHash(passwd);
        String hashedPassword = hashToHex(password);
        passwordStore.put(user, hashedPassword);

    }

    public boolean checkPassword(String user, String passwd) throws NoSuchAlgorithmException {
        byte[] password = toHash(passwd);
        String hashedPassword = hashToHex(password);
        return hashedPassword.equals(passwordStore.get(user));
    }

    public boolean checkPassword2(String user, String passwd) throws NoSuchAlgorithmException, AuthenticationFailedException {


        String password = passwordStore.get(user);

        if (password == null){
            throw new AuthenticationFailedException("No password was entered.");
        }

        return password.equals(passwd);
    }

    byte[] toHash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(nomAlgoHash);
        byte[] hash = messageDigest.digest(password.getBytes());
        return hash;
    }

    String hashToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static Map<String, String> getPasswordStore() {
        return passwordStore;
    }
}
