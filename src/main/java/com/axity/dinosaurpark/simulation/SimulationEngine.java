package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Motor principal de la simulacion.
 *
 * Coordina el flujo del parque: llegada de turistas, movimiento por zonas,
 * eventos, trabajadores, gastos e impresion del monitoreo.
 */
public class SimulationEngine {

    private final ParkConfig config;
    private final Random random;

    private CsvWriter csvWriter;
    private ParkState state;
    private EventScheduler eventScheduler;

    private ArrivalZone arrivalZone;
    private CentralHub centralHub;
    private BathroomZone bathroomZone;
    private PowerPlant powerPlant;
    private List<ObservationEnclosure> enclosures;

    private List<Guard> guards;
    private List<Technician> technicians;

    public SimulationEngine(ParkConfig config) {
        this.config = config;
        this.random = new Random(config.getSeed());
    }

    public void run() {
        initializePark();

        int totalSteps = config.getTotalSteps();

        for (int step = 1; step <= totalSteps; step++) {
            state.setCurrentStep(step);

            processArrivals();
            moveActiveTourists();
            tickZones();
            executeScheduledEvent(step);
            executeWorkers();
            registerWorkerSalaries();

            ParkMonitor.displaySnapshot(state);
        }

        System.out.println("Simulacion finalizada.");
        System.out.println("Revisa los archivos CSV generados en la carpeta output.");
    }

    private void initializePark() {
        this.csvWriter = new CsvWriter(config.getString("output.directory", "output"));

        List<Tourist> tourists = createTourists();
        List<Dinosaur> dinosaurs = createDinosaurs();

        this.powerPlant = createPowerPlant();
        this.arrivalZone = createArrivalZone();
        this.centralHub = createCentralHub();
        this.bathroomZone = createBathroomZone();
        this.enclosures = createEnclosures();

        this.guards = createGuards();
        this.technicians = createTechnicians();

        this.state = new ParkState(tourists, dinosaurs, powerPlant, csvWriter);
        this.eventScheduler = new EventScheduler(config.getSeed(), config.getTotalSteps());

        for (Tourist tourist : tourists) {
            arrivalZone.enter(tourist);
        }
    }

    private List<Tourist> createTourists() {
        int totalTourists = config.getInt("tourists", 50);
        List<Tourist> tourists = new ArrayList<>();

        for (int index = 1; index <= totalTourists; index++) {
            tourists.add(new Tourist(index, "Turista " + index));
        }

        return tourists;
    }

    private List<Dinosaur> createDinosaurs() {
        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        int herbivores = config.getInt("dinosaurs.herbivores", 15);

        List<Dinosaur> dinosaurs = new ArrayList<>();
        int id = 1;

        for (int index = 1; index <= carnivores; index++) {
            dinosaurs.add(new CarnivoreDinosaur(id, "Carnivoro " + index, "T-Rex"));
            id++;
        }

        for (int index = 1; index <= herbivores; index++) {
            dinosaurs.add(new HerbivoreDinosaur(id, "Herbivoro " + index, "Triceratops"));
            id++;
        }

        return dinosaurs;
    }

    private PowerPlant createPowerPlant() {
        return new PowerPlant(
                config.getDouble("powerplant.initialEnergy", 100.0),
                config.getDouble("powerplant.consumptionPerStep", 1.5),
                config.getDouble("powerplant.failureProbability", 0.05),
                config.getDouble("powerplant.maintenanceCost", 200.0),
                config.getDouble("powerplant.repairCost", 500.0)
        );
    }

    private ArrivalZone createArrivalZone() {
        return new ArrivalZone(
                config.getInt("arrival.maxCapacity", 30),
                config.getDouble("arrival.ticketPrice", 25.0)
        );
    }

    private CentralHub createCentralHub() {
        return new CentralHub(
                config.getDouble("hub.souvenirPrice", 15.0),
                config.getDouble("hub.souvenirPurchaseProbability", 0.4)
        );
    }

