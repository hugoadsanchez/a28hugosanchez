package com.axity.dinosaurpark.model;

/**
 * Dinosaurio carnivoro.
 *
 * Tiene un nivel de peligro alto y un costo mayor de alimentacion.
 */
public class CarnivoreDinosaur extends Dinosaur {

    private static final double FEEDING_COST = 500.0;
    private static final double DANGER_LEVEL = 0.9;

    public CarnivoreDinosaur(int id, String name, String species) {
        super(id, name, species, FEEDING_COST);
    }

    @Override
    public String getDiet() {
        return "CARNIVORE";
    }

    @Override
    public double getDangerLevel() {
        return DANGER_LEVEL;
    }
}