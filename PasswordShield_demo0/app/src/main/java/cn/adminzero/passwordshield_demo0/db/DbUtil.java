package cn.adminzero.passwordshield_demo0.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.passwordshield_demo0.MyApplication;
import cn.adminzero.passwordshield_demo0.entity.PasswordItem;
import cn.adminzero.passwordshield_demo0.util.Encryption;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;


/**
 * 开发者调用API接口
 * 1.SQLiteDatabase getDatabase();
 * 打开默认的数据库,返回SQLiteDatabase句柄
 * 2.public static boolean AddAccount(String account, String password, int type, String uri, String note);
 * 添加信息到数据库
 * 3.public static List<PasswordItem> initPasswordListView();
 * 查询数据库所有数据 返回数据集合List给适配器
 * 4.public static List<PasswordItem> getPasswordItemByUri(String uri);
 * 填充查询根据程序的包名或者域名返回秘码列表
 */


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
        password = new Encryption().encode(password);

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

    public static List<PasswordItem> initPasswordListView() {
        List<PasswordItem> passwordItemList = new ArrayList<PasswordItem>();
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem ORDER BY type;", null);
        Encryption encryption = new Encryption();
        String ciperText;
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(id);
                passwordItem.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                /*-------------------------------------------*/
                //解密password
                /*-------------------------------------------*/
                ciperText = cursor.getString(cursor.getColumnIndex("password"));
                passwordItem.setPassword(encryption.decode(ciperText));
                passwordItem.setType(cursor.getInt(cursor.getColumnIndex("type")));
                passwordItem.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                passwordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                passwordItemList.add(passwordItem);
                id++;
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        if (db != null) {
            db.close();
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
        Encryption encryption = new Encryption();
        String ciperText;
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(id);
                passwordItem.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                /*-------------------------------------------*/
                //解密password
                /*-------------------------------------------*/
                ciperText = cursor.getString(cursor.getColumnIndex("password"));
                passwordItem.setPassword(encryption.decode(ciperText));
                passwordItem.setType(cursor.getInt(cursor.getColumnIndex("type")));
                passwordItem.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                passwordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                passwordItemList.add(passwordItem);
                id++;
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return passwordItemList;
    }

    /**
     * 从数据库删除条目
     * 输入:账户名 account
     * 输入:路径名 uri
     * 输出 : 成功与否
     */
    public static boolean deletePasswordItem(String account, String uri) {
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("DELETE FROM PasswordItem WHERE account = ? AND uri = ?;", new String[]{account, uri});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            d(e.getMessage());
        }
        return false;
    }

    /**
     * 插入数据库
     */

    //伪造数据库
    public static void fuck_database() {
        Encryption encryption = new Encryption();

        SQLiteDatabase db = getDatabase();
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"百度", encryption.encode("zjc"), String.valueOf(1), "www.baidu.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"jd", encryption.encode("zjc"), String.valueOf(1), "www.jd.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"qq", encryption.encode("zjcadasdasdasd"), String.valueOf(1), "www.qq.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"taobao", encryption.encode("zjcadasdasdasd"), String.valueOf(1), "www.taobao.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"sina", encryption.encode("zjcadasdasdasd"), String.valueOf(1), "www.sina.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"sohu", encryption.encode("zjcadasdasdasd"), String.valueOf(1), "www.sohu.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"csdn", encryption.encode("zjcadasdasdasd"), String.valueOf(1), "www.csdn.com", "1号备注"});
        db.execSQL("INSERT INTO PasswordItem (account, password, type, uri, note) values (?,?,?,?,?)", new String[]{"163", encryption.encode("zjcadasdasdasd"), String.valueOf(1), "www.163.com", "1号备注"});


    }


}

