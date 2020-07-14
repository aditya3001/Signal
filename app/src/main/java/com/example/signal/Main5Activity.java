package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

public class Main5Activity extends AppCompatActivity {

    MyDataBase myDb;
    private SignalAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        myDb = new MyDataBase(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SignalAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

    }

    private Cursor getAllItems() {
        return myDb.getData();
    }
}