    private BathroomZone createBathroomZone() {
        return new BathroomZone(
                config.getInt("bathroom.maxCapacity", 10),
                config.getInt("bathroom.useDurationSteps", 3),
                config.getDouble("bathroom.spaPrice", 20.0),
                config.getDouble("bathroom.spaPurchaseProbability", 0.2)
        );
    }

    private List<ObservationEnclosure> createEnclosures() {
        List<ObservationEnclosure> createdEnclosures = new ArrayList<>();

        createdEnclosures.add(new ObservationEnclosure(
                "Recinto Basico",
                ExperienceType.BASIC,
                config.getInt("enclosure.basic.maxVisitors", 20),
                config.getDouble("enclosure.basic.entryFee", 10.0)
        ));

        createdEnclosures.add(new ObservationEnclosure(
                "Recinto Premium",
                ExperienceType.PREMIUM,
                config.getInt("enclosure.premium.maxVisitors", 12),
                config.getDouble("enclosure.premium.entryFee", 30.0)
        ));

        createdEnclosures.add(new ObservationEnclosure(
                "Recinto VIP",
                ExperienceType.VIP,
                config.getInt("enclosure.vip.maxVisitors", 5),
                config.getDouble("enclosure.vip.entryFee", 75.0)
        ));

        return createdEnclosures;
    }

    private List<Guard> createGuards() {
        int guardCount = config.getInt("workers.guards", 3);
        double salary = config.getDouble("workers.dailySalary", 150.0);

        List<Guard> createdGuards = new ArrayList<>();

        for (int index = 1; index <= guardCount; index++) {
            createdGuards.add(new Guard(index, "Guardia " + index, salary));
        }

        return createdGuards;
    }

    private List<Technician> createTechnicians() {
        int technicianCount = config.getInt("workers.technicians", 2);
        double salary = config.getDouble("workers.dailySalary", 150.0);

        List<Technician> createdTechnicians = new ArrayList<>();

        for (int index = 1; index <= technicianCount; index++) {
            createdTechnicians.add(new Technician(index, "Tecnico " + index, salary));
        }

        return createdTechnicians;
    }

    private void processArrivals() {
        int batchSize = config.getArrivalBatchSize();
        List<Tourist> admittedTourists = arrivalZone.processBatch(batchSize, csvWriter);

        double ticketPrice = config.getDouble("arrival.ticketPrice", 25.0);
        state.addRevenue(admittedTourists.size() * ticketPrice);
    }

    private void moveActiveTourists() {
        List<Tourist> activeTourists = state.getActiveTourists();

        for (Tourist tourist : activeTourists) {
            centralHub.visit(tourist, random, csvWriter);
            bathroomZone.tryEnter(tourist, random, csvWriter);

            ObservationEnclosure enclosure = chooseEnclosure(tourist);
            enclosure.visit(tourist, random, csvWriter);
        }
    }

    private ObservationEnclosure chooseEnclosure(Tourist tourist) {
        int index = tourist.getId() % enclosures.size();
        return enclosures.get(index);
    }

    private void tickZones() {
        bathroomZone.tick();
        powerPlant.tick(random, csvWriter, state.getCurrentStep());
    }

    private void executeScheduledEvent(int step) {
        eventScheduler.checkForEvent(step)
                .ifPresent(event -> event.execute(state, random));
    }

    private void executeWorkers() {
        for (Guard guard : guards) {
            guard.recaptureEscapedDinosaurs(state.getDinosaurs());
        }

        for (Technician technician : technicians) {
            technician.repairIfNeeded(powerPlant);
        }
    }

    private void registerWorkerSalaries() {
        double totalSalary = 0.0;

        for (Guard guard : guards) {
            totalSalary += guard.getDailySalary();
        }

        for (Technician technician : technicians) {
            totalSalary += technician.getDailySalary();
        }

        state.addExpense(totalSalary);

        csvWriter.appendExpense(new ExpenseRecord(
                state.getCurrentStep(),
                "SALARY",
                totalSalary,
                "Pago diario de trabajadores",
                LocalDateTime.now()
        ));
    }
}