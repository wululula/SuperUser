package com.example.superuser.superuser.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.superuser.superuser.util.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 1、打开应用
 * 2、
 */
public class MainServices extends Service {

    private static LogUtils logUtils;
    private Context mContext;
    private ActivityManager am;
    private TimerTask task;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        logUtils = LogUtils.getInstance();
        startApp();
        task = new TimerTask() {
            @Override
            public void run() {
                if (!istopPackage()){
                    Log.i("aaa","TimerTask");
                    startApp();
                }
            }
        };

        new Timer().schedule(task, 1000*5); // 1s后启动任务，每2s执行一次

        new Thread(new TestRunnable()).start();
    }


    private class TestRunnable implements Runnable{

        @Override
        public void run() {
            while (true){
                while (!istopPackage()){
                    try {
                        startApp();
                        Thread.sleep(1000*5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000*3);
                    startUserApp();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int time = 0;
                while (time < 5){
                    try {
                        Thread.sleep((new Random().nextInt(9)+1) * 1000);  // 随机刷屏时间
                        startSlide();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;
                }
            }
        }
    }


    private boolean istopPackage(){
        return "com.jifen.qukan".equals(getCurTopPackgeName());
    }

    // 点击事件
    private void startUserApp(){
        do_exec("adb shell input tap 508 270 ");
    }

    // 滑动事件
    private void startSlide(){
        do_exec("adb shell input swipe 400 600 400 200");
    }

    public static void do_exec(String cmdString) {
        System.out.println("在cmd里面输入" + cmdString);
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmdString);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null)
                logUtils.addLog(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据包名启动应用
    private void startApp() {
        Log.i("aaa","startApp");
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage("com.jifen.qukan");
        if (intent == null) {
            Toast.makeText(mContext,"请安装趣头条",Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }

    }

    /**
     * 获取最顶层的app包名,若是自己，则指定为其上一个
     */
    public String getCurTopPackgeName() {
        am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String curAppTaskPackgeName = null;
        String myPackageName = mContext.getPackageName();
        List<ActivityManager.RunningTaskInfo> appTask = am.getRunningTasks(Integer.MAX_VALUE);
        if (appTask.size() > 0) {
            curAppTaskPackgeName = appTask.get(0).topActivity.getPackageName();
            if (appTask.size() > 1) {
                if (curAppTaskPackgeName.equals(myPackageName)
                        && appTask.get(1) != null) {
                    curAppTaskPackgeName = appTask.get(1).topActivity
                            .getPackageName();
                }
            }
        }
        Log.i("aaa","curAppTaskPackgeName : "+curAppTaskPackgeName);
        return curAppTaskPackgeName;
    }
}
