package ru.yandex.shtukarr.bigdigitalstestb;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ContentProviderManager {
    private static final String ACTION = "ru.yandex.shtukarr.action.REFRESH_DB";

    private final static String LINKS_ID = "id_link";
    private final static String LINKS_URL = "url";
    private final static String LINKS_STATUS = "status";
    private final static String LINKS_TIME = "time";

    private static final Uri URI = Uri.parse("content://ru.yandex.shtukarr.MyCP/linksProvider");

    private Context context;

    ContentProviderManager(Context context) {
        this.context = context;
    }

    public void insertLink(String link, int status, long time) {
        ContentValues cv = new ContentValues();
        cv.put(LINKS_URL, link);
        cv.put(LINKS_STATUS, status);
        cv.put(LINKS_TIME, time);
        context.getContentResolver().insert(URI, cv);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION);
        context.sendBroadcast(broadcastIntent);
    }

    public void updateLink(int id, String link, int status, long time) {
        ContentValues cv = new ContentValues();
        cv.put(LINKS_URL, link);
        cv.put(LINKS_STATUS, status);
        cv.put(LINKS_TIME, time);
        context.getContentResolver().update(ContentUris.withAppendedId(URI, id), cv, null, null);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION);
        context.sendBroadcast(broadcastIntent);
    }

    public void deleteLink(int id) {
        context.getContentResolver().delete(URI, LINKS_ID + "= ?", new String[]{String.valueOf(id)});
    }
}
