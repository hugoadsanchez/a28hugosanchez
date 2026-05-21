package com.axity.dinosaurpark;

import com.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PowerPlantTest {

    @Test
    void shouldCreateOperationalPowerPlant() {
        PowerPlant powerPlant = new PowerPlant(100.0);

        assertEquals(100.0, powerPlant.getEnergyLevel());
        assertTrue(powerPlant.isOperational());
    }

    @Test
    void shouldTriggerFailure() {
        PowerPlant powerPlant = new PowerPlant(100.0);

        powerPlant.triggerFailure();

        assertEquals(0.0, powerPlant.getEnergyLevel());
        assertFalse(powerPlant.isOperational());
    }

    @Test
    void shouldRepairPowerPlant() {
        PowerPlant powerPlant = new PowerPlant(100.0);

        powerPlant.triggerFailure();
        powerPlant.repair();

        assertEquals(100.0, powerPlant.getEnergyLevel());
        assertTrue(powerPlant.isOperational());
    }

    @Test
    void shouldConsumeEnergyOnTick() {
        PowerPlant powerPlant = new PowerPlant(
                100.0,
                1.5,
                0.0,
                200.0,
                500.0
        );

        powerPlant.tick(new Random(42), null, 1);

        assertEquals(98.5, powerPlant.getEnergyLevel());
        assertTrue(powerPlant.isOperational());
    }

    @Test
    void shouldFailWhenEnergyReachesZero() {
        PowerPlant powerPlant = new PowerPlant(
                1.0,
                2.0,
                0.0,
                200.0,
                500.0
        );

        powerPlant.tick(new Random(42), null, 1);

        assertEquals(0.0, powerPlant.getEnergyLevel());
        assertFalse(powerPlant.isOperational());
    }
}