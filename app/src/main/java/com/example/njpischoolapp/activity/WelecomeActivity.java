package com.example.njpischoolapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.njpischoolapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelecomeActivity extends AppCompatActivity {

    private Button count;
    private TextView callme;

    Timer time = new Timer();
    //计划
    int timeover = 4;
    String email,pwd;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welecome);

        time.schedule(task,1000,1000);
        count = (Button) findViewById(R.id.count);
        callme = (TextView) findViewById(R.id.callme);

        callme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:15094319303"));
                startActivity(intent);
            }
        });
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        email =sharedPreferences.getString("email","");
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeover-- ;
                    count.setText("-" + timeover + "-");
                    if (timeover < 1){
                        time.cancel();
                        count.setText("");
                        Intent intent = new Intent(WelecomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    };
}
