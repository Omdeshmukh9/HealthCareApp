package com.example.myapplication;

import java.util.Date;

public class StepCountData {
    private int stepCount;
    private Date date;

    public StepCountData() {
        // Required default constructor for Firestore
    }

    public StepCountData(int stepCount, Date date) {
        this.stepCount = stepCount;
        this.date = date;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
