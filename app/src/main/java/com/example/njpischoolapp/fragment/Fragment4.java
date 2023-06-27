package com.example.njpischoolapp.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.njpischoolapp.activity.LoginActivity;
import com.example.njpischoolapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment4 extends Fragment {

    private static final int CROP_PHOTO = 12;// 裁剪图片
    private static final int LOCAL_CROP = 13;// 本地图库

    private TextView selefName,revise_tv;
    private EditText selefPhone,revise_et;
    private EditText selefPwd;
    private EditText selefEmail,mima,mima_2;
    private Button exchange,button_revise,local_gallery,revise_mima;
    private Button refuse;
    private ImageView ivPhoto,iv,tLeftIv;
    View view,v,vv,v_back,v1;
    long firstTime = 0;

    public static AlertDialog ab;
    SharedPreferences sharedPreferences,sf,photo,sp;
    RequestQueue queue;
    String email,pwd,tel,myselfname,userid;
    GridLayout gl;
    int imageid_x,imageid_y;
    Integer[][] image = {{R.mipmap.h1,R.mipmap.h2,R.mipmap.t8},{R.mipmap.h3,R.mipmap.h4,R.mipmap.t7},{R.mipmap.h5,R.mipmap.h6,R.mipmap.t9}};
    GridLayout.LayoutParams params = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment4,null);
        v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_revise,null);
        vv = LayoutInflater.from(getContext()).inflate(R.layout.dialog_photo,null);
        v1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_revise_mima,null);
        v_back = LayoutInflater.from(getContext()).inflate(R.layout.layout_header_bar,null);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivPhoto = view.findViewById(R.id.iv_photo);
        selefName = (TextView) view.findViewById(R.id.selef_name);
        selefPhone = (EditText) view.findViewById(R.id.selef_phone);
        selefPwd = (EditText) view.findViewById(R.id.selef_pwd);
        selefEmail = (EditText) view.findViewById(R.id.selef_email);
        exchange = (Button) view.findViewById(R.id.exchange);
        refuse = (Button) view.findViewById(R.id.refuse);
        button_revise = view.findViewById(R.id.button_revise);
        revise_mima = view.findViewById(R.id.revise_mima);
        revise_tv = v.findViewById(R.id.revise_tv);


        showimgae();

        queue = Volley.newRequestQueue(getContext());
        getselfmessage();
        button_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }
        });
        revise_mima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog_2();
            }
        });

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(getContext(), LoginActivity.class);
                sharedPreferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long secondTime =System.currentTimeMillis();
                if (secondTime - firstTime < 2000) {
                    getActivity().finish();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    photo = getContext().getSharedPreferences("image",MODE_PRIVATE);
                    SharedPreferences.Editor photo_editor = photo.edit();
                    photo_editor.clear();
                    photo_editor.commit();
                }else{
                    Toast.makeText(getContext(),"再点一次,注销登录",Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                }
            }

        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_dialog();
            }
        });
    }

    //修改用户弹窗
    public void opendialog(){
        v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_revise,null);
        revise_et = v.findViewById(R.id.revise_et);
        revise_tv.setText("用户名：");
        revise_et.setHint("请修改用户名");
        AlertDialog.Builder re = new AlertDialog.Builder(getContext()).setTitle("修改用户信息").
                        setNegativeButton( "修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (revise_et.getText().toString().trim().equals("")){
                                    Toast.makeText(getContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
                                    v = null;
                                }else {
                                    revisemyname();
                                    getselfmessage();
                                    v = null;
                                }
                            }
                        }).setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        v = null;
            }
        }).setCancelable(false);
        re.setView(v);
        re.create();
        re.show();
    }

    public void opendialog_2(){
        v1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_revise_mima,null);
        mima = v1.findViewById(R.id.mima);
        mima_2 = v1.findViewById(R.id.mima_2);
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        final String mima_r = sharedPreferences.getString("pwd","");
        AlertDialog.Builder re = new AlertDialog.Builder(getContext()).setTitle("修改用户信息").
                setNegativeButton( "修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mima.getText().toString().trim().equals("")){
                            Toast.makeText(getContext(),"原密码不能为空",Toast.LENGTH_SHORT).show();
                            v1 = null;
                        }if (mima_2.getText().toString().trim().equals("")){
                            Toast.makeText(getContext(),"新密码不能为空",Toast.LENGTH_SHORT).show();
                            v1 = null;
                        }if (!mima.getText().toString().trim().equals(mima_r)){
                            Toast.makeText(getContext(),"原密码不对",Toast.LENGTH_SHORT).show();
                            v1 = null;
                        }else {
                            revisemymima();
                            getselfmessage();
                            v1 = null;
                        }
                    }
                }).setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                v1 = null;
            }
        }).setCancelable(false);
        re.setView(v1);
        re.create();
        re.show();
    }
    //得到个人信息
    public void  getselfmessage(){
        JSONObject jo = null;
        try {
            jo = new JSONObject();
            sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            email =sharedPreferences.getString("email","");
            jo.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR = new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "getUsername",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            //Toast.makeText(LoginActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                            Log.e("jsonObject", "onResponse: " + jsonObject);
                            pwd = jsonObject.getString("pwd");
                            myselfname = jsonObject.getString("name");
                            tel = jsonObject.getString("tel");

                            sf = getContext().getSharedPreferences("lifename",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf.edit();
                            editor.putString("lifename",jsonObject.getString("name"));
                            //步骤4：提交
                            editor.commit();

                            selefEmail.setText("  " + email);
                            selefPwd.setText(pwd);
                            selefName.setText("  " + myselfname);
                            selefPhone.setText("  " + tel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),volleyError+"",Toast.LENGTH_LONG).show();
                Log.e("volleyError", "onErrorResponse: " + volleyError);
            }
        });
        queue.add(joR);
    }

    //展示图片
    public void showimgae(){
        SharedPreferences su = getContext().getSharedPreferences("tu",Context.MODE_PRIVATE);
        String stu = su.getString("stu","");
        Bitmap bitmap = stringtoBitmap(stu);

        SharedPreferences s = getContext().getSharedPreferences("image_n",Context.MODE_PRIVATE);
        imageid_x = s.getInt("image_x",0);
        imageid_y = s.getInt("image_y",0);
        if (imageid_x == 0 && imageid_y == 0 && stu.trim().equals("")){
            ivPhoto.setImageResource(image[0][0]);
        }if (! stu.trim().equals("")){
            ivPhoto.setImageBitmap(bitmap);
        }else {
            ivPhoto.setImageResource(image[imageid_x][imageid_y]);
        }
    }

    //图片弹窗
    public void image_dialog(){
        vv = LayoutInflater.from(getContext()).inflate(R.layout.dialog_photo,null);
        local_gallery = vv.findViewById(R.id.local_gallery);
        local_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 启动intent打开本地图库
                startActivityForResult(intent1,LOCAL_CROP);
            }
        });
        gl = vv.findViewById(R.id.gl);
        for (int i = 0; i < 3; i++ ){
            for (int j = 0; j < 3 ;j++){
                iv = new ImageView(getContext());
                iv.setImageResource(image[i][j]);
                iv.setPadding(10, 5, 10, 5);
                final int image_x = i ;
                final int image_y = j ;

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences s = getContext().getSharedPreferences("image_n",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = s.edit();
                        editor.putInt("image_x",image_x);
                        editor.putInt("image_y",image_y);
                        editor.commit();
                        ivPhoto.setImageResource(image[image_x][image_y]);

                        SharedPreferences sg = getContext().getSharedPreferences("tu",Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed= sg.edit();
                        ed.clear();
                        ed.commit();
                        vv = null;
                        ab.dismiss();
                    }
                });
                GridLayout.Spec rowSpec = GridLayout.spec(i); // 设置它的行和列
                GridLayout.Spec columnSpec = GridLayout.spec(j);
                params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                params.width = 300;
                params.height = 300;
                params.setGravity(Gravity.CENTER);
                gl.addView(iv, params);
        }
        }

        tLeftIv = v_back.findViewById(R.id.tLeftIv);
        tLeftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab.dismiss();
            }
        });
        ab =new AlertDialog.Builder(getContext()).setCancelable(false).create();
        ab.setView(vv);
        ab.show();
    }

    //修改个人信息
    public void revisemyname(){
        JSONObject jo=new JSONObject();
        sp = getContext().getSharedPreferences("default", MODE_PRIVATE);
        userid = sp.getString("userid", "");
        String uname = revise_et.getText().toString();
        try {
            jo.put("name",uname+"");
            jo.put("userid",userid+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR=new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "revisename",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),"修改异常，请重新修改",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(joR);
    }

    public void revisemymima(){
        JSONObject jo=new JSONObject();
        sp = getContext().getSharedPreferences("default", MODE_PRIVATE);
        userid = sp.getString("userid", "");
        String upwd = mima.getText().toString();
        try {
            jo.put("mima",upwd);
            jo.put("userid",userid+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR=new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "revisemima",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),"修改异常，请重新修改",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(joR);
    }
//    图库返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case LOCAL_CROP:// 系统图库
                if(resultCode == RESULT_OK){
                    // 创建intent用于裁剪图片
                    Intent intent1 = new Intent("com.android.camera.action.CROP");
                    // 获取图库所选图片的uri
                    Uri uri = data.getData();
                    intent1.setDataAndType(uri,"image/*");
                    //  设置裁剪图片的宽高
                    intent1.putExtra("outputX", 300);
                    intent1.putExtra("outputY", 300);
                    // 裁剪后返回数据
                    intent1.putExtra("return-data", true);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent1, CROP_PHOTO);
                }

                break;
            case CROP_PHOTO:// 裁剪后展示图片
                if(resultCode == RESULT_OK){
                    try{// 展示图库中选择裁剪后的图片
                        if(data != null){
                            // 根据返回的data，获取Bitmap对象
                            Bitmap bitmap = data.getExtras().getParcelable("data");
                            String stu = bitmaptoString(bitmap);
                            SharedPreferences st= getContext().getSharedPreferences("tu",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("stu",stu);
                            editor.commit();

                            // 展示图片
                            ivPhoto.setImageBitmap(bitmap);
                            SharedPreferences s = getContext().getSharedPreferences("image_n",Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed= s.edit();
                            ed.clear();
                            ed.commit();

                            vv = null;
                            ab.dismiss();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public Bitmap stringtoBitmap(String st)
    {
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String bitmaptoString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string=Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }
}
