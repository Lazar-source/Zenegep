package com.example.zenegep;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> orokoslist;
    ArrayList<String> napilist;
    private final static String TABLE_NAME = "Kliens";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilestatistics);
        DatabaseHelper dh= new DatabaseHelper(this);
        listView=findViewById(R.id.orokosranglista);
        orokoslist = dh.getAllTimePopularList(TABLE_NAME);
        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,orokoslist);
        listView.setAdapter(adapter);
        listView=findViewById(R.id.napiranglista);
        napilist = dh.getLastDayPopularList(TABLE_NAME);
        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,napilist);
        listView.setAdapter(adapter);

    }
}
