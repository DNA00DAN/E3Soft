package com.example.e3soft;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class StationData extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "flocalMarketing.sqlite";

    private static final String tblProduct = "products";
    private static final String tblSequence = "sequence";
    private static final String tblContacts = "contacts";
    private static final String tblSites = "sites";
    private static final String tblDrawings = "drawing";
    private static final String tblSiteIDs = "siteIDs";
    private static final String tblDepots = "depots";

    private static final String KEY_NAME = "site";
    private static final String KEY_DRAWING = "drawing";
    private static final String KEY_STREAM = "stream";
    private static final String KEY_LEAKER_ID = "leakerID";
    private static final String KEY_SITE_ID = "siteID";
    private static final String KEY_CONTACT = "contact";

    private static final String KEY_CAMOP = "camOperator";
    private static final String KEY_CAMSERIAL = "camSerial";
    private static final String KEY_SURVEYOP = "gasSurveyOperator";
    private static final String KEY_SURVEYSERIAL = "gasSurveySerial";

    private static final String KEY_PRODUCT = "product";
    private static final String KEY_PRODUCT_CF = "cf";
    private static final String KEY_EQUIPMENTDESCRIPTION = "equipmentDesc";
    private static final String KEY_EQUIPMENTYPE = "equipmentType";
    private static final String KEY_EQUIPMENTSIZE = "equipmentSize";
    private static final String KEY_MEASUREMENTPOSITION = "measurementPosition";
    private static final String KEY_SUBDESCRIPTION = "subDescription";
    private static final String KEY_EQUIPMENTID = "equipmentID";
    private static final String KEY_MEASUREMENTRESULT = "measurementResult";
    private static final String KEY_READING = "readingUnit";
    private static final String KEY_BACKGROUND = "background";
    private static final String KEY_ANNUALLOSS = "loss";
    private static final String KEY_RECOMEDATION = "recommendation";
    private static final String KEY_DATECREATE = "date";
    private static final String KEY_DATEMOD = "dateMod";

    public StationData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/"
                + DATABASE_NAME, null);

    }

    public List<String> getAlldays() {
        List<String> dDay = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblSequence;

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

    public List<String> getAllSites(String seq) {
        List<String> sSites = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM siteIDs ORDER BY siteID ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                sSites.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return sSites;
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    public List<String> getAllProducts() {
        List<String> pProds = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblProduct;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pProds.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pProds;
    }

    public List<String> getAllDepots() {
        List<String> pProds = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblDepots;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pProds.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pProds;
    }
}
