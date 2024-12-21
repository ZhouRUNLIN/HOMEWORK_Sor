package regEx;

import java.util.*;

public class Construction {
    private int nextDFAState = 0;
    private int stateCount = 0;
    public static final char ANY = (char) -1;   // Define a constant for ANY character

    // Main method to convert NDFA to DFA
    public DetFiniAuto convertToDFA(NonDetFiniAuto ndfa) {
        Map<Set<Integer>, Integer> dfaStateMap = new HashMap<>();
        Queue<Set<Integer>> unprocessedStates = new LinkedList<>();
        Map<Integer, Map<Character, Integer>> dfaTransitions = new HashMap<>();
        Set<Integer> dfaAcceptStates = new HashSet<>();

        // Step 1: obtain the alphabet in ndfa
        Set<Character> alphabet = extractAlphabet(ndfa);

        // Step 2: Compute ε-closure for NDFA start state
        Set<Integer> startStateSet = epsilonClosure(ndfa, Collections.singleton(ndfa.startState));
        int startDFAState = nextDFAState++;
        dfaStateMap.put(startStateSet, startDFAState);
        unprocessedStates.add(startStateSet);

        // Step 3: Process each set of NDFA states (representing DFA state)
        while (!unprocessedStates.isEmpty()) {
            Set<Integer> currentStateSet = unprocessedStates.poll();
            int currentDFAState = dfaStateMap.get(currentStateSet);
            dfaTransitions.putIfAbsent(currentDFAState, new HashMap<>());

            // Step 4: Process each character symbol in the alphabet
            for (char symbol : alphabet) {
                Set<Integer> nextStateSet = move(ndfa, currentStateSet, symbol);
                nextStateSet = epsilonClosure(ndfa, nextStateSet); // Get ε-closure

                if (!nextStateSet.isEmpty()) {
                    if (!dfaStateMap.containsKey(nextStateSet)) {
                        int newDFAState = nextDFAState++;
                        dfaStateMap.put(nextStateSet, newDFAState);
                        unprocessedStates.add(nextStateSet);
                    }
                    int nextDFAState = dfaStateMap.get(nextStateSet);
                    dfaTransitions.get(currentDFAState).put(symbol, nextDFAState);
                }
            }

            // Step 5: Process ANY transitions
            Set<Integer> anyNextStateSet = move(ndfa, currentStateSet, ANY);
            anyNextStateSet = epsilonClosure(ndfa, anyNextStateSet);

            if (!anyNextStateSet.isEmpty()) {
                if (!dfaStateMap.containsKey(anyNextStateSet)) {
                    int newDFAState = nextDFAState++;
                    dfaStateMap.put(anyNextStateSet, newDFAState);
                    unprocessedStates.add(anyNextStateSet);
                }
                int anyDFAState = dfaStateMap.get(anyNextStateSet);

                // For all possible symbols (e.g., ASCII characters)
                for (char c = 32; c <= 126; c++) {
                    // If this symbol doesn't already have a transition from currentDFAState
                    if (!dfaTransitions.get(currentDFAState).containsKey(c)) {
                        dfaTransitions.get(currentDFAState).put(c, anyDFAState);
                    }
                }
            }
        }

        // Step 6: Identify accept states
        for (Map.Entry<Set<Integer>, Integer> entry : dfaStateMap.entrySet()) {
            if (entry.getKey().contains(ndfa.acceptState)) {
                dfaAcceptStates.add(entry.getValue());
            }
        }

        return new DetFiniAuto(startDFAState, dfaAcceptStates, dfaTransitions);
    }
    // ε-Closure: Computes the set of NDFA states reachable by ε-transitions
    private Set<Integer> epsilonClosure(NonDetFiniAuto ndfa, Set<Integer> states) {
        Set<Integer> closure = new HashSet<>(states);
        Stack<Integer> stack = new Stack<>();
        stack.addAll(closure);

        while (!stack.isEmpty()) {
            int state = stack.pop();
            for (Transition t : ndfa.transitions) {
                if (t.from == state && t.symbol == 'ε' && !closure.contains(t.to)) {
                    closure.add(t.to);
                    stack.push(t.to);
                }
            }
        }
        return closure;
    }

    // Move: Computes the set of states reachable from a set of states by a given symbol
    private Set<Integer> move(NonDetFiniAuto ndfa, Set<Integer> states, char symbol) {
        Set<Integer> result = new HashSet<>();
        for (int state : states) {
            for (Transition t : ndfa.transitions) {
                if (t.from == state && (t.symbol == symbol || t.symbol == ANY)) {
                    result.add(t.to);
                }
            }
        }
        return result;
    }

    // Extracts the alphabet (excluding ε and ANY symbol)
    private Set<Character> extractAlphabet(NonDetFiniAuto ndfa) {
        Set<Character> alphabet = new HashSet<>();
        for (Transition t : ndfa.transitions) {
            if (t.symbol != 'ε' && t.symbol != ANY) {  // Exclude ε and ANY symbol
                alphabet.add(t.symbol);
            }
        }
        return alphabet;
    }


