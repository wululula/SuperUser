package com.example.superuser.util;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Arrays;

public class MyAccessibilityNodeInfo {
    private static String TAG = "aaa";
    private static int tabcount = -1;
    private static String viewname;

    public static AccessibilityNodeInfo findNodeByViewName(AccessibilityNodeInfo root, String SName) {
        tabcount = 0;
        int[] is = {};
        viewname = SName;
        return analysisPacketInfo(root, is);
    }

    // 遍历找控件
    private static AccessibilityNodeInfo analysisPacketInfo(AccessibilityNodeInfo info, int... ints) {
        if (info == null) {
            return null;
        }

        String name = info.getClassName().toString();
        String[] split = name.split("\\.");
        name = split[split.length - 1];

        if ("TextView".equals(name) || "Button".equals(name)) {
            CharSequence text = info.getText();
            Log.i(TAG, "text : " + text + " viewname :  " + viewname);
            if (!TextUtils.isEmpty(text) && text.equals(viewname)) {
                Log.i(TAG, " return text : " + text);
                return info;
            }
        } else {
            int count = info.getChildCount();
            if (count > 0) {
                tabcount++;
                int len = ints.length + 1;
                int[] newInts = Arrays.copyOf(ints, len);

                for (int i = 0; i < count; i++) {
                    newInts[len - 1] = i;
                    AccessibilityNodeInfo result = analysisPacketInfo(info.getChild(i), newInts);
                    if (result != null) {
                        Log.i(TAG, " return result ");
                        return result;
                    }
                }
                tabcount--;
            }
        }
        Log.i(TAG, " return null 222 ");
        return null;
    }
}
