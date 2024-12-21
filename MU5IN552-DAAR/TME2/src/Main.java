import java.net.MalformedURLException;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws MalformedURLException {

        KPMalgo kpMalgo = new KPMalgo();

        String factor = "Chihuahua";


        //local file
        //String file = "src/Pony Tracks.txt";

        //net file
        String file = "https://gutenberg.org/files/41011/41011-0.txt";

        String factor2 = "Pepperoni";

        String factor3 = "Pizza";

        String factor4 = "Poppers";

        timeCalculation(kpMalgo,factor,file);

        timeCalculation(kpMalgo,factor2,file);

        timeCalculation(kpMalgo,factor3,file);

        timeCalculation(kpMalgo,factor4,file);
    }

    private static void timeCalculation(KPMalgo kpMalgo,String factor,String file) throws MalformedURLException {

        long startTime = System.nanoTime();

        kpMalgo.KPM(factor,file);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000;

        System.out.println("\"" + factor + "\" " + "execution Time: " + duration + " ms");

    }
}