package com.axity.dinosaurpark;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TouristTest {

    @Test
    void shouldCreateTouristWithInitialValues() {
        Tourist tourist = new Tourist(1, "Turista Prueba");

        assertEquals(1, tourist.getId());
        assertEquals("Turista Prueba", tourist.getName());
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
        assertEquals(0.0, tourist.getMoneySpent());
        assertEquals(0, tourist.getVisitedZones().size());
    }

    @Test
    void shouldAccumulateMoneySpent() {
        Tourist tourist = new Tourist(1, "Turista Prueba");

        tourist.spend(25.0);
        tourist.spend(15.0);

        assertEquals(40.0, tourist.getMoneySpent());
    }

    @Test
    void shouldIgnoreInvalidSpentAmount() {
        Tourist tourist = new Tourist(1, "Turista Prueba");

        tourist.spend(-10.0);
        tourist.spend(0.0);

        assertEquals(0.0, tourist.getMoneySpent());
    }

    @Test
    void shouldRecordVisitedZone() {
        Tourist tourist = new Tourist(1, "Turista Prueba");

        tourist.recordVisit("ARRIVAL");

        assertEquals(1, tourist.getVisitedZones().size());
        assertEquals("ARRIVAL", tourist.getVisitedZones().get(0));
    }

    @Test
    void shouldChangeStatus() {
        Tourist tourist = new Tourist(1, "Turista Prueba");

        tourist.setStatus(TouristStatus.IN_PARK);

        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Tourist(1, ""));
    }
}