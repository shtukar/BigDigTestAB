package ru.yandex.shtukarr.bigdigitalstesta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "linksDB";
    private final static int DATABASE_VERSION = 1;

    private static final String LINKS_TABLE = "Links";

    private static final String LINKS_ID = "id_link";
    private static final String LINKS_URL = "url";
    private static final String LINKS_STATUS = "status";
    private static final String LINKS_TIME = "time";

    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LINK_TABLE = "CREATE TABLE " + LINKS_TABLE + "("
                + LINKS_ID + " INTEGER PRIMARY KEY," + LINKS_URL + " TEXT,"
                + LINKS_STATUS + " TEXT," + LINKS_TIME + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_LINK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LINKS_TABLE);
        onCreate(sqLiteDatabase);
    }

    public List<LinkModel> getAllLinks() {
        List<LinkModel> linkModelList = new ArrayList<LinkModel>();
        String selectQuery = "SELECT  * FROM " + LINKS_TABLE;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LinkModel linkModel = new LinkModel();
                linkModel.setId(Integer.parseInt(cursor.getString(0)));
                linkModel.setUrl(cursor.getString(1));
                linkModel.setStatus(Integer.parseInt(cursor.getString(2)));
                linkModel.setDate(new Date(Long.parseLong(cursor.getString(3))));
                linkModelList.add(linkModel);
            } while (cursor.moveToNext());
        }

        return linkModelList;
    }

    public long addLink(ContentValues values) {
        db = this.getWritableDatabase();
        long rowID = db.insert(LINKS_TABLE, null, values);
        db.close();
        return rowID;
    }

    public int update(ContentValues values, String selection, String[] selectionArgs) {
        return db.update(LINKS_TABLE, values, selection, selectionArgs);
    }

    public int removeCity(String selection, String[] selectionArgs) {
        db = this.getWritableDatabase();
        return db.delete(LINKS_TABLE, selection, selectionArgs);
    }
}