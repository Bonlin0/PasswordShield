package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateUserActivity extends AppCompatActivity {

    //TODO 确认主密码Edit Text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = findViewById(R.id.toolbar_create_user);
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
}
