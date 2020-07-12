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
    String Action;
    String Request_token;
    String ur;
    String api_secret_key = new String("abc");  //need to keep it hidden
    String status = new String("status=");
    String action = new String("action=");
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
        KiteConnect kiteSdk = new KiteConnect("tjcby5dbku38j51o");
        web1.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                parse_url(url, request_token, status, action);
                if (Status.equals("success")) {
                    Intent intent_1 = new Intent(getApplicationContext(), Main4Activity.class);
                    Log.d("Success","We are here");
                    Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_LONG).show();
                    intent_1.putExtra("request_token",  request_token);
                    //intent_1.putExtra("kite1", (Parcelable) kiteSdk);
                    //intent_1.putExtra("profile", (Parcelable) kiteSdk.getProfile());
                    startActivity(intent_1);

                }else {
                    super.onPageFinished(view, url);
                }
            }
        });
        web1.loadUrl(website);
        web1.getSettings().setJavaScriptEnabled(true);





//        try {
//            User user = kiteSdk.generateSession(this.Request_token, api_secret_key);
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
//        kiteSdk.setAccessToken(accessToken);
//        kiteSdk.setPublicToken(public_token);

    }
    public void parse_url(String url, String req, String sta, String act) {
        Log.d("In parse_url","Url is "+url);
        if (url != null) {

            int Status_index = url.indexOf(sta) + sta.length();
            this.Status = url.substring(Status_index);

            if (this.Status.equals("success")) {
                int request_token_index = url.indexOf(req) + req.length();
                int index_2 = url.indexOf("&");
                this.Request_token = url.substring(request_token_index, index_2);

                int Action_index = url.indexOf(act) + act.length();
                int index_3 = url.indexOf("&", index_2);
                this.Action = url.substring(Action_index);
            }

        }
    }
}
