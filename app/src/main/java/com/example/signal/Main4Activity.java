package com.example.signal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.AsyncTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.Routes;
import com.zerodhatech.kiteconnect.kitehttp.KiteRequestHandler;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import com.zerodhatech.models.Tick;
import com.zerodhatech.models.User;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnTicks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.signal.Mynotificationmanager.CHANNEL_ID;

public class Main4Activity extends AppCompatActivity {
    private String Profile_name;
    private String request_token;
    private String[][] data;
    KiteConnect kiteSdk;
    User user = null;
    private String AccessToken = "";
    float Current_balance;
    TextView textview1,textview2;
    ArrayList<String> Stock_Name = new ArrayList<String>();
    ArrayList<String> HitArray = new ArrayList<String>();
    ArrayList<Float> Target = new ArrayList<Float>();
    ArrayList<Float> Stop_Loss = new ArrayList<Float>();
    ArrayList<Long> InstrumentTokendb = new ArrayList<Long>();
    ArrayList<String> ActionTaken = new ArrayList<String>();

    EditText editCompanyName,stopLossValue,targetValue,actionview;
    ArrayList<Long> Ids = new ArrayList<>();
    Button sendButton;
    Button seeDetails;
    Switch switchbutton1;
    int Flag = 10;
    public int DONE=1;
    String public_token="";
    MyDataBase myDb;
    notifyMe notify;
    KiteTicker mykiteTicker;
    List<Instrument> subInstruments = new ArrayList<>();
    Myasync1 myasync1 = new Myasync1();
    public Main4Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        myDb = new MyDataBase(this);
        notify = new notifyMe(this);
        final Intent intent = getIntent();
        request_token = intent.getStringExtra("request_token");
        myDb = new MyDataBase(this);
        Cursor cursor = getAllItems();
        fetchDataFromSQL(cursor);
        Log.d("fetchStockFromSQL"," "+Stock_Name);
        kiteSdk = new KiteConnect(getString(R.string.apikey));
        final Myasync myasync = new Myasync();
        myasync.execute("me");
        editCompanyName = (EditText)findViewById(R.id.editTextCompanyName);
        stopLossValue =  (EditText)findViewById(R.id.stopLossValue);
        targetValue = (EditText)findViewById(R.id.targetValue);
        sendButton = (Button)findViewById(R.id.sendButton);
        actionview = findViewById(R.id.actiontaken);
        switchbutton1 = findViewById(R.id.switch1);
        seeDetails = findViewById(R.id.button3);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyName = editCompanyName.getText().toString();
                String stopLoss = stopLossValue.getText().toString();
                String target = targetValue.getText().toString();
                String action = actionview.getText().toString();
                if(companyName.isEmpty() || stopLoss.isEmpty() || target.isEmpty() || action.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Empty Field Not Valid",Toast.LENGTH_LONG).show();
                }else {
                    if (myDb.insertData(companyName, target, stopLoss,action)) {
                        editCompanyName.getText().clear();
                        stopLossValue.getText().clear();
                        targetValue.getText().clear();
                        actionview.getText().clear();
                        Cursor cursor = getAllItems();
                        fetchDataFromSQL(cursor);
                        int i=0;
                        for( i = 0; i<Stock_Name.size();i++) {
                            if (Stock_Name.get(i).equalsIgnoreCase(companyName)) {
                                break;
                            }
                        }
                        mapToken(i);
                    }
                }
            }
        });

        seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext() ,Main5Activity.class);
                startActivity(intent1);
            }
        });


        switchbutton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(myasync.getStatus() == AsyncTask.Status.FINISHED){
                if(isChecked) {
                    Flag = 1;
                    syncTasks();
//                    myasync1.execute("me");

                } else {
                    Flag = 0;
                    Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_LONG).show();
                    myasync1.cancel(true);
                    mykiteTicker.unsubscribe(InstrumentTokendb);
                    mykiteTicker.disconnect();
                }
            }else{
                    Toast.makeText(getApplicationContext(),"background process pending",Toast.LENGTH_LONG).show();
                }
            }
        });
}
//    private void fetchStocksFromSQL(Cursor curs) {
//        if (curs != null) {
//            int i = 0;
//
//            Stock_Name.clear();
//
//            while (curs.moveToNext() && !curs.getString(curs.getColumnIndex(MyDataBase.Col_2)).isEmpty()) {
//                try {
////                    Log.d("fetchdatafromSql",""+curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
//                    Stock_Name.add(i, curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//
//                }
//            }
//            i++;
//            curs.moveToNext();
//        }
//        curs.close();
//    }
    private void fetchDataFromSQL(Cursor curs) {
        if (curs != null) {
            int i = 0;

            Stock_Name.clear();Target.clear();Stop_Loss.clear();Ids.clear();HitArray.clear();ActionTaken.clear();InstrumentTokendb.clear();

            while(curs.moveToNext() && !curs.getString(curs.getColumnIndex(MyDataBase.Col_2)).isEmpty()) {
                try {
                    if("Neutral".equalsIgnoreCase(curs.getString(curs.getColumnIndex(MyDataBase.Col_7)))) {
                        Log.d("fetchdatafromSql", "" + curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
                        Stock_Name.add(i, curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
                        Target.add(i, Float.parseFloat(curs.getString(curs.getColumnIndex(MyDataBase.Col_3))));
                        Stop_Loss.add(i, Float.parseFloat(curs.getString(curs.getColumnIndex(MyDataBase.Col_4))));
                        InstrumentTokendb.add(i, Long.parseLong(curs.getString(curs.getColumnIndex(MyDataBase.Col_5))));
                        ActionTaken.add(i, curs.getString(curs.getColumnIndex(MyDataBase.Col_8)));
                        Ids.add(i, curs.getLong(curs.getColumnIndex(MyDataBase.Col_1)));
                        HitArray.add(i, curs.getString(curs.getColumnIndex(MyDataBase.Col_7)));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();

                }
            }
                i++;
                curs.moveToNext();
            }
            curs.close();
        myDb.close();
        }

    private Cursor getAllItems() {
        return myDb.getData();
    }
    public class Myasync extends AsyncTask<String , Void, Void> {
        @Override
        protected void onPreExecute() {
            kiteSdk.setUserId(getString(R.string.userid));
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String api_secret_key = getString(R.string.secret);
                user = kiteSdk.generateSession(request_token, api_secret_key);
            } catch (KiteException | IOException | JSONException e) {
                e.printStackTrace();
            }
            textview1 = findViewById(R.id.textView1);
            textview2 = findViewById(R.id.textView2);
            if(user!=null) {
                public_token = user.publicToken;
                AccessToken = user.accessToken;
            }
            kiteSdk.setAccessToken(AccessToken);
            kiteSdk.setPublicToken(public_token);
            Margin margin = null;
            try {
                Profile profile = kiteSdk.getProfile();
                margin = kiteSdk.getMargins("equity");
                Profile_name = profile.userName;
                Current_balance = Float.parseFloat(margin.net);
                subInstruments = kiteSdk.getInstruments("NSE");
                mapToken();

            } catch (KiteException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            textview1.setText("Hi: "+Profile_name);
            textview2.setText("Amount "+Current_balance);
        }
    }
    public class Myasync1 extends AsyncTask<String , Void, Void> {

        @Override
        protected void onPreExecute() {
            mykiteTicker = new KiteTicker(kiteSdk.getAccessToken(),kiteSdk.getApiKey());
            mykiteTicker.setOnConnectedListener(new OnConnect() {
                @Override
                public void onConnected() {
                    Log.d("Connected","We are in connected");
                    try {
                        Cursor cursor = getAllItems();
                        fetchDataFromSQL(cursor);
                        mykiteTicker.subscribe(InstrumentTokendb);
                    } catch ( Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Mapping Failed",Toast.LENGTH_LONG).show();
                    }
                }
            });

            mykiteTicker.setOnTickerArrivalListener(new OnTicks() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTicks(ArrayList<Tick> arrayList) {
                                Log.d("Ticks"," We are here"+arrayList.size());
//                                Cursor cursor = getAllItems();
//                                fetchDataFromSQL(cursor);
//                    try {
//                        mapToken();
//                        mykiteTicker.subscribe(InstrumentTokendb);
//                    } catch (IOException | JSONException | KiteException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(),"Mapping Failed",Toast.LENGTH_LONG).show();
//                    }
                int i;
                for (i=0;i<Stock_Name.size();i++){
                    int j=0;
                    for(j=0;j<arrayList.size();j++){
                        Log.d("Ticks"," We are in for loop");
                        if(InstrumentTokendb.get(i) == (arrayList.get(j).getInstrumentToken()) && ActionTaken.get(i).equalsIgnoreCase("BUY")){
                            Log.d("Ticks"," We are in if"+DONE);
                            if (Target.get(i)<=arrayList.get(j).getLastTradedPrice()&& "Neutral".equalsIgnoreCase(HitArray.get(i))){
                                notify.showNotification(Stock_Name.get(i), "Target " + Target.get(i) + " Hit");
                                myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)), Float.toString(Stop_Loss.get(i)), "Target", InstrumentTokendb.get(i));
                                DONE=0;
                            }else if(Stop_Loss.get(i)>=arrayList.get(j).getLastTradedPrice()&& "Neutral".equalsIgnoreCase(HitArray.get(i))){
                                    notify.showNotification(Stock_Name.get(i), "Stop_Loss " + Stop_Loss.get(i) + " Hit");
                                myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)), Float.toString(Stop_Loss.get(i)), "Stop Loss", InstrumentTokendb.get(i));
                                DONE = 0;
                        }
                    }else if(InstrumentTokendb.get(i) == (arrayList.get(j).getInstrumentToken()) && ActionTaken.get(i).equalsIgnoreCase("SELL")){
                            if (Target.get(i)>=arrayList.get(j).getLastTradedPrice()&& "Neutral".equalsIgnoreCase(HitArray.get(i))){
                                notify.showNotification(Stock_Name.get(i), "Target " + Target.get(i) + " Hit");
                                myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)), Float.toString(Stop_Loss.get(i)), "Target", InstrumentTokendb.get(i));
                                DONE=0;
                            }else if(Stop_Loss.get(i)<=arrayList.get(j).getLastTradedPrice()&& "Neutral".equalsIgnoreCase(HitArray.get(i))){
                                notify.showNotification(Stock_Name.get(i), "Stop_Loss " + Stop_Loss.get(i) + " Hit");
                                myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)), Float.toString(Stop_Loss.get(i)), "Stop Loss", InstrumentTokendb.get(i));
                                DONE = 0;
                            }

                        }
                }
            }
                }
            });
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (Flag==1){
                mykiteTicker.setTryReconnection(true);
                try {
                    mykiteTicker.setMaximumRetries(10);
                    mykiteTicker.connect();
                } catch (KiteException e) {
                    e.printStackTrace();
                }
            }else if (Flag==0){
                mykiteTicker.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mykiteTicker.isConnectionOpen()){
                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void syncTasks() {
        try {
            if (myasync1.getStatus() != AsyncTask.Status.RUNNING){   // check if asyncTasks is running
                myasync1.cancel(true); // asyncTasks not running => cancel it
                myasync1 = new Myasync1(); // reset task
                myasync1.execute(); // execute new task (the same task)
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity_TSK", "Error: "+e.toString());
        }
    }
    private void mapToken()  {
        int i;
        for (i=0;i<Stock_Name.size();i++){
            int j=0;
            for(j=0;j<subInstruments.size();j++){
                if(Stock_Name.get(i).equalsIgnoreCase(subInstruments.get(j).tradingsymbol)){
//                    InstrumentTokendb.add(i, subInstruments.get(j).getInstrument_token());
                    myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)),
                            Float.toString(Stop_Loss.get(i)), HitArray.get(i), subInstruments.get(j).getInstrument_token());
                    break;

                }
            }
        }
//        Log.d("mapToken","Token is"+InstrumentTokendb.get(0));
    }

    private void mapToken(int i) {

        for(int j=0;j<subInstruments.size();j++){
            if(Stock_Name.get(i).equalsIgnoreCase(subInstruments.get(j).tradingsymbol)){
                myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)),
                        Float.toString(Stop_Loss.get(i)), HitArray.get(i), subInstruments.get(j).getInstrument_token());
                break;

            }
        }
    }
}
