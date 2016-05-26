package com.pk.purse.models;

public class Plan {

    private double moneyUsedPerDay;
    private int daysUntilPurchasable;

    public Plan(double moneyUsedPerDay, int daysUntilPurchasable) {
        this.moneyUsedPerDay = moneyUsedPerDay;
        this.daysUntilPurchasable = daysUntilPurchasable;
    }

    public void setMoneyUsedPerDay(double moneyUsedPerDay) {
        this.moneyUsedPerDay = moneyUsedPerDay;
    }

    public void setDaysUntilPurchasable(int daysUntilPurchasable) {
        this.daysUntilPurchasable = daysUntilPurchasable;
    }
}
