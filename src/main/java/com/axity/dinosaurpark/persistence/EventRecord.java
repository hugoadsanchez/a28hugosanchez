package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/**
 * Registro de evento ocurrido durante la simulacion.
 *
 * Ejemplos: escape de dinosaurio, apagon o tormenta.
 */
public record EventRecord(
        long step,
        String eventName,
        String description,
        String affectedEntities,
        LocalDateTime timestamp
) {

    public String toCsvLine() {
        return step + "," + eventName + "," + description + "," + affectedEntities + "," + timestamp;
    }
}