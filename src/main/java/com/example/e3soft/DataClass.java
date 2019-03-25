package com.example.e3soft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataClass extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "e3softdata.db";

    private static final String tblGI = "generalInfo";
    private static final String tblMAARes = "maaResults";
    private static final String tblMABRes = "mabResults";
    private static final String tblSHURes = "shuResults";

    private static final String tblMAADates = "maaCycleDates";
    private static final String tblMABDates = "mabCycleDates";
    private static final String tblSHUDates = "shuCycleDates";

    private static final String tblMAALoss = "maaLoss";
    private static final String tblMABLoss = "mabLoss";
    private static final String tblSHULoss = "shuLoss";

    private static final String tblTmpData = "TmpData";

    private static final String KEY_NAME = "site";
    private static final String KEY_AREA = "area";
    private static final String KEY_UNITNUMBER = "unit";
    private static final String KEY_UNIT = "unitName";
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
    private static final String KEY_EQUIPMENTDESCRIPTION = "equipmentDesc";
    private static final String KEY_EQUIPMENTYPE = "equipmentType";
    private static final String KEY_EQUIPMENTSIZE = "equipmentSize";
    private static final String KEY_MEASUREMENTPOSITION = "measurementPosition";
    private static final String KEY_SUBDESCRIPTION = "[Maintenance type]";
    private static final String KEY_EQUIPMENTID = "equipmentID";


    public DataClass(Context context) {
        super(context, Environment
                        .getExternalStorageDirectory()
                        + "/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment
                .getExternalStorageDirectory()
                + "/e3softData/Data/" + DATABASE_NAME, null);

    }

    public void insertGI(int leakerID, String name, String area,
                         String unitNumber, String unit, String drawing, String stream,
                         String specificLoc, String system, String equipLoc, String product,
                         String equipDesc, String subDesc, String equipType,
                         String equipSize, String equipId, String mPosition, String camOp,
                         String camSerial, String surveyOp, String surveySerial, String cycle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(KEY_NAME, name);
        values.put(KEY_AREA, area);
        values.put(KEY_UNITNUMBER, unitNumber);
        values.put(KEY_UNIT, unit);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_STREAM, stream);
        values.put(KEY_SPECIFICLOC, specificLoc);
        values.put(KEY_SYSTEM, system);
        values.put(KEY_EQUIPLOC, equipLoc);
        values.put(KEY_PRODUCT, product);
        values.put(KEY_EQUIPMENTDESCRIPTION, equipDesc);
        values.put(KEY_SUBDESCRIPTION, subDesc);
        values.put(KEY_EQUIPMENTYPE, equipType);
        values.put(KEY_EQUIPMENTSIZE, equipSize);
        values.put(KEY_EQUIPMENTID, equipId);
        values.put(KEY_MEASUREMENTPOSITION, mPosition);
        values.put(KEY_CAMOP, camOp);
        values.put(KEY_CAMSERIAL, camSerial);
        values.put(KEY_SURVEYOP, surveyOp);
        values.put(KEY_SURVEYSERIAL, surveySerial);
        values.put("equipmentClass", "NA");
        values.put("age", "New");
        values.put("cycle", cycle);

        db.insert(tblGI, null, values);
        db.close(); // Closing database connection
    }

    public void insertGIFS(String leakerID, String stationID, String stationName,
                           String drawing, String contact,
                           String product,
                           String equipDesc, String subDesc, String equipType,
                           String equipSize, String equipId, String mPosition, String camOp,
                           String camSerial, String surveyOp, String surveySerial, String cycle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put("siteID", stationID);
        values.put("site", stationName);
        values.put("drawing", drawing);
        values.put("contact", contact);

        values.put(KEY_PRODUCT, product);
        values.put("equipmentDescription", equipDesc);
        values.put("maintenanceType", subDesc);
        values.put(KEY_EQUIPMENTYPE, equipType);
        values.put(KEY_EQUIPMENTSIZE, equipSize);
        values.put(KEY_EQUIPMENTID, equipId);
        values.put(KEY_MEASUREMENTPOSITION, mPosition);

        db.insert("fillingGeneralInfo", null, values);
        db.close(); // Closing database connection
    }

    public void insertGIDE(String leakerID, String stationID,
                           String drawing,
                           String product,
                           String equipDesc, String subDesc, String equipType,
                           String equipSize, String equipId, String mPosition, String camOp,
                           String camSerial, String surveyOp, String surveySerial, String cycle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put("siteName", stationID);
        values.put("drawing", drawing);

        values.put(KEY_PRODUCT, product);
        values.put("equipmentDescription", equipDesc);
        values.put("maintenanceType", subDesc);
        values.put(KEY_EQUIPMENTYPE, equipType);
        values.put(KEY_EQUIPMENTSIZE, equipSize);
        values.put(KEY_EQUIPMENTID, equipId);
        values.put(KEY_MEASUREMENTPOSITION, mPosition);

        db.insert("fillingGeneralInfo", null, values);
        db.close(); // Closing database connection
    }

    public void insertTmpData(int leakerID, String name, String area,
                              String unitNumber, String unit, String drawing, String stream,
                              String specificLoc, String system, String equipLoc, String product,
                              String equipDesc, String subDesc, String equipType,
                              String equipSize, String equipId, String mPosition, String camOp,
                              String camSerial, String surveyOp, String surveySerial, String cycle,
                              String date, String result, String loss, String recom) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(KEY_NAME, name);
        values.put(KEY_AREA, area);
        values.put(KEY_UNITNUMBER, unitNumber);
        values.put(KEY_UNIT, unit);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_STREAM, stream);
        values.put(KEY_SPECIFICLOC, specificLoc);
        values.put(KEY_SYSTEM, system);
        values.put(KEY_EQUIPLOC, equipLoc);
        values.put(KEY_PRODUCT, product);
        values.put(KEY_EQUIPMENTDESCRIPTION, equipDesc);
        values.put(KEY_SUBDESCRIPTION, subDesc);
        values.put(KEY_EQUIPMENTYPE, equipType);
        values.put(KEY_EQUIPMENTSIZE, equipSize);
        values.put(KEY_EQUIPMENTID, equipId);
        values.put(KEY_MEASUREMENTPOSITION, mPosition);
        values.put(KEY_CAMOP, camOp);
        values.put(KEY_CAMSERIAL, camSerial);
        values.put(KEY_SURVEYOP, surveyOp);
        values.put(KEY_SURVEYSERIAL, surveySerial);
        values.put("cycle", cycle);
        values.put("date", date);
        values.put("result", result);
        values.put("loss", loss);
        values.put("recom", recom);


        db.insert(tblTmpData, null, values);
        db.close(); // Closing database connection
    }

    public void insertMAAResult(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblMAARes, null, values);
        db.close(); // Closing database connection
    }

    public void insertFSResult(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert("fillingResults", null, values);
        db.close(); // Closing database connection
    }

    public void insertDepotResult(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert("depotResults", null, values);
        db.close(); // Closing database connection
    }

    public void insertMAADates(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblMAADates, null, values);
        db.close(); // Closing database connection
    }

    public void insertMAALoss(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblMAALoss, null, values);
        db.close(); // Closing database connection
    }

    public void insertMABResult(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblMABRes, null, values);
        db.close(); // Closing database connection
    }

    public void insertMABDates(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblMABDates, null, values);
        db.close(); // Closing database connection
    }

    public void insertMABLoss(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblMABLoss, null, values);
        db.close(); // Closing database connection
    }

    public void insertSHUResult(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblSHURes, null, values);
        db.close(); // Closing database connection
    }

    public void insertSHUDates(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblSHUDates, null, values);
        db.close(); // Closing database connection
    }

    public void insertSHULoss(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LEAKER_ID, leakerID);
        values.put(resCol, result);


        db.insert(tblSHULoss, null, values);
        db.close(); // Closing database connection
    }

    public void updateReport2(String cName, String cName2, String cName3) {
        SQLiteDatabase dbr22 = this.getWritableDatabase();
        dbr22.execSQL("UPDATE TmpData SET '" + cName2 + "' = '" + cName3
                + "' WHERE leakerID = '" + cName + "'");
        Log.d("update Reports", cName);
        Log.d("update Reports", cName2);
        Log.d("update Reports", cName3);
        Log.d("update Reports", "success");
        dbr22.close(); // Closing database connection

    }

    public void updateReport3(String cName, String cName2, String cName3, String result, String loss, String recom, String product) {
        SQLiteDatabase dbr22 = this.getWritableDatabase();
        dbr22.execSQL("UPDATE TmpData SET '" + cName2 + "' = '" + cName3 + "', result = '" + result + "'"
                + ", loss = '" + loss + "'" + ", recom = '" + recom + "'" + ", product = '" + product + "'"
                + " WHERE leakerID = '" + cName + "'");
        Log.d("update Reports", cName);
        Log.d("update Reports", cName2);
        Log.d("update Reports", cName3);
        Log.d("update Reports", result);
        Log.d("update Reports", loss);
        Log.d("update Reports", recom);
        Log.d("update Reports", "success");
        dbr22.close(); // Closing database connection

    }

    public void updateFSRes(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE fillingResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateDepotRes(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE depotResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMaaRes(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE maaResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMabRes(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE mabResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateShuRes(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE shuResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMaaResRep(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE maaResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateFSResRep(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE fillingResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateDepotResRep(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE depotResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMabResRep(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE mabResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateShuResRep(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE shuResults SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateFSLoss(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE fillingLoss SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateDepotLoss(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE DepotLoss SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMaaLoss(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE maaLoss SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMabLoss(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE mabLoss SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateShuLoss(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE shuLoss SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateFSDates(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE fillingCycleDates SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateDepotDates(String leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE depotCycleDates SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMaaDates(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE maaCycleDates SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateMabDates(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE mabCycleDates SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public void updateShuDates(int leakerID, String resCol, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE shuCycleDates SET " + resCol + " = '" + result + "'"
                + " WHERE leakerID = '" + leakerID + "'");
        db.close(); // Closing database connection
    }

    public List<String> getAllLeaks() {
        List<String> pLocs = new ArrayList<String>();

        String proc = "SELECT " + "TmpData.leakerID " + "FROM TmpData "
                + "WHERE TmpData.leakerID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pLocs.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pLocs;
    }

    public List<String> getAllLeaks2() {
        List<String> pLocs = new ArrayList<String>();

        String proc = "SELECT " + "TmpData.leakerID " + "FROM TmpData "
                + "WHERE TmpData.leakerID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pLocs.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pLocs;
    }

    public List<String> viewUnit2() {
        String area = HLeaker.refinery;
        area = "'" + area + "'";
        Log.d("area ", area);
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "generalInfo.unit " + "FROM generalInfo"
                + " WHERE generalInfo.site = "
                + area + "GROUP BY generalInfo.unit";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> viewDepots() {
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "depotGeneralInfo.leakerID " + "FROM depotGeneralInfo"
                + " GROUP BY depotGeneralInfo.leakerID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> viewStations() {
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "fillingGeneralInfo.leakerID " + "FROM fillingGeneralInfo "
                + "GROUP BY fillingGeneralInfo.leakerID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> viewUnit2R() {
        String area = Repairs.refinery;
        area = "'" + area + "'";
        Log.d("area ", area);
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "generalInfo.unit " + "FROM generalInfo"
                + " WHERE generalInfo.site = "
                + area + "GROUP BY generalInfo.unit";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> viewLeaks() {
        String area = HLeaker.uni;
        area = "'" + area + "'";
        Log.d("area ", area);
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "generalInfo.leakerID " + "FROM generalInfo"
                + " WHERE generalInfo.unit = "
                + area + "GROUP BY generalInfo.leakerID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> viewLeaksR() {
        String area = Repairs.uni;
        area = "'" + area + "'";
        Log.d("area ", area);
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "generalInfo.leakerID " + "FROM generalInfo"
                + " WHERE generalInfo.unit = "
                + area + "GROUP BY generalInfo.leakerID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> getAllMeasurementPositions() {
        List<String> pMPosition = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM generalInfo GROUP BY generalInfo.measurementPosition";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pMPosition.add(cursor.getString(21));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pMPosition;
    }

    public List<String> getAllEquipmentType() {
        List<String> pEquipmentType = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT generalInfo.equipmentType FROM generalInfo GROUP BY generalInfo.equipmentType";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pEquipmentType.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return pEquipmentType;
    }

    public void deleteTmpData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TmpData");
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
        db.execSQL("DROP TABLE IF EXISTS " + tblGI);
    }
}
