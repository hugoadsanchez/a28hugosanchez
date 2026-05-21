package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Representa la planta de energia del parque.
 *
 * Consume energia en cada paso de simulacion y puede fallar de forma
 * pseudoaleatoria. Tambien registra gastos y eventos importantes.
 */
public class PowerPlant {

    private static final String ZONE_NAME = "POWER_PLANT";

    private final double initialEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    private final double repairCost;

    private double energyLevel;
    private boolean operational;
    private long expenseSequence;

    public PowerPlant(
            double initialEnergy,
            double consumptionPerStep,
            double failureProbability,
            double maintenanceCost,
            double repairCost
    ) {
        this.initialEnergy = validatePositiveAmount(initialEnergy, "La energia inicial debe ser mayor a cero.");
        this.consumptionPerStep = validatePositiveAmount(consumptionPerStep, "El consumo debe ser mayor a cero.");
        this.failureProbability = validateProbability(failureProbability);
        this.maintenanceCost = validatePositiveAmount(maintenanceCost, "El costo de mantenimiento debe ser mayor a cero.");
        this.repairCost = validatePositiveAmount(repairCost, "El costo de reparacion debe ser mayor a cero.");
        this.energyLevel = initialEnergy;
        this.operational = true;
        this.expenseSequence = 1;
    }

    /**
     * Constructor sencillo para pruebas o usos basicos.
     */
    public PowerPlant(double initialEnergy) {
        this(initialEnergy, 1.5, 0.05, 200.0, 500.0);
    }

    public String getName() {
        return ZONE_NAME;
    }

    public double getEnergyLevel() {
        return energyLevel;
    }

    public boolean isOperational() {
        return operational;
    }

    public void tick(Random random, CsvWriter csvWriter, long currentStep) {
        if (random == null) {
            return;
        }

        if (!operational) {
            return;
        }

        consumeEnergy();
        registerMaintenanceCost(csvWriter);

        if (energyLevel <= 0) {
            triggerFailure(csvWriter, currentStep);
            return;
        }

        if (random.nextDouble() < failureProbability) {
            triggerFailure(csvWriter, currentStep);
        }
    }

    public void triggerFailure() {
        this.energyLevel = 0.0;
        this.operational = false;
    }

    public void triggerFailure(CsvWriter csvWriter, long currentStep) {
        triggerFailure();
        registerFailureEvent(csvWriter, currentStep);
    }

    public void repair() {
        this.energyLevel = initialEnergy;
        this.operational = true;
    }

    public void repair(CsvWriter csvWriter) {
        repair();
        registerRepairCost(csvWriter);
    }

    private void consumeEnergy() {
        energyLevel -= consumptionPerStep;

        if (energyLevel < 0) {
            energyLevel = 0;
        }
    }

    private void registerMaintenanceCost(CsvWriter csvWriter) {
        if (csvWriter == null) {
            return;
        }

        ExpenseRecord record = new ExpenseRecord(
                expenseSequence++,
                "MAINTENANCE",
                maintenanceCost,
                "Mantenimiento regular de la planta de energia",
                LocalDateTime.now()
        );

        csvWriter.appendExpense(record);
    }

    private void registerRepairCost(CsvWriter csvWriter) {
        if (csvWriter == null) {
            return;
        }

        ExpenseRecord record = new ExpenseRecord(
                expenseSequence++,
                "REPAIR",
                repairCost,
                "Reparacion de la planta de energia",
                LocalDateTime.now()
        );

        csvWriter.appendExpense(record);
    }

    private void registerFailureEvent(CsvWriter csvWriter, long currentStep) {
        if (csvWriter == null) {
            return;
        }

        EventRecord record = new EventRecord(
                currentStep,
                "POWER_FAILURE",
                "Falla detectada en la planta de energia",
                ZONE_NAME,
                LocalDateTime.now()
        );

        csvWriter.appendEvent(record);
    }

    private double validatePositiveAmount(double value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    private double validateProbability(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("La probabilidad debe estar entre 0 y 1.");
        }

        return probability;
    }
}