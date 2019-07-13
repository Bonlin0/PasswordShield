package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//TODO Font color of Tool bar is black

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar_authentication);
        setSupportActionBar(toolbar);
    }

    public void onClickConfirm(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickExit(View view){
        finish();
    }

    //TODO 将使用人脸解锁的按钮方法改为正确的方法
    public void onClickUnlockWithFace(View view){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
        finish();
    }
    //TODO 将使用人脸解锁的按钮方法改为正确的方法
    public void onClickUnlockWithFingerPrint(View view){
        Intent intent = new Intent(this, fingerActivity.class);
        startActivity(intent);
        finish();
    }
}
