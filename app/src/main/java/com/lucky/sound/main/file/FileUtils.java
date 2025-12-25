package com.lucky.sound.main.file;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @module:
 * @desc:
 * @author: heli
 * @time: 2023/12/1
 */
public class FileUtils {

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/game/storyprogrammer";
    /** 临时目录*/
    public static final String BASE_TEMP_PATH = BASE_PATH + "/temp";

    private Activity activity;

    public FileUtils(Activity context) {
        activity = context;
    }

    /**
     * 复制assets下的文件时用这个方法
     * @throws IOException
     */
    public void copyBigDataBase(String assetName, String savepath, String savename) throws IOException {
        InputStream myInput;
        File dir = new File(savepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbf = new File(savepath + savename);
        if (dbf.exists()) {
            dbf.delete();
        }
        String outFileName = savepath + savename;
        OutputStream myOutput = new FileOutputStream(outFileName);
        myInput = activity.getAssets().open(assetName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
}