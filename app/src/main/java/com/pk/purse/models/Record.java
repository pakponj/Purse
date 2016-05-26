package com.pk.purse.models;

import java.util.Date;

public class Record {
    private Item item;
    private Date time;

    public Record(Item item) {
        this.item = item;
        time = new Date(System.currentTimeMillis());
    }

    public Item getItem() { return item; }

    public Date getTime() { return time; }
}
