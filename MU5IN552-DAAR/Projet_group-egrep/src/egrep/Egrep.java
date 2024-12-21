package egrep;

import java.util.Scanner;

public class Egrep {
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
        // We place the documents through this rep
        String file = "src/testRessources/" + preFile;
        try {
            String[] command = {"/bin/sh", "-c", "egrep" + regEx + file + "> /dev/null 2>&1"};
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


