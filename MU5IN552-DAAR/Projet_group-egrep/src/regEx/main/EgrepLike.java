package regEx.main;

import regEx.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class EgrepLike {
    public static void main(String[] args) {
        String regEx;
        String preFile;
        if (args.length != 0) {
            regEx = args[0];
            preFile = args[1];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  >> Please enter a regular expression: ");
            regEx = scanner.next();
            System.out.print("  >> Please enter the file name: ");
            preFile = scanner.next();
        }

        String file = "src/testRessources/" + preFile;
        // analyse the reg expression
        RegEx regEx1 = new RegEx();
        RegExTree tree = regEx1.toTree(regEx);  // transform the regEx into a tree

        Construction construction = new Construction();    // init the construction
        NonDetFiniAuto ndfa = construction.convert(tree);  // using Thompson construct algo to generate NFA
        DetFiniAuto dfa = construction.convertToDFA(ndfa);  // turn NFA into DFA
        DetFiniAuto minimizeDFA = construction.minimizeDFA(dfa); // turn DFA into mDFA

        // apply the mDFA to match the context in the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // match each line with the mDFA
                if (matchesDFA(minimizeDFA, line)) {
                    System.out.println(lineNumber + "：" + line);
                    // a print if there exist a match
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static boolean matchesDFA(DetFiniAuto dfa, String line) { // to check if a line is accepted by the DFA
        // Check each substring character by character
        for (int start = 0; start < line.length(); start++) {
            int currentState = dfa.startState;

            // Match character by character starting from the current position
            for (int i = start; i < line.length(); i++) {
                char c = line.charAt(i);

                // Get the transitions from the current state
                Map<Character, Integer> transitions = dfa.transitions.get(currentState);
                if (transitions == null || !transitions.containsKey(c)) {
                    break;  // Current substring does not match, check the next one
                }

                // Move to the next state
                currentState = transitions.get(c);

                // If the current state is an accepting state, return true for a successful match
                if (dfa.acceptStates.contains(currentState)) {
                    return true;
                }
            }
        }
        return false;  // No substring matched
    }

}
