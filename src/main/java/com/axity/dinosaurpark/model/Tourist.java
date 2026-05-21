package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un turista dentro de la simulacion del parque.
 *
 * La clase mantiene datos simples del visitante: identificador, nombre,
 * estado actual, dinero gastado y zonas visitadas.
 */
public class Tourist {

    private final int id;
    private final String name;
    private TouristStatus status;
    private double moneySpent;
    private final List<String> visitedZones;

    public Tourist(int id, String name) {
        this.id = id;
        this.name = validateName(name);
        this.status = TouristStatus.WAITING;
        this.moneySpent = 0.0;
        this.visitedZones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TouristStatus getStatus() {
        return status;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public List<String> getVisitedZones() {
        return Collections.unmodifiableList(visitedZones);
    }

    public void setStatus(TouristStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("El estado del turista no puede ser null.");
        }

        this.status = status;
    }

    public void spend(double amount) {
        if (amount <= 0) {
            return;
        }

        this.moneySpent += amount;
    }

    public void recordVisit(String zoneName) {
        if (zoneName == null || zoneName.isBlank()) {
            return;
        }

        visitedZones.add(zoneName);
    }

    public boolean isActive() {
        return status == TouristStatus.IN_PARK;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del turista es obligatorio.");
        }

        return name;
    }
}