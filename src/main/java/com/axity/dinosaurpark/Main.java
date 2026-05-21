package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;

/**
 * Punto de entrada de la simulacion del Parque Turistico de Dinosaurios.
 */
public class Main {

    public static void main(String[] args) {
        ParkConfig config = ParkConfig.getInstance();

        System.out.println("Iniciando simulacion del Parque Turistico de Dinosaurios...");
        System.out.println("Total de pasos configurados: " + config.getTotalSteps());
        System.out.println("Turistas por lote de llegada: " + config.getArrivalBatchSize());
        System.out.println("Semilla de simulacion: " + config.getSeed());

        CsvWriter csvWriter = new CsvWriter(config.getString("output.directory", "output"));

        csvWriter.appendRevenue(new RevenueRecord(
                1,
                "TICKET",
                25.0,
                1,
                "ARRIVAL",
                LocalDateTime.now()
        ));

        csvWriter.appendExpense(new ExpenseRecord(
                1,
                "MAINTENANCE",
                200.0,
                "Prueba de gasto operativo",
                LocalDateTime.now()
        ));

        csvWriter.appendEvent(new EventRecord(
                1,
                "PRUEBA_EVENTO",
                "Evento de prueba para validar CSV",
                "Sistema",
                LocalDateTime.now()
        ));

        System.out.println("Archivos CSV generados correctamente en carpeta output.");
    }
}