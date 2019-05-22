package com.ihesen.netframe;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ihesen on 2019-05-20
 */
public class ThreadPoolManager {

    private static ThreadPoolManager mThreadPoolManager = new ThreadPoolManager();

    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //线程池执行失败的任务，重新添加的任务队列中
                addTask(r);
            }
        });
        mThreadPoolExecutor.execute(coreTask);
        mThreadPoolExecutor.execute(delayTask);
    }

    public static ThreadPoolManager getInstance() {
        return mThreadPoolManager;
    }

    //1、创建任务队列
    private LinkedBlockingQueue<Runnable> mTaskQuene = new LinkedBlockingQueue<>();

    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mTaskQuene.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //2、创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    //3、创建任务轮询
    public Runnable coreTask = new Runnable() {
        Runnable runnable = null;

        @Override
        public void run() {
            while (true) {
                try {
                    runnable = mTaskQuene.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runnable);
            }
        }
    };

    //重试机制 使用延迟队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask httpTask) {
        if (httpTask != null) {
            httpTask.setDelayTime(3000);
            mDelayQueue.put(httpTask);
        }

    }

    public Runnable delayTask = new Runnable() {
        HttpTask httpTask = null;

        @Override
        public void run() {
            while (true) {
                try {
                    httpTask = mDelayQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重试机制 小于3次，重新执行
                if (httpTask.getDelayCount() < 3) {
                    mThreadPoolExecutor.execute(httpTask);
                    httpTask.setDelayCount(httpTask.getDelayCount() + 1);
                    Log.e("ihesen", "==== 重试机制 ==== 重试第" + httpTask.getDelayCount() + "次 重试时间：" + httpTask.getDelayTime());
                } else {
                    Log.e("ihesen", "==== 重试机制 ==== 已经超过三次了");
                }
            }
        }
    };

}
