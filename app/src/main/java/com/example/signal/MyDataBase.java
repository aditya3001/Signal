package com.example.signal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDataBase extends SQLiteOpenHelper {
    public static final String Db_Name = "App_Db";
    public static final String Tb_Name = "Input_Details";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "Stock_Name";
    public static final String Col_3 = "Target";
    public static final String Col_4 = "Stop_Loss";
    public static final String Col_5 = "Instrument_Token";
    public static final String Col_6 = "Time_Stamp";
    public static final String Col_8 = "Action_Taken";
    public static final String Col_7 = "Hit";


    String[] Stock_name;
    Cursor res;


    public MyDataBase(@Nullable Context context) {
        super(context, Db_Name, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Tb_Name+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,Stock_Name TEXT,Target float,Stop_Loss float,Instrument_Token long Default 0, Time_Stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, Hit TEXT, Action_Taken TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+Tb_Name);
        onCreate(db);
    }

    public boolean insertData(String StckNme, String Tgt,String Stp_ls , String Action){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,StckNme);
        contentValues.put(Col_3,Tgt);
        contentValues.put(Col_4,Stp_ls);
        contentValues.put(Col_8,Action);
        contentValues.put(Col_7,"Neutral");

        long result = db.insert(Tb_Name,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean updateData(long id,String StckNme, String Tgt,String Stp_ls, String hit ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,StckNme);
        contentValues.put(Col_3,Tgt);
        contentValues.put(Col_4,Stp_ls);
        contentValues.put(Col_7,hit);
        long result = db.update(Tb_Name, contentValues, Col_1 + "=" + id, null);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean updateData(long id,String StckNme, String Tgt,String Stp_ls, String hit, long instrumentToken ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,StckNme);
        contentValues.put(Col_3,Tgt);
        contentValues.put(Col_4,Stp_ls);
        contentValues.put(Col_5,instrumentToken);
        contentValues.put(Col_7,hit);
        long result = db.update(Tb_Name, contentValues, Col_1 + "=" + id, null);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            res = db.query(Tb_Name, null,null,null,null,null,Col_6 + " DESC");

        }catch(SQLException e) {
            Log.d("DATABASE", e.toString());
        }
        return res;
    }
    public void deleteData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(Tb_Name,
                    Col_1 + "=" + id, null);
        } catch (Exception e) {
            Log.d("DATABASE DELETE", e.toString());
        }
    }

}

