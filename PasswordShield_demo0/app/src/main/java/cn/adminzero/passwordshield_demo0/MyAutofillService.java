package cn.adminzero.passwordshield_demo0;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyAutofillService extends Service {
    public MyAutofillService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
