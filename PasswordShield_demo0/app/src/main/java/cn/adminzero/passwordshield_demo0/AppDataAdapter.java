package cn.adminzero.passwordshield_demo0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.adminzero.passwordshield_demo0.entity.ControledApp;

public class AppDataAdapter extends ArrayAdapter<ControledApp> {
    private int resourceId;

    public AppDataAdapter(Context context, int viewResourceid, List<ControledApp> objects) {
        super(context, viewResourceid, objects);
        resourceId = viewResourceid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        super.getView(position, convertView, parent);
        ControledApp appData = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView AppDataLable = (TextView) view.findViewById(R.id.label);
        TextView AppDataPackageName = (TextView) view.findViewById(R.id.pname);
        ImageView AppDataIcon = (ImageView) view.findViewById(R.id.icon);

        AppDataLable.setText(appData.getLable());
        AppDataPackageName.setText(appData.getPackageName());

        AppDataIcon.setImageDrawable(appData.getIcon());

        return view;

    }
}
