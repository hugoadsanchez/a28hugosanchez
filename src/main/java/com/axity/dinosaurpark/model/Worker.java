package com.axity.dinosaurpark.model;

/**
 * Clase base para los trabajadores del parque.
 *
 * Se usa como clase abstracta porque todos los trabajadores tienen datos
 * similares, pero cada tipo cumple una funcion diferente dentro del parque.
 */
public abstract class Worker {

    private final int id;
    private final String name;
    private final double dailySalary;

    protected Worker(int id, String name, double dailySalary) {
        this.id = id;
        this.name = validateName(name);
        this.dailySalary = validateSalary(dailySalary);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDailySalary() {
        return dailySalary;
    }

    public abstract String getRole();

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del trabajador es obligatorio.");
        }

        return name;
    }

    private double validateSalary(double dailySalary) {
        if (dailySalary <= 0) {
            throw new IllegalArgumentException("El salario diario debe ser mayor a cero.");
        }

        return dailySalary;
    }
}