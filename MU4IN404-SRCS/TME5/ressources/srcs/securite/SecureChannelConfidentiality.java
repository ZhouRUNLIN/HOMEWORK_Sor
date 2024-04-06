package srcs.securite;

import javax.crypto.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SecureChannelConfidentiality extends ChannelDecorator {
    private static SecretKey secretKey = null;
    private String nomAlgo;
    private SecretKey newSecretKey;

    public SecureChannelConfidentiality(Channel channel, Authentication authentication, String nomAlgo, int tailleCle) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException, ClassNotFoundException, BadPaddingException {
        super(channel);
        this.nomAlgo = nomAlgo;

        KeyGenerator keyGen = KeyGenerator.getInstance(nomAlgo);
        keyGen.init(tailleCle);
        newSecretKey = keyGen.generateKey();

        Cipher cipher = Cipher.getInstance(authentication.getLocalKeys().getPublic().getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, authentication.getRemoteCertif().getPublicKey());
        SealedObject sealedObject = new SealedObject(newSecretKey, cipher);
        channel.send(serializeSealedObject(sealedObject));

        sealedObject = deserializeSealedObject(channel.recv(), authentication.getLocalKeys().getPrivate(), cipher);
        this.newSecretKey = (SecretKey) sealedObject.getObject(cipher);
        setSercreKey();
    }

    public synchronized void setSercreKey(){
        if (secretKey == null) {
            this.secretKey = newSecretKey;
        }
    }

    public SecretKey getSecretKey(){
        return secretKey;
    }

    @Override
    public void send(byte[] data) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(nomAlgo);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            super.send(cipher.doFinal(data));
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
    }

    @Override
    public byte[] recv() throws IOException{
        byte[] encryptedData = super.recv();
        try {
            Cipher cipher = Cipher.getInstance(nomAlgo);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedData);
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
    }

    private static byte[] serializeSealedObject(SealedObject sealedObject) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(sealedObject);
        return byteArrayOutputStream.toByteArray();
    }

    private static SealedObject deserializeSealedObject(byte[] sealedObjectBytes, Key key, Cipher cipher) throws IOException, ClassNotFoundException, InvalidKeyException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(sealedObjectBytes));
        cipher.init(Cipher.DECRYPT_MODE, key);
        SealedObject sealedObject = (SealedObject) objectInputStream.readObject();
        return sealedObject;
    }
}