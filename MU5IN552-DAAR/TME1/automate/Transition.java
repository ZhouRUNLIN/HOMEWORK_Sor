package TME1.automate;

public class Transition {
    public int from;
    public int to;
    public char symbol;

    public Transition(int from, int to, char syboml) {
        this.from = from;
        this.to = to;
        this.symbol = syboml;
    }
}
