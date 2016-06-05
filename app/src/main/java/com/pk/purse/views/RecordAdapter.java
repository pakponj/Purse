package com.pk.purse.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pk.purse.R;
import com.pk.purse.models.Record;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecordAdapter extends ArrayAdapter<Record> {

    public RecordAdapter(Context context, int resource, List<Record> records) {
        super(context, resource, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.listcell_record, null);

        }

        TextView recordType = (TextView) convertView.findViewById(R.id.cell_recordtype);
        TextView itemName = (TextView) convertView.findViewById(R.id.cell_itemname);
        TextView pricePerItem = (TextView) convertView.findViewById(R.id.cell_priceperitem);
        TextView quantity = (TextView) convertView.findViewById(R.id.cell_quantity);
        TextView remainingMoney = (TextView) convertView.findViewById(R.id.cell_remainingmoney);
        TextView time = (TextView) convertView.findViewById(R.id.cell_time);

        Record record = getItem(position);

        recordType.setText("Record Type: ["+record.getType()+"]");
        itemName.setText("Item's name: ["+record.getItem().getName()+"]");
        pricePerItem.setText("Price: ["+String.valueOf(record.getItem().getPrice())+"]");
        quantity.setText("Quantity: ["+String.valueOf(record.getItem().getQuantity())+"]");
        remainingMoney.setText("Remaining money: ["+record.getRemainingMoney()+"]");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time.setText("Date: ["+simpleDateFormat.format(record.getTime())+"]");

        return convertView;
    }
}
