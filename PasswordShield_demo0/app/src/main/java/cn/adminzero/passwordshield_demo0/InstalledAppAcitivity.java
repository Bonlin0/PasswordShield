package cn.adminzero.passwordshield_demo0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.passwordshield_demo0.entity.ControledApp;

import static cn.adminzero.passwordshield_demo0.db.DbUtil.fetchAllAppInfo;
import static cn.adminzero.passwordshield_demo0.util.LogUtils.t;

public class InstalledAppAcitivity extends AppCompatActivity {

    List<ControledApp> dataList = new ArrayList<ControledApp>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_app_acitivity);


        dataList = fetchAllAppInfo();
        AppDataAdapter adapter = new AppDataAdapter(this, R.layout.list_item, dataList);
        ListView listView = (ListView) findViewById(R.id.list_main);

        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ControledApp item = dataList.get(position);
//                PackageManager pManager = getPackageManager();
//                Intent intent = pManager.getLaunchIntentForPackage(item.packageName);
//                startActivity(intent);
                String uri = item.getPackageName();
                t(uri);
                Intent intent = new Intent();
                intent.putExtra("uri_return",uri);
                setResult(RESULT_OK,intent);
                finish();

            }
        });


    }


}
