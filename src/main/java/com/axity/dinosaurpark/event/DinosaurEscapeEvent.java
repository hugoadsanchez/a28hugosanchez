package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Evento donde un dinosaurio puede escapar.
 *
 * Si el dinosaurio es peligroso, tambien puede atacar a un turista activo.
 */
public class DinosaurEscapeEvent implements SimulationEvent {

    private String affectedEntities = "Sin afectaciones";

    @Override
    public String getName() {
        return "ESCAPE_DINOSAURIO";
    }

    @Override
    public String getDescription() {
        return "Un dinosaurio escapo de su recinto.";
    }

    @Override
    public void execute(ParkState state, Random random) {
        if (state == null || random == null) {
            return;
        }

        List<Dinosaur> availableDinosaurs = state.getDinosaurs().stream()
                .filter(dinosaur -> !dinosaur.isEscaped())
                .toList();

        if (availableDinosaurs.isEmpty()) {
            affectedEntities = "No habia dinosaurios disponibles para escapar";
            registerEvent(state);
            return;
        }

        Dinosaur escapedDinosaur = availableDinosaurs.get(random.nextInt(availableDinosaurs.size()));
        escapedDinosaur.escape();

        affectedEntities = "Dinosaurio: " + escapedDinosaur.getName();

        if (random.nextDouble() < escapedDinosaur.getDangerLevel()) {
            attackRandomTourist(state, random);
        }

        registerEvent(state);
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(
                step,
                getName(),
                getDescription(),
                affectedEntities,
                LocalDateTime.now()
        );
    }

    private void attackRandomTourist(ParkState state, Random random) {
        List<Tourist> activeTourists = state.getActiveTourists();

        if (activeTourists.isEmpty()) {
            affectedEntities += " | Sin turistas atacados";
            return;
        }

        Tourist attackedTourist = activeTourists.get(random.nextInt(activeTourists.size()));
        attackedTourist.setStatus(TouristStatus.ATTACKED);

        affectedEntities += " | Turista atacado: " + attackedTourist.getName();
    }

    private void registerEvent(ParkState state) {
        if (state.getCsvWriter() != null) {
            state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
}