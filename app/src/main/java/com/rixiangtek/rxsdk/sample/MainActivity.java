package com.rixiangtek.rxsdk.sample;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rixiangtek.rxlibrary.contants.SwisConstants;
import com.rixiangtek.rxlibrary.utils.EthUtil;
import com.rixiangtek.rxlibrary.utils.ShellUtil;
import com.rixiangtek.rxlibrary.utils.SoundBrightnessUtil;
import com.rixiangtek.rxlibrary.utils.SysUtil;

/**
 * @author ：David
 * @email: mcxiaobing@outlook.com
 */
public class MainActivity extends AppCompatActivity {


    private Button btn_sound;
    private Button btn_brightness;
    private Button btn_shutdown;
    private Button btn_reboot;
    private Button btn_start_sys_ui;
    private Button btn_switch_dhcp;
    private Button btn_switch_static_ip;
    private Button btn_set_static_ip;
    private Button btn_get_sn;
    private Button btn_get_model;
    private Button btn_get_ota_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSwisData();
        setOnclickListener();

    }

    private void initSwisData() {

        /**
         * 读取系统日期，时间毫秒
         */
        Log.e("MainActivity", String.valueOf(System.currentTimeMillis()));

    }

    private void getSnMac() {

        ShellUtil.CommandResult result = ShellUtil.execCommand(SwisConstants.GETPROP_RO_BOOT_SERIALNO, false, true);
        String val = result.responseMsg;
        if (TextUtils.isEmpty(val)) {
            val = "";
        }
        Log.e("MainActivity ", "getSnMac|" + val);
    }


    private void getDeviceSystemVersion() {

        ShellUtil.CommandResult result = ShellUtil.execCommand(SwisConstants.GETPROP_RO_BUILD_DATE, false, true);
        String val = result.responseMsg;
        if (TextUtils.isEmpty(val)) {
            val = "";
        }
        Log.e("MainActivity ", "getDeviceSystemVersion|" + val);
    }

    private void getDeviceProductModel() {

        ShellUtil.CommandResult result = ShellUtil.execCommand(SwisConstants.GETPROP_RO_PRODUCT_MODEL, false, true);
        String val1 = result.responseMsg;
        if (TextUtils.isEmpty(val1)) {
            val1 = "";
        }
        result = ShellUtil.execCommand(SwisConstants.GETPROP_RO_PRODUCT_DEVICE, false, true);
        String val2 = result.responseMsg;
        if (TextUtils.isEmpty(val2)) {
            val2 = "";
        }
        Log.e("MainActivity", "getDeviceProductModel|" + String.format("%s / %s", val1, val2));
    }

    private void startSysUi() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setOnclickListener() {
        btn_sound.setOnClickListener(view -> {
            /**
             * 设置多媒体声音大小
             * process:0 ~ 15
             *
             */
            SoundBrightnessUtil.getInstance().setMediaVolume(this, 2);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        });

        btn_brightness.setOnClickListener(view -> {
            /**
             * 设置系统亮度
             * processIndex  0 ~ 255
             */
            SoundBrightnessUtil.getInstance().setBrightNess(this, 2);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        });


        btn_shutdown.setOnClickListener(view -> {

            /**
             * 关机
             */
            SysUtil.shutdown(this);
        });

        btn_reboot.setOnClickListener(view -> {
            /**
             * 重启
             */
            SysUtil.reboot(this);
        });


        btn_start_sys_ui.setOnClickListener(view -> {

            /**
             * 进入系统设置
             */
            startSysUi();
        });

        btn_switch_dhcp.setOnClickListener(view -> {

            /**
             * 切换为DHCP
             */
            EthUtil.getInstance().setDHCP(this);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        });

        btn_switch_static_ip.setOnClickListener(view -> {
            /**
             * 切换为为静态ip
             */
            EthUtil.getInstance().setStaticIP(this);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        });

        btn_set_static_ip.setOnClickListener(view -> {

            /**
             * 设置静态ip地址
             */
            EthUtil.getInstance().setStaticIpInput(this, "192.168.1.12", "255.255.255.0", "192.168.1.1"
                    , "8.8.8.8", "8.8.4.4");
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();


        });

        btn_get_sn.setOnClickListener(view -> {

            /**
             * 获取设备序列号，我们的mac是唯一的
             */
            getSnMac();
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        });

        btn_get_model.setOnClickListener(view -> {
            /**
             * 获取设备型号
             */
            getDeviceProductModel();
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        });

        btn_get_ota_version.setOnClickListener(view -> {

            /**
             * 获取OTA系统版本号
             */
            getDeviceSystemVersion();
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        });
    }

    private void initView() {
        btn_sound = (Button) findViewById(R.id.btn_sound);
        btn_brightness = (Button) findViewById(R.id.btn_brightness);
        btn_shutdown = (Button) findViewById(R.id.btn_shutdown);
        btn_reboot = (Button) findViewById(R.id.btn_reboot);
        btn_start_sys_ui = (Button) findViewById(R.id.btn_start_sys_ui);
        btn_switch_dhcp = (Button) findViewById(R.id.btn_switch_dhcp);
        btn_switch_static_ip = (Button) findViewById(R.id.btn_switch_static_ip);
        btn_set_static_ip = (Button) findViewById(R.id.btn_set_static_ip);
        btn_get_sn = (Button) findViewById(R.id.btn_get_sn);
        btn_get_model = (Button) findViewById(R.id.btn_get_model);
        btn_get_ota_version = (Button) findViewById(R.id.btn_get_ota_version);

    }
}