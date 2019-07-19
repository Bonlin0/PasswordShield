package cn.adminzero.passwordshield_demo0;



import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private String username;
    private String password;
    private String website;
    private String note;
    private String name;
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent_FromListActivity=getIntent();
        name=intent_FromListActivity.getStringExtra("name");
        username=intent_FromListActivity.getStringExtra("account");
        password=intent_FromListActivity.getStringExtra("password");
        website=intent_FromListActivity.getStringExtra("uri");
        note=intent_FromListActivity.getStringExtra("note");
        setContentView(R.layout.activity_modify);
        final Button passwod_button = (Button) findViewById(R.id.password_display);
        final Button username_button =(Button)findViewById(R.id.username);
        Button website_button= (Button) findViewById(R.id.websit_display);
        Button note_button=(Button) findViewById(R.id.note_display);
        Button edit_button=(Button) findViewById(R.id.edit_account);
        Button back_button=(Button) findViewById(R.id.back_account);
        Button name_account=(Button) findViewById(R.id.account_name);


        note_button.setText(note);
        website_button.setText(website);
        passwod_button.setText("●●●●●●●●●●");
        username_button.setText(username);
        name_account.setText(name);

        //初始化剪切板管理器
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);


        passwod_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupMenu(passwod_button);
                PopupMenu popupMenu = new PopupMenu(ModifyActivity.this, passwod_button);
                // menu布局
                popupMenu.getMenuInflater().inflate(R.menu.password_display, popupMenu.getMenu());
                // menu的item点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.password_copy:
                                //复制到剪贴板
                                clipData = ClipData.newPlainText("password_saved",password);
                                clipboardManager.setPrimaryClip(clipData);
                                //Toast.makeText(ModifyActivity.this,"调用API把密码复制到剪切板",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.password_display:
                                // passwod_button.setText("123456789");
                                passwod_button.setText(password);
                                break;
                            case R.id.enlarge_display:
                               /* AlertDialog.Builder dialog=new AlertDialog.Builder(ModifyActivity.this);
                                dialog.setTitle("password");
                                dialog.setMessage(password);
                                dialog.show();
                                */
                                final Button enlarge_diplay=(Button) findViewById(R.id.enlarge_dispaly_text);
                                enlarge_diplay.setText(password);
                                Toast.makeText(ModifyActivity.this,
                                        "Please scroll down to see the Enlarged Password.",
                                        Toast.LENGTH_SHORT).show();
                                // enlarge_diplay.setBackgroundColor(R.color.enlarge_display);
                                /*点击一下取消*/
                                enlarge_diplay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        enlarge_diplay.setText("");
                                        //     enlarge_diplay.setBackgroundColor(R.color.touming);
                                    }
                                });

                                break;


                        }
                        //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
                // PopupMenu关闭事件
                    /*popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                popupMenu.show();
            }
        });
        edit_button.setOnClickListener(this);
        back_button.setOnClickListener(this);

    }
    @Override
    public  void  onClick (View v){
        switch (v.getId()) {
            case R.id.edit_account:
                Intent intent_ToEditActivity=new Intent(ModifyActivity.this,EditActivity.class);
//                String debugdata="debug_data_1111";
//                intent_ToEditActivity.putExtra("account","111111111111111111111");
//                intent_ToEditActivity.putExtra("account",debugdata);
                intent_ToEditActivity.putExtra("account",username);
                intent_ToEditActivity.putExtra("password",password);
                intent_ToEditActivity.putExtra("uri",website);
                intent_ToEditActivity.putExtra("note",note);
                intent_ToEditActivity.putExtra("name",name);
                Toast.makeText(ModifyActivity.this, "修改", Toast.LENGTH_SHORT).show();
                startActivity(intent_ToEditActivity);
                finish();
                break;
            case R.id.back_account:
                finish();
                break;
            default:
                break;

        }
    }

}

