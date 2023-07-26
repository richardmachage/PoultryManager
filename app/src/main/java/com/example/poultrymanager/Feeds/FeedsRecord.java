package com.example.poultrymanager.Feeds;

import java.util.Date;

public class FeedsRecord {
    private Date date;
    private Float numOfSacks;

    public FeedsRecord(Date date, Float numOfSacks) {
        this.date = date;
        this.numOfSacks = numOfSacks;
    }

    public FeedsRecord() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getNumOfSacks() {
        return numOfSacks;
    }

    public void setNumOfSacks(Float numOfSacks) {
        this.numOfSacks = numOfSacks;
    }
}
