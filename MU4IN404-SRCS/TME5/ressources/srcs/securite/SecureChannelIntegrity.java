package srcs.securite;

import java.io.IOException;
import java.security.*;

public class SecureChannelIntegrity extends ChannelDecorator{

    private String nomAlgo;
    private Authentication authentication;




    public SecureChannelIntegrity(Channel channel, Authentication authentication, String nomAlgo ) {
        super(channel);
        this.authentication = authentication;
        this.nomAlgo = nomAlgo;
    }

    @Override
    public void send(byte[] bytesArray) throws IOException {

        PrivateKey privkey = authentication.getLocalKeys().getPrivate();
        Signature signature;
        byte[] res;
        try {
            signature = Signature.getInstance(nomAlgo);
            signature.initSign(privkey);
            signature.update(bytesArray);
            res = signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        super.send(bytesArray);
        super.send(res);


    }

    @Override
    public byte[] recv() throws IOException{

        byte[] bytesArray = super.recv();
        byte[] res = super.recv();

        PublicKey pubkey = authentication.getRemoteCertif().getPublicKey();

        try {
            Signature sig = Signature.getInstance(nomAlgo);
            sig.initVerify(pubkey);
            sig.update(bytesArray);

            boolean verified = sig.verify(res);
            if (!verified) {
                throw new CorruptedMessageException("Signature verification failed.");
            }
            return bytesArray;
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new CorruptedMessageException("error");
        }

    }

}
