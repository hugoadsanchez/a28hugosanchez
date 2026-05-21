package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Zona de llegada de turistas.
 *
 * Administra una fila de espera, controla capacidad y registra la venta
 * de boletos para los turistas que ingresan al parque.
 */
public class ArrivalZone implements ParkZone {

    private static final String ZONE_NAME = "ARRIVAL";

    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingQueue;
    private int currentOccupancy;
    private long ticketSequence;

    public ArrivalZone(int maxCapacity, double ticketPrice) {
        this.maxCapacity = validateCapacity(maxCapacity);
        this.ticketPrice = validatePrice(ticketPrice);
        this.waitingQueue = new LinkedList<>();
        this.currentOccupancy = 0;
        this.ticketSequence = 1;
    }

    @Override
    public String getName() {
        return ZONE_NAME;
    }

    @Override
    public boolean hasCapacity() {
        return currentOccupancy < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist == null) {
            return;
        }

        waitingQueue.offer(tourist);
    }

    @Override
    public void exit(Tourist tourist) {
        if (tourist == null || currentOccupancy <= 0) {
            return;
        }

        currentOccupancy--;
        tourist.setStatus(TouristStatus.EXITED);
    }

    public int getWaitingCount() {
        return waitingQueue.size();
    }

    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        List<Tourist> admittedTourists = new ArrayList<>();
        int processed = 0;

        while (processed < batchSize && hasCapacity() && !waitingQueue.isEmpty()) {
            Tourist tourist = waitingQueue.poll();

            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.spend(ticketPrice);
            tourist.recordVisit(ZONE_NAME);

            currentOccupancy++;
            processed++;
            admittedTourists.add(tourist);

            registerTicketSale(tourist, csvWriter);
        }

        return admittedTourists;
    }

    private void registerTicketSale(Tourist tourist, CsvWriter csvWriter) {
        if (csvWriter == null) {
            return;
        }

        RevenueRecord record = new RevenueRecord(
                ticketSequence++,
                "TICKET",
                ticketPrice,
                tourist.getId(),
                ZONE_NAME,
                LocalDateTime.now()
        );

        csvWriter.appendRevenue(record);
    }

    private int validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }

        return capacity;
    }

    private double validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio del boleto debe ser mayor a cero.");
        }

        return price;
    }
}