package com.example.administrator.intelligentparkingapp.sign;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.intelligentparkingapp.R;
import com.example.administrator.intelligentparkingapp.constant.Constants;
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

public class RegistActivity extends AppCompatActivity {
    private TextView textView;
    private EditText uname;
    private TextView textView2;
    private TextView textView3;
    private TextView textView5;
    private EditText upwd;
    private EditText confirm;
    private EditText editText6;
    private Button button3;
    private Button button4;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        textView = (TextView) findViewById(R.id.textView);
        uname = (EditText) findViewById(R.id.editText3);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        upwd = (EditText) findViewById(R.id.editText4);
        confirm = (EditText) findViewById(R.id.editText5);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAsyn(uname.getText().toString(), upwd.getText().toString());
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               System.exit(0);
            }
        });
    }

    //异步POST请求
    public void postAsyn(final String name, final String pwd) {
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
                        Toast.makeText(RegistActivity.this, "网路连接错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String str = response.body().string();
                        Map<String, Object> map = JSONObject.parseObject(str, Map.class);
                        int flag = (Integer) map.get("status");
                        if (flag == 1) {
                            Intent intent = new Intent();
                            intent.setClass(RegistActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else if (flag == 0) {
                            Looper.prepare();
                            Toast.makeText(RegistActivity.this, "该用户名已存在", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else if (flag == 2) {
                            Looper.prepare();
                            Toast.makeText(RegistActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(RegistActivity.this, "未知错误，请联系管理员", Toast.LENGTH_LONG).show();
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
}


