package com.pk.purse.models;

import java.util.ArrayList;
import java.util.List;

public class MoneyRecorder {

    private List<Record> records;
    private double savedMoney;
    private static MoneyRecorder instance;

    private MoneyRecorder() {
        records = new ArrayList<Record>();
        savedMoney = 0;
    }

    public static MoneyRecorder getInstance() {
        if(instance == null) instance = new MoneyRecorder();
        return instance;
    }

    public void addRecord(Record record) { records.add(record); }

    public void addMoney(double amount) { savedMoney += amount; }

    public void substractMoney(double amount) { savedMoney -= amount; }

    public void setMoney(double amount) { savedMoney = amount; }

    public void setRecords(List<Record> records) { this.records = records; }

    public List<Record> getRecords() { return records; }

    public double getSavedMoney() { return savedMoney; }

}
