package cn.adminzero.passwordshield_demo0.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.adminzero.passwordshield_demo0.MyApplication;

public class MyStorage {


    private SharedPreferences preferences;

    //PasswordSheild保存于PasswordSheild
    public MyStorage() {
        preferences = MyApplication.getContext().getSharedPreferences(MyApplication.getDbName(), Activity.MODE_PRIVATE);
    }

    //没有String 默认 ""
    public String getData(String key) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        return preferences.getString(key, "");
    }

    //保存data
    public boolean storeData(String key, String data) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        return editor.commit();
    }

    //约束getData不为空或者null
    public boolean containsKey(String key) {
        return !TextUtils.isEmpty(getData(key));
    }
    //约束getData不为空或者null
}
