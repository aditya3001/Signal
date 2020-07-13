//package com.example.signal;
//
//import android.content.Context;
//import android.database.Cursor;
//
//import com.zerodhatech.kiteconnect.KiteConnect;
//import com.zerodhatech.ticker.KiteTicker;
//import com.zerodhatech.ticker.OnConnect;
//import com.zerodhatech.ticker.OnTicks;
//
//import java.util.ArrayList;
//
//public class MykiteTicker extends KiteTicker {
//    private String InstrumentArray;
//    private float[] TargetArray;
//    private float[] StopLossArray;
//    public MykiteTicker(String accessToken, String apiKey) {
//        super(accessToken, apiKey);
//    }
//    MyDataBase myDataBase = new MyDataBase(null);
//    Cursor cursor = myDataBase.getData();
//
//
//
//    @Override
//    public void setOnConnectedListener(OnConnect listener) {
//
//    }
//
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
//
//
//}
//
