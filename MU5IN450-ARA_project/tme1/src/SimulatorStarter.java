public class SimulatorStarter {
    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("Usage: java SimulatorStarter <config_file>");
//            System.exit(1);
//        }

        String configFileName = "config.txt";
        System.out.println("Loading configuration from: " + configFileName);
        try {
            peersim.Simulator.main(new String[] {configFileName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
