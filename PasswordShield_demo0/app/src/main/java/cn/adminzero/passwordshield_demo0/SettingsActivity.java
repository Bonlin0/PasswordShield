package cn.adminzero.passwordshield_demo0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import cn.adminzero.passwordshield_demo0.biometriclib.BiometricPromptManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch unlock_with_fingerprint_switch_preference;
    private BiometricPromptManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //使用指纹识别管理器
//        fingerprintManager = BiometricPromptManager.from(this);
        //查找switch控件
//        unlock_with_fingerprint_switch_preference = findViewById
//                (R.id.unlock_with_fingerprint_switch_preference);
//        if (unlock_with_fingerprint_switch_preference == null) {
//            Toast.makeText(SettingsActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
//            return;
//        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}

    /*
    private class onChangeUnlockWithFingerPrintListener implements PreferenceManager.OnPreferenceTreeClickListener {
        public boolean onPreferenceTreeClick(Preference preference){

            if(unlockWithFingerprintSwitch.isChecked()){
                Toast.makeText(SettingsActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
            }

            if (fingerprintManager.isBiometricPromptEnable()) {
                fingerprintManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                    @Override
                    public void onUsePassword() {

                    }
                    //成功识别后
                    @Override
                    public void onSucceeded() {
                        unlockWithFingerprintSwitch.setChecked(true);
                    }

                    @Override
                    public void onFailed() {
                        //验证失败后弹出提示
                        Toast.makeText(SettingsActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, String reason) {
                        //验证出现错误时弹出提示
                        Toast.makeText(SettingsActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        //用户取消
                    }

                });
            }
        }
    }
*/


/*
    public void onChangeUnlockWithFingerPrint(final View view){
        final Switch unlockWithFingerprintSwitch = (Switch)view;
        if(unlockWithFingerprintSwitch.isChecked()){
            Toast.makeText(SettingsActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
        }

        if (fingerprintManager.isBiometricPromptEnable()) {
            fingerprintManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {

                }
                //成功识别后
                @Override
                public void onSucceeded() {
                    unlockWithFingerprintSwitch.setChecked(true);
                }

                @Override
                public void onFailed() {
                    //验证失败后弹出提示
                    Toast.makeText(SettingsActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(int code, String reason) {
                    //验证出现错误时弹出提示
                    Toast.makeText(SettingsActivity.this, "onError", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    //用户取消
                }

            });
        }
    }
}

*/