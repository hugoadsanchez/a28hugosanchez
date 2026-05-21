package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;

/**
 * Punto de entrada de la simulacion del Parque Turistico de Dinosaurios.
 */
public class Main {

    public static void main(String[] args) {
        ParkConfig config = ParkConfig.getInstance();

        System.out.println("Iniciando simulacion del Parque Turistico de Dinosaurios...");
        System.out.println("Total de pasos configurados: " + config.getTotalSteps());
        System.out.println("Turistas por lote de llegada: " + config.getArrivalBatchSize());
        System.out.println("Semilla de simulacion: " + config.getSeed());
    }
}