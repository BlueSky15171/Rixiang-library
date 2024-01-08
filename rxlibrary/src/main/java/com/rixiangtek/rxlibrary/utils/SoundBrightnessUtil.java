package com.rixiangtek.rxlibrary.utils;

import android.content.Context;
import android.media.AudioManager;


/**
 * Created by zhengchuanwei on 2020/10/10.
 * <p>
 * 音量设置
 */

public class SoundBrightnessUtil {

    private static SoundBrightnessUtil instance = null;



    public static SoundBrightnessUtil getInstance(){
        if (instance == null) {
            synchronized (SoundBrightnessUtil.class) {
                if (instance == null) {
                    instance = new SoundBrightnessUtil();
                }
            }
        }
        return instance;
    }

    private AudioManager mgr;

    /**
     * 设置声音  process:0 ~ 15
     */
    public void setMediaVolume(Context context, int process) {
        if (mgr == null) {
            mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (process > 15) {
            process = 15;
        }
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, process, AudioManager.FLAG_PLAY_SOUND);
    }


    /**
     * 设置亮度
     * @param processIndex  0 ~ 255
     */
    public void setBrightNess(Context context, int processIndex) {
        if (processIndex > 255) {
            processIndex = 255;
        }
        BrightnessUtil.setBrightness(context.getContentResolver(), processIndex);
    }
}
