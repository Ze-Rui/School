package com.example.njpischoolapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.njpischoolapp.bean.LifeguideObject;
import com.example.njpischoolapp.R;
import com.example.njpischoolapp.bean.Releaselifeguide;
import com.example.njpischoolapp.activity.ShowlifeguideActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment1 extends Fragment {

    View view;
    ListView listView;
    RequestQueue queue;
    int start = 0;
    int end = 5;
    ArrayList<LifeguideObject> arrayList;
    private Button button_plus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = view.findViewById(R.id.listView);
        button_plus = view.findViewById(R.id.button_plus);

        queue = Volley.newRequestQueue(getContext());
        button_plus.setOnClickListener(new OnClickListener());
        getLifeguide();
    }

    public void getLifeguide() {
        JSONObject jo = null;
        try {
            jo = new JSONObject();
            jo.put("start", start);
            jo.put("end", end);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jaR = new JsonArrayRequest(Request.Method.POST,
                getResources().getString(R.string.url) + "getLifeguide",
                jo,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            arrayList = new ArrayList<>();
                            //JSONArray result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo2 = jsonArray.getJSONObject(i);
                                LifeguideObject lgo = new LifeguideObject();
                                lgo.setId(jo2.getString("id"));
                                lgo.setContent(jo2.getString("content"));
                                lgo.setKind(jo2.getString("kind"));
                                lgo.setPubman(jo2.getString("pubman"));
                                lgo.setPubtime(jo2.getString("pubtime"));
                                lgo.setTitle(jo2.getString("title"));
                                lgo.setType(jo2.getString("kind"));
                                arrayList.add(lgo);
                            }
                            listView.setAdapter(new MyAdapter());
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
            convertView = View.inflate(getContext(), R.layout.item_fragment1, null);
            TextView titleItemFragment1;
            TextView pubmanItemFragment1;
            TextView pubtimeItemFragment1,t_type;

            titleItemFragment1 = (TextView) convertView.findViewById(R.id.title_item_fragment1);
            pubmanItemFragment1 = (TextView) convertView.findViewById(R.id.pubman_item_fragment1);
            pubtimeItemFragment1 = (TextView) convertView.findViewById(R.id.pubtime_item_fragment1);
            t_type = convertView.findViewById(R.id.t_type);
            titleItemFragment1.setText(arrayList.get(position).getTitle());
            pubmanItemFragment1.setText(arrayList.get(position).getPubman());
            t_type.setText(arrayList.get(position).getType());
            Date date = new Date(Long.parseLong(arrayList.get(position).getPubtime()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时");
            final String sd = sdf.format(date);
            pubtimeItemFragment1.setText(sd);

            final LifeguideObject object = arrayList.get(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("title", object.getTitle());
                    intent.putExtra("pubman", object.getPubman());
                    intent.putExtra("pubtime", sd);
                    intent.putExtra("content", object.getContent());
                    intent.putExtra("kind", object.getType());
                    intent.setClass(getContext(), ShowlifeguideActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    class OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(),Releaselifeguide.class);
            startActivity(intent);
        }
    }
}
