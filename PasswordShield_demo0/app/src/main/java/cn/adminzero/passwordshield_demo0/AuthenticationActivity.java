package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.adminzero.passwordshield_demo0.biometriclib.BiometricPromptManager;
import cn.adminzero.passwordshield_demo0.util.MyStorage;
import cn.adminzero.passwordshield_demo0.util.SHA256;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;

//TODO Font color of Tool bar is black

public class AuthenticationActivity extends AppCompatActivity {

    private BiometricPromptManager fingerprintManager;
    private Button unlockWithFingerprintButton;
    private Button unlockWithFaceButton;
    private EditText master_password_edit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar_authentication);
        setSupportActionBar(toolbar);
        //设置指纹识别相关
        unlockWithFingerprintButton = findViewById(R.id.unlock_with_fingerprint_button);
        unlockWithFaceButton = findViewById(R.id.unlock_with_face_button);
        if (isHardwareDetected() && hasEnrolledFingerprints()) {
            fingerprintManager = BiometricPromptManager.from(this);
        } else {
            fingerprintManager = null;
        }
        master_password_edit = findViewById(R.id.master_password_edit);

        //检查设置文件
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean switch_unlock_with_fingerprint_is_checked = sharedPreferences.getBoolean("switch_unlock_with_fingerprint"
                , false);
        boolean switch_unlock_with_face_is_checked = sharedPreferences.getBoolean("switch_unlock_with_face"
                , false);
        if(!switch_unlock_with_fingerprint_is_checked){
            unlockWithFingerprintButton.setVisibility(View.INVISIBLE);
        }
        if(!switch_unlock_with_face_is_checked){
            unlockWithFaceButton.setVisibility(View.INVISIBLE);
        }

    }


    public static boolean isMasterKey(String keyInput) {
        keyInput = SHA256.Sha512(keyInput);
        MyStorage myStorage = new MyStorage();
        if (myStorage.getData(MyApplication.isMaster).equals(keyInput)) {
            return true;
        }
        return false;
    }

    /**
     * Determine if there is at least one fingerprint enrolled.
     *
     * @return true if at least one fingerprint is enrolled, false otherwise
     */
    public boolean hasEnrolledFingerprints() {
        if (isAboveApi23()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            final FingerprintManager manager = this.getSystemService(FingerprintManager.class);
            return manager != null && manager.hasEnrolledFingerprints();
        } else {
            return false;
        }
    }

    /**
     * Determine if fingerprint hardware is present and functional.
     *
     * @return true if hardware is present and functional, false otherwise.
     */
    private boolean isHardwareDetected() {
        if (isAboveApi23()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            final FingerprintManager fm = this.getSystemService(FingerprintManager.class);
            return fm != null && fm.isHardwareDetected();
        } else {
            return false;
        }
    }

    private boolean isAboveApi28() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    private boolean isAboveApi23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }



    public void onClickConfirm(View view){


        EditText master_password_edit = findViewById(R.id.master_password_edit);
        String masterPasswordInput = master_password_edit.getText().toString();
        if(isMasterKey(masterPasswordInput)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText
                    (this, R.string.wrong_master_password,Toast.LENGTH_SHORT).show();
            master_password_edit.setText("");
        }

    }

    public void onClickExit(View view){
        finish();
    }

    public void onClickUnlockWithFace(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickUnlockWithFingerPrint(View view) {
        //指纹使用条件检测
        if (fingerprintManager == null) {
            if (!isAboveApi23()) {
                t("指纹API未达到23及以上");
            } else if (!isHardwareDetected()) {
                t("缺乏指纹硬件支持");
            } else {
                t("系统未注册指纹");
            }
            return;
        }

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
