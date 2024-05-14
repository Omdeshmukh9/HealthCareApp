package com.example.myapplication.reminders;

import java.util.Date;

public class Medication {

    private String medicineName;
    private Date date;

    // Required empty constructor for Firestore
    public Medication() {
    }

    public Medication(String medicineName, Date date) {
        this.medicineName = medicineName;
        this.date = date;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

