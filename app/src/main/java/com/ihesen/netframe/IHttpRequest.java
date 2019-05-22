package com.ihesen.netframe;

/**
 * Created by ihesen on 2019-05-20
 */
public interface IHttpRequest {

    void setUrl(String url);
    void setData(byte[] data);
    void setListener(CallBackListener callBackListener);
    void execute();
}
