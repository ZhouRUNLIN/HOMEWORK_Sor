package ara.projet.ee1;

import ara.projet.mutex.NaimiTrehelAlgo;
import peersim.Simulator;

public class SimulationLauncher {

    public static void main(String[] args) {
        String configFileName = "naimi_trehel.cfg";

        try {
                Simulator.main(new String[]{configFileName});
                NaimiTrehelAlgo.printStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
