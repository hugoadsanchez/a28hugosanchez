package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/**
 * Registro de gasto operativo del parque.
 *
 * Ejemplos: salarios, mantenimiento, reparaciones o energia.
 */
public record ExpenseRecord(
        long id,
        String type,
        double amount,
        String description,
        LocalDateTime timestamp
) {

    public String toCsvLine() {
        return id + "," + type + "," + amount + "," + description + "," + timestamp;
    }
}