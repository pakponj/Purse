package com.pk.purse.models;

public class WishItem {

    private static Item wishedItem;
    private static Plan plan;
    private static int daysToPurchase;
    private static WishItem instance;

    public static WishItem getInstance() {
        if(instance == null) instance = new WishItem();
        return instance;
    }

    public static void setWishedItem(Item item) {
        wishedItem = item;
        getPlan();
    }

    public static void setDaysToPurchase(int days) {
        daysToPurchase = days;
    }

    public static Plan getPlan() {
        if(wishedItem == null) return null;
        else {
            if(plan == null) {
                Planner planner = Planner.getInstance();
//                plan = planner.createPlan(wishedItem, daysToPurchase);
            }
            return plan;
        }
    }
}
