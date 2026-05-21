package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.simulation.ParkState;

/**
 * Muestra en consola un resumen del estado actual del parque.
 *
 * Se mantiene separado del motor de simulacion para no mezclar
 * la logica del flujo con la logica de salida por consola.
 */
public final class ParkMonitor {

    private ParkMonitor() {
        // Constructor privado para evitar instancias.
    }

    public static void displaySnapshot(ParkState state) {
        if (state == null) {
            return;
        }

        System.out.println();
        System.out.println("========== MONITOREO DEL PARQUE ==========");
        System.out.println("Paso actual: " + state.getCurrentStep());
        System.out.println("Turistas activos: " + state.countActiveTourists());
        System.out.println("Dinosaurios en recintos: " + state.countDinosaursInEnclosure());

        if (state.getPowerPlant() != null) {
            System.out.println("Energia disponible: " + state.getPowerPlant().getEnergyLevel());
            System.out.println("Planta operativa: " + state.getPowerPlant().isOperational());
        }

        System.out.println("Ingresos acumulados: $" + state.getTotalRevenue());
        System.out.println("Gastos acumulados: $" + state.getTotalExpenses());
        System.out.println("==========================================");
        System.out.println();
    }
}