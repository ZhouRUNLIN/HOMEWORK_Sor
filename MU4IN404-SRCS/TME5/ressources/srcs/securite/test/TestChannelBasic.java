package srcs.securite.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static srcs.securite.test.ChannelTestUtil.testAppPropStore;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import srcs.securite.ChannelBasic;
import srcs.securite.ChannelDecorator;
import srcs.securite.test.ChannelTestUtil.ChannelFactory;
import srcs.securite.test.ChannelTestUtil.ChannelSniffer;
import srcs.securite.test.ChannelTestUtil.NoException;
import srcs.securite.test.ChannelTestUtil.RetourTest;

public class TestChannelBasic {
	
	
	@Test
	public void testa() throws IOException {
		ChannelFactory fact = (s)->new ChannelBasic(s);
		for(int i=0;i<3;i++) {
			RetourTest ret = testAppPropStore(fact,fact);
			assertEquals(NoException.class,ret.getExceptionClient().getClass());
			assertEquals(NoException.class,ret.getExceptionServeur().getClass());
		}		
	}
	
	@Test
	public void testb() throws IOException {
		
		for(int i=0;i<3;i++) {
			RetourTest ret = testAppPropStore(s->new ChannelDecorator(new ChannelBasic(s)),
					 s->new ChannelDecorator(new ChannelDecorator(new ChannelBasic(s))));
			assertEquals(NoException.class,ret.getExceptionClient().getClass());
			assertEquals(NoException.class,ret.getExceptionServeur().getClass());
		}
	}
	
	private ChannelSniffer snifferclient;
	private ChannelSniffer snifferserveur;
	
	@Test
	public void testc() throws IOException {
		
		for(int i=0;i<3;i++) {
			RetourTest ret = testAppPropStore(					
					 s->{
						 snifferserveur = new ChannelSniffer(new ChannelBasic(s));
						 return snifferserveur;
					 },					 
					 s->{
						 snifferclient =  new ChannelSniffer(new ChannelBasic(s)); 
						 return snifferclient;
					 });			
			assertEquals(NoException.class,ret.getExceptionClient().getClass());
			assertEquals(NoException.class,ret.getExceptionServeur().getClass());
			
			//le nombre de message envoyé doit être égal au nombre de message reçus
			assertEquals(snifferserveur.getSent().size(),snifferclient.getReceived().size());
			assertEquals(snifferclient.getSent().size(),snifferserveur.getReceived().size());
			
			//chaque message envoyé doit être inchangé à sa réception
			for(int j=0;j<snifferserveur.getSent().size();j++) {
				assertTrue(Arrays.equals(snifferserveur.getSent().get(j), snifferclient.getReceived().get(j)));
			}
			for(int j=0;j<snifferclient.getSent().size();j++) {
				assertTrue(Arrays.equals(snifferclient.getSent().get(j), snifferserveur.getReceived().get(j)));
			}
			
			
		}
	}
	
}
