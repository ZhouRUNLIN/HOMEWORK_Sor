package srcs.securite;

import srcs.securite.Authentication;
import srcs.securite.Certif;

import java.security.*;
import java.util.Base64;

public class CertifTest {

    public static void main(String[] args) {
        try {
            // 1. 生成密钥对
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 2. 使用私钥生成签名
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);

            String data = "这是一个需要被签名的消息";
            signature.update(data.getBytes());
            byte[] digitalSignature = signature.sign();

            // 创建Certif对象，使用生成的签名
            String identifier = "TestIdentifier";
            String algoSign = "SHA256withRSA"; // 签名算法
            Certif certif = new Certif(identifier, publicKey, digitalSignature, algoSign);

            // 使用toBytes序列化Certif对象
            byte[] serializedCertif = Authentication.toBytes(certif);

            // 使用bytesTo反序列化Certif对象
            Certif deserializedCertif = Authentication.bytesTo(serializedCertif);

            // 验证反序列化得到的Certif对象的公钥
            boolean isVerified = deserializedCertif.verify(publicKey); // 这里需要您的verify方法能正确实现公钥验证逻辑

            System.out.println("Verification result: " + isVerified);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
