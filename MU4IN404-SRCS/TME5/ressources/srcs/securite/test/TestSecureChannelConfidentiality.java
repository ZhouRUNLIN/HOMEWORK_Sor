package srcs.securite.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static srcs.securite.test.ChannelTestUtil.ALGOKEY_S;
import static srcs.securite.test.ChannelTestUtil.SIZEKEY_S;
import static srcs.securite.test.ChannelTestUtil.included;
import static srcs.securite.test.ChannelTestUtil.testAppPropStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.Arrays;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import org.junit.Test;

import srcs.securite.Authentication;
import srcs.securite.Channel;
import srcs.securite.ChannelBasic;
import srcs.securite.SecureChannelConfidentiality;
import srcs.securite.test.ChannelTestUtil.ChannelSniffer;
import srcs.securite.test.ChannelTestUtil.NoException;
import srcs.securite.test.ChannelTestUtil.RetourTest;

public class TestSecureChannelConfidentiality  {

	private ChannelSniffer pirat=null;
	private SecureChannelConfidentiality secure=null;
	private ChannelSniffer witness=null;
	private Authentication authserveur=null;
	private Authentication authclient=null;
	
	@Test
	public void test() throws Exception {
		
		AuthenticationFactory authfact=new AuthenticationFactory();
		
		RetourTest ret = testAppPropStore( (s)-> {
			try {
				Channel base = new ChannelBasic(s);
				authserveur =authfact.newServeurAuthentication(base);
				return new SecureChannelConfidentiality(base, authserveur, ALGOKEY_S, SIZEKEY_S);
			} catch (IOException | ClassNotFoundException | GeneralSecurityException e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
		},(s)-> {
			try {
				Channel base = new ChannelBasic(s);
				pirat = new ChannelSniffer(base);
				authclient = authfact.newClientAuthentication(base);
				secure = new SecureChannelConfidentiality(pirat,authclient, ALGOKEY_S, SIZEKEY_S);
				witness = new ChannelSniffer(secure);
				return witness;
			} catch (IOException | ClassNotFoundException | GeneralSecurityException e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
		});
		
		assertEquals(NoException.class,ret.getExceptionClient().getClass());
		assertEquals(NoException.class,ret.getExceptionServeur().getClass());
		
		assertNotNull(pirat);
		assertNotNull(witness);
		assertNotNull(secure);
		
		assertEquals(witness.getSent().size()+1, pirat.getSent().size());// pirat a enregistre aussi le message d'envoi de la cle secrete
		assertEquals(witness.getReceived().size()+1, pirat.getReceived().size());// pirat a enregistre aussi le message de reception de la cle secrete
		
		
		try(ObjectInputStream oissent = new ObjectInputStream(new ByteArrayInputStream(pirat.getSent().get(0)))){
			Object o_sent =  oissent.readObject();
			assertEquals(SealedObject.class,o_sent.getClass());
			
			assertThrows(InvalidKeyException.class,()->((SealedObject) o_sent).getObject(authclient.getLocalKeys().getPrivate()));
			
			SecretKey key_sent = (SecretKey) ((SealedObject) o_sent).getObject(authserveur.getLocalKeys().getPrivate());
			
			try(ObjectInputStream oisrcv = new ObjectInputStream(new ByteArrayInputStream(pirat.getReceived().get(0)))){
				Object o_recv =  oisrcv.readObject();
				assertEquals(SealedObject.class,o_recv.getClass());
				assertThrows(InvalidKeyException.class,()->((SealedObject) o_recv).getObject(authserveur.getLocalKeys().getPrivate()));
				SecretKey key_receive = (SecretKey) ((SealedObject) o_recv).getObject(authclient.getLocalKeys().getPrivate());
				
				SecretKey effective_key = secure.getSecretKey();
				assertTrue(Arrays.equals(effective_key.getEncoded(), key_receive.getEncoded())
						 || Arrays.equals(effective_key.getEncoded(), key_sent.getEncoded()));
				
				
				assertFalse(included(pirat.getSent().get(0), key_receive.getEncoded()));
				assertFalse(included(pirat.getSent().get(0), key_sent.getEncoded()));
				assertFalse(included(pirat.getReceived().get(0), key_receive.getEncoded()));
				assertFalse(included(pirat.getReceived().get(0), key_sent.getEncoded()));
				
			}
		}
		
		
		
		for(int i=0;i<witness.getSent().size();i++) {
			assertFalse(Arrays.equals(witness.getSent().get(i), pirat.getSent().get(i+1)));
		}
		for(int i=0;i<witness.getReceived().size();i++) {
			assertFalse(Arrays.equals(witness.getReceived().get(i), pirat.getReceived().get(i+1)));
		}
		
	}
}
