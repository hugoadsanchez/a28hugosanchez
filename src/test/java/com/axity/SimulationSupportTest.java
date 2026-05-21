package com.axity.dinosaurpark;

import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Ticket;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.simulation.EventScheduler;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationSupportTest {

    @TempDir
    Path tempDirectory;

    @Test
    void shouldWriteCsvFiles() throws Exception {
        CsvWriter writer = new CsvWriter(tempDirectory.toString());

        writer.appendRevenue(new RevenueRecord(
                1,
                "TICKET",
                25.0,
                1,
                "ARRIVAL",
                LocalDateTime.now()
        ));

        writer.appendExpense(new ExpenseRecord(
                1,
                "MAINTENANCE",
                200.0,
                "Mantenimiento de prueba",
                LocalDateTime.now()
        ));

        writer.appendEvent(new EventRecord(
                1,
                "EVENTO_PRUEBA",
                "Evento de prueba",
                "Sistema",
                LocalDateTime.now()
        ));

        Path revenuesFile = tempDirectory.resolve("revenues.csv");
        Path expensesFile = tempDirectory.resolve("expenses.csv");
        Path eventsFile = tempDirectory.resolve("events.csv");

        assertTrue(Files.exists(revenuesFile));
        assertTrue(Files.exists(expensesFile));
        assertTrue(Files.exists(eventsFile));

        assertTrue(Files.readString(revenuesFile).contains("TICKET"));
        assertTrue(Files.readString(expensesFile).contains("MAINTENANCE"));
        assertTrue(Files.readString(eventsFile).contains("EVENTO_PRUEBA"));
    }

    @Test
    void shouldConvertRecordsToCsvLines() {
        LocalDateTime date = LocalDateTime.of(2026, 5, 21, 12, 0);

        RevenueRecord revenue = new RevenueRecord(1, "TICKET", 25.0, 1, "ARRIVAL", date);
        ExpenseRecord expense = new ExpenseRecord(1, "SALARY", 150.0, "Pago", date);
        EventRecord event = new EventRecord(1, "STORM", "Tormenta", "Turistas", date);

        assertEquals("1,TICKET,25.0,1,ARRIVAL,2026-05-21T12:00", revenue.toCsvLine());
        assertEquals("1,SALARY,150.0,Pago,2026-05-21T12:00", expense.toCsvLine());
        assertEquals("1,STORM,Tormenta,Turistas,2026-05-21T12:00", event.toCsvLine());
    }

    @Test
    void shouldMaintainParkStateCounters() {
        Tourist touristOne = new Tourist(1, "Turista 1");
        Tourist touristTwo = new Tourist(2, "Turista 2");
        touristOne.setStatus(TouristStatus.IN_PARK);

        Dinosaur dinosaurOne = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        Dinosaur dinosaurTwo = new HerbivoreDinosaur(2, "Trici", "Triceratops");
        dinosaurOne.escape();

        ParkState state = new ParkState(
                List.of(touristOne, touristTwo),
                List.of(dinosaurOne, dinosaurTwo),
                new PowerPlant(100.0),
                null
        );

        state.setCurrentStep(5);
        state.addRevenue(100.0);
        state.addExpense(50.0);

        assertEquals(5, state.getCurrentStep());
        assertEquals(1, state.countActiveTourists());
        assertEquals(1, state.countDinosaursInEnclosure());
        assertEquals(100.0, state.getTotalRevenue());
        assertEquals(50.0, state.getTotalExpenses());
    }

    @Test
    void shouldRecaptureEscapedDinosaursWithGuard() {
        Guard guard = new Guard(1, "Guardia 1", 150.0);
        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "T-Rex");

        dinosaur.escape();
        guard.recaptureEscapedDinosaurs(List.of(dinosaur));

        assertEquals("GUARD", guard.getRole());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dinosaur.getStatus());
    }

    @Test
    void shouldRepairPowerPlantWithTechnician() {
        Technician technician = new Technician(1, "Tecnico 1", 150.0);
        PowerPlant powerPlant = new PowerPlant(100.0);

        powerPlant.triggerFailure();
        technician.repairIfNeeded(powerPlant);

        assertEquals("TECHNICIAN", technician.getRole());
        assertTrue(powerPlant.isOperational());
        assertEquals(100.0, powerPlant.getEnergyLevel());
    }

    @Test
    void shouldScheduleThreeDeterministicEvents() {
        EventScheduler schedulerOne = new EventScheduler(42, 100);
        EventScheduler schedulerTwo = new EventScheduler(42, 100);

        int eventCount = 0;

        for (int step = 1; step <= 100; step++) {
            Optional<SimulationEvent> firstEvent = schedulerOne.checkForEvent(step);
            Optional<SimulationEvent> secondEvent = schedulerTwo.checkForEvent(step);

            assertEquals(firstEvent.map(SimulationEvent::getName), secondEvent.map(SimulationEvent::getName));

            if (firstEvent.isPresent()) {
                eventCount++;
            }
        }

        assertEquals(3, eventCount);
    }

    @Test
    void shouldExecuteDinosaurEscapeEvent() {
        Tourist tourist = new Tourist(1, "Turista 1");
        tourist.setStatus(TouristStatus.IN_PARK);

        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "T-Rex");

        ParkState state = new ParkState(
                List.of(tourist),
                List.of(dinosaur),
                new PowerPlant(100.0),
                null
        );

        state.setCurrentStep(10);

        DinosaurEscapeEvent event = new DinosaurEscapeEvent();
        event.execute(state, new Random(42));

        assertEquals("ESCAPE_DINOSAURIO", event.getName());
        assertTrue(dinosaur.isEscaped());
        assertEquals(TouristStatus.ATTACKED, tourist.getStatus());
        assertNotNull(event.toRecord(10));
    }

    @Test
    void shouldExecuteBlackoutEvent() {
        PowerPlant powerPlant = new PowerPlant(100.0);

        ParkState state = new ParkState(
                List.of(),
                List.of(),
                powerPlant,
                null
        );

        state.setCurrentStep(20);

        BlackoutEvent event = new BlackoutEvent();
        event.execute(state, new Random(42));

        assertEquals("APAGON_MASIVO", event.getName());
        assertFalse(powerPlant.isOperational());
        assertEquals(2000.0, state.getTotalExpenses());
        assertNotNull(event.toRecord(20));
    }

    @Test
    void shouldExecuteStormEvent() {
        Tourist tourist = new Tourist(1, "Turista 1");
        tourist.setStatus(TouristStatus.IN_PARK);

        ParkState state = new ParkState(
                List.of(tourist),
                List.of(),
                new PowerPlant(100.0),
                null
        );

        state.setCurrentStep(30);

        StormEvent event = new StormEvent();
        event.execute(state, new Random(42));

        assertEquals("TORMENTA_TORRENCIAL", event.getName());
        assertTrue(tourist.getVisitedZones().contains("EVACUACION"));
        assertEquals(500.0, state.getTotalExpenses());
        assertNotNull(event.toRecord(30));
    }

    @Test
    void shouldCreateTicketAndSurvey() {
        LocalDateTime date = LocalDateTime.of(2026, 5, 21, 12, 30);

        Ticket ticket = new Ticket(1, 10, 25.0, "GENERAL", date);
        SatisfactionSurvey survey = new SatisfactionSurvey(10, "Recinto VIP", 5);

        assertEquals(1, ticket.getId());
        assertEquals(10, ticket.getTouristId());
        assertEquals(25.0, ticket.getPrice());
        assertEquals("GENERAL", ticket.getCategory());
        assertEquals(date, ticket.getIssuedAt());

        assertEquals(10, survey.getTouristId());
        assertEquals("Recinto VIP", survey.getEnclosureName());
        assertEquals(5, survey.getScore());
    }
}