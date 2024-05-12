package com.example.myapplication.medicine;

public class Medicine {
    private String name;
    private String dosage;
    private int frequency;
    private long lastTaken;

    public Medicine(String name, String dosage, int frequency) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.lastTaken = 0; // Initially not taken
    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public int getFrequency() {
        return frequency;
    }

    public long getLastTaken() {
        return lastTaken;
    }

    public void setLastTaken(long lastTaken) {
        this.lastTaken = lastTaken;
    }
}

