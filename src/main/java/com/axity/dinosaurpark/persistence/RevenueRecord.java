package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/**
 * Registro de ingreso generado por el parque.
 *
 * Ejemplos: boletos, souvenirs, SPA o entrada a recintos.
 */
public record RevenueRecord(
        long id,
        String type,
        double amount,
        int touristId,
        String zone,
        LocalDateTime timestamp
) {

    public String toCsvLine() {
        return id + "," + type + "," + amount + "," + touristId + "," + zone + "," + timestamp;
    }
}