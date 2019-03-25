package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HighData extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "highLeaker.db";

    private static final String tblHighLeaker = "highLeaker";
    private static final String tblProduct = "product";
    private static final String tblStream = "stream";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "site";
    private static final String KEY_AREA = "area";
    private static final String KEY_UNITNUMBER = "unit";
    private static final String KEY_UNIT = "unitName";
    private static final String KEY_UNIT_SIZE = "unitSize";
    private static final String KEY_DRAWING = "drawing";
    private static final String KEY_EQUIPLOC = "equipmentLoc";
    private static final String KEY_SPECIFICLOC = "specificLoc";
    private static final String KEY_STREAM = "stream";
    private static final String KEY_SYSTEM = "system";
    private static final String KEY_LEAKER_ID = "leakerID";

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

    public HighData(Context context) {
        super(context, "/sdcard/e3softData/Data/" + DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase("/sdcard/e3softData/Data/" + DATABASE_NAME, null);

    }

    public void insertHighLeaker(int id, String name,
                                 String area, String unitNumber, String unit, String unitSize, String drawing,
                                 String stream, String leakerID, String specificLoc, String system, String equipLoc,
                                 String camOp, String camSerial, String surveyOp,
                                 String surveySerial, String product, String equipDesc,
                                 String subDesc, String equipType, String equipSize,
                                 String equipId, String mPosition,
                                 String mResult, String reading, String aLoss, String recom,
                                 String dateCreate, String cyc
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ID", id);
        values.put(KEY_NAME, name);
        values.put(KEY_AREA, area);
        values.put(KEY_UNITNUMBER, unitNumber);
        values.put(KEY_UNIT, unit);
        values.put(KEY_UNIT_SIZE, unitSize);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_STREAM, stream);
        values.put(KEY_LEAKER_ID, leakerID);
        values.put(KEY_SPECIFICLOC, specificLoc);
        values.put(KEY_SYSTEM, system);
        values.put(KEY_EQUIPLOC, equipLoc);

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
        values.put(KEY_READING, reading);
        values.put(KEY_ANNUALLOSS, aLoss);
        values.put(KEY_RECOMEDATION, recom);
        values.put(KEY_DATECREATE, dateCreate);

        // Inserting Row
        db.insert(tblHighLeaker, null, values);
        db.close(); // Closing database connection
    }

    public List<String> getAllLeakers() {
        List<String> hLeaker = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblHighLeaker;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                hLeaker.add(cursor.getString(46));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return hLeaker;
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

    public void updateHighLeaker2(String leakerT, String strMRes,
                                  String readingT, String strMResult, String strRecommendation, String Cyc, String comment) {
        SQLiteDatabase dbr2 = this.getWritableDatabase();
        dbr2.execSQL("UPDATE highLeaker SET " + Cyc + " = '" + strMRes + "', "
                + "loss" + " = '" + strMResult + "', "
                + "readingUnit" + " = '" + readingT + "', "
                + "recommendation" + " = '" + strRecommendation + "', "
                + "comment" + " = '" + comment + "' "
                + " WHERE leakerID='" + leakerT + "'");
        Log.d("update Reports", leakerT + "Valve");
        Log.d("update Reports", strMRes);
        Log.d("update Reports", strMResult);
        Log.d("update Reports", strRecommendation);
        Log.d("update Reports", "success");
        dbr2.close(); // Closing database connection
    }
}
