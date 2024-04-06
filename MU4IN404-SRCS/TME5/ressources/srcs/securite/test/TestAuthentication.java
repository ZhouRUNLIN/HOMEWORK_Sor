package srcs.securite.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static srcs.securite.test.ChannelTestUtil.included;
import static srcs.securite.test.ChannelTestUtil.testAppPropStore;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import srcs.securite.Authentication;
import srcs.securite.AuthenticationFailedException;
import srcs.securite.Certif;
import srcs.securite.CertificateCorruptedException;
import srcs.securite.Channel;
import srcs.securite.ChannelBasic;
import srcs.securite.ChannelDecorator;
import srcs.securite.Util;
import srcs.securite.test.ChannelTestUtil.ChannelSniffer;
import srcs.securite.test.ChannelTestUtil.NoException;
import srcs.securite.test.ChannelTestUtil.RetourTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAuthentication {

	private Authentication authserveur=null;
	private Authentication authclient=null;

	private ChannelSniffer snifferclient;
	private ChannelSniffer snifferserveur;
	private AuthenticationFactory authfact;
	private RetourTest ret;

	@Before
	public void first() throws Exception {
		authfact = new AuthenticationFactory();
		ret = testAppPropStore(
				s->{
					Channel base = new ChannelBasic(s);
					snifferserveur = new ChannelSniffer(base);
					authserveur=authfact.newServeurAuthentication(snifferserveur);
					return base;
				},
				s->{
					Channel base = new ChannelBasic(s);
					snifferclient = new ChannelSniffer(base);
					authclient=authfact.newClientAuthentication(snifferclient);
					return base;
				}
				);
		assertNotNull(authserveur);
		assertNotNull(authclient);
		assertNotNull(snifferclient);
		assertNotNull(snifferserveur);
	}


	@Test
	public void test_a_AuthNormale() throws Exception{
		System.out.println("");
		System.out.println("****************************");
		System.out.println("Authentification normale : ");

		assertEquals(NoException.class,ret.getExceptionClient().getClass());
		assertEquals(NoException.class,ret.getExceptionServeur().getClass());	

		//controle du protocole d'authentification
		assertEquals(authfact.getKpclient(),authclient.getLocalKeys());
		assertEquals(authfact.getKpserveur(),authserveur.getLocalKeys());
		assertNotEquals(authfact.getKpserveur(),authclient.getLocalKeys());
		assertNotEquals(authfact.getKpclient(),authserveur.getLocalKeys());

		assertArrayEquals(authfact.getCertif_client().getAuthoritySignature() ,authclient.getLocalCertif().getAuthoritySignature());
		assertEquals(authfact.getCertif_client().getIdentifier() ,authclient.getLocalCertif().getIdentifier());
		assertEquals(authfact.getCertif_client().getPublicKey() ,authclient.getLocalCertif().getPublicKey());

		assertArrayEquals(authfact.getCertif_serveur().getAuthoritySignature() ,authserveur.getLocalCertif().getAuthoritySignature());
		assertEquals(authfact.getCertif_serveur().getIdentifier() ,authserveur.getLocalCertif().getIdentifier());
		assertEquals(authfact.getCertif_serveur().getPublicKey() ,authserveur.getLocalCertif().getPublicKey());

		assertArrayEquals(authfact.getCertif_client().getAuthoritySignature() ,authserveur.getRemoteCertif().getAuthoritySignature());
		assertEquals(authfact.getCertif_client().getIdentifier() ,authserveur.getRemoteCertif().getIdentifier());
		assertEquals(authfact.getCertif_client().getPublicKey() ,authserveur.getRemoteCertif().getPublicKey());

		assertArrayEquals(authfact.getCertif_serveur().getAuthoritySignature() ,authclient.getRemoteCertif().getAuthoritySignature());
		assertEquals(authfact.getCertif_serveur().getIdentifier() ,authclient.getRemoteCertif().getIdentifier());
		assertEquals(authfact.getCertif_serveur().getPublicKey() ,authclient.getRemoteCertif().getPublicKey());


		//on s'assure que le premier message du serveur vers le client contient les info du certif du serveur
		assertTrue(included(snifferserveur.getSent().get(0),authfact.getCertif_serveur().getAuthoritySignature()));
		assertTrue(included(snifferserveur.getSent().get(0),authfact.getCertif_serveur().getIdentifier().getBytes()));
		assertTrue(included(snifferserveur.getSent().get(0),authfact.getCertif_serveur().getPublicKey().getEncoded()));
		assertTrue(included(snifferclient.getReceived().get(0),authfact.getCertif_serveur().getAuthoritySignature()));
		assertTrue(included(snifferclient.getReceived().get(0),authfact.getCertif_serveur().getIdentifier().getBytes()));
		assertTrue(included(snifferclient.getReceived().get(0),authfact.getCertif_serveur().getPublicKey().getEncoded()));



		//on s'assure que le premier message du client vers le serveur contient les info du certif du client
		assertTrue(included(snifferclient.getSent().get(0),authfact.getCertif_client().getAuthoritySignature()));
		assertTrue(included(snifferclient.getSent().get(0),authfact.getCertif_client().getIdentifier().getBytes()));
		assertTrue(included(snifferclient.getSent().get(0),authfact.getCertif_client().getPublicKey().getEncoded()));
		assertTrue(included(snifferserveur.getReceived().get(0),authfact.getCertif_client().getAuthoritySignature()));
		assertTrue(included(snifferserveur.getReceived().get(0),authfact.getCertif_client().getIdentifier().getBytes()));
		assertTrue(included(snifferserveur.getReceived().get(0),authfact.getCertif_client().getPublicKey().getEncoded()));


		Cipher cipher = Cipher.getInstance(ChannelTestUtil.ALGOKEY_A);
		//on s'assure que le passwd du client ne passe pas en clair sur le réseau
		for(byte[] mess : snifferclient.getSent()) {
			assertFalse(included(mess,authfact.getPasswdclient().getBytes()));
			cipher.init(Cipher.DECRYPT_MODE,authfact.getKpserveur().getPublic());
			//on verifie que c'est aussi le cas en dechiffrant avec une cle publique
			try{
				assertFalse(included(cipher.doFinal(mess),authfact.getPasswdclient().getBytes()));			
			} catch(Exception e) {}
			cipher.init(Cipher.DECRYPT_MODE,authfact.getKpclient().getPublic());
			try{
				assertFalse(included(cipher.doFinal(mess),authfact.getPasswdclient().getBytes()));			
			} catch(Exception e) {}

		}
		for(byte[] mess : snifferserveur.getSent()) {
			assertFalse(included(mess,authfact.getPasswdclient().getBytes()));
			cipher.init(Cipher.DECRYPT_MODE,authfact.getKpserveur().getPublic());
			//on verifie que c'est aussi le cas en dechiffrant avec une cle publique
			try{
				assertFalse(included(cipher.doFinal(mess),authfact.getPasswdclient().getBytes()));			
			} catch(Exception e) {}
			cipher.init(Cipher.DECRYPT_MODE,authfact.getKpclient().getPublic());
			try{
				assertFalse(included(cipher.doFinal(mess),authfact.getPasswdclient().getBytes()));			
			} catch(Exception e) {}
		}
	}


	@Test
	public void test_b_attack_certif() throws Exception {
		//tentative d'attaque de corruption de certificat
		System.out.println("");
		System.out.println("****************************");
		System.out.println("Authentification avec attaque corruption certificat : ");
		ret = testAppPropStore(
				s->{
					Channel base = new ChannelBasic(s);
					authfact.newServeurAuthentication(base);
					return base;				},
				s->{
					Channel base = new ChannelBasic(s);
					Certif certifclient=authfact.getCertif_client();
					KeyPair kppirat = Util.generateNewKeyPair(ChannelTestUtil.ALGOKEY_A, ChannelTestUtil.SIZEKEY_A);
					List<Field> fields =  ChannelTestUtil.getFields(certifclient.getClass(), PublicKey.class);
					Field f_pubk = fields.get(0);
					f_pubk.setAccessible(true);
					try {
						f_pubk.set(certifclient, kppirat.getPublic());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						assertFalse(true);
					}
					f_pubk.setAccessible(false);

					new Authentication(base, certifclient, authfact.getKpclient(),authfact.getLoginclient(),
							authfact.getPasswdclient(), authfact.getCA().getPublicKey());
					return base;
				}
				);
		assertEquals(CertificateCorruptedException.class,ret.getExceptionServeur().getClass());
	}


	@Test
	public void test_c_attack_pwd() throws Exception{
		//tentative d'attaque par faux mot de passe sur le serveur

		System.out.println("");
		System.out.println("****************************");
		System.out.println("Authentification avec faux mot de passe : ");

		ret = testAppPropStore(
				s->{
					Channel base = new ChannelBasic(s);
					authfact.newServeurAuthentication(base);
					return base;				},
				s->{
					Channel base = new ChannelBasic(s);
					new Authentication(base, authfact.getCertif_client(), authfact.getKpclient(),authfact.getLoginclient(),
							"fauxpassword", authfact.getCA().getPublicKey());
					return base;
				}
				);

		assertEquals(AuthenticationFailedException.class,ret.getExceptionServeur().getClass());
	}


	@Test
	public void test_d_attackReplayToClient() throws Exception {
		//tentative d'attaque par rejeu vers le client (l'attaquant se fait passe pour le serveur)
		System.out.println("");
		System.out.println("****************************");
		System.out.println("Authentification avec rejeu vers le client : ");
		ret = testAppPropStore(
				s->{
					Channel base = new ChannelBasic(s);
					return new PiratReplay(base, snifferserveur.getSent(), Collections.emptyList());
				},
				s->{
					Channel base = new ChannelBasic(s);
					authfact.newClientAuthentication(base);
					return base;
				}
				);

		assertEquals(AuthenticationFailedException.class,ret.getExceptionClient().getClass());
	}


	@Test
	public void test_e_attackReplayToServer() throws Exception {

		//tentative d'attaque par rejeu vers le serveur (l'attaquant se fait passe pour le client)

		System.out.println("");
		System.out.println("****************************");
		System.out.println("Authentification avec rejeu vers le serveur : ");
		ret = testAppPropStore(
				s->{
					Channel base = new ChannelBasic(s);
					authfact.newServeurAuthentication(base);
					return base;
				},
				s->{
					Channel base = new ChannelBasic(s);
					Channel res = new PiratReplay(base, snifferclient.getSent(),snifferclient.getReceived());
					return res;
				}
				);

		assertEquals(AuthenticationFailedException.class,ret.getExceptionServeur().getClass());		
	}

	private static class PiratReplay extends ChannelDecorator{

		private List<byte[]> toreplay;
		private List<byte[]> expectedreceived;
		private boolean alreadyreplay=false;
		private boolean alreadyexpectedreceived=false;

		public PiratReplay(Channel decorated, List<byte[]> toreplay, List<byte[]> expectedreceived) throws IOException {
			super(decorated);
			this.toreplay=toreplay;
			this.expectedreceived=expectedreceived;
			if(expectedreceived.isEmpty())
				replay();
		}

		@Override
		public void send(byte[] byteArray) throws IOException {
			replay();
			super.send(byteArray);
		}



		@Override
		public byte[] recv() throws IOException{
			if(!alreadyexpectedreceived) {
				//on cache ici ce que la victime est cense envoyer pour l'authtentification
				for(int i=0;i<expectedreceived.size();i++) {
					super.recv();
				}
				alreadyexpectedreceived=true;
			}			
			return super.recv();
		}

		private void replay() throws IOException {
			if(!alreadyreplay) {
				for(byte[] mess: toreplay) {
					super.send(mess);				
				}
				alreadyreplay=true;
			}
		}
	}

}
