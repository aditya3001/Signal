package com.example.signal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.zerodhatech.kiteconnect.KiteConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView token_status;
    private static EditText email;
    private Button buttonRegister;
    private Button next;
    public String Key_Access_Token = "token";
    private BroadcastReceiver broadcastReceiver;
    private static final String url = "http://192.168.1.100/FCM_MESSAGING/php_updated.php";




    @Override
    protected void onDestroy() {
        try{
            if(broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);

        }catch(Exception e){}
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        email = (EditText) findViewById(R.id.editText);
        token_status = findViewById(R.id.tokenview);
        if(sharedPrefManager.getToken()!=null){
            token_status.setText("Token Generated");
        }else{
            token_status.setText("Token Not Generated");
        }
        buttonRegister = (Button) findViewById(R.id.button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Onclick","Button is clicked");
                sendMail();
               sendTokentoServer();

            }
        });

        next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);

            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };


        registerReceiver(broadcastReceiver, new IntentFilter(MyFireBaseInstanceIdManager.TOKEN_BROADCAST));

    }
    private void sendMail(){
        final String EXTRA_EMAIL = email.getText().toString();
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String subject = "Registration Token";
        String message = sharedPrefManager.getToken();
        Log.d("sendTokentoServer","We got the email "+EXTRA_EMAIL);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EXTRA_EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        startActivity(Intent.createChooser(intent, "Choose an email client:"));
    }

    private void sendTokentoServer(){
        final String email_id = email.getText().toString();
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());

        if (TextUtils.isEmpty(email_id)){
            Toast.makeText(this,"Enter your email Please!", Toast.LENGTH_LONG).show();
        }if(sharedPrefManager.getToken()!=null){
            Log.d("StringRequest","We are declaring string request ");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("Response","We got the response");
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                Log.d("Response","We got the error");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Response","We got the Volleyerror "+error.getCause());
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    Log.d("Params","We are putting parameter "+sharedPrefManager.getToken()+email_id);
                    params.put("Registration_Token", sharedPrefManager.getToken());
                    params.put("Email", email_id);
                    return params;
                }
            };
            Log.d("Response","We are initializing requestQueue");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            Log.d("Response","We are adding StringRequest");
            requestQueue.add(stringRequest);
        }else{
            Log.d("Params","Token not generated");
            Toast.makeText(this,"Token Generation Failed", Toast.LENGTH_LONG).show();
        }
    }
}
