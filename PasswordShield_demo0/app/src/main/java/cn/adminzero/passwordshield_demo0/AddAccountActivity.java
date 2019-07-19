package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static cn.adminzero.passwordshield_demo0.db.DbUtil.AddAccount;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText account_text;
    private EditText password_text;
    private EditText website_text;
    private EditText note_text;
    private EditText name_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏  好像没用
        setContentView(R.layout.activity_add_account);
        Button cancel = (Button) findViewById(R.id.cancel);
        Button commit = (Button) findViewById(R.id.commit);
        Button creat_password = (Button) findViewById(R.id.creat_password);
        //
        name_text = (EditText) findViewById(R.id.account_name);
        account_text = (EditText) findViewById(R.id.account_username);
        password_text = (EditText) findViewById(R.id.password);
        website_text = (EditText) findViewById(R.id.website);
        note_text = (EditText) findViewById(R.id.note);
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

                int input_type = 1;
                AddAccount(input_account_name, input_account, input_password, input_type, input_website, input_note);
                finish();
                //    Toast.makeText(this, "账号" + input_account + "密码" + input_password + "网址" + input_website + "备注" + input_note, Toast.LENGTH_LONG).show();
                break;

            case R.id.cancel:
                finish();
                Toast.makeText(this, "取消添加账号信息", Toast.LENGTH_LONG).show();
                break;
            case R.id.creat_password:
                Intent intent = new Intent(AddAccountActivity.this, PasswordGeneratorActivity.class);
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
            default:

        }
    }
}
