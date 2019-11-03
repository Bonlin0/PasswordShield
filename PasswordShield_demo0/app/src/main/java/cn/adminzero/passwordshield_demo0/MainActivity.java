package cn.adminzero.passwordshield_demo0;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

import cn.adminzero.passwordshield_demo0.db.DbUtil;
import cn.adminzero.passwordshield_demo0.entity.PasswordItem;
import cn.adminzero.passwordshield_demo0.util.IconFinder;

import static cn.adminzero.passwordshield_demo0.db.DbUtil.deletePasswordItem;
import static cn.adminzero.passwordshield_demo0.db.DbUtil.fetchAppInfo;
import static cn.adminzero.passwordshield_demo0.db.DbUtil.init_database;
import static cn.adminzero.passwordshield_demo0.db.DbUtil.storeAppInfo;
//import static cn.adminzero.passwordshield_demo0.db.DbUtil.fuck_database;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity_debug";

    public  static List<PasswordItem> accountList = new ArrayList<PasswordItem>();
    public long current_date;

    //public  Boolean isFirstLogin = true;
    public long Locktime;

    static {
            //fuck_database();
    }
    static {
//        init_database();
        storeAppInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_date = System.currentTimeMillis();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        accountList = DbUtil.initplainPasswordListView();

        MainActivity.List_adapter list_adapter = new MainActivity.List_adapter(MainActivity.this, R.layout.listview_image, accountList);
        ListView listView = (ListView) findViewById(R.id.mainlist_view);
        listView.setAdapter(list_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasswordItem account = accountList.get(position);
                // Toast.makeText(MainActivity.this, account.getAccount(), Toast.LENGTH_LONG).show();
                Intent intent_ToModifyActivity = new Intent(MainActivity.this, ModifyActivity.class);
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
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("删除该密码条目");
                dialog.setMessage("您确定要删除该密码条目吗?");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePasswordItem(account.getAccount(),account.getUri());
                        //刷新
                        //onCreate(null);
                        Intent intent=new Intent(MainActivity.this,MainActivity.class);
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
                Intent intent_to_Add = new Intent(MainActivity.this, AddAccountActivity.class);
                intent_to_Add.putExtra("isimportant",'0');
                startActivity(intent_to_Add);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        NavigationView navigationView = findViewById(R.id.nav_view_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //获取用户名并显示
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String usernameToDisplay = sharedPreferences.getString("preference_username", "Default Username");
        TextView drawerUsername = navigationView.findViewById(R.id.drawer_username_text);
        if (drawerUsername != null) {
            drawerUsername.setText(usernameToDisplay);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int lock_min = Integer.parseInt(sharedPreferences.getString("time_to_lock", "1"));
        Locktime = (lock_min) * 60 * 1000;
        long sub_time = System.currentTimeMillis() - current_date;
        Log.d(TAG, System.currentTimeMillis() + "-" + current_date + "sub_time" + sub_time + "lock_time" + Locktime);
        if (Locktime < (sub_time)) {
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }


        accountList = DbUtil.initplainPasswordListView();
        MainActivity.List_adapter list_adapter = new MainActivity.List_adapter(MainActivity.this, R.layout.listview_image, accountList);
        ListView listView = (ListView) findViewById(R.id.mainlist_view);
        listView.setAdapter(list_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasswordItem account = accountList.get(position);
                // Toast.makeText(MainActivity.this, account.getAccount(), Toast.LENGTH_LONG).show();
                Intent intent_ToModifyActivity = new Intent(MainActivity.this, ModifyActivity.class);
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
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Delete the account");
                dialog.setMessage("Are you sure to delete the account information?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePasswordItem(account.getAccount(),account.getUri());
                        //刷新
                        //onCreate(null);
                        Intent intent=new Intent(MainActivity.this,MainActivity.class);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            //弹出关于信息的对话框

            //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //    设置Title的图标
            builder.setIcon(R.mipmap.ic_launcher);
            //    设置Title的内容
            builder.setTitle(R.string.about_dialog_title);
            //    设置Content来显示一个信息
            builder.setMessage(R.string.about_content);
            //    设置一个PositiveButton
            builder.setPositiveButton("I see", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                }
            });
            //    显示出该对话框
            builder.show();

            //Toast.makeText(MainActivity.this, R.string.about, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_help) {
            //弹出网页https://github.com/Bonlin0/PasswordShield
            Intent intent_help = new Intent(Intent.ACTION_VIEW);
            intent_help.setData(Uri.parse("https://github.com/Bonlin0/PasswordShield"));
            startActivity(intent_help);
            //Toast.makeText(MainActivity.this, R.string.help, Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    // 这部分是关于drawer内元素被点击的代码
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_password_vault) {
            Toast.makeText(MainActivity.this, "You are already here!",
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_password_generator) {
            Intent passwordGeneratorIntent = new Intent
                    (this, PasswordGeneratorActivity.class);
            startActivity(passwordGeneratorIntent);

        } else if (id == R.id.nav_menu_settings) {
            Intent settingsIntent = new Intent
                    (this, SettingsActivity.class);
            startActivity(settingsIntent);

        } else if (id == R.id.nav_important_password) {
            Intent ImportantPasswordIntent = new Intent
                    (this, ImportantPasswordActivity.class);
            startActivity(ImportantPasswordIntent);

        }else if (id == R.id.nav_lock_now) {
            finish();
            //TODO 补充锁定方法，而不是直接退出
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class List_adapter extends ArrayAdapter<PasswordItem> {
        private int resourceId;

        public List_adapter(Context context, int textViewResourseId, List<PasswordItem> objects) {
            super(context, textViewResourseId, objects);
            resourceId = textViewResourseId;
        }

        @SuppressLint("ResourceAsColor")
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
            //debug
            System.out.println(TAG+" "+account.getName()+"date "+ account.getTime()+"  "+MyApplication.getnowDate());
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
                Toast.makeText(MainActivity.this,account.getName()+"密码过期，请及时修改",Toast.LENGTH_LONG).show();
            }
            return view;
        }

        /**
         * 给列表项分配对应的图片  然后在getView 中创建适配器时调用
         */


    }
}
