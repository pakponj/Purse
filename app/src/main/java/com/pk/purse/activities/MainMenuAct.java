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
import android.widget.RadioButton;
import android.widget.TextView;

import com.pk.purse.R;
import com.pk.purse.models.Item;
import com.pk.purse.models.MoneyRecorder;
import com.pk.purse.models.Plan;
import com.pk.purse.models.Planner;
import com.pk.purse.models.Record;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        readWishedItem();
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
                String[] recordArray = s.split(" ",6);
                int recordType = Integer.valueOf(recordArray[0]);
                String itemName = recordArray[1];
                double price = Double.parseDouble(recordArray[2]);
                int quantity = Integer.parseInt(recordArray[3]);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz", Locale.getDefault());
                double remainingMoney = Double.parseDouble(recordArray[4]);
                Date time = simpleDateFormat.parse(recordArray[5], new ParsePosition(0));
                Item item = new Item(itemName, price, quantity);
                Record record = new Record(recordType, item, time, remainingMoney);
                records.add(record);
            }
        }
        MoneyRecorder.getInstance().setRecords(records);
    }

    private void readSavedMoney() {
        SharedPreferences sharedPref = this.getSharedPreferences("savingMoneyPref", Context.MODE_PRIVATE);
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
        SharedPreferences sharedPref = this.getSharedPreferences("savingMoneyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("savedMoney", String.valueOf(MoneyRecorder.getInstance().getSavedMoney()));
        editor.apply();
    }

    private void readWishedItem() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("wishedItemPref", Context.MODE_PRIVATE);
        String itemName = sharedPreferences.getString("itemname", null);
        if( itemName == null ) return;
        StringBuilder defaultValue = new StringBuilder("0");
        double price = Double.parseDouble(sharedPreferences.getString("price", defaultValue.toString()));
        if( price == 0 ) return;
        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", defaultValue.toString()));
        if( quantity == 0 ) return;
        double moneyUsedPerDay = Double.parseDouble(sharedPreferences.getString("moneyUsedPerDay", defaultValue.toString()));
        if( moneyUsedPerDay == 0 ) return;
        int daysUntilPurchase = Integer.parseInt(sharedPreferences.getString("daysUntilPurchase", defaultValue.toString()));
        if( daysUntilPurchase == 0 ) return;
        String startDateText = sharedPreferences.getString("startDate", null);
        if( startDateText == null ) return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz", Locale.getDefault());
        Date startDate = dateFormat.parse(startDateText, new ParsePosition(0));
        Date currentDate = new Date( System.currentTimeMillis() );
        long diffTimeInMillSec = currentDate.getTime() - startDate.getTime();
        long passedDays = diffTimeInMillSec / 1000 / 3600 / 24;
        int daysLeft = daysUntilPurchase - (int) passedDays;
        Log.i("WISHED ITEM", "START DATE: "+startDate.toString());
        Log.i("WISHED ITEM", "CURRENT DATE: "+currentDate.toString());
        Log.i("WISHED ITEM", "DAYS LEFT: "+daysLeft);
        //update wished item panel
    }

    private void writeWishedItem( Item item, Plan plan ) {
        Log.i("WRITE", "Wished item ["+item.getName()+"]");
        SharedPreferences sharedPreference = this.getSharedPreferences("wishedItemPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString( "itemname", item.getName() );
        editor.putString( "price", String.valueOf(item.getPrice()) );
        editor.putString( "quantity", String.valueOf(item.getQuantity()) );
        editor.putString( "moneyUsedPerDay", String.valueOf(plan.getMoneyUsedPerDay()) );
        editor.putString( "daysUntilPurchase", String.valueOf(plan.getDaysUntilPurchasable()) );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz", Locale.getDefault());
        editor.putString( "startDate", dateFormat.format(plan.getStartDate()) );
        editor.apply();
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
                        double amount = Double.parseDouble(incomeAmount.getText().toString());
                        MoneyRecorder mr = MoneyRecorder.getInstance();
                        mr.addMoney(amount);
                        Record record = new Record(Record.TYPE_INCOME, new Item("additionMoney", amount, 1));
                        mr.addRecord(record);
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
                        MoneyRecorder mr = MoneyRecorder.getInstance();
                        mr.substractMoney(price * quantity);
                        Record record = new Record(Record.TYPE_OUTCOME, item);
                        mr.addRecord(record);
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
                        EditText itemNameText = (EditText) dialogView.findViewById(R.id.setwisheditem_itemname);
                        EditText priceText = (EditText) dialogView.findViewById(R.id.setwisheditem_price);
                        EditText daysUntilPurchaseText = (EditText) dialogView.findViewById(R.id.setwisheditem_daysuntilpurchase);
                        RadioButton useSavingMoneyButton = (RadioButton) dialogView.findViewById(R.id.setwisheditem_usesavingmoney);
                        String name = itemNameText.getText().toString();
                        double price = Double.parseDouble(priceText.getText().toString());
                        int daysUntilPurchase = Integer.parseInt(daysUntilPurchaseText.getText().toString());
                        boolean useSavingMoney = useSavingMoneyButton.isChecked();
                        Item item = new Item(name, price, 1);
                        Record record = new Record(Record.TYPE_WISHED, item);
                        MoneyRecorder.getInstance().addRecord(record);
                        Plan plan = Planner.getInstance().createPlan(item, daysUntilPurchase, useSavingMoney);
                        writeWishedItem(item, plan);
                        writeRecords();
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
