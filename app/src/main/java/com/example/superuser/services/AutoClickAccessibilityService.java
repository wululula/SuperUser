package com.example.superuser.services;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.superuser.model.Contast;
import com.example.superuser.util.MyAccessibilityNodeInfo;


public class AutoClickAccessibilityService extends AccessibilityService {
    private static final String TAG = "aaa";
    private KeyWordReceiver keyWordReceiver;
    private String text;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        try {
            //拿到根节点
            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
            if (rootInfo == null) {
                return;
            }
            //开始遍历，这里拎出来细讲，直接往下看正文
            AccessibilityNodeInfo info = MyAccessibilityNodeInfo.findNodeByViewName(rootInfo, text);
            Log.i(TAG, "rootInfo : " + info);
            if (info != null) {
                performClick(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInterrupt() {

    }

    private void performClick(AccessibilityNodeInfo targetInfo) {
        targetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        keyWordReceiver = new KeyWordReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contast.KEYWORD_ACTION);
        registerReceiver(keyWordReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        if (keyWordReceiver != null) unregisterReceiver(keyWordReceiver);
        super.onDestroy();
    }

    private class KeyWordReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Contast.KEYWORD_ACTION.equals(intent.getAction())) {
                text = intent.getStringExtra(Contast.keyword);
            }
        }
    }
}
