package cn.adminzero.passwordshield_demo0;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
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
import android.widget.ArrayAdapter;
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
import static cn.adminzero.passwordshield_demo0.db.DbUtil.fuck_database;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity_debug";
    //    private String[] data = {"Apple", "Banana", "Orange", "Watermelon", "Pear",
//            "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"};
    private List<PasswordItem> accountList = new ArrayList<PasswordItem>();
    public long current_date;

    //public  Boolean isFirstLogin = true;
    public long Locktime;

    static {
            //fuck_database();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_date = System.currentTimeMillis();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        /*添加适配器的这段代码必须放在setContentView 的后面,不然会闪退*/
      /*  ListView listView=(ListView) findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        */
        /*添加适配器的这段代码必须放在setContentView 的后面,不然会闪退*/
      /*  ListView listView=(ListView) findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        */
        // initAccounts();
        accountList = DbUtil.initPasswordListView();
        MainActivity.List_adapter list_adapter = new MainActivity.List_adapter(MainActivity.this, R.layout.listview_image, accountList);
        ListView listView = (ListView) findViewById(R.id.mainlist_view);
        listView.setAdapter(list_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasswordItem account = accountList.get(position);
                Toast.makeText(MainActivity.this, account.getAccount(), Toast.LENGTH_LONG).show();
                Intent intent_ToModifyActivity = new Intent(MainActivity.this, ModifyActivity.class);
                intent_ToModifyActivity.putExtra("id", account.getId());
                intent_ToModifyActivity.putExtra("account", account.getAccount());
                intent_ToModifyActivity.putExtra("password", account.getPassword());
                intent_ToModifyActivity.putExtra("uri", account.getUri());
                intent_ToModifyActivity.putExtra("type", account.getType());
                intent_ToModifyActivity.putExtra("note", account.getNote());
                startActivity(intent_ToModifyActivity);
            }
        });
        ///长按删除
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
                        Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_LONG).show();
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
        //SharedPreferences.Editor sharedPreferenceEditor;
        //sharedPreferenceEditor = sharedPreferences.edit();
        //sharedPreferenceEditor.putString("preference_username",usernameInput);
        //sharedPreferenceEditor.apply();
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


        accountList = DbUtil.initPasswordListView();
        MainActivity.List_adapter list_adapter = new MainActivity.List_adapter(MainActivity.this, R.layout.listview_image, accountList);
        ListView listView = (ListView) findViewById(R.id.mainlist_view);
        listView.setAdapter(list_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasswordItem account = accountList.get(position);
                Toast.makeText(MainActivity.this, account.getAccount(), Toast.LENGTH_LONG).show();
                Intent intent_ToModifyActivity = new Intent(MainActivity.this, ModifyActivity.class);
                intent_ToModifyActivity.putExtra("id", account.getId());
                intent_ToModifyActivity.putExtra("account", account.getAccount());
                intent_ToModifyActivity.putExtra("password", account.getPassword());
                intent_ToModifyActivity.putExtra("type", account.getType());
                intent_ToModifyActivity.putExtra("uri", account.getUri());
                intent_ToModifyActivity.putExtra("note", account.getNote());

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
                        Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_LONG).show();
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
//        //获取用户名并显示
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        //SharedPreferences.Editor sharedPreferenceEditor;
//        //sharedPreferenceEditor = sharedPreferences.edit();
//        //sharedPreferenceEditor.putString("preference_username",usernameInput);
//        //sharedPreferenceEditor.apply();
//        String usernameToDisplay = sharedPreferences.getString("preference_username","Default Username");
//        TextView drawerUsername = findViewById(R.id.drawer_username);
//        if(drawerUsername!=null){
//            drawerUsername.setText(usernameToDisplay);
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
            Toast.makeText(MainActivity.this, R.string.about, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_help) {
            Toast.makeText(MainActivity.this, R.string.help, Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
        } else if (id == R.id.nav_lock_now) {
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

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final PasswordItem account = getItem(position);//获取当前项的Account实例

            final View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            final ImageView accountImage = (ImageView) view.findViewById(R.id.list_image);
            TextView accountName = (TextView) view.findViewById(R.id.list_name);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PasswordItem account = getItem(position);//获取当前项的Account实例

                        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                        ImageView accountImage = (ImageView) view.findViewById(R.id.list_image);
                        Bitmap bm = IconFinder.getBitmap(account.getUri());
                        accountImage.setImageBitmap(bm);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            accountImage.setImageBitmap(IconFinder.lastFetchBitmap);
//            try {
//                accountImage.setImageBitmap(IconFinder.getBitmap(account.getUri()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //accountImage.setImageResource(IconFinder.getBitmap(account.getUri()));
            //accountImage.setImageResource(setImageId(account.getAccount()));
            accountName.setText(account.getAccount());
            return view;
        }

        /**
         * 给列表项分配对应的图片  然后在getView 中创建适配器时调用
         */
        private int setImageId(String account_name) {
            int imageId = 0;
            int c = (int) account_name.charAt(0);
          /*  if(c>=65 &&c<=90){
                imageId=(65-c)+R.drawable.a;
            }
            else if(c>=91&&c<=122){
                imageId=(91-c)+R.drawable.a;
            }
            else imageId=R.drawable.a;
            */
            switch (c) {
                case 65:
                    imageId = R.drawable.a;
                    break;
                case 66:
                    imageId = R.drawable.b;
                    break;
                case 67:
                    imageId = R.drawable.c;
                    break;
                case 68:
                    imageId = R.drawable.d;
                    break;
                case 69:
                    imageId = R.drawable.e;
                    break;
                case 70:
                    imageId = R.drawable.f;
                    break;
                case 71:
                    imageId = R.drawable.g;
                    break;
                case 72:
                    imageId = R.drawable.h;
                    break;
                case 73:
                    imageId = R.drawable.i;
                    break;
                case 74:
                    imageId = R.drawable.j;
                    break;
                case 75:
                    imageId = R.drawable.k;
                    break;
                case 76:
                    imageId = R.drawable.l;
                    break;
                case 77:
                    imageId = R.drawable.m;
                    break;
                case 78:
                    imageId = R.drawable.n;
                    break;
                case 79:
                    imageId = R.drawable.o;
                    break;
                case 80:
                    imageId = R.drawable.p;
                    break;
                case 81:
                    imageId = R.drawable.q;
                    break;
                case 82:
                    imageId = R.drawable.r;
                    break;
                case 83:
                    imageId = R.drawable.s;
                    break;
                case 84:
                    imageId = R.drawable.t;
                    break;
                case 85:
                    imageId = R.drawable.u;
                    break;
                case 86:
                    imageId = R.drawable.v;
                    break;
                case 87:
                    imageId = R.drawable.w;
                    break;
                case 88:
                    imageId = R.drawable.x;
                    break;
                case 89:
                    imageId = R.drawable.y;
                    break;
                case 90:
                    imageId = R.drawable.z;
                    break;
                case 97:
                    imageId = R.drawable.a;
                    break;
                case 98:
                    imageId = R.drawable.b;
                    break;
                case 99:
                    imageId = R.drawable.c;
                    break;
                case 100:
                    imageId = R.drawable.d;
                    break;
                case 101:
                    imageId = R.drawable.e;
                    break;
                case 102:
                    imageId = R.drawable.f;
                    break;
                case 103:
                    imageId = R.drawable.g;
                    break;
                case 104:
                    imageId = R.drawable.h;
                    break;
                case 105:
                    imageId = R.drawable.i;
                    break;
                case 106:
                    imageId = R.drawable.j;
                    break;
                case 107:
                    imageId = R.drawable.k;
                    break;
                case 108:
                    imageId = R.drawable.l;
                    break;
                case 109:
                    imageId = R.drawable.m;
                    break;
                case 110:
                    imageId = R.drawable.n;
                    break;
                case 111:
                    imageId = R.drawable.o;
                    break;
                case 112:
                    imageId = R.drawable.p;
                    break;
                case 113:
                    imageId = R.drawable.q;
                    break;
                case 114:
                    imageId = R.drawable.r;
                    break;
                case 115:
                    imageId = R.drawable.s;
                    break;
                case 116:
                    imageId = R.drawable.t;
                    break;
                case 117:
                    imageId = R.drawable.u;
                    break;
                case 118:
                    imageId = R.drawable.v;
                    break;
                case 119:
                    imageId = R.drawable.w;
                    break;
                case 120:
                    imageId = R.drawable.x;
                    break;
                case 121:
                    imageId = R.drawable.y;
                    break;
                case 122:
                    imageId = R.drawable.z;
                    break;
                default:
                    imageId = R.drawable.add;
                    break;
            }
            return imageId;
        }


    }
}
