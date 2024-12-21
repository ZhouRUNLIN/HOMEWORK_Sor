package ara.util;

import ara.util.Message;

import java.util.List;

public class HelloMessage extends Message {

    private final List<Integer> info;

    public HelloMessage(long idsrc, long iddest, int pid, List<Integer> info) {
        super(idsrc, iddest, pid);
        this.info = info;
    }

    public List<Integer> getInfo() {
        return info;
    }
}
