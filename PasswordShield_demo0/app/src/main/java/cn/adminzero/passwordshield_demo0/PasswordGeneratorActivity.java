package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// TODO: 2019/7/10 seekbar黑色条取消

public class PasswordGeneratorActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView length_number_text;
    private TextView password_generated;
    private Switch password_AZ_switch;
    private Switch password_az_lower_switch;
    private Switch password_numbers_switch;
    private Switch password_punctuation_switch;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    private String password_generated_string = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);
        Toolbar toolbar = findViewById(R.id.toolbar_password_generator);
        setSupportActionBar(toolbar);
        Button return_password=findViewById(R.id.return_password);
        return_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PasswordGeneratorActivity.this,AddAccountActivity.class);
                intent.putExtra("data_return",password_generated_string);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        seekBar = findViewById(R.id.seekBar);
        length_number_text = findViewById(R.id.length_number_text);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //拖动条停止拖动的时候调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            //拖动条开始拖动的时候调用
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //拖动条进度改变的时候调用
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度条最低长度为5，初始进度条进度为5，显示数字10
                length_number_text.setText(String.valueOf(progress + 5));
                refreshPassword();
            }

        });
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        //在活动启动时，调用刷新口令函数
        refreshPassword();

        //如果是从add_account活动进入该活动，显示最下方的返回键
        Intent intent = getIntent();
        // 默认不是从add_account进入
        boolean isFromAddingAccount = intent.getBooleanExtra("from_add_account",
                false);
        if(!isFromAddingAccount){
            Button returnButton = findViewById(R.id.return_password);
            returnButton.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshPassword(){
        password_AZ_switch = findViewById(R.id.password_AZ_switch);
        password_az_lower_switch= findViewById(R.id.password_az_lower_switch);
        password_numbers_switch= findViewById(R.id.password_numbers_switch);
        password_punctuation_switch= findViewById(R.id.password_punctuation_switch);
        length_number_text = findViewById(R.id.length_number_text);
        password_generated = findViewById(R.id.password_generated);
        //可选的口令字符
        ArrayList<Character> passwordCharCandidate = new ArrayList<Character>();
        Character[] numCharArray = {'1','2','3','4','5','6','7','8','9','0'};
        Character lowerAlphabetArray[] = {'a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        Character captialAlphabetArray[] = {'A','B','C','D','E','F','G','H','I','J',
                'K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        Character punctuationArray[] = {'!','@','#','$','%','^','&','*'};
        //检查对应的switch控件
        if(password_numbers_switch.isChecked()){
            passwordCharCandidate.addAll(Arrays.asList(numCharArray));
        }
        if(password_az_lower_switch.isChecked()){
            passwordCharCandidate.addAll(Arrays.asList(lowerAlphabetArray));
        }
        if(password_AZ_switch.isChecked()){
            passwordCharCandidate.addAll(Arrays.asList(captialAlphabetArray));
        }
        if(password_punctuation_switch.isChecked()){
            passwordCharCandidate.addAll(Arrays.asList(punctuationArray));
        }

        int lengthOfCandidate = passwordCharCandidate.size();
        //Log.e("Candidate",passwordCharCandidate.toString());

        if(lengthOfCandidate <= 0)
        {
            //如果发现没有选中任何组成元素 提示并直接返回函数
            password_generated.setText(R.string.password_generator_no_switch_on);
            return;
        }
        //Log.e("length","lengthOfCandidate:"+String.valueOf(lengthOfCandidate));
        StringBuilder passwordBuilder = new StringBuilder();
        //此处强制类型转换CharSequence至String 可能会出bug
        int lengthOfPassword = Integer.valueOf((String)length_number_text.getText());
        Random ran =new Random();
        for(int i=0 ; i<lengthOfPassword ; i++){
            int randomIndex = ran.nextInt(lengthOfCandidate);

            passwordBuilder.append(passwordCharCandidate.get(randomIndex));
        }
        password_generated_string = passwordBuilder.toString();
        password_generated.setText(password_generated_string);
        return;
    }

    public void onClickRefreshButton(View view){
        refreshPassword();
    }

    public void onClickCopyButton(View view){
        //Log.e(password_generated_string,password_generated_string);
        clipData = ClipData.newPlainText("password_generated",password_generated_string);
        clipboardManager.setPrimaryClip(clipData);
    }



}
