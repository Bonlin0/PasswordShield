package cn.adminzero.passwordshield_demo0.util;

import android.util.Log;
import android.widget.Toast;

import cn.adminzero.passwordshield_demo0.MyApplication;

public class LogUtils {

    public static String tag = "debug";

    public static void d(String msg) {
        Log.d(tag, msg);
    }

    public static void t(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}