package TME1.automate;

import java.util.ArrayList;

public class NonDetFiniAuto {
    public int startState;
    public int acceptState;
    public ArrayList<Transition> transitions;

    public NonDetFiniAuto(int startState, int acceptState, ArrayList<Transition> transitions) {
        this.startState = startState;
        this.acceptState = acceptState;
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NDFA Representation:\n");
        sb.append("Start State: ").append(startState).append("\n");
        sb.append("Accept State: ").append(acceptState).append("\n");
        sb.append("Transitions:\n");

        for (Transition t : transitions) {
            sb.append("  State ").append(t.from)
                    .append(" --").append(t.symbol == 'ε' ? "ε" : t.symbol).append("--> ")
                    .append("State ").append(t.to).append("\n");
        }

        return sb.toString();
    }
}