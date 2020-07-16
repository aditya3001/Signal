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
    ArrayList<Long> Ids = new ArrayList<>();
    EditText editCompanyName,stopLossValue,targetValue;
    Button sendButton;
    Button seeDetails;
    Switch switchbutton1;
    int Flag = 10;
    public int DONE=1;
    String public_token="";
    MyDataBase myDb;
    notifyMe notify;
    KiteTicker mykiteTicker;


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
        Log.d("fetchDataFromSQL"," "+Stock_Name);
        kiteSdk = new KiteConnect(getString(R.string.apikey));
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
                if(companyName.isEmpty() || stopLoss.isEmpty() || target.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Empty Field Not Valid",Toast.LENGTH_LONG).show();
                }else {
                    if (myDb.insertData(companyName, target, stopLoss)) {
                        editCompanyName.getText().clear();
                        stopLossValue.getText().clear();
                        targetValue.getText().clear();
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
                    mykiteTicker.unsubscribe(InstrumentTokendb);
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
            Stock_Name.clear();Target.clear();Stop_Loss.clear();InstrumentTokendb.clear();Ids.clear();HitArray.clear();
            while(curs.moveToNext()) {
                try {
                    Log.d("fetchdatafromSql", " error" + curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
                    Stock_Name.add(i, curs.getString(curs.getColumnIndex(MyDataBase.Col_2)));
                    Target.add(i, Float.parseFloat(curs.getString(curs.getColumnIndex(MyDataBase.Col_3))));
                    Stop_Loss.add(i, Float.parseFloat(curs.getString(curs.getColumnIndex(MyDataBase.Col_4))));
                    InstrumentTokendb.add(i, Long.parseLong(curs.getString(curs.getColumnIndex(MyDataBase.Col_5))));
                    Ids.add(i,curs.getLong(curs.getColumnIndex(MyDataBase.Col_1)));
                    HitArray.add(i,curs.getString(curs.getColumnIndex(MyDataBase.Col_7)));

                } catch (NumberFormatException e) {
                    e.printStackTrace();

                }
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
                        mapToken();
                        mykiteTicker.subscribe(InstrumentTokendb);
                    } catch (IOException | JSONException | KiteException e) {
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
                    Cursor cursor = getAllItems();
                    fetchDataFromSQL(cursor);
                    if (DONE==1){
                        notify.showNotification("Signal","Check");
                        DONE=0;
                    }
//                    int i;
//                    for (i=0;i<Stock_Name.size();i++) {
//                        int j = 0;
//                        for (j = 0; j < 4; j++) {
//                            Log.d("Ticks", " We are in for loop"+DONE);
////                        if(InstrumentTokendb.get(i) == (arrayList.get(j).getInstrumentToken()))
//                            {
//                                if (Target.get(i) <= 100 && DONE == 1) {
//                                    notify.showNotification(Stock_Name.get(i), "Target " + Target.get(i) + " Hit");
//                                    DONE = 0;
//                                } else {
//                                    if (Stop_Loss.get(i) >= 100 && DONE == 1) {
//                                        notify.showNotification(Stock_Name.get(i), "Stop_Loss " + Target.get(i) + " Hit");
//                                        DONE = 0;
//                                    }
//                                }
//                            }
//                        }
//                    }
                int i;
                for (i=0;i<Stock_Name.size();i++){
                    int j=0;

//                    for(j=0;j<arrayList.size();j++)
                    {
                        Log.d("Ticks"," We are in for loop"+DONE);
//                        if(InstrumentTokendb.get(i) == (arrayList.get(j).getInstrumentToken()))
                        {
                            if (Target.get(i)<=100 && "Neutral".equalsIgnoreCase(HitArray.get(i))) {
                                notify.showNotification(Stock_Name.get(i), "Target " + Target.get(i) + " Hit");
                                myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)), Float.toString(Stop_Loss.get(i)), "Target");

                            }else{
                                if (Stop_Loss.get(i)>=100 && "Neutral".equalsIgnoreCase(HitArray.get(i))) {
                                    notify.showNotification(Stock_Name.get(i), "Stop_Loss " + Stop_Loss.get(i) + " Hit");
                                    myDb.updateData(Ids.get(i), Stock_Name.get(i), Float.toString(Target.get(i)), Float.toString(Stop_Loss.get(i)), "Stop Loss");
                                }
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
    private void mapToken() throws IOException, JSONException, KiteException {
        final List<Instrument> instruments = kiteSdk.getInstruments("NSE");
        int i;
        Log.d("mapToken","Token is"+Stock_Name.get(0));
        for (i=0;i<Stock_Name.size();i++){
            int j=0;
            for(j=0;j<instruments.size();j++){
                if(Stock_Name.get(i).equals(instruments.get(j).tradingsymbol)){
                    InstrumentTokendb.set(i, instruments.get(j).getInstrument_token());
                }
            }
        }
        Log.d("mapToken","Token is"+InstrumentTokendb.get(0));
    }
}
