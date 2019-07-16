package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.adminzero.passwordshield_demo0.util.MyKeyStore;
import cn.adminzero.passwordshield_demo0.util.MyStorage;
import cn.adminzero.passwordshield_demo0.util.SHA256;

public class CreateUserActivity extends AppCompatActivity {

    EditText masterPasswordEdit;

    //TODO 确认主密码Edit Text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = findViewById(R.id.toolbar_create_user);
        setSupportActionBar(toolbar);


    }

    public void onClickConfirm(View view){
        masterPasswordEdit = findViewById(R.id.master_password_create_edit);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor sharedPreferenceEditor;
//        sharedPreferenceEditor = sharedPreferences.edit();
//        sharedPreferenceEditor.putBoolean("isUserCreated",true);
//        sharedPreferenceEditor.apply();

        String pKey  = masterPasswordEdit.getText().toString();
        //TODO  检测合法性
        if(pKey.length()==0) {
            Toast.makeText(CreateUserActivity.this, R.string.password_is_not_valid ,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        initKey(pKey);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void initKey(String pKey) {
        //进入初始化界面Intent
        MyStorage myStorage = new MyStorage();
        pKey = SHA256.Sha256(pKey);
        myStorage.storeData(MyApplication.KEY, MyKeyStore.encryptKey(pKey));
        String cunchuKey = myStorage.getData(MyApplication.KEY);
        myStorage.storeData("isFirst", "NOT_FIRST");

    }

    public void onClickExit(View view){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor sharedPreferenceEditor;
//        sharedPreferenceEditor = sharedPreferences.edit();
//        sharedPreferenceEditor.putBoolean("isUserCreated",false);
//        sharedPreferenceEditor.apply();
        finish();
    }
}
