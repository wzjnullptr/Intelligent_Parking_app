package com.example.administrator.intelligentparkingapp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.intelligentparkingapp.MainActivity;
import com.example.administrator.intelligentparkingapp.R;
import com.example.administrator.intelligentparkingapp.Test.TestActivity;
import com.example.administrator.intelligentparkingapp.appliaction.AppVariables;
import com.example.administrator.intelligentparkingapp.constant.Constants;
import com.example.administrator.intelligentparkingapp.entity.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;

public class LoginActivity extends AppCompatActivity {
    private TextView textView;
    private EditText uname;
    private EditText upwd;
    private EditText editText;
    private EditText editText2;
    private Button login;
    private Button regist;
    private User user;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname = (EditText) findViewById(R.id.uname);
        upwd = (EditText) findViewById(R.id.upwd);
        login = (Button) findViewById(R.id.button);
        regist = (Button) findViewById(R.id.button2);
        user = new User();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAsyn(uname.getText().toString(), upwd.getText().toString());

            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    //异步GET请求
    public void getAsyn(View View) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://";
                OkHttpClient client = new OkHttpClient();
                //get方式
                //Request request = new Request.Builder().url(url).build();
                //post方式
                RequestBody body = new FormBody.Builder().add("password", "111")
                        .add("username", "111").build();
                Request request = new Request.Builder().post(body).url(url).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        res = response.body().toString();
                    }
                });
            }
        }).start();
    }


    public void postAsyn(final String name, final String pwd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                user.setUname(name);

                RequestBody body = new FormBody.Builder()
                        .add("name",name)
                        .add("pwd",pwd)
                        .build();

                final String url = String.format("%s/%s", Constants.context, "user/login/do");
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String str = response.body().string();
                        Map<String,Object> map = com.alibaba.fastjson.JSONObject.parseObject(str,Map.class) ;
                        int flag =(Integer)map.get("status");

                        if (flag==1){
                            //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                            Headers headers = response.headers();
                            Log.d("info_headers", "header " + headers);
                            List<String> cookies = headers.values("Set-Cookie");
                            String session = cookies.get(0);
                            Log.d("info_cookies", "onResponse-size: " + cookies);

                            AppVariables.cookieStr = session.substring(0, session.indexOf(";"));
                            Log.i("info_s", "session is  :" + AppVariables.cookieStr);

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class);

                            Integer uid = (Integer) map.get("uid");
                            user.setUid(uid);
                            Integer umoney = (Integer) map.get("umoney");
                            user.setUmoney(umoney);
                            AppVariables.map.put("user", user);
                            startActivity(intent);
                        }else if (flag==0){
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }else {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }


                    }
                });
            }
        }).start();
    }

}
