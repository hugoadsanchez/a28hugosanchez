package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Evento de apagon masivo.
 *
 * La planta de energia falla y se registra un gasto operativo adicional.
 */
public class BlackoutEvent implements SimulationEvent {

    private static final double BLACKOUT_COST = 2000.0;

    @Override
    public String getName() {
        return "APAGON_MASIVO";
    }

    @Override
    public String getDescription() {
        return "Se presento un apagon masivo en el parque.";
    }

    @Override
    public void execute(ParkState state, Random random) {
        if (state == null || state.getPowerPlant() == null) {
            return;
        }

        state.getPowerPlant().triggerFailure(state.getCsvWriter(), state.getCurrentStep());
        state.addExpense(BLACKOUT_COST);

        if (state.getCsvWriter() != null) {
            state.getCsvWriter().appendExpense(new ExpenseRecord(
                    state.getCurrentStep(),
                    "BLACKOUT",
                    BLACKOUT_COST,
                    "Costo operativo por apagon masivo",
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
                "Planta de energia",
                LocalDateTime.now()
        );
    }
}