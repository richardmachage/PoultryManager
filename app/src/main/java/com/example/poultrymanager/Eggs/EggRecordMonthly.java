package com.example.poultrymanager.Eggs;

public class EggRecordMonthly {
    String month;
    String year;
    int numOfEggs;

    public EggRecordMonthly(String month, String year, int numOfEggs) {
        this.month = month;
        this.year = year;
        this.numOfEggs = numOfEggs;
    }



    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getNumOfEggs() {
        return numOfEggs;
    }

    public void setNumOfEggs(int numOfEggs) {
        this.numOfEggs = numOfEggs;
    }
}
