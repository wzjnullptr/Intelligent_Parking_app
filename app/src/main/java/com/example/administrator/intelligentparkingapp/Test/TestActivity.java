package com.example.administrator.intelligentparkingapp.Test;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.intelligentparkingapp.MainActivity;
import com.example.administrator.intelligentparkingapp.R;
import com.example.administrator.intelligentparkingapp.constant.Constants;
import com.example.administrator.intelligentparkingapp.utils.Autjcode;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {
    private TextView registerBackText;
    private TextView registerTitle;
    private ImageView registerUser;
    private EditText registerId;
    private TextView registerIdText;
    private EditText registerPassword;
    private TextView registerPwText;
    private EditText turePassword;
    private TextView turePwText;
    private EditText registerAuth;
    private TextView registerAuthText;
    private ImageView registerAuthimg;
    private Button registerCheck;
    private Button registerBtn;
    private String  isPassword, isTruePassword, Autecode, Autecodeimg;
    private int  flagPassword, flagTruePassword, flagAutecode;

    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        registerBackText = (TextView) findViewById(R.id.registerBackText);
        registerTitle = (TextView) findViewById(R.id.registerTitle);
        registerUser = (ImageView) findViewById(R.id.registerUser);
        registerId = (EditText) findViewById(R.id.registerId);
        registerIdText = (TextView) findViewById(R.id.registerIdText);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerPwText = (TextView) findViewById(R.id.registerPwText);
        turePassword = (EditText) findViewById(R.id.turePassword);

        turePwText = (TextView) findViewById(R.id.turePwText);
        registerAuth = (EditText) findViewById(R.id.registerAuth);
        registerAuthText = (TextView) findViewById(R.id.registerAuthText);

        registerAuthimg = (ImageView) findViewById(R.id.registerAuthimg);
        //registerAuthimg.setOnClickListener((View.OnClickListener) this);

        registerCheck = (Button) findViewById(R.id.registerCheck);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        //设置验证码图片
        registerAuthimg.setImageBitmap(Autjcode.getInstance().createBitmap());


        Autecodeimg = Autjcode.getInstance().getCode().toUpperCase();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPassword = registerPassword.getText().toString();
                isTruePassword = turePassword.getText().toString();
                Autecode = registerAuth.getText().toString();
                if(!isPassword.equals(isTruePassword)){
                    Toast.makeText(TestActivity.this,"两次输入的密码必须相同",Toast.LENGTH_LONG).show();
                }else if(!Autecode.toUpperCase().equals(Autecodeimg)) {
                    Toast.makeText(TestActivity.this,"验证码错误",Toast.LENGTH_LONG).show();
                }else{
                    postAsyn(registerId.getText().toString(), registerPassword.getText().toString());
                }
            }
        });

       registerBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestActivity.this.finish();
            }
        });

        registerCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAuthimg.setImageBitmap(Autjcode.getInstance()
                        .createBitmap());
            }
        });

    }


    //异步POST请求
    public  void postAsyn(final String name, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("name", name)
                        .add("pwd", pwd)
                        .build();
                String url = String.format("%s/%s", Constants.context, "user/regist/do");
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(TestActivity.this, "网路连接错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String str = response.body().string();
                        Map<String, Object> map = JSONObject.parseObject(str, Map.class);
                        int flag = (Integer) map.get("status");
                        if (flag == 1) {
                            Intent intent = new Intent();
                            intent.setClass(TestActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else if (flag == 0) {
                            Looper.prepare();
                            Toast.makeText(TestActivity.this, "该用户名已存在", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else if (flag == 2) {
                            Looper.prepare();
                            Toast.makeText(TestActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(TestActivity.this, "未知错误，请联系管理员", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
                    }
                });
            }
        }).start();
    }

   /* @Override
    public void onFocusChange(View v, boolean hasFocus) {
        isPassword = registerPassword.getText().toString();
        isTruePassword = turePassword.getText().toString();
        Autecode = registerAuth.getText().toString();
        Autecodeimg = AutjCode.getInstance().getCode().toUpperCase();
        switch (v.getId()) {
            case R.id.registerPassword:
                if (hasFocus == false) {
                    if (isPassword.length() != 0) {
                        registerPwText.setVisibility(View.VISIBLE);
                    } else {
                        registerPwText.setVisibility(View.INVISIBLE);
                        flagPassword = 1;
                    }
                }
                break;
            case R.id.turePassword:
                if (hasFocus == false) {
                    if (isTruePassword.equals(isPassword)) {
                        turePwText.setVisibility(View.INVISIBLE);
                        flagTruePassword = 1;
                    } else {
                        if (turePassword.length() != 0) {
                            turePwText.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case R.id.registerAuth:
                if (hasFocus == false) {
                    // 判断验证码是否正确，toUpperCase()是不区分大小写
                    if (Autecode.toUpperCase().equals(Autecodeimg)) {
                        registerAuthText.setVisibility(View.INVISIBLE);
                        flagAutecode = 1;
                    } else {
                        if (registerAuth.length() != 0) {
                            registerAuthText.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }*/
}
