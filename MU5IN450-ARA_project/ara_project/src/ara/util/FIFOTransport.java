package ara.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import peersim.config.Configuration;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

/**
 * @author jonathan.lejeune@lip6.fr
 *
 */

public class FIFOTransport implements Transport, EDProtocol {

	private static final String PAR_TRANSPORT = "fifo_transport";

	private final int transport;
	private final int protocol_id;

	private Map<Long, Integer> sending_cpt;
	private Map<Long, Integer> next_expected_num_seq;
	private List<FIFOMessage> pendMsg;

	public FIFOTransport(String prefix) {
		String tmp[] = prefix.split("\\.");
		protocol_id = Configuration.lookupPid(tmp[tmp.length - 1]);
		transport = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
	}

	@Override
	public Object clone() {

		FIFOTransport res = null;
		try {
			res = (FIFOTransport) super.clone();
			res.sending_cpt = new HashMap<Long, Integer>();
			res.next_expected_num_seq = new HashMap<Long, Integer>();
			res.pendMsg = new ArrayList<FIFOMessage>();
		} catch (CloneNotSupportedException e) {
		} // never happens
		return res;
	}

	@Override
	public void send(Node src, Node dest, Object msg, int pid) {

		if (!(msg instanceof Message)) {
			throw new IllegalArgumentException("msg must be instance of Message");
		}

		Transport t = (Transport) src.getProtocol(transport);
		if (!this.sending_cpt.containsKey(dest.getID())) {
			this.sending_cpt.put(dest.getID(), 0);
		}
		int numseq = sending_cpt.get(dest.getID());
		t.send(src, dest, new FIFOMessage(src.getID(), dest.getID(), protocol_id, (Message) msg, numseq), protocol_id);
		sending_cpt.put(dest.getID(), numseq + 1);
	}

	@Override
	public long getLatency(Node src, Node dest) {
		Transport t = (Transport) src.getProtocol(transport);
		return t.getLatency(src, dest);
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {
		if (protocol_id != pid) {
			throw new RuntimeException("Receive an event for wrong protocol");
		}
		if (event instanceof FIFOMessage) {
			FIFOMessage fifomess_received = (FIFOMessage) event;
			pendMsg.add(fifomess_received);
			long id_sender = fifomess_received.getIdSrc();
			if (!next_expected_num_seq.containsKey(id_sender)) {
				next_expected_num_seq.put(fifomess_received.getIdSrc(), 0);
			}
			boolean fini = false;
			while (!fini) {
				fini = true;
				int i = 0;
				for (FIFOMessage fifomess : pendMsg) {

					if (fifomess.getIdSrc() == id_sender) {
						int next = next_expected_num_seq.get(id_sender);
						if (fifomess.getNumseq() == next) {

							Message content = fifomess.getMessage();

							((EDProtocol) node.getProtocol(content.getPid())).processEvent(node, content.getPid(),
									content);

							next_expected_num_seq.put(id_sender, next + 1);
							pendMsg.remove(i);
							fini = false;
							break;
						}
					}
					i++;
				}

			}

		} else {
			throw new RuntimeException("Receive unknown type event");
		}

	}

	private static class FIFOMessage extends Message {

		private final int numseq;
		private final Message message;

		public int getNumseq() {
			return numseq;
		}

		public Message getMessage() {
			return message;
		}

		public FIFOMessage(long idsrc, long iddest, int pid, Message message, int numseq) {
			super(idsrc, iddest, pid);
			this.message = message;
			this.numseq = numseq;

		}
	}

}
