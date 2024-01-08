package com.rixiangtek.rxlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.LinkAddress;
import android.net.StaticIpConfiguration;
import android.text.TextUtils;
import android.util.Log;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.InetAddress;

import static com.rixiangtek.rxlibrary.utils.NetUtil.getIPv4Address;


/**
 * @Author David
 * @email 986945193@qq.com
 * @Date 2019/12/10 13:02
 */

public class EthUtil {


    private static EthUtil instance = null;


    public static EthUtil getInstance() {
        if (instance == null) {
            synchronized (EthUtil.class) {
                if (instance == null) {
                    instance = new EthUtil();
                }
            }
        }
        return instance;
    }


    StaticIpConfiguration mStaticIpConfiguration;


    IpConfiguration mIpConfiguration;
    EthernetManager mEthManager;

    private String mEthIpAddress = "";  //IP
    private String mEthNetmask = "255.255.255.0";  //  子网掩码
    private String mEthGateway = "";   //网关
    private String mEthdns1 = "8.8.8.8";   // DNS1
    private String mEthdns2 = "8.8.4.4";   // DNS2

    @SuppressLint({"WrongConstant", "NewApi"})
    public void setStaticIP(final Context context) {
                mEthIpAddress = NetUtil.getIpAddress(context);
                mEthGateway = NetUtil.getGateWay();
                if (TextUtils.isEmpty(mEthIpAddress)) {
                    Log.e("EthUtil","setStaticIP  ip address is null");
                    return;
                }

                mEthManager = (EthernetManager) context.getSystemService("ethernet");
                if (mEthManager.getConfiguration().ipAssignment == IpConfiguration.IpAssignment.DHCP) {
                    // 如果自动获取
                    mEthManager = (EthernetManager) context.getSystemService("ethernet");
                    mStaticIpConfiguration = new StaticIpConfiguration();
                    Inet4Address inetAddr = getIPv4Address(mEthIpAddress);
                    int prefixLength = NetUtil.maskStr2InetMask(mEthNetmask);
                    InetAddress gatewayAddr = getIPv4Address(mEthGateway);
                    InetAddress dnsAddr = getIPv4Address(mEthdns1);

                    if (inetAddr.getAddress().toString().isEmpty() || prefixLength == 0 || gatewayAddr.toString().isEmpty()
                            || dnsAddr.toString().isEmpty()) {
                        return;
                    }

                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName("android.net.LinkAddress");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    Class[] cl = new Class[]{InetAddress.class, int.class};
                    Constructor cons = null;

                    //取得所有构造函数
                    try {
                        cons = clazz.getConstructor(cl);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    //给传入参数赋初值
                    Object[] x = {inetAddr, prefixLength};

                    String dnsStr2 = mEthdns2;
                    //mStaticIpConfiguration.ipAddress = new LinkAddress(inetAddr, prefixLength);
                    try {
                        mStaticIpConfiguration.ipAddress = (LinkAddress) cons.newInstance(x);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    mStaticIpConfiguration.gateway = gatewayAddr;
                    mStaticIpConfiguration.dnsServers.add(dnsAddr);

                    if (!dnsStr2.isEmpty()) {
                        mStaticIpConfiguration.dnsServers.add(getIPv4Address(dnsStr2));
                    }

                    mIpConfiguration = new IpConfiguration(false ? IpConfiguration.IpAssignment.DHCP : IpConfiguration.IpAssignment.STATIC, IpConfiguration.ProxySettings.NONE, mStaticIpConfiguration, null);
                    mEthManager.setConfiguration(mIpConfiguration);
                    Log.e("EthUtil","静态ip设置成功");
                } else {
                    Log.e("EthUtil","已经是静态ip了，不用再次设置了");
                }



    }

    @SuppressLint({"WrongConstant", "NewApi"})
    public void setDHCP(final Context context) {
            mEthIpAddress = NetUtil.getIpAddress(context);
            mEthGateway = NetUtil.getGateWay();

            mEthManager = (EthernetManager) context.getSystemService("ethernet");
            // 如果自动获取
            mEthManager = (EthernetManager) context.getSystemService("ethernet");
            mStaticIpConfiguration = new StaticIpConfiguration();
            Inet4Address inetAddr = getIPv4Address(mEthIpAddress);
            int prefixLength = NetUtil.maskStr2InetMask(mEthNetmask);
            InetAddress gatewayAddr = getIPv4Address(mEthGateway);
            InetAddress dnsAddr = getIPv4Address(mEthdns1);

            if (inetAddr.getAddress().toString().isEmpty() || prefixLength == 0 || gatewayAddr.toString().isEmpty()
                    || dnsAddr.toString().isEmpty()) {
                return;
            }

            Class<?> clazz = null;
            try {
                clazz = Class.forName("android.net.LinkAddress");
            } catch (Exception e) {
                // TODO: handle exception
            }

            Class[] cl = new Class[]{InetAddress.class, int.class};
            Constructor cons = null;

            //取得所有构造函数
            try {
                cons = clazz.getConstructor(cl);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            //给传入参数赋初值
            Object[] x = {inetAddr, prefixLength};

            String dnsStr2 = mEthdns2;
            //mStaticIpConfiguration.ipAddress = new LinkAddress(inetAddr, prefixLength);
            try {
                mStaticIpConfiguration.ipAddress = (LinkAddress) cons.newInstance(x);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            mStaticIpConfiguration.gateway = gatewayAddr;
            mStaticIpConfiguration.dnsServers.add(dnsAddr);

            if (!dnsStr2.isEmpty()) {
                mStaticIpConfiguration.dnsServers.add(getIPv4Address(dnsStr2));
            }

            mIpConfiguration = new IpConfiguration(true ? IpConfiguration.IpAssignment.DHCP : IpConfiguration.IpAssignment.STATIC, IpConfiguration.ProxySettings.NONE, mStaticIpConfiguration, null);
//        mEthManager.set
            mEthManager.setConfiguration(mIpConfiguration);
        Log.e("EthUtil","DHCP设置成功");


    }


    @SuppressLint({"WrongConstant", "NewApi"})
    public void setStaticIpInput(final Context context
            ,String ip
            ,String netmask
            ,String gateway
            ,String dns1
            ,String dns2) {

        if (TextUtils.isEmpty(ip)) {
            Log.e("EthUtil","setStaticIP  ip address is null");
            return;
        }

        mEthManager = (EthernetManager) context.getSystemService("ethernet");
        if (mEthManager.getConfiguration().ipAssignment == IpConfiguration.IpAssignment.DHCP) {
            // 如果自动获取
            mEthManager = (EthernetManager) context.getSystemService("ethernet");
            mStaticIpConfiguration = new StaticIpConfiguration();
            Inet4Address inetAddr = getIPv4Address(ip);
            int prefixLength = NetUtil.maskStr2InetMask(netmask);
            InetAddress gatewayAddr = getIPv4Address(gateway);
            InetAddress dnsAddr = getIPv4Address(dns1);

            if (inetAddr.getAddress().toString().isEmpty() || prefixLength == 0 || gatewayAddr.toString().isEmpty()
                    || dnsAddr.toString().isEmpty()) {
                return;
            }

            Class<?> clazz = null;
            try {
                clazz = Class.forName("android.net.LinkAddress");
            } catch (Exception e) {
                // TODO: handle exception
            }

            Class[] cl = new Class[]{InetAddress.class, int.class};
            Constructor cons = null;

            //取得所有构造函数
            try {
                cons = clazz.getConstructor(cl);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            //给传入参数赋初值
            Object[] x = {inetAddr, prefixLength};

            String dnsStr2 = dns2;
            //mStaticIpConfiguration.ipAddress = new LinkAddress(inetAddr, prefixLength);
            try {
                mStaticIpConfiguration.ipAddress = (LinkAddress) cons.newInstance(x);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            mStaticIpConfiguration.gateway = gatewayAddr;
            mStaticIpConfiguration.dnsServers.add(dnsAddr);

            if (!dnsStr2.isEmpty()) {
                mStaticIpConfiguration.dnsServers.add(getIPv4Address(dnsStr2));
            }

            mIpConfiguration = new IpConfiguration(false ? IpConfiguration.IpAssignment.DHCP : IpConfiguration.IpAssignment.STATIC, IpConfiguration.ProxySettings.NONE, mStaticIpConfiguration, null);
            mEthManager.setConfiguration(mIpConfiguration);
            Log.e("EthUtil","静态ip设置成功");
        } else {
            Log.e("EthUtil","已经是静态ip了，不用再次设置了");
        }



    }
}
