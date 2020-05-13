package com.example.administrator.intelligentparkingapp.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.intelligentparkingapp.appliaction.AppVariables;
import com.example.administrator.intelligentparkingapp.constant.Constants;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class RequestUtil {

    public RequestUtil() {
    }

    public RequestUtil(String url, Map map, Callback callback) {
        doPost(url,map,callback);
    }

    public  void doPost(String url, final Map map, final Callback callback) {

        final String urlx = String.format("%s/%s", Constants.context, url);
        final String data = JSONObject.toJSONString(map);


        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
//               RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);
                FormBody.Builder builder = new FormBody.Builder();
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String key = valueOf(entry.getKey());
                    String value = valueOf(entry.getValue());
                    builder.add(key, value);
                }
                Request request = new Request.Builder()
                        .addHeader("Content-Type","application/x-www-form-urlencoded")//添加头部
                        .addHeader("Cookie", AppVariables.cookieStr)
                        .url(urlx)
                        .post(builder.build())
                        .build();
                Call call = client.newCall(request);
                call.enqueue(callback);
            }
        }).start();




    }

    public  Response  doPostSy(String url, Map map) throws IOException {

        final String urlx = String.format("%s/%s", Constants.context, url);
        final String data = JSON.toJSONString(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
        Request request = new Request.Builder()
                .url(urlx)
                .post(body)
                .build();
        Response response = null;
        response = client.newCall(request).execute();

        return response;

    }



}