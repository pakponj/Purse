package com.pk.purse.models;

import java.util.Date;

public class Plan {

    private double moneyUsedPerDay;
    private int daysUntilPurchasable;
    private Date startDate;
    private boolean usingSavingMoney;

    public Plan(double moneyUsedPerDay, int daysUntilPurchasable, boolean usingSavingMoney) {
        this.moneyUsedPerDay = moneyUsedPerDay;
        this.daysUntilPurchasable = daysUntilPurchasable;
        this.usingSavingMoney = usingSavingMoney;
        startDate = new Date( System.currentTimeMillis() );
    }

    public void setMoneyUsedPerDay(double moneyUsedPerDay) {
        this.moneyUsedPerDay = moneyUsedPerDay;
    }

    public void setDaysUntilPurchasable(int daysUntilPurchasable) {
        this.daysUntilPurchasable = daysUntilPurchasable;
    }

    public double getMoneyUsedPerDay() {
        return moneyUsedPerDay;
    }

    public int getDaysUntilPurchasable() {
        return daysUntilPurchasable;
    }

    public Date getStartDate() { return startDate; }

    public boolean isUsingSavingMoney() { return usingSavingMoney; }
}
