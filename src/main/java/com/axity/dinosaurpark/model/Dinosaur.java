package com.axity.dinosaurpark.model;

/**
 * Clase base para representar dinosaurios del parque.
 *
 * Se define como abstracta porque todos los dinosaurios comparten datos,
 * pero cada tipo puede tener dieta y nivel de peligro diferente.
 */
public abstract class Dinosaur {

    private final int id;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;

    protected Dinosaur(int id, String name, String species, double feedingCostPerDay) {
        this.id = id;
        this.name = validateRequiredText(name, "El nombre del dinosaurio es obligatorio.");
        this.species = validateRequiredText(species, "La especie del dinosaurio es obligatoria.");
        this.feedingCostPerDay = validatePositiveAmount(feedingCostPerDay);
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    public double getFeedingCostPerDay() {
        return feedingCostPerDay;
    }

    public abstract String getDiet();

    public abstract double getDangerLevel();

    public void escape() {
        this.status = DinosaurStatus.ESCAPED;
    }

    public void recapture() {
        this.status = DinosaurStatus.RECAPTURED;
    }

    public void returnToEnclosure() {
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    public boolean isEscaped() {
        return status == DinosaurStatus.ESCAPED;
    }

    private String validateRequiredText(String value, String errorMessage) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }

        return value;
    }

    private double validatePositiveAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El costo de alimentacion debe ser mayor a cero.");
        }

        return amount;
    }
}