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
import android.widget.ListView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.zerodhatech.kiteconnect.KiteConnect;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private TextView txt;
    private Button signin;
    public KiteConnect kite = new KiteConnect("o2u3tpulm3z3agny");
//    MyFireBaseMessagingManager myFireBaseMessagingManager = new MyFireBaseMessagingManager();
//    private List<String> title;
//    private List<String> body ;
//    private ListView listview;
//    ArrayList<JavaLayout> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent i = getIntent();
        signin = findViewById(R.id.button2);
//        listview = findViewById(R.id.listview);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(v);

            }
        });
        kite.setUserId("QM7226");

//        title = myFireBaseMessagingManager.title;
//        body = myFireBaseMessagingManager.body;
//        if (title.size() > 0) {
//            int j;
//            for (j = 0; j < title.size(); j++) {
//                items.add(new JavaLayout(title.get(j), body.get(j)));
//            }
//            listview.setAdapter(new CustomAdapter(Main2Activity.this, R.layout.my_list_item, items));
//        }
//


    }

    private void SignIn(View v) {
        String url = this.kite.getLoginURL();
        Intent intent  = new Intent(getApplicationContext(),Main3Activity.class);
        intent.putExtra("links",url);
        startActivity(intent);
    }
}
