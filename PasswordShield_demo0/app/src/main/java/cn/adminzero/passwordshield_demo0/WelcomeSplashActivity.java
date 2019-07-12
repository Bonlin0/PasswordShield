package cn.adminzero.passwordshield_demo0;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import static java.lang.Thread.sleep;

public class WelcomeSplashActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeSplash_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 进入了onCreate");
        setContentView(R.layout.activity_welcome_splash);
        //延时两秒
        Log.d(TAG, "onCreate: 设置了view");
     /*   try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "延时完成");
        */
        Intent intent=new Intent(WelcomeSplashActivity.this,AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

}
