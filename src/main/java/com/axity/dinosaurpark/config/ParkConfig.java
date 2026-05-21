package com.axity.dinosaurpark.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lee la configuracion general del parque desde park.properties.
 *
 * Se usa Singleton para tener una sola instancia de configuracion
 * durante toda la ejecucion de la simulacion.
 */
public final class ParkConfig {

    private static final String CONFIG_FILE = "park.properties";

    private static ParkConfig instance;

    private final Properties properties;

    private ParkConfig() {
        this.properties = new Properties();
        loadProperties();
    }

    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }

        return instance;
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Integer.parseInt(value);
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Double.parseDouble(value);
    }

    public String getString(String key, String defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }

    public long getSeed() {
        return getInt("simulation.seed", 42);
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    public int getArrivalBatchSize() {
        return getInt("simulation.arrivalBatchSize", 5);
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException("No se encontro el archivo " + CONFIG_FILE);
            }

            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Error al leer el archivo " + CONFIG_FILE, exception);
        }
    }

    /**
     * Metodo usado solo en pruebas unitarias.
     * Permite reiniciar la instancia entre tests.
     */
    static void resetForTesting() {
        instance = null;
    }
}