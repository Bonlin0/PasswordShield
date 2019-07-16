package cn.adminzero.passwordshield_demo0;

import android.app.Application;
import android.content.Context;

import cn.adminzero.passwordshield_demo0.db.DbUtil;
import cn.adminzero.passwordshield_demo0.util.MyKeyStore;
import cn.adminzero.passwordshield_demo0.util.MyStorage;
import cn.adminzero.passwordshield_demo0.util.SHA256;

import static cn.adminzero.passwordshield_demo0.test.Test.testPre;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;

public class MyApplication extends Application {

    private static Context context;
    private static int dbVersion = 1;
    private static String dbName = "PasswordSheild";
    public final static String isFirst = "isFirst";  //判断是否第一次安装
    public static String FUCK_DB = "FUCK_DB";
    public final static String KEY = "KEY";          //存储秘钥信息(经加密)
    public final static String ALIAS = "PasswordSheild";
    public final static String isMaster = "isMaster";

    public static Boolean isFirstLogin=true;



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
        String keyEnc = myStorage.getData(KEY);
        String DB_FIRST = myStorage.getData(FUCK_DB);

//        myStorage.storeData(isFirst, "");
//        myStorage.storeData(isFirst,"erw")
        String initial = myStorage.getData(isFirst);
        if ("".equals(initial)) {
            //进入初始化函数
//            initApp();
            isFirstLogin=true;
            t(String.valueOf(isFirstLogin));
            d(String.valueOf(isFirstLogin));
        }
        else{
            isFirstLogin = false;
            d(String.valueOf(isFirstLogin));
        }

        if ("".equals(DB_FIRST)) {
            DbUtil.deleteDatabase();
//            DbUtil.fuck_database();
        }
        //恢复初始化状态
        //myStorage.storeData(isFirst, "");
        d("进入主函数");

    }


}
