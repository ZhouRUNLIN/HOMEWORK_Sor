package TME1.test;

import TME1.RegEx;
import TME1.RegExTree;
import TME1.SubsetConstruction;
import TME1.ThompsonConstruction;
import TME1.automate.DetFiniAuto;
import TME1.automate.NonDetFiniAuto;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
        String regEx;

        if (args.length != 0) {
            regEx = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  >> Please enter a regEx: ");
            regEx = scanner.next();
        }

        RegEx regEx1 = new RegEx();
        System.out.println(regEx1.toTree(regEx).toString());

        RegExTree tree =  regEx1.toTree(regEx);// 构造或解析正则表达式树
        ThompsonConstruction tc = new ThompsonConstruction();
        NonDetFiniAuto ndfa = tc.convert(tree);
        System.out.println(ndfa.toString());


        SubsetConstruction subsetConstruction = new SubsetConstruction();
        DetFiniAuto dfa = subsetConstruction.convertToDFA(ndfa);

        // Print the resulting DFA
        System.out.println(dfa.toString());

        DetFiniAuto minimizedDFA = subsetConstruction.minimizeDFA(dfa);
        System.out.println(minimizedDFA.toString());
    }
}
