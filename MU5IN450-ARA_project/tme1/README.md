find src -name "*.java" | xargs javac -cp $PWD/peersim-1.0.5/peersim-1.0.5.jar:$PWD/peersim-1.0.5/djep-1.0.0.jar:/home/weida/peersim/peersim-1.0.5/jep-2.3.0.jar -d out

java -cp .:out:$PWD/peersim-1.0.5/peersim-1.0.5.jar:$PWD/peersim-1.0.5/djep-1.0.0.jar:/home/weida/peersim/peersim-1.0.5/jep-2.3.0.jar SimulatorStarter src/config.txt 
