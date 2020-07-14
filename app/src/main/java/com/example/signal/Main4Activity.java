package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
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

public class Main4Activity extends AppCompatActivity {
    private String Profile_name;
    private String request_token;
    private ArrayList<Long> InstrumentToken;
    private String[][] data;
    KiteConnect kiteSdk = new KiteConnect("o2u3tpulm3z3agny");
    User user = null;
    private String AccessToken = "";
    float Current_balance;
    TextView textview1,textview2;
    ArrayList<Long> testtoken = new ArrayList<Long>(Arrays.asList((long)1207553, 2714625L));

    ArrayList<String> Stock_Name;
    ArrayList<Float> Target;
    ArrayList<Float> Stop_Loss;
    ArrayList<Long> InstrumentTokendb;

    EditText editCompanyName,stopLossValue,targetValue;
    Button sendButton;
    Button seeDetails;
    Switch switchbutton1;
    int Flag = 10;
    public int DONE;
    String public_token="";
    MyDataBase myDb;

    KiteTicker mykiteTicker;

    Instrument instrument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        myDb = new MyDataBase(this);
        final Intent intent = getIntent();
        request_token = intent.getStringExtra("request_token");
        final Mynotificationmanager mynotificationmanager = new Mynotificationmanager(this);
        myDb = new MyDataBase(this);
        Cursor cursor = getAllItems();
        fetchDataFromSQL(cursor);
        Log.d("fetchDataFromSQL"," "+Stock_Name);
        final Myasync myasync = new Myasync();
        myasync.execute("me");

        editCompanyName = (EditText)findViewById(R.id.editTextCompanyName);
        stopLossValue =  (EditText)findViewById(R.id.stopLossValue);
        targetValue = (EditText)findViewById(R.id.targetValue);
        sendButton = (Button)findViewById(R.id.sendButton);
        switchbutton1 = findViewById(R.id.switch1);
        seeDetails = findViewById(R.id.button3);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String companyName = editCompanyName.getText().toString();
                String stopLoss = stopLossValue.getText().toString();
                String target = targetValue.getText().toString();
                if(myDb.insertData(companyName,target,stopLoss)){
                    
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
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
        final Myasync1 myasync1 = new Myasync1();

        switchbutton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(myasync.getStatus() == AsyncTask.Status.FINISHED){
                if(isChecked) {
                    Flag = 1;
                    myasync1.execute("me");

                } else {
                    Flag = 0;
                    Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_LONG).show();
                    myasync1.cancel(true);
                    mykiteTicker.unsubscribe(testtoken);
                    mykiteTicker.disconnect();
                }
            }else{
                    Toast.makeText(getApplicationContext(),"background process pending",Toast.LENGTH_LONG).show();
                }
            }
        });
}

    private void fetchDataFromSQL(Cursor curs) {
        if (curs != null) {
            int i = 0;
            while (curs.moveToNext()) {
                Stock_Name.add(i,curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
                Target.add(i,Float.parseFloat(curs.getString(curs.getColumnIndex(MyDataBase.Col_3))));
                Stop_Loss.add(i,Float.parseFloat(curs.getString(curs.getColumnIndex(MyDataBase.Col_4))));
                InstrumentTokendb.add(i,Long.parseLong(curs.getString(curs.getColumnIndex(MyDataBase.Col_5))));
                }
                i++;
                curs.moveToNext();
            }
            curs.close();
        }




    private Cursor getAllItems() {
        return myDb.getData();
    }
    public class Myasync extends AsyncTask<String , Void, Void> {
        @Override
        protected void onPreExecute() {
            kiteSdk.setUserId("QM7226");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String api_secret_key = "";

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
                    mykiteTicker.subscribe(testtoken);
                    Log.d("Connected","We are in connected");

//                try {
//                    mapToken(InstrumentT);
//                } catch (IOException | KiteException | JSONException e) {
//                    e.printStackTrace();
//                }
//                mykiteTicker.subscribe(InstrumentToken);
                }
            });

            mykiteTicker.setOnTickerArrivalListener(new OnTicks() {
                @Override
                public void onTicks(ArrayList<Tick> arrayList) {
                    Log.d("Ticks"," We are here"+arrayList.size());
                int i;
                for (i=0;i<data.length;i++){

//                    String Stock_Name = data[i][1];
//                    Long tok = Long.parseLong(data[i][4]);
//                    float tgt = Float.parseFloat(data[i][2]);
//                    float stpl = Float.parseFloat(data[i][3]);
//
//                    int j=0;
//                    for(j=0;j<arrayList.size();j++){
//                        if(tok == (((arrayList.get(j).getInstrumentToken())))){
//                            if (tgt<=arrayList.get(j).getClosePrice()){
//                                mynotificationmanager.showNotification(Stock_Name,"Target "+tgt+" Hit",intent);
//                            }else{
//                                if (stpl>=arrayList.get(j).getClosePrice()){
//                                    mynotificationmanager.showNotification(Stock_Name,"Target "+stpl+" Hit",intent);
//                            }
//                        }
//                    }
//                }
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

    private ArrayList<Long> mapToken(String[] ins) throws IOException, JSONException, KiteException {
        final List<Instrument> instruments = kiteSdk.getInstruments("NSE");
        int i = 0;
        for (i=0;i<ins.length;i++){
            int j=0;
            for(j=0;j<instruments.size();j++){
                if(ins[i].equals(instruments.get(j).name)){
                    InstrumentToken.set(i, instruments.get(j).getInstrument_token());
                    data[i][4] = String.valueOf(instruments.get(j).getInstrument_token());
                }

            }
        }
        return InstrumentToken;
    }
}
