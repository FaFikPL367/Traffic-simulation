package org.project;

import java.io.FileNotFoundException;

public class Main {
    static void main(String[] args) throws FileNotFoundException {
        runConsoleMode(args);
    }

    private static void runConsoleMode(String[] args) throws FileNotFoundException {
        // Check number of arguments
        if (args.length < 2) {
            System.out.println("Number of arguments is wrong. Need to pass input file name and output file name!");
            return;
        }

        // Get program arguments
        String inputFileName = args[0];
        String outputFileName = args[1];

        // Start simulation
        Simulation simulation = new Simulation(
                inputFileName,
                outputFileName
        );

        // Run simulation
        simulation.runSimulation();
    }
}
