package srcs.interpretor;

import java.io.PrintStream;

public interface Command {
    void execute(PrintStream out);
}
