package com.wwengine.hw;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by suixi on 2018/9/22.
 */
public class WWHandWrite {
    public static final int WWHW_RANGE_ASC_SYMBOL = 8;
    public static final int WWHW_RANGE_BIG5 = 512;
    public static final int WWHW_RANGE_CHN_SYMBOL = 2048;
    public static final int WWHW_RANGE_GB2312 = 32768;
    public static final int WWHW_RANGE_GBK = 1024;
    public static final int WWHW_RANGE_LOWER_CHAR = 2;
    public static final int WWHW_RANGE_NUMBER = 1;
    public static final int WWHW_RANGE_UPPER_CHAR = 4;
    private static boolean isRealMachine;

    static {
        isRealMachine=checkRealPhone();
        if(isRealMachine)
            System.loadLibrary("dwEngineHw");
    }
    private static native int apkBinding(Context context);
    private static native int hwInit(byte[] buffer, int option);
    private static native int hwRecognize(short[] points, char[] chars, int option1, int option2);

    public static boolean checkRealPhone() {
        String cpuInfo = readCpuInfo();
        if ((cpuInfo.contains("intel") /*|| cpuInfo.contains("amd")*/)) {
            return false;
        }
        return true;
    }
    public static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {

        }
        return result;
    }

    public   int java_apkBinding(Context context){
        if(isRealMachine)
            return apkBinding(context);
        return 0;
    }
    public   int java_hwInit(byte[] buffer, int option){
        if(isRealMachine)
            return hwInit(buffer,option);
        return 0;
    }
    public  int java_hwRecognize(short[] points, char[] chars, int option1, int option2){
        if(isRealMachine)
            return hwRecognize(points,chars,option1,option2);
        chars[0]='模';
        chars[1]='拟';
        chars[2]='器';
        chars[3]='测';
        chars[4]='试';
        return 5;
    }


    public byte[] readData(AssetManager assetManager, String path) {
        try {
            InputStream is = assetManager.open(path);
            if (is == null) {
                return null;
            }
            int i = is.available();
            if (i <= 0) {
                return null;
            }
            byte[] buffer = new byte[i];
            if (is.read(buffer, 0, i) == -1) {
                return null;
            }
            is.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
