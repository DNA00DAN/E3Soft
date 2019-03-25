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

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "enserve3DatabaseV1.0";
    // Labels table name

    private static final String tblCamOperator = "cameraOperator";
    private static final String tblCamSerial = "cameraSerial";
    private static final String tblSurveyOperator = "surveyOperator";
    private static final String tblSurveySerial = "surveySerial";
    private static final String tblOperatorDetails = "operatorDetails";

    private static final String tblPlantDescription = "plantDescription";
    private static final String tblSite = "site";
    private static final String tblArea = "area";
    private static final String tblUnit = "units";
    private static final String tblDrawing = "drawing";
    private static final String tblStream = "stream";

    private static final String tblEquipmentDescription = "equipmentDescription";
    private static final String tblEquipmentType = "equipmentType";
    private static final String tblEquipmentSize = "equipmentSize";
    private static final String tblEquipmentClass = "equipmentClass";
    private static final String tblMeasurementPosition = "measurementPosition";
    private static final String tblPlantSurvey = "plantSurvey";
    private static final String tblReports = "reports";
    private static final String tblHighLeaker = "highLeaker";

    // Labels Table Columns names
    private static final String KEY_NAME = "site";
    private static final String KEY_AREA = "area";
    private static final String KEY_UNITNUMBER = "unitNumber";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_UNIT_SIZE = "unitSize";
    private static final String KEY_DRAWING = "drawing";
    private static final String KEY_EQUIPLOC = "equipmentLocation";
    private static final String KEY_SPECIFICLOC = "specificLocation";
    private static final String KEY_STREAM = "stream";
    private static final String KEY_SYSTEM = "systemLocation";
    private static final String KEY_LEAKER_ID = "leakerID";

    private static final String KEY_CAMOP = "cameraOperator";
    private static final String KEY_CAMSERIAL = "cameraSerial";
    private static final String KEY_SURVEYOP = "surveyOperator";
    private static final String KEY_SURVEYSERIAL = "surveySerial";

    private static final String KEY_PRODUCT = "product";
    private static final String KEY_EQUIPMENTDESCRIPTION = "equipmentDescription";
    private static final String KEY_EQUIPMENTYPE = "equipmentType";
    private static final String KEY_EQUIPMENTSIZE = "equipmentSize";
    private static final String KEY_EQUIPMENTCLASS = "equipmentClass";
    private static final String KEY_MEASUREMENTPOSITION = "measurementPosition";
    private static final String KEY_SUBDESCRIPTION = "subDescription";
    private static final String KEY_EQUIPMENTID = "equipmentId";
    private static final String KEY_MEASUREMENTRESULT = "measurementResult";
    private static final String KEY_READING = "reading";
    private static final String KEY_BACKGROUND = "background";
    private static final String KEY_ANNUALLOSS = "annualLoss";
    private static final String KEY_RECOMEDATION = "recommendation";
    private static final String KEY_CYCLE = "cycle";
    private static final String KEY_IMAGE = "image";

    private static final String KEY_DATECREATE = "datecreate";
    private static final String KEY_DATEMOD = "dateMod";
    public static Current_Site sites;

    public String site, area, unit, drawing, stream;
    public String day;

    public DatabaseHandler(Context context) {
        super(context, Environment
                        .getExternalStorageDirectory()
                        + "/e3softData/Data/" + DATABASE_NAME, null,
                DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment
                .getExternalStorageDirectory()
                + "/e3softData/Data/" + DATABASE_NAME, null);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key constraints
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    public void insertPlantDesc(String name, String area, String unitNumber,
                                String unit, String drawing, String stream, String leakerID,
                                String specificLoc, String system, String equipLoc,
                                String dateCreate, String dateMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_AREA, area);
        values.put(KEY_UNITNUMBER, unitNumber);
        values.put(KEY_UNIT, unit);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_STREAM, stream);
        values.put(KEY_LEAKER_ID, leakerID);
        values.put(KEY_SPECIFICLOC, specificLoc);
        values.put(KEY_SYSTEM, system);
        values.put(KEY_EQUIPLOC, equipLoc);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);

        // Inserting Row
        db.insert(tblPlantDescription, null, values);
        db.close(); // Closing database connection
    }

    public void insertDrawing(String unit, String drawing, String dateCreate,
                              String dateMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UNITNUMBER, unit);
        values.put(KEY_DRAWING, drawing);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);

        // Inserting Row
        db.insert(tblDrawing, null, values);
        db.close(); // Closing database connection
    }

    public void insertStream(String drawingID, String Stream,
                             String dateCreate, String dateMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UNITNUMBER, drawingID);
        values.put(KEY_STREAM, Stream);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);

        // Inserting Row
        db.insert(tblStream, null, values);
        db.close(); // Closing database connection
    }

    public void insertCamOp(String op, String dateCreate, String dateMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAMOP, op);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);

        // Inserting Row
        db.insert(tblCamOperator, null, values);
        db.close(); // Closing database connection
    }


    public void insertHighLeaker(String name, String area, String unitNumber,
                                 String unit, String unitSize, String drawing, String stream,
                                 String leakerID, String specificLoc, String system,
                                 String equipLoc, String camOp, String camSerial, String surveyOp,
                                 String surveySerial, String product, String equipDesc,
                                 String subDesc, String equipType, String equipSize, String equipId,
                                 String mPosition, String mResult, String reading, String backG,
                                 String aLoss, String recom, String dateCreate, String dateMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
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
        values.put(KEY_MEASUREMENTRESULT, mResult);
        values.put(KEY_READING, reading);
        values.put(KEY_BACKGROUND, backG);
        values.put(KEY_ANNUALLOSS, aLoss);
        values.put(KEY_RECOMEDATION, recom);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);

        // Inserting Row
        db.insert(tblHighLeaker, null, values);
        db.close(); // Closing database connection
    }

    public void insertPlantSurvey(String product, String equipDesc,
                                  String subDesc, String equipType, String equipSize,
                                  String equipClass, String equipId, String mPosition,
                                  String mResult, String reading, String background,
                                  String recomedation, String dateCreate, String dateMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
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
        values.put(KEY_BACKGROUND, background);
        values.put(KEY_RECOMEDATION, recomedation);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);

        // Inserting Row
        db.insert(tblPlantSurvey, null, values);
        db.close(); // Closing database connection
    }

    public void insertReports(String name, String area, String unitNumber,
                              String unit, String unitSize, String drawing, String stream,
                              String leakerID, String specificLoc, String system,
                              String equipLoc, String camOp, String camSerial, String surveyOp,
                              String surveySerial, String product, String equipDesc,
                              String subDesc, String equipType, String equipSize,
                              String equipClass, String equipId, String mPosition,
                              String mResult, String reading, String backG, String aLoss,
                              String recom, String img, String dateCreate, String dateMod,
                              String cyc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
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
        values.put(KEY_EQUIPMENTCLASS, equipClass);
        values.put(KEY_EQUIPMENTID, equipId);

        values.put(KEY_MEASUREMENTPOSITION, mPosition);
        values.put(KEY_MEASUREMENTRESULT, mResult);
        values.put(KEY_READING, reading);
        values.put(KEY_BACKGROUND, backG);
        values.put(KEY_ANNUALLOSS, aLoss);
        values.put(KEY_RECOMEDATION, recom);
        values.put(KEY_IMAGE, img);
        values.put(KEY_DATECREATE, dateCreate);
        values.put(KEY_DATEMOD, dateMod);
        values.put(KEY_CYCLE, cyc);
        // Inserting Row
        db.insert(tblReports, null, values);
        db.close(); // Closing database connection
    }

    public void updateReport2(String cName, String cName2, String cName3) {
        SQLiteDatabase dbr22 = this.getWritableDatabase();
        dbr22.execSQL("UPDATE reports SET '" + cName2 + "' = '" + cName3
                + "' WHERE leakerID = '" + cName + "'");
        Log.d("update Reports", cName);
        Log.d("update Reports", cName2);
        Log.d("update Reports", cName3);
        Log.d("update Reports", "success");
        dbr22.close(); // Closing database connection

    }

    public List<String> viewPLocation() {
        day = Local_Marketing.seqT;
        day = "'" + day + "'";
        Log.d("day", day);
        List<String> pLocs = new ArrayList<String>();

        String proc = "SELECT " + "ls.day, ls.site " + "FROM localSite ls "
                + "INNER JOIN localDay da ON ls.day=da.day "
                + "WHERE da.day = " + day;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pLocs.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pLocs;
    }

    public List<String> viewArea() {
        site = NewLeaks.siteT;
        site = "'" + site + "'";
        Log.d("site ", site);
        List<String> pAreas = new ArrayList<String>();

        String proc = "SELECT " + "ar.site, ar.area " + "FROM area ar "
                + "INNER JOIN site si ON ar.site=si.site " + "WHERE si.site = "
                + site;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pAreas.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pAreas;
    }

    public List<String> viewUnit() {
        area = NewLeaks.areaT;
        area = "'" + area + "'";
        Log.d("area ", area);
        List<String> pUnits = new ArrayList<String>();

        String proc = "SELECT " + "un.area, un.unitNumber " + "FROM units un "
                + "INNER JOIN area ar ON un.area=ar.area" + " WHERE ar.area = "
                + area;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pUnits;
    }

    public List<String> viewDrawing() {
        unit = NewLeaks.unitNumberT;
        unit = "'" + unit + "'";
        Log.d("unit ", unit);
        List<String> pDrawing = new ArrayList<String>();

        String proc = "SELECT " + "dr.unitNumber, dr.drawing "
                + "FROM drawing dr "
                + "INNER JOIN units un ON dr.unitNumber=un.unitNumber "
                + " WHERE un.unitNumber = " + unit;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pDrawing.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pDrawing;
    }

    public List<String> viewStreams() {
        unit = NewLeaks.unitNumberT;
        unit = "'" + unit + "'";
        Log.d("unit ", unit);
        List<String> pStream = new ArrayList<String>();

        String proc = "SELECT " + "str.unitNumber, str.stream "
                + "FROM stream str "
                + "INNER JOIN units un ON str.unitNumber=un.unitNumber "
                + " WHERE un.unitNumber = " + unit;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        if (cursor.moveToFirst()) {
            do {
                pStream.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        return pStream;
    }

    public List<String> getAllProducts() {

        stream = Inspection.streamT;
        stream = "'" + stream + "'";
        Log.d("streams ", stream);

        List<String> pProducts = new ArrayList<String>();

        String proc = "SELECT " + "pr.stream, pr.product " + "FROM product pr "
                + "INNER JOIN stream str ON pr.stream=str.stream "
                + "WHERE pr.stream = " + stream;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(proc, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pProducts.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pProducts;
    }

    /**
     * Getting all labels returns list of labels
     */
    public List<String> getAllPSites() {
        List<String> pSites = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblSite;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pSites.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pSites;
    }

    public List<String> getAllAreas() {
        List<String> pAreas = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * " + "FROM " + tblArea;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pAreas.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return pAreas;
    }

    public List<String> getAllUnits() {
        List<String> pUnits = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblUnit;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pUnits.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pUnits;
    }

    public List<String> getAllDrawings() {
        List<String> pDrawings = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblDrawing;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pDrawings.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pDrawings;
    }

    public List<String> getAllStreams() {
        List<String> pStreams = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblStream;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pStreams.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pStreams;
    }

    public List<String> getAllPSurveys() {
        List<String> pSurveys = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblPlantDescription;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pSurveys.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pSurveys;
    }

    public List<String> getAllCamOps() {
        List<String> pCamOps = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblCamOperator;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pCamOps.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pCamOps;
    }

    public List<String> getAllCamSerials() {
        List<String> pCamSerials = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblCamSerial;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pCamSerials.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pCamSerials;
    }

    public List<String> getAllSurveyOps() {
        List<String> pSOp = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblSurveyOperator;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pSOp.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pSOp;
    }

    public List<String> getAllSurveySerials() {
        List<String> pSSerials = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblSurveySerial;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pSSerials.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pSSerials;
    }

    public List<String> getAllOperatorDetails() {
        List<String> pOpDetails = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblOperatorDetails;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pOpDetails.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pOpDetails;
    }

    public List<String> getAllEquipmentDescription() {
        List<String> pEquipmentDesc = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblEquipmentDescription;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pEquipmentDesc.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pEquipmentDesc;
    }

    public List<String> getAllEquipmentType() {
        List<String> pEquipmentType = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblEquipmentType;

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

        // returning lables
        return pEquipmentType;
    }

    public List<String> getAllEquipmentSize() {
        List<String> pEquipmentSize = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblEquipmentSize;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pEquipmentSize.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pEquipmentSize;
    }

    public List<String> getAllEquipmentClass() {
        List<String> pEquipmentClass = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblEquipmentClass;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pEquipmentClass.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pEquipmentClass;
    }

    public List<String> getAllMeasurementPositions() {
        List<String> pMPosition = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblMeasurementPosition;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pMPosition.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pMPosition;
    }


    public List<String> getAllPlantSurveys() {
        List<String> pPlantSurvey = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tblPlantSurvey;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pPlantSurvey.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pPlantSurvey;
    }

    public List<String> getAllReports() {
        List<String> pReports = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM  reports ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pReports.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pReports;
    }

    public List<String> viewDay() {
        List<String> pDay = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM  localDay ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                pDay.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return pDay;
    }

    public void updateProduct(String leakerIDs, String addPro,
                              String addStream, String addCf) {
        SQLiteDatabase dbr2 = this.getWritableDatabase();
        dbr2.execSQL("UPDATE product SET " + "product" + " = '" + addPro
                + "', " + "stream" + " = '" + addStream + "', " + "cf" + " = '"
                + addCf + "'" + " WHERE stream = '" + addStream + "'");

        Log.d("update products", leakerIDs);
        Log.d("update products", addPro);
        Log.d("update products", addStream);
        Log.d("update products", addCf);
        Log.d("update products", "success");
        dbr2.close(); // Closing database connection
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
