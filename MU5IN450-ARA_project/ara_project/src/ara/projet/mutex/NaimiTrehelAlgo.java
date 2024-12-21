package ara.projet.mutex;

import static ara.util.Constantes.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ara.projet.mutex.InternalEvent.TypeEvent;
import ara.util.Message;
import ara.util.MyRandom;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

public class NaimiTrehelAlgo implements EDProtocol {

	// Nom des arguments du fichiers de configuration
	private static final String PAR_TRANSPORT = "naimi_transport";
	private static final String PAR_TIME_CS = "timeCS";
	private static final String PAR_TIME_BETWEEN_CS = "timeBetweenCS";

	// constantes de l'algorithme
	public static final long initial_owner = 0L;
	public static final long nil = -2L;

	// tag des messages
	// public static final String REQUEST_TAG = "request";
	// public static final String TOKEN_TAG = "token";

	// etats possibles du noeud dans l'application
	public static enum State {
		tranquil, requesting, inCS
	}

	// etats du Token
	public static enum TokenState {
		InUse, InTransit, Unused
	}

	// paramètres de l'algorithme lus depuis le fichier de configuration
	protected final long timeCS;
	protected final long timeBetweenCS;
	protected final int transport_id;
	protected final int protocol_id;

	// variables d'état de l'application
	protected State state;
	protected Queue<Long> next;
	protected long last;
	protected int nb_cs = 0;// permet de compter le nombre de section critiques
							// exécutées par le noeud

	protected int global_counter = 0; // compteur qui sera inclu dans le message
										// jeton, sa valeur est égale à la
										// dernière valeur connue
										// (i.e. depuis la dernière fois où le
										// noeud a vu passer le jeton)
										// ATTENTION, cette variable n'est pas
										// globale, elle est propre à chaque
										// noeud
										// mais ils ne peuvent
										// la modifier uniquement lorsqu'ils
										// possèdent le jeton

	protected int id_execution; // permet d'identifier l'id d'exécution,
								// incrémenté si l'application est
								// suspendue
								// (toujours constant dans cette classe mais
								// peut être incrémenté dans les sous-classes)

	// Si le nœud détient un jeton
	private boolean holdsToken = false;

	private static Map<TokenState, Long> tokenStateDurations = new HashMap<>();
	private static long lastTokenStateChangeTime = 0;
	private static TokenState currentTokenState = TokenState.Unused;
	private static List<Integer> tokenMessagesPerCS = new ArrayList<>();
	private static List<Integer> requestMessagesPerCS = new ArrayList<>();

	private static int tokenMessagesSinceLastCS = 0;
	private static int requestMessagesSinceLastCS = 0;

	private static Map<Integer, Integer> requestMessagesByNode = new HashMap<>();
	private static Map<Integer, Long> totalRequestTimeByNode = new HashMap<>();
	private static Map<Integer, Integer> requestCountByNode = new HashMap<>();

	private static Map<Integer, Long> requestStartTimeByNode = new HashMap<>();

	private static List<Long> allRequestTimes = new ArrayList<>();

	private static long totalTokenTime;

	public NaimiTrehelAlgo(String prefix) {
		String tmp[] = prefix.split("\\.");
		protocol_id = Configuration.lookupPid(tmp[tmp.length - 1]);

		transport_id = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
		timeCS = Configuration.getLong(prefix + "." + PAR_TIME_CS);
		timeBetweenCS = Configuration.getLong(prefix + "." + PAR_TIME_BETWEEN_CS);
	}

