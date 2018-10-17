package com.example.superuser.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.superuser.services.MainServices;


public class BootBroadcastReceiver extends BroadcastReceiver {
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_ACTION)) {
            Intent serviceintent = new Intent(context, MainServices.class);
            context.startService(serviceintent);
        }
    }
}
