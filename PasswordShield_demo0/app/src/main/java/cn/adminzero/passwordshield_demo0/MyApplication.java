package cn.adminzero.passwordshield_demo0;

import android.app.Application;
import android.content.Context;

import cn.adminzero.passwordshield_demo0.util.MyStorage;
import cn.adminzero.passwordshield_demo0.util.SHA256;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;

public class MyApplication extends Application {

    private static Context context;
    private static int dbVersion = 1;
    private static String dbName = "PasswordSheild";
    private static String isFirst = "isFirst";  //判断是否第一次安装
    private static String KEY = "KEY";          //存储秘钥信息(经加密)


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
        }
        //恢复初始化状态
        //myStorage.storeData(isFirst, "");


    }


    private void initApp() {
        //进入初始化界面Intent
        String pKey = "zhaojunchen";//主秘钥
        pKey = SHA256.Sha256(pKey);
        myStorage.storeData(KEY, EncryUtils.getInstance().encryptString(pKey, getDbName()));
        //String cunchuKey = myStorage.getData(KEY);
        //d(cunchuKey);
        //String cunchuisFirst = myStorage.getData(isFirst);
        //d(cunchuisFirst);

        myStorage.storeData(isFirst, "Not_First");
        //cunchuisFirst = myStorage.getData(isFirst);
        //d(cunchuisFirst);

        //d("------------------------------");
        //String getFromPre = myStorage.getData(KEY);
        //String decode = EncryUtils.getInstance().decryptString(getFromPre, getDbName());

    }
}
