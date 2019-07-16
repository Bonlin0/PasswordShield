package cn.adminzero.passwordshield_demo0;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import cn.adminzero.passwordshield_demo0.util.MyKeyStore;
import cn.adminzero.passwordshield_demo0.util.MyStorage;
import cn.adminzero.passwordshield_demo0.util.SHA256;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;

public class MyApplication extends Application {

    private static Context context;
    private static int dbVersion = 1;
    private static String dbName = "PasswordSheild";
    private static String isFirst = "isFirst";  //判断是否第一次安装
    private static String KEY = "KEY";          //存储秘钥信息(经加密)
    public static String ALIAS = "PasswordSheild";
    public static Boolean isFirstLogin = false;

    private MyStorage myStorage;


    public static Context getContext() {
        return context;
    }

    public static int getDbVersion() {
        return dbVersion;
    }

    public static String getDbName() {
        return dbName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //LitePal.initialize(context);
        myStorage = new MyStorage();
        String initial = myStorage.getData(isFirst);
        if ("".equals(initial)) {
            //进入初始化函数
            initApp();
            // debug line
            // isFirstLogin=true;
        }
        //恢复初始化状态 status
        myStorage.storeData(isFirst, "");
//        myStorage.storeData(isFirst, "no");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor sharedPreferenceEditor;
        sharedPreferenceEditor = sharedPreferences.edit();
        if(!sharedPreferences.contains("isUserCreated")){
            //如果没有找到该preference ， 先预设为false
            sharedPreferenceEditor.putBoolean("isUserCreated",true);
            sharedPreferenceEditor.apply();
        }
        else{
            isFirstLogin = sharedPreferences.getBoolean("switch_unlock_with_fingerprint"
                    , false);
        }

    }


    private void initApp() {
        //进入初始化界面Intent
        String pKey = "zhaojunchen";//主秘钥
        pKey = SHA256.Sha256(pKey);
        myStorage.storeData(KEY, MyKeyStore.encryptKey(MyApplication.ALIAS, pKey));
        String cunchuKey = myStorage.getData(KEY);
        d(cunchuKey);
        String cunchuisFirst = myStorage.getData(isFirst);
        d(cunchuisFirst);

        myStorage.storeData(isFirst, "");
        cunchuisFirst = myStorage.getData(isFirst);
        d(cunchuisFirst);

        d("------------------------------");
        String getFromPre = myStorage.getData(KEY);
        String decode = MyKeyStore.decryptKey(MyApplication.ALIAS, cunchuKey);



    }
}
