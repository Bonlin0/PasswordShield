package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.adminzero.passwordshield_demo0.biometriclib.BiometricPromptManager;

//TODO Font color of Tool bar is black

public class AuthenticationActivity extends AppCompatActivity {

    private BiometricPromptManager fingerprintManager;
    private Button unlockWithFingerprintButton;
    private EditText master_password_edit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar_authentication);
        setSupportActionBar(toolbar);
        //设置指纹识别相关
        unlockWithFingerprintButton = findViewById(R.id.unlock_with_fingerprint_button);
        fingerprintManager = BiometricPromptManager.from(this);
        master_password_edit = findViewById(R.id.master_password_edit);

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

    public void onClickUnlockWithFingerPrint(View view) {
        if (fingerprintManager.isBiometricPromptEnable()) {
            fingerprintManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {
                    //使得密码输入框获得焦点
                    master_password_edit.requestFocus();
                    AuthenticationActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                //成功识别后
                @Override
                public void onSucceeded() {
                    Toast.makeText(AuthenticationActivity.this, "onSucceeded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailed() {
                    //验证失败后弹出提示
                    Toast.makeText(AuthenticationActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(int code, String reason) {
                    //验证出现错误时弹出提示
                    Toast.makeText(AuthenticationActivity.this, "onError", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    //用户取消
                }
            });
        }

    }

}