	public Object clone() {
		NaimiTrehelAlgo res = null;
		try {
			res = (NaimiTrehelAlgo) super.clone();
		} catch (CloneNotSupportedException e) {
		} // never happens
		res.initialisation(CommonState.getNode());

		return res;
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {
		if (protocol_id != pid) {
			throw new RuntimeException("Receive an event for wrong protocol");
		}

		if (event instanceof InternalEvent) {
			InternalEvent ev = (InternalEvent) event;
			if (ev.getDate() == id_execution) {
				switch (ev.getType()) {
					case release_cs:
						nb_cs++;
						this.releaseCS(node);
						break;
					case request_cs:
						this.requestCS(node);
						break;
					default:
						throw new RuntimeException("Receive unknown type event");
				}
			} else {
				log.warning(node.getID() + " : ignoring obsolete event " + ev);
			}
		} else if (event instanceof Message) {
			Message m = (Message) event;
			if (m instanceof RequestMessage) {
				RequestMessage rm = (RequestMessage) m;
				this.receive_request(node, m.getIdSrc(), rm.getRequester());
			} else if (m instanceof TokenMessage) {
				TokenMessage tm = (TokenMessage) m;
				this.receive_token(node, tm.getIdSrc(), tm.getNext(), tm.getCounter());
			} else {
				throw new RuntimeException("Receive unknown type Message");
			}

		} else {
			throw new RuntimeException("Receive unknown type event");
		}

	}

	/////////////////////////////////////////// METHODES DE
	/////////////////////////////////////////// L'ALGORITHME////////////////////////////////////////////
	private void executeCS(Node host) {

		int nodeId = (int) host.getID();

		// Délai entre la demande et l'entrée au CS
		if (requestStartTimeByNode.containsKey(nodeId)) {
			long requestStartTime = requestStartTimeByNode.get(nodeId);
			long currentTime = CommonState.getTime();
			long timeToEnterCS = currentTime - requestStartTime;

			// Mettre à jour le temps total d'entrée de chaque nœud dans la CS
			totalRequestTimeByNode.put(nodeId,
					totalRequestTimeByNode.getOrDefault(nodeId, 0L) + timeToEnterCS);
			// Nombre d'entrées dans le CS
			requestCountByNode.put(nodeId, requestCountByNode.getOrDefault(nodeId, 0) + 1);

			// Délai d'enregistrement de toutes les demandes
			allRequestTimes.add(timeToEnterCS);

			// Supprimer l'heure de début de la demande en cours
			requestStartTimeByNode.remove(nodeId);
		}


		log.info("Node " + host.getID() + " executing its CS num " + nb_cs + " : next= " + next.toString());
		global_counter++;
		log.info("Node " + host.getID() + " global counter = " + global_counter);


		updateTokenState(TokenState.InUse);

		// Enregistrer le nombre de messages d'application par CS
		tokenMessagesPerCS.add(tokenMessagesSinceLastCS);
		requestMessagesPerCS.add(requestMessagesSinceLastCS);


		tokenMessagesSinceLastCS = 0;
		requestMessagesSinceLastCS = 0;

	}

	private void initialisation(Node host) {
		changestate(host, State.tranquil);
		next = new ArrayDeque<Long>();
		if (host.getID() == initial_owner) {
			last = nil;
			holdsToken = true;
			// Initialisation de la minuterie d'état
			lastTokenStateChangeTime = CommonState.getTime();
			currentTokenState = TokenState.Unused;
		} else {
			last = initial_owner;
			holdsToken = false;
		}
	}

	private void requestCS(Node host) {
		log.fine("Node " + host.getID() + " requestCS");
		changestate(host, State.requesting);
		int nodeId = (int) host.getID();
		long currentTime = CommonState.getTime();

		// Enregistrer l'heure de début de la demande
		requestStartTimeByNode.put(nodeId, currentTime);

		// Mise à jour du nombre de messages de demande pour le nœud
		requestMessagesByNode.put(nodeId, requestMessagesByNode.getOrDefault(nodeId, 0) + 1);
		requestMessagesSinceLastCS++;


		if (last != nil) {
			Transport tr = (Transport) host.getProtocol(transport_id);
			Node dest = Network.get((int) last);
			tr.send(host, dest, new RequestMessage(host.getID(), dest.getID(), protocol_id, host.getID()), protocol_id);
			last = nil;
			return; // on simule un wait ici
		}
		changestate(host, State.inCS);
		// DEBUT CS
	}

	private void releaseCS(Node host) {
		log.fine("Node " + host.getID() + " releaseCS next=" + next);
		changestate(host, State.tranquil);
		if (!next.isEmpty()) {
			last = getLast(next);
			long next_holder = next.poll(); // dequeue
			Transport tr = (Transport) host.getProtocol(transport_id);
			Node dest = Network.get((int) next_holder);
			log.fine("Node " + host.getID() + " send token( counter = " + global_counter + " next =" + next + ") to "
					+ dest.getID());
			tr.send(host, dest, new TokenMessage(host.getID(), dest.getID(), protocol_id, new ArrayDeque<Long>(next),
					global_counter), protocol_id);
			next.clear();

			holdsToken = false;

			// Mise à jour du nombre de messages de token
			tokenMessagesSinceLastCS++;

			updateTokenState(TokenState.InTransit);
		} else {
			updateTokenState(TokenState.Unused);
		}
	}

	private void receive_request(Node host, long from, long requester) {
		log.fine("Node " + host.getID() + " receive request message from Node " + from + " for Node " + requester);
		int nodeId = (int) host.getID();

		synchronized (NaimiTrehelAlgo.class) {
			requestMessagesSinceLastCS++;
		}

		if (last == nil) {
			if (state != State.tranquil) {
				next.add(requester);
			} else {
				Transport tr = (Transport) host.getProtocol(transport_id);
				Node dest = Network.get((int) requester);
				log.fine("Node " + host.getID() + " send token( counter = " + global_counter + " next =" + next
						+ ") to " + dest.getID() + " (no need)");
				tr.send(host, dest, new TokenMessage(host.getID(), dest.getID(), protocol_id, new ArrayDeque<Long>(),
						global_counter), protocol_id);


				tokenMessagesSinceLastCS++;

				last = requester;
				holdsToken = false;
			}
		} else {
			Transport tr = (Transport) host.getProtocol(transport_id);
			Node dest = Network.get((int) last);
			tr.send(host, dest, new RequestMessage(host.getID(), dest.getID(), protocol_id, requester), protocol_id);

			requestMessagesSinceLastCS++;

			last = requester;
		}
	}

	private void receive_token(Node host, long from, Queue<Long> remote_queue, int counter) {
		log.fine("Node " + host.getID() + " receive token message (" + remote_queue.toString() + ", counter = "
				+ counter + ") from Node " + from + " next =" + next.toString());
		global_counter = counter;
		remote_queue.addAll(next);
		next = remote_queue;

		holdsToken = true;

		if (state == State.requesting) {
			changestate(host, State.inCS);
			updateTokenState(TokenState.InUse);
		} else {
			updateTokenState(TokenState.Unused);
		}
	}

	/////////////////////////////////////// METHODES
	/////////////////////////////////////////// UTILITAIRES////////////////////////////////////////////
	protected void changestate(Node host, State s) {
		long currentTime = CommonState.getTime();
		long duration = currentTime - lastTokenStateChangeTime;

		// Mise à jour de l'heure d'état du token uniquement si le nœud actuel détient le token
		if (holdsToken) {
			updateTokenStateDuration(currentTokenState, duration);
		}

		this.state = s;

		switch (this.state) {
			case inCS:
				executeCS(host);
				schedule_release(host);
				break;
			case tranquil:
				schedule_request(host);
				break;
			default:
		}

		// Mise à jour de l'heure du dernier changement d'état
		lastTokenStateChangeTime = currentTime;
	}

	private static void updateTokenStateDuration(TokenState state, long duration) {
		tokenStateDurations.put(state, tokenStateDurations.getOrDefault(state, 0L) + duration);
	}

	private static long getLast(Queue<Long> q) {
		Object tmp[] = q.toArray();
		return (Long) tmp[tmp.length - 1];
	}

	private void schedule_release(Node host) {
		long res = MyRandom.nextLong(timeCS, 0.1);
		EDSimulator.add(res, new InternalEvent(TypeEvent.release_cs, id_execution), host, protocol_id);
	}

	private void schedule_request(Node host) {
		long res = MyRandom.nextLong(timeBetweenCS, 0.1);
		EDSimulator.add(res, new InternalEvent(TypeEvent.request_cs, id_execution), host, protocol_id);
	}

	//  Mise à jour etat de token
	private static void updateTokenState(TokenState newState) {
		long currentTime = CommonState.getTime();
		long duration = currentTime - lastTokenStateChangeTime;

		updateTokenStateDuration(currentTokenState, duration);

		currentTokenState = newState;
		lastTokenStateChangeTime = currentTime;
	}

	////////////////////////////////////////// classe des messages ////////////////////////////////////////// /////////////////////////////////////

	public static class RequestMessage extends Message {

		private final long requester;

		public RequestMessage(long idsrc, long iddest, int pid, long initiator) {
			super(idsrc, iddest, pid);
			this.requester = initiator;
		}
		public long getRequester() {
			return requester;
		}

	}

	public static class TokenMessage extends Message {

		private final int counter;
		private final Queue<Long> next;

		public TokenMessage(long idsrc, long iddest, int pid, Queue<Long> next, int counter) {
			super(idsrc, iddest, pid);
			this.counter = counter;
			this.next = next;
		}

		public int getCounter() {
			return counter;
		}

		public Queue<Long> getNext() {
			return new ArrayDeque<Long>(next);
		}

		@Override
		public String toString() {
			return "TokenMessage( from=" + getIdSrc() + ", to = " + getIdDest() + "  counter = " + getCounter()
					+ " next = " + getNext() + ")";
		}

	}

	////////////////////////////////////////////// méthodes
	// ////////////////////////////////////////////// d'impression ////////////////////////////////////////////

	public static void printStatistics() {
		System.out.println("===== NaimiTrehelAlgo Informations statistiques =====");

		// Mise à jour de la durée de l'état actuel du jeton en fonction de l'heure actuelle
		long currentTime = CommonState.getTime();
		long duration = currentTime - lastTokenStateChangeTime;

		// Mise à jour de la durée de l'état actuel du jeton
		updateTokenStateDuration(currentTokenState, duration);

		// Imprimer le nombre de messages d'application dans chaque CS
		System.out.println("Le nombre de messages d'application dans chaque CS:");
		for (int i = 0; i < tokenMessagesPerCS.size(); i++) {
			System.out.println("  CS " + (i + 1) + ": Token msg = " + tokenMessagesPerCS.get(i)
					+ ", Request msg = " + requestMessagesPerCS.get(i));
		}

		// Imprimer le nombre de messages de demande par nœud
		System.out.println("Le nombre de messages de demande envoyés par chaque nœud :");
		for (int i = 0; i < 10; i++) {
			int count = requestMessagesByNode.getOrDefault(i, 0);
			System.out.println("nœud " + i + "： " + count);
		}

		// Imprimer le temps moyen entre la demande et l'entrée dans le CS pour chaque nœud.
		System.out.println("Le temps moyen entre la demande et l'entrée dans le CS pour chaque nœud :");
		for (int i = 0; i < 10; i++) {
			long totalTime = totalRequestTimeByNode.getOrDefault(i, 0L);
			int count = requestCountByNode.getOrDefault(i, 0);
			if (count > 0) {
				double averageTime = (double) totalTime / count;
				System.out.println("nœud " + i + "： " + String.format("%.2f", averageTime));
			} else {
				System.out.println("nœud " + i + "： sans information");
			}
		}

		// Calculer et imprimer le pourcentage de temps pendant lequel le jeton se trouve dans chaque état.
		System.out.println("Le pourcentage de temps pendant lequel le Token se trouve dans chaque État :");
		totalTokenTime = 0;
		for (Long time : tokenStateDurations.values()) {
			totalTokenTime += time;
		}
		for (TokenState state : TokenState.values()) {
			long stateTime = tokenStateDurations.getOrDefault(state, 0L);
			if (totalTokenTime > 0) {
				double percentage = ((double) stateTime / totalTokenTime) * 100;
				System.out.println("etat " + state + ": " + stateTime + " (" + String.format("%.2f", percentage) + "%)");
			} else {
				System.out.println("etat " + state + ": 0 (0%)");
			}
		}
		System.out.println("=======================================");

		writeStatisticsToCSV();
	}

	// Écrire au format csv
	private static void writeStatisticsToCSV() {
		String csvFile = "NaimiTrehelStats.csv";
		boolean fileExists = new File(csvFile).exists();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
			if (!fileExists) {
				writer.write("RunID,TotalCS,TokenSum,TokenAverage,TokenMode,TokenMedian,RequestSum,RequestAverage,RequestMode,RequestMedian");

				for (int i = 0; i < 10; i++) {
					writer.write(",Node" + i + "_Requests");
				}

				for (int i = 0; i < 10; i++) {
					writer.write(",Node" + i + "_AvgTime");
				}

				writer.write(",AllNodes_AvgTime,AllNodes_ModeTime,AllNodes_MedianTime,AllNodes_TotalTime");

				writer.write(",Token_InUse_Percentage,Token_InTransit_Percentage,Token_Unused_Percentage");

				writer.newLine();
			}

			long runId = System.currentTimeMillis();

			// Calculer les statistiques du CS
			int totalCS = tokenMessagesPerCS.size();
			long tokenSum = sum(tokenMessagesPerCS);
			double tokenAverage = totalCS > 0 ? (double) tokenSum / totalCS : 0.0;
			int tokenMode = mode(tokenMessagesPerCS);
			double tokenMedian = median(tokenMessagesPerCS);

			long requestSum = sum(requestMessagesPerCS);
			double requestAverage = totalCS > 0 ? (double) requestSum / totalCS : 0.0;
			int requestMode = mode(requestMessagesPerCS);
			double requestMedian = median(requestMessagesPerCS);


			StringBuilder nodeRequests = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				int count = requestMessagesByNode.getOrDefault(i, 0);
				nodeRequests.append(",").append(count);
			}

			// Calculer le temps moyen par nœud
			StringBuilder nodeAvgTimes = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				long totalTime = totalRequestTimeByNode.getOrDefault(i, 0L);
				int count = requestCountByNode.getOrDefault(i, 0);
				double avgTime = count > 0 ? (double) totalTime / count : 0.0;
				nodeAvgTimes.append(",").append(String.format("%.2f", avgTime));
			}

