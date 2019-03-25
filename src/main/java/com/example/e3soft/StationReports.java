package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class StationReports extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "fHighLeaker.db";
    // Labels table name
    private static final String tblReports = "fHighLeaker";

    // Labels Table Columns names
    private static final String KEY_ID = "ID";
    private static final String KEY_SITE = "site";
    private static final String KEY_SITE_ID = "siteID";
    private static final String KEY_DRAWING = "drawing";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_LEAKER_ID = "leakerID";
    private static final String KEY_BUSI = "business";

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
    public static Current_Site sites;
    public static Local_Marketing lM;
    public static Inspection inspec;
    public String tmpKey, site, area, unit, drawing, stream;
    public String day, loc, fID, fDraw;

    public StationReports(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/"
                + DATABASE_NAME, null);

    }

    public void insertReports(String busi, String loc, String statID,
                              String contact, String drawing, String leakerID, String camOp,
                              String camSerial, String surveyOp, String surveySerial,
                              String product, String equipDesc, String subDesc, String equipType,
                              String equipSize, String equipClass, String equipId,
                              String mPosition, String mResult, String reading, String backG,
                              String aLoss, String recom, String dateCreate, String dateMod, String cyc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BUSI, busi);
        values.put(KEY_SITE, loc);
        values.put(KEY_SITE_ID, statID);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_LEAKER_ID, leakerID);
        values.put(KEY_CONTACT, contact);

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

    public List<String> getAllReports() {
        List<String> pReports = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblReports + " ORDER BY leakerID DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pReports.add(cursor.getString(23));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pReports;
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}
