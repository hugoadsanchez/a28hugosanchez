package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

/**
 * Representa un boleto vendido a un turista.
 *
 * Es una clase inmutable: despues de crear el boleto, sus datos no cambian.
 */
public class Ticket {

    private final long id;
    private final int touristId;
    private final double price;
    private final String category;
    private final LocalDateTime issuedAt;

    public Ticket(long id, int touristId, double price, String category, LocalDateTime issuedAt) {
        this.id = id;
        this.touristId = touristId;
        this.price = validatePrice(price);
        this.category = validateCategory(category);
        this.issuedAt = validateIssuedAt(issuedAt);
    }

    public long getId() {
        return id;
    }

    public int getTouristId() {
        return touristId;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    private double validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio del boleto debe ser mayor a cero.");
        }

        return price;
    }

    private String validateCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("La categoria del boleto es obligatoria.");
        }

        return category;
    }

    private LocalDateTime validateIssuedAt(LocalDateTime issuedAt) {
        if (issuedAt == null) {
            throw new IllegalArgumentException("La fecha de emision del boleto es obligatoria.");
        }

        return issuedAt;
    }
}