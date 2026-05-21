package com.axity.dinosaurpark;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZoneTest {

    @Test
    void shouldAddTouristsToArrivalQueueAndProcessBatch() {
        ArrivalZone arrivalZone = new ArrivalZone(2, 25.0);
        Tourist touristOne = new Tourist(1, "Turista 1");
        Tourist touristTwo = new Tourist(2, "Turista 2");
        Tourist touristThree = new Tourist(3, "Turista 3");

        arrivalZone.enter(touristOne);
        arrivalZone.enter(touristTwo);
        arrivalZone.enter(touristThree);

        List<Tourist> admittedTourists = arrivalZone.processBatch(2, null);

        assertEquals(2, admittedTourists.size());
        assertEquals(1, arrivalZone.getWaitingCount());
        assertEquals(2, arrivalZone.getCurrentOccupancy());
        assertFalse(arrivalZone.hasCapacity());
    }

    @Test
    void shouldReleaseBathroomAfterTicks() {
        BathroomZone bathroomZone = new BathroomZone(1, 2, 20.0, 0.0);
        Tourist tourist = new Tourist(1, "Turista 1");

        boolean entered = bathroomZone.tryEnter(tourist, new Random(42), null);

        assertTrue(entered);
        assertEquals(1, bathroomZone.getCurrentOccupancy());

        bathroomZone.tick();
        assertEquals(1, bathroomZone.getCurrentOccupancy());

        bathroomZone.tick();
        assertEquals(0, bathroomZone.getCurrentOccupancy());
    }

    @Test
    void shouldVisitCentralHub() {
        CentralHub centralHub = new CentralHub(15.0, 1.0);
        Tourist tourist = new Tourist(1, "Turista 1");

        centralHub.visit(tourist, new Random(42), null);

        assertEquals(15.0, tourist.getMoneySpent());
        assertEquals(0, centralHub.getCurrentOccupancy());
        assertTrue(tourist.getVisitedZones().contains("CENTRAL_HUB"));
    }

    @Test
    void shouldVisitObservationEnclosureAndGenerateSurvey() {
        ObservationEnclosure enclosure = new ObservationEnclosure(
                "Recinto VIP",
                ExperienceType.VIP,
                5,
                75.0
        );

        Tourist tourist = new Tourist(1, "Turista 1");

        var survey = enclosure.visit(tourist, new Random(42), null);

        assertNotNull(survey);
        assertEquals(75.0, tourist.getMoneySpent());
        assertEquals("Recinto VIP", survey.getEnclosureName());
        assertEquals(0, enclosure.getCurrentOccupancy());
    }
}