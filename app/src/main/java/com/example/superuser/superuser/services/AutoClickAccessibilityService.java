package com.example.superuser.superuser.services;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class AutoClickAccessibilityService extends AccessibilityService {
    private static final String TAG = "aaa";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        try {
            //拿到根节点
            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
            if (rootInfo == null) {
                return;
            }
            //开始遍历，这里拎出来细讲，直接往下看正文
            if (rootInfo.getChildCount() != 0) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInterrupt() {

    }

    /**
     * 深度优先遍历寻找目标节点
     */
    private void DFS(AccessibilityNodeInfo rootInfo) {
        if (rootInfo == null || TextUtils.isEmpty(rootInfo.getClassName())) {
            return;
        }
        if (!"android.widget.GridView".equals(rootInfo.getClassName())) {
            Log.i(TAG, rootInfo.getClassName().toString());
            for (int i = 0; i < rootInfo.getChildCount(); i++) {
                DFS(rootInfo.getChild(i));
            }
        } else {
            Log.i(TAG,"==find gridView==");
            final AccessibilityNodeInfo GridViewInfo = rootInfo;
            for (int i = 0; i < GridViewInfo.getChildCount(); i++) {
                final AccessibilityNodeInfo frameLayoutInfo = GridViewInfo.getChild(i);
                //细心的同学会发现，我代码里的遍历的逻辑跟View树里显示的结构不一样，
                //快照显示的FrameLayout下明明该是LinearLayout，我这里却是TextView，
                //这个我也不知道，实际调试出来的就是这样……所以大家实操过程中也要注意了
                final AccessibilityNodeInfo childInfo = frameLayoutInfo.getChild(0);
                String text = childInfo.getText().toString();
                if (text.equals("专栏")) {
                    performClick(frameLayoutInfo);
                } else {
                    Log.i(TAG,text);
                }
            }
        }
    }

    private void performClick(AccessibilityNodeInfo targetInfo) {
        targetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }
}
