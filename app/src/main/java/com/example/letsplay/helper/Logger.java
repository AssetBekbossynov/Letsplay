package com.example.letsplay.helper;

import android.util.Log;

import com.example.letsplay.BuildConfig;

public class Logger {

    public static void msg(Object msg) {
        if(BuildConfig.DEBUG) {
            Log.i("MSG", msg + "");
        }
    }

    public static void api(String msg) {
        if(BuildConfig.DEBUG) {
            Log.i("API", msg);
        }
    }

    public static void msg(String tag, Object msg) {
        if(BuildConfig.DEBUG) {
            Log.i(tag, msg + "");
        }
    }
}
