package ara.projet.checkpointing.algorithm;

import static ara.util.Constantes.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ara.projet.checkpointing.Checkpointable;
import ara.projet.checkpointing.Checkpointer;
import ara.projet.checkpointing.NodeState;
import ara.util.Message;
import ara.util.MyRandom;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Fallible;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

/**
 * 
 * @author jlejeune Version modifiée de l'algorithme de Juang Venkatesan, où on
 *         fait x broadcasts de message de rollback (= un round) pour obtenir
 *         une ligne de recouvrement cohérente. Dans la version initiale x était
 *         toujours égal à N-1 (N = nombre de noeuds du système) ce qui pouvait
 *         poser les problèmes suivants : 1) si il faut plus de N-1 broadcasts
 *         alors erreur 2) si on a trouvé avant une ligne de recouvrement alors
 *         on fait des broadcasts pour rien. Dans cette version on fait des
 *         broadcasts tant qu'il y a quelqu'un qui a effacé un checkpoint (un
 *         rollback) dans le round La ligne de recouvrement est trouvée lorsque
 *         personne a fait un rollback dans le round
 */
public class JuangVenkatesanAlgo implements Checkpointer, EDProtocol, Transport {

	private static final String PAR_TRANSPORT = "jv_transport";
	private static final String PAR_CHECKPOINTABLE = "checkpointable";
	private static final String PAR_TIMECHECKPOINTING = "timecheckpointing";

	private final int checkpointable_id;
	private final int transport;
	private final int protocol_id;
	private final long timecheckpointing;

	// attributs pour compter le nombre de messages entrants et sortants
	private Map<Long, Integer> sent;
	private Map<Long, Integer> rcvd;
	// attribut pour sauvegarder les messages envoyés depuis le dernier
	// checkpoint
	private Map<Long, List<WrappingMessage>> sent_messages;

	// ATtributs de sauvegarde
	private Stack<NodeState> states; // etat de l'application
	private Stack<Map<Long, Integer>> saved_sent;// nombre de message envoyés
	private Stack<Map<Long, Integer>> saved_rcvd;// nombre de message reçus
	private Stack<Map<Long, List<WrappingMessage>>> saved_sent_messages;// message envoyés

	// attributs pour l'algo de recovery
	private boolean is_recovery = false;
	private int idround = 0;
	private int nb_remaining_received_ack_rollback = 0;
	private boolean should_continue_rollback = false;
	private int nb_remaining_received_rollback = 0;
	private List<WrappingMessage> message_to_replay_after_recovery;
	private int nb_remaining_replyrecovery;

	public JuangVenkatesanAlgo(String prefix) {
		String tmp[] = prefix.split("\\.");
		protocol_id = Configuration.lookupPid(tmp[tmp.length - 1]);
		transport = Configuration.getPid(prefix + "." + PAR_TRANSPORT);

		checkpointable_id = Configuration.getPid(prefix + "." + PAR_CHECKPOINTABLE);
		timecheckpointing = Configuration.getLong(prefix + "." + PAR_TIMECHECKPOINTING);

	}

	public Object clone() {
		JuangVenkatesanAlgo res = null;
		try {
			res = (JuangVenkatesanAlgo) super.clone();
		} catch (CloneNotSupportedException e) {
		} // never happens
		res.states = new Stack<>();
		res.sent = new HashMap<>();
		res.rcvd = new HashMap<>();
		for (int i = 0; i < Network.size(); i++) {
			res.sent.put(Long.valueOf(i), 0);
			res.rcvd.put(Long.valueOf(i), 0);
		}

		res.saved_rcvd = new Stack<>();
		res.saved_sent = new Stack<>();
		res.sent_messages = new HashMap<>();
		res.saved_sent_messages = new Stack<>();

		res.message_to_replay_after_recovery = new ArrayList<>();

		next_turn(CommonState.getNode());
		return res;
	}

