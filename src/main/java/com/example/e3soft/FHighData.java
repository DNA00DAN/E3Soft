package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FHighData extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "fHighLeaker.db";

    private static final String tblHighLeaker = "fHighLeaker";
    private static final String tblProduct = "product";

    private static final String KEY_TYPE = "business";
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "stationName";
    private static final String KEY_STATID = "stationID";
    private static final String KEY_CONT = "contactN";
    private static final String KEY_LEAKER_ID = "leakerID";
    private static final String KEY_DRAW = "drawing";

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

    public FHighData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/" + DATABASE_NAME, null);

    }

    public void insertFHighLeaker(String leakerID, String busi, String name,
                                  String statID, String cont, String drawing,
                                  String camOp, String camSerial, String surveyOp,
                                  String surveySerial, String product, String equipDesc,
                                  String subDesc, String equipType, String equipSize,
                                  String equipId, String mPosition,
                                  String mResult, String aLoss, String recom,
                                  String dateCreate, String cyc
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LEAKER_ID, leakerID);
        values.put(KEY_TYPE, busi);
        values.put(KEY_STATID, name);
        values.put(KEY_NAME, statID);
        values.put(KEY_CONT, cont);
        values.put(KEY_DRAW, drawing);


        values.put(KEY_CAMOP, camOp);
        values.put(KEY_CAMSERIAL, camSerial);
        values.put(KEY_SURVEYOP, surveyOp);
        values.put(KEY_SURVEYSERIAL, surveySerial);

        values.put(KEY_PRODUCT, product);
        values.put(KEY_EQUIPMENTDESCRIPTION, equipDesc);
        values.put(KEY_SUBDESCRIPTION, subDesc);
        values.put(KEY_EQUIPMENTYPE, equipType);
        values.put(KEY_EQUIPMENTSIZE, equipSize);
        values.put(KEY_EQUIPMENTID, equipId);

        values.put(KEY_MEASUREMENTPOSITION, mPosition);
        values.put(cyc, mResult);
        values.put(KEY_ANNUALLOSS, aLoss);
        values.put(KEY_RECOMEDATION, recom);
        values.put(KEY_DATECREATE, dateCreate);
        values.put("cycle", cyc);

        // Inserting Row
        db.insert(tblHighLeaker, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + tblHighLeaker);
    }

    public void updateFHighLeaker(String leakerT, String strMRes,
                                  String readingT, String strMResult, String strRecommendation) {
        SQLiteDatabase dbr2 = this.getWritableDatabase();
        dbr2.execSQL("UPDATE fHighLeaker SET " + "newC1Result" + " = '" + strMRes + "', "
                + "loss" + " = '" + strMResult + "', "
                + "readingUnit" + " = '" + readingT + "', "
                + "recommendation" + " = '" + strRecommendation + "' "
                + " WHERE leakerID='" + leakerT + "'");
        Log.d("update Reports", leakerT + "Valve");
        Log.d("update Reports", strMRes);
        Log.d("update Reports", strMResult);
        Log.d("update Reports", strRecommendation);
        Log.d("update Reports", "success");
        dbr2.close(); // Closing database connection
    }

    public void updateFHighLeaker2(String leakerT, String strMRes,
                                   String readingT, String strMResult, String strRecommendation, String Cyc) {
        SQLiteDatabase dbr2 = this.getWritableDatabase();
        dbr2.execSQL("UPDATE fHighLeaker SET " + Cyc + " = '" + strMRes + "', "
                + "loss" + " = '" + strMResult + "', "
                + "readingUnit" + " = '" + readingT + "', "
                + "recommendation" + " = '" + strRecommendation + "' "
                + " WHERE leakerID='" + leakerT + "'");
        Log.d("update Reports", leakerT + "Valve");
        Log.d("update Reports", strMRes);
        Log.d("update Reports", strMResult);
        Log.d("update Reports", strRecommendation);
        Log.d("update Reports", "success");
        dbr2.close(); // Closing database connection
    }

    public List<String> getAllLeakers(String leakerT) {
        List<String> hLeaker = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblHighLeaker +
                " WHERE fHighLeaker.leakerID = '" + leakerT + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                hLeaker.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return hLeaker;
    }

    public List<String> getAllData() {
        List<String> pReports = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM fHighLeaker ORDER BY leakerID DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pReports.add(cursor.getString(38));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pReports;
    }

    public List<String> getAllReports() {
        // TODO Auto-generated method stub
        return null;
    }
}
