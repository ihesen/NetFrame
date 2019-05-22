package com.ihesen.netframe;

/**
 * Created by ihesen on 2019-05-20
 */
public class NetUtils {

    public static <T, D> void sendRequest(String url, T request, Class<D> clazz, ResponseListener<D> responseListener) {
        IHttpRequest jsonHttpRequest = new JsonHttpRequest();
        CallBackListener jsonCallBackListener = new JsonCallBackListener<>(clazz, responseListener);
        HttpTask httpTask = new HttpTask(url, request, jsonHttpRequest, jsonCallBackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
