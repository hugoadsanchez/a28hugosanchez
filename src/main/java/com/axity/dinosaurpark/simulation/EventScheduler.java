package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Programa eventos de forma determinista usando una semilla fija.
 *
 * Si se usa la misma semilla y la misma cantidad de pasos, los eventos
 * se programan en los mismos momentos.
 */
public class EventScheduler {

    private final Map<Integer, SimulationEvent> scheduledEvents;

    public EventScheduler(long seed, int totalSteps) {
        this.scheduledEvents = new HashMap<>();
        scheduleEvents(seed, totalSteps);
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(scheduledEvents.get(step));
    }

    private void scheduleEvents(long seed, int totalSteps) {
        Random random = new Random(seed);

        scheduleSingleEvent(random, totalSteps, new DinosaurEscapeEvent());
        scheduleSingleEvent(random, totalSteps, new BlackoutEvent());
        scheduleSingleEvent(random, totalSteps, new StormEvent());
    }

    private void scheduleSingleEvent(Random random, int totalSteps, SimulationEvent event) {
        if (totalSteps <= 0 || event == null) {
            return;
        }

        int step = random.nextInt(totalSteps) + 1;

        while (scheduledEvents.containsKey(step)) {
            step = random.nextInt(totalSteps) + 1;
        }

        scheduledEvents.put(step, event);
    }
}