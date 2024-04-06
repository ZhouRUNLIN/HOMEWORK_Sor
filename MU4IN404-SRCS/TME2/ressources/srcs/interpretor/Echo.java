package srcs.interpretor;

import java.io.PrintStream;
import java.util.List;

public class Echo implements Command{
    private final List<String> args;

    public Echo(List<String> args){
        if (args == null){
            throw new IllegalArgumentException("Echo Err : Arguments not exist");
        }
        this.args = args;
    }

    @Override
    public void execute(PrintStream out) {
        args.remove(0);
        out.print(String.join(" ", args));
    }
}
