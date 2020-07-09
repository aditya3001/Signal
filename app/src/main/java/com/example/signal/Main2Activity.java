package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.zerodhatech.kiteconnect.KiteConnect;

public class Main2Activity extends AppCompatActivity {
    private TextView txt;
    private Button signin;
    public Mykiteconnect kite = new Mykiteconnect("tjcby5dbku38j51o");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent i = getIntent();
        txt = findViewById(R.id.textPython);
        signin = findViewById(R.id.button2);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(v);

            }
        });


//        String str = pythonprocess();
        //        startPython();
//        txt.setText(str);


    }

    private void SignIn(View v) {
        String url = this.kite.getLoginURL();
        Intent intent  = new Intent(getApplicationContext(),Main3Activity.class);
        intent.putExtra("links",url);
        startActivity(intent);
    }

//    private void startPython() {
//        if (! Python.isStarted()) {
//            Python.start(new AndroidPlatform(this));
//        }
//    }

//    private String pythonprocess(){
//        Python ins = Python.getInstance();
//        PyObject val = ins.getModule("livevol");
//        Log.d("Python","We are here");
//        return val.callAttr("hello","aditya").toString();
//
//    }
}
