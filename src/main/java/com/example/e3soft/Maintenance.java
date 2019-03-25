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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Maintenance extends Activity {

    private Button addComment;

    private Spinner comment, sType;

    private String commentT, typeT, outcomeT;

    private EditText com;

    private Button updateB, clearB, loadHD, addP;

    private String correctedReading, correctedR, cycT;

    private EditText backGr;

    private RadioButton ppm, lel, gas;

    private EditText mResult, searchEt;

    private TextView date;

    private TextView site, area, unitNumber, unit, drawing, unitSize,
            equipmentLoc, specificLoc, stream, system, product, equipmentDesc,
            equipmentType, equipmentSize, equipmentId, mPosition, dateCreate,
            subD, leaker;

    private String leakerIDs,
            strRecommendation;

    private String addPro, addCf, addStream;

    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT,
            dateCreateT, dateModT, currentDate, mPositionT, reading;

    private String siteT, areaT, unitNumberT, unitT, unitSizeT, drawingT,
            equipmentLocT, specificLocT, streamT, systemT;

    private String strMResult, strBackG, strCf, strMRes, DATE_TAKEN;

    private String productT, equipmentDescT, subDescriptionT,
            equipmentTypeT, mResultT, aLossT, readingT, equipmentSizeT,
            equipmentClassT, equipmentIdT, dateMod, recomT, reportT, image,
            leakerT, oldLeakerT, cycleT, oldReadT;

    private Double cf = 0.0, mRes = 0.0, lRa = 0.0, trendF = 0.0, backG = 0.0,
            sv = 0.0, correctedReading1;

    private TextView c1, c2, c3, c4, c5, c6, c7, c8, c9;
    private TextView c11, c21, c31, c41, c51, c61, c71, c81, c91;

    private TableRow t1, t2, t3, t4, t5, t6, t7, t8, t9;

    private String c1T, c2T, c3T, c4T, c5T, c6T, c7T, c8T, c9T, e0T;
    private String c11T, c21T, c31T, c41T, c51T, c61T, c71T, c81T, c91T;

    private TableLayout tab1, tab2;

    private RadioButton rep, notRep;

    private String reps, tmpSs;

    private RadioGroup repG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        initialiseComponents();
        leakerIDs = "1";
        populateText();
        populatePrevResults();
        checkResults();
        populateComments();

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
                    if (typeT.equals("Maintenance")) {
                        calculateOutcome();
                    }
                    addHighLeaker();
                    try {
                        saveHighcsv();
                        savehighExcelcsv();
                        Log.d("Data", "saved");
                        populateText();
                        populatePrevResults();
                        checkResults();

                        ppm.setVisibility(View.VISIBLE);
                        lel.setVisibility(View.VISIBLE);
                        gas.setVisibility(View.VISIBLE);

                        mResult.setText("");
                        backGr.setText("");
                        searchEt.setText("");
                        searchEt.requestFocus();

                        currentDate = DateUtils.formatDateTime(
                                getBaseContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_NUMERIC_DATE
                                        | DateUtils.FORMAT_24HOUR);

                        date.setText(currentDate);

                        RequestAction();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

            }

        });

        addComment.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                commentT = com.getText().toString();
                if (com.isShown()) {
                    com.setVisibility(View.GONE);
                    addComment.setBackgroundResource(R.drawable.edit);
                    addComment.setText("ADD");

                    if (commentT.trim().length() > 0) {
                        // database handler
                        CommentData db = new CommentData(
                                getApplicationContext());

                        // inserting new label into database
                        db.insertComment(commentT);

                        // making input filed text to blank
                        com.setText("");

                        Toast.makeText(getApplicationContext(), "Saved",
                                Toast.LENGTH_SHORT).show();
                        populateComments();
                        // Hiding the keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(com.getWindowToken(), 0);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter Value", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    com.setVisibility(View.VISIBLE);
                    addComment.setBackgroundResource(R.drawable.save);
                    addComment.setText("SAVE");
                }
            }
        });

        comment.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                commentT = comment.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        sType.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                typeT = sType.getSelectedItem().toString();
                if (typeT.equals("Maintenance")) {
                    tab1.setVisibility(View.GONE);
                    tab2.setVisibility(View.GONE);
                    repG.setVisibility(View.VISIBLE);
                } else {
                    tab1.setVisibility(View.VISIBLE);
                    tab2.setVisibility(View.VISIBLE);
                    repG.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        loadHD.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerIDs = searchEt.getText().toString();
                populateText();
                populatePrevResults();
                checkResults();
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

                ppm.setVisibility(View.VISIBLE);
                lel.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);

                Toast.makeText(getBaseContext(), "Cleared all Data",
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    protected void calculateOutcome() {
        if (cycleT.equals("Cycle 1")) {
            tmpSs = c1.getText().toString();
        } else if (cycleT.equals("Cycle 2")) {
            tmpSs = c2.getText().toString();
        } else if (cycleT.equals("Cycle 3")) {
            tmpSs = c3.getText().toString();
        } else if (cycleT.equals("Cycle 4")) {
            tmpSs = c4.getText().toString();
        } else if (cycleT.equals("Cycle 5")) {
            tmpSs = c5.getText().toString();
        } else if (cycleT.equals("Cycle 6")) {
            tmpSs = c6.getText().toString();
        } else if (cycleT.equals("Cycle 7")) {
            tmpSs = c7.getText().toString();
        } else if (cycleT.equals("Cycle 8")) {
            tmpSs = c8.getText().toString();
        } else if (cycleT.equals("Cycle 9")) {
            tmpSs = c9.getText().toString();
        }

        Double tmpOld = 0.0;
        if (tmpSs.equals("") || tmpSs.equals("null")) {
            tmpSs = "0";
            tmpOld = 0.0;
        } else {
            tmpOld = Double.parseDouble(tmpSs);
        }

        Double tmpNew = Double.parseDouble(correctedReading);
        if (rep.isChecked()) {
            reps = "Repair Attempted";
        } else {
            reps = "Repair Not Attempted";
        }
        if (reps.equals("Repair Not Attempted") || (commentT.equals("SD"))) {
            outcomeT = "Not repaired due to inaccessibility";
        } else if (tmpNew >= tmpOld) {
            outcomeT = "Tightening not successful";
        } else if (tmpNew > 10000) {
            outcomeT = "Reduced but still significant";
        } else if (tmpNew > 1000) {
            outcomeT = "Reduced but still needs attention";
        } else if (tmpNew <= 1000) {
            outcomeT = "Repaired";
        }
    }

    private void populateComments() {
        CommentData db = new CommentData(getApplicationContext());

        // Spinner Drop down elements
        List<String> pComms = db.getAllComments();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterComms = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pComms);
        // Drop down layout style - list view with radio button
        dataAdapterComms
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comment.setAdapter(dataAdapterComms);
    }

    protected void RequestAction() {

        new AlertDialog.Builder(this).setTitle("HIGH LEAKER SAVED")
                .setMessage("DO ANOTHER SEARCH?").setCancelable(false)
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(Maintenance.this, Home.class);
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("YES", null).show();
    }

    private void populatePrevResults() {
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


        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/highLeaker.db", null, 0);
        Cursor c10 = db10.rawQuery("SELECT *" + " FROM highLeaker "
                + "WHERE highLeaker.leakerID = '" + leakerIDs + "'", null);

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

    protected void savehighExcelcsv() throws IOException {
        if (typeT.equals("Maintenance")) {
            String tmpDate = dateCreateT.substring(7);
            String tmpTime = dateCreateT.substring(0, 5);
            String direc = "/e3softData/";
            String fileName = "maintenanceExcel.csv";
            String tmpAre = areaT.substring(4);
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
                String data = tmpDate + "," + leakerT + "," + tmpAre + ","
                        + unitNumberT + "," + unitT + "," + streamT + ","
                        + productT + "," + specificLocT + "," + equipmentLocT
                        + "," + drawingT + "," + systemT + "," + equipmentDescT
                        + "," + equipmentTypeT + "," + equipmentSizeT + ","
                        + equipmentIdT + "," + mPositionT + "," + mResultT
                        + "," + subDescriptionT + "," + oldReadT + "," + cycleT
                        + "," + gasSurveyOpT + "," + gasSurveySerialT + ","
                        + aLossT + "," + tmpTime + "," + outcomeT + ","
                        + commentT + "," + reps + "," + strRecommendation + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            } else {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                createHead();
                String data = tmpDate + "," + leakerT + "," + tmpAre + ","
                        + unitNumberT + "," + unitT + "," + streamT + ","
                        + productT + "," + specificLocT + "," + equipmentLocT
                        + "," + drawingT + "," + systemT + "," + equipmentDescT
                        + "," + equipmentTypeT + "," + equipmentSizeT + ","
                        + equipmentIdT + "," + mPositionT + "," + mResultT
                        + "," + subDescriptionT + "," + oldReadT + "," + cycleT
                        + "," + gasSurveyOpT + "," + gasSurveySerialT + ","
                        + aLossT + "," + tmpTime + "," + outcomeT + ","
                        + commentT + "," + reps + "," + strRecommendation + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            }
        } else {
            String tmpDate = dateCreateT.substring(7);
            String tmpTime = dateCreateT.substring(0, 6);
            String direc = "/e3softData/";
            String fileName = "highLeakerExcel.csv";
            String tmpAre = areaT.substring(4);
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
                String data = tmpDate + "," + leakerT + "," + tmpAre + ","
                        + unitNumberT + "," + unitT + "," + streamT + ","
                        + productT + "," + specificLocT + "," + equipmentLocT
                        + "," + drawingT + "," + systemT + "," + equipmentDescT
                        + "," + equipmentTypeT + "," + equipmentSizeT + ","
                        + equipmentIdT + "," + mPositionT + "," + mResultT
                        + "," + subDescriptionT + "," + oldReadT + "," + cycleT
                        + "," + camOpT + "," + camSerialT + "," + gasSurveyOpT
                        + "," + gasSurveySerialT + "," + aLossT + "," + strRecommendation + "," + tmpTime
                        + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            } else {
                String f = file.toString();
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(file, true));
                createHead();
                String data = tmpDate + "," + leakerT + "," + tmpAre + ","
                        + unitNumberT + "," + unitT + "," + streamT + ","
                        + productT + "," + specificLocT + "," + equipmentLocT
                        + "," + drawingT + "," + systemT + "," + equipmentDescT
                        + "," + equipmentTypeT + "," + equipmentSizeT + ","
                        + equipmentIdT + "," + mPositionT + "," + mResultT
                        + "," + subDescriptionT + "," + oldReadT + "," + cycleT
                        + "," + camOpT + "," + camSerialT + "," + gasSurveyOpT
                        + "," + gasSurveySerialT + "," + aLossT + "," + strRecommendation + "," + tmpTime
                        + "\n";
                bw.append(data);
                bw.flush();
                bw.close();
            }
        }
    }

    private void createHead() throws IOException {
        if (typeT.equals("Maintenance")) {
            String direc = "/e3softData/";
            String fileName = "maintenanceExcel.csv";
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
            String Data2 = "Date,Report #,Area,Unit #,Unit Name,"
                    + "Stream,Product,Specific Information,Equip Location,Drawing,System,"
                    + "Equip Desc,Equip Type,"
                    + "Equip Size,Equip ID,Leak Source,"
                    + "Measurement Result (PPM),"
                    + "Maintenance Type,Prev Measurement Result (PPM),Cycle,Gas Surveyor,Sniffer Serial #,Annual Loss (KG),Time,Outcome,Comment,Repair Status,Recommendations"
                    + "\n";
            bw.write(Data2);
            bw.flush();
            bw.close();
        } else {
            String direc = "/e3softData/";
            String fileName = "highLeakerExcel.csv";
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
            String Data2 = "Date,Report #,Area,Unit #,Unit Name,"
                    + "Stream,Product,Specific Information,Equip Location,Drawing,System,"
                    + "Equip Desc,Equip Type,"
                    + "Equip Size,Equip ID,Leak Source,"
                    + "Measurement Result (PPM),"
                    + "Maintenance Type,Prev Measurement Result (PPM),Cycle,Camera Operator,Camera Serial #,Gas Surveyor,Sniffer Serial #,Annual Loss (KG),Recommendations,Time"
                    + "\n";
            bw.write(Data2);
            bw.flush();
            bw.close();
        }
    }

    protected void saveHighcsv() throws IOException {
        // TODO Auto-generated method stub
        if (typeT.equals("Maintenance")) {
            String direc = "/e3softData/";
            String fileName = "maintenance.csv";
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

            String data = "'" + siteT + "'" + "," + "'" + areaT + "'" + ","
                    + "'" + unitNumberT + "'" + "," + "'" + unitT + "'" + ","
                    + "'" + unitSizeT + "'" + "," + "'" + drawingT + "'" + ","
                    + "'" + streamT + "'" + "," + "'" + specificLocT + "'"
                    + "," + "'" + systemT + "'" + "," + "'" + equipmentLocT
                    + "'" + "," + "'" + camOpT + "'" + "," + "'" + camSerialT
                    + "'" + "," + "'" + gasSurveyOpT + "'" + "," + "'"
                    + gasSurveySerialT + "'" + "," + "'" + productT + "'" + ","
                    + "'" + equipmentDescT + "'" + "," + "'" + subDescriptionT
                    + "'" + "," + "'" + equipmentTypeT + "'" + "," + "'"
                    + equipmentSizeT + "'" + "," + "'" + equipmentClassT + "'"
                    + "," + "'" + equipmentIdT + "'" + "," + "'" + mPositionT
                    + "'" + "," + "'" + mResultT + "'" + "," + "'" + oldReadT
                    + "'" + "," + "'" + readingT + "'" + "," + "'" + aLossT
                    + "'" + "," + "'" + strRecommendation + "'" + "," + "'"
                    + outcomeT + "'," + "'" + commentT + "'," + "'"
                    + dateCreateT + "'" + "," + "'" + leakerIDs + "';" + "\n";
            bw.write(data);
            bw.flush();
            bw.close();
        } else {
            String direc = "/e3softData/";
            String fileName = "highLeaker.csv";
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

            String data = "'" + siteT + "'" + "," + "'" + areaT + "'" + ","
                    + "'" + unitNumberT + "'" + "," + "'" + unitT + "'" + ","
                    + "'" + unitSizeT + "'" + "," + "'" + drawingT + "'" + ","
                    + "'" + streamT + "'" + "," + "'" + specificLocT + "'"
                    + "," + "'" + systemT + "'" + "," + "'" + equipmentLocT
                    + "'" + "," + "'" + camOpT + "'" + "," + "'" + camSerialT
                    + "'" + "," + "'" + gasSurveyOpT + "'" + "," + "'"
                    + gasSurveySerialT + "'" + "," + "'" + productT + "'" + ","
                    + "'" + equipmentDescT + "'" + "," + "'" + subDescriptionT
                    + "'" + "," + "'" + equipmentTypeT + "'" + "," + "'"
                    + equipmentSizeT + "'" + "," + "'" + equipmentClassT + "'"
                    + "," + "'" + equipmentIdT + "'" + "," + "'" + mPositionT
                    + "'" + "," + "'" + mResultT + "'" + "," + "'" + readingT
                    + "'" + "," + "'" + aLossT + "'" + "," + "'"
                    + strRecommendation + "'" + "," + "'" + dateCreateT + "'"
                    + "," + "'" + leakerIDs + "';" + "\n";
            bw.write(data);
            bw.flush();
            bw.close();
        }
    }

    protected void loadCf() {
        String data222 = "";
        String prod222 = "";
        streamT = stream.getText().toString();
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/newPros.db", null, 0);
        Cursor c2 = db.rawQuery(
                "SELECT pr.cf FROM nwPros pr where pr.stream = " + "'"
                        + streamT + "'", null);
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
        correctedReading1 = (cf * mRes);
        calculateRecomedation();
        correctedReading = correctedReading1.toString();
        correctedR = "PPM";
        sv = ((cf * mRes) - backG);

        if (sv <= 2000) {
            lRa = (sv * 0.01);
        } else if (sv <= 10000) {
            lRa = ((sv * 0.1225) - 225);
        } else if (sv <= 50000) {
            lRa = ((sv * 0.05) + 500);
        } else {
            lRa = ((sv * 0.04) + 1000);
        }

        strMResult = lRa.toString((double) lRa);
        mResultT = correctedReading;
        aLossT = strMResult;
        readingT = "PPM";
    }

    private void calculateRecomedation() {
        if (correctedReading1 >= 20000) {
            strRecommendation = "DANGER";
        } else if (correctedReading1 >= 10000) {
            strRecommendation = "URGENT";
        } else if (correctedReading1 >= 5000) {
            strRecommendation = "Maintenance Required";
        } else if (correctedReading1 > 1000) {
            strRecommendation = "Consider Maintenance";
        } else if (correctedReading1 <= 1000) {
            strRecommendation = "Acceptable";
        }
    }

    protected void populateVariables() {
        // TODO Auto-generated method stub
        siteT = site.getText().toString();
        areaT = area.getText().toString();
        unitNumberT = unitNumber.getText().toString();
        unitT = unit.getText().toString();
        unitSizeT = unitSize.getText().toString();

        drawingT = drawing.getText().toString();
        equipmentLocT = equipmentLoc.getText().toString();
        specificLocT = specificLoc.getText().toString();
        streamT = stream.getText().toString();
        systemT = system.getText().toString();

        productT = product.getText().toString();
        equipmentDescT = equipmentDesc.getText().toString();
        subDescriptionT = subD.getText().toString();
        equipmentTypeT = equipmentType.getText().toString();
        equipmentSizeT = equipmentSize.getText().toString();
        equipmentIdT = equipmentId.getText().toString();
        mPositionT = mPosition.getText().toString();
        dateCreateT = currentDate;
        equipmentClassT = "0";
        oldLeakerT = c1.getText().toString();
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

        SQLiteDatabase db11 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/highLeaker.db", null, 0);
        Cursor c11 = db11.rawQuery("SELECT " + "highLeaker.site, "
                + "highLeaker.area, " + "highLeaker.unit, "
                + "highLeaker.unitName, " + "highLeaker.unitSize, "
                + "highLeaker.drawing, " + "highLeaker.stream, "
                + "highLeaker.leakerID, " + "highLeaker.specificLoc, "
                + "highLeaker.system, " + "highLeaker.equipmentLoc, "
                + "highLeaker.product, " + "highLeaker.equipmentDesc, "
                + "highLeaker.subDescription, " + "highLeaker.equipmentType, "
                + "highLeaker.equipmentSize, " + "highLeaker.equipmentID, "
                + "highLeaker.measurementPosition, " + "highLeaker.date "
                + "FROM highLeaker" + " WHERE highLeaker.leakerID = '"
                + leakerIDs + "'", null);

        int pro2 = c11.getColumnIndex("site");
        int pro3 = c11.getColumnIndex("area");
        int pro4 = c11.getColumnIndex("unit");
        int pro5 = c11.getColumnIndex("unitName");
        int pro6 = c11.getColumnIndex("unitSize");
        int pro7 = c11.getColumnIndex("drawing");
        int pro8 = c11.getColumnIndex("stream");
        int pro31 = c11.getColumnIndex("leakerID");
        int pro9 = c11.getColumnIndex("specificLoc");
        int pro10 = c11.getColumnIndex("system");
        int pro11 = c11.getColumnIndex("equipmentLoc");

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
                site.setText(data2);
                Log.d("dataLoaded", "1");

                prod3 = c11.getString(pro3);
                data3 = data3 + prod3;
                area.setText(data3);
                Log.d("dataLoaded", "2");
                prod4 = c11.getString(pro4);
                data4 = data4 + prod4;
                unitNumber.setText(data4);
                Log.d("dataLoaded", "3");
                prod5 = c11.getString(pro5);
                data5 = data5 + prod5;
                unit.setText(data5);
                Log.d("dataLoaded", "4");
                prod6 = c11.getString(pro6);
                data6 = data6 + prod6;
                unitSize.setText(data6);
                Log.d("dataLoaded", "5");
                prod7 = c11.getString(pro7);
                data7 = data7 + prod7;
                drawing.setText(data7);
                Log.d("dataLoaded", "6");
                prod8 = c11.getString(pro8);
                data8 = data8 + prod8;
                stream.setText(data8);
                Log.d("dataLoaded", "7");
                prod9 = c11.getString(pro9);
                data9 = data9 + prod9;
                specificLoc.setText(data9);
                Log.d("dataLoaded", "8");
                prod10 = c11.getString(pro10);
                data10 = data10 + prod10;
                system.setText(data10);
                Log.d("dataLoaded", "9");
                prod11 = c11.getString(pro11);
                data11 = data11 + prod11;
                equipmentLoc.setText(data11);
                Log.d("dataLoaded", "10");

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
                prod31 = c11.getString(pro31);
                data31 = data31 + prod31;
                leaker.setText(data31);
                Log.d("dataLoaded", "19");

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

        leakerT = leaker.getText().toString();

        if (camOpT.trim().length() > 0 && camSerialT.trim().length() > 0
                && gasSurveyOpT.trim().length() > 0
                && gasSurveySerialT.trim().length() > 0) {
            // database handler
            HighData db = new HighData(getApplicationContext());

            // inserting new label into database
            db.updateHighLeaker2(leakerT, correctedReading, correctedR,
                    strMResult, strRecommendation, cycT, commentT);

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

        updateB = (Button) findViewById(R.id.submitBtn1);
        clearB = (Button) findViewById(R.id.clearBtn1);
        loadHD = (Button) findViewById(R.id.searchBtn);
        addComment = (Button) findViewById(R.id.addCommentBtn);


        ppm = (RadioButton) findViewById(R.id.radioPPM1);
        lel = (RadioButton) findViewById(R.id.radioLEL1);
        gas = (RadioButton) findViewById(R.id.radioGAS1);

        comment = (Spinner) findViewById(R.id.commentCombo);

        mResult = (EditText) findViewById(R.id.measurementTxt1);
        backGr = (EditText) findViewById(R.id.backGTxt1);
        searchEt = (EditText) findViewById(R.id.searchET);
        com = (EditText) findViewById(R.id.addCommentTxt);

        tab1 = (TableLayout) findViewById(R.id.tab1);
        tab2 = (TableLayout) findViewById(R.id.tab2);

        t1 = (TableRow) findViewById(R.id.t1);
        t2 = (TableRow) findViewById(R.id.t2);
        t3 = (TableRow) findViewById(R.id.t3);
        t4 = (TableRow) findViewById(R.id.t4);
        t5 = (TableRow) findViewById(R.id.t5);
        t6 = (TableRow) findViewById(R.id.t6);
        t7 = (TableRow) findViewById(R.id.t7);
        t8 = (TableRow) findViewById(R.id.t8);
        t9 = (TableRow) findViewById(R.id.t9);

        rep = (RadioButton) findViewById(R.id.radioRepair);
        notRep = (RadioButton) findViewById(R.id.radioNRepair);

        repG = (RadioGroup) findViewById(R.id.repGroup);

        mResult.setSingleLine();
        backGr.setSingleLine();
        com.setVisibility(View.GONE);
        com.setSingleLine();

        date = (TextView) findViewById(R.id.dateTxt2);
        site = (TextView) findViewById(R.id.mSite);
        area = (TextView) findViewById(R.id.mArea);
        unitNumber = (TextView) findViewById(R.id.mUnit);
        unit = (TextView) findViewById(R.id.mUnitName);
        drawing = (TextView) findViewById(R.id.mDrawing);
        unitSize = (TextView) findViewById(R.id.mUnitSize);
        equipmentLoc = (TextView) findViewById(R.id.mEquipLoc);
        specificLoc = (TextView) findViewById(R.id.mSpecificLoc);
        stream = (TextView) findViewById(R.id.mStream);
        system = (TextView) findViewById(R.id.mSystem);

        c1 = (TextView) findViewById(R.id.c1);
        c2 = (TextView) findViewById(R.id.c2);
        c3 = (TextView) findViewById(R.id.c3);
        c4 = (TextView) findViewById(R.id.c4);
        c5 = (TextView) findViewById(R.id.c5);
        c6 = (TextView) findViewById(R.id.c6);
        c7 = (TextView) findViewById(R.id.c7);
        c8 = (TextView) findViewById(R.id.c8);
        c9 = (TextView) findViewById(R.id.c9);
        c11 = (TextView) findViewById(R.id.c11Res);
        c21 = (TextView) findViewById(R.id.c21);
        c31 = (TextView) findViewById(R.id.c31);
        c41 = (TextView) findViewById(R.id.c41);
        c51 = (TextView) findViewById(R.id.c51);
        c61 = (TextView) findViewById(R.id.c61);
        c71 = (TextView) findViewById(R.id.c71);
        c81 = (TextView) findViewById(R.id.c81);
        c91 = (TextView) findViewById(R.id.c91);

        addP.setVisibility(View.GONE);

        product = (TextView) findViewById(R.id.mProduct);
        equipmentDesc = (TextView) findViewById(R.id.mEquipDesc);
        equipmentType = (TextView) findViewById(R.id.mEquipType);
        equipmentSize = (TextView) findViewById(R.id.mEquipSize);
        equipmentId = (TextView) findViewById(R.id.mEquipID);
        mPosition = (TextView) findViewById(R.id.mMPosition);
        subD = (TextView) findViewById(R.id.mSubDesc);
        leaker = (TextView) findViewById(R.id.lID);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        date.setText(currentDate);

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
        if (c1.getText().toString().equals("null") || c1.getText().toString().equals("")) {
            t1.setVisibility(View.GONE);
        } else {
            t1.setVisibility(View.VISIBLE);
        }
        if (c2.getText().toString().equals("null") || c2.getText().toString().equals("")) {
            t2.setVisibility(View.GONE);
        } else {
            t2.setVisibility(View.VISIBLE);
        }
        if (c3.getText().toString().equals("null") || c3.getText().toString().equals("")) {
            t3.setVisibility(View.GONE);
        } else {
            t3.setVisibility(View.VISIBLE);
        }
        if (c4.getText().toString().equals("null") || c4.getText().toString().equals("")) {
            t4.setVisibility(View.GONE);
        } else {
            t4.setVisibility(View.VISIBLE);
        }
        if (c5.getText().toString().equals("null") || c5.getText().toString().equals("")) {
            t5.setVisibility(View.GONE);
        } else {
            t5.setVisibility(View.VISIBLE);
        }
        if (c6.getText().toString().equals("null") || c6.getText().toString().equals("")) {
            t6.setVisibility(View.GONE);
        } else {
            t6.setVisibility(View.VISIBLE);
        }
        if (c7.getText().toString().equals("null") || c7.getText().toString().equals("")) {
            t7.setVisibility(View.GONE);
        } else {
            t7.setVisibility(View.VISIBLE);
        }
        if (c8.getText().toString().equals("null") || c8.getText().toString().equals("")) {
            t8.setVisibility(View.GONE);
        } else {
            t8.setVisibility(View.VISIBLE);
        }
        if (c9.getText().toString().equals("null") || c9.getText().toString().equals("")) {
            t9.setVisibility(View.GONE);
        } else {
            t9.setVisibility(View.VISIBLE);
        }

        if (c11.getText().toString().equals("null") || c11.getText().toString().equals("")) {
            c11.setVisibility(View.GONE);
        } else {
            c11.setVisibility(View.VISIBLE);
        }
        if (c21.getText().toString().equals("null") || c21.getText().toString().equals("")) {
            c21.setVisibility(View.GONE);
        } else {
            c21.setVisibility(View.VISIBLE);
        }
        if (c31.getText().toString().equals("null") || c31.getText().toString().equals("")) {
            c31.setVisibility(View.GONE);
        } else {
            c31.setVisibility(View.VISIBLE);
        }
        if (c41.getText().toString().equals("null") || c41.getText().toString().equals("")) {
            c41.setVisibility(View.GONE);
        } else {
            c41.setVisibility(View.VISIBLE);
        }
        if (c51.getText().toString().equals("null") || c51.getText().toString().equals("")) {
            c51.setVisibility(View.GONE);
        } else {
            c51.setVisibility(View.VISIBLE);
        }
        if (c61.getText().toString().equals("null") || c61.getText().toString().equals("")) {
            c61.setVisibility(View.GONE);
        } else {
            c61.setVisibility(View.VISIBLE);
        }
        if (c71.getText().toString().equals("null") || c71.getText().toString().equals("")) {
            c71.setVisibility(View.GONE);
        } else {
            c71.setVisibility(View.VISIBLE);
        }
        if (c81.getText().toString().equals("null") || c81.getText().toString().equals("")) {
            c81.setVisibility(View.GONE);
        } else {
            c81.setVisibility(View.VISIBLE);
        }
        if (c91.getText().toString().equals("null") || c91.getText().toString().equals("")) {
            c91.setVisibility(View.GONE);
        } else {
            c91.setVisibility(View.VISIBLE);
        }
    }
}