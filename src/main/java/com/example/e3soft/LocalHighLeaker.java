package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalHighLeaker extends Activity {

    private Button updateB, backB, clearB, loadHD, addP;

    private Button addCamOp, addCamSerial, addGasSurveyOp, addGasSurveySerial;

    private Spinner camOp, camSerial, gasSurveyOp, gasSurveySerial, searchSp1,
            cycleCom, bTypes;

    private String camOpPressed, camSerialPressed, gasSurveyOpPressed,
            gasSurveySerialPressed, correctedReading, correctedR, bTypeT;

    private EditText camOpTxt, camSerialTxt, gasSurveyOpTxt,
            gasSurveySerialTxt, backGr;

    private String camOpData, camSerialData, gasSurveyOpData,
            gasSurveySerialData;

    private RadioButton ppm, lel, gas;

    private EditText mResult, searchEt, proTt, streamTt, cfTt;

    private TextView date;

    private TextView fLoc, fID, fCon, fDraw, dLoc, dDraw, vLoc, vDraw, product,
            equipmentDesc, equipmentType, equipmentSize, equipmentId,
            mPosition, dateCreate, subD, leaker;

    private String currentActivityName, activityName, leakerIDs,
            strRecommendation;

    private String counter;

    private String addPro, addCf, addStream;

    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT,
            dateCreateT, dateModT, currentDate, mPositionT, reading;

    private String siteT, areaT, unitNumberT, unitT, unitSizeT, drawingT,
            equipmentLocT, specificLocT, streamT, systemT;

    private String strMResult, strBackG, strCf, strMRes, DATE_TAKEN;

    private String imageLocT, productT, equipmentDescT, subDescriptionT,
            equipmentTypeT, mResultT, aLossT, readingT, equipmentSizeT,
            equipmentClassT, equipmentIdT, dateMod, recomT, reportT, image,
            leakerT;

    private Double cf = 0.0, mRes = 0.0, lRa = 0.0, trendF = 0.0, backG = 0.0,
            sv = 0.0;

    private TextView c1, c2, c3, c4, c5, c6, c7, c8, c9;
    private TextView c11, c21, c31, c41, c51, c61, c71, c81, c91;

    private TableRow t1, t2, t3, t4, t5, t6, t7, t8, t9;

    private String c1T, c2T, c3T, c4T, c5T, c6T, c7T, c8T, c9T, e0T;
    private String c11T, c21T, c31T, c41T, c51T, c61T, c71T, c81T, c91T;

    private TableLayout fTab, dTab, vTab;

    private String fLocT, fIDT, fConT, fDrawT, vLocT, vDrawT, dLocT, dDrawT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localhighleaker);
        initialiseComponents();
        populateSpinners2();
        populateLeakers();

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                populateVariables();
                strMResult = mResult.getText().toString();
                strMRes = strMResult;

                if (strMResult == null || strMResult.isEmpty()) {
                    double mRes = 0.0;
                } else {
                    loadCf();
                    calculateBackG();
                    calculateLoss();
                    addHighLeaker();
                    try {
                        saveHighcsv();
                        savehighExcelcsv();
                        Log.d("Data", "saved");
                        requestAction();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }

        });

        loadHD.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Log.d("search Button", "PRESSED");
                leakerIDs = searchEt.getText().toString();
                leakerIDs = "LM_" + leakerIDs;
                if (bTypes.getSelectedItem().toString()
                        .equals("Filling Station")) {
                    populateText();
                    populatePrevResults();
                    checkResults();
                    fTab.setVisibility(View.VISIBLE);
                    vTab.setVisibility(View.GONE);
                    dTab.setVisibility(View.GONE);
                } else if (bTypes.getSelectedItem().toString()
                        .equals("Valve Station")) {
                    populateTextV();
                    populatePrevResults();
                    checkResults();
                    vTab.setVisibility(View.VISIBLE);
                    fTab.setVisibility(View.GONE);
                    dTab.setVisibility(View.GONE);
                } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
                    populateTextD();
                    populatePrevResults();
                    checkResults();
                    dTab.setVisibility(View.VISIBLE);
                    vTab.setVisibility(View.GONE);
                    fTab.setVisibility(View.GONE);
                }

            }

        });

        addCamOp.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (camOpTxt.isShown()) {

                    camOpTxt.setVisibility(View.GONE);
                    addCamOp.setBackgroundResource(R.drawable.edit);
                    addCamOp.setText("ADD");
                    addCamOp();

                } else {
                    camOpTxt.setVisibility(View.VISIBLE);
                    addCamOp.setBackgroundResource(R.drawable.save);
                    addCamOp.setText("SAVE");
                    gasSurveySerialTxt.setVisibility(View.GONE);
                    gasSurveyOpTxt.setVisibility(View.GONE);
                    camSerialTxt.setVisibility(View.GONE);
                    addCamSerial.setBackgroundResource(R.drawable.edit);
                    addGasSurveyOp.setBackgroundResource(R.drawable.edit);
                    addGasSurveySerial.setBackgroundResource(R.drawable.edit);

                    addGasSurveySerial.setText("ADD");
                    addCamSerial.setText("ADD");
                    addGasSurveyOp.setText("ADD");
                }
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

        clearB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                camOpTxt.setEnabled(true);
                camOp.setEnabled(true);
                camSerialTxt.setEnabled(true);
                camSerial.setEnabled(true);

                gasSurveyOpTxt.setEnabled(true);
                gasSurveyOp.setEnabled(true);
                gasSurveySerialTxt.setEnabled(true);
                gasSurveySerial.setEnabled(true);

                addCamOp.setEnabled(true);
                addCamSerial.setEnabled(true);
                addGasSurveyOp.setEnabled(true);
                addGasSurveySerial.setEnabled(true);

                addCamOp.setBackgroundResource(R.drawable.edit);
                addCamSerial.setBackgroundResource(R.drawable.edit);
                addGasSurveyOp.setBackgroundResource(R.drawable.edit);
                addGasSurveySerial.setBackgroundResource(R.drawable.edit);

                ppm.setVisibility(View.VISIBLE);
                lel.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);

                updateB.setBackgroundResource(R.drawable.submit_inactive);
                Toast.makeText(getBaseContext(), "Cleared all Data",
                        Toast.LENGTH_LONG).show();

            }
        });

        backB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(LocalHighLeaker.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        addP.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                addPro = proTt.getText().toString();
                addStream = streamTt.getText().toString();
                addCf = cfTt.getText().toString();
                updateProduct();
            }

            private void updateProduct() {
                leakerIDs = leaker.getText().toString();
                DatabaseHandler dbr = new DatabaseHandler(
                        getApplicationContext());

                dbr.updateProduct(leakerIDs, addPro, addStream, addCf);

                Log.d("database", "report Saved");
                dbr.close();

                proTt.setVisibility(View.GONE);
                streamTt.setVisibility(View.GONE);
                cfTt.setVisibility(View.GONE);
                addP.setVisibility(View.GONE);
            }
        });

        searchSp1.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                bTypeT = bTypes.getSelectedItem().toString();
                leakerIDs = searchSp1.getSelectedItem().toString();
                if (bTypes.getSelectedItem().toString()
                        .equals("Filling Station")) {
                    populateText();
                } else if (bTypes.getSelectedItem().toString()
                        .equals("Valve Station")) {
                    populateTextV();
                } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
                    populateTextD();
                }
                populatePrevResults();
                checkResults();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        checkCompletetion();
    }

    protected void requestAction() {
        new AlertDialog.Builder(this)
                .setTitle("HIGH LEAKER SAVED")
                .setMessage("DO ANOTHER SEARCH?")
                .setCancelable(false)
                .setPositiveButton("NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(LocalHighLeaker.this,
                                        Home.class);
                                startActivity(i);
                                finish();
                            }
                        })
                .setNegativeButton("YES", null).show();
        searchEt.setText("");
        mResult.setText("");
        backGr.setText("");

        ppm.setVisibility(View.VISIBLE);
        lel.setVisibility(View.VISIBLE);
        gas.setVisibility(View.VISIBLE);
    }

    private void populatePrevResults() {
        bTypeT = bTypes.getSelectedItem().toString();
        if (bTypeT.equals("Filling Station")) {
            String data1 = "";
            String prod1 = "";

            String data2 = "";
            String prod2 = "";

            String data3 = "";
            String prod3 = "";

            String data4 = "";
            String prod4 = "";

            String data5 = "";
            String prod5 = "";

            String data6 = "";
            String prod6 = "";

            String data7 = "";
            String prod7 = "";

            String data8 = "";
            String prod8 = "";

            String data9 = "";
            String prod9 = "";

            String data10 = "";
            String prod10 = "";

            String data11 = "";
            String prod11 = "";

            String data12 = "";
            String prod12 = "";

            String data13 = "";
            String prod13 = "";

            String data14 = "";
            String prod14 = "";

            String data15 = "";
            String prod15 = "";

            String data16 = "";
            String prod16 = "";

            String data17 = "";
            String prod17 = "";

            String data18 = "";
            String prod18 = "";

            SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/fHighLeaker.db", null, 0);

            Cursor c10 = db10.rawQuery("SELECT *" + " FROM fHighLeaker "
                    + "WHERE fHighLeaker.leakerID = '" + leakerIDs + "'", null);

            int pro1 = c10.getColumnIndex("cycle1Result");
            int pro2 = c10.getColumnIndex("newC1Result");
            int pro3 = c10.getColumnIndex("cycle2Result");
            int pro4 = c10.getColumnIndex("newC2Result");
            int pro5 = c10.getColumnIndex("cycle3Result");
            int pro6 = c10.getColumnIndex("newC3Result");
            int pro7 = c10.getColumnIndex("cycle4Result");
            int pro8 = c10.getColumnIndex("newC4Result");
            int pro9 = c10.getColumnIndex("cycle5Result");
            int pro10 = c10.getColumnIndex("newC5Result");
            int pro11 = c10.getColumnIndex("cycle6Result");
            int pro12 = c10.getColumnIndex("newC6Result");

            int pro13 = c10.getColumnIndex("cycle7Result");
            int pro14 = c10.getColumnIndex("newC7Result");
            int pro15 = c10.getColumnIndex("cycle8Result");
            int pro16 = c10.getColumnIndex("newC8Result");

            int pro17 = c10.getColumnIndex("cycle9Result");
            int pro18 = c10.getColumnIndex("newC9Result");

            c10.moveToLast();

            // looping through all rows and adding to list
            if (c10.getCount() > 0) {
                do {
                    prod1 = c10.getString(pro1);
                    data1 = data1 + prod1;
                    c1.setText(data1);

                    prod2 = c10.getString(pro2);
                    data2 = data2 + prod2;
                    c11.setText(data2);

                    prod3 = c10.getString(pro3);
                    data3 = data3 + prod3;
                    c2.setText(data3);

                    prod4 = c10.getString(pro4);
                    data4 = data4 + prod4;
                    c21.setText(data4);

                    prod5 = c10.getString(pro5);
                    data5 = data5 + prod5;
                    c3.setText(data5);

                    prod6 = c10.getString(pro6);
                    data6 = data6 + prod6;
                    c31.setText(data6);

                    prod7 = c10.getString(pro7);
                    data7 = data7 + prod7;
                    c4.setText(data7);

                    prod8 = c10.getString(pro8);
                    data8 = data8 + prod8;
                    c41.setText(data8);

                    prod9 = c10.getString(pro9);
                    data9 = data9 + prod9;
                    c5.setText(data9);

                    prod10 = c10.getString(pro10);
                    data10 = data10 + prod10;
                    c51.setText(data10);

                    prod11 = c10.getString(pro11);
                    data11 = data11 + prod11;
                    c6.setText(data11);

                    prod12 = c10.getString(pro12);
                    data12 = data12 + prod12;
                    c61.setText(data12);

                    prod13 = c10.getString(pro13);
                    data13 = data13 + prod13;
                    c7.setText(data13);

                    prod14 = c10.getString(pro14);
                    data14 = data14 + prod14;
                    c71.setText(data14);

                    prod15 = c10.getString(pro15);
                    data15 = data15 + prod15;
                    c8.setText(data15);

                    prod16 = c10.getString(pro16);
                    data16 = data16 + prod16;
                    c81.setText(data16);

                    prod17 = c10.getString(pro17);
                    data17 = data17 + prod17;
                    c9.setText(data17);

                    prod18 = c10.getString(pro18);
                    data18 = data18 + prod18;
                    c91.setText(data18);

                } while (c10.moveToNext());
            } else {
                Log.d("Empty Report", data1 + "EMPTY");
            }
            // closing connection
            c10.close();
            db10.close();
        } else if (bTypeT.equals("Valve Station")) {
            String data1 = "";
            String prod1 = "";

            String data2 = "";
            String prod2 = "";

            String data3 = "";
            String prod3 = "";

            String data4 = "";
            String prod4 = "";

            String data5 = "";
            String prod5 = "";

            String data6 = "";
            String prod6 = "";

            String data7 = "";
            String prod7 = "";

            String data8 = "";
            String prod8 = "";

            String data9 = "";
            String prod9 = "";

            String data10 = "";
            String prod10 = "";

            String data11 = "";
            String prod11 = "";

            String data12 = "";
            String prod12 = "";

            String data13 = "";
            String prod13 = "";

            String data14 = "";
            String prod14 = "";

            String data15 = "";
            String prod15 = "";

            String data16 = "";
            String prod16 = "";

            String data17 = "";
            String prod17 = "";

            String data18 = "";
            String prod18 = "";

            SQLiteDatabase db10 =
                    SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/vHighLeaker.db", null, 0);

            Cursor c10 = db10.rawQuery("SELECT *" + " FROM vHighLeaker "
                    + "WHERE vHighLeaker.leakerID = '" + leakerIDs + "'", null);

            int pro1 = c10.getColumnIndex("cycle1Result");
            int pro2 = c10.getColumnIndex("newC1Result");
            int pro3 = c10.getColumnIndex("cycle2Result");
            int pro4 = c10.getColumnIndex("newC2Result");
            int pro5 = c10.getColumnIndex("cycle3Result");
            int pro6 = c10.getColumnIndex("newC3Result");
            int pro7 = c10.getColumnIndex("cycle4Result");
            int pro8 = c10.getColumnIndex("newC4Result");
            int pro9 = c10.getColumnIndex("cycle5Result");
            int pro10 = c10.getColumnIndex("newC5Result");
            int pro11 = c10.getColumnIndex("cycle6Result");
            int pro12 = c10.getColumnIndex("newC6Result");

            int pro13 = c10.getColumnIndex("cycle7Result");
            int pro14 = c10.getColumnIndex("newC7Result");
            int pro15 = c10.getColumnIndex("cycle8Result");
            int pro16 = c10.getColumnIndex("newC8Result");

            int pro17 = c10.getColumnIndex("cycle9Result");
            int pro18 = c10.getColumnIndex("newC9Result");

            c10.moveToLast();

            // looping through all rows and adding to list
            if (c10.getCount() > 0) {
                do {
                    prod1 = c10.getString(pro1);
                    data1 = data1 + prod1;
                    c1.setText(data1);

                    prod2 = c10.getString(pro2);
                    data2 = data2 + prod2;
                    c11.setText(data2);

                    prod3 = c10.getString(pro3);
                    data3 = data3 + prod3;
                    c2.setText(data3);

                    prod4 = c10.getString(pro4);
                    data4 = data4 + prod4;
                    c21.setText(data4);

                    prod5 = c10.getString(pro5);
                    data5 = data5 + prod5;
                    c3.setText(data5);

                    prod6 = c10.getString(pro6);
                    data6 = data6 + prod6;
                    c31.setText(data6);

                    prod7 = c10.getString(pro7);
                    data7 = data7 + prod7;
                    c4.setText(data7);

                    prod8 = c10.getString(pro8);
                    data8 = data8 + prod8;
                    c41.setText(data8);

                    prod9 = c10.getString(pro9);
                    data9 = data9 + prod9;
                    c5.setText(data9);

                    prod10 = c10.getString(pro10);
                    data10 = data10 + prod10;
                    c51.setText(data10);

                    prod11 = c10.getString(pro11);
                    data11 = data11 + prod11;
                    c6.setText(data11);

                    prod12 = c10.getString(pro12);
                    data12 = data12 + prod12;
                    c61.setText(data12);

                    prod13 = c10.getString(pro13);
                    data13 = data13 + prod13;
                    c7.setText(data13);

                    prod14 = c10.getString(pro14);
                    data14 = data14 + prod14;
                    c71.setText(data14);

                    prod15 = c10.getString(pro15);
                    data15 = data15 + prod15;
                    c8.setText(data15);

                    prod16 = c10.getString(pro16);
                    data16 = data16 + prod16;
                    c81.setText(data16);

                    prod17 = c10.getString(pro17);
                    data17 = data17 + prod17;
                    c9.setText(data17);

                    prod18 = c10.getString(pro18);
                    data18 = data18 + prod18;
                    c91.setText(data18);

                } while (c10.moveToNext());
            } else {
                Log.d("Empty Report", data1 + "EMPTY");
            }
            // closing connection
            c10.close();
            db10.close();
        } else if (bTypeT.equals("Depot")) {
            String data1 = "";
            String prod1 = "";

            String data2 = "";
            String prod2 = "";

            String data3 = "";
            String prod3 = "";

            String data4 = "";
            String prod4 = "";

            String data5 = "";
            String prod5 = "";

            String data6 = "";
            String prod6 = "";

            String data7 = "";
            String prod7 = "";

            String data8 = "";
            String prod8 = "";

            String data9 = "";
            String prod9 = "";

            String data10 = "";
            String prod10 = "";

            String data11 = "";
            String prod11 = "";

            String data12 = "";
            String prod12 = "";

            String data13 = "";
            String prod13 = "";

            String data14 = "";
            String prod14 = "";

            String data15 = "";
            String prod15 = "";

            String data16 = "";
            String prod16 = "";

            String data17 = "";
            String prod17 = "";

            String data18 = "";
            String prod18 = "";

            SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/dHighLeaker.db", null, 0);
            Cursor c10 = db10.rawQuery("SELECT *" + " FROM dHighLeaker "
                    + "WHERE dHighLeaker.leakerID = '" + leakerIDs + "'", null);

            int pro1 = c10.getColumnIndex("cycle1Result");
            int pro2 = c10.getColumnIndex("newC1Result");
            int pro3 = c10.getColumnIndex("cycle2Result");
            int pro4 = c10.getColumnIndex("newC2Result");
            int pro5 = c10.getColumnIndex("cycle3Result");
            int pro6 = c10.getColumnIndex("newC3Result");
            int pro7 = c10.getColumnIndex("cycle4Result");
            int pro8 = c10.getColumnIndex("newC4Result");
            int pro9 = c10.getColumnIndex("cycle5Result");
            int pro10 = c10.getColumnIndex("newC5Result");
            int pro11 = c10.getColumnIndex("cycle6Result");
            int pro12 = c10.getColumnIndex("newC6Result");

            int pro13 = c10.getColumnIndex("cycle7Result");
            int pro14 = c10.getColumnIndex("newC7Result");
            int pro15 = c10.getColumnIndex("cycle8Result");
            int pro16 = c10.getColumnIndex("newC8Result");

            int pro17 = c10.getColumnIndex("cycle9Result");
            int pro18 = c10.getColumnIndex("newC9Result");

            c10.moveToLast();

            // looping through all rows and adding to list
            if (c10.getCount() > 0) {
                do {
                    prod1 = c10.getString(pro1);
                    data1 = data1 + prod1;
                    c1.setText(data1);

                    prod2 = c10.getString(pro2);
                    data2 = data2 + prod2;
                    c11.setText(data2);

                    prod3 = c10.getString(pro3);
                    data3 = data3 + prod3;
                    c2.setText(data3);

                    prod4 = c10.getString(pro4);
                    data4 = data4 + prod4;
                    c21.setText(data4);

                    prod5 = c10.getString(pro5);
                    data5 = data5 + prod5;
                    c3.setText(data5);

                    prod6 = c10.getString(pro6);
                    data6 = data6 + prod6;
                    c31.setText(data6);

                    prod7 = c10.getString(pro7);
                    data7 = data7 + prod7;
                    c4.setText(data7);

                    prod8 = c10.getString(pro8);
                    data8 = data8 + prod8;
                    c41.setText(data8);

                    prod9 = c10.getString(pro9);
                    data9 = data9 + prod9;
                    c5.setText(data9);

                    prod10 = c10.getString(pro10);
                    data10 = data10 + prod10;
                    c51.setText(data10);

                    prod11 = c10.getString(pro11);
                    data11 = data11 + prod11;
                    c6.setText(data11);

                    prod12 = c10.getString(pro12);
                    data12 = data12 + prod12;
                    c61.setText(data12);

                    prod13 = c10.getString(pro13);
                    data13 = data13 + prod13;
                    c7.setText(data13);

                    prod14 = c10.getString(pro14);
                    data14 = data14 + prod14;
                    c71.setText(data14);

                    prod15 = c10.getString(pro15);
                    data15 = data15 + prod15;
                    c8.setText(data15);

                    prod16 = c10.getString(pro16);
                    data16 = data16 + prod16;
                    c81.setText(data16);

                    prod17 = c10.getString(pro17);
                    data17 = data17 + prod17;
                    c9.setText(data17);

                    prod18 = c10.getString(pro18);
                    data18 = data18 + prod18;
                    c91.setText(data18);

                } while (c10.moveToNext());
            } else {
                Log.d("Empty Report", data1 + "EMPTY");
            }
            // closing connection
            c10.close();
            db10.close();
        }
    }

    private void populateLeakers() {
        leakerT = searchEt.getText().toString();
        if (bTypes.getSelectedItem().toString().equals("Filling Station")) {
            FHighData db = new FHighData(getApplicationContext());

            // Spinner Drop down elements
            List<String> pleakers = db.getAllLeakers(leakerT);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapterleakers = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pleakers);

            // Drop down layout style - list view with radio button
            dataAdapterleakers
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            searchSp1.setAdapter(dataAdapterleakers);
        } else if (bTypes.getSelectedItem().toString().equals("Valve Station")) {
            VHighData db = new VHighData(getApplicationContext());

            // Spinner Drop down elements
            List<String> pleakers = db.getAllLeakers(leakerT);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapterleakers = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pleakers);

            // Drop down layout style - list view with radio button
            dataAdapterleakers
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            searchSp1.setAdapter(dataAdapterleakers);
        } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
            DHighData db = new DHighData(getApplicationContext());

            // Spinner Drop down elements
            List<String> pleakers = db.getAllLeakers(leakerT);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapterleakers = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pleakers);

            // Drop down layout style - list view with radio button
            dataAdapterleakers
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            searchSp1.setAdapter(dataAdapterleakers);
        }
    }

    protected void savehighExcelcsv() throws IOException {
        if (bTypes.getSelectedItem().toString().equals("Filling Station")) {
            String direc = "/e3softData/";
            String fileName = "fillingHighLeakerExcel.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            if (file.exists()) {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                String data = "" + bTypeT + "" + "," + "" + fLocT + "" + "," + ""
                        + fIDT + "" + "," + "" + fConT + "" + "," + ""
                        + fDrawT + "" + "," + ""
                        + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                        + gasSurveyOpT + "" + "," + "" + gasSurveySerialT + "" + ","
                        + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                        + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                        + "" + "," + "" + equipmentSizeT + "" + "," + ""
                        + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                        + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                        + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                        + strRecommendation + "" + "," + "" + dateCreateT + "" + ","
                        + "" + leakerIDs + "" + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            } else {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                createHead();
                String data = "" + bTypeT + "" + "," + "" + fLocT + "" + "," + ""
                        + fIDT + "" + "," + "" + fConT + "" + "," + ""
                        + fDrawT + "" + "," + ""
                        + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                        + gasSurveyOpT + "" + "," + "" + gasSurveySerialT + "" + ","
                        + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                        + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                        + "" + "," + "" + equipmentSizeT + "" + "," + ""
                        + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                        + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                        + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                        + strRecommendation + "" + "," + "" + dateCreateT + "" + ","
                        + "" + leakerIDs + "" + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            }
        } else if (bTypes.getSelectedItem().toString().equals("Valve Station")) {
            String direc = "/e3softData/";
            String fileName = "valveHighLeakerExcel.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            if (file.exists()) {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                String data = "" + bTypeT + "" + "," + "" + vLocT + "" + "," + ""
                        + vDrawT + "" + "," + ""
                        + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                        + gasSurveyOpT + "" + "," + "" + gasSurveySerialT + "" + ","
                        + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                        + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                        + "" + "," + "" + equipmentSizeT + "" + "," + ""
                        + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                        + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                        + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                        + strRecommendation + "" + "," + "" + dateCreateT + "" + ","
                        + "" + leakerIDs + "" + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            } else {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                createHead();
                String data = "" + bTypeT + "" + "," + "" + vLocT + "" + "," + ""
                        + vDrawT + "" + "," + ""
                        + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                        + gasSurveyOpT + "" + "," + "" + gasSurveySerialT + "" + ","
                        + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                        + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                        + "" + "," + "" + equipmentSizeT + "" + "," + ""
                        + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                        + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                        + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                        + strRecommendation + "" + "," + "" + dateCreateT + "" + ","
                        + "" + leakerIDs + "" + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            }
        } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
            String direc = "/e3softData/";
            String fileName = "depotHighLeakerExcel.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            if (file.exists()) {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                String data = "" + bTypeT + "" + "," + "" + dLocT + "" + "," + ""
                        + dDrawT + "" + "," + ""
                        + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                        + gasSurveyOpT + "" + "," + "" + gasSurveySerialT + "" + ","
                        + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                        + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                        + "" + "," + "" + equipmentSizeT + "" + "," + ""
                        + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                        + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                        + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                        + strRecommendation + "" + "," + "" + dateCreateT + "" + ","
                        + "" + leakerIDs + "" + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            } else {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                createHead();
                String data = "" + bTypeT + "" + "," + "" + dLocT + "" + "," + ""
                        + dDrawT + "" + "," + ""
                        + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                        + gasSurveyOpT + "" + "," + "" + gasSurveySerialT + "" + ","
                        + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                        + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                        + "" + "," + "" + equipmentSizeT + "" + "," + ""
                        + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                        + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                        + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                        + strRecommendation + "" + "," + "" + dateCreateT + "" + ","
                        + "" + leakerIDs + "" + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            }
        }

    }

    private void createHead() throws IOException {
        if (bTypes.getSelectedItem().toString().equals("Filling Station")) {
            String direc = "/e3softData/";
            String fileName = "fillingHighLeakerExcel.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            String Data2 = "Type,Station ID,Name,Contact,drawing,"
                    + "cameraOperator,"
                    + "cameraSerial,gasSurveyor, gasSurveyorSerial,product,"
                    + "equipmentDescription,measurementType,equipmentType,"
                    + "equipmentSize,equipmentClass,equipmentID,measurementPosition,"
                    + "measuremntResult,readingUnit,potentialLoss,recommendations,"
                    + "time,date,leakerID" + "\n";
            bw.write(Data2);
            bw.flush();
            bw.close();
        } else if (bTypes.getSelectedItem().toString().equals("Valve Station")) {
            String direc = "/e3softData/";
            String fileName = "valveHighLeakerExcel.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            String Data2 = "Type,Name,drawing,"
                    + "cameraOperator,"
                    + "cameraSerial,gasSurveyor, gasSurveyorSerial,product,"
                    + "equipmentDescription,measurementType,equipmentType,"
                    + "equipmentSize,equipmentClass,equipmentID,measurementPosition,"
                    + "measuremntResult,readingUnit,potentialLoss,recommendations,"
                    + "time,date,leakerID" + "\n";
            bw.write(Data2);
            bw.flush();
            bw.close();
        } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
            String direc = "/e3softData/";
            String fileName = "depotHighLeakerExcel.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            String Data2 = "Type,Name,drawing,"
                    + "cameraOperator,"
                    + "cameraSerial,gasSurveyor, gasSurveyorSerial,product,"
                    + "equipmentDescription,measurementType,equipmentType,"
                    + "equipmentSize,equipmentClass,equipmentID,measurementPosition,"
                    + "measuremntResult,readingUnit,potentialLoss,recommendations,"
                    + "time,date,leakerID" + "\n";
            bw.write(Data2);
            bw.flush();
            bw.close();
        }

    }

    protected void saveHighcsv() throws IOException {
        if (bTypes.getSelectedItem().toString().equals("Filling Station")) {
            String direc = "/e3softData/";
            String fileName = "fillingHighLeaker.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));

            String data = "'" + bTypeT + "'" + "," + "'" + fLocT + "'" + "," + "'"
                    + fIDT + "'" + "," + "'" + fConT + "'" + "," + "'"
                    + fDrawT + "'" + "," + "'"
                    + camOpT + "'" + "," + "'" + camSerialT + "'" + "," + "'"
                    + gasSurveyOpT + "'" + "," + "'" + gasSurveySerialT + "'" + ","
                    + "'" + productT + "'" + "," + "'" + equipmentDescT + "'" + ","
                    + "'" + subDescriptionT + "'" + "," + "'" + equipmentTypeT
                    + "'" + "," + "'" + equipmentSizeT + "'" + "," + "'"
                    + equipmentClassT + "'" + "," + "'" + equipmentIdT + "'" + ","
                    + "'" + mPositionT + "'" + "," + "'" + mResultT + "'" + ","
                    + "'" + readingT + "'" + "," + "'" + aLossT + "'" + "," + "'"
                    + strRecommendation + "'" + "," + "'" + dateCreateT + "'" + ","
                    + "'" + leakerIDs + "';" + "\n";
            bw.write(data);
            bw.flush();
            bw.close();
        } else if (bTypes.getSelectedItem().toString().equals("Valve Station")) {
            String direc = "/e3softData/";
            String fileName = "valveHighLeaker.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));

            String data = "'" + bTypeT + "'" + "," + "'" + vLocT + "'" + "," + "'"
                    + vDrawT + "'" + "," + "'"
                    + camOpT + "'" + "," + "'" + camSerialT + "'" + "," + "'"
                    + gasSurveyOpT + "'" + "," + "'" + gasSurveySerialT + "'" + ","
                    + "'" + productT + "'" + "," + "'" + equipmentDescT + "'" + ","
                    + "'" + subDescriptionT + "'" + "," + "'" + equipmentTypeT
                    + "'" + "," + "'" + equipmentSizeT + "'" + "," + "'"
                    + equipmentClassT + "'" + "," + "'" + equipmentIdT + "'" + ","
                    + "'" + mPositionT + "'" + "," + "'" + mResultT + "'" + ","
                    + "'" + readingT + "'" + "," + "'" + aLossT + "'" + "," + "'"
                    + strRecommendation + "'" + "," + "'" + dateCreateT + "'" + ","
                    + "'" + leakerIDs + "';" + "\n";
            bw.write(data);
            bw.flush();
            bw.close();
        } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
            String direc = "/e3softData/";
            String fileName = "depotHighLeaker.csv";
            // get the path to sdcard
            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + direc);
            // create this directory if not already created
            dir.mkdir();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));

            String data = "'" + bTypeT + "'" + "," + "'" + dLocT + "'" + "," + "'"
                    + dDrawT + "'" + "," + "'"
                    + camOpT + "'" + "," + "'" + camSerialT + "'" + "," + "'"
                    + gasSurveyOpT + "'" + "," + "'" + gasSurveySerialT + "'" + ","
                    + "'" + productT + "'" + "," + "'" + equipmentDescT + "'" + ","
                    + "'" + subDescriptionT + "'" + "," + "'" + equipmentTypeT
                    + "'" + "," + "'" + equipmentSizeT + "'" + "," + "'"
                    + equipmentClassT + "'" + "," + "'" + equipmentIdT + "'" + ","
                    + "'" + mPositionT + "'" + "," + "'" + mResultT + "'" + ","
                    + "'" + readingT + "'" + "," + "'" + aLossT + "'" + "," + "'"
                    + strRecommendation + "'" + "," + "'" + dateCreateT + "'" + ","
                    + "'" + leakerIDs + "';" + "\n";
            bw.write(data);
            bw.flush();
            bw.close();
        }
    }

    protected void loadCf() {
        try {
            String data222 = "";
            String prod222 = "";
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/flocalMarkering.sqlite", null, 0);
            Cursor c2 = db.rawQuery(
                    "SELECT pr.cf FROM streams pr where pr.product = " + "'"
                            + productT + "'", null);
            int pro2 = c2.getColumnIndex("cf");
            c2.moveToLast();

            // looping through all rows and adding to list
            if (c2 != null) {
                do {
                    prod222 = c2.getString(pro2);
                    data222 = data222 + prod222;
                    cf = Double.parseDouble(data222);
                    strCf = cf.toString();
                    Log.d("cf", strCf);
                } while (c2.moveToNext());
            }

            // closing connection
            c2.close();
            db.close();
        } catch (Exception e) {
            proTt.setVisibility(View.VISIBLE);
            cfTt.setVisibility(View.VISIBLE);
            streamTt.setVisibility(View.VISIBLE);
            addP.setVisibility(View.VISIBLE);

            proTt.setText(product.getText().toString());
        }
    }

    protected void calculateBackG() {
        // TODO Auto-generated method stub

        strBackG = backGr.getText().toString();

        if (strBackG == null || strBackG.isEmpty()) {
            backG = 0.0;
        } else {
            backG = Double.parseDouble(strBackG);
        }
    }

    protected void calculateLoss() {
        // TODO Auto-generated method stub
        mRes = Double.parseDouble(mResult.getText().toString());

        if (reading == "LEL") {
            mRes = (mRes * 1000);
        }
        if (reading == "GAS %") {
            mRes = (mRes * 10000);
        }
        calculateRecomedation();
        Integer correctedReading1 = (int) (cf * mRes);
        correctedReading = correctedReading1.toString();
        correctedR = "PPM";
        sv = ((cf * mRes) - backG);

        if (sv <= 2000) {
            lRa = (sv * 0.01);
        } else if (sv <= 10000) {
            lRa = ((sv * 0.1225) - 225);
        } else if (sv <= 50000) {
            lRa = ((sv * 0.05) + 500);
            lRa = (lRa / 1000);
        } else {
            lRa = ((sv * 0.04) + 1000);
            lRa = (lRa / 1000);
        }

        strMResult = lRa.toString((double) lRa);
        Integer tmphL = (int) (cf * mRes);
        mResultT = tmphL.toString();
        aLossT = strMResult;
        readingT = "PPM";
    }

    private void calculateRecomedation() {
        // TODO Auto-generated method stub
        if (mRes >= 20000) {
            strRecommendation = "DANGER";
        } else if (mRes >= 10000) {
            strRecommendation = "URGENT";
        } else if (mRes >= 5000) {
            strRecommendation = "Maintenance Required";
        } else if (mRes < 5000) {
            strRecommendation = "Consider Maintenance";
        } else if (mRes <= 1000) {
            strRecommendation = "Acceptable";
        }

    }

    protected void populateVariables() {
        // TODO Auto-generated method stub
        camOpT = camOp.getSelectedItem().toString();
        camSerialT = camSerial.getSelectedItem().toString();
        gasSurveyOpT = gasSurveyOp.getSelectedItem().toString();
        gasSurveySerialT = gasSurveySerial.getSelectedItem().toString();

        productT = product.getText().toString();
        equipmentDescT = equipmentDesc.getText().toString();
        subDescriptionT = subD.getText().toString();
        equipmentTypeT = equipmentType.getText().toString();
        equipmentSizeT = equipmentSize.getText().toString();
        equipmentIdT = equipmentId.getText().toString();
        mPositionT = mPosition.getText().toString();
        dateCreateT = currentDate;
        equipmentClassT = "0";
        bTypeT = bTypes.getSelectedItem().toString();

        fLocT = fLoc.getText().toString();
        fIDT = fID.getText().toString();
        fConT = fCon.getText().toString();
        fDrawT = fDraw.getText().toString();

        vLocT = vLoc.getText().toString();
        vDrawT = vDraw.getText().toString();

        dLocT = dLoc.getText().toString();
        dDrawT = dDraw.getText().toString();


    }

    private void populateText() {

        String data1 = "";
        String prod1 = "";

        String data2 = "";
        String prod2 = "";

        String data3 = "";
        String prod3 = "";

        String data4 = "";
        String prod4 = "";

        String data5 = "";
        String prod5 = "";

        String data6 = "";
        String prod6 = "";

        String data7 = "";
        String prod7 = "";

        String data8 = "";
        String prod8 = "";

        String data9 = "";
        String prod9 = "";

        String data10 = "";
        String prod10 = "";

        String data11 = "";
        String prod11 = "";

        String data12 = "";
        String prod12 = "";

        String data13 = "";
        String prod13 = "";

        String data14 = "";
        String prod14 = "";

        String data15 = "";
        String prod15 = "";

        String data16 = "";
        String prod16 = "";

        String data17 = "";
        String prod17 = "";

        String data18 = "";
        String prod18 = "";

        String data19 = "";
        String prod19 = "";

        String data20 = "";
        String prod20 = "";

        String data21 = "";
        String prod21 = "";

        String data22 = "";
        String prod22 = "";

        String data23 = "";
        String prod23 = "";

        String data24 = "";
        String prod24 = "";

        String data25 = "";
        String prod25 = "";

        String data26 = "";
        String prod26 = "";

        String data27 = "";
        String prod27 = "";

        String data28 = "";
        String prod28 = "";

        String data29 = "";
        String prod29 = "";

        String data30 = "";
        String prod30 = "";

        String data31 = "";
        String prod31 = "";

        SQLiteDatabase db11 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/fHighLeaker.db", null, 0);
        Cursor c11 = db11.rawQuery("SELECT * " + "FROM fHighLeaker"
                + " WHERE fHighLeaker.leakerID = '" + leakerIDs + "'", null);

        int pro2 = c11.getColumnIndex("location");
        int pro3 = c11.getColumnIndex("stationID");
        int pro4 = c11.getColumnIndex("contact");
        int pro5 = c11.getColumnIndex("drawing");

        int pro16 = c11.getColumnIndex("product");
        int pro17 = c11.getColumnIndex("equipmentDesc");
        int pro18 = c11.getColumnIndex("subDescription");
        int pro19 = c11.getColumnIndex("equipmentType");
        int pro20 = c11.getColumnIndex("equipmentSize");
        int pro22 = c11.getColumnIndex("equipmentID");
        int pro23 = c11.getColumnIndex("measurementPosition");

        c11.moveToLast();

        // looping through all rows and adding to list
        if (c11.getCount() > 0) {
            do {

                prod2 = c11.getString(pro2);
                data2 = data2 + prod2;
                fLoc.setText(data2);
                Log.d("dataLoaded", "1");

                prod3 = c11.getString(pro3);
                data3 = data3 + prod3;
                fID.setText(data3);
                Log.d("dataLoaded", "2");
                prod4 = c11.getString(pro4);
                data4 = data4 + prod4;
                fCon.setText(data4);
                Log.d("dataLoaded", "3");
                prod5 = c11.getString(pro5);
                data5 = data5 + prod5;
                fDraw.setText(data5);
                Log.d("dataLoaded", "4");

                prod16 = c11.getString(pro16);
                data16 = data16 + prod16;
                product.setText(data16);
                Log.d("dataLoaded", "11");
                prod17 = c11.getString(pro17);
                data17 = data17 + prod17;
                equipmentDesc.setText(data17);
                Log.d("dataLoaded", "12");
                prod18 = c11.getString(pro18);
                data18 = data18 + prod18;
                subD.setText(data18);
                Log.d("dataLoaded", "13");
                prod19 = c11.getString(pro19);
                data19 = data19 + prod19;
                equipmentType.setText(data19);
                Log.d("dataLoaded", "14");
                prod20 = c11.getString(pro20);
                data20 = data20 + prod20;
                equipmentSize.setText(data20);
                Log.d("dataLoaded", "15");
                prod22 = c11.getString(pro22);
                data22 = data22 + prod22;
                equipmentId.setText(data22);
                Log.d("dataLoaded", "16");
                prod23 = c11.getString(pro23);
                data23 = data23 + prod23;
                mPosition.setText(data23);
                Log.d("dataLoaded", "17");


            } while (c11.moveToNext());
            Log.d("dataLoaded", "All Loaded");
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c11.close();
        db11.close();

    }

    private void populateTextV() {
        String data1 = "";
        String prod1 = "";

        String data2 = "";
        String prod2 = "";

        String data3 = "";
        String prod3 = "";

        String data4 = "";
        String prod4 = "";

        String data5 = "";
        String prod5 = "";

        String data6 = "";
        String prod6 = "";

        String data7 = "";
        String prod7 = "";

        String data8 = "";
        String prod8 = "";

        String data9 = "";
        String prod9 = "";

        String data10 = "";
        String prod10 = "";

        String data11 = "";
        String prod11 = "";

        String data12 = "";
        String prod12 = "";

        String data13 = "";
        String prod13 = "";

        String data14 = "";
        String prod14 = "";

        String data15 = "";
        String prod15 = "";

        String data16 = "";
        String prod16 = "";

        String data17 = "";
        String prod17 = "";

        String data18 = "";
        String prod18 = "";

        String data19 = "";
        String prod19 = "";

        String data20 = "";
        String prod20 = "";

        String data21 = "";
        String prod21 = "";

        String data22 = "";
        String prod22 = "";

        String data23 = "";
        String prod23 = "";

        String data24 = "";
        String prod24 = "";

        String data25 = "";
        String prod25 = "";

        String data26 = "";
        String prod26 = "";

        String data27 = "";
        String prod27 = "";

        String data28 = "";
        String prod28 = "";

        String data29 = "";
        String prod29 = "";

        String data30 = "";
        String prod30 = "";

        String data31 = "";
        String prod31 = "";

        SQLiteDatabase db11 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/vHighLeaker.db", null, 0);
        Cursor c11 = db11.rawQuery("SELECT * " + "FROM vHighLeaker"
                + " WHERE vHighLeaker.leakerID = '" + leakerIDs + "'", null);

        int pro2 = c11.getColumnIndex("location");
        int pro5 = c11.getColumnIndex("drawing");

        int pro16 = c11.getColumnIndex("product");
        int pro17 = c11.getColumnIndex("equipmentDesc");
        int pro18 = c11.getColumnIndex("subDescription");
        int pro19 = c11.getColumnIndex("equipmentType");
        int pro20 = c11.getColumnIndex("equipmentSize");
        int pro22 = c11.getColumnIndex("equipmentID");
        int pro23 = c11.getColumnIndex("measurementPosition");
        int pro29 = c11.getColumnIndex("date");

        c11.moveToLast();

        // looping through all rows and adding to list
        if (c11.getCount() > 0) {
            do {

                prod2 = c11.getString(pro2);
                data2 = data2 + prod2;
                vLoc.setText(data2);
                Log.d("dataLoaded", "1");
                prod5 = c11.getString(pro5);
                data5 = data5 + prod5;
                vDraw.setText(data5);
                Log.d("dataLoaded", "4");

                prod16 = c11.getString(pro16);
                data16 = data16 + prod16;
                product.setText(data16);
                Log.d("dataLoaded", "11");
                prod17 = c11.getString(pro17);
                data17 = data17 + prod17;
                equipmentDesc.setText(data17);
                Log.d("dataLoaded", "12");
                prod18 = c11.getString(pro18);
                data18 = data18 + prod18;
                subD.setText(data18);
                Log.d("dataLoaded", "13");
                prod19 = c11.getString(pro19);
                data19 = data19 + prod19;
                equipmentType.setText(data19);
                Log.d("dataLoaded", "14");
                prod20 = c11.getString(pro20);
                data20 = data20 + prod20;
                equipmentSize.setText(data20);
                Log.d("dataLoaded", "15");
                prod22 = c11.getString(pro22);
                data22 = data22 + prod22;
                equipmentId.setText(data22);
                Log.d("dataLoaded", "16");
                prod23 = c11.getString(pro23);
                data23 = data23 + prod23;
                mPosition.setText(data23);
                Log.d("dataLoaded", "17");

                fTab.setVisibility(View.VISIBLE);
                fLoc.setVisibility(View.VISIBLE);
                fID.setVisibility(View.VISIBLE);
                fDraw.setVisibility(View.VISIBLE);
                fCon.setVisibility(View.VISIBLE);

            } while (c11.moveToNext());
            Log.d("dataLoaded", "All Loaded");
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c11.close();
        db11.close();

    }

    private void populateTextD() {
        String data1 = "";
        String prod1 = "";

        String data2 = "";
        String prod2 = "";

        String data3 = "";
        String prod3 = "";

        String data4 = "";
        String prod4 = "";

        String data5 = "";
        String prod5 = "";

        String data6 = "";
        String prod6 = "";

        String data7 = "";
        String prod7 = "";

        String data8 = "";
        String prod8 = "";

        String data9 = "";
        String prod9 = "";

        String data10 = "";
        String prod10 = "";

        String data11 = "";
        String prod11 = "";

        String data12 = "";
        String prod12 = "";

        String data13 = "";
        String prod13 = "";

        String data14 = "";
        String prod14 = "";

        String data15 = "";
        String prod15 = "";

        String data16 = "";
        String prod16 = "";

        String data17 = "";
        String prod17 = "";

        String data18 = "";
        String prod18 = "";

        String data19 = "";
        String prod19 = "";

        String data20 = "";
        String prod20 = "";

        String data21 = "";
        String prod21 = "";

        String data22 = "";
        String prod22 = "";

        String data23 = "";
        String prod23 = "";

        String data24 = "";
        String prod24 = "";

        String data25 = "";
        String prod25 = "";

        String data26 = "";
        String prod26 = "";

        String data27 = "";
        String prod27 = "";

        String data28 = "";
        String prod28 = "";

        String data29 = "";
        String prod29 = "";

        String data30 = "";
        String prod30 = "";

        String data31 = "";
        String prod31 = "";

        SQLiteDatabase db11 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/dHighLeaker.db", null, 0);
        Cursor c11 = db11.rawQuery("SELECT * " + "FROM dHighLeaker"
                + " WHERE dHighLeaker.leakerID = '" + leakerIDs + "'", null);

        int pro2 = c11.getColumnIndex("location");
        int pro5 = c11.getColumnIndex("drawing");

        int pro16 = c11.getColumnIndex("product");
        int pro17 = c11.getColumnIndex("equipmentDesc");
        int pro18 = c11.getColumnIndex("subDescription");
        int pro19 = c11.getColumnIndex("equipmentType");
        int pro20 = c11.getColumnIndex("equipmentSize");
        int pro22 = c11.getColumnIndex("equipmentID");
        int pro23 = c11.getColumnIndex("measurementPosition");
        int pro29 = c11.getColumnIndex("date");

        c11.moveToLast();

        // looping through all rows and adding to list
        if (c11.getCount() > 0) {
            do {

                prod2 = c11.getString(pro2);
                data2 = data2 + prod2;
                dLoc.setText(data2);
                Log.d("dataLoaded", "1");
                prod5 = c11.getString(pro5);
                data5 = data5 + prod5;
                dDraw.setText(data5);
                Log.d("dataLoaded", "4");

                prod16 = c11.getString(pro16);
                data16 = data16 + prod16;
                product.setText(data16);
                Log.d("dataLoaded", "11");
                prod17 = c11.getString(pro17);
                data17 = data17 + prod17;
                equipmentDesc.setText(data17);
                Log.d("dataLoaded", "12");
                prod18 = c11.getString(pro18);
                data18 = data18 + prod18;
                subD.setText(data18);
                Log.d("dataLoaded", "13");
                prod19 = c11.getString(pro19);
                data19 = data19 + prod19;
                equipmentType.setText(data19);
                Log.d("dataLoaded", "14");
                prod20 = c11.getString(pro20);
                data20 = data20 + prod20;
                equipmentSize.setText(data20);
                Log.d("dataLoaded", "15");
                prod22 = c11.getString(pro22);
                data22 = data22 + prod22;
                equipmentId.setText(data22);
                Log.d("dataLoaded", "16");
                prod23 = c11.getString(pro23);
                data23 = data23 + prod23;
                mPosition.setText(data23);
                Log.d("dataLoaded", "17");

            } while (c11.moveToNext());
            Log.d("dataLoaded", "All Loaded");
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c11.close();
        db11.close();

    }

    protected void addHighLeaker() {

        camOpT = camOp.getSelectedItem().toString();
        camSerialT = camSerial.getSelectedItem().toString();
        gasSurveyOpT = gasSurveyOp.getSelectedItem().toString();
        gasSurveySerialT = gasSurveySerial.getSelectedItem().toString();
        leakerT = searchEt.getText().toString();

        if (camOpT.trim().length() > 0 && camSerialT.trim().length() > 0
                && gasSurveyOpT.trim().length() > 0
                && gasSurveySerialT.trim().length() > 0) {
            // database handler
            if (bTypes.getSelectedItem().toString().equals("Filling Station")) {
                FHighData db = new FHighData(getApplicationContext());

                // inserting new label into database
                db.updateFHighLeaker(leakerT, correctedReading, correctedR,
                        strMResult, strRecommendation);

                db.close();
            } else if (bTypes.getSelectedItem().toString()
                    .equals("Valve Station")) {
                VHighData db = new VHighData(getApplicationContext());

                // inserting new label into database
                db.updateHighLeaker(leakerT, correctedReading, correctedR,
                        strMResult, strRecommendation);

                db.close();
            } else if (bTypes.getSelectedItem().toString().equals("Depot")) {
                DHighData db = new DHighData(getApplicationContext());

                // inserting new label into database
                db.updateHighLeaker(leakerT, correctedReading, correctedR,
                        strMResult, strRecommendation);

                db.close();
            }

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();

            Toast.makeText(
                    getBaseContext(),
                    "Cam Operator:	" + camOpT + "\n" + "Cam Serial:	"
                            + camSerialT + "\n" + "Gas Surveyor Operator:	"
                            + gasSurveyOpT + "\n" + "Gas Surveyor Serial:	"
                            + gasSurveySerialT + "\n"
                            + "Measurement Position: " + mPositionT + "\n"
                            + "Measurement Result: " + strMResult + "\n"
                            + "Reading Unit: " + reading + "\n"
                            + "Date Created: " + currentDate, Toast.LENGTH_LONG)
                    .show();

        } else {
            Toast.makeText(getApplicationContext(), "No Empty Fields",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void populateSpinners2() {
        // TODO Auto-generated method stub
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> pCamOps = db.getAllCamOps();
        List<String> pCamSerials = db.getAllCamSerials();
        List<String> pSurveyOps = db.getAllSurveyOps();
        List<String> pSurveySerials = db.getAllSurveySerials();
        List<String> cycless = new ArrayList<String>();
        cycless.add("Cycle 1");
        cycless.add("Cycle 2");
        cycless.add("Cycle 3");
        cycless.add("Cycle 4");
        cycless.add("Cycle 5");
        cycless.add("Cycle 6");
        cycless.add("Cycle 7");
        cycless.add("Cycle 8");
        cycless.add("Cycle 9");

        List<String> types = new ArrayList<String>();
        types.add("Filling Station");
        types.add("Valve Station");
        types.add("Depot");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterCamOps = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pCamOps);

        ArrayAdapter<String> dataAdapterCamSerials = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pCamSerials);

        ArrayAdapter<String> dataAdapterSurveyOps = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pSurveyOps);

        ArrayAdapter<String> dataAdapterSurveySerials = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pSurveySerials);

        ArrayAdapter<String> dataAdapterCycles = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, cycless);

        ArrayAdapter<String> dataAdapterTypes = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, types);

        // Drop down layout style - list view with radio button
        dataAdapterCamOps
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterCamSerials
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterSurveyOps
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterSurveySerials
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterCycles
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterTypes
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        camOp.setAdapter(dataAdapterCamOps);
        camSerial.setAdapter(dataAdapterCamSerials);
        gasSurveyOp.setAdapter(dataAdapterSurveyOps);
        gasSurveySerial.setAdapter(dataAdapterSurveySerials);
        cycleCom.setAdapter(dataAdapterCycles);
        bTypes.setAdapter(dataAdapterTypes);

    }

    protected void addCamOp() {
        // TODO Auto-generated method stub
        camOpData = camOpTxt.getText().toString();
        dateCreateT = currentDate;
        dateModT = currentDate;
        if (camOpT.trim().length() > 0) {
            // database handler
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            // inserting new label into database
            db.insertCamOp(camOpT, dateCreateT, dateModT);

            // making input filed text to blank
            camOpTxt.setText("");

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();

            // Hiding the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(camOpTxt.getWindowToken(), 0);

            // loading spinner with newly added data
            populateSpinners2();
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Value",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCompletetion() {
        // TODO Auto-generated method stub

        if (camOpPressed == "True" && camSerialPressed == "True"
                && gasSurveyOpPressed == "True"
                && gasSurveySerialPressed == "True") {

            updateB.setEnabled(true);
            checkImages();
        } else if (camOpPressed == "False" && camSerialPressed == "False"
                && gasSurveyOpPressed == "False"
                && gasSurveySerialPressed == "False") {
            updateB.setEnabled(false);
            checkImages();
        }
    }

    private void checkImages() {
        // TODO Auto-generated method stub

        if (updateB.isEnabled()) {
            updateB.setBackgroundResource(R.drawable.submit);
        } else {
            updateB.setBackgroundResource(R.drawable.submit_inactive);
        }
    }

    private void initialiseComponents() {
        // TODO Auto-generated method stub

        updateB = (Button) findViewById(R.id.loHsubmitBtn1);
        backB = (Button) findViewById(R.id.loHbackBtn1);
        clearB = (Button) findViewById(R.id.loHclearBtn1);
        loadHD = (Button) findViewById(R.id.loHsearchBtn);
        addP = (Button) findViewById(R.id.loHaddPBtn);

        addCamOp = (Button) findViewById(R.id.loHaddCamOpBtn1);
        addCamSerial = (Button) findViewById(R.id.loHaddCamSerialBtn1);
        addGasSurveyOp = (Button) findViewById(R.id.loHaddGasOpBtn1);
        addGasSurveySerial = (Button) findViewById(R.id.loHaddGasSurveySerialBtn1);

        ppm = (RadioButton) findViewById(R.id.loHradioPPM1);
        lel = (RadioButton) findViewById(R.id.loHradioLEL1);
        gas = (RadioButton) findViewById(R.id.loHradioGAS1);

        camOp = (Spinner) findViewById(R.id.loHcamOperatorCombo1);
        camSerial = (Spinner) findViewById(R.id.loHcamSerialCombo1);
        gasSurveyOp = (Spinner) findViewById(R.id.loHgasSurveyOpCombo1);
        gasSurveySerial = (Spinner) findViewById(R.id.loHgasSurveySerialCombo1);
        searchSp1 = (Spinner) findViewById(R.id.loHsearchSpr2);
        cycleCom = (Spinner) findViewById(R.id.loHcycCom);
        bTypes = (Spinner) findViewById(R.id.loHtypeSprH);

        camOpTxt = (EditText) findViewById(R.id.loHaddCamOpTxt1);
        camSerialTxt = (EditText) findViewById(R.id.loHaddCamSerialTxt1);
        gasSurveyOpTxt = (EditText) findViewById(R.id.loHaddSurveyOpTxt1);
        gasSurveySerialTxt = (EditText) findViewById(R.id.loHaddSurveySerialTxt1);
        mResult = (EditText) findViewById(R.id.loHmeasurementTxt1);
        backGr = (EditText) findViewById(R.id.loHbackGTxt1);
        searchEt = (EditText) findViewById(R.id.loHsearchET);
        proTt = (EditText) findViewById(R.id.loHproductTt);
        streamTt = (EditText) findViewById(R.id.loHstreamTt);
        cfTt = (EditText) findViewById(R.id.loHcfTt);

        t1 = (TableRow) findViewById(R.id.loHt1);
        t2 = (TableRow) findViewById(R.id.loHt2);
        t3 = (TableRow) findViewById(R.id.loHt3);
        t4 = (TableRow) findViewById(R.id.loHt4);
        t5 = (TableRow) findViewById(R.id.loHt5);
        t6 = (TableRow) findViewById(R.id.loHt6);
        t7 = (TableRow) findViewById(R.id.loHt7);
        t8 = (TableRow) findViewById(R.id.loHt8);
        t9 = (TableRow) findViewById(R.id.loHt9);

        camOpTxt.setSingleLine();
        camSerialTxt.setSingleLine();
        gasSurveyOpTxt.setSingleLine();
        gasSurveySerialTxt.setSingleLine();
        mResult.setSingleLine();
        backGr.setSingleLine();

        camOpTxt.setVisibility(View.GONE);
        camSerialTxt.setVisibility(View.GONE);
        gasSurveyOpTxt.setVisibility(View.GONE);
        gasSurveySerialTxt.setVisibility(View.GONE);

        date = (TextView) findViewById(R.id.loHdateTxt2);
        fLoc = (TextView) findViewById(R.id.loHfhloc);
        fID = (TextView) findViewById(R.id.loHfhSID);
        fCon = (TextView) findViewById(R.id.loHfhContact);
        fDraw = (TextView) findViewById(R.id.loHfhDraw);

        vLoc = (TextView) findViewById(R.id.loHvhloc);
        vDraw = (TextView) findViewById(R.id.loHvhDraw);

        dLoc = (TextView) findViewById(R.id.loHdhloc);
        dDraw = (TextView) findViewById(R.id.loHdhDraw);

        c1 = (TextView) findViewById(R.id.loHc1);
        c2 = (TextView) findViewById(R.id.loHc2);
        c3 = (TextView) findViewById(R.id.loHc3);
        c4 = (TextView) findViewById(R.id.loHc4);
        c5 = (TextView) findViewById(R.id.loHc5);
        c6 = (TextView) findViewById(R.id.loHc6);
        c7 = (TextView) findViewById(R.id.loHc7);
        c8 = (TextView) findViewById(R.id.loHc8);
        c9 = (TextView) findViewById(R.id.loHc9);
        c11 = (TextView) findViewById(R.id.loHc11);
        c21 = (TextView) findViewById(R.id.loHc21);
        c31 = (TextView) findViewById(R.id.loHc31);
        c41 = (TextView) findViewById(R.id.loHc41);
        c51 = (TextView) findViewById(R.id.loHc51);
        c61 = (TextView) findViewById(R.id.loHc61);
        c71 = (TextView) findViewById(R.id.loHc71);
        c81 = (TextView) findViewById(R.id.loHc81);
        c91 = (TextView) findViewById(R.id.loHc91);

        fTab = (TableLayout) findViewById(R.id.loHfhTable);
        vTab = (TableLayout) findViewById(R.id.loHvhTable);
        dTab = (TableLayout) findViewById(R.id.loHdhTable);

        proTt.setVisibility(View.GONE);
        streamTt.setVisibility(View.GONE);
        cfTt.setVisibility(View.GONE);
        addP.setVisibility(View.GONE);

        fTab.setVisibility(View.GONE);
        dTab.setVisibility(View.GONE);
        vTab.setVisibility(View.GONE);

        product = (TextView) findViewById(R.id.loHmProduct);
        equipmentDesc = (TextView) findViewById(R.id.loHmEquipDesc);
        equipmentType = (TextView) findViewById(R.id.loHmEquipType);
        equipmentSize = (TextView) findViewById(R.id.loHmEquipSize);
        equipmentId = (TextView) findViewById(R.id.loHmEquipID);
        mPosition = (TextView) findViewById(R.id.loHmMPosition);
        subD = (TextView) findViewById(R.id.loHmSubDesc);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        date.setText(currentDate);
        searchSp1.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
    }

    private void checkResults() {
        if (c1.getText().toString().equals("null")) {
            t1.setVisibility(View.GONE);
        } else {
            t1.setVisibility(View.VISIBLE);
        }
        if (c2.getText().toString().equals("null")) {
            t2.setVisibility(View.GONE);
        } else {
            t2.setVisibility(View.VISIBLE);
        }
        if (c3.getText().toString().equals("null")) {
            t3.setVisibility(View.GONE);
        } else {
            t3.setVisibility(View.VISIBLE);
        }
        if (c4.getText().toString().equals("null")) {
            t4.setVisibility(View.GONE);
        } else {
            t4.setVisibility(View.VISIBLE);
        }
        if (c5.getText().toString().equals("null")) {
            t5.setVisibility(View.GONE);
        } else {
            t5.setVisibility(View.VISIBLE);
        }
        if (c6.getText().toString().equals("null")) {
            t6.setVisibility(View.GONE);
        } else {
            t6.setVisibility(View.VISIBLE);
        }
        if (c7.getText().toString().equals("null")) {
            t7.setVisibility(View.GONE);
        } else {
            t7.setVisibility(View.VISIBLE);
        }
        if (c8.getText().toString().equals("null")) {
            t8.setVisibility(View.GONE);
        } else {
            t8.setVisibility(View.VISIBLE);
        }
        if (c9.getText().toString().equals("null")) {
            t9.setVisibility(View.GONE);
        } else {
            t9.setVisibility(View.VISIBLE);
        }

        if (c11.getText().toString().equals("null")) {
            c11.setVisibility(View.GONE);
        } else {
            c11.setVisibility(View.VISIBLE);
        }
        if (c21.getText().toString().equals("null")) {
            c21.setVisibility(View.GONE);
        } else {
            c21.setVisibility(View.VISIBLE);
        }
        if (c31.getText().toString().equals("null")) {
            c31.setVisibility(View.GONE);
        } else {
            c31.setVisibility(View.VISIBLE);
        }
        if (c41.getText().toString().equals("null")) {
            c41.setVisibility(View.GONE);
        } else {
            c41.setVisibility(View.VISIBLE);
        }
        if (c51.getText().toString().equals("null")) {
            c51.setVisibility(View.GONE);
        } else {
            c51.setVisibility(View.VISIBLE);
        }
        if (c61.getText().toString().equals("null")) {
            c61.setVisibility(View.GONE);
        } else {
            c61.setVisibility(View.VISIBLE);
        }
        if (c71.getText().toString().equals("null")) {
            c71.setVisibility(View.GONE);
        } else {
            c71.setVisibility(View.VISIBLE);
        }
        if (c81.getText().toString().equals("null")) {
            c81.setVisibility(View.GONE);
        } else {
            c81.setVisibility(View.VISIBLE);
        }
        if (c91.getText().toString().equals("null")) {
            c91.setVisibility(View.GONE);
        } else {
            c91.setVisibility(View.VISIBLE);
        }
    }
}