package srcs.chat.implem.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import srcs.chat.implem.Chat;
import srcs.chat.implem.ChatImpl;
import srcs.chat.implem.ChatProxy;
import srcs.chat.implem.MessageReceiver;
import srcs.grpc.util.BuilderUtil;

public class Testchat {
	
	static int portserver=1200;
	
	static Client[] client = new Client[] {
		new Client(12000),
		new Client(12001),
		new Client(12002),
		new Client(12003),
		new Client(12004)
	};
	
	static int nbmessage=client.length*2;
	static String[] messages;
	
	public static class Client implements MessageReceiver{

		private Map<String,List<String>> received= new HashMap<>();
		

		
		private final Chat chat;
		private final int portrappel;
		
		public Client( int portrappel) {
			chat=new ChatProxy("localhost", portserver, this);
			this.portrappel=portrappel;
		}
		
		@Override
		public synchronized void newMessage(String from, String content) {
			if(!received.containsKey(from)) {
				received.put(from, new ArrayList<String>());
			}
			received.get(from).add(content);
			this.notify();
		}		
	}
	static Server server;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ServerBuilder<?> serverbuilder = ServerBuilder.forPort(portserver)
                .addService(new ChatImpl());
		server = BuilderUtil.disableStat(serverbuilder).build();
		server.start();
		Thread.sleep(200);
		messages=new String[nbmessage];			
		for(int i=0;i< nbmessage;i++) {
			messages[i]="Message"+i;
		}
		
	}
	@AfterClass
	public static void tearsDownAfterClass() throws Exception {
		server.shutdownNow();
	}
	
	@Test
	public void testSubscribe() {
		//test subscribe pour tout le monde sauf 1
		for(int i=0; i< client.length-1;i++) {
			assertTrue(client[i].chat.subscribe("client"+i, "localhost", client[i].portrappel));
		}
		assertFalse(client[client.length-1].chat.subscribe("client0", "localhost", client[client.length-1].portrappel));
		
		//test du contenu de la liste des chatters
		for(int i=0; i< client.length-1;i++) {
			for(int j=0;j < client.length-1;j++) {
				assertTrue(client[i].chat.listChatter().contains("client"+j));
			}
			assertFalse(client[i].chat.listChatter().contains("client"+(client.length-1)));
		}
		
		//désabonnement
		for(int i=0; i< client.length-1;i++) {
			client[i].chat.unsubscribe("client"+i);			
		}
		//test si les chatter ne sont plus présent dans la liste des chatter
		for(int i=0; i< client.length-1;i++) {
			for(int j=0;j < client.length-1;j++) {
				assertFalse(client[i].chat.listChatter().contains("client"+j));
			}
			assertFalse(client[i].chat.listChatter().contains("client"+(client.length-1)));
		}
		
		//test réabonnement
		for(int i=0; i< client.length-1;i++) {
			assertTrue(client[i].chat.subscribe("client"+i, "localhost", client[i].portrappel));
		}
		//désabonnement
		for(int i=0; i< client.length-1;i++) {
			client[i].chat.unsubscribe("client"+i);			
		}
	}
	
	@Test
	public void testSend() throws InterruptedException {
		//inscription des clients
		for(int i=0; i< client.length;i++) {
			assertTrue(client[i].chat.subscribe("client"+i, "localhost", client[i].portrappel));
		}
		
		//chaque client envoie un message à tout le monde
		for(int i=0; i< client.length;i++) {
			assertEquals(client.length, client[i].chat.send("client"+i, messages[i]));
		}
		
		for(int i=0; i< client.length;i++) {
			//on attend pour chaque chatter inscrit que l'ensemble des messages envoyés ont bien été recus
			synchronized(client[i]) {
				while(client[i].received.values().size() != client.length) {
					client[i].wait();
				}
			}
			//on s'assure que ça soit les bons messages qui ont été recus
			for(int j=0;j<client.length;j++) {
				assertTrue(client[i].received.get("client"+j).contains(messages[j]) );
			}
		}
		//le client 0 ne fait plus partie des chatter (mais il peut encore envoyer des messages)
		client[0].chat.unsubscribe("client"+0);
		
		//le client 1 envoie un nouveau message
		assertEquals(client.length-1,client[1].chat.send("client"+1, messages[client.length]));
		
		//on s'assure que le client 0 n'a pas reçu le message
		assertFalse(client[0].received.get("client"+1).contains(messages[client.length]));
		for(int i=1; i< client.length;i++) {
			//on attend pour chaque chatter inscrit que l'ensemble des messages envoyés ont bien été recus
			synchronized(client[i]) {
				while(client[i].received.get("client"+1).size()!=2) {
					client[i].wait();
				}
			}
			//on s'assure que le client i a bien reçu le nouveau message du client 1
			assertTrue(client[i].received.get("client"+1).contains(messages[client.length]));
			
		}
		
		
		
		for(int i=1; i< client.length;i++) {
			client[i].chat.unsubscribe("client"+i);			
		}
	}
	

}
