package com.axity.dinosaurpark.model;

/**
 * Encuesta de satisfaccion generada despues de visitar un recinto.
 */
public class SatisfactionSurvey {

    private final int touristId;
    private final String enclosureName;
    private final int score;

    public SatisfactionSurvey(int touristId, String enclosureName, int score) {
        this.touristId = touristId;
        this.enclosureName = validateEnclosureName(enclosureName);
        this.score = validateScore(score);
    }

    public int getTouristId() {
        return touristId;
    }

    public String getEnclosureName() {
        return enclosureName;
    }

    public int getScore() {
        return score;
    }

    private String validateEnclosureName(String enclosureName) {
        if (enclosureName == null || enclosureName.isBlank()) {
            throw new IllegalArgumentException("El nombre del recinto es obligatorio.");
        }

        return enclosureName;
    }

    private int validateScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La puntuacion debe estar entre 1 y 5.");
        }

        return score;
    }
}