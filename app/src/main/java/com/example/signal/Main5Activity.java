package com.example.signal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        Cursor cursor = getAllItems();
        mAdapter = new SignalAdapter(this,cursor);

        recyclerView.setAdapter(mAdapter);
//        boolean b = recyclerView.postDelayed(Runnable {
//            recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
//        }
//        1000);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                removeItem((long) viewHolder.itemView.getTag());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .create()
                        .show();

            }

        }).attachToRecyclerView(recyclerView);

    }

    private Cursor getAllItems() {
        return myDb.getData();
    }
    private void removeItem(long id) {
        myDb.deleteData(id);
        mAdapter.swapCursor(getAllItems());
    }
}