package com.ihesen.netframe;

import java.io.InputStream;

/**
 * Created by ihesen on 2019-05-20
 */
public interface CallBackListener {

    void onSuccess(InputStream inputStream);

    void onFailure();
}
