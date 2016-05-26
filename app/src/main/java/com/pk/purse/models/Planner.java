package com.pk.purse.models;

public class Planner {

    private static Planner instance;

    public static Planner getInstance() {
        if(instance == null) instance = new Planner();
        return instance;
    }

    public Plan createPlan(Item item, int days) {
        double totalPrice = item.getPrice() * item.getQuantity();
        MoneyRecorder recorder = MoneyRecorder.getInstance();
        double savedMoney = recorder.getSavedMoney();
        double savedPerDay = ( totalPrice - savedMoney) / days;

        return new Plan(savedPerDay, days);
    }

}
