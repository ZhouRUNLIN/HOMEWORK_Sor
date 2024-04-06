package srcs.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import srcs.service.EtatGlobal;
import srcs.service.SansEtat;
import srcs.service.ServeurMultiThread;
import srcs.service.Service;

public class TestServeurMultiThread {

	public static abstract class ServiceImpl implements Service{
		
		public static int cpt_instance=0;
		public static int cpt_execute=0;
		public static Set<Long> thread_ids = new HashSet<>();
		
		
		public static void reset() {
			cpt_instance=0;
			cpt_execute=0;
			thread_ids.clear();
		}
		
		public ServiceImpl() {
			cpt_instance++;
		}
		public void execute(Socket connexion) {
			cpt_execute++;
			thread_ids.add(Thread.currentThread().getId());
		}
	}
		
	@SansEtat
	public static class ServiceSansEtat extends ServiceImpl{}
	
	@EtatGlobal
	public static class ServiceEtatGlobal extends ServiceImpl{}
	
	public static class ServiceSansAnnotation extends ServiceImpl{}
	
	public static int port=4234;	
	
	@Before
	public void before() {
		ServiceImpl.reset();
		port++;//pour eviter erreur de bind entre deux serveurs
	}
	
	
	
	public void stress(Class<? extends Service> cl_service, int nb_connexions) throws InterruptedException, IOException {
		Thread thread_serveur = new Thread(()->	new ServeurMultiThread(port, cl_service).listen());
		thread_serveur.start();
		Thread.sleep(200);
		for(int i=0;i < nb_connexions ; i++) {
			Socket sock = new Socket("localhost", port);
			Thread.sleep(50);
			sock.close();
		}
		thread_serveur.interrupt();
				
	}
	
	
	
	@Test
	public void testSansEtat() throws UnknownHostException, IOException, InterruptedException {
						
		int nb_connexion=4;
		stress(ServiceSansEtat.class,nb_connexion);
		assertEquals(nb_connexion, ServiceImpl.cpt_execute);
		assertEquals(nb_connexion, ServiceImpl.cpt_instance);
		assertEquals(nb_connexion, ServiceImpl.thread_ids.size());
	}
	
	
	@Test
	public void testAvecEtat() throws UnknownHostException, IOException, InterruptedException {
						
		int nb_connexion=4;
		stress(ServiceEtatGlobal.class,nb_connexion);
		assertEquals(nb_connexion, ServiceImpl.cpt_execute);
		assertEquals(1, ServiceImpl.cpt_instance);	
		assertEquals(nb_connexion, ServiceImpl.thread_ids.size());
	}
	
	
	boolean passage=false;
	@Test
	public void testSansAnnotation() throws UnknownHostException, IOException, InterruptedException {
		Thread thread_serveur = new Thread( ()-> { 
			try{ 
				new ServeurMultiThread(port, ServiceSansAnnotation.class).listen() ;
				passage=false;
			}catch(IllegalStateException e) {
				passage=true;
			}
			
		});
		thread_serveur.start();
		Thread.sleep(200);
		Socket sock = new Socket("localhost", port);
		Thread.sleep(50);
		sock.close();
		thread_serveur.interrupt();
		assertTrue(passage);	
	}
	
	
	
	
	

}
