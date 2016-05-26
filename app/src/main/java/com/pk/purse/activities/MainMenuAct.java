package com.pk.purse.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pk.purse.R;
import com.pk.purse.models.Item;
import com.pk.purse.models.MoneyRecorder;
import com.pk.purse.models.Record;

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
        init();
    }

    private void init() {
        addIncomeTextView = (TextView) findViewById(R.id.textview_addIncome);
        addOutcomeTextView = (TextView) findViewById(R.id.textview_addOutcome);
        setWishItemTextView = (TextView) findViewById(R.id.textview_addWishitem);
        showRecordsTextView = (TextView) findViewById(R.id.textview_showRecords);
        savedMoneyTextView = (TextView) findViewById(R.id.textview_savedmoney);

        addIncomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Dialog addIncomeDialog = createAddIncomeDialog();
//                addIncomeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialog) {
//                        Button button = (Button)addIncomeDialog.get
//                        EditText incomeAmount = (EditText) findViewById(R.id.edittext_incomeamount);
//                        MoneyRecorder mr = MoneyRecorder.getInstance();
//                        mr.addMoney(Double.parseDouble(incomeAmount.getText().toString()));
//                        savedMoneyTextView.setText("Your purse: "+mr.getSavedMoney());
//                    }
//                });
//                dialog.show();
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
                //Go to show all outcome records
            }
        });
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
