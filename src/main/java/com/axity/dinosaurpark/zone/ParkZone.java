package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;

/**
 * Contrato base para todas las zonas del parque.
 *
 * Cada zona debe indicar su nombre, capacidad y forma de entrada/salida
 * de turistas. Esto permite tratar diferentes zonas de manera uniforme.
 */
public interface ParkZone {

    String getName();

    boolean hasCapacity();

    int getCurrentOccupancy();

    int getMaxCapacity();

    void enter(Tourist tourist);

    void exit(Tourist tourist);
}