package com.axity.dinosaurpark.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Clase responsable de guardar ingresos, gastos y eventos en archivos CSV.
 *
 * Se mantiene separada del resto del sistema para no mezclar logica de negocio
 * con logica de escritura de archivos.
 */
public class CsvWriter {

    private static final String REVENUES_FILE = "revenues.csv";
    private static final String EXPENSES_FILE = "expenses.csv";
    private static final String EVENTS_FILE = "events.csv";

    private final Path outputDirectory;

    public CsvWriter(String outputDirectory) {
        this.outputDirectory = Path.of(outputDirectory);
        createOutputDirectory();
        initializeFiles();
    }

    public void appendRevenue(RevenueRecord record) {
        appendLine(REVENUES_FILE, record.toCsvLine());
    }

    public void appendExpense(ExpenseRecord record) {
        appendLine(EXPENSES_FILE, record.toCsvLine());
    }

    public void appendEvent(EventRecord record) {
        appendLine(EVENTS_FILE, record.toCsvLine());
    }

    private void createOutputDirectory() {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo crear la carpeta de salida.", exception);
        }
    }

    private void initializeFiles() {
        initializeFile(REVENUES_FILE, "id,type,amount,touristId,zone,timestamp");
        initializeFile(EXPENSES_FILE, "id,type,amount,description,timestamp");
        initializeFile(EVENTS_FILE, "step,eventName,description,affectedEntities,timestamp");
    }

    private void initializeFile(String fileName, String header) {
        Path filePath = outputDirectory.resolve(fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile(), false))) {
            writer.println(header);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo inicializar el archivo " + fileName, exception);
        }
    }

    private void appendLine(String fileName, String line) {
        Path filePath = outputDirectory.resolve(fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile(), true))) {
            writer.println(line);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo escribir en el archivo " + fileName, exception);
        }
    }
}