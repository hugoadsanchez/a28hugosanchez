package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Evento de tormenta torrencial.
 *
 * Los turistas activos son marcados con una visita de evacuacion
 * y se registra un gasto operativo.
 */
public class StormEvent implements SimulationEvent {

    private static final double STORM_COST = 500.0;

    @Override
    public String getName() {
        return "TORMENTA_TORRENCIAL";
    }

    @Override
    public String getDescription() {
        return "Tormenta torrencial obliga a evacuar zonas del parque.";
    }

    @Override
    public void execute(ParkState state, Random random) {
        if (state == null) {
            return;
        }

        for (Tourist tourist : state.getActiveTourists()) {
            tourist.recordVisit("EVACUACION");
        }

        state.addExpense(STORM_COST);

        if (state.getCsvWriter() != null) {
            state.getCsvWriter().appendExpense(new ExpenseRecord(
                    state.getCurrentStep(),
                    "STORM",
                    STORM_COST,
                    "Costo operativo por tormenta torrencial",
                    LocalDateTime.now()
            ));

            state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(
                step,
                getName(),
                getDescription(),
                "Turistas activos",
                LocalDateTime.now()
        );
    }
}