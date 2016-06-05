package com.pk.purse.models;

public class Planner {

    private static Planner instance;

    public static Planner getInstance() {
        if(instance == null) instance = new Planner();
        return instance;
    }

    public Plan createPlan(Item item, int days, boolean useSavingMoney) {
        double totalPrice = item.getPrice() * item.getQuantity();
        MoneyRecorder recorder = MoneyRecorder.getInstance();
        double savedMoney;
        if(useSavingMoney) savedMoney = recorder.getSavedMoney();
        else savedMoney = 0;
        double savedPerDay = ( totalPrice - savedMoney) / days;

        return new Plan(savedPerDay, days, useSavingMoney);
    }

}
