package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Recinto donde los turistas observan dinosaurios.
 *
 * Cada recinto tiene un tipo de experiencia, capacidad maxima y costo de entrada.
 */
public class ObservationEnclosure implements ParkZone {

    private final String name;
    private final ExperienceType experienceType;
    private final int maxVisitors;
    private final double entryFee;
    private final Set<Integer> currentVisitors;

    private long revenueSequence;

    public ObservationEnclosure(
            String name,
            ExperienceType experienceType,
            int maxVisitors,
            double entryFee
    ) {
        this.name = validateText(name, "El nombre del recinto es obligatorio.");
        this.experienceType = validateExperienceType(experienceType);
        this.maxVisitors = validatePositiveInt(maxVisitors, "La capacidad del recinto debe ser mayor a cero.");
        this.entryFee = validatePositiveAmount(entryFee, "El costo de entrada debe ser mayor a cero.");
        this.currentVisitors = new HashSet<>();
        this.revenueSequence = 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return currentVisitors.size() < maxVisitors;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentVisitors.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxVisitors;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist == null || !hasCapacity()) {
            return;
        }

        currentVisitors.add(tourist.getId());
        tourist.recordVisit(name);
    }

    @Override
    public void exit(Tourist tourist) {
        if (tourist == null) {
            return;
        }

        currentVisitors.remove(tourist.getId());
    }

    public SatisfactionSurvey visit(Tourist tourist, Random random, CsvWriter csvWriter) {
        if (tourist == null || random == null || !hasCapacity()) {
            return null;
        }

        enter(tourist);
        tourist.spend(entryFee);
        registerEntryFee(tourist, csvWriter);

        SatisfactionSurvey survey = conductSurvey(tourist, random);

        exit(tourist);
        return survey;
    }

    public SatisfactionSurvey conductSurvey(Tourist tourist, Random random) {
        if (tourist == null || random == null) {
            return null;
        }

        int score = generateScore(random);
        return new SatisfactionSurvey(tourist.getId(), name, score);
    }

    private int generateScore(Random random) {
        return switch (experienceType) {
            case BASIC -> random.nextInt(3) + 1;      // 1 a 3
            case PREMIUM -> random.nextInt(3) + 2;    // 2 a 4
            case VIP -> random.nextInt(3) + 3;        // 3 a 5
        };
    }

    private void registerEntryFee(Tourist tourist, CsvWriter csvWriter) {
        if (csvWriter == null) {
            return;
        }

        RevenueRecord record = new RevenueRecord(
                revenueSequence++,
                "ENCLOSURE_ENTRY",
                entryFee,
                tourist.getId(),
                name,
                LocalDateTime.now()
        );

        csvWriter.appendRevenue(record);
    }

    private String validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    private ExperienceType validateExperienceType(ExperienceType experienceType) {
        if (experienceType == null) {
            throw new IllegalArgumentException("El tipo de experiencia es obligatorio.");
        }

        return experienceType;
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
}