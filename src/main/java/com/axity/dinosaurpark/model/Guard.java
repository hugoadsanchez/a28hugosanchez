package com.axity.dinosaurpark.model;

import java.util.List;

/**
 * Trabajador encargado de vigilar el parque y recapturar dinosaurios.
 */
public class Guard extends Worker {

    public Guard(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "GUARD";
    }

    public void recaptureEscapedDinosaurs(List<Dinosaur> dinosaurs) {
        if (dinosaurs == null || dinosaurs.isEmpty()) {
            return;
        }

        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.isEscaped()) {
                dinosaur.returnToEnclosure();
            }
        }
    }
}
