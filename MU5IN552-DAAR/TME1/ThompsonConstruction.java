package TME1;

import TME1.automate.NonDetFiniAuto;
import TME1.automate.Transition;

import java.util.ArrayList;

public class ThompsonConstruction {

    private int stateCount = 0;

    // Generate a fresh state id
    private int nextState() {
        return stateCount++;
    }

    // Main method to convert RegExTree to NDFA
    public NonDetFiniAuto convert(RegExTree tree) {
        switch (tree.root) {
            case RegEx.CONCAT:
                return concat(convert(tree.subTrees.get(0)), convert(tree.subTrees.get(1)));
            case RegEx.ALTERN:
                return altern(convert(tree.subTrees.get(0)), convert(tree.subTrees.get(1)));
            case RegEx.ETOILE:
                return star(convert(tree.subTrees.get(0)));
            case RegEx.DOT:
                return dot();
            default:
                return base(tree.root); // Assumes this is a character (leaf node)
        }
    }

    // Create NDFA for a single character
    private NonDetFiniAuto base(int character) {
        int start = nextState();
        int accept = nextState();
        ArrayList<Transition> transitions = new ArrayList<>();
        transitions.add(new Transition(start, accept, (char) character));
        return new NonDetFiniAuto(start, accept, transitions);
    }

    // CONCAT operation
    private NonDetFiniAuto concat(NonDetFiniAuto ndfa1, NonDetFiniAuto ndfa2) {
        ArrayList<Transition> transitions = new ArrayList<>(ndfa1.transitions);
        transitions.addAll(ndfa2.transitions);
        // ε-transition from ndfa1's accept to ndfa2's start
        transitions.add(new Transition(ndfa1.acceptState, ndfa2.startState, 'ε'));
        return new NonDetFiniAuto(ndfa1.startState, ndfa2.acceptState, transitions);
    }

    // ALTERN operation
    private NonDetFiniAuto altern(NonDetFiniAuto ndfa1, NonDetFiniAuto ndfa2) {
        int start = nextState();
        int accept = nextState();
        ArrayList<Transition> transitions = new ArrayList<>();
        // ε-transition from new start to both ndfa1 and ndfa2's start
        transitions.add(new Transition(start, ndfa1.startState, 'ε'));
        transitions.add(new Transition(start, ndfa2.startState, 'ε'));
        // ε-transition from both ndfa1 and ndfa2's accept to new accept
        transitions.add(new Transition(ndfa1.acceptState, accept, 'ε'));
        transitions.add(new Transition(ndfa2.acceptState, accept, 'ε'));
        transitions.addAll(ndfa1.transitions);
        transitions.addAll(ndfa2.transitions);
        return new NonDetFiniAuto(start, accept, transitions);
    }

    // ETOILE operation
    private NonDetFiniAuto star(NonDetFiniAuto ndfa) {
        int start = nextState();
        int accept = nextState();
        ArrayList<Transition> transitions = new ArrayList<>();
        // ε-transition from new start to ndfa's start and to new accept
        transitions.add(new Transition(start, ndfa.startState, 'ε'));
        transitions.add(new Transition(start, accept, 'ε'));
        // ε-transition from ndfa's accept back to ndfa's start and to new accept
        transitions.add(new Transition(ndfa.acceptState, ndfa.startState, 'ε'));
        transitions.add(new Transition(ndfa.acceptState, accept, 'ε'));
        transitions.addAll(ndfa.transitions);
        return new NonDetFiniAuto(start, accept, transitions);
    }

    // DOT operation (matches any character)
    private NonDetFiniAuto dot() {
        int start = nextState();
        int accept = nextState();
        ArrayList<Transition> transitions = new ArrayList<>();
        transitions.add(new Transition(start, accept, '.')); // represents any character
        return new NonDetFiniAuto(start, accept, transitions);
    }
}
