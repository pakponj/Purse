package com.pk.purse.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pk.purse.R;
import com.pk.purse.models.Item;
import com.pk.purse.models.MoneyRecorder;
import com.pk.purse.models.Record;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainMenuAct extends AppCompatActivity {

    private TextView addIncomeTextView;
    private TextView addOutcomeTextView;
    private TextView setWishItemTextView;
    private TextView showRecordsTextView;
    private TextView savedMoneyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        readSavedMoney();
        readRecords();
        init();
    }

    private void init() {
        addIncomeTextView = (TextView) findViewById(R.id.textview_addIncome);
        addOutcomeTextView = (TextView) findViewById(R.id.textview_addOutcome);
        setWishItemTextView = (TextView) findViewById(R.id.textview_addWishitem);
        showRecordsTextView = (TextView) findViewById(R.id.textview_showRecords);
        savedMoneyTextView = (TextView) findViewById(R.id.textview_savedmoney);
        savedMoneyTextView.setText("Your purse: "+String.valueOf(MoneyRecorder.getInstance().getSavedMoney()));

        addIncomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddIncomeDialog().show();
            }
        });
        addOutcomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddOutcomeDialog().show();
            }
        });
        setWishItemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSetWishedItemDialog().show();
            }
        });
        showRecordsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuAct.this, ShowAllRecordsAct.class);
                startActivity(intent);
            }
        });
    }

    private void readRecords() {
        List<Record> records = new ArrayList<Record>();
        String filename = "outcomeRecords";
        FileInputStream inputStream;
        StringBuilder stringBuilder = new StringBuilder();

        try{
            inputStream = openFileInput(filename);
            int character;
            while( (character = inputStream.read()) != -1 ) {
                stringBuilder.append( (char) character);
            }
            inputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("readRecord", stringBuilder.toString());
        if(stringBuilder.length() > 0 ) {
            String[] recordsArray = stringBuilder.toString().split("\n");
            for(String s: recordsArray) {
                String[] recordArray = s.split(" ",4);
                String itemName = recordArray[0];
                double price = Double.parseDouble(recordArray[1]);
                int quantity = Integer.parseInt(recordArray[2]);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = simpleDateFormat.parse(recordArray[3], new ParsePosition(0));
//                Date time = Date.valueOf(recordArray[3]);
                Item item = new Item(itemName, price, quantity);
                Record record = new Record(item, time);
                records.add(record);
            }
        }
        MoneyRecorder.getInstance().setRecords(records);
    }

    private void readSavedMoney() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        double savedMoney = Double.parseDouble(sharedPref.getString("savedMoney", "0"));
        MoneyRecorder.getInstance().setMoney(savedMoney);
    }

    private void writeRecords() {
        List<Record> records = MoneyRecorder.getInstance().getRecords();
        String filename = "outcomeRecords";
        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            for(Record record : records) {
                outputStream.write(record.toString().getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        }catch( Exception e ) {
            e.printStackTrace();
        }
    }
    private void writeSavedMoney() {
        Log.i("WRITE", "Current saved money ["+MoneyRecorder.getInstance().getSavedMoney()+"]");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("savedMoney", String.valueOf(MoneyRecorder.getInstance().getSavedMoney()));
        editor.commit();
    }

    private Dialog createAddIncomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_addincome, null);

        builder.setTitle("Add Income")
                .setView(dialogView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText incomeAmount = (EditText) dialogView.findViewById(R.id.edittext_incomeamount);
                        MoneyRecorder mr = MoneyRecorder.getInstance();
                        mr.addMoney(Double.parseDouble(incomeAmount.getText().toString()));
                        savedMoneyTextView.setText("Your purse: "+mr.getSavedMoney());
                        writeSavedMoney();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private Dialog createAddOutcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_addoutcome, null);

        builder.setTitle("Add Outcome")
                .setView(dialogView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText itemName = (EditText) dialogView.findViewById(R.id.edittext_itemname);
                        EditText itemQuantity = (EditText) dialogView.findViewById(R.id.edittext_itemquantity);
                        EditText pricePerItem = (EditText) dialogView.findViewById(R.id.edittext_priceperitem);
                        String name = itemName.getText().toString();
                        int quantity = Integer.parseInt(itemQuantity.getText().toString());
                        double price = Double.parseDouble(pricePerItem.getText().toString());
                        Item item = new Item(name, price, quantity);
                        Record record = new Record(item);
                        MoneyRecorder mr = MoneyRecorder.getInstance();
                        mr.addRecord(record);
                        mr.substractMoney(price * quantity);
                        savedMoneyTextView.setText("Your purse: "+mr.getSavedMoney());
                        writeSavedMoney();
                        writeRecords();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    private Dialog createSetWishedItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_setwisheditem, null);

        builder.setTitle("Set Wished Item")
                .setView(dialogView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
