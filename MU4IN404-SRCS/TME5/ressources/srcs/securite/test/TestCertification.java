package srcs.securite.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static srcs.securite.test.ChannelTestUtil.ALGOKEY_A;
import static srcs.securite.test.ChannelTestUtil.ALGOSIGN;
import static srcs.securite.test.ChannelTestUtil.SIZEKEY_A;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.List;

import org.junit.Test;

import srcs.securite.Certif;
import srcs.securite.CertificationAuthority;
import srcs.securite.Util;

public class TestCertification {

	@Test
	public void test() throws Exception {
		
		//vérifications sur la classe Certif
		Class<Certif> cl = Certif.class;
		//Verifier que tous les attrobuts sont final
		for(Field f : cl.getDeclaredFields()) {
			if(!Modifier.isStatic(f.getModifiers())) {
				assertTrue(Modifier.isFinal(f.getModifiers()));
			}
		}
		
		//vérifier que le constructeur n'est pas de visibilité publique 
		Constructor<Certif> cons = cl.getDeclaredConstructor(String.class, PublicKey.class, byte[].class, String.class);
		assertFalse(Modifier.isPublic(cons.getModifiers()));
		
		
		CertificationAuthority ca = new CertificationAuthority(ALGOKEY_A, SIZEKEY_A, ALGOSIGN);
		
		String id1="id1";
		KeyPair kp1 = Util.generateNewKeyPair(ALGOKEY_A, SIZEKEY_A);
		Certif certif1 = ca.declarePublicKey(id1, kp1.getPublic());
		
		
		
		String id2="id2";
		KeyPair kp2 = Util.generateNewKeyPair(ALGOKEY_A, SIZEKEY_A);
		Certif certif2 = ca.declarePublicKey(id2, kp2.getPublic());
		
		//verifier qu'un certificat inexistant renvoie null
		assertNull(ca.getCertificate("toto"));
		
		//verifier que si on declare un certificat pour un identifiant existant, ceci jette une exeption de securite
		assertThrows(GeneralSecurityException.class, ()->ca.declarePublicKey(id2, kp2.getPublic()));
		
		//verifier que les cles publiques sont bien toutes distinctes
		assertEquals(certif2,ca.getCertificate(id2));
		assertEquals(certif1,ca.getCertificate(id1));
		assertNotEquals(kp1.getPublic(),kp2.getPublic());
		assertNotEquals(kp1.getPublic(),ca.getPublicKey());
		assertNotEquals(kp2.getPublic(),ca.getPublicKey());
		
		//verifier que les identifiant d'un certificat sont conformes
		assertEquals(id1,certif1.getIdentifier());
		assertEquals(id2,certif2.getIdentifier());
		
		//verifier que les cles publiques des certif sont  conformes
		assertEquals(kp1.getPublic(),certif1.getPublicKey());
		assertEquals(kp2.getPublic(),certif2.getPublicKey());
		
		//verifier que seule la cle publique du ca est la seule a valider les certif 
		assertFalse(certif1.verify(kp1.getPublic()));
		assertFalse(certif1.verify(kp2.getPublic()));
		assertFalse(certif2.verify(kp1.getPublic()));
		assertFalse(certif2.verify(kp2.getPublic()));
		assertTrue(certif1.verify(ca.getPublicKey()));
		assertTrue(certif2.verify(ca.getPublicKey()));
				
		
		
		KeyPair kppirat = Util.generateNewKeyPair(ALGOKEY_A, SIZEKEY_A);
		List<Field> fields =  ChannelTestUtil.getFields(certif1.getClass(), PublicKey.class);
		//une seule cle publique attendue en attribut
		assertEquals(1,fields.size());
	
		
		//verifier qu'un certificat corrompue est detectable
		Field f_pubk = fields.get(0);
		f_pubk.setAccessible(true);
		f_pubk.set(certif1, kppirat.getPublic());
		f_pubk.setAccessible(false);
				
		assertFalse(certif1.verify(ca.getPublicKey()));
		assertFalse(certif1.verify(kp1.getPublic()));
		assertFalse(certif1.verify(kp2.getPublic()));
		assertFalse(certif1.verify(kppirat.getPublic()));
		
		//verifier que si on declare un certificat pour un identifiant existant, ceci jette une exeption de securite
		assertThrows(GeneralSecurityException.class, ()->ca.declarePublicKey(id2, kppirat.getPublic()));
		assertThrows(GeneralSecurityException.class, ()->ca.declarePublicKey(id1, kppirat.getPublic()));
			
		
	}

}
