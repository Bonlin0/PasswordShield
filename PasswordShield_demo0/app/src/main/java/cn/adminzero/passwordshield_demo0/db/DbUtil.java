package cn.adminzero.passwordshield_demo0.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.passwordshield_demo0.Encryption;
import cn.adminzero.passwordshield_demo0.MyApplication;
import cn.adminzero.passwordshield_demo0.entity.PasswordItem;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;

/**
 * 经过测试通过
 */
public class DbUtil {
    //cursor getInt()  当为null 返回0
    public static int TYTE_APP = 1;
    public static int TYPE_WEB = 2;


    /**
     * 打开数据库,返回指定数据库SQLiteDatabase
     */
    public static SQLiteDatabase getDatabase() {
        MyDatabaseHelp dbhelper = new MyDatabaseHelp(MyApplication.getContext(), MyApplication.getDbName(), null, MyApplication.getDbVersion());
        try {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            d("数据库打开失败");
        }

        return null;
    }

    /**
     * 打开数据库,返回dbName数据库SQLiteDatabase
     */
    public static SQLiteDatabase getDatabase(String dbName) {
        MyDatabaseHelp dbhelper = new MyDatabaseHelp(MyApplication.getContext(), dbName, null, MyApplication.getDbVersion());
        try {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            d("数据库打开失败");
        }

        return null;
    }

    /**
     * 数据库删除函数(删除默认的数据库)
     */
    public static boolean deleteDatabase() {
        return MyApplication.getContext().deleteDatabase(MyApplication.getDbName());
    }

    /**
     * 数据库删除函数(删除指定名称的数据库)
     */
    public static boolean deleteDatabase(String dbName) {
        return MyApplication.getContext().deleteDatabase(dbName);
    }

    /**
     * 添加账户函数 提前需检查
     * 数据合法性,  防止崩溃
     */
    public static boolean AddAccount(String account, String password, int type, String uri, String note) {

        SQLiteDatabase db = getDatabase();
        password = Encryption.encode(password);

        if (TextUtils.isEmpty(note)) {
            note = "none";
        }
        try {
            db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)",
                    new String[]{account, password, String.valueOf(type), uri, note}
            );
            return true;
        } catch (Exception e) {
            d("数据表PasswordItem插入失败");
            t("数据表PasswordItem插入失败");
        }

        return false;

    }

    /**
     * 读取所有的数据进入ListView
     * 初始化 passwordItem数据
     */

    public static List<PasswordItem> initPassword() {
        List<PasswordItem> passwordItemList = new ArrayList<PasswordItem>();
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem ORDER BY type;", null);

        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(cursor.getInt(0));
                passwordItem.setAccount(cursor.getString(1));
                passwordItem.setPassword(cursor.getString(2));
                passwordItem.setType(cursor.getInt(3));
                passwordItem.setUri(cursor.getString(4));
                passwordItem.setNote(cursor.getString(5));
                passwordItemList.add(passwordItem);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return passwordItemList;

    }

    /**
     * 根据uri筛选数据
     */
    public static List<PasswordItem> getPasswordItemByUri(String uri) {
        List<PasswordItem> passwordItemList = new ArrayList<PasswordItem>();
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem WHERE uri = ? ;", new String[]{uri});

        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(cursor.getInt(0));
                passwordItem.setAccount(cursor.getString(1));
                passwordItem.setPassword(cursor.getString(2));
                passwordItem.setType(cursor.getInt(3));
                passwordItem.setUri(cursor.getString(4));
                passwordItem.setNote(cursor.getString(5));
                passwordItemList.add(passwordItem);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return passwordItemList;
    }

    /**
     * 删除函数
     * */


}

