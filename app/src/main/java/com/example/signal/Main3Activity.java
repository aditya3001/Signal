package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;

import org.json.JSONException;

import java.io.IOException;

public class Main3Activity extends AppCompatActivity {
    WebView web1;
    String Status = "Failed";
    String Request_token = "abc";
    String status = new String("status=");
    String request_token = new String("request_token=");

    @Override
    public void onBackPressed() {
        if (web1.canGoBack()) {
            web1.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        web1 = findViewById(R.id.web);
        Intent intent = getIntent();
        String website = intent.getStringExtra("links");

        web1.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                parse_url(url, request_token, status);
                if (Status.equals("success")) {
                    Intent intent_1 = new Intent(getApplicationContext(), Main4Activity.class);
                    Log.d("Parsed",Request_token);
                    Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_LONG).show();
                    intent_1.putExtra("request_token",  Request_token);
                    startActivity(intent_1);
                }else {
                    super.onPageFinished(view, url);
                }
            }
        });
        web1.loadUrl(website);
        web1.getSettings().setJavaScriptEnabled(true);

    }
    public void parse_url(String url, String req, String sta) {
        Log.d("In parse_url","Url is "+url);
        if (url != null) {

            int Status_index = url.indexOf(sta) + sta.length();
            Status = url.substring(Status_index,Status_index+7);

            if (Status.equals("success")) {
                int request_token_index = url.indexOf(req) + req.length();
                int index_2 = url.indexOf("&",request_token_index);
                if(index_2<0){
                    Request_token = url.substring(request_token_index);
                }else {
                    Request_token = url.substring(request_token_index, index_2);
                }
            }


        }
    }
}
