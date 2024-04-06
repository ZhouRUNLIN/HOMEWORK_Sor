package srcs.securite;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class CertificationAuthority {
    private final String signAlgo;
    private final KeyPair keyPair;
    private final Map<String, Certif> certifMap;

    public CertificationAuthority(String encryptAlgo, int keySize, String signAlgo) throws NoSuchAlgorithmException {
        this.signAlgo = signAlgo;
        keyPair = Util.generateNewKeyPair(encryptAlgo, keySize);
        certifMap = new HashMap<>();
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public Certif getCertificate(String identifier) {
        return certifMap.get(identifier);
    }

    public Certif declarePublicKey(String identifier, PublicKey pubk) throws GeneralSecurityException {
        if (certifMap.containsKey(identifier)) {
            throw new GeneralSecurityException("Already exists.");
        }

        Signature signature = Signature.getInstance(signAlgo);
        signature.initSign(keyPair.getPrivate());
        signature.update(pubk.getEncoded());
        certifMap.put(identifier, new Certif(identifier, pubk, signature.sign(), signAlgo));
        return getCertificate(identifier);
    }
}
