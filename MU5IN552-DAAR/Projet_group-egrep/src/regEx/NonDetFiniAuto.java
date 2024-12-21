package regEx;

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

        for (Transition transition : transitions) {
            sb.append(transition);
        }
        return sb.toString();
    }
}
