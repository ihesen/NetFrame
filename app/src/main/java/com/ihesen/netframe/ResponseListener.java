package com.ihesen.netframe;

/**
 * Created by ihesen on 2019-05-20
 */
public interface ResponseListener<T> {
    void onSuccess(T t);
    void onFailure();
}
