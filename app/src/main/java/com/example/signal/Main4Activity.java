package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import com.zerodhatech.models.User;

import org.json.JSONException;

import java.io.IOException;

public class Main4Activity extends AppCompatActivity {
    private String Profile_name;
    private String request_token;
    Margin.Available Current_balance;
    TextView textview1,textview2;
    Switch switchbutton1;
    String api_secret_key = new String("abc");  //need to keep it hidden
    String public_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent intent = getIntent();
        KiteConnect kiteSdk = new KiteConnect("tjcby5dbku38j51o");
        request_token = intent.getParcelableExtra("request_token");

        try {
            User user = kiteSdk.generateSession(request_token, api_secret_key);
            public_token = user.publicToken;
            SharedPreferences sharedPref = getParent().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("access_token", user.accessToken);
            editor.commit();
        } catch (KiteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getParent().getPreferences(Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString("access_token", "abc");
        kiteSdk.setAccessToken(accessToken);
        kiteSdk.setPublicToken(public_token);

        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);


        try {

            Profile profile = kiteSdk.getProfile();
            Margin margin = kiteSdk.getMargins("equity");
            this.Profile_name = profile.userName;
            this.Current_balance = margin.available;
            textview1.setText("Hi "+ this.Profile_name);
            textview1.setText("Woah! You have got: "+ this.Current_balance);

        } catch (KiteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
