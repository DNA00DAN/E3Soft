package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ValveData extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "valveReports.db";
    // Labels table name
    private static final String tblReports = "valveReports";
    private static final String tblValves = "valves";

    // Labels Table Columns names
    private static final String KEY_ID = "ID";
    private static final String KEY_SITE = "site";
    private static final String KEY_SITE_ID = "siteID";
    private static final String KEY_DRAWING = "drawing";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_LEAKER_ID = "leakerID";
    private static final String KEY_BUSI = "business";
    private static final String KEY_VALVE = "valves";

    private static final String KEY_CAMOP = "camOperator";
    private static final String KEY_CAMSERIAL = "camSerial";
    private static final String KEY_SURVEYOP = "gasSurveyOperator";
    private static final String KEY_SURVEYSERIAL = "gasSurveySerial";

    private static final String KEY_PRODUCT = "product";
    private static final String KEY_EQUIPMENTDESCRIPTION = "equipmentDesc";
    private static final String KEY_EQUIPMENTYPE = "equipmentType";
    private static final String KEY_EQUIPMENTSIZE = "equipmentSize";
    private static final String KEY_EQUIPMENTCLASS = "equipmentClass";
    private static final String KEY_MEASUREMENTPOSITION = "measurementPosition";
    private static final String KEY_SUBDESCRIPTION = "subDescription";
    private static final String KEY_EQUIPMENTID = "equipmentID";
    private static final String KEY_MEASUREMENTRESULT = "measurementResult";
    private static final String KEY_READING = "readingUnit";
    private static final String KEY_BACKGROUND = "background";
    private static final String KEY_ANNUALLOSS = "loss";
    private static final String KEY_RECOMEDATION = "recommendation";

    private static final String KEY_DATECREATE = "date";
    private static final String KEY_DATEMOD = "date";

    public ValveData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/"
                + DATABASE_NAME, null);

    }

    public List<String> getAllDepots() {
        List<String> dDay = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblValves;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dDay.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return dDay;
    }

    public List<String> getAllReports() {
        List<String> pReports = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblReports + " ORDER BY leakerID DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pReports.add(cursor.getString(21));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pReports;
    }

    public void insertReports(String busi, String depot,
                              String drawing, String leakerID, String camOp,
                              String camSerial, String surveyOp, String surveySerial,
                              String product, String equipDesc, String subDesc, String equipType,
                              String equipSize, String equipClass, String equipId,
                              String mPosition, String mResult, String reading, String backG,
                              String aLoss, String recom, String dateCreate, String dateMod, String cyc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BUSI, busi);
        values.put(KEY_VALVE, depot);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_LEAKER_ID, leakerID);

        values.put(KEY_CAMOP, camOp);
        values.put(KEY_CAMSERIAL, camSerial);
        values.put(KEY_SURVEYOP, surveyOp);
        values.put(KEY_SURVEYSERIAL, surveySerial);

        values.put(KEY_PRODUCT, product);
        values.put(KEY_EQUIPMENTDESCRIPTION, equipDesc);
        values.put(KEY_SUBDESCRIPTION, subDesc);
        values.put(KEY_EQUIPMENTYPE, equipType);
        values.put(KEY_EQUIPMENTSIZE, equipSize);
        values.put(KEY_EQUIPMENTCLASS, equipClass);
        values.put(KEY_EQUIPMENTID, equipId);
        values.put(KEY_MEASUREMENTPOSITION, mPosition);
        values.put(KEY_MEASUREMENTRESULT, mResult);
        values.put(KEY_READING, reading);
        values.put(KEY_ANNUALLOSS, aLoss);
        values.put(KEY_RECOMEDATION, recom);
        values.put(KEY_DATECREATE, dateCreate);
        values.put("cycle", cyc);
        // Inserting Row
        db.insert(tblReports, null, values);
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
