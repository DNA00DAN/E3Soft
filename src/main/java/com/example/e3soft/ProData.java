package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ProData extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "newPros.db";
    // Labels table name
    private static final String tblSTREAM = "nwPros";
    private static final String tblValves = "valves";

    // Labels Table Columns names
    private static final String KEY_AREA = "area";

    private static final String KEY_PRODUCT = "product";
    private static final String KEY_CF = "cf";
    private static final String KEY_STREAM = "stream";

    private static final String KEY_DATECREATE = "date";
    private static final String KEY_DATEMOD = "date";

    public ProData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/"
                + DATABASE_NAME, null);

    }

    public List<String> getAllStreams(String are) {
        List<String> dDay = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  nwStreams.stream FROM " + tblSTREAM +
                " WHERE nwStreams.area = '" + are + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dDay.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return dDay;
    }

    public List<String> getAllStreams2() {
        List<String> dDay = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblSTREAM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dDay.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return dDay;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void insertStream(String tmpAr, String streamT, String productTT) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AREA, tmpAr);
        values.put(KEY_STREAM, streamT);
        values.put(KEY_PRODUCT, productTT);

        db.insert("nwStreams", null, values);
        db.close(); // Closing database connection

    }
}
