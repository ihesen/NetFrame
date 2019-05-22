package com.ihesen.netframe;


import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by ihesen on 2019-05-20
 */
public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest iHttpRequest;

    public HttpTask(String url, T requestParams, IHttpRequest httpRequest, CallBackListener callBackListener) {
        this.iHttpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(callBackListener);
        String content = JSON.toJSONString(requestParams);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            iHttpRequest.execute();
        } catch (Exception e) {
            //重试机制
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    private long delayTime;
    private int delayCount;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }
}
