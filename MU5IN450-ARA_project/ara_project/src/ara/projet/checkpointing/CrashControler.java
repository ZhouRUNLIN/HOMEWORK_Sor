package ara.projet.checkpointing;

import static ara.util.Constantes.log;

import java.util.ArrayList;
import java.util.List;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Fallible;
import peersim.core.Network;
import peersim.core.Node;

public class CrashControler implements Control {

	private static final String PAR_FAULTYNODES = "faulty_nodes";
	private static final String PAR_PROBACRASH = "probacrash";
	private static final String PAR_CHECKPOINTER = "checkpointer";

	private final List<Long> faulty_nodes;
	private final double probacrash;
	private final int checkpointer_id;

	public CrashControler(String prefix) {
		String tmp = Configuration.getString(prefix + "." + PAR_FAULTYNODES, "");
		faulty_nodes = new ArrayList<>();
		if (tmp != "") {
			for (String s : tmp.split("_")) {
				faulty_nodes.add(Long.parseLong(s));
			}

		}
		probacrash = Configuration.getDouble(prefix + "." + PAR_PROBACRASH);
		checkpointer_id = Configuration.getPid(prefix + "." + PAR_CHECKPOINTER, -1);
	}

	@Override
	public boolean execute() {
		boolean panne = false;
		for (int i = 0; i < Network.size(); i++) {
			if (faulty_nodes.contains(Long.valueOf(i))) {
				if (CommonState.r.nextDouble() <= probacrash) {
					// i tombe en panne
					Node n = Network.get(i);
					if (n.getFailState() == Fallible.OK) {
						n.setFailState(Fallible.DOWN);
						log.info(n.getID() + " EST EN PANNE !!!!!!");
						panne = true;
						break;
					}
				}
			}
		}

		if (panne && checkpointer_id != -1) {
			for (int i = 0; i < Network.size(); i++) {
				Node n = Network.get(i);
				Checkpointer c = (Checkpointer) n.getProtocol(checkpointer_id);
				c.recover(n);
			}

		}

		return false;
	}

}
