package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.util.Random;

/**
 * Contrato para los eventos de la simulacion.
 *
 * Este contrato permite aplicar el patron Strategy: cada evento tiene
 * una forma distinta de ejecutarse, pero todos se usan igual desde el motor.
 */
public interface SimulationEvent {

    String getName();

    String getDescription();

    void execute(ParkState state, Random random);

    EventRecord toRecord(long step);
}