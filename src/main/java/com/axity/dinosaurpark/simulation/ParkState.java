package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.PowerPlant;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda el estado principal del parque durante la simulacion.
 *
 * Los eventos usan esta clase para consultar turistas, dinosaurios,
 * planta de energia y persistencia.
 */
public class ParkState {

    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final PowerPlant powerPlant;
    private final CsvWriter csvWriter;

    private long currentStep;
    private double totalRevenue;
    private double totalExpenses;

    public ParkState(
            List<Tourist> tourists,
            List<Dinosaur> dinosaurs,
            PowerPlant powerPlant,
            CsvWriter csvWriter
    ) {
        this.tourists = new ArrayList<>(tourists);
        this.dinosaurs = new ArrayList<>(dinosaurs);
        this.powerPlant = powerPlant;
        this.csvWriter = csvWriter;
        this.currentStep = 0;
        this.totalRevenue = 0.0;
        this.totalExpenses = 0.0;
    }

    public List<Tourist> getTourists() {
        return tourists;
    }

    public List<Dinosaur> getDinosaurs() {
        return dinosaurs;
    }

    public PowerPlant getPowerPlant() {
        return powerPlant;
    }

    public CsvWriter getCsvWriter() {
        return csvWriter;
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(long currentStep) {
        this.currentStep = currentStep;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void addRevenue(double amount) {
        if (amount > 0) {
            totalRevenue += amount;
        }
    }

    public void addExpense(double amount) {
        if (amount > 0) {
            totalExpenses += amount;
        }
    }

    public List<Tourist> getActiveTourists() {
        return tourists.stream()
                .filter(tourist -> tourist.getStatus() == TouristStatus.IN_PARK)
                .toList();
    }

    public long countActiveTourists() {
        return getActiveTourists().size();
    }

    public long countDinosaursInEnclosure() {
        return dinosaurs.stream()
                .filter(dinosaur -> !dinosaur.isEscaped())
                .count();
    }
}