package org.rhw.bmr.project.common.algo.egreplike;

import java.util.Map;
import java.util.Set;

public class DetFiniAuto {
    public int startState;
    public Set<Integer> acceptStates;
    public Map<Integer, Map<Character, Integer>> transitions;

    public DetFiniAuto(int startState, Set<Integer> acceptStates, Map<Integer, Map<Character, Integer>> transitions) {
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DFA Representation:\n");
        sb.append("Start State: ").append(startState).append("\n");
        sb.append("Accept States: ").append(acceptStates).append("\n");
        sb.append("Transitions:\n");

        for (Map.Entry<Integer, Map<Character, Integer>> entry : transitions.entrySet()) {
            int fromState = entry.getKey();
            // Check if transitions from this state are null
            Map<Character, Integer> stateTransitions = entry.getValue();
            if (stateTransitions == null) continue;

            for (Map.Entry<Character, Integer> transition : stateTransitions.entrySet()) {
                char symbol = transition.getKey();
                // Check if the transition target state is null
                Integer toState = transition.getValue();
                if (toState == null) continue;
                sb.append(transition);
            }
        }
        return sb.toString();
    }
}
