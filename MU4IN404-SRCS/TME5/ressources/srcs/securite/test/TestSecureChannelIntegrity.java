package srcs.securite.test;

import static org.junit.Assert.assertEquals;
import static srcs.securite.test.ChannelTestUtil.ALGOSIGN;
import static srcs.securite.test.ChannelTestUtil.testAppPropStore;

import java.io.EOFException;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.Test;

import srcs.securite.Authentication;
import srcs.securite.Channel;
import srcs.securite.ChannelBasic;
import srcs.securite.CorruptedMessageException;
import srcs.securite.SecureChannelIntegrity;
import srcs.securite.test.ChannelTestUtil.ChannelModifier;
import srcs.securite.test.ChannelTestUtil.NoException;
import srcs.securite.test.ChannelTestUtil.RetourTest;

public class TestSecureChannelIntegrity  {
		
	public RetourTest test(boolean attack) throws Exception {

		AuthenticationFactory authfact=new AuthenticationFactory();
		
		return testAppPropStore( (s)-> {
			try {
				Channel base = new ChannelBasic(s);
				Authentication auth = authfact.newServeurAuthentication(base);
				return new SecureChannelIntegrity(base, auth, ALGOSIGN);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
		},(s)-> {
			try {
				Channel base = new ChannelBasic(s);
				Authentication auth = authfact.newClientAuthentication(base);
				return new SecureChannelIntegrity(new ChannelModifier(base, attack), auth, ALGOSIGN);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
		});
	}
	
	@Test
	public void testAttack() throws Exception {
		RetourTest ret = test(true);
		assertEquals(EOFException.class,ret.getExceptionClient().getClass());
		assertEquals(CorruptedMessageException.class,ret.getExceptionServeur().getClass());
	}
	
	@Test
	public void testNonAttack() throws Exception {
		RetourTest ret = test(false);
		assertEquals(NoException.class,ret.getExceptionClient().getClass());
		assertEquals(NoException.class,ret.getExceptionServeur().getClass());
		
	}
}
