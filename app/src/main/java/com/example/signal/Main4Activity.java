package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Margin.Available Current_balance;
    TextView textview1,textview2;

    EditText editCompanyName,stopLossValue,targetValue;
    Button sendButton;
    Switch switchbutton1;

    String api_secret_key = new String("abc");  //need to keep it hidden
    String public_token;
    MyDataBase myDb;
    KiteConnect kiteSdk = new KiteConnect("tjcby5dbku38j51o");
    MykiteTicker mykiteTicker = new MykiteTicker(kiteSdk.getAccessToken(),kiteSdk.getApiKey());
    Cursor cur;
    KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(Proxy.NO_PROXY);
    Instrument instrument;
    Routes routes = new Routes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        myDb = new MyDataBase(this);
        Intent intent = getIntent();
//        KiteConnect kiteSdk = new KiteConnect("tjcby5dbku38j51o");
//        request_token = intent.getParcelableExtra("request_token");
//
//        try {
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
//
//        SharedPreferences sharedPref = getParent().getPreferences(Context.MODE_PRIVATE);
//        String accessToken = sharedPref.getString("access_token", "abc");
//        kiteSdk.setAccessToken(accessToken);
//        kiteSdk.setPublicToken(public_token);
//
//        textview1 = findViewById(R.id.textView1);
//        textview2 = findViewById(R.id.textView2);
//
//
//        try {
//
//            Profile profile = kiteSdk.getProfile();
//            Margin margin = kiteSdk.getMargins("equity");
//            this.Profile_name = profile.userName;
//            this.Current_balance = margin.available;
//            textview1.setText("Hi "+ this.Profile_name);
//            textview1.setText("Woah! You have got: "+ this.Current_balance);
//
//        } catch (KiteException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);
        textview1.setText("Hi ");
        textview1.setText(R.string.amount);
        editCompanyName = (EditText)findViewById(R.id.editTextCompanyName);
        stopLossValue =  (EditText)findViewById(R.id.stopLossValue);
        targetValue = (EditText)findViewById(R.id.targetValue);
        sendButton = (Button)findViewById(R.id.sendButton);

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
                Intent intent1 = new Intent(getApplicationContext() ,Main5Activity.class);
                startActivity(intent1);
            }
        });


        data = fetchDataFromSQL();
        final Mynotificationmanager mynotificationmanager = new Mynotificationmanager(this);
        mykiteTicker.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> arrayList) {
                int i = 0;
                for (i=0;i<data.length;i++){
                    Long tok = Long.parseLong(data[i][4]);
                    float tgt = Float.parseFloat(data[i][2]);
                    float stpl = Float.parseFloat(data[i][3]);

                    int j=0;
                    for(j=0;j<arrayList.size();j++){
                        if(tok == (((arrayList.get(j).getInstrumentToken()))))
                        {
                            if (tgt<=arrayList.get(j).getClosePrice()){
//                                mynotificationmanager.showNotification("");
                            }
                        }
                    }
                }
            }
        });
        mykiteTicker.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                int j = 0;
                for (j=0;j<fetchDataFromSQL().length;j++) {
                    InstrumentT[j] =fetchDataFromSQL()[j][1];
                }
                try {
                    mapToken(InstrumentT);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (KiteException e) {
                    e.printStackTrace();
                }
                mykiteTicker.subscribe(InstrumentToken);
            }
        });

        mykiteTicker.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                mykiteTicker.unsubscribe(InstrumentToken);
            }
        });
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

    public String[][] fetchDataFromSQL(){
        cur = myDb.getData();
        String data[][] = new String[cur.getCount()][cur.getColumnCount()];

        if (cur != null) {
            int i = 0;
            while (cur.moveToNext()) {
                int j = 0;
                while (j < cur.getColumnCount()) {
                    data[i][j] = cur.getString(j);
                    j++;
                }
                i++;
                cur.moveToNext();
            }
            cur.close();
        }

        return data;
    }
}
