package cn.adminzero.passwordshield_demo0.test;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.adminzero.passwordshield_demo0.EncryUtils;
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

    public static void testRSA() {
        String alisa = "PasswordSheild_zjc";
        String zjc1 = "qwewqeqweqweqw`12121212e";
        String zjc2 = "qwewqeqweqwewqe123213wq";
        String zjc3 = "as3453ndjadjaskdjkad";
        String zjc4 = "asdnsakjnd657sakjdnsajndkjasn";
        String zjc1_ciper = EncryUtils.getInstance().encryptString(zjc1, alisa);
        String zjc2_ciper = EncryUtils.getInstance().encryptString(zjc2, alisa);
        String zjc3_ciper = EncryUtils.getInstance().encryptString(zjc3, alisa);
        String zjc4_ciper = EncryUtils.getInstance().encryptString(zjc4, alisa);

        Log.d("de", zjc1 + "enc-->" + zjc1_ciper);
        Log.d("de", zjc2 + "enc-->" + zjc2_ciper);
        Log.d("de", zjc3 + "enc-->" + zjc3_ciper);
        Log.d("de", zjc4 + "enc-->" + zjc4_ciper);

        String zjc1_plainText = EncryUtils.getInstance().decryptString(zjc1_ciper, alisa);
        String zjc2_plainText = EncryUtils.getInstance().decryptString(zjc2_ciper, alisa);
        String zjc3_plainText = EncryUtils.getInstance().decryptString(zjc3_ciper, alisa);
        String zjc4_plainText = EncryUtils.getInstance().decryptString(zjc4_ciper, alisa);

        Log.d("de", zjc1_ciper + "enc-->" + zjc1);
        Log.d("de", zjc2_ciper + "enc-->" + zjc2);
        Log.d("de", zjc3_ciper + "enc-->" + zjc3);
        Log.d("de", zjc4_ciper + "enc-->" + zjc4);

    }

    //测试基本sharePreference
    public static void testPre(){

    }


}
