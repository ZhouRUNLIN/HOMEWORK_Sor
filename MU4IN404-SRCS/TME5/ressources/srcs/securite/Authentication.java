package srcs.securite;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;


public class Authentication {
    private Certif localCertif;
    private Certif remoteCertif;
    private KeyPair keyPair;
    private Channel channel;
    private PasswordStore passwordStore;
    private PublicKey publicKey;
    private String login;
    private String password;


    //server
    public Authentication(Channel channel, Certif certif, KeyPair keyPair, PasswordStore passwordStore, PublicKey publicKey) throws IOException, GeneralSecurityException {
        this.channel = channel;
        this.localCertif = certif;
        this.keyPair = keyPair;
        this.passwordStore = passwordStore;
        this.publicKey = publicKey;

        authenticateServer();

    }

    //client
    public Authentication(Channel channel, Certif certif, KeyPair keyPair, String login, String password, PublicKey publicKey) throws IOException, GeneralSecurityException {
        this.channel = channel;
        this.localCertif = certif;
        this.keyPair = keyPair;
        this.login = login;
        this.password = password;
        this.publicKey = publicKey;

        authenticateClient();

    }

    private void authenticateServer() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {


        Cipher cipher = Cipher.getInstance("RSA");
        byte[] sendCertHash = certifToHash(localCertif.toByteArray());


        channel.send(localCertif.toByteArray());
        channel.send(sendCertHash);


        byte[] certBytes = channel.recv();
        byte[] recvCertHash = channel.recv();

        byte[] calCertHash = certifToHash(certBytes);
        if (!compareHashes(recvCertHash, calCertHash)) {
            throw new CertificateCorruptedException();
        }


        this.remoteCertif = toCertif(certBytes);
        if(!remoteCertif.verify(publicKey))
            throw new CertificateCorruptedException();


        Random rand = new Random();
        int nonce = rand.nextInt(100000);
        channel.send(toByteArray(nonce));


        cipher.init(Cipher.DECRYPT_MODE, remoteCertif.getPublicKey());
        byte[] nonceDecrypt = cipher.doFinal(channel.recv());
        if(toInt(nonceDecrypt) != nonce)
            throw new AuthenticationFailedException();



        cipher.init(Cipher.ENCRYPT_MODE, this.keyPair.getPrivate());
        byte[] res = cipher.doFinal(channel.recv());
        channel.send(res);


//        String login = toString(channel.recv());
//
//        byte[] pass = channel.recv();
//        cipher.init(Cipher.DECRYPT_MODE, this.keyPair.getPrivate());
//        byte[] passDecrypt = cipher.doFinal(pass);
//        String passwd = toString(passDecrypt);
//        if(!passwordStore.checkPassword(login,passwd))
//            throw new AuthenticationFailedException();

        byte[] recvLogin = channel.recv();
        byte[] recvEncryptedPassword = channel.recv();


        String login = new String(recvLogin);

        String password = passwordStore.hashToHex(recvEncryptedPassword);


        if (!passwordStore.checkPassword2(login,password)) {
            throw new AuthenticationFailedException("Password verification failed.");
        }


    }

    private void authenticateClient() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");

        byte[] sendCertHash = certifToHash(localCertif.toByteArray());

        channel.send(localCertif.toByteArray());
        channel.send(sendCertHash);


        byte[] certBytes = channel.recv();
        byte[] recvCertHash = channel.recv();

        byte[] calCertHash = certifToHash(certBytes);
        if (!compareHashes(recvCertHash, calCertHash)) {
            throw new CertificateCorruptedException();
        }


        this.remoteCertif = toCertif(certBytes);
        if(!remoteCertif.verify(publicKey))
            throw new CertificateCorruptedException();

        cipher.init(Cipher.ENCRYPT_MODE, this.keyPair.getPrivate());
        byte[] res = cipher.doFinal(channel.recv());
        channel.send(res);



        Random rand = new Random();
        int nonce = rand.nextInt(10000);
        channel.send(toByteArray(nonce));

        cipher.init(Cipher.DECRYPT_MODE, remoteCertif.getPublicKey());
        byte[] nonceDecrypt = cipher.doFinal(channel.recv());
        if(toInt(nonceDecrypt) != nonce)
            throw new AuthenticationFailedException();


        PasswordStore passwordStore = new PasswordStore("SHA");


        byte[] sendLogin = login.getBytes();
        byte[] sendEncryptedPassword = passwordStore.toHash(this.password);


        channel.send(sendLogin);
        channel.send(sendEncryptedPassword);
    }

    public Certif getLocalCertif(){
        return localCertif;
    }

    public Certif getRemoteCertif(){
        return remoteCertif;
    }

    public KeyPair getLocalKeys(){
        return keyPair;
    }

    private Certif toCertif (byte[] bytes) {
        Object obj = null;
        try (ObjectInputStream ois = new ObjectInputStream (new ByteArrayInputStream (bytes));){
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return (Certif)obj;
    }

    private byte[] toByteArray (String s) {
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos);){
            oos.writeUTF(s);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    private String toString (byte[] bytes) {
        Object obj;
        try (ObjectInputStream ois = new ObjectInputStream (new ByteArrayInputStream (bytes));){
            obj = ois.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) obj;
    }

    private byte[] toByteArray (int i) {
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos);){
            oos.writeInt(i);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    private int toInt(byte[] bytes) {
        Object obj;
        try (ByteArrayInputStream bis = new ByteArrayInputStream (bytes); ObjectInputStream ois = new ObjectInputStream (bis);){
            obj = ois.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (Integer) obj;
    }

    private byte[] certifToHash(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    private boolean compareHashes(byte[] hash1, byte[] hash2) {
        return Arrays.equals(hash1, hash2);
    }


}
