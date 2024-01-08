package com.rixiangtek.rxlibrary.contants;

/**
 * @author David Zheng
 * @date 2021/08/31
 * <p>
 * 常量
 */
public interface SwisConstants {


    /**
     * 亮屏
     */
    String SCREEN_KEY_EVENT_ON = "input keyevent 224";
    /**
     * 息屏
     */
    String SCREEN_KEY_EVENT_OFF = "input keyevent 276";

    /**
     * 打开swis系统日志
     */
    String SYS_SUNORIEN_CATLOG_1 = "setprop sys.sunorien.catlog 1";

    /**
     * 关闭framework看门狗
     */
    String CLOSE_FRAMEWORK_WATCH_DOG = "setprop persist.HWWD.fw 0";
    /**
     * 获取系统版本日期
     */
    String GETPROP_RO_BUILD_DATE = "getprop ro.build.date";

    /**
     * TV电视apk包名
     */
    String APP_PACKAGE_COM_TVAPPK8_MAIN = "com.tvappk8.main";
    /**
     * tv电视apk 首页启动类
     */
    String APP_PACKAGE_COM_TVAPPK8_MAIN_ACT = "com.tvappk8.tvlive.TvLiveActivity";

    /**
     * 获取cpu频率
     */
    String GETPROP_SYS_SUNORIEN_CPUFREQ = "getprop sys.sunorien.cpufreq";
    /**
     * 获取serialno
     */
    String GETPROP_RO_BOOT_SERIALNO = "getprop ro.boot.serialno";
    /**
     * 获取product>model
     */
    String GETPROP_RO_PRODUCT_MODEL = "getprop ro.product.model";
    /**
     * 获取product>device
     */
    String GETPROP_RO_PRODUCT_DEVICE = "getprop ro.product.device";
    /**
     * 获取catlog
     */
    String GETPROP_SYS_SUNORIEN_CATLOG = "getprop sys.sunorien.catlog";


    String SWIS_MAC_CONFIG_PATH = "/mnt/private/ULI/factory/mac.txt";


}