			// Calculer le temps moyen, le temps pluriel, le temps médian et le temps total pour tous les nœuds.
			double allNodesAvgTime = 0.0;
			long allNodesModeTime = 0;
			double allNodesMedianTime = 0.0;
			long allNodesTotalTime = 0L;

			synchronized (allRequestTimes) {
				if (!allRequestTimes.isEmpty()) {
					allNodesTotalTime = sumLong(allRequestTimes);
					allNodesAvgTime = (double) allNodesTotalTime / allRequestTimes.size();
					allNodesModeTime = modeLong(allRequestTimes);
					allNodesMedianTime = medianLong(allRequestTimes);
				}
			}

			// Calculer le pourcentage de temps pendant lequel le jeton se trouve dans chaque État.
			double inUsePercentage = 0.0;
			double inTransitPercentage = 0.0;
			double unusedPercentage = 0.0;
			if (totalTokenTime > 0) {
				inUsePercentage = ((double) tokenStateDurations.getOrDefault(TokenState.InUse, 0L) / totalTokenTime) * 100;
				inTransitPercentage = ((double) tokenStateDurations.getOrDefault(TokenState.InTransit, 0L) / totalTokenTime) * 100;
				unusedPercentage = ((double) tokenStateDurations.getOrDefault(TokenState.Unused, 0L) / totalTokenTime) * 100;
			}


