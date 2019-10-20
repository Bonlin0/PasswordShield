package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static cn.adminzero.passwordshield_demo0.db.DbUtil.AddAccount;
import static cn.adminzero.passwordshield_demo0.db.DbUtil.deletePasswordItem;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText account_text;
    private EditText password_text;
    private EditText website_text;
    private EditText note_text;
    private EditText name_text;
    String account_key;
    String website_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏  好像没用

        setContentView(R.layout.edit_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        Button cancel = (Button) findViewById(R.id.cancel);
        Button commit = (Button) findViewById(R.id.commit);
        Button creat_password = (Button) findViewById(R.id.creat_password);
        //
        account_text = (EditText) findViewById(R.id.account_username);
        password_text = (EditText) findViewById(R.id.password);
        website_text = (EditText) findViewById(R.id.website);
        note_text = (EditText) findViewById(R.id.note);
        name_text = (EditText) findViewById(R.id.account_name);


        Intent intent = getIntent();
        //    account_text.setText("11111");//debug 使用
        account_text.setText(intent.getStringExtra("account"));
        password_text.setText(intent.getStringExtra("password"));
        website_text.setText(intent.getStringExtra("uri"));
        note_text.setText(intent.getStringExtra("note"));
        name_text.setText(intent.getStringExtra("name"));
        account_key = intent.getStringExtra("account");
        website_key = intent.getStringExtra("uri");


        cancel.setOnClickListener(this);
        commit.setOnClickListener(this);
        creat_password.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                String input_account_name = name_text.getText().toString();
                if ("".equals(input_account_name)) {
                    Toast.makeText(this, "账户类型不能为空", Toast.LENGTH_LONG).show();
                    break;
                }
                String input_account = account_text.getText().toString();
                if ("".equals(input_account)) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    break;
                }
                String input_password = password_text.getText().toString();
                if ("".equals(input_password)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
                    break;
                }
                String input_website = website_text.getText().toString();
                if ("".equals(input_website)) {
                    Toast.makeText(this, "网址不能为空，QQ可以输入www.qq.com,微信为www.wx.qq.com", Toast.LENGTH_LONG).show();
                    break;
                }

                String input_note = note_text.getText().toString();
                //删除原来的列表
                deletePasswordItem(account_key, website_key);
                // TODO 判断是否为二次加密
                boolean isImportant =  false;
                //加入新的
                AddAccount(input_account_name, input_account, input_password, 1, input_website, input_note,isImportant);
                Toast.makeText(this, "已保存：" + "账号" + input_account + "密码" + input_password + "网址" + input_website + "备注" + input_note, Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.cancel:
                finish();
                Toast.makeText(this, "取消添加账号信息", Toast.LENGTH_LONG).show();
                break;
            case R.id.creat_password:
                Intent intent = new Intent(EditActivity.this, PasswordGeneratorActivity.class);
                intent.putExtra("from_add_account", true);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    password_text.setText(data.getStringExtra("data_return"));

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    website_text.setText(data.getStringExtra("uri_return"));

                }
                break;
            default:

        }
    }

    public void onClickFindPackageNameButton(View view){
        Intent installedAppIntent = new Intent(this, InstalledAppAcitivity.class);
        startActivityForResult(installedAppIntent,2);
    }

}
