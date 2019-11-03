package cn.adminzero.passwordshield_demo0;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.passwordshield_demo0.db.DbUtil;
import cn.adminzero.passwordshield_demo0.entity.PasswordItem;

import static cn.adminzero.passwordshield_demo0.db.DbUtil.deletePasswordItem;
import static cn.adminzero.passwordshield_demo0.db.DbUtil.fetchAppInfo;

public class ImportantPasswordActivity extends AppCompatActivity {
    private List<PasswordItem> accountList = new ArrayList<PasswordItem>();
    private static final String TAG = "ImportantActivity";
    public long current_date;
    public long Locktime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        accountList = DbUtil.initimportantPasswordListView();
        ImportantPasswordActivity.List_adapter list_adapter = new ImportantPasswordActivity.List_adapter(ImportantPasswordActivity.this, R.layout.listview_image, accountList);
        ListView listView = (ListView) findViewById(R.id.mainlist_view);
        listView.setAdapter(list_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasswordItem account = accountList.get(position);
                // Toast.makeText(MainActivity.this, account.getAccount(), Toast.LENGTH_LONG).show();
                Intent intent_ToModifyActivity = new Intent(ImportantPasswordActivity.this, ModifyActivity.class);
                intent_ToModifyActivity.putExtra("id", account.getId());
                intent_ToModifyActivity.putExtra("account", account.getAccount());
                intent_ToModifyActivity.putExtra("password", account.getPassword());
                intent_ToModifyActivity.putExtra("uri", account.getUri());
                intent_ToModifyActivity.putExtra("type", account.getType());
                intent_ToModifyActivity.putExtra("note", account.getNote());
                intent_ToModifyActivity.putExtra("name",account.getName());
                startActivity(intent_ToModifyActivity);
            }
        });
        ///长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final PasswordItem account = accountList.get(position);
                AlertDialog.Builder dialog=new AlertDialog.Builder(ImportantPasswordActivity.this);
                dialog.setTitle("删除该密码条目");
                dialog.setMessage("您确定要删除该密码条目吗?");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePasswordItem(account.getAccount(),account.getUri());
                        //刷新
                        //onCreate(null);
                        Intent intent=new Intent(ImportantPasswordActivity.this,ImportantPasswordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
                //阻断
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_Ad = new Intent(ImportantPasswordActivity.this, AddAccountActivity.class);
                intent_to_Ad.putExtra("important","1");
                startActivity(intent_to_Ad);
            }
        });

    }





    public class List_adapter extends ArrayAdapter<PasswordItem> {
        private int resourceId;

        public List_adapter(Context context, int textViewResourseId, List<PasswordItem> objects) {
            super(context, textViewResourseId, objects);
            resourceId = textViewResourseId;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            PasswordItem account = getItem(position);//获取当前项的Account实例

            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            // int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.WRITE_EXTERNAL_STORAGE);

            //  final View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ImageView accountImage = (ImageView) view.findViewById(R.id.list_image);
            TextView accountName = (TextView) view.findViewById(R.id.list_name);
            TextView account_username=(TextView) view.findViewById(R.id.list_account);

            accountImage.setImageDrawable(fetchAppInfo(account.getUri()).getIcon());
//            accountImage.setImageResource(setImageId(account.getAccount()));
            accountName.setText(account.getName());
            account_username.setText(account.getAccount());
            //如果密码时间超过 90天 就红色标记
            int sub=0;
            int old=Integer.valueOf(account.getTime());
            int now=Integer.valueOf(MyApplication.getnowDate());
            sub=(old/10000-now/10000)*365+ ((old%10000)/100-(now%10000)/100)+((old%100)-(now%100));
            sub=0-sub;
            System.out.println(TAG+""+sub);

            if(sub>=90)
            {
                account_username.setBackgroundColor(Color.YELLOW);
                accountName.setBackgroundColor(Color.YELLOW);
                Toast.makeText(ImportantPasswordActivity.this,account.getName()+"密码过期，请及时修改",Toast.LENGTH_LONG).show();
            }
            return view;
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");


        accountList = DbUtil.initimportantPasswordListView();
        ImportantPasswordActivity.List_adapter list_adapter = new ImportantPasswordActivity.List_adapter(ImportantPasswordActivity.this, R.layout.listview_image, accountList);
        ListView listView = (ListView) findViewById(R.id.mainlist_view);
        listView.setAdapter(list_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasswordItem account = accountList.get(position);
                // Toast.makeText(MainActivity.this, account.getAccount(), Toast.LENGTH_LONG).show();
                Intent intent_ToModifyActivity = new Intent(ImportantPasswordActivity.this, ModifyActivity.class);
                intent_ToModifyActivity.putExtra("id", account.getId());
                intent_ToModifyActivity.putExtra("account", account.getAccount());
                intent_ToModifyActivity.putExtra("password", account.getPassword());
                intent_ToModifyActivity.putExtra("type", account.getType());
                intent_ToModifyActivity.putExtra("uri", account.getUri());
                intent_ToModifyActivity.putExtra("note", account.getNote());
                intent_ToModifyActivity.putExtra("name",account.getName());

                startActivity(intent_ToModifyActivity);
            }
        });
        //长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final PasswordItem account = accountList.get(position);
                AlertDialog.Builder dialog=new AlertDialog.Builder(ImportantPasswordActivity.this);
                dialog.setTitle("Delete the account");
                dialog.setMessage("Are you sure to delete the account information?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePasswordItem(account.getAccount(),account.getUri());
                        //刷新
                        //onCreate(null);
                        Intent intent=new Intent(ImportantPasswordActivity.this,ImportantPasswordActivity.class);

                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
                //阻断
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        current_date = System.currentTimeMillis();
        Log.d(TAG, "onStop: " + current_date);

    }

}


