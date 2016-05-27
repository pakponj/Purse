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

    static class ViewHolder {
        private TextView itemname;
        private TextView quantity;
        private TextView pricePerItem;
        private TextView time;
    }

    public RecordAdapter(Context context, int resource, List<Record> records) {
        super(context, resource, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.listcell_record, null);
//            holder = new ViewHolder();
//            holder.itemname = (TextView) convertView.findViewById(R.id.cell_itemname);
//            holder.pricePerItem = (TextView) convertView.findViewById(R.id.cell_priceperitem);
//            holder.quantity = (TextView) convertView.findViewById(R.id.cell_quantity);
//            holder.time = (TextView) convertView.findViewById(R.id.cell_time);

        }
//        else holder = (ViewHolder) convertView.getTag();

        TextView itemname = (TextView) convertView.findViewById(R.id.cell_itemname);
        TextView pricePerItem = (TextView) convertView.findViewById(R.id.cell_priceperitem);
        TextView quantity = (TextView) convertView.findViewById(R.id.cell_quantity);
        TextView time = (TextView) convertView.findViewById(R.id.cell_time);

        Record record = getItem(position);

//        holder.itemname.setText(record.getItem().getName());
//        holder.pricePerItem.setText(String.valueOf(record.getItem().getPrice()));
//        holder.quantity.setText(String.valueOf(record.getItem().getQuantity()));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        holder.time.setText(simpleDateFormat.format(record.getTime()));

        itemname.setText(record.getItem().getName());
        pricePerItem.setText(String.valueOf(record.getItem().getPrice()));
        quantity.setText(String.valueOf(record.getItem().getQuantity()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time.setText(simpleDateFormat.format(record.getTime()));

        return convertView;
    }
}
