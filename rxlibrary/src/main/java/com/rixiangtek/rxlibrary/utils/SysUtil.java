package com.rixiangtek.rxlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import java.lang.reflect.Method;

/**
 * Created by David on 2020/5/27.
 */

public class SysUtil {


    //关机
    @SuppressLint("ObsoleteSdkInt")
    public static  void shutdown(Context context) {
        if (Build.VERSION.SDK_INT > 25) {
            try {
                Class<?> serviceManager = Class.forName("android.os.ServiceManager");
                Method getService = serviceManager.getMethod("getService", String.class);
                Object remoteService = getService.invoke(null, Context.POWER_SERVICE);
                Class<?> stub = Class.forName("android.os.IPowerManager$Stub");
                Method asInterface = stub.getMethod("asInterface", IBinder.class);
                Object powerManager = asInterface.invoke(null, remoteService);
                Method shutdown = powerManager.getClass().getDeclaredMethod("shutdown",
                        boolean.class, String.class, boolean.class);
                shutdown.invoke(powerManager, false, "", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent shutdown = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            // 这里设置的是：是否需要用户确认，若不需要，可以不设置或设置为false
            shutdown.putExtra("android.intent.extra.KEY_CONFIRM", false);
            shutdown.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shutdown);
        }
    }

    // 重启
    public static  void reboot(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pm.reboot("");
    }

}
