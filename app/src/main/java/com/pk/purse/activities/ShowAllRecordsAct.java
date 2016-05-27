package com.pk.purse.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.pk.purse.R;
import com.pk.purse.models.MoneyRecorder;
import com.pk.purse.views.RecordAdapter;

public class ShowAllRecordsAct extends AppCompatActivity {

    private RecordAdapter recordAdapter;
    private ListView recordsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_records);
        init();
    }

    private void init() {
        recordAdapter = new RecordAdapter(this, R.layout.listcell_record, MoneyRecorder.getInstance().getRecords());
        recordsListView = (ListView) findViewById(R.id.listview_allrecords);
        recordsListView.setAdapter(recordAdapter);
    }

}
