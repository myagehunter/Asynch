package com.example.asynch;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;


/**
 * IntentService是继承于Service并处理异步请求的一个类，在IntentService内有一个工作线程来处理耗时操作，
 * 启动IntentService的方式和启动传统Service一样，同时，当任务执行完后，IntentService会自动停止，而不需要我们去手动控制。
 * 另外，可以启动IntentService多次，而每一个耗时操作会以工作队列的方式在IntentService的onHandleIntent回调方法中执行，并且，
 * 每次只会执行一个工作线程，执行完第一个再执行第二个，以此类推。
 * 而且，所有请求都在一个单线程中，不会阻塞应用程序的主线程（UI Thread），同一时间只处理一个请求。
 * 那么，用IntentService有什么好处呢？首先，我们省去了在Service中手动开线程的麻烦，第二，当操作完成时，我们不用手动停止Service。
 * 接下来让我们来看看如何使用，写一个Demo来模拟两个耗时操作，Operation1与Operation2，先执行1，2必须等1执行完才能执行：
 */
public class IntentServiceDemo extends IntentService {

    public IntentServiceDemo() {
        //必须实现父类的构造方法
        super("IntentServiceDemo");
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return super.onBind(intent);
    }


    @Override
    public void onCreate() {
        System.out.println("onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("onStart");
        super.onStart(intent, startId);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        System.out.println("setIntentRedelivery");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Intent是从Activity发过来的，携带识别参数，根据参数不同执行不同的任务
        String action = intent.getExtras().getString("param");
        if (action.equals("oper1")) {
            System.out.println("Operation1");
        }else if (action.equals("oper2")) {
            System.out.println("Operation2");
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

}
