package com.example.njpischoolapp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 12044 on 2019/12/11.
 */

public class sendCode {

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    String post(String url,String json) throws IOException{
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try(Response response=client.newCall(request).execute()){
            return response.body().string();
        }
    }

    String bowlingJson(String checkCode,String phoneNo){
        return "{ 'sid':'432c1bae10e825d43460be59e7c5a8fb',"+
                "'token':'2cecdc548211f834df272f009a258b35',"+
                "'appid':'5ced85e96aec4e6babc445e3cfc4cf41',"+
                "'templateid':'522444',"+
                "'param':'"+checkCode+",3',"+
                "'mobile':'"+phoneNo+"',"+
                "'uid':''}";
    }
}
