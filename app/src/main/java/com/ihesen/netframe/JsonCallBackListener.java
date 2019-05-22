package com.ihesen.netframe;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ihesen on 2019-05-20
 */
public class JsonCallBackListener<T> implements CallBackListener {

    private Class<T> reponseClazz;
    private ResponseListener responseListener;

    private Handler handler = new Handler(Looper.getMainLooper());

    public JsonCallBackListener(Class<T> clazz, ResponseListener responseListener) {
        this.reponseClazz = clazz;
        this.responseListener = responseListener;

    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String data = getContent(inputStream);
        final T clazz = JSON.parseObject(data, reponseClazz);
        handler.post(new Runnable() {
            @Override
            public void run() {
                responseListener.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    public void onFailure() {
        responseListener.onFailure();
    }
}