			StringBuilder csvLine = new StringBuilder();
			csvLine.append(runId).append(",")
					.append(totalCS).append(",")
					.append(tokenSum).append(",")
					.append(String.format("%.2f", tokenAverage)).append(",")
					.append(tokenMode).append(",")
					.append(String.format("%.2f", tokenMedian)).append(",")
					.append(requestSum).append(",")
					.append(String.format("%.2f", requestAverage)).append(",")
					.append(requestMode).append(",")
					.append(String.format("%.2f", requestMedian))
					.append(nodeRequests.toString())
					.append(nodeAvgTimes.toString())
					.append(",").append(String.format("%.2f", allNodesAvgTime))
					.append(",").append(allNodesModeTime)
					.append(",").append(String.format("%.2f", allNodesMedianTime))
					.append(",").append(allNodesTotalTime)
					.append(",").append(String.format("%.2f", inUsePercentage))
					.append(",").append(String.format("%.2f", inTransitPercentage))
					.append(",").append(String.format("%.2f", unusedPercentage));

			writer.write(csvLine.toString());
			writer.newLine();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
////////////////////////////////////////////// méthodes ////////////////////////////////////////////
// //////////////////////////////////////////////d'outillage ////////////////////////////////////////////
	// ////////////////////////////////////////////// d'impression ////////////////////////////////////////////

