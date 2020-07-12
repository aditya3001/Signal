package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.Routes;
import com.zerodhatech.kiteconnect.kitehttp.KiteRequestHandler;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import com.zerodhatech.models.Tick;
import com.zerodhatech.models.User;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnTicks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {
    private String Profile_name;
    private String request_token;
    private String[] InstrumentT;
    private ArrayList<Long> InstrumentToken;
    private String[][] data;

    TextView textview1,textview2;
    String public_token;
    MyDataBase myDb;

    MykiteTicker mykiteTicker;
    Cursor cur;
    Instrument instrument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent intent = getIntent();



        MyThread thread = new MyThread(intent);
        thread.start();
        Log.d("Activity 4","We are in Activity 4");
//        try {
//            //need to keep it hidden
//            String api_secret_key = "";
//            request_token = intent.getStringExtra("request_token");
//            User user = kiteSdk.generateSession(request_token, api_secret_key);
//            public_token = user.publicToken;
//            SharedPreferences sharedPref = getParent().getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("access_token", user.accessToken);
//            editor.commit();
//        } catch (KiteException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        SharedPreferences sharedPref = getParent().getPreferences(Context.MODE_PRIVATE);
//        String accessToken = sharedPref.getString("access_token", "abc");



        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);


        textview1.setText("Hi "+ thread.Profile_name);
        textview1.setText("Woah! You have got: "+ thread.Current_balance);


//        mykiteTicker = new MykiteTicker(kiteSdk.getAccessToken(),kiteSdk.getApiKey());

//        myDb = new MyDataBase(this);
//        data = fetchDataFromSQL();
//        final Mynotificationmanager mynotificationmanager = new Mynotificationmanager(this);
//        mykiteTicker.setOnTickerArrivalListener(new OnTicks() {
//            @Override
//            public void onTicks(ArrayList<Tick> arrayList) {
//                int i = 0;
//                for (i=0;i<data.length;i++){
//                    Long tok = Long.parseLong(data[i][4]);
//                    float tgt = Float.parseFloat(data[i][2]);
//                    float stpl = Float.parseFloat(data[i][3]);
//
//                    int j=0;
//                    for(j=0;j<arrayList.size();j++){
//                        if(tok = (((arrayList.get(j).getInstrumentToken()))){
//                            if (tgt<=arrayList.get(j).getClosePrice()){
//                                mynotificationmanager.showNotification("");
//                            }
//                        }
//                    }
//                }
//            }
//        });
//        mykiteTicker.setOnConnectedListener(new OnConnect() {
//            @Override
//            public void onConnected() {
//                int j = 0;
//                for (j=0;j<fetchDataFromSQL().length;j++) {
//                    InstrumentT[j] =fetchDataFromSQL()[j][1];
//                }
//                try {
//                    mapToken(InstrumentT);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (KiteException e) {
//                    e.printStackTrace();
//                }
//                mykiteTicker.subscribe(InstrumentToken);
//            }
//        });
//
//        mykiteTicker.setOnDisconnectedListener(new OnDisconnect() {
//            @Override
//            public void onDisconnected() {
//                mykiteTicker.unsubscribe(InstrumentToken);
//            }
//        });
//    }
//
//
//
//
//    private ArrayList<Long> mapToken(String[] ins) throws IOException, JSONException, KiteException {
//        final List<Instrument> instruments = kiteSdk.getInstruments("NSE");
//        int i = 0;
//        for (i=0;i<ins.length;i++){
//            int j=0;
//            for(j=0;j<instruments.size();j++){
//                if(ins[i].equals(instruments.get(j).name)){
//                    InstrumentToken.set(i, instruments.get(j).getInstrument_token());
//                    data[i][4] = String.valueOf(instruments.get(j).getInstrument_token());
//                }
//
//            }
//        }
//        return InstrumentToken;
//    }
//
//    public String[][] fetchDataFromSQL(){
//        cur = myDb.getData();
//        String data[][] = new String[cur.getCount()][cur.getColumnCount()];
//
//        if (cur != null) {
//            int i = 0;
//            while (cur.moveToNext()) {
//                int j = 0;
//                while (j < cur.getColumnCount()) {
//                    data[i][j] = cur.getString(j);
//                    j++;
//                }
//                i++;
//                cur.moveToNext();
//            }
//            cur.close();
//        }
//
//        return data;
    }
}
