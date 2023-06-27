package com.example.njpischoolapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.njpischoolapp.R;
import com.example.njpischoolapp.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLgEd;
    private EditText pwdLgEd;
    private Button lgLgBt;
    private Button regLgBt;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        {
            emailLgEd = (EditText) findViewById(R.id.email_lg_ed);
            pwdLgEd = (EditText) findViewById(R.id.pwd_lg_ed);
            lgLgBt = (Button) findViewById(R.id.lg_lg_bt);
            regLgBt = (Button) findViewById(R.id.reg_lg_bt);
        }
        queue = Volley.newRequestQueue(this);
        //登录按钮点击监听
        {
            lgLgBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject();
                        jo.put("email", emailLgEd.getText().toString());
                        jo.put("pwd", pwdLgEd.getText().toString());
                        //步骤1：创建一个SharedPreferences对象
                        SharedPreferences sharedPreferences= getSharedPreferences("data",Context.MODE_PRIVATE);
                        //步骤2： 实例化SharedPreferences.Editor对象
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //步骤3：将获取过来的值放入文件
                        editor.putString("email",emailLgEd.getText().toString());
                        editor.putString("pwd", pwdLgEd.getText().toString());
                        //步骤4：提交
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest joR = new JsonObjectRequest(Request.Method.POST,
                            getResources().getString(R.string.url) + "login",
                            jo,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    try {
                                        //Toast.makeText(LoginActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                                        Log.e("jsonObject", "onResponse: " + jsonObject);
                                        String result = jsonObject.getString("result");
                                        String message = jsonObject.getString("message");
                                        if (!result.equals("true")) {
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        String userid = jsonObject.getString("userid");
                                        SharedPreferences sp = getSharedPreferences("default", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("userid", userid);
                                        editor.commit();
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent();
                                        intent.setClass(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this,volleyError+"",Toast.LENGTH_LONG).show();
                            Log.e("volleyError", "onErrorResponse: " + volleyError);
                        }
                    });
                    queue.add(joR);
                }
            });
        }
        //注册按钮点击监听
        {
            regLgBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.finish();
                    startActivity(intent);
                }
            });
        }
    }
    long firstTime = 0;
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        long secondTime =System.currentTimeMillis();
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            }else{
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime =System.currentTimeMillis();
            }
            return  true;
        }
        return  super.onKeyDown(keyCode,event);
    }
}
