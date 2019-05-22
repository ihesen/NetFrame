package com.ihesen.netframe;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ihesen on 2019-05-20
 */
public class JsonHttpRequest implements IHttpRequest {

    private String url;
    private CallBackListener callBackListener;
    private byte[] data;
    private HttpURLConnection httpURLConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public void execute() {
        URL url = null;
        try {
            url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();//打开http连接
            httpURLConnection.setConnectTimeout(6000);//设置超时时间
            httpURLConnection.setUseCaches(false);//不使用缓存
            httpURLConnection.setInstanceFollowRedirects(true);//设置链接是否可以重定向
            httpURLConnection.setReadTimeout(3000);//设置响应的超时时间
            httpURLConnection.setDoInput(true);//设置连接是否可以写入数据
            httpURLConnection.setDoOutput(true);//设置连接是否可以输出数据
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.connect();//与服务器建立TCP连接
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bos.write(data);
            bos.flush();
            outputStream.close();
            bos.close();
            //得到服务器的返回码是否连接成功
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                callBackListener.onSuccess(inputStream);
            } else {
                callBackListener.onFailure();
                throw new RuntimeException("请求失败");
            }
        } catch (Exception e) {
            callBackListener.onFailure();
            throw new RuntimeException("请求失败");
        } finally {
            httpURLConnection.disconnect();
        }

    }
}
