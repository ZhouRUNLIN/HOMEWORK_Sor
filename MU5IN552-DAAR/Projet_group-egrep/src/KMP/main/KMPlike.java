package KMP.main;

import KMP.KPMalgo;

import java.net.MalformedURLException;
import java.util.Scanner;

public class KMPlike {
    public static void main(String[] args){
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

        String file = "src/testRessources/" + preFile;
        KPMalgo kpMalgo = new KPMalgo();
        try {
            kpMalgo.KPM(factor,file);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}