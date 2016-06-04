package com.pk.purse.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Record {

    public static final int TYPE_INCOME = 0;
    public static final int TYPE_OUTCOME = 1;
    public static final int TYPE_WISHED = 2;

    private static String income = "INCOME";
    private static String outcome = "OUTCOME";
    private static String wished = "WISHED";

    private int type;
    private Item item;
    private Date time;
    private double remainingMoney;

    public Record(int type, Item item) {
        this.type = type;
        this.item = item;
        time = new Date(System.currentTimeMillis());
        remainingMoney = MoneyRecorder.getInstance().getSavedMoney();
    }

    public Record(int type, Item item, Date time, double remainingMoney) {
        this.type = type;
        this.item = item;
        this.time = time;
        this.remainingMoney = remainingMoney;
    }

    public String getType() {
        if( type == TYPE_INCOME ) return income;
        else if (type == TYPE_OUTCOME ) return outcome;
        else return wished;
    }

    public Item getItem() { return item; }

    public Date getTime() { return time; }

    public double getRemainingMoney() { return remainingMoney; }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder space = new StringBuilder(" ");
        stringBuilder.append(type);
        stringBuilder.append(space);
        stringBuilder.append(item.getName());
        stringBuilder.append(space);
        stringBuilder.append(item.getPrice());
        stringBuilder.append(space);
        stringBuilder.append(item.getQuantity());
        stringBuilder.append(space);
        stringBuilder.append(remainingMoney);
        stringBuilder.append(space);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz", Locale.getDefault());
        stringBuilder.append(dateFormat.format(time));
        return stringBuilder.toString();
    }
}
