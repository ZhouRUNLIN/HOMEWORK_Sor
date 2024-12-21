package TME1;

import TME1.automate.DetFiniAuto;
import TME1.automate.NonDetFiniAuto;
import TME1.automate.Transition;

import java.util.*;

public class SubsetConstruction {

    private int nextDFAState = 0;

    // Main method to convert NDFA to DFA
    public DetFiniAuto convertToDFA(NonDetFiniAuto ndfa) {
        Map<Set<Integer>, Integer> dfaStateMap = new HashMap<>();
        Queue<Set<Integer>> unprocessedStates = new LinkedList<>();
        Map<Integer, Map<Character, Integer>> dfaTransitions = new HashMap<>();
        Set<Integer> dfaAcceptStates = new HashSet<>();

        // Step 1: Compute ε-closure for NDFA start state
        Set<Integer> startStateSet = epsilonClosure(ndfa, Collections.singleton(ndfa.startState));
        int startDFAState = nextDFAState++;
        dfaStateMap.put(startStateSet, startDFAState);
        unprocessedStates.add(startStateSet);

        // Step 2: Process each set of NDFA states (representing DFA state)
        while (!unprocessedStates.isEmpty()) {
            Set<Integer> currentStateSet = unprocessedStates.poll();
            int currentDFAState = dfaStateMap.get(currentStateSet);
            dfaTransitions.putIfAbsent(currentDFAState, new HashMap<>());

            // Step 3: Process each character symbol
            for (char symbol = 'a'; symbol <= 'z'; symbol++) {
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

            // Step 4: Check if this state set contains an NDFA accept state
            if (currentStateSet.contains(ndfa.acceptState)) {
                dfaAcceptStates.add(currentDFAState);
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
                if (t.from == state && t.symbol == symbol) {
                    result.add(t.to);
                }
            }
        }
        return result;
    }

    public DetFiniAuto minimizeDFA(DetFiniAuto dfa) {
        Set<Integer> nonAcceptStates = new HashSet<>(dfa.transitions.keySet());
        nonAcceptStates.removeAll(dfa.acceptStates);

        List<Set<Integer>> partition = new ArrayList<>();
        partition.add(dfa.acceptStates);   // Add accept states partition
        partition.add(nonAcceptStates);    // Add non-accept states partition

        Queue<Set<Integer>> workQueue = new LinkedList<>(partition);

        // Step 2: Refinement of partition
        while (!workQueue.isEmpty()) {
            Set<Integer> A = workQueue.poll();

            for (char symbol = 'a'; symbol <= 'z'; symbol++) { // Iterate over all possible symbols
                List<Set<Integer>> newPartition = new ArrayList<>();  // 临时存储新的分区
                List<Set<Integer>> toAddToQueue = new ArrayList<>();  // 临时存储需要加入队列的新组

                // Step 3: Split the states based on their transition on the symbol
                for (Set<Integer> group : partition) {
                    Set<Integer> intersect = new HashSet<>();
                    Set<Integer> difference = new HashSet<>(group);

                    for (Integer state : group) {
                        int target = dfa.transitions.getOrDefault(state, Collections.emptyMap()).getOrDefault(symbol, -1);
                        if (A.contains(target)) {
                            intersect.add(state);
                        }
                    }
                    difference.removeAll(intersect);

                    if (!intersect.isEmpty() && !difference.isEmpty()) {
                        newPartition.add(intersect);
                        newPartition.add(difference);

                        if (!workQueue.contains(intersect)) {
                            toAddToQueue.add(intersect);
                        }
                        if (!workQueue.contains(difference)) {
                            toAddToQueue.add(difference);
                        }
                    } else {
                        newPartition.add(group);  // 没有分割的组保持不变
                    }
                }

                partition = newPartition;  // 完成迭代后更新分区
                workQueue.addAll(toAddToQueue);  // 将新的组加入工作队列
            }
        }

        // Step 4: Rebuild minimized DFA
        Map<Integer, Integer> stateMapping = new HashMap<>();
        int newStateId = 0;

        for (Set<Integer> group : partition) {
            for (Integer state : group) {
                stateMapping.put(state, newStateId);
            }
            newStateId++;
        }

        // Step 5: Create minimized DFA transitions
        Map<Integer, Map<Character, Integer>> minimizedTransitions = new HashMap<>();
        for (Integer oldState : dfa.transitions.keySet()) {
            int newState = stateMapping.get(oldState);
            minimizedTransitions.putIfAbsent(newState, new HashMap<>());

            for (Map.Entry<Character, Integer> transition : dfa.transitions.get(oldState).entrySet()) {
                char symbol = transition.getKey();
                int targetState = transition.getValue();
                minimizedTransitions.get(newState).put(symbol, stateMapping.get(targetState));
            }
        }

        // Step 6: Determine minimized accept states
        Set<Integer> minimizedAcceptStates = new HashSet<>();
        for (Integer acceptState : dfa.acceptStates) {
            minimizedAcceptStates.add(stateMapping.get(acceptState));
        }

        int minimizedStartState = stateMapping.get(dfa.startState);

        return new DetFiniAuto(minimizedStartState, minimizedAcceptStates, minimizedTransitions);
    }
}
