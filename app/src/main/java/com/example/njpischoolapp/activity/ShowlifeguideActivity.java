package com.example.njpischoolapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.njpischoolapp.R;

public class ShowlifeguideActivity extends AppCompatActivity {

    private TextView titleTv;
    private TextView pubmanTv;
    private TextView pubtimeTv;
    private TextView contentTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlifeguide);

        titleTv = (TextView) findViewById(R.id.title_tv);
        pubmanTv = (TextView) findViewById(R.id.pubman_tv);
        pubtimeTv = (TextView) findViewById(R.id.pubtime_tv);
        contentTv = (TextView) findViewById(R.id.content_tv);
        contentTv.setMovementMethod(new ScrollingMovementMethod());

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String pubman=intent.getStringExtra("pubman");
        String pubtime=intent.getStringExtra("pubtime");
        String content=intent.getStringExtra("content");

        titleTv.setText(title);
        pubmanTv.setText(pubman);
        pubtimeTv.setText(pubtime);
        contentTv.setText(content);
    }
}
