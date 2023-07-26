package com.example.poultrymanager.Eggs;

import java.util.Date;

public class EggRecord {
    private Date date;
    private long numOfEggs;

    public EggRecord(Date date, long numOfEggs) {
        this.date = date;
        this.numOfEggs = numOfEggs;
    }

    public EggRecord() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getNumOfEggs() {
        return numOfEggs;
    }

    public void setNumOfEggs(int numOfEggs) {
        this.numOfEggs = numOfEggs;
    }
}
