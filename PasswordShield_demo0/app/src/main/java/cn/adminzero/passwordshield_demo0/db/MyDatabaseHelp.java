package cn.adminzero.passwordshield_demo0.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;


/**
 * https://github.com/amitshekhariitbhu/Android-Debug-Database
 * debugImplementation 'com.amitshekhar.android:debug-db:1.0.6'
 * debugImplementation 'com.amitshekhar.android:debug-db-encrypt:1.0.6'
 * adb forward tcp:8080 tcp:8080
 * http://localhost:8080 in emulator
 * <p>
 * SQLciper: https://blog.csdn.net/top_code/article/details/41178607
 * https://www.zetetic.net/sqlcipher/sqlcipher-for-android/    official document
 * The call to SQLiteDatabase.loadLibs(this) must occur before any other database operation.
 */
public class MyDatabaseHelp extends SQLiteOpenHelper {

    private static final String PasswordItem =
            "CREATE TABLE PasswordItem(" +
                    "   name              TEXT    NOT NULL," +
                    "   account           TEXT    NOT NULL," +
                    "   password          TEXT    NOT NULL," +
                    "   uri               TEXT    NOT NULL," +
                    "   type              INT     NOT NULL," +
                    "   note              TEXT            ," +
                    "constraint pk primary key (account,uri)"+
                    ");";

    private static final String ControledApp =
            "CREATE TABLE ControledApp(" +
                    "   lable          TEXT      NOT NULL," +
                    "   uri            TEXT      NOT NULL," +
                    "   image          BLOB              ," +
                    "   constraint pk  primary key(uri)   " +
                    ");";

    private Context mcontext;
    private String NAME;

    public MyDatabaseHelp(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        mcontext = context;
        NAME = name;
    }

    /**
     * 初始化创建数据表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建数据库 在数据库第一次被创建时调用
         * */
        db.beginTransaction();
        try {
            db.execSQL(PasswordItem);
            db.execSQL(ControledApp);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            /**数据库创建失败*/
            e.printStackTrace();
            t("数据表创建失败、回滚");
            d("数据表创建失败、回滚");
        } finally {
            db.endTransaction();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**
         * drop table if exists table_name;
         * */

        db.beginTransaction();
        try {
            t("更新数据库");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        onCreate(db);
    }


}