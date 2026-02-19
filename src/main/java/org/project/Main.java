package org.project;


import java.io.FileNotFoundException;

public class Main {
    static void main(String[] args) throws FileNotFoundException {
        // Get program arguments
        String inputFileName = args[0];
        String outputFileName = args[1];

        // Start simulation
        Simulation simulation = new Simulation(
                inputFileName,
                outputFileName
        );
    }
}
