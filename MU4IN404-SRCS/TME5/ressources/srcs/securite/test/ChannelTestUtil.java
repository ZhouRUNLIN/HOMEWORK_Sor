package srcs.securite.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import srcs.securite.Channel;
import srcs.securite.ChannelDecorator;
import srcs.securite.app.PropertiesStore;
import srcs.securite.app.PropertiesStoreImpl;
import srcs.securite.app.PropertiesStoreProxy;
import srcs.securite.app.PropertiesStoreSkeleton;

public final class ChannelTestUtil {

	private ChannelTestUtil() {}
	
	public static final String IDCLIENT="client";
	public static final String IDSERVER="server";
	
	public static final String ALGOKEY_A="RSA";
	public static final int SIZEKEY_A=2048;
	public static final String ALGODIGEST="SHA";
	public static final String ALGOSIGN="SHA1withRSA";
	public static final String ALGOKEY_S="AES";
	public static final int SIZEKEY_S=128;
	
	
	public final static int PORTSERVER=1234;
		
	protected final static PropertiesStoreImpl store = new PropertiesStoreImpl();
	
	
	@FunctionalInterface
	public static  interface ChannelFactory{
		Channel newChannel(Socket s)throws IOException,GeneralSecurityException;
	}
	
	public static class ChannelModifier extends ChannelDecorator {
		
		private final boolean shouldmodify;
		
		public ChannelModifier(Channel decorated, boolean shouldmodify) {
			super(decorated);
			this.shouldmodify=shouldmodify;
		}
		
		@Override
		public void send(byte[] byteArray) throws IOException {
			int flip = 40;
			if(byteArray.length > flip && shouldmodify) 
				byteArray[flip]++;
			super.send(byteArray);
		}

		@Override
		public byte[] recv() throws IOException{
			byte[] res = super.recv();
			return res;
		}

	}
	
	
	public static class RetourTest{
		private final Exception exceptionclient;
		private final Exception exceptionserveur;
		public RetourTest(Exception exceptionclient, Exception exceptionserveur) {
			this.exceptionclient = exceptionclient;
			this.exceptionserveur = exceptionserveur;
		}
		public Exception getExceptionClient() {
			return exceptionclient;
		}
		public Exception getExceptionServeur() {
			return exceptionserveur;
		}
		
	}
	
	public static class Serveur extends Thread{
		
		private final ServerSocket serversocket;
		private final ChannelFactory channelfactory;
		
		private Exception retour=NoException.instance();
		
		public Serveur(ServerSocket serversocket, ChannelFactory channelfactory){
			this.serversocket=serversocket;
			this.channelfactory=channelfactory;
			
		}
		
		public void run() {
			try {
				Socket s = serversocket.accept();
				try(s){
					Channel c = channelfactory.newChannel(s);
					new PropertiesStoreSkeleton(c,store).loop();
				}catch (Exception e) {
					//e.printStackTrace();
					retour=e;
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public Exception getRetour() {
			return retour;
		}
		
	}

	public static class ChannelSniffer  extends ChannelDecorator  {

		private final List<byte[]> received = new ArrayList<>();
		private final List<byte[]> sent = new ArrayList<>();
		
		public ChannelSniffer(Channel decorated) {
			super(decorated);
			
		}
		
		@Override
		public void send(byte[] byteArray) throws IOException {
			sent.add(Arrays.copyOf(byteArray, byteArray.length));
			super.send(byteArray);
		}

		@Override
		public byte[] recv() throws IOException {
			byte[] res = super.recv();
			received.add(Arrays.copyOf(res,res.length));
			return res;
		}
		
		public List<byte[]> getReceived(){
			return Collections.unmodifiableList(received);
		}
		
		public List<byte[]> getSent(){
			return Collections.unmodifiableList(sent);
		}
		
	}
	
	
	/**
	 * 
	 * @param serverfact la factory permettant de construire le canal coté serveur une fois la connexion TCP établie par le client
	 * @param clientfact la factory permettant de constuire le canale coté client une fois la connexion TCP étable avec le serveur
	 * @return le couple des exception de retour client et serveur. Si tout se passe bien, les deux valeurs sont égale à null
	 * @throws IOException en cas d'échec de l'instanciation de socket d'écoute coté serveur
	 */
	
	public static RetourTest testAppPropStore(ChannelFactory serverfact, ChannelFactory clientfact) throws IOException {
		store.clear();
		try(ServerSocket ss = new ServerSocket(PORTSERVER)){
			Serveur serveur = new Serveur(ss, serverfact);
			serveur.start();
			
			Random rand = new Random();
			String k = rand.nextInt()+"";
					
			Exception retourclient=NoException.instance();
			try(Socket s = new Socket("localhost", PORTSERVER)){	
				PropertiesStore ps = new PropertiesStoreProxy(clientfact.newChannel(s));
				assertEquals("",ps.put(k, "val"+k));
				assertEquals("val"+k,store.get(k));
				assertEquals("val"+k,ps.get(k));
			}catch(Exception e) {
				//e.printStackTrace();
				retourclient=e;
			} 
			try {
				serveur.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Exception retourserveur =  serveur.getRetour();
			
				System.out.println("Retour Client : ");
				System.out.println(retourclient.getClass()+ (retourclient.getMessage()!=null ? " : "+retourclient.getMessage() : "" ) );
				System.out.println("");

				System.out.println("Retour Serveur : ");
				System.out.println(retourserveur.getClass()+(retourserveur.getMessage()!=null ? " : "+retourserveur.getMessage() : "" ));
				System.out.println("");
		
		
			return new RetourTest(retourclient, retourserveur);
		}
		
		
	}
	
	public static class NoException extends Exception{
		private static final long serialVersionUID = 1L;
		private static final NoException instance=new NoException();
		public static NoException instance() {
			return instance;
		}
	}
	
	
	
	public static boolean included(byte[] enc, byte[] in) {
		if(in.length > enc.length) return false;
		if(in.length == enc.length) return Arrays.equals(enc, in);
		for(int i=0;i<enc.length;i++) {
			int j=0;
			if(i+in.length > enc.length) return false;
			while(j< in.length && in[j]==enc[i+j] ) {
				j++;
			}
			if(j==in.length) return true;
		}
		return false;
		
	}
	
	public static List<Field> getFields(Class<?> from, Class<?> type){
		List<Field> res = new ArrayList<>();
		for(Field f : from.getDeclaredFields()) {
			if(type.isAssignableFrom(f.getType()))
				res.add(f);
		}
		return res;
	}
	
}
