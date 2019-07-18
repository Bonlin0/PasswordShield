package cn.adminzero.passwordshield_demo0;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class IconFinderService extends Service {
    public IconFinderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