	@Override
	public long getLatency(Node src, Node dest) {
		return ((Transport) src.getProtocol(transport)).getLatency(src, dest);
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {
		if (protocol_id != pid) {
			throw new RuntimeException("Receive an event for wrong protocol");
		}
		if (event instanceof String) {
			String ev = (String) event;
			if (ev.equals("loop")) {
				loop(node);
			} else {
				throw new RuntimeException("Receive unknown type event");
			}
		} else if (event instanceof WrappingMessage) {
			receiveWrappingMessage(node, (WrappingMessage) event);
		} else if (event instanceof RollBackMessage) {
			receiveRollBackMessage(node, (RollBackMessage) event);
		} else if (event instanceof AckRollBackMessage) {
			receiveAckRollBackMessage(node, (AckRollBackMessage) event);
		} else if (event instanceof AskMissingMessMessage) {
			receiveAskMissingMessMessage(node, (AskMissingMessMessage) event);
		} else if (event instanceof ReplyAskMissingMessMessage) {
			receiveReplyAskMissingMessMessage(node, (ReplyAskMissingMessMessage) event);
		} else {
			throw new RuntimeException("Receive unknown type event");
		}
	}

	private void receiveWrappingMessage(Node host, WrappingMessage wm) {
		Message m = wm.getMessage();
		long sender = m.getIdSrc();
		if (!is_recovery) {
			rcvd.put(sender, rcvd.get(sender) + 1);
			((EDProtocol) host.getProtocol(m.getPid())).processEvent(host, m.getPid(), m);
		}
	}

	@Override
	public void send(Node src, Node dest, Object msg, int pid) {
		Transport t = (Transport) src.getProtocol(transport);
		if (!is_recovery) {
			sent.put(dest.getID(), sent.get(dest.getID()) + 1);
			WrappingMessage mess = new WrappingMessage(src.getID(), dest.getID(), protocol_id, (Message) msg);
			if (!sent_messages.containsKey(dest.getID())) {
				sent_messages.put(dest.getID(), new ArrayList<>());
			}
			sent_messages.get(dest.getID()).add(mess);
			log.fine("Node " + src.getID() + " : add " + mess + " in sent_messages, sent_messages = " + sent_messages);
			t.send(src, dest, mess, protocol_id);
		}

	}

	@Override
	public void createCheckpoint(Node host) {

		Checkpointable chk = (Checkpointable) host.getProtocol(checkpointable_id);
		NodeState ns = chk.getCurrentState();
		states.push(ns);
		saved_sent.push(new HashMap<>(sent));
		saved_rcvd.push(new HashMap<>(rcvd));
		saved_sent_messages.push(new HashMap<>(sent_messages));
		sent_messages.clear();

		log.fine("Node " + host.getID() + " : saved  state (" + (states.size()) + ") " + states.peek() + " sent = "
				+ saved_sent.peek() + " rcvd = " + saved_rcvd.peek() + " sent_messages = "
				+ saved_sent_messages.peek());

	}

	///////////////////////////////////////// MEthodes pour le Recouvrement

	@Override
	public void recover(Node host) {
		idround = 0;

		log.fine("Node " + host.getID() + " : start recovering");
		Checkpointable chk = (Checkpointable) host.getProtocol(checkpointable_id);
		chk.suspend();
		is_recovery = true;
		if (host.isUp()) {
			createCheckpoint(host);
		} else {
			host.setFailState(Fallible.OK);
		}
		log.info("Node " + host.getID() + " : start recovering (" + states.size() + " checkpoints) last state = "
				+ states.peek());

		nb_remaining_received_rollback = Network.size() - 1;
		send_rollback_messages(host);

	}

	private void send_rollback_messages(Node host) {
		log.fine("Node " + host.getID() + " : start round " + (idround++));
		should_continue_rollback = false;// si ceci reste faux alors on arretra
											// le rollback
		Transport t = (Transport) host.getProtocol(transport);
		for (int j = 0; j < Network.size(); j++) {
			if (j != host.getIndex()) {
				long id_dest = Network.get(j).getID();
				int nb_sent = saved_sent.peek().get(id_dest);
				t.send(host, Network.get(j), new RollBackMessage(host.getID(), id_dest, protocol_id, nb_sent),
						protocol_id);
			}
		}
		nb_remaining_received_ack_rollback = Network.size() - 1;
	}

	private void receiveRollBackMessage(Node host, RollBackMessage rbmess) {
		log.fine("Node " + host.getID() + " : receive RollBackMessage from " + rbmess.getIdSrc());
		log.fine("Node " + host.getID() + " : (" + states.size() + " checkpoints)" + "  state = " + states.peek()
				+ " sent = " + saved_sent.peek() + " rcvd = " + saved_rcvd.peek());

		nb_remaining_received_rollback--;
		int nb_recv = saved_rcvd.peek().get(rbmess.getIdSrc());
		while (nb_recv > rbmess.getNbSent()) {
			delete_checkpoint();
			should_continue_rollback = true;
			log.fine("Node " + host.getID() + " : delete checkpoint because node " + rbmess.getIdSrc() + " has sent "
					+ rbmess.getNbSent() + " messages to I but I receive " + nb_recv + " messages from "
					+ rbmess.getIdSrc());
			log.fine("Node " + host.getID() + " : find last checkpoint (" + states.size() + " checkpoints)"
					+ "  state = " + states.peek() + " sent = " + saved_sent.peek() + " rcvd = " + saved_rcvd.peek());
			nb_recv = saved_rcvd.peek().get(rbmess.getIdSrc());
		}

		if (nb_remaining_received_rollback == 0) {

			nb_remaining_received_rollback = Network.size() - 1;

			// une fois que l'on a recu tous les messages de rollbacks , il faut
			// repondre à
			// tous si on a fait un rollback ou pas
			Transport t = (Transport) host.getProtocol(transport);
			for (int i = 0; i < Network.size(); i++) {
				Node dest = Network.get(i);
				if (dest.getID() != host.getID()) {
					t.send(host, dest,
							new AckRollBackMessage(host.getID(), dest.getID(), protocol_id, should_continue_rollback),
							protocol_id);
					log.fine("Node " + host.getID() + " : send AckRollBackMessage to " + dest.getID() + " ("
							+ should_continue_rollback + ")");
				}
			}

		}
	}

	private void receiveAckRollBackMessage(Node host, AckRollBackMessage mess) {
		log.fine("Node " + host.getID() + " : receive AckRollBackMessage from " + mess.getIdSrc() + " ("
				+ mess.should_contine_rollback() + ")");
		nb_remaining_received_ack_rollback--;
		should_continue_rollback = should_continue_rollback || mess.should_contine_rollback();
		if (nb_remaining_received_ack_rollback == 0) {// on a recu tous les acks
			if (should_continue_rollback) {// il y a au moins un noeud qui a
											// fait
											// un rollback
				send_rollback_messages(host);// on refait un round du coup
			} else {
				findMessagesToReplay(host);// on a trouvé la ligne de
											// recouvrement, on passe à la phase
											// de rejeu des
											// messages
			}
		}
	}

	private void delete_checkpoint() {
		states.pop();
		saved_sent.pop();
		saved_rcvd.pop();
		saved_sent_messages.pop();

	}

	private void findMessagesToReplay(Node host) {
		Transport t = (Transport) host.getProtocol(transport);
		nb_remaining_replyrecovery = Network.size() - 1;
		message_to_replay_after_recovery.clear();
		for (int i = 0; i < Network.size(); i++) {
			Node dest = Network.get(i);
			if (dest.getID() != host.getID()) {
				t.send(host, dest, new AskMissingMessMessage(host.getID(), dest.getID(), protocol_id,
						saved_rcvd.peek().get(dest.getID())), protocol_id);
			}

		}
	}

	private void receiveAskMissingMessMessage(Node host, AskMissingMessMessage amess) {
		log.fine("Node " + host.getID() + " receive AskMissingMessMessage from " + amess.getIdSrc());
		Transport t = (Transport) host.getProtocol(transport);
		int nb_sent = this.saved_sent.peek().get(amess.getIdSrc());
		int nb_rcv = amess.getNbRcv();
		if (nb_rcv > nb_sent) {
			throw new RuntimeException("Error : inconcistency in cover line : ( Node " + host.getID() + " : node "
					+ amess.getIdSrc() + " received " + nb_rcv + " messages from I but I sent " + nb_sent);
		}
		List<WrappingMessage> missing_mess = new ArrayList<>();
		if (nb_rcv < nb_sent) {
			int nb_missing = nb_sent - nb_rcv;
			Stack<Map<Long, List<WrappingMessage>>> tmp = new Stack<>();
			;
			do {
				List<WrappingMessage> l = this.saved_sent_messages.peek().get(amess.getIdSrc());
				while (l == null) {
					tmp.push(saved_sent_messages.pop());
					l = this.saved_sent_messages.peek().get(amess.getIdSrc());
				}
				int debut = Math.max(0, l.size() - nb_missing);
				for (int i = debut; i < l.size(); i++) {
					missing_mess.add(l.get(i));
					nb_missing--;
				}
			} while (nb_missing > 0);

			while (!tmp.isEmpty()) {
				saved_sent_messages.push(tmp.pop());
			}
		}

		for (int i = 0; i < Network.size(); i++) {
			Node dest = Network.get(i);
			if (dest.getID() == amess.getIdSrc()) {
				t.send(host, dest,
						new ReplyAskMissingMessMessage(host.getID(), amess.getIdSrc(), protocol_id, missing_mess),
						protocol_id);
				break;
			}
		}

	}

	private void receiveReplyAskMissingMessMessage(Node host, ReplyAskMissingMessMessage reply) {
		nb_remaining_replyrecovery--;
		this.message_to_replay_after_recovery.addAll(reply.getMissingMessages());
		if (nb_remaining_replyrecovery == 0) {
			stop_recover(host);
		}
	}

	private void stop_recover(Node host) {
		Checkpointable chk = (Checkpointable) host.getProtocol(checkpointable_id);
		chk.resume();
		is_recovery = false;
		this.sent = new HashMap<>(saved_sent.peek());
		this.rcvd = new HashMap<>(saved_rcvd.peek());
		this.sent_messages.clear();
		log.info("Node " + host.getID() + " : end recovering (recover from checkpoint " + states.size() + ")"
				+ "  state = " + states.peek() + " nb reply messages = " + message_to_replay_after_recovery.size() + " "
				+ message_to_replay_after_recovery);
		chk.restoreState(states.peek());
		for (WrappingMessage wm : message_to_replay_after_recovery) {
			receiveWrappingMessage(host, wm);
		}

	}

	////////////////////////////////////////// Fin des methodes de recouvrement

	//////////////////// Methode simulant le code suivant
	// while(true){
	// ...if(!is_recovery) createCheckpoint(...);
	// ...s'endormir en moyenne timecheckpointing unité de temps
	// }

	public void loop(Node host) {
		if (CommonState.r.nextInt() % 2 == 0 && !is_recovery) {
			createCheckpoint(host);
		}
		next_turn(host);
		return;
	}

	private void next_turn(Node host) {
		long res = MyRandom.nextLong(timecheckpointing, 0.1);
		EDSimulator.add(res, "loop", host, protocol_id);
	}

	////////////////////////////////////////////////// Classes des messages

	public static class AskMissingMessMessage extends Message {

		private final int nbrcv;

		public int getNbRcv() {
			return nbrcv;
		}

		public AskMissingMessMessage(long idsrc, long iddest, int pid, int nbrcv) {
			super(idsrc, iddest, pid);
			this.nbrcv = nbrcv;
		}

	}

	public static class AckRollBackMessage extends Message {

		private final boolean nack;

		public AckRollBackMessage(long idsrc, long iddest, int pid, boolean nack) {
			super(idsrc, iddest, pid);
			this.nack = nack;
		}

		public boolean should_contine_rollback() {
			return nack;
		}

	}

	public static class ReplyAskMissingMessMessage extends Message {

		private final List<WrappingMessage> missingmessages;

		public ReplyAskMissingMessMessage(long idsrc, long iddest, int pid, List<WrappingMessage> missingmessages) {
			super(idsrc, iddest, pid);
			this.missingmessages = missingmessages;

		}

		public List<WrappingMessage> getMissingMessages() {
			return missingmessages;
		}

	}

	public static class RollBackMessage extends Message {

		private final int nbsent;

		public int getNbSent() {
			return nbsent;
		}

		public RollBackMessage(long idsrc, long iddest, int pid, int nb_sent) {
			super(idsrc, iddest, pid);
			this.nbsent = nb_sent;
		}

	}

	public static class WrappingMessage extends Message {

		private final Message message;

		public Message getMessage() {
			return message;
		}

		public WrappingMessage(long idsrc, long iddest, int pid, Message mess) {
			super(idsrc, iddest, pid);
			this.message = mess;
		}

		@Override
		public String toString() {
			return "WrappingMessage( from=" + getIdSrc() + ", to = " + getIdDest() + "  mess = " + getMessage() + ")";
		}
	}
}
