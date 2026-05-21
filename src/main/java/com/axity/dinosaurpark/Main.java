package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.simulation.SimulationEngine;

/**
 * Punto de entrada de la simulacion del Parque Turistico de Dinosaurios.
 */
public class Main {

    public static void main(String[] args) {
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);

        engine.run();
    }
}