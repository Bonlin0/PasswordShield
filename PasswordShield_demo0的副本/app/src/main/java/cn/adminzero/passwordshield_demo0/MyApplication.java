package cn.adminzero.passwordshield_demo0;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;
    private static int dbVersion = 1;
    private static String dbName = "PasswordSheild";


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //LitePal.initialize(context);

    }

    public static int getDbVersion() {
        return dbVersion;
    }

    public static String getDbName() {
        return dbName;
    }
}
