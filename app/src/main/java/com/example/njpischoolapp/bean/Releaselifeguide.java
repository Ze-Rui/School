package com.example.njpischoolapp.bean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.njpischoolapp.R;
import com.example.njpischoolapp.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Releaselifeguide extends AppCompatActivity {

    private EditText releaseTitle;
    private TextView lifeguideDate,release_publish;
    private EditText releaseContent;
    Spinner type;

    boolean title = false,content = false;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releaselifeguide);

        releaseTitle = (EditText) findViewById(R.id.release_title);
        lifeguideDate = (TextView) findViewById(R.id.lifeguide_date);
        releaseContent = (EditText) findViewById(R.id.release_content);
        release_publish = findViewById(R.id.release_publish);
        type = findViewById(R.id.type);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        lifeguideDate.setText(simpleDateFormat.format(date));
        releaseTitle.addTextChangedListener(new releaseTitlewatcher());
        releaseContent.addTextChangedListener(new releaseContentwatcher());

        queue = Volley.newRequestQueue(Releaselifeguide.this);
    }

    class releaseTitlewatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!releaseTitle.getText().toString().trim().equals("") && !releaseContent.getText().toString().trim().equals("")){
                release_publish.setTextColor(Color.parseColor("#FF8C00"));
                release_publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlg();
                        Intent intent = new Intent(Releaselifeguide.this,MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
            }else {
                release_publish.setTextColor(Color.parseColor("#cccccc"));
                release_publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    class releaseContentwatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!releaseTitle.getText().toString().trim().equals("") && !releaseContent.getText().toString().trim().equals("")){
                release_publish.setTextColor(Color.parseColor("#FF8C00"));
                release_publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlg();
                        Intent intent = new Intent(Releaselifeguide.this,MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
            }else {
                release_publish.setTextColor(Color.parseColor("#cccccc"));
                release_publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    public void rlg(){
        JSONObject jo = new JSONObject();
        SharedPreferences sf = getSharedPreferences("lifename",Context.MODE_PRIVATE);
        String lifename = sf.getString("lifename","");
         try {
            jo.put("lifename", lifename);
            jo.put("lifedate", System.currentTimeMillis() + "");
            jo.put("lifetitle", releaseTitle.getText().toString());
            jo.put("lifecontent", releaseContent.getText().toString());
            jo.put("lifetype", type.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR = new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "releaselifeguide",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("onResponse", "onResponse: " + jsonObject);
                        Toast.makeText(Releaselifeguide.this, "发布成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "onErrorResponse: " + volleyError);
            }
        });
        queue.add(joR);
    }
}
