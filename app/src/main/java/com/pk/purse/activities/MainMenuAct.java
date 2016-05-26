package com.pk.purse.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pk.purse.R;

public class MainMenuAct extends AppCompatActivity {

    private TextView addIncomeTextView;
    private TextView addOutcomeTextView;
    private TextView setWishItemTextView;
    private TextView showRecordsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    private void init() {
        addIncomeTextView = (TextView) findViewById(R.id.textview_addIncome);
        addOutcomeTextView = (TextView) findViewById(R.id.textview_addOutcome);
        setWishItemTextView = (TextView) findViewById(R.id.textview_addWishitem);
        showRecordsTextView = (TextView) findViewById(R.id.textview_showRecords);

        addIncomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to add income act
            }
        });
        addOutcomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to add outcome act
            }
        });
        setWishItemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to set wished item
            }
        });
        showRecordsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to show all outcome records
            }
        });
    }
}
