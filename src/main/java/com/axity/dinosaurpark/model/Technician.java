package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.zone.PowerPlant;

/**
 * Trabajador encargado de reparar la planta de energia cuando falla.
 */
public class Technician extends Worker {

    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlant powerPlant) {
        if (powerPlant == null) {
            return;
        }

        if (!powerPlant.isOperational()) {
            powerPlant.repair();
        }
    }
}