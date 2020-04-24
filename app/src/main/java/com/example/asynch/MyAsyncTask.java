package com.example.asynch;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * 步骤1：创建AsyncTask子类
 * 注：
 * a. 继承AsyncTask类
 * b. 为3个泛型参数指定类型；若不使用，可用java.lang.Void类型代替
 * c. 根据需求，在AsyncTask子类内实现核心方法:
 */

public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    //onPreExecute用于异步处理前的操作
    private ImageView imageView;
    private Context context;
    private int mItemSize;

    @Override
    protected void onPreExecute() {
        ////此方法会在后台任务执行前被调用，用于进行一些准备工作
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ////后台任务执行完毕后，此方法会被调用，参数即为后台任务的返回结果
        super.onPostExecute(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //由publishProgress内部调用，表示任务进度更新
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
    }

    @Override
    protected void onCancelled() {
        //此方法会在后台任务被取消时被调用
        super.onCancelled();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        ////此方法中定义要执行的后台任务，在这个方法中可以调用
        //获取传进来的参数
        String url = params[0];
        Bitmap bitmap = null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
        File file = new File(url);//实例化File对象，文件路径为
        if (file.exists()) {
            mmr.setDataSource(file.getAbsolutePath());//设置数据源为该文件对象指定的绝对路径
            bitmap = mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
        }
        return bitmap;
    }


}
