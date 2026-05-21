package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Recinto central del parque.
 *
 * Funciona como punto de distribucion y permite registrar compras
 * de souvenirs de forma pseudoaleatoria.
 */
public class CentralHub implements ParkZone {

    private static final String ZONE_NAME = "CENTRAL_HUB";

    private final double souvenirPrice;
    private final double purchaseProbability;
    private final Set<Integer> touristsInside;
    private long revenueSequence;

    public CentralHub(double souvenirPrice, double purchaseProbability) {
        this.souvenirPrice = validatePrice(souvenirPrice);
        this.purchaseProbability = validateProbability(purchaseProbability);
        this.touristsInside = new HashSet<>();
        this.revenueSequence = 1;
    }

    @Override
    public String getName() {
        return ZONE_NAME;
    }

    @Override
    public boolean hasCapacity() {
        return true;
    }

    @Override
    public int getCurrentOccupancy() {
        return touristsInside.size();
    }

    @Override
    public int getMaxCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist == null) {
            return;
        }

        touristsInside.add(tourist.getId());
        tourist.recordVisit(ZONE_NAME);
    }

    @Override
    public void exit(Tourist tourist) {
        if (tourist == null) {
            return;
        }

        touristsInside.remove(tourist.getId());
    }

    public void visit(Tourist tourist, Random random, CsvWriter csvWriter) {
        if (tourist == null || random == null) {
            return;
        }

        enter(tourist);

        if (random.nextDouble() < purchaseProbability) {
            tourist.spend(souvenirPrice);
            registerSouvenirSale(tourist, csvWriter);
        }

        exit(tourist);
    }

    private void registerSouvenirSale(Tourist tourist, CsvWriter csvWriter) {
        if (csvWriter == null) {
            return;
        }

        RevenueRecord record = new RevenueRecord(
                revenueSequence++,
                "SOUVENIR",
                souvenirPrice,
                tourist.getId(),
                ZONE_NAME,
                LocalDateTime.now()
        );

        csvWriter.appendRevenue(record);
    }

    private double validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio del souvenir debe ser mayor a cero.");
        }

        return price;
    }

    private double validateProbability(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("La probabilidad debe estar entre 0 y 1.");
        }

        return probability;
    }
}