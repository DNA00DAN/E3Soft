package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class NewLeaks extends Activity {

    final static int img = 1;
    public static String siteT, areaT, unitNumberT, unitT, drawingT, unitSizeT,
            equipmentLocT, specificLocT, streamT, systemT = "NA", dateCreateT,
            dateModT, leaker = "0";
    public static String ste, addProT, addCfT, addStreamT;
    public static String products = "";
    public static String data = "", data2 = "", byteImage;
    static int idsss = 0;
    private static String sites = "", areas = "", units = "", drawings = "",
            streams = "";
    private static String data3 = "", data4 = "", data5 = "", data6 = "",
            data7 = "", data8 = "";
    private static Integer siteIDs = 0, areaIDs = 0, unitIDs = 0,
            drawingIDs = 0, tmpUnitNum = 0, areaIDs2 = 0, unitIDs2 = 0,
            drawingIDs2 = 0;
    private static TableRow lRow;
    Bitmap imageTa;
    byte[] byteArray;
    private String productTt;
    private Button updateB, backB, clearB, loadL;
    private EditText addDrawingTxt;
    private EditText addUnitNumberTxt;
    private EditText addSpecificTxt;
    private EditText addStreamTxt;
    private EditText addSystemTxt;
    private EditText addEquipLocTxt;
    private EditText addUnitNameTxt;
    private EditText addUnitSizeTxt;
    private EditText addPro1;
    private EditText addcf1;
    private EditText addLeakerId;
    private EditText emergencyTxt;
    private EditText heightTxt;
    private Button addSite, addDrawing, addArea, addUnitNumber,
            addStreamLocation;
    private Spinner site, drawing, area, unitNumber, streamLocation,
            streamLocation2, level, loc1, loc2, sysFor, sysFor1, sysFor2;
    private String sitePressed, drawingPressed, areaPressed, unitNumberPressed,
            streamLocationPressed, productTT, cfTT, cycTt, heightT, levelT;
    private String currentActivityName, activityName, currentDate,
            emerT = "not";
    private TextView unitName, unitSize, uName, uSize, uNum, unitN, pro1, cf1,
            exT, prodt;
    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT;
    private CheckBox emer;
    private Spinner equipDesc, equipType, equipSize, mPosition, subDesc;
    private EditText equipID, mResult, backGr;
    private TextView date;
    private RadioButton ppm, lel, gas;
    private String reading, correctedReading, correctedR;
    private String strMResult, strBackG, strCf, strMRes, DATE_TAKEN,
            strRecomedation;
    private String productT, equipmentDescT, subDescriptionT, equipmentTypeT,
            readingT = "PPM", equipmentSizeT, equipmentClassT, equipmentIdT,
            mPositionT;
    private Double cf = 0.0, mRes = 0.0, lRa = 0.0, backG = 0.0, sv = 0.0,
            correctedReading1;
    private TableRow sysF1, sysF2;

    private TextView sFU1, sFU2;

    private Spinner sysFor11, sysFor12, sysFor13, sysFor14;
    private Spinner sysFor21, sysFor22, sysFor23, sysFor24;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newleaks);

        try {
            initialiseComponents();
            loadL();
            populateSpinners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadL.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leaker = addLeakerId.getText().toString().trim();
                if (leaker.length() > 0) {
                    Integer tmpLeaks = Integer.parseInt(leaker);
                    tmpLeaks++;
                    leaker = tmpLeaks.toString();
                    addLeakerId.setText(leaker);
                    loadL.setEnabled(false);
                    updateB.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Starting Number", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        updateB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (userDetails.camTech.equals(userDetails.surveyTech)) {
                    noteUnchangedData();
                } else {

                    Double tmpR = Double.parseDouble(mResult.getText().toString());
                    if (tmpR < 1.0) {
                        Toast.makeText(getApplicationContext(), "NO ZERO VALUES", Toast.LENGTH_LONG).show();
                    } else {
                        calculateBackG();
                        populateVariables();
                        calculateLoss();
                        if (equipDesc.getSelectedItem().toString().equals("Choose 1")
                                || equipType.getSelectedItem().toString()
                                .equals("Choose 1")
                                || equipSize.getSelectedItem().toString()
                                .equals("Choose 1")
                                || mPosition.getSelectedItem().toString()
                                .equals("Choose 1")) {
                            Toast.makeText(getApplicationContext(),
                                    "Complete All Dropdowns", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            insertGIData();
                            insertResData();
                            insertCycleLoss();
                            insertCycleDates();
                            insertTmpData();
                            saveLe();
                            clearRes();
                            emer.requestFocus();
                            Intent i = new Intent(NewLeaks.this, NewReports.class);
                            startActivity(i);
                        }
                    }

                }
            }

            private void saveLe() {
                try {
                    File myFile = new File("/sdcard/e3softData/Data/leaker.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(
                            fOut);
                    myOutWriter.write(leaker);
                    myOutWriter.close();
                    fOut.close();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            private void clearRes() {
                mResult.setText("");
                ppm.setVisibility(View.VISIBLE);
                lel.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);
                addLeakerId.requestFocus();
                addLeakerId.setEnabled(false);
                addLeakerId.requestFocus();
                loadL.setEnabled(true);
                updateB.setEnabled(false);
            }
        });

        sysFor.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (sysFor.getSelectedItem().equals("V-00-000-A")) {
                    sysF1.setVisibility(View.VISIBLE);
                    sysF2.setVisibility(View.GONE);
                } else if (sysFor.getSelectedItem().equals("0E-000-A")) {
                    sysF2.setVisibility(View.VISIBLE);
                    sysF1.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        site.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                siteT = site.getSelectedItem().toString();

                loadAreaSpinner();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        area.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                areaT = area.getSelectedItem().toString();
                loadUnitSpinner();
                loadStreamSpinner();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        unitNumber.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                unitNumberT = unitNumber.getSelectedItem().toString();
                calculateUnitName();
                loadDrawingSpinner();
                drawing.setEnabled(true);
                sFU1.setText(unitNumberT);
                sFU2.setText(unitNumberT);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        drawing.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                drawingT = drawing.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        streamLocation.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                streamT = streamLocation.getSelectedItem().toString();
                loadProductName();
                loadCf();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        streamLocation2.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                streamT = streamLocation2.getSelectedItem().toString();
                loadProductName();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ppm.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                reading = "PPM";
                lel.setVisibility(View.GONE);
                gas.setVisibility(View.GONE);

            }

        });

        lel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                reading = "LEL";
                ppm.setVisibility(View.GONE);
                gas.setVisibility(View.GONE);
            }
        });

        gas.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                                    reading = "GAS %";
                lel.setVisibility(View.GONE);
                ppm.setVisibility(View.GONE);
            }
        });

        addDrawing.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                drawingT = addDrawingTxt.getText().toString();
                unitNumberT = unitNumber.getSelectedItem().toString();
                if (addDrawingTxt.isShown()) {
                    addDrawingTxt.setVisibility(View.GONE);
                    addDrawing.setBackgroundResource(R.drawable.edit);
                    addDrawing.setText("ADD");
                    if (drawingT.trim().length() > 0) {
                        // database handler
                        DatabaseHandler db = new DatabaseHandler(
                                getApplicationContext());

                        // inserting new label into database
                        db.insertDrawing(unitNumberT, drawingT, dateCreateT,
                                dateModT);

                        // making input filed text to blank
                        addDrawingTxt.setText("");

                        Toast.makeText(getApplicationContext(), "Saved",
                                Toast.LENGTH_SHORT).show();

                        // Hiding the keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(
                                addDrawingTxt.getWindowToken(), 0);

                        // loading spinner with newly added data
                        loadDrawingSpinner();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter Value", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    addDrawingTxt.setVisibility(View.VISIBLE);
                    addDrawing.setBackgroundResource(R.drawable.save);
                    addDrawing.setText("SAVE");
                }
            }

        });

        addStreamLocation.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (streamLocation2.isShown()) {
                    String tmpAr = area.getSelectedItem().toString();
                    streamLocationPressed = streamLocation2.getSelectedItem()
                            .toString();
                    getProduct();
                    addStreamLocation.setBackgroundResource(R.drawable.edit);
                    addStreamLocation.setText("ADD");
                    streamLocation2.setVisibility(View.GONE);
                    // database handler
                    StreamData db = new StreamData(getApplicationContext());
                    // inserting new label into database
                    db.insertStream(tmpAr, streamLocationPressed, productTt);

                    Toast.makeText(getApplicationContext(), "Saved",
                            Toast.LENGTH_SHORT).show();

                    // loading spinner with newly added data
                    loadStreamSpinner();

                } else {
                    addStreamLocation.setBackgroundResource(R.drawable.save);
                    addStreamLocation.setText("SAVE");
                    streamLocation2.setVisibility(View.VISIBLE);
                    popStreams();
                }
            }
        });

        clearB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                lel.setVisibility(View.VISIBLE);
                ppm.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void noteUnchangedData() {
        new AlertDialog.Builder(this)
                .setMessage("Please Complete Technician Details & Cycle Details")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                .setNeutralButton("HOME",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(NewLeaks.this, Home.class);
                                startActivity(i);
                                finish();
                            }
                        }).setNegativeButton("NO", null).show();
    }

    protected void insertTmpData() {
        DataClass db = new DataClass(getApplicationContext());
        db.insertTmpData(Integer.parseInt(leaker), siteT, areaT, unitNumberT,
                unitT, drawingT, streamT, specificLocT, systemT, equipmentLocT,
                productT, equipmentDescT, subDescriptionT, equipmentTypeT,
                equipmentSizeT, equipmentIdT, mPositionT, userDetails.camTech,
                userDetails.camSerial, userDetails.surveyTech,
                userDetails.surveySerial, userDetails.cycleRes, currentDate,
                correctedReading, strMResult, strRecomedation);

    }

    protected void insertCycleDates() {
        DataClass db = new DataClass(getApplicationContext());

        if (siteT.equals("Mina Al Ahmadi")) {
            db.insertMAADates(Integer.parseInt(leaker), userDetails.cycleDate,
                    currentDate);
        } else if (siteT.equals("Mina Abdulla")) {
            db.insertMABDates(Integer.parseInt(leaker), userDetails.cycleDate,
                    currentDate);
        } else if (siteT.equals("Shuaiba")) {
            db.insertSHUDates(Integer.parseInt(leaker), userDetails.cycleDate,
                    currentDate);
        }
    }

    protected void insertCycleLoss() {
        DataClass db = new DataClass(getApplicationContext());

        if (siteT.equals("Mina Al Ahmadi")) {
            db.insertMAALoss(Integer.parseInt(leaker), userDetails.cycleLoss,
                    strMResult);
        } else if (siteT.equals("Mina Abdulla")) {
            db.insertMABLoss(Integer.parseInt(leaker), userDetails.cycleLoss,
                    strMResult);
        } else if (siteT.equals("Shuaiba")) {
            db.insertSHULoss(Integer.parseInt(leaker), userDetails.cycleLoss,
                    strMResult);
        }
    }

    protected void insertResData() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteT.equals("Mina Al Ahmadi")) {
            db.insertMAAResult(Integer.parseInt(leaker), userDetails.cycleRes,
                    correctedReading);
        } else if (siteT.equals("Mina Abdulla")) {
            db.insertMABResult(Integer.parseInt(leaker), userDetails.cycleRes,
                    correctedReading);
        } else if (siteT.equals("Shuaiba")) {
            db.insertSHUResult(Integer.parseInt(leaker), userDetails.cycleRes,
                    correctedReading);
        }
    }

    protected void insertGIData() {
        DataClass db = new DataClass(getApplicationContext());
        db.insertGI(Integer.parseInt(leaker), siteT, areaT, unitNumberT, unitT,
                drawingT, streamT, specificLocT, systemT, equipmentLocT,
                productT, equipmentDescT, subDescriptionT, equipmentTypeT,
                equipmentSizeT, equipmentIdT, mPositionT, userDetails.camTech,
                userDetails.camSerial, userDetails.surveyTech,
                userDetails.surveySerial, userDetails.cycleRes);
        Toast.makeText(getApplicationContext(), "General Info Added",
                Toast.LENGTH_SHORT).show();
    }

    private void calculateLoss() {

        mRes = Double.parseDouble(strMResult);
        sv = (Calculations.calculateSV(mRes, cf, reading));
        correctedReading1 = sv;
        calculateRecomedation();
        correctedReading = correctedReading1.toString();
        correctedR = "PPM";


        strMResult = Calculations.calculateLossBySource(sv, mPositionT);

        Log.d("Calculate Loss: ", correctedReading);
        Log.d("Calculate Loss: ", strRecomedation);
        Log.d("Calculate Loss: ", strMResult);
    }

    private void calculateRecomedation() {
        if (correctedReading1 >= 20000) {
            strRecomedation = "DANGER";
        } else if (correctedReading1 >= 10000) {
            strRecomedation = "URGENT";
        } else if (correctedReading1 >= 5000) {
            strRecomedation = "Maintenance Required";
        } else if (correctedReading1 > 1000) {
            strRecomedation = "Consider Maintenance";
        } else if (correctedReading1 <= 1000) {
            strRecomedation = "Acceptable";
        }
    }

    private void calculateBackG() {

        strBackG = backGr.getText().toString();

        if (strBackG == null || strBackG.isEmpty()) {
            backG = 0.0;
        } else {
            backG = Double.parseDouble(strBackG);
        }
    }

    protected void populateVariables() {
        siteT = site.getSelectedItem().toString();
        areaT = area.getSelectedItem().toString();
        unitNumberT = unitNumber.getSelectedItem().toString();
        unitT = unitName.getText().toString();
        drawingT = drawing.getSelectedItem().toString();
        streamT = streamLocation.getSelectedItem().toString();
        productT = prodt.getText().toString();
        specificLocT = addSpecificTxt.getText().toString();
        equipmentLocT = addEquipLocTxt.getText().toString();
        leaker = addLeakerId.getText().toString().trim();
        heightT = heightTxt.getText().toString();
        levelT = level.getSelectedItem().toString();

        String loc1ss = loc1.getSelectedItem().toString();
        String loc2ss = loc2.getSelectedItem().toString();
        equipmentLocT = loc1ss + " " + loc2ss + " of " + equipmentLocT + " H:"
                + heightT + "m" + " LV: " + levelT;

        if (sysFor.getSelectedItem().equals("V-00-000-A")) {
            if (!sysFor14.getSelectedItem().toString().equals("NA")) {
                systemT = sysFor1.getSelectedItem().toString() + "-"
                        + unitNumberT + "-"
                        + sysFor11.getSelectedItem().toString()
                        + sysFor12.getSelectedItem().toString()
                        + sysFor13.getSelectedItem().toString() + "-"
                        + sysFor14.getSelectedItem().toString();
            } else {
                systemT = sysFor1.getSelectedItem().toString() + "-"
                        + unitNumberT + "-"
                        + sysFor11.getSelectedItem().toString()
                        + sysFor12.getSelectedItem().toString()
                        + sysFor13.getSelectedItem().toString();
            }
        } else if (sysFor.getSelectedItem().equals("0E-000-A")) {
            if (!sysFor24.getSelectedItem().toString().equals("NA")) {
                systemT = sysFor2.getSelectedItem().toString() + "-"
                        + unitNumberT + "-"
                        + sysFor21.getSelectedItem().toString()
                        + sysFor22.getSelectedItem().toString()
                        + sysFor23.getSelectedItem().toString() + "-"
                        + sysFor24.getSelectedItem().toString();
            } else {
                systemT = sysFor2.getSelectedItem().toString() + "-"
                        + unitNumberT + "-"
                        + sysFor21.getSelectedItem().toString()
                        + sysFor22.getSelectedItem().toString()
                        + sysFor23.getSelectedItem().toString();
            }
        }

        equipmentDescT = equipDesc.getSelectedItem().toString();
        equipmentTypeT = equipType.getSelectedItem().toString();
        equipmentSizeT = equipSize.getSelectedItem().toString();
        equipmentClassT = "NA";
        equipmentIdT = equipID.getText().toString();
        mPositionT = mPosition.getSelectedItem().toString();
        strMResult = mResult.getText().toString();
        strMRes = strMResult;
        currentDate = date.getText().toString();
        subDescriptionT = subDesc.getSelectedItem().toString();

        if (equipID.getText().toString().equals("")) {
            equipmentIdT = "NA";
        }

        Log.d("Populate Variables: ", leaker);
        Log.d("Populate Variables: ", siteT);
        Log.d("Populate Variables: ", areaT);
        Log.d("Populate Variables: ", unitNumberT);
        Log.d("Populate Variables: ", unitT);
        Log.d("Populate Variables: ", drawingT);
        Log.d("Populate Variables: ", systemT);
        Log.d("Populate Variables: ", streamT);
        Log.d("Populate Variables: ", productT);
        Log.d("Populate Variables: ", specificLocT);
        Log.d("Populate Variables: ", equipmentLocT);
        Log.d("Populate Variables: ", equipmentDescT);
        Log.d("Populate Variables: ", subDescriptionT);
        Log.d("Populate Variables: ", equipmentTypeT);
        Log.d("Populate Variables: ", equipmentSizeT);
        Log.d("Populate Variables: ", equipmentClassT);
        Log.d("Populate Variables: ", equipmentIdT);
        Log.d("Populate Variables: ", mPositionT);
        Log.d("Populate Variables: ", strMResult);
        Log.d("Populate Variables: ", backGr.getText().toString());
        Log.d("Populate Variables: ", currentDate);

    }

    protected void loadCf() {

        data2 = "";
        String prod2 = "";

        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/newPros.db", null, 0);

        Cursor c2 = db.rawQuery(
                "SELECT pr.cf FROM nwPros pr where pr.stream = " + "'"
                        + streamT + "'", null);
        int pro2 = c2.getColumnIndex("cf");
        c2.moveToLast();

        // looping through all rows and adding to list
        if (c2 != null) {
            do {
                prod2 = c2.getString(pro2);
                data2 = data2 + prod2;
                cf = Double.parseDouble(data2);
                strCf = cf.toString();
                Log.d("cf", strCf);
            } while (c2.moveToNext());
        }

        // closing connection
        c2.close();
        db.close();
    }

    protected void loadProductName() {
        data4 = "";
        String prod4 = "";
        String stre = "'" + streamT + "'";
        SQLiteDatabase db4 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/newPros.db", null, 0);

        Cursor c4 = db4.rawQuery(
                "SELECT pr.product FROM nwPros pr where pr.stream = " + stre,
                null);
        int pro4 = c4.getColumnIndex("product");
        c4.moveToFirst();

        // looping through all rows and adding to list
        if (c4.getCount() > 0) {
            do {
                prod4 = c4.getString(pro4);
                data4 = data4 + prod4;
                prodt.setText(data4);

            } while (c4.moveToNext());
        } else {
            prodt.setText(data4);
            Log.d("Product: ", data4 + "EMPTY");
        }

        // closing connection
        c4.close();
        db4.close();
    }

    private String calculateUnitName() {
        data3 = "";
        String prod3 = "";
        unitNumberT = unitNumber.getSelectedItem().toString();
        String unitNumberT2 = "'" + unitNumberT + "'";
        SQLiteDatabase db2 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/enserve3DatabaseV1.0", null, 0);

        Cursor c3 = db2
                .rawQuery(
                        "SELECT un.unit FROM units un , area ar where un.area = ar.area AND un.unitNumber= "
                                + unitNumberT2, null);
        int pro3 = c3.getColumnIndex("unit");
        c3.moveToFirst();

        // looping through all rows and adding to list
        if (c3.getCount() > 0) {
            do {
                prod3 = c3.getString(pro3);
                data3 = data3 + prod3;
                unitName.setVisibility(View.VISIBLE);
                unitName.setText(data3);
            } while (c3.moveToNext());
        } else {
            unitName.setVisibility(View.VISIBLE);
            unitName.setText(data3);
            Log.d("unitName", data3 + "EMPTY");
        }

        // closing connection
        c3.close();
        db2.close();
        return data3;
    }

    private void loadAreaSpinner() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        try {
            List<String> pArea = db.viewArea();

            ArrayAdapter<String> dataAdapterArea = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item, pArea);

            dataAdapterArea
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            area.setAdapter(dataAdapterArea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadUnitSpinner() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        try {
            List<String> pUnit = db.viewUnit();

            ArrayAdapter<String> dataAdapterUnit = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item, pUnit);

            dataAdapterUnit
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            unitNumber.setAdapter(dataAdapterUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadDrawingSpinner() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        try {
            List<String> pDrawing = db.viewDrawing();

            ArrayAdapter<String> dataAdapterDrawing = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pDrawing);

            dataAdapterDrawing
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            drawing.setAdapter(dataAdapterDrawing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadStreamSpinner() {
        StreamData db = new StreamData(getApplicationContext());

        // Spinner Drop down elements
        try {
            List<String> pStreams = db.getAllStreams(areaT);
            ArrayAdapter<String> dataAdapterStream = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pStreams);
            dataAdapterStream
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            streamLocation.setAdapter(dataAdapterStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateSpinners() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        DataClass db2 = new DataClass(getApplicationContext());

        // Spinner Drop down elements
        List<String> pSites = db.getAllPSites();

        List<String> levels = new ArrayList<String>();
        levels.add("Ground");
        levels.add("Level 1");
        levels.add("Level 2");
        levels.add("Level 3");
        levels.add("Level 4");
        levels.add("Level 5");

        List<String> loc1s = new ArrayList<String>();
        loc1s.add("distance");
        loc1s.add("0m");
        loc1s.add("0.5m");
        loc1s.add("1m");
        loc1s.add("2m");
        loc1s.add("3m");
        loc1s.add("4m");
        loc1s.add("5m");
        loc1s.add("6m");
        loc1s.add("7m");
        loc1s.add("8m");
        loc1s.add("9m");
        loc1s.add("10m");

        List<String> loc2s = new ArrayList<String>();
        loc2s.add("direction");
        loc2s.add("Top");
        loc2s.add("Below");
        loc2s.add("N");
        loc2s.add("NE");
        loc2s.add("NW");
        loc2s.add("S");
        loc2s.add("SE");
        loc2s.add("SW");
        loc2s.add("E");
        loc2s.add("W");

        List<String> formats = new ArrayList<String>();
        formats.add("V-00-000-A");
        formats.add("0E-000-A");

        List<String> format1 = new ArrayList<String>();
        format1.add("");

        // Creating adapter for spinner

        ArrayAdapter<String> dataAdapterSite = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pSites);

        ArrayAdapter<String> dataAdapterLevels = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, levels);

        ArrayAdapter<String> dataAdapterLoc1s = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, loc1s);

        ArrayAdapter<String> dataAdapterLoc2s = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, loc2s);

        ArrayAdapter<String> dataAdapterformats = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, formats);

        // Drop down layout style - list view with radio button
        dataAdapterSite
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterLevels
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterLoc1s
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterLoc2s
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterformats
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        site.setAdapter(dataAdapterSite);

        for (int i = 0; i < dataAdapterSite.getCount(); i++) {
            System.out.println(dataAdapterSite.getItem(i).toString());
        }
        level.setAdapter(dataAdapterLevels);
        loc1.setAdapter(dataAdapterLoc1s);
        loc2.setAdapter(dataAdapterLoc2s);
        sysFor.setAdapter(dataAdapterformats);

        List<String> pEquipmentDesc = db.getAllEquipmentDescription();
        ArrayAdapter<String> dataAdapterEquipmentDesc = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentDesc);
        dataAdapterEquipmentDesc
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipDesc.setAdapter(dataAdapterEquipmentDesc);

        for (int i = 0; i < dataAdapterEquipmentDesc.getCount(); i++) {
            System.out.println(dataAdapterEquipmentDesc.getItem(i).toString());
        }

        List<String> mainTypes = new ArrayList<String>();
        mainTypes.add("MECHANICAL");
        mainTypes.add("INSTRUMENT");
        mainTypes.add("ROTATING");
        ArrayAdapter<String> dataAdaptermTypes = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, mainTypes);
        dataAdaptermTypes
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subDesc.setAdapter(dataAdaptermTypes);

        List<String> pEquipmentSize = db.getAllEquipmentSize();
        ArrayAdapter<String> dataAdapterEquipmentSize = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentSize);
        dataAdapterEquipmentSize
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipSize.setAdapter(dataAdapterEquipmentSize);

        for (int i = 0; i < dataAdapterEquipmentSize.getCount(); i++) {
            System.out.println(dataAdapterEquipmentSize.getItem(i).toString());
        }

        List<String> pEquipmentType = db.getAllEquipmentType();
        ArrayAdapter<String> dataAdapterEquipmentType = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentType);
        dataAdapterEquipmentType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipType.setAdapter(dataAdapterEquipmentType);

        for (int i = 0; i < dataAdapterEquipmentType.getCount(); i++) {
            System.out.println(dataAdapterEquipmentType.getItem(i).toString());
        }

        List<String> pMPosition = db.getAllMeasurementPositions();
        ArrayAdapter<String> dataAdapterMPosition = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pMPosition);
        dataAdapterMPosition
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPosition.setAdapter(dataAdapterMPosition);

        System.err.println("M Position Count:   " + dataAdapterMPosition.getCount());

        for (int i = 0; i < dataAdapterMPosition.getCount(); i++) {
            System.out.println(dataAdapterMPosition.getItem(i).toString());
        }

        List<String> sp1 = new ArrayList<String>();
        sp1.add("V");
        sp1.add("H");
        sp1.add("F");
        sp1.add("E");
        sp1.add("P");
        sp1.add("TK");
        sp1.add("C");
        sp1.add("TC");
        sp1.add("ST");
        sp1.add("T");
        sp1.add("N");
        sp1.add("R");
        sp1.add("BR");

        ArrayAdapter<String> dataAdapterSp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sp1);

        dataAdapterSp1
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sysFor1.setAdapter(dataAdapterSp1);

        List<String> sp21 = new ArrayList<String>();
        sp21.add("1C");
        sp21.add("2C");
        sp21.add("3C");
        sp21.add("4C");
        sp21.add("5C");

        sp21.add("1E");
        sp21.add("2E");
        sp21.add("3E");
        sp21.add("4E");
        sp21.add("5E");

        sp21.add("1F");
        sp21.add("2F");
        sp21.add("3F");
        sp21.add("4F");
        sp21.add("5F");

        sp21.add("1G");
        sp21.add("2G");
        sp21.add("3G");
        sp21.add("4G");
        sp21.add("5G");

        sp21.add("OY");
        sp21.add("1Y");
        sp21.add("2Y");
        sp21.add("3Y");
        sp21.add("4Y");
        sp21.add("5Y");

        ArrayAdapter<String> dataAdapterSp21 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sp21);

        dataAdapterSp21
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sysFor2.setAdapter(dataAdapterSp21);

        List<String> sp2 = new ArrayList<String>();
        sp2.add("0");
        sp2.add("1");
        sp2.add("2");
        sp2.add("3");
        sp2.add("4");
        sp2.add("5");
        sp2.add("6");
        sp2.add("7");
        sp2.add("8");
        sp2.add("9");

        ArrayAdapter<String> dataAdapterSp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sp2);

        dataAdapterSp2
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sysFor11.setAdapter(dataAdapterSp2);
        sysFor12.setAdapter(dataAdapterSp2);
        sysFor13.setAdapter(dataAdapterSp2);

        List<String> sp3 = new ArrayList<String>();
        sp3.add("0");
        sp3.add("1");
        sp3.add("2");
        sp3.add("3");
        sp3.add("4");
        sp3.add("5");
        sp3.add("6");
        sp3.add("7");
        sp3.add("8");
        sp3.add("9");

        ArrayAdapter<String> dataAdapterSp3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sp3);

        dataAdapterSp3
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sysFor21.setAdapter(dataAdapterSp3);
        sysFor22.setAdapter(dataAdapterSp3);
        sysFor23.setAdapter(dataAdapterSp3);

        List<String> sp5 = new ArrayList<String>();
        sp5.add("NA");
        sp5.add("A");
        sp5.add("B");
        sp5.add("C");
        sp5.add("D");
        sp5.add("E");
        sp5.add("F");
        sp5.add("A-B");
        sp5.add("B-C");
        sp5.add("A-B-C");
        sp5.add("A-B-E");
        sp5.add("C-D");
        sp5.add("D-E");
        sp5.add("E-F");
        sp5.add("D-E-F");
        sp5.add("G-H-I");
        sp5.add("J-K-L");
        sp5.add("M-N-0");
        sp5.add("P-Q-R");
        sp5.add("S-T-U");
        sp5.add("V-W-X");

        ArrayAdapter<String> dataAdapterSp5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sp5);

        dataAdapterSp5
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sysFor14.setAdapter(dataAdapterSp5);
        sysFor24.setAdapter(dataAdapterSp5);
    }

    private void initialiseComponents() {
        // Plant Details
        updateB = (Button) findViewById(R.id.submitBtnN);
        clearB = (Button) findViewById(R.id.clearBtnN);
        loadL = (Button) findViewById(R.id.updateLeakerBtnN);
        addDrawing = (Button) findViewById(R.id.addDrawingN);
        addStreamLocation = (Button) findViewById(R.id.addStreamLocationBtn);

        site = (Spinner) findViewById(R.id.siteSp);
        area = (Spinner) findViewById(R.id.areaSp);
        drawing = (Spinner) findViewById(R.id.drawingSp);
        unitNumber = (Spinner) findViewById(R.id.unitNumSp);
        streamLocation = (Spinner) findViewById(R.id.streamLocationSp);
        streamLocation2 = (Spinner) findViewById(R.id.streamLocationSp2);

        level = (Spinner) findViewById(R.id.floorSp);
        loc1 = (Spinner) findViewById(R.id.locSp1);
        loc2 = (Spinner) findViewById(R.id.locSp2);
        sysFor = (Spinner) findViewById(R.id.systemFormatSp);
        sysFor1 = (Spinner) findViewById(R.id.systemFormatSp1);
        sysFor2 = (Spinner) findViewById(R.id.systemFormatSp2);

        addSpecificTxt = (EditText) findViewById(R.id.addSpecificTxtN);
        addEquipLocTxt = (EditText) findViewById(R.id.addEquipLocTxtN);
        addDrawingTxt = (EditText) findViewById(R.id.addDrawingTxtN);
        addLeakerId = (EditText) findViewById(R.id.addLeakerIdTxtN);
        emergencyTxt = (EditText) findViewById(R.id.addEmerTxtN);
        heightTxt = (EditText) findViewById(R.id.addHeightTxtN);

        unitName = (TextView) findViewById(R.id.unitNamesTxtN);
        prodt = (TextView) findViewById(R.id.productViewN);

        emer = (CheckBox) findViewById(R.id.emerChN);

        sysF1 = (TableRow) findViewById(R.id.sysFormat1);
        sysF2 = (TableRow) findViewById(R.id.sysFormat2);

        sysFor11 = (Spinner) findViewById(R.id.systemformatNumSp11);
        sysFor12 = (Spinner) findViewById(R.id.systemformatNumSp12);
        sysFor13 = (Spinner) findViewById(R.id.systemformatNumSp13);
        sysFor14 = (Spinner) findViewById(R.id.systemformatNumSp14);

        sysFor21 = (Spinner) findViewById(R.id.systemformatNumSp21);
        sysFor22 = (Spinner) findViewById(R.id.systemformatNumSp22);
        sysFor23 = (Spinner) findViewById(R.id.systemformatNumSp23);
        sysFor24 = (Spinner) findViewById(R.id.systemformatNumSp24);

        sFU1 = (TextView) findViewById(R.id.systemFormatUnit11);
        sFU2 = (TextView) findViewById(R.id.systemFormatUnit21);

        addSpecificTxt.setSingleLine(true);
        addEquipLocTxt.setSingleLine(true);
        addDrawingTxt.setSingleLine(true);
        addLeakerId.setSingleLine(true);
        emergencyTxt.setSingleLine(true);
        heightTxt.setSingleLine(true);

        emergencyTxt.setVisibility(View.GONE);
        addDrawingTxt.setVisibility(View.GONE);
        streamLocation2.setVisibility(View.GONE);

        // Equipment Details
        equipDesc = (Spinner) findViewById(R.id.eDescSp);
        equipType = (Spinner) findViewById(R.id.eTypeSp);
        equipSize = (Spinner) findViewById(R.id.eSizeSp);
        mPosition = (Spinner) findViewById(R.id.mPositionSp);
        subDesc = (Spinner) findViewById(R.id.mainTypeSp);

        ppm = (RadioButton) findViewById(R.id.radioPPMN);
        lel = (RadioButton) findViewById(R.id.radioLELN);
        gas = (RadioButton) findViewById(R.id.radioGASN);

        equipID = (EditText) findViewById(R.id.eIDTxtN);
        mResult = (EditText) findViewById(R.id.measurementTxtN);
        backGr = (EditText) findViewById(R.id.backgroundTxtN);
        date = (TextView) findViewById(R.id.dateTxtN);

        String currentDate1 = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR);

        String currentDate2 = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE);

        currentDate = currentDate1.substring(8) + "-" + currentDate2.substring(3) + "-" + currentDate2.substring(0, 2);
        Log.d("Date 1", currentDate.substring(5, 6));
        Log.d("Date 2", currentDate.substring(8, 9));
        if (!currentDate.substring(5, 6).equals("-") || !currentDate.substring(8, 9).equals("-")) {
            currentDate = DateUtils.formatDateTime(getBaseContext(),
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_NUMERIC_DATE
                            | DateUtils.FORMAT_24HOUR);

            if (currentDate.substring(7, 11).equals("2014")
                    || currentDate.substring(7, 11).equals("2015")
                    || currentDate.substring(7, 11).equals("2016")
                    || currentDate.substring(7, 11).equals("2017")) {

                String tmpdate = currentDate.substring(7, 11) + "-"
                        + currentDate.substring(12, 14) + "-"
                        + currentDate.substring(15, 17);
                date.setText(tmpdate);
                currentDate = date.getText().toString();
            } else {
//                String tmpdate = currentDate.substring(13, 17) + "-"
                //                       + currentDate.substring(10, 12) + "-"
                //                       + currentDate.substring(7, 9);
                date.setText(currentDate);
                currentDate = date.getText().toString();
            }
        }
        date.setText(currentDate);

        equipID.setSingleLine(true);
        mResult.setSingleLine(true);
        backGr.setSingleLine(true);
    }

    private void loadL() {
        File dir = Environment.getExternalStorageDirectory();
        // File yourFile = new File(dir,
        // "path/to/the/file/inside/the/sdcard.ext");

        // Get the text file
        File file = new File("/sdcard/e3softData/Data/leaker.txt");
        // i have kept text.txt in the sd-card

        if (file.exists()) // check if file exist
        {
            // Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
            } catch (IOException e) {
                // You'll need to add proper error handling here
            }
            // Set the text
            addLeakerId.setText(text);
        } else {
            addLeakerId.setText("Sorry file doesn't exist!!");
        }
    }

    private void getProduct() {
        String tmpStream = streamLocation2.getSelectedItem().toString();
        SQLiteDatabase dbS = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/newStreams.db", null, 0);

        Cursor cS = dbS.rawQuery(
                "SELECT nwStreams.product FROM nwStreams where nwStreams.stream ='"
                        + tmpStream + "'", null);
        int proSt = cS.getColumnIndex("product");
        cS.moveToLast();

        // looping through all rows and adding to list
        if (cS.getCount() > 0) {
            do {
                String prodSt = "";
                prodSt = cS.getString(proSt);
                productTt = prodSt;
            } while (cS.moveToNext());
        } else {
            Log.d("Product", productTt + "EMPTY");
        }

        // closing connection
        cS.close();
        dbS.close();

    }

    private void popStreams() {
        ProData db = new ProData(getApplicationContext());
        try {
            List<String> pStreams = db.getAllStreams2();
            ArrayAdapter<String> dataAdapterStream = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pStreams);
            dataAdapterStream
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            streamLocation2.setAdapter(dataAdapterStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
