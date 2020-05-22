package com.example.livepushclient.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.livepushclient.app.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Desc：Created by panyemin on 16/6/14.
 * Email：1261329618@qq.com
 */
public class CLogUtil {
    public static boolean bDebug = true;
    private static final int num = 1500;
    private static final String TAG = "CLogUtil";


    public static void V(String msg){
        if(bDebug && !TextUtils.isEmpty(msg)){
            while(msg.length() > num){
                Log.v(TAG, msg.substring(0, num));
                msg = msg.substring(num);
            }
            Log.i(TAG, msg);
        }
        writeTextFileAppend(msg);
    }

    public static void D(String msg){
        if(bDebug && !TextUtils.isEmpty(msg)){
            while(msg.length() > num){
                Log.d(TAG, msg.substring(0, num));
                msg = msg.substring(num);
            }
            Log.i(TAG, msg);
        }
        writeTextFileAppend(msg);
    }

    public static void I(String msg){
        if(bDebug && !TextUtils.isEmpty(msg)){
            while(msg.length() > num){
                Log.i(TAG, msg.substring(0, num));
                msg = msg.substring(num);
            }
            Log.i(TAG, msg);
        }
        writeTextFileAppend(msg);
    }

    public static void I(String tag, String msg){
        if(bDebug && !TextUtils.isEmpty(msg)){
            while(msg.length() > num){
                Log.i(tag, msg.substring(0, num));
                msg = msg.substring(num);
            }
            Log.i(tag, msg);
        }
        writeTextFileAppend(msg);
    }

    public static void W(String msg){
        if(bDebug && !TextUtils.isEmpty(msg)){
            while(msg.length() > num){
                Log.w(TAG, msg.substring(0, num));
                msg = msg.substring(num);
            }
            Log.w(TAG, msg);
        }
        writeTextFileAppend(msg);
    }

    public static void E(String msg){
        if(!TextUtils.isEmpty(msg)){
            while(msg.length() > num){
                Log.e(TAG, msg.substring(0, num));
                msg = msg.substring(num);
            }
            Log.e(TAG, msg);
        }
        writeTextFileAppend(msg);
    }

    public static void V(String key, String msg){
        if(key != null && key.length() > 0){
            if(bDebug && !TextUtils.isEmpty(msg)){
                while(msg.length() > num){
                    Log.v(key, msg.substring(0, num));
                    msg = msg.substring(num);
                }
                Log.v(key, msg);
            }
        }
        writeTextFileAppend(msg);
    }

    public static void D(String key, String msg){
        if(key != null && key.length() > 0){
            if(bDebug && !TextUtils.isEmpty(msg)){
                while(msg.length() > num){
                    Log.d(key, msg.substring(0, num));
                    msg = msg.substring(num);
                }
                Log.i(key, msg);
            }
        }
        writeTextFileAppend(msg);
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式;
    public static SimpleDateFormat dateFormat2 =new SimpleDateFormat("yyyy-MM-dd");

    public static void writeTextFileAppend(String str) {
        if (bDebug){
            BufferedWriter out = null;
            try {
                File file=new File(Environment.getExternalStorageDirectory()+"/live"+ MyApplication.getInstance().getPackageName()+"-"+dateFormat2.format(new Date())+".log");
                str=dateFormat.format(new Date())+" "+str;
                str+="\n";
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(str);
            } catch (Exception e) {
                Log.d(TAG,"没有读写权限");
            } finally{
                try {
                    if (out!=null){
                        out.close();
                    }
                } catch (IOException e) {

                }
            }
        }
    }

}
