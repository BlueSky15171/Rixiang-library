package com.rixiangtek.rxlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @Author David
 * @email 986945193@qq.com
 * @Date 2019/12/10 13:02
 */
public class NetUtil {
    public static boolean isConnect = false,
            isCheck = false,
            isPppoeConnect = false;
    private static Thread mThread = null;
    int netconfigDetail, valueEthernet, valueWifi;

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {

        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }


    public static boolean isNetworkAvailable() {
        return isConnect;
    }

    @SuppressLint("MissingPermission")
    public int getNetwork(Context context) {
        isCheck = true;
        isConnect = false;
        WifiManager mWifiManager;
        ConnectivityManager mConnectivityManager;
        NetworkInfo ethernetNetInfo;

        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);//获得WifiManager对象
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);//获得ConnectivityManager对象
        ethernetNetInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);//获得NetworkInfo对象


        if (ethernetNetInfo.isConnected()) {
            netconfigDetail = 4;//如果有线网连接，值为4
            valueEthernet = 1;

        } else {

            if (WifiManager.WIFI_STATE_ENABLED == mWifiManager.getWifiState()) {
                if (mWifiManager.getConnectionInfo().getIpAddress() == 0) {
                    valueWifi = -1;//如果无线网可用，值为-1
                    netconfigDetail = 6;//如果无线网不可用，值为6
                } else {
                    netconfigDetail = WifiManager.calculateSignalLevel(mWifiManager
                            .getConnectionInfo().getRssi(), 4);
                    //如果无线网可用，值为0-3
                }

            } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                netconfigDetail = 5;//如果无线网不可用，值为5
                valueEthernet = -1;//如果无线网不可用，值为-1
            }

        }
        if (valueWifi < 0 && valueEthernet < 0) {
            checkPppoe();//满足判断的条件则判断是否可用，判断网络连通（包含了pppoe的检测）
        } else {
            isConnect = true;
            isCheck = false;
            isPppoeConnect = false;

        }
        Log.e("getNetwork(Context", "valueWifi   " + netconfigDetail);
        return netconfigDetail;

    }

    public static void checkPppoe() {
        if (mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();//检测前将thread置为null
            mThread = null;
        }
        if (mThread == null || mThread.isInterrupted()) {
            mThread = new Thread(() -> {
                isCheck = true;
                isConnect = false;
                isConnect = isConnected();
                isCheck = false;
                isPppoeConnect = isConnect;
            });
            mThread.start();
        }
    }

    private static boolean isConnected() {
        //发送一个请求用来检测是否已连接网络，并判断通畅
        //在这里你也可以ping某个IP，来判断是否已连接
        try {
            URL url = new URL("https://www.baidu.com");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("connection", "close");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            BufferedReader reader = null;

            try {
                conn.connect();
                try {
                    if (conn.getInputStream() != null)
                        reader = new BufferedReader(new InputStreamReader(
                                conn.getInputStream()), 512);
                } catch (IOException e) {
                    if (conn.getErrorStream() != null)
                        reader = new BufferedReader(new InputStreamReader(
                                conn.getErrorStream()), 512);
                }

                if (reader != null)
                    reader.close();

                if (conn != null)
                    conn.disconnect();
                return true;
            } catch (SocketException e) {
                e.printStackTrace();
                return false;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignored) {

        }
        return "0.0.0.0";

    }

    /*
     * convert subMask string to prefix length
     */
    public static int maskStr2InetMask(String maskStr) {
        StringBuffer sb;
        String str;
        int inetmask = 0;
        int count = 0;
        /*
         * check the subMask format
         */
        Pattern pattern = Pattern.compile("(^((\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])$)|^(\\d|[1-2]\\d|3[0-2])$");
        if (!pattern.matcher(maskStr).matches()) {
            Log.e("33333", "subMask is error");
            return 0;
        }

        String[] ipSegment = maskStr.split("\\.");
        for (String s : ipSegment) {
            sb = new StringBuffer(Integer.toBinaryString(Integer.parseInt(s)));
            str = sb.reverse().toString();
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf("1", i);
                if (i == -1)
                    break;
                count++;
            }
            inetmask += count;
        }
        return inetmask;
    }


    public static Inet4Address getIPv4Address(String text) {
        try {
            return (Inet4Address) NetworkUtils.numericToInetAddress(text);
        } catch (IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }


    public static String getGateWay() {
        String[] arr;
        try {
            Process process = Runtime.getRuntime().exec("ip route list table 0");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String string = in.readLine();

            arr = string.split("\\s+");
            return arr[2];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
