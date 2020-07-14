package com.example.signal;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnTicks;

import java.util.ArrayList;
import java.util.Arrays;

public class MykiteTicker extends KiteTicker {
    private String InstrumentArray;
    private float[] TargetArray;
    private float[] StopLossArray;
    Context ctxt;
    ArrayList<Tick> arrayList;
    Tick tick;
    ArrayList<Long> testtoken = new ArrayList<Long>(Arrays.asList((long)1207553, 2714625L));

    public MykiteTicker(String accessToken, String apiKey) {
        super(accessToken, apiKey);
    }

    public void checkrun(){
        int j;
        Log.d("Ticks"," We are not in for loop");
        for (j=0;j<5;j++) {
            Log.d("Ticks"," We are in for loop");
            //InstrumentT[j] = fetchDataFromSQL()[j][1];

        }
    }


    @Override
    public void setOnConnectedListener(OnConnect listener) {
        Toast.makeText(ctxt,"Connected",Toast.LENGTH_LONG).show();
        checkrun();
        subscribe(testtoken);
    }

    @Override
    public void setOnDisconnectedListener(OnDisconnect listener) {
        unsubscribe(testtoken);
    }

    @Override
    public void setOnTickerArrivalListener(OnTicks onTickerArrivalListener) {



        Log.d("Ticks"," We are here"+OnTicks.class);
    }
    //    public void setInstrumentArray(){
//        this.InstrumentArray = cursor.getString(cursor.getColumnIndex("Stock_Name"));
//    }
//
//    public void setTargetArray(float[] targetArray){
//        this.TargetArray = targetArray;
//    }
//
//    public void getStopLossArray(float[] stopLossArray) {
//        this.StopLossArray = stopLossArray;
//    }


}

