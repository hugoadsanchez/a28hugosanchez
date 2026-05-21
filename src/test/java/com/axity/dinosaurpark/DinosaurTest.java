package com.axity.dinosaurpark;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DinosaurTest {

    @Test
    void shouldCreateCarnivoreDinosaur() {
        CarnivoreDinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "T-Rex");

        assertEquals(1, dinosaur.getId());
        assertEquals("Rex", dinosaur.getName());
        assertEquals("T-Rex", dinosaur.getSpecies());
        assertEquals("CARNIVORE", dinosaur.getDiet());
        assertEquals(0.9, dinosaur.getDangerLevel());
        assertEquals(500.0, dinosaur.getFeedingCostPerDay());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dinosaur.getStatus());
    }

    @Test
    void shouldCreateHerbivoreDinosaur() {
        HerbivoreDinosaur dinosaur = new HerbivoreDinosaur(2, "Trici", "Triceratops");

        assertEquals("HERBIVORE", dinosaur.getDiet());
        assertEquals(0.2, dinosaur.getDangerLevel());
        assertEquals(200.0, dinosaur.getFeedingCostPerDay());
    }

    @Test
    void shouldChangeDinosaurStatus() {
        CarnivoreDinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "T-Rex");

        dinosaur.escape();
        assertEquals(DinosaurStatus.ESCAPED, dinosaur.getStatus());

        dinosaur.recapture();
        assertEquals(DinosaurStatus.RECAPTURED, dinosaur.getStatus());

        dinosaur.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dinosaur.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new CarnivoreDinosaur(1, "", "T-Rex"));
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new HerbivoreDinosaur(1, "Trici", ""));
    }
}