    public DetFiniAuto minimizeDFA(DetFiniAuto dfa) {
        // Initialize a set containing all non-accepting states
        Set<Integer> nonAcceptStates = new HashSet<>(dfa.transitions.keySet());
        nonAcceptStates.removeAll(dfa.acceptStates);

        // P is a list of state sets (partitions of the DFA)
        // W is a queue used to process partitions during Hopcroft's algorithm
        List<Set<Integer>> P = new ArrayList<>();
        Queue<Set<Integer>> W = new LinkedList<>();

        // Divide the states into accepting and non-accepting sets, and add them to P and W
        if (!dfa.acceptStates.isEmpty()) {
            P.add(dfa.acceptStates);
            W.add(dfa.acceptStates);
        }
        if (!nonAcceptStates.isEmpty()) {
            P.add(nonAcceptStates);
            W.add(nonAcceptStates);
        }

        // Get the alphabet from the DFA transitions
        Set<Character> alphabet = new HashSet<>();
        for (Map<Character, Integer> trans : dfa.transitions.values()) {
            alphabet.addAll(trans.keySet());
        }

        // Start the Hopcroft's algorithm to minimize the DFA
        while (!W.isEmpty()) {
            // Get and remove a set A from W
            Set<Integer> A = W.poll();

            // For each symbol in the alphabet
            for (char c : alphabet) {
                // Calculate Pre(A), the set of states that can transition to A on symbol c
                Set<Integer> PreA = new HashSet<>();
                for (Integer s : dfa.transitions.keySet()) {
                    Integer t = dfa.transitions.get(s).get(c);
                    if (t != null && A.contains(t)) {
                        PreA.add(s);
                    }
                }

                // Create a new partition set based on Pre(A)
                List<Set<Integer>> newP = new ArrayList<>();
                for (Set<Integer> Y : P) {
                    // Split Y into two sets: intersection and difference with PreA
                    Set<Integer> intersection = new HashSet<>(Y);
                    intersection.retainAll(PreA);

                    Set<Integer> difference = new HashSet<>(Y);
                    difference.removeAll(PreA);

                    // If both intersection and difference exist, split Y
                    if (!intersection.isEmpty() && !difference.isEmpty()) {
                        newP.add(intersection);
                        newP.add(difference);

                        // Update W if Y is part of it
                        if (W.contains(Y)) {
                            W.remove(Y);
                            W.add(intersection);
                            W.add(difference);
                        } else {
                            // Add the smaller of intersection and difference to W
                            if (intersection.size() <= difference.size()) {
                                W.add(intersection);
                            } else { W.add(difference); }
                        }
                    } else { newP.add(Y); } // If no split occurs, keep Y as it is
                }
                P = newP; // Update the partition P
            }
        }

        // Map old states to new states for minimized DFA
        Map<Integer, Integer> stateMapping = new HashMap<>();
        Map<Set<Integer>, Integer> groupMapping = new HashMap<>();
        int newStateId = 0;

        // Sort the partitions P to ensure deterministic state assignments
        P.sort(Comparator.comparingInt(set -> set.stream().min(Integer::compareTo).orElse(Integer.MAX_VALUE)));

        // Assign a new state ID to each state group in P
        for (Set<Integer> group : P) {
            List<Integer> sortedStates = new ArrayList<>(group);
            sortedStates.sort(Integer::compareTo);

            // Map each state in the group to the new minimized state
            for (Integer state : sortedStates) {
                stateMapping.put(state, newStateId);
            }
            newStateId++;
        }

        // Construct the transition function for the minimized DFA
        Map<Integer, Map<Character, Integer>> minimizedTransitions = new HashMap<>();
        for (Integer oldState : dfa.transitions.keySet()) {
            int newState = stateMapping.get(oldState);
            minimizedTransitions.putIfAbsent(newState, new HashMap<>());

            for (Map.Entry<Character, Integer> entry : dfa.transitions.get(oldState).entrySet()) {
                char symbol = entry.getKey();
                int oldTarget = entry.getValue();
                int newTarget = stateMapping.get(oldTarget);
                minimizedTransitions.get(newState).put(symbol, newTarget);
            }
        }

        // Determine the new set of accepting states for the minimized DFA
        Set<Integer> minimizedAcceptStates = new HashSet<>();
        for (Integer acceptState : dfa.acceptStates) {
            minimizedAcceptStates.add(stateMapping.get(acceptState));
        }
        // Get the new start state for the minimized DFA
        int minimizedStartState = stateMapping.get(dfa.startState);
        return new DetFiniAuto(minimizedStartState, minimizedAcceptStates, minimizedTransitions);
    }

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
            case RegEx.PLUS:
                return plus(convert(tree.subTrees.get(0))); // 添加这一行
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

    // PLUS operation
    private NonDetFiniAuto plus(NonDetFiniAuto ndfa) {
        int start = nextState();
        int accept = nextState();
        ArrayList<Transition> transitions = new ArrayList<>();

        // ε-transition from new start to ndfa's start
        transitions.add(new Transition(start, ndfa.startState, 'ε'));
        // Copy transitions from ndfa
        transitions.addAll(ndfa.transitions);
        // ε-transition from ndfa's accept state back to ndfa's start state
        transitions.add(new Transition(ndfa.acceptState, ndfa.startState, 'ε'));
        // ε-transition from ndfa's accept state to new accept state
        transitions.add(new Transition(ndfa.acceptState, accept, 'ε'));

        return new NonDetFiniAuto(start, accept, transitions);
    }

    // DOT operation (matches any character)
    private NonDetFiniAuto dot() {

        int start = nextState();
        int accept = nextState();
        ArrayList<Transition> transitions = new ArrayList<>();
        transitions.add(new Transition(start, accept, ANY));
        return new NonDetFiniAuto(start, accept, transitions);
    }
}
