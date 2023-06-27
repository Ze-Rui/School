package com.example.njpischoolapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.njpischoolapp.util.TabsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Fragment3 extends Fragment {

    private EditText gnameEd;
    private EditText priceEd;
    private Button publishBt;
    private Spinner spinnerKind;
    SharedPreferences sp;
    RequestQueue queue;
    long firstTime = 0;
    String userid;
    ArrayList<SecondhandGoodsObject> arrayList,arrayList_2;
    ListView listView,listView_2;
    View view;
    ViewPager fragment3_vp;
    ArrayList<View> vpList;
    ArrayList<String> titleList ;
    private TabsView mTabs;
    int i;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment3,null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragment3_vp = view.findViewById(R.id.fragment3_vp);
        mTabs = view.findViewById(R.id.tabslayout);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v1 = inflater.inflate(R.layout.activity_publish_secondhand_goods,null);
        View v2 = inflater.inflate(R.layout.activity_trade,null);
        View v3 = inflater.inflate(R.layout.activity_trade_2,null);

        gnameEd = (EditText) v1.findViewById(R.id.gname_ed);
        priceEd = (EditText) v1.findViewById(R.id.price_ed);
        publishBt = (Button) v1.findViewById(R.id.publish_bt);
        spinnerKind = v1.findViewById(R.id.spinner_kind);
        listView =  v2.findViewById(R.id.listView);
        listView_2 = v3.findViewById(R.id.listView_2);

        sp = getContext().getSharedPreferences("default", MODE_PRIVATE);
        userid = sp.getString("userid", "");
        queue = Volley.newRequestQueue(getContext());

        {
            psg();
        }

        vpList = new ArrayList<View>();
        vpList.add(v1);
        vpList.add(v2);
        vpList.add(v3);

        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return vpList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view==o;
            }

            //获取当前显示界面的位置
            public Object instantiateItem(ViewGroup vg,int postion){
                vg.addView(vpList.get(postion));
                return vpList.get(postion);
            }

            //销毁上一次显示的界面
            public void destroyItem(ViewGroup vg,int postion,Object object){
                vg.removeView(vpList.get(postion));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        };

        fragment3_vp.setAdapter(adapter);
        mTabs.setTabs("发布二手物品","我的二手物品","购物车");
        mTabs.setOnTabsItemClickListener(new TabsView.OnTabsItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                fragment3_vp.setCurrentItem(position, true);
            }
        });
        fragment3_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTabs.setCurrentTab(0,false);
                        break;
                    case 1:
                        mTabs.setCurrentTab(1,false);
                         {
                             trade();
                         }
                        break;
                    case 2:
                        mTabs.setCurrentTab(2,false);
                        {
                            trade_2();
                        }
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void psg(){
        publishBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gnameEd.getText().toString().trim().equals("")
                        &&!priceEd.getText().toString().trim().equals("")){
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("pubuserid", sp.getString("userid", ""));
                        jo.put("pubtime", System.currentTimeMillis() + "");
                        jo.put("gname", gnameEd.getText().toString());
                        jo.put("price", priceEd.getText().toString());
                        jo.put("kind", spinnerKind.getSelectedItem().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest joR = new JsonObjectRequest(Request.Method.POST,
                            getResources().getString(R.string.url) + "publishSecondhandGoods",
                            jo,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.e("onResponse", "onResponse: " + jsonObject);
                                    Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                    gnameEd.setText("");
                                    priceEd.setText("");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("volleyError", "onErrorResponse: " + volleyError);
                        }
                    });
                    queue.add(joR);
                }else if (gnameEd.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "物品名不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "价格不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void trade() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("pubuserid", userid + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jaR = new JsonArrayRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "getMySecondhandGoods",
                jo,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.e("jsonArray", "onResponse: "+jsonArray );
                        arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject myjo = jsonArray.getJSONObject(i);
                                SecondhandGoodsObject sgo = new SecondhandGoodsObject();
                                sgo.setGname(myjo.getString("gname"));
                                sgo.setPubuserid(myjo.getString("pubuserid"));
                                sgo.setBuyuserid(myjo.getString("buyuserid"));
                                sgo.setPubname(myjo.getString("pubtime"));
                                sgo.setState(myjo.getString("state"));
                                sgo.setPubtime(myjo.getString("pubtime"));
                                sgo.setPrice(myjo.getString("price"));
                                sgo.setGoodsid(myjo.getString("goodsid"));
                                arrayList.add(sgo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listView.setAdapter(new MyAdapter());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "onErrorResponse: "+volleyError );
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
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_trade, null);
            }
            TextView gnameItemTrade;
            TextView priceItemTrade;
            TextView pubmanItemTrade;
            TextView pubtimeItemTrade,jiaoyi;
            Button button1ItemTrade;
            Button button2ItemTrade;

            gnameItemTrade = (TextView) convertView.findViewById(R.id.gname_item_trade);
            priceItemTrade = (TextView) convertView.findViewById(R.id.price_item_trade);
            pubmanItemTrade = (TextView) convertView.findViewById(R.id.pubman_item_trade);
            pubtimeItemTrade = (TextView) convertView.findViewById(R.id.pubtime_item_trade);
            button1ItemTrade = (Button) convertView.findViewById(R.id.button1_item_trade);
            button2ItemTrade = (Button) convertView.findViewById(R.id.button2_item_trade);
            jiaoyi = convertView.findViewById(R.id.jiaoyi);

            gnameItemTrade.setText("物品名称: "+arrayList.get(position).getGname()+"");
            priceItemTrade.setText("价格(元): "+arrayList.get(position).getPrice()+"");
            if(!arrayList.get(position).getState().equals("0")){
                pubmanItemTrade.setText("购买人id:"+arrayList.get(position).getBuyuserid()+"");
            }else {
                pubmanItemTrade.setText("发布人id:"+arrayList.get(position).getPubuserid()+"");
            }

            {
                Date date = new Date(Long.parseLong(arrayList.get(position).getPubtime()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时");
                final String sd = sdf.format(date);
                pubtimeItemTrade.setText("发布时间:"+sd);
            }
            if(arrayList.get(position).getState().equals("0")){
                button1ItemTrade.setVisibility(View.GONE);
                button2ItemTrade.setVisibility(View.GONE);
                jiaoyi.setText("等待交易");
            }
            if(arrayList.get(position).getState().equals("1")){
                button1ItemTrade.setVisibility(View.GONE);
                button2ItemTrade.setVisibility(View.GONE);
                jiaoyi.setText("交易完成");
            }
            if(arrayList.get(position).getState().equals("2")){
                button1ItemTrade.setVisibility(View.VISIBLE);
                button2ItemTrade.setVisibility(View.VISIBLE);
                jiaoyi.setText("正在交易");
                button1ItemTrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long secondTime =System.currentTimeMillis();
                        if (secondTime - firstTime < 2000) {
                            //终止交易
                            abortSecondhandGoods(Integer.parseInt(arrayList.get(position).getGoodsid()));
                        }else{
                            Toast.makeText(getContext(),"再点一次,终止交易",Toast.LENGTH_SHORT).show();
                            firstTime = System.currentTimeMillis();
                        }
                    }
                });
                button2ItemTrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long secondTime =System.currentTimeMillis();
                        if (secondTime - firstTime < 2000) {
                            //完成交易
                            completeSecondhandGoods(Integer.parseInt(arrayList.get(position).getGoodsid()));
                        }else{
                            Toast.makeText(getContext(),"再点一次,完成交易",Toast.LENGTH_SHORT).show();
                            firstTime = System.currentTimeMillis();
                        }
                    }
                });
            }
            return convertView;
        }
    }

    public void abortSecondhandGoods(int goodsid){
        JSONObject jo=new JSONObject();
        try {
            jo.put("goodsid",goodsid+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR=new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "abortSecondhandGoods",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        trade();
                        trade_2();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("abortSecondhandGoods", "onErrorResponse: "+volleyError );
            }
        });
        queue.add(joR);
    }

    public void completeSecondhandGoods(int goodsid){
        JSONObject jo=new JSONObject();
        try {
            jo.put("goodsid",goodsid+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest joR=new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "completeSecondhandGoods",
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        trade();
                        trade_2();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("completeSecondhandGoods", "onErrorResponse: "+volleyError );
            }
        });
        queue.add(joR);
    }

    public void trade_2() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("buyuserid", userid + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jaR = new JsonArrayRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "getMySecondhandGoodsOnBus",
                jo,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.e("jsonArray", "onResponse: "+jsonArray );
                        arrayList_2 = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject myjo = jsonArray.getJSONObject(i);
                                SecondhandGoodsObject sgo = new SecondhandGoodsObject();
                                sgo.setGname(myjo.getString("gname"));
                                sgo.setPubuserid(myjo.getString("pubuserid"));
                                sgo.setBuyuserid(myjo.getString("buyuserid"));
                                sgo.setPubname(myjo.getString("pubtime"));
                                sgo.setState(myjo.getString("state"));
                                sgo.setPubtime(myjo.getString("pubtime"));
                                sgo.setPrice(myjo.getString("price"));
                                sgo.setGoodsid(myjo.getString("goodsid"));
                                arrayList_2.add(sgo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listView_2.setAdapter(new MyAdapter_2());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError", "onErrorResponse: "+volleyError );
            }
        });
        queue.add(jaR);
    }

    class MyAdapter_2 extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList_2.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_trade_2, null);
            }
            TextView gnameItemTrade;
            TextView priceItemTrade;
            TextView pubmanItemTrade;
            TextView pubtimeItemTrade,jiaoyi;
            Button button1ItemTrade;
            Button button2ItemTrade;

            gnameItemTrade = (TextView) convertView.findViewById(R.id.gname_item_trade);
            priceItemTrade = (TextView) convertView.findViewById(R.id.price_item_trade);
            pubmanItemTrade = (TextView) convertView.findViewById(R.id.pubman_item_trade);
            pubtimeItemTrade = (TextView) convertView.findViewById(R.id.pubtime_item_trade);
            button1ItemTrade = (Button) convertView.findViewById(R.id.button1_item_trade);
            button2ItemTrade = (Button) convertView.findViewById(R.id.button2_item_trade);
            jiaoyi = convertView.findViewById(R.id.jiaoyi);

            gnameItemTrade.setText("物品名称: "+arrayList_2.get(position).getGname()+"");
            priceItemTrade.setText("价格(元): "+arrayList_2.get(position).getPrice()+"");
            if (arrayList_2.get(position).getState().equals("0")){
                pubmanItemTrade.setText("发布人id:"+arrayList_2.get(position).getPubuserid()+"");
            }else {
                if (!arrayList_2.get(position).getPubuserid().equals(userid)){
                    pubmanItemTrade.setText("发布人id:"+arrayList_2.get(position).getPubuserid()+"");
                }else {
                    pubmanItemTrade.setText("购买人id:"+arrayList_2.get(position).getBuyuserid()+"");
                }
            }

            {
                Date date = new Date(Long.parseLong(arrayList_2.get(position).getPubtime()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时");
                final String sd = sdf.format(date);
                pubtimeItemTrade.setText("发布时间:"+sd);
            }
            if(arrayList_2.get(position).getState().equals("0")){
                button1ItemTrade.setVisibility(View.GONE);
                button2ItemTrade.setVisibility(View.GONE);
                jiaoyi.setText("等待交易");
            }
            if(arrayList_2.get(position).getState().equals("1")){
                button1ItemTrade.setVisibility(View.GONE);
                button2ItemTrade.setVisibility(View.GONE);
                jiaoyi.setText("交易完成");
            }
            if(arrayList_2.get(position).getState().equals("2")){
                if (!arrayList_2.get(position).getPubuserid().equals(userid)){
                    button1ItemTrade.setVisibility(View.VISIBLE);
                    button2ItemTrade.setVisibility(View.GONE);
                    jiaoyi.setText("正在交易");
                    button1ItemTrade.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //终止交易
                            abortSecondhandGoods(Integer.parseInt(arrayList.get(position).getGoodsid()));
                        }
                    });
                }else {
                    button1ItemTrade.setVisibility(View.VISIBLE);
                    button2ItemTrade.setVisibility(View.VISIBLE);
                    jiaoyi.setText("正在交易");
                    button1ItemTrade.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long secondTime =System.currentTimeMillis();
                            if (secondTime - firstTime < 2000) {
                                //终止交易
                                abortSecondhandGoods(Integer.parseInt(arrayList_2.get(position).getGoodsid()));
                            }else{
                                Toast.makeText(getContext(),"再点一次,终止交易",Toast.LENGTH_SHORT).show();
                                firstTime = System.currentTimeMillis();
                            }
                        }
                    });
                    button2ItemTrade.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long secondTime =System.currentTimeMillis();
                            if (secondTime - firstTime < 2000) {
                                //完成交易
                                completeSecondhandGoods(Integer.parseInt(arrayList_2.get(position).getGoodsid()));
                            }else{
                                Toast.makeText(getContext(),"再点一次,完成交易",Toast.LENGTH_SHORT).show();
                                firstTime = System.currentTimeMillis();
                            }
                        }
                    });
                }
            }
            return convertView;
        }
    }
}
