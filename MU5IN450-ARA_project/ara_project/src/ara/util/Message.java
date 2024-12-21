package ara.util;

/**
 * @author jonathan.lejeune@lip6.fr
 *
 */
public abstract class Message {

	private final long idsrc;
	private final long iddest;
	private final int pid;

	public long getIdSrc() {
		return idsrc;
	}

	public long getIdDest() {
		return iddest;
	}

	public int getPid() {
		return pid;
	}

	public Message(long idsrc, long iddest, int pid) {
		this.iddest = iddest;
		this.idsrc = idsrc;
		this.pid = pid;
	}
}
