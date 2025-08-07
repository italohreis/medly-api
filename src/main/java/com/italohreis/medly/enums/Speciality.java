package com.italohreis.medly.enums;

public enum Speciality {
    CARDIOLOGY("Cardiology"),
    DERMATOLOGY("Dermatology"),
    NEUROLOGY("Neurology"),
    PEDIATRICS("Pediatrics"),
    PSYCHIATRY("Psychiatry"),
    RADIOLOGY("Radiology"),
    SURGERY("Surgery");

    private final String description;

    Speciality(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
