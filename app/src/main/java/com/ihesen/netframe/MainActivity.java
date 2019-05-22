package com.ihesen.netframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ihesen.netframe.bean.Response;

public class MainActivity extends AppCompatActivity {

    //    private String url = "http://apis.juhe.cn/lottery/types";
    private String url = "http://xxxx";//测试重试机制使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendRequest();
    }

    private void sendRequest() {

        NetUtils.sendRequest(url, null, Response.class, new ResponseListener<Response>() {
            @Override
            public void onSuccess(Response response) {
                Log.e("ihesen", "resultcode:" + response.resultcode + "\nreason:" + response.reason);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
