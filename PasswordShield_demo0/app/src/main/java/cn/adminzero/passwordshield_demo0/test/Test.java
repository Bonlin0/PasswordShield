package cn.adminzero.passwordshield_demo0.test;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.adminzero.passwordshield_demo0.db.DbUtil;
import cn.adminzero.passwordshield_demo0.util.SHA256;

import static cn.adminzero.passwordshield_demo0.db.DbUtil.getDatabase;
import static cn.adminzero.passwordshield_demo0.util.AESUtils.aesDecodeStr;
import static cn.adminzero.passwordshield_demo0.util.AESUtils.aesEncryptStr;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;


/**
 * 测试类
 */
public class Test {
    public static void testDb() {
        d("进入数据库函数模块");
        DbUtil.deleteDatabase();
        DbUtil.deleteDatabase("zjc_info");
        /**
         * 初始化db,不会创建数据库*/
        SQLiteDatabase db = getDatabase();
//        SQLiteDatabase db1 = getMyDatabaseHelp("zjc_info");
        if (db == null) {
            d("数据失败");
            return;
        }
        try {
//            db.execSQL("create table if not exists zjc_info(id integer primary key autoincrement, name text);");
//            db1.execSQL("create table if not exists zjc_info(id integer primary key autoincrement, name text);");
        } catch (Exception e) {
            d("捕获到异常");
            e.printStackTrace();
        }

        /**
         * 打开一个写的数据库
         * 当这个数据库未被创建时
         * 会调用数据库的onCreate()方法*/

        if (db != null) {
            db.close();
        }

        d("是否被创建数据库");
        d("退出数据库模块");
    }

    public static void testAES() {
        d("sha256函数");
        String pkey = SHA256.Sha256("Liujiao986812982");
        d("进入AES调试函数");
        //明文
        String content = "qq245635595";
        d("待加密报文:" + content);
        d("密匙:" + pkey);
        String aesEncryptStr = aesEncryptStr(content, pkey);
        d("加密报文:" + aesEncryptStr);
        String aesDecodeStr = aesDecodeStr(aesEncryptStr, pkey);
        d("解密报文:" + aesDecodeStr);
        d("加解密前后内容是否相等:" + aesDecodeStr.equals(content));
        d("AES函数退出");
    }



    //测试基本sharePreference
    public static void testPre(){

    }


}
