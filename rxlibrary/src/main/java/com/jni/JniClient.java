package com.jni;

public class JniClient {
    static {
        System.loadLibrary("JNI1");
    }


    /**
     * 灯 开关
     *
     * @param color 灯的颜色。红灯:1，蓝灯:3，手柄灯:6，紧急指示灯：5
     * @param state 大于1小于255，如：255:on  0:off
     * @return 0:成功，-1:失败
     */
    static public native int switchLed(int color, int state);
}
