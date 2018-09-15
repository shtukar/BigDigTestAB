package ru.yandex.shtukarr.bigdigitalstesta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import ru.yandex.shtukarr.bigdigitalstesta.view.HistoryScreen;

public class RefreshDbBroadcastReceiver extends BroadcastReceiver {
    private HistoryScreen historyScreen;

    public RefreshDbBroadcastReceiver(HistoryScreen historyScreen) {
        this.historyScreen = historyScreen;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive");
        historyScreen.refresh();
        Toast.makeText(context, "Список ссылок обновлён", Toast.LENGTH_SHORT).show();
    }
}
