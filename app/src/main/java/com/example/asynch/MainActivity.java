package com.example.asynch;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * AsyncTask、IntentService、HandlerThread，RxJava，runOnUiThread
 */
public class MainActivity extends AppCompatActivity {

    private Handler getmHandler = null;
    private Disposable mDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("hadler","发送");
                //发消息到目标子线程
                if(getmHandler!=null){
                    getmHandler.obtainMessage(0).sendToTarget();
                }
            }
        });
        Main();
    }

    private void Main() {
        //2.1子线程通知主线程实现方式
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        });
        mThread.start();
        //2.2 主线程发消息给子线程
        new MyThread().start();
        //3.Rxjava
        //4.runOnUiThread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*  Runnable对像就能在ui程序中被调用。如果当前线程是UI线程，那么行动是立即执行,
                如果当前线程不是UI线程,操作是发布到事件队列的UI线程*/
            }
        });
        //5.handler 两种发送消息方式  handler.post(Runnabel)/handler.post(Runnable,1000)
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            //延迟操作
            }
        },100);
    }

    /**
     * 1.Asynctask   a创建AsyncTask子类的实例对象（即 任务实例）
     * *   b. 同一个AsyncTask实例对象只能执行1次，若执行第2次将会抛出异常
     * c. 执行任务中，系统会自动调用AsyncTask的一系列方法：onPreExecute() 、doInBackground()、onProgressUpdate() 、onPostExecute()
     */
    private void Task() {
        MyAsyncTask mTask = new MyAsyncTask();
        mTask.execute();
    }

    /**
     * 2.1 .Handler 最简单的使用Handler完成子线程和主线程的通信（子线程发消息给主线程）
     * 注：1.子线程异步通知主线程刷新UI  2.主线程通知子线程通信方式
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 0) {
                Log.d("hadler","更新UI");
                //在主线程中需要执行的操作，一般是UI操作
            }
        }
    };

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            //建立消息循环，初始化looper
            Looper.prepare();
            getmHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.d("hadler","收到");
                }
            };
            //启动消息循环
            Looper.loop();
        }
    }

    /**
     * 3.Rxjava 是目前比较流行的异步操作，经常用户网络请求回调  原理主要是订阅关系，线程切换，它以数据流回调
     */
    private void RxJava(){
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        closeTimer();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
    /**
     * 关闭定时器
     */
    public void closeTimer() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

}
