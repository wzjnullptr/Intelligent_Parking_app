package com.example.administrator.intelligentparkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.intelligentparkingapp.appliaction.AppVariables;
import com.example.administrator.intelligentparkingapp.constant.Constants;
import com.example.administrator.intelligentparkingapp.entity.User;
import com.example.administrator.intelligentparkingapp.okhttp.RequestUtil;
import com.example.administrator.intelligentparkingapp.sign.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CarActivity extends AppCompatActivity {

    List<String> cmark=new ArrayList<>();
    List<String> cmarkk=new ArrayList<>();
    List<String> test=new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        listView = (ListView) findViewById(R.id.listView);
        getcmarks();

        String urlx = String.format("%s/%s", Constants.context,"car/car");
        for (int i=0;i<cmark.size();i++){
            String qurl=String.format("%s/%s?num=%s",urlx,cmark.get(i),i+1);
            String test=qurl.substring(qurl.lastIndexOf("/")+1,qurl.indexOf("?"));
            cmarkk.add(test);
        }

       Log.d("车牌","cmark"+cmarkk);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                CarActivity.this, android.R.layout.simple_list_item_1, cmarkk);
        listView.setAdapter(adapter);
    }

    public void getcmarks() {
        if (AppVariables.map.get("user") != null) {
            Map map = new HashMap();
            map.put("uid", ((User) AppVariables.map.get("user")).getUid());
            RequestUtil requestUtil = new RequestUtil();
            requestUtil.doPost("car/car/", map, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data = response.body().string();
                    Map map1 = (Map) JSON.parse(data);
                    cmark = (List<String>) map1.get("cmark");
                    Log.d("车牌", "cmark " + cmark);
                    String urlx = String.format("%s/%s", Constants.context, "car/car");
                    Log.d("urlx", "urlx " + urlx);
                    for (int i = 0; i < cmark.size(); i++) {
                        int k = i + 1;
                        String qurl = String.format("%s/%s?num=%s&uid=%s", urlx, cmark.get(i), k, ((User) AppVariables.map.get("user")).getUid());
                        String test = qurl.substring(qurl.lastIndexOf("/") + 1, qurl.indexOf("?"));
                        Log.d("test", "test" + test);
                        cmarkk.add(test);
                    }
                }
            });
        }else{
            Toast.makeText(CarActivity.this,"请先登陆",Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setClass(CarActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
