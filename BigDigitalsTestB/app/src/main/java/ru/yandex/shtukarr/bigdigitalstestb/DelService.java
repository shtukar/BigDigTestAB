package ru.yandex.shtukarr.bigdigitalstestb;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class DelService extends Service {
    private ContentProviderManager contentProviderManager;
    private int id;
    private static final String ACTION = "ru.yandex.shtukarr.action.REFRESH_DB";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "onStartCommand");
        contentProviderManager = new ContentProviderManager(this);
        id = intent.getIntExtra("ID", -1);
        int countTime = 15000;
        if (id >= 0)
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    contentProviderManager.deleteLink(id);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ACTION);
                    sendBroadcast(broadcastIntent);
                    stopSelf();
                }
            }, countTime);
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
