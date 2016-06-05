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
        StringBuilder defaultValue = new StringBuilder("0");
        double price = Double.parseDouble(sharedPreferences.getString("price", defaultValue.toString()));
        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", defaultValue.toString()));
        double moneySavedPerDay = Double.parseDouble(sharedPreferences.getString("moneyUsedPerDay", defaultValue.toString()));
        int daysUntilPurchase = Integer.parseInt(sharedPreferences.getString("daysUntilPurchase", defaultValue.toString()));
        String startDateText = sharedPreferences.getString("startDate", null);
        if( itemName == null || price == 0 || quantity == 0 || moneySavedPerDay == 0 || startDateText == null ) return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz", Locale.getDefault());
        Date startDate = dateFormat.parse(startDateText, new ParsePosition(0));
        Date currentDate = new Date( System.currentTimeMillis() );
        long diffTimeInMillSec = currentDate.getTime() - startDate.getTime();
        long passedDays = diffTimeInMillSec / 1000 / 3600 / 24;
        int daysLeft = daysUntilPurchase - (int) passedDays;
        Log.i("WISHED ITEM", "START DATE: "+startDate.toString());
        Log.i("WISHED ITEM", "CURRENT DATE: "+currentDate.toString());
        Log.i("WISHED ITEM", "DAYS LEFT: "+daysLeft);
        boolean isUsingSavingMoney = sharedPreferences.getBoolean("usingSavingMoney", false);
        if( daysLeft == 0 ) {
            //Confirm that wished item has been purchased and add it to the recorder.
            Item item = new Item(itemName, price, quantity);
            createWishedItemPurchaseDialog(item, isUsingSavingMoney).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }else {
            createWishedItemStatusDialog(itemName, moneySavedPerDay, daysLeft).show();
        }
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
        editor.putBoolean( "usingSavingMoney", plan.isUsingSavingMoney() );
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
                        String name = itemName.getText().toString().replaceAll(" ", "");
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
                        String name = itemNameText.getText().toString().replaceAll(" ", "");
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

    private Dialog createWishedItemStatusDialog(String itemName, double moneySavedPerDay, int daysLeft) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_wisheditemstatus, null);
        builder.setTitle("Your Wished Item")
                .setView(dialogView)
                .setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        TextView name = (TextView) dialogView.findViewById(R.id.wisheditemstatus_itemname);
        name.setText(itemName);
        TextView savedPerDay = (TextView) dialogView.findViewById(R.id.wisheditemstatus_moneysavedperday);
        String format = String.format(Locale.getDefault(),"Money to save today [%.2f]", moneySavedPerDay);
        savedPerDay.setText(format);
        TextView daysLeftText = (TextView) dialogView.findViewById(R.id.wisheditemstatus_daysleft);
        daysLeftText.setText( String.valueOf(daysLeft) + " days left" );

        return builder.create();
    }

    private Dialog createWishedItemPurchaseDialog(final Item wishedItem, final boolean isUsingSavingMoney ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_purchasewisheditem, null);

        TextView nameText = (TextView) dialogView.findViewById(R.id.purchasewisheditem_name);
        nameText.setText( wishedItem.getName() );
        TextView priceText = (TextView) dialogView.findViewById(R.id.purchasewisheditem_price);
        priceText.setText( String.valueOf(wishedItem.getPrice()) );
        TextView useSavingMoneyText = (TextView) dialogView.findViewById(R.id.purchasewisheditem_usesavingmoney);
        useSavingMoneyText.setText( String.valueOf(isUsingSavingMoney) );
        final TextView moneyText = (TextView) dialogView.findViewById(R.id.purchasewisheditem_youhave);
        if( isUsingSavingMoney ) moneyText.setText( String.valueOf(MoneyRecorder.getInstance().getSavedMoney()) );
        else moneyText.setText( "0" );
        final EditText addMoneyText = (EditText) dialogView.findViewById(R.id.purchasewisheditem_addmoney);

        builder.setTitle("Purchase Your Wished Item")
                .setView(dialogView)
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double savingMoney = Double.parseDouble(moneyText.getText().toString());
                        double addedMoney = Double.parseDouble(addMoneyText.getText().toString());
                        MoneyRecorder moneyRecorder = MoneyRecorder.getInstance();
                        moneyRecorder.addMoney( addedMoney );
                        double totalMoney = savingMoney + addedMoney;
                        double change = totalMoney - wishedItem.getPrice();
                        if( change >= 0 ) {
                            moneyRecorder.substractMoney( wishedItem.getPrice() );
                            Record record = new Record(Record.TYPE_OUTCOME, wishedItem);
                            moneyRecorder.addRecord( record );
                            savedMoneyTextView.setText( "Your purse: "+moneyRecorder.getSavedMoney() );
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
