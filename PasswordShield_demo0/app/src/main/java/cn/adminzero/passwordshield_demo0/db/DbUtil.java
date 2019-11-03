package cn.adminzero.passwordshield_demo0.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.adminzero.passwordshield_demo0.MyApplication;
import cn.adminzero.passwordshield_demo0.R;
import cn.adminzero.passwordshield_demo0.entity.ControledApp;
import cn.adminzero.passwordshield_demo0.entity.PasswordItem;
import cn.adminzero.passwordshield_demo0.util.Encryption;
import cn.adminzero.passwordshield_demo0.util.MyStorage;

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
    public static boolean AddAccount(String name, String account, String password, int type, String uri, String note, boolean isImportant) {

        SQLiteDatabase db = getDatabase();
        password = new Encryption().encode(password);

        if (TextUtils.isEmpty(note)) {
            note = "none";
        }
        int temp = 0;
        if (isImportant) {
            temp = 1;
        }
        try {
            db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note, recordtime, isImportant) values (?,?,?,?,?,?,?,?)",
                    new String[]{name, account, password, String.valueOf(type), uri, note, MyApplication.getnowDate(), String.valueOf(temp)}
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

        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem ORDER BY type, uri;", null);
        Encryption encryption = new Encryption();
        String ciperText;
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(id);
                passwordItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                passwordItem.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                /*-------------------------------------------*/
                //解密password
                /*-------------------------------------------*/
                ciperText = cursor.getString(cursor.getColumnIndex("password"));
                passwordItem.setPassword(encryption.decode(ciperText));
                passwordItem.setType(cursor.getInt(cursor.getColumnIndex("type")));
                passwordItem.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                passwordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                passwordItem.setTime(cursor.getString(cursor.getColumnIndex("recordtime")));
                if (cursor.getInt(cursor.getColumnIndex("isImportant")) == 0)
                    passwordItem.setImportant(false);
                else
                    passwordItem.setImportant(true);
                Log.d("test", "Date" + MyApplication.datediffer(passwordItem.getTime()));
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


    //返回不重要的
    public static List<PasswordItem> initplainPasswordListView() {
        List<PasswordItem> passwordItemList = new ArrayList<PasswordItem>();
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem ORDER BY type, uri;", null);
        Encryption encryption = new Encryption();
        String ciperText;
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(id);
                passwordItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                passwordItem.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                /*-------------------------------------------*/
                //解密password
                /*-------------------------------------------*/
                ciperText = cursor.getString(cursor.getColumnIndex("password"));
                passwordItem.setPassword(encryption.decode(ciperText));
                passwordItem.setType(cursor.getInt(cursor.getColumnIndex("type")));
                passwordItem.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                passwordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                passwordItem.setTime(cursor.getString(cursor.getColumnIndex("recordtime")));
                if (cursor.getInt(cursor.getColumnIndex("isImportant")) == 0)
                    passwordItem.setImportant(false);
                else
                    passwordItem.setImportant(true);
                Log.d("test", "Date" + MyApplication.datediffer(passwordItem.getTime()));

                if(passwordItem.getImport()==false)
                {
                    passwordItemList.add(passwordItem);
                    id++;
                }

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


    //返回不重要的
    public static List<PasswordItem> initimportantPasswordListView() {
        List<PasswordItem> passwordItemList = new ArrayList<PasswordItem>();
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem ORDER BY type, uri;", null);
        Encryption encryption = new Encryption();
        String ciperText;
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(id);
                passwordItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                passwordItem.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                /*-------------------------------------------*/
                //解密password
                /*-------------------------------------------*/
                ciperText = cursor.getString(cursor.getColumnIndex("password"));
                passwordItem.setPassword(encryption.decode(ciperText));
                passwordItem.setType(cursor.getInt(cursor.getColumnIndex("type")));
                passwordItem.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                passwordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                passwordItem.setTime(cursor.getString(cursor.getColumnIndex("recordtime")));
                if (cursor.getInt(cursor.getColumnIndex("isImportant")) == 0)
                    passwordItem.setImportant(false);
                else
                    passwordItem.setImportant(true);
                Log.d("test", "Date" + MyApplication.datediffer(passwordItem.getTime()));

                if(passwordItem.getImport()==true)
                {
                    passwordItemList.add(passwordItem);
                    id++;
                }

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
     * 根据uri筛选数据  TODO
     */
    public static List<PasswordItem> getPasswordItemByUri(String uri) {
        List<PasswordItem> passwordItemList = new ArrayList<PasswordItem>();
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PasswordItem WHERE uri = ? order by uri;", new String[]{uri});
        Encryption encryption = new Encryption();
        String ciperText;
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                PasswordItem passwordItem = new PasswordItem();
                passwordItem.setId(id);
                passwordItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                passwordItem.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                /*-------------------------------------------*/
                //解密password
                /*-------------------------------------------*/
                ciperText = cursor.getString(cursor.getColumnIndex("password"));
                passwordItem.setPassword(encryption.decode(ciperText));
                passwordItem.setType(cursor.getInt(cursor.getColumnIndex("type")));
                passwordItem.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                passwordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                passwordItem.setTime(cursor.getString(cursor.getColumnIndex("recordtime")));
                if (cursor.getInt(cursor.getColumnIndex("isImportant")) == 0)
                    passwordItem.setImportant(false);
                else
                    passwordItem.setImportant(true);
                Log.d("test", "Date" + MyApplication.datediffer(passwordItem.getTime()));
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

    //伪造（初始化）数据库
    public static void init_database() {
        Encryption encryption = new Encryption();

        SQLiteDatabase db = getDatabase();
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"Telephone", "13778791018", encryption.encode("12345678"), String.valueOf(1), "com.android.providers.telephony", "手机默认", MyApplication.getnowDate(), "0"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"网易云音乐", "12345678@163.com", encryption.encode("82unuf32ny3yd"), String.valueOf(1), "com.netease.cloudmusic", "网易云", MyApplication.getnowDate(), "1"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"支付宝", "98326287@qq.com", encryption.encode("woaini111"), String.valueOf(1), "com.eg.android.AlipayGphone", "我的支付宝", MyApplication.getnowDate(), "1"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"QQ", "4865238221", encryption.encode("123qwe"), String.valueOf(1), "com.tencent.mobileqq", "QQ大号", MyApplication.getnowDate(), "1"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"Photos", "17702737629", encryption.encode("321www.."), String.valueOf(1), "com.google.android.videos", "相册", MyApplication.getnowDate(), "0"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"淘宝", "1372623452", encryption.encode("123rqw."), String.valueOf(1), "com.taobao.taobao", "剁剁剁", "20181011", "0"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"微信", "Hecate_sairen", encryption.encode("asdvxzzxv."), String.valueOf(1), "com.tencent.mm", "微信", MyApplication.getnowDate(), "0"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"百度云盘", "327865492@qq.com", encryption.encode("sgdsgsAC."), String.valueOf(1), "com.baidu.netdisk", "百度云资料", MyApplication.getnowDate(), "0"});
        db.execSQL("INSERT INTO PasswordItem (name, account, password, type, uri, note,recordtime,isImportant) values (?,?,?,?,?,?,?,?)", new String[]{"京东", "jingdong@gmail.com", encryption.encode("GVDSAGsfsd."), String.valueOf(1), "com.jingdong.app.mall", "买买买", MyApplication.getnowDate(), "0"});

    }


    public static ControledApp fetchAppInfo(String uri) {
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery("select * from ControledApp where uri = ? ;", new String[]{uri});

        Drawable none = MyApplication.getContext().getResources().getDrawable(R.drawable.key);
        ControledApp data = new ControledApp();
        byte[] bytes;

        try {
            if (cursor.moveToFirst()) {
                do {
                    bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                    if (bytes != null && bytes.length != 0) {
                        data.setIcon(bitmapToDrawable(bytesToBitmap(bytes)));
                    } else {
                        data.setIcon(none);
                    }

                    data.setLable(cursor.getString(cursor.getColumnIndex("lable")));
                    data.setPackageName(cursor.getString(cursor.getColumnIndex("uri")));
                    return data;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            data.setIcon(none);
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }

        }
        data.setIcon(none);
        return data;
    }


    public static List<ControledApp> fetchAllAppInfo() {
        SQLiteDatabase db = getDatabase();
        List<ControledApp> dataList = new ArrayList<ControledApp>();

        Cursor cursor = db.rawQuery("select * from ControledApp;", null);
        byte[] bytes;

        try {
            if (cursor.moveToFirst()) {
                do {
                    ControledApp data = new ControledApp();
                    bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                    if (bytes != null && bytes.length != 0) {
                        data.setIcon(bitmapToDrawable(bytesToBitmap(bytes)));
                    } else {
                        data.setIcon(null);
                    }

                    data.setLable(cursor.getString(cursor.getColumnIndex("lable")));
                    data.setPackageName(cursor.getString(cursor.getColumnIndex("uri")));
                    dataList.add(data);
                } while (cursor.moveToNext());
            }
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }

        }
        return null;
    }

    public static void storeAppInfo() {
        //  获取终端上安装的应用程序列表信息
        final PackageManager pm = MyApplication.getContext().getPackageManager();
        final int flags = PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS;
        @SuppressLint("WrongConstant") final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(flags);
        int count = installedAppList.size();
        MyStorage myStorage = new MyStorage();
        if (myStorage.getData("installedAppListCount").equals(String.valueOf(count))) {
            return;
        }

        // 将列表数据存储在列表中
        SQLiteDatabase db = getDatabase();
        db.execSQL("delete from ControledApp;");
        Drawable drawable;
        for (ApplicationInfo app : installedAppList) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("lable", app.loadLabel(pm).toString());
            contentValues.put("uri", app.packageName);

            drawable = app.loadIcon(pm);
            if (drawable != null) {
                contentValues.put("image", bitmapTobytes(drawableToBitmap(drawable)));
            } else {
                contentValues.put("image", "");
            }
            db.insert("ControledApp", null, contentValues);
            contentValues.clear();
        }
        if (db != null) {
            db.close();
        }

        myStorage.storeData("installedAppListCount", String.valueOf(count));

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
//        System.out.println("Drawable转Bitmap");
        try {
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null)
            return new BitmapDrawable(MyApplication.getContext().getResources(), bitmap);
        return null;
    }


    /**
     * byte串转换为Bitmap
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    public static byte[] bitmapTobytes(Bitmap bm) {
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

}

