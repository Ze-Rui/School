package com.example.njpischoolapp;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.njpischoolapp.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameRegEd;
    private EditText pwdRegEd,pwdRegEd2;
    private EditText telRegEd;
    private EditText emailRegEd;
    private Button regRegBt;
    private RadioGroup rg;
    private RadioButton boy,girl;
    private ImageView iv_sex;
    RequestQueue queue;

    char checkCode[] = {
            'a','b','c','d','e','f',
            'g','h','i','j','k','l',
            'm','n','o','p','q','r',
            's','t','u','v','w','x',
            'y','z','0','1','2','3',
            '4','5','6','7','8','9'
    };
    //随机产生验证码
    String strCheckCode  = "";

    boolean isSend = false;
    boolean isMatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        {
            nameRegEd = (EditText) findViewById(R.id.name_reg_ed);
            pwdRegEd = (EditText) findViewById(R.id.pwd_reg_ed);
            pwdRegEd2 = (EditText) findViewById(R.id.pwd_reg_ed2);
            telRegEd = (EditText) findViewById(R.id.tel_reg_ed);
            emailRegEd = (EditText) findViewById(R.id.email_reg_ed);
            regRegBt = (Button) findViewById(R.id.reg_reg_bt);
            rg = findViewById(R.id.rg);
            boy = findViewById(R.id.boy);
            iv_sex = findViewById(R.id.iv_sex);
        }

        boy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    iv_sex.setBackgroundResource(R.drawable.register_b);
                }else {
                    iv_sex.setBackgroundResource(R.drawable.register_g);
                }
            }
        });

        queue= Volley.newRequestQueue(this);
        {
            regRegBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(isSend==true){
//                        if(verification_code.getText().toString().trim().equals(strCheckCode)){
                            if (!nameRegEd.getText().toString().trim().equals("") && pwdRegEd.length() >= 6
                                    && pwdRegEd.getText().toString().equals(pwdRegEd2.getText().toString())
                                    && telRegEd.length() == 11 && !emailRegEd.getText().toString().trim().equals("")
                                    ){
                                JSONObject jo= null;
                                try {
                                    jo = new JSONObject();
                                    jo.put("name",nameRegEd.getText().toString());
                                    jo.put("pwd",pwdRegEd.getText().toString());
                                    jo.put("tel",telRegEd.getText().toString());
                                    jo.put("email",emailRegEd.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest joR=new JsonObjectRequest(Request.Method.POST,
                                        getResources().getString(R.string.url) + "register",
                                        jo,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                try {
                                                    //Toast.makeText(LoginActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                                                    String result=jsonObject.getString("result");
                                                    String message=jsonObject.getString("message");
                                                    if(result.equals("true")){
                                                        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                                                        finish();
                                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                    }else {
                                                        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Log.e("volleyError", "onErrorResponse: "+volleyError );
                                    }
                                });
                                queue.add(joR);
                            } else if (nameRegEd.getText().toString().trim().equals("")){
                                Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                            }else if (pwdRegEd.length() < 6 ){
                                Toast.makeText(RegisterActivity.this,"密码少于六位数",Toast.LENGTH_SHORT).show();
                            }else if (telRegEd.length() != 11){
                                Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                            }else if (pwdRegEd.getText().toString().equals(pwdRegEd2.getText().toString())){
                                Toast.makeText(RegisterActivity.this,"两次密码不同",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                            }
//                        }else{
//                            Toast.makeText(RegisterActivity.this,"验证码错误！",Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        Toast.makeText(RegisterActivity.this,"请先发送验证码！",Toast.LENGTH_SHORT).show();
//                    }
                    }
            });
        }

    }

    long firstTime = 0;
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        long secondTime =System.currentTimeMillis();
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.finish();
                startActivity(intent);
            }else{
                Toast.makeText(this, "未注册，再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime =System.currentTimeMillis();
            }
            return  true;
        }
        return  super.onKeyDown(keyCode,event);
    }

    public void verificationcode(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //4位或6位验证码
                for(int i=0;i<4;++i){
                    strCheckCode += checkCode[(int) (checkCode.length*Math.random())];
                }
                sendCode ss = new sendCode();
                String json = ss.bowlingJson(strCheckCode,telRegEd.getText().toString().trim());
                try{
                    String response = ss.post("https://open.ucpaas.com/ol/sms/sendsms",json);
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"发送成功！"+response,Toast.LENGTH_SHORT).show();
                    isSend = true;
                    Looper.loop();
                }catch(Exception e){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"发送失败！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16([5,6])|(17[0-8])|(18[0-9]))|(19[1,8,9]))\\d{8}$";
        if (phone.length() != 11) {
            Toast.makeText(this,"手机号应为11位数", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            isMatch = m.matches();
            return isMatch;
        }
    }
    @Override
    public void onClick(View v){

    }
}
