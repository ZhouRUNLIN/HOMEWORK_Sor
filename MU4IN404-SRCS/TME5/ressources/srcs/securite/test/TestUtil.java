package srcs.securite.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static srcs.securite.test.ChannelTestUtil.ALGOKEY_A;
import static srcs.securite.test.ChannelTestUtil.SIZEKEY_A;

import java.security.KeyPair;
import java.util.Arrays;

import org.junit.Test;

import srcs.securite.Util;

public class TestUtil {

	@Test
	public void testKPgen() throws Exception {
		
		KeyPair kp1 = Util.generateNewKeyPair(ALGOKEY_A, SIZEKEY_A);
		KeyPair kp2 = Util.generateNewKeyPair(ALGOKEY_A, SIZEKEY_A);
		
		assertFalse(Arrays.equals(kp1.getPrivate().getEncoded(), kp2.getPrivate().getEncoded()));
		assertFalse(Arrays.equals(kp1.getPublic().getEncoded(), kp2.getPublic().getEncoded()));
		assertEquals(ALGOKEY_A,kp1.getPrivate().getAlgorithm());
		assertEquals(ALGOKEY_A,kp2.getPrivate().getAlgorithm());
		assertEquals(ALGOKEY_A,kp1.getPublic().getAlgorithm());
		assertEquals(ALGOKEY_A,kp2.getPublic().getAlgorithm());
			
	}
}
