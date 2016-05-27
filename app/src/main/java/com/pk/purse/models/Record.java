package com.pk.purse.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
    private Item item;
    private Date time;

    public Record(Item item) {
        this.item = item;
        time = new Date(System.currentTimeMillis());
    }

    public Record(Item item, Date time) {
        this.item = item;
        this.time = time;
    }

    public Item getItem() { return item; }

    public Date getTime() { return time; }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder space = new StringBuilder(" ");
        stringBuilder.append(item.getName());
        stringBuilder.append(space);
        stringBuilder.append(item.getPrice());
        stringBuilder.append(space);
        stringBuilder.append(item.getQuantity());
        stringBuilder.append(space);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stringBuilder.append(dateFormat.format(time));
//        stringBuilder.append(time.toString());
        return stringBuilder.toString();
    }
}
