package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Zona de banos del parque.
 *
 * Tiene capacidad limitada y cada turista ocupa un espacio durante
 * cierta cantidad de pasos de simulacion.
 */
public class BathroomZone implements ParkZone {

    private static final String ZONE_NAME = "BATHROOM";

    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaPurchaseProbability;

    private final Map<Integer, Integer> touristsUsingBathroom;
    private long revenueSequence;

    public BathroomZone(
            int maxCapacity,
            int useDurationSteps,
            double spaPrice,
            double spaPurchaseProbability
    ) {
        this.maxCapacity = validatePositiveInt(maxCapacity, "La capacidad de banos debe ser mayor a cero.");
        this.useDurationSteps = validatePositiveInt(useDurationSteps, "La duracion de uso debe ser mayor a cero.");
        this.spaPrice = validatePositiveAmount(spaPrice, "El precio del SPA debe ser mayor a cero.");
        this.spaPurchaseProbability = validateProbability(spaPurchaseProbability);
        this.touristsUsingBathroom = new HashMap<>();
        this.revenueSequence = 1;
    }

    @Override
    public String getName() {
        return ZONE_NAME;
    }

    @Override
    public boolean hasCapacity() {
        return touristsUsingBathroom.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return touristsUsingBathroom.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist == null || !hasCapacity()) {
            return;
        }

        touristsUsingBathroom.put(tourist.getId(), useDurationSteps);
        tourist.recordVisit(ZONE_NAME);
    }

    @Override
    public void exit(Tourist tourist) {
        if (tourist == null) {
            return;
        }

        touristsUsingBathroom.remove(tourist.getId());
    }

    public boolean tryEnter(Tourist tourist, Random random, CsvWriter csvWriter) {
        if (tourist == null || random == null || !hasCapacity()) {
            return false;
        }

        enter(tourist);

        if (random.nextDouble() < spaPurchaseProbability) {
            tourist.spend(spaPrice);
            registerSpaSale(tourist, csvWriter);
        }

        return true;
    }

    /**
     * Avanza un paso de simulacion.
     * Cuando el contador llega a cero, el turista libera el espacio.
     */
    public void tick() {
        Iterator<Map.Entry<Integer, Integer>> iterator = touristsUsingBathroom.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            int remainingSteps = entry.getValue() - 1;

            if (remainingSteps <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remainingSteps);
            }
        }
    }

    private void registerSpaSale(Tourist tourist, CsvWriter csvWriter) {
        if (csvWriter == null) {
            return;
        }

        RevenueRecord record = new RevenueRecord(
                revenueSequence++,
                "SPA",
                spaPrice,
                tourist.getId(),
                ZONE_NAME,
                LocalDateTime.now()
        );

        csvWriter.appendRevenue(record);
    }

    private int validatePositiveInt(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }

        return value;
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