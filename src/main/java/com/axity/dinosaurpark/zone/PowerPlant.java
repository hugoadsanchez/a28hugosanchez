package com.axity.dinosaurpark.zone;

/**
 * Representa la planta de energia del parque.
 *
 * Esta primera version es sencilla para poder conectar a los tecnicos.
 * Mas adelante se agregara consumo de energia, fallas y costos.
 */
public class PowerPlant {

    private double energyLevel;
    private boolean operational;

    public PowerPlant(double initialEnergy) {
        this.energyLevel = initialEnergy;
        this.operational = true;
    }

    public double getEnergyLevel() {
        return energyLevel;
    }

    public boolean isOperational() {
        return operational;
    }

    public void triggerFailure() {
        this.energyLevel = 0.0;
        this.operational = false;
    }

    public void repair() {
        this.energyLevel = 100.0;
        this.operational = true;
    }
}