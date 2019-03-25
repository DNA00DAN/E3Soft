package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CommentData extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "comments.db";
    private static final String tblComment = "comments";

    public CommentData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/" + DATABASE_NAME, null);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tblComment);
    }

    public List<String> getAllComments() {
        List<String> comm = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblComment + " ORDER BY comments.comments ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                comm.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return comm;
    }

    public void insertComment(String commentT) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("comments", commentT);

        // Inserting Row
        db.insert(tblComment, null, values);
        db.close(); // Closing database connection
    }

}
