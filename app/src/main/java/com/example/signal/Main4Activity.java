package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;

import org.json.JSONException;

import java.io.IOException;

public class Main4Activity extends AppCompatActivity {
    private String Profile_name;
    private String request_token;
    Margin.Available Current_balance;
    TextView textview1,textview2;
    EditText editCompanyName,stopLossValue,targetValue;
    Button sendButton;
    Switch switchbutton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        final Intent intent = getIntent();
        KiteConnect kiteSdk = new KiteConnect("tjcby5dbku38j51o");

//        Mykiteconnect kiteSdk = new Mykiteconnect("tjcby5dbku38j51o");
        request_token = intent.getParcelableExtra("request_token");
//        try {
//            Profile profile = kiteSdk.getProfile();
//            Margin margin = kiteSdk.getMargins("equity");
//            this.Profile_name = profile.userName;
//            this.Current_balance = margin.available;
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
                Intent intent1 = new Intent(getApplicationContext() ,Main5Activity.class);
                startActivity(intent1);
            }
        });



    }
}
