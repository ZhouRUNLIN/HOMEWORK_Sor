import KMP.main.KMPlike;
import regEx.main.EgrepLike;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class main {

    public static void main(String[] args) {
        String factor;
        String preFile;
        if (args.length != 0) {
            factor = args[0];
            preFile = args[1];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  >> Please enter a regular expression: ");
            factor = scanner.next();
            System.out.print("  >> Please enter the file name: ");
            preFile = scanner.next();
        }

        String[] argsSearch = {factor,preFile};

        try {
            Pattern.compile(factor);

            if (isRegex(factor)){
                EgrepLike.main(argsSearch);
            }else {
                if (isOtherRegex(factor)){
                    System.out.println("This regular expression is not yet implemented.");
                }else {
                    KMPlike.main(argsSearch);
                }
            }

        } catch (PatternSyntaxException e) {
            System.out.println("The input does not match the regular expression.");
        }

    }

    public static boolean isRegex(String input) {
        String basicRegexFeatures = ".*[()|*+\\.].*";
        return input.matches(basicRegexFeatures);
    }


    public static boolean isOtherRegex(String input) {
        String nonComplexRegex = "^[^()|*+\\.]*$";
        return input.matches(nonComplexRegex);
    }

}
