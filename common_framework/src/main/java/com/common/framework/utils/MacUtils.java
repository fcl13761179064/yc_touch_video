package com.common.framework.utils;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.common.fragment.BuildConfig;
import com.common.framework.base.BaseApplication;
import com.common.framework.storage.MMKVManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MacUtils {

    private static String mac;

    public static String getMacFromHardware() {
      if (!TextUtils.isEmpty(mac)) return mac;
        String deviceMac = MMKVManager.INSTANCE.getInstance().decodeString("deviceMac", null);
        if (!TextUtils.isEmpty(deviceMac)) {
            mac = deviceMac;
            return mac;
        }
        String ret = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                Log.e("MacUtils", "NET_CARD_NAME:" + nif.getName());
                if (!nif.getName().equalsIgnoreCase("eth0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    ret = "";
                }

                StringBuilder res1 = new StringBuilder();
                if (macBytes != null) {
                    for (int i = 0; i < macBytes.length; i++) {
                        res1.append(String.format("%02X", macBytes[i]));
                        if (i != macBytes.length - 1) res1.append(":");
                    }
                }
                ret = res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //失败则取androidID
        if (TextUtils.isEmpty(ret)) {

            ret = Settings.Secure.getString(BaseApplication.mApplication.getContentResolver(), Settings.Secure.ANDROID_ID);

            System.out.println("11111111ANDROID_ID" + ret);
        }

        if (TextUtils.isEmpty(ret)) {
            ret = UUID.randomUUID().toString();
            System.out.println("11111111UUID" + ret);
        }

        mac = ret;
       //  ret = "fafdsdg-5464896-99dfs";
        if (!TextUtils.isEmpty(mac)) {
            MMKVManager.INSTANCE.getInstance().encode("deviceMac", mac);
        }
        return ret;
    }
}
