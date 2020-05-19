package com.example.administrator.intelligentparkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.intelligentparkingapp.appliaction.AppVariables;
import com.example.administrator.intelligentparkingapp.constant.Constants;
import com.example.administrator.intelligentparkingapp.entity.User;
import com.example.administrator.intelligentparkingapp.okhttp.RequestUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MycenterActivity extends AppCompatActivity {

    private TextView textView6;
    private TextView textView7;
    private Button button5;
    private Button button6;
    private Integer uid;
    private TextView textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);

        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        textView8=(TextView) findViewById(R.id.textView8);

        if (AppVariables.map.get("user") != null) {
            textView6.setText(((User) AppVariables.map.get("user")).getUname());
            textView7.setText(((User) AppVariables.map.get("user")).getUmoney().toString());
        } else {
            textView6.setText("亲，你还没登录哦");
            textView7.setText("请先登陆");
        }

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MycenterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        textView8.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MycenterActivity.this, CarActivity.class);
                startActivity(intent);
            }
        });
    }

}
