package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class oldData extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "oldLeakers.db";
    // Labels table name
    private static final String tblleakers = "oldLeakers";

    // Labels Table Columns names
    private static final String KEY_oldID = "leakerID";
    private static final String KEY_newID = "oldLeak";

    public oldData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/"
                + DATABASE_NAME, null);

    }

    public void insertLeaker(String neww, String oldd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_newID, oldd);
        values.put(KEY_oldID, neww);
        // Inserting Row
        db.insert(tblleakers, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
