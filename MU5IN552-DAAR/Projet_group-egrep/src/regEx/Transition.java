package regEx;

public class Transition {
    public int from;
    public int to;
    public char symbol;

    public Transition(int from, int to, char symbol) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Transition created: State " + from + " --" + (symbol == 'ε' ? "ε" : symbol) + "--> State " + to;
    }
}
