package com.example.njpischoolapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.njpischoolapp.R;
import com.example.njpischoolapp.bean.SecondhandGoodsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment2 extends Fragment {

    View view;
    ListView listView;
    private EditText edSearch;
    private Button btSearch;
    private Spinner spinnerSearch;
    private ImageView btLastpage;
    private ImageView btNextpage;
    private TextView txPagenum;

    RequestQueue
            queue;
    int start = 0;
    int end = 5;
    ArrayList<SecondhandGoodsObject> arrayList;
    final int onePageNum=5;
    int userid=-1;
    int totalcount=0;
    int pagecount=0;
    int nowpagecount=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = view.findViewById(R.id.listView);
        edSearch = view.findViewById(R.id.ed_search);
        btSearch = view.findViewById(R.id.bt_search);
        spinnerSearch = view.findViewById(R.id.spinner_search);
        btLastpage = view.findViewById(R.id.bt_lastpage);
        btNextpage = view.findViewById(R.id.bt_nextpage);
        txPagenum = view.findViewById(R.id.tx_pagenum);


        queue = Volley.newRequestQueue(getContext());
        SharedPreferences sp = getContext().getSharedPreferences("default", Context.MODE_PRIVATE);
        userid = Integer.parseInt(sp.getString("userid", "-1"));
        getSecondhandGoodsByPage();
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowpagecount=1;
                refreshtxPagenum();
                getSecondhandGoodsByPageInLikeMODE();
            }
        });
        btNextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowpagecount>=pagecount){
                    return;
                }
                nowpagecount++;
                refreshtxPagenum();
                getSecondhandGoodsByPageInLikeMODE();
            }
        });
        btLastpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowpagecount<=1){
                    return;
                }
                nowpagecount--;
                refreshtxPagenum();
                getSecondhandGoodsByPageInLikeMODE();
            }
        });
    }

    public void getSecondhandGoodsByPage() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("start", start);
            jo.put("end", end);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jaR = new JsonArrayRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "getSecondhandGoodsByPage",
                jo, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    JSONObject totalcountObj=jsonArray.getJSONObject(0);
                    totalcount=totalcountObj.getInt("COUNT");
                    arrayList = new ArrayList<>();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jo2 = jsonArray.getJSONObject(i);
                        SecondhandGoodsObject sgo = new SecondhandGoodsObject();
                        sgo.setGname(jo2.getString("gname"));
                        sgo.setGoodsid(jo2.getString("goodsid"));
                        sgo.setPrice(jo2.getString("price"));
                        sgo.setPubuserid(jo2.getString("pubuserid"));
                        sgo.setPubtime(jo2.getString("pubtime"));
                        sgo.setPubname(jo2.getString("name"));
                        sgo.setState(jo2.getString("state"));
                        arrayList.add(sgo);
                    }
                    listView.setAdapter(new MyAdapter());
                    refreshtxPagenum();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "onErrorResponse: " + volleyError);
            }
        });
        queue.add(jaR);
    }

    public void getSecondhandGoodsByPageInLikeMODE(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("gname",edSearch.getText().toString()+"");
            jo.put("start", start);
            jo.put("end", end);
            jo.put("kind",spinnerSearch.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jaR = new JsonArrayRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "getSecondhandGoodsByPageInLikeMODE",
                jo, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    JSONObject totalcountObj=jsonArray.getJSONObject(0);
                    totalcount=totalcountObj.getInt("COUNT");
                    arrayList = new ArrayList<>();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jo2 = jsonArray.getJSONObject(i);
                        SecondhandGoodsObject sgo = new SecondhandGoodsObject();
                        sgo.setGname(jo2.getString("gname"));
                        sgo.setGoodsid(jo2.getString("goodsid"));
                        sgo.setPrice(jo2.getString("price"));
                        sgo.setPubuserid(jo2.getString("pubuserid"));
                        sgo.setPubtime(jo2.getString("pubtime"));
                        sgo.setPubname(jo2.getString("name"));
                        sgo.setState(jo2.getString("state"));
                        arrayList.add(sgo);
                    }
                    listView.setAdapter(new MyAdapter());
                    refreshtxPagenum();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "onErrorResponse: " + volleyError);
            }
        });
        queue.add(jaR);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            }
            convertView = View.inflate(getContext(), R.layout.item_fragment2, null);
            TextView gnameItemFragment2;
            TextView priceItemFragment2;
            TextView pubmanItemFragment2;
            TextView pubtimeItemFragment2;
            Button buttonItemFragment2;

            gnameItemFragment2 = (TextView) convertView.findViewById(R.id.gname_item_fragment2);
            priceItemFragment2 = (TextView) convertView.findViewById(R.id.price_item_fragment2);
            pubmanItemFragment2 = (TextView) convertView.findViewById(R.id.pubman_item_fragment2);
            pubtimeItemFragment2 = (TextView) convertView.findViewById(R.id.pubtime_item_fragment2);
            buttonItemFragment2 = (Button) convertView.findViewById(R.id.button_item_fragment2);

            gnameItemFragment2.setText(arrayList.get(position).getGname());
            priceItemFragment2.setText("价格:" + arrayList.get(position).getPrice());
            pubmanItemFragment2.setText("发布人:" + arrayList.get(position).getPubname());

            Date date = new Date(Long.parseLong(arrayList.get(position).getPubtime()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时");
            String sd = sdf.format(date);
            pubtimeItemFragment2.setText(sd);
            buttonItemFragment2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList.get(position).getPubuserid().equals(userid + "")) {
                        Toast.makeText(getContext(), "不能对自己的二手物品开启交易", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setPositiveButton("确认交易", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSecondhandGoods(arrayList.get(position).getGoodsid() + "");
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });

            return convertView;
        }
    }

    public void startSecondhandGoods(String goodsid){
        JSONObject jo = new JSONObject();
        try {
            jo.put("buyuserid", userid + "");
            jo.put("goodsid", goodsid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR = new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "startSecondhandGoods",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("result").equals("true")) {
                                Toast.makeText(getContext(), "开启交易成功", Toast.LENGTH_SHORT).show();
                                getSecondhandGoodsByPage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "onErrorResponse: " + volleyError);
            }
        });
        queue.add(joR);
    }

    //刷新页数显示
    public void refreshtxPagenum(){
        if(totalcount%onePageNum==0){
            pagecount=totalcount/onePageNum;
        }else {
            pagecount=(totalcount/onePageNum)+1;
        }
        txPagenum.setText(nowpagecount+"/"+pagecount);
        start=nowpagecount*onePageNum-onePageNum;
        end=onePageNum;
    }
}