	private static long sum(List<Integer> list) {
		long total = 0;
		for (int num : list) {
			total += num;
		}
		return total;
	}

	private static long sumLong(List<Long> list) {
		long total = 0;
		for (long num : list) {
			total += num;
		}
		return total;
	}

	private static double average(List<Integer> list) {
		if (list.isEmpty()) return 0.0;
		return sum(list) / (double) list.size();
	}

	private static int mode(List<Integer> list) {
		if (list.isEmpty()) return 0;
		Map<Integer, Integer> frequencyMap = new HashMap<>();
		for (int num : list) {
			frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
		}
		int mode = list.get(0);
		int maxCount = 1;
		for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
			if (entry.getValue() > maxCount) {
				mode = entry.getKey();
				maxCount = entry.getValue();
			}
		}
		return mode;
	}

	private static double median(List<Integer> list) {
		if (list.isEmpty()) return 0.0;
		List<Integer> sorted = new ArrayList<>(list);
		Collections.sort(sorted);
		int n = sorted.size();
		if (n % 2 == 1) {
			return sorted.get(n / 2);
		} else {
			return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
		}
	}

	private static double averageLong(List<Long> list) {
		if (list.isEmpty()) return 0.0;
		return sumLong(list) / (double) list.size();
	}

	private static long modeLong(List<Long> list) {
		if (list.isEmpty()) return 0;
		Map<Long, Integer> frequencyMap = new HashMap<>();
		for (long num : list) {
			frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
		}
		long mode = list.get(0);
		int maxCount = 1;
		for (Map.Entry<Long, Integer> entry : frequencyMap.entrySet()) {
			if (entry.getValue() > maxCount) {
				mode = entry.getKey();
				maxCount = entry.getValue();
			}
		}
		return mode;
	}

	private static double medianLong(List<Long> list) {
		if (list.isEmpty()) return 0.0;
		List<Long> sorted = new ArrayList<>(list);
		Collections.sort(sorted);
		int n = sorted.size();
		if (n % 2 == 1) {
			return sorted.get(n / 2);
		} else {
			return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
		}
	}

}
