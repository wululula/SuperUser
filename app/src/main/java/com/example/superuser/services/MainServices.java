package com.example.superuser.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.superuser.util.LogUtils;

import java.util.List;
import java.util.TimerTask;

/**
 * 1、打开应用
 * 2、
 */
public class MainServices extends Service {
    private String TAG = "aaa";

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
                if (!istopPackage()) {
                    Log.i(TAG, "TimerTask");
                    startApp();
                }
            }
        };

        startAccessService();

        //new Timer().schedule(task, 1000*5); // 1s后启动任务，每5s执行一次

    }

    private void startAccessService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean istopPackage() {
        return "com.jifen.qukan".equals(getCurTopPackgeName());
    }

    // 根据包名启动应用
    private void startApp() {
        Log.i(TAG, "startApp");
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage("com.jifen.qukan");
        if (intent == null) {
            Toast.makeText(mContext, "请安装趣头条", Toast.LENGTH_SHORT).show();
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
        Log.i(TAG, "curAppTaskPackgeName : " + curAppTaskPackgeName);
        return curAppTaskPackgeName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
