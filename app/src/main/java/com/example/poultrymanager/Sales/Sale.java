package com.example.poultrymanager.Sales;

import java.util.Date;

public class Sale {
    private Date date;
    private long numOfTrays;
    private long pricePerTray;
    private String buyerName;

    public Sale(Date date, long numOfTrays, long pricePerTray, String buyerName) {
        this.date = date;
        this.numOfTrays = numOfTrays;
        this.pricePerTray = pricePerTray;
        this.buyerName = buyerName;
    }

    public Sale() {
    }

    public Date getDate() {
        return date;
    }

    public long getNumOfTrays() {
        return numOfTrays;
    }

    public long getPricePerTray() {
        return pricePerTray;
    }

    public String getBuyerName() {
        return buyerName;
    }

}
