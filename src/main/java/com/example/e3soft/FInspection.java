package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class FInspection extends Activity {

    final static int img = 1;
    public static String ste, addProT, addCfT, addStreamT;
    public static String siteT, areaT, unitNumberT, unitT, unitSizeT, drawingT,
            equipmentLocT, specificLocT, streamT, systemT, leakerT;
    public static String products = "", streams = "";
    public static String data = "", data2 = "", byteImage, emerT;
    static int idsss = 0;
    Bitmap imageTa;
    byte[] byteArray;
    private String counter;
    private Button updateB, backB, refreshB, clearB, restartB;
    private Button addEquipDesc, addEquipType, addEquipSize, addEquipClass,
            addProduct, addMPosition, addP;
    private Spinner equipDesc, equipType, equipSize, equipClass, product,
            mPosition, subDesc;
    private EditText addEquipDescTxt, addEquipTypeTxt, addEquipSizeTxt,
            addEquipClassTxt, addMPositionTxt, equipID, mResult, backGr, proTt,
            streamTt, cfTt;
    private TextView date, streamss, cfs, pros;
    private RadioButton ppm, lel, gas;
    private String currentActivityName, activityName, currentDate, reading,
            correctedReading, correctedR;
    private String strMResult, strBackG, strCf, strMRes, DATE_TAKEN,
            strRecomedation;
    private String productT, equipmentDescT, subDescriptionT, equipmentTypeT,
            readingT = "PPM", equipmentSizeT, equipmentClassT, equipmentIdT,
            mPositionT, dateCreateT, dateModT;
    private String equipDescPressed, equipTypePressed, equipSizePressed,
            equipClassPressed, productPressed, mPositionPressed,
            cycTt = "Cycle 1";
    private Double cf = 0.0, mRes = 0.0, lRa = 0.0, trendF = 0.0, backG = 0.0,
            sv = 0.0;
    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT;
    private Bitmap bm;
    private Drawable imas;
    private String busiT, seq, loc, statID, cont, draw;
    private String depot, depotDrawing;
    private String valve, valveDrawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finspection);
        initialiseComponents();
        receiveData();
        loadSpinnerData();
        loadPro();

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                strMResult = mResult.getText().toString();
                strMRes = strMResult;

                equipmentDescT = equipDesc.getSelectedItem().toString();
                equipmentTypeT = equipType.getSelectedItem().toString();
                equipmentSizeT = equipSize.getSelectedItem().toString();
                equipmentClassT = equipClass.getSelectedItem().toString();
                productT = product.getSelectedItem().toString();
                equipmentIdT = equipID.getText().toString();
                mPositionT = mPosition.getSelectedItem().toString();
                subDescriptionT = subDesc.getSelectedItem().toString();
                if (equipmentClassT.equals("Choose 1")) {
                    equipmentClassT = "Undefined";
                }

                if (equipmentDescT.equals("Choose 1")
                        || equipmentTypeT.equals("Choose 1")
                        || equipmentSizeT.equals("Choose 1")
                        || equipmentIdT.equals("Choose 1")
                        || mPositionT.equals("Choose 1")) {
                    Toast.makeText(getBaseContext(),
                            "Please Complete all DropDowns", Toast.LENGTH_LONG)
                            .show();
                } else {
                    calcID();
                    calcCycle();
                    calculateBackG();
                    calculateLoss();
                    addHighLeaker();
                    addReport();
                    saveLe();
                    Log.d("database", "report saved");
                    Toast.makeText(
                            getBaseContext(),
                            "LR/a:	" + strMResult + " kg/annum" + "\n"
                                    + "Measurement Reading:	" + strMRes + "\n"
                                    + "Background Reading:	" + strBackG + "\n"
                                    + "Correction Factor:		" + strCf + "\n"
                                    + "Reading:		" + reading, Toast.LENGTH_LONG)
                            .show();
                    showReport();
                    ppm.setVisibility(View.VISIBLE);
                    lel.setVisibility(View.VISIBLE);
                    gas.setVisibility(View.VISIBLE);
                    mResult.setText("");
                    backGr.setText("");
                    equipID.setText("");
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(0);
                }
            }

            private void showReport() {
                if (busiT.equals("Filling Station")) {
                    Intent i = new Intent(FInspection.this, Freport.class);
                    startActivity(i);
                } else if (busiT.equals("Depot")) {
                    Intent i = new Intent(FInspection.this, Dreport.class);
                    startActivity(i);
                } else if (busiT.equals("Valve Station")) {
                    Intent i = new Intent(FInspection.this, Vreport.class);
                    startActivity(i);
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

            private void calculateLoss() {
                if (reading == "PPM") {
                    strMRes = mResult.getText().toString();
                    mRes = Double.parseDouble(strMRes);
                    Log.d("result", strMRes + "++result");
                } else if (reading == "LEL") {
                    strMRes = mResult.getText().toString();
                    mRes = Double.parseDouble(strMRes);
                    Log.d("result", strMRes + "++result");
                    mRes = (mRes * 1000);
                } else if (reading == "GAS %") {
                    strMRes = mResult.getText().toString();
                    mRes = Double.parseDouble(strMRes);
                    Log.d("result", strMRes + "++result");
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
                } else {
                    lRa = ((sv * 0.04) + 1000);
                }

                strMResult = Double.toString(lRa);
            }
        });

        restartB.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                TabActivity tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(0);
            }

        });

        backB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(FInspection.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        refreshB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                currentDate = DateUtils.formatDateTime(getBaseContext(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_NUMERIC_DATE
                                | DateUtils.FORMAT_24HOUR);

                date.setText(currentDate);
            }
        });

        addP.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                addProT = proTt.getText().toString();
                addStreamT = streamTt.getText().toString();
                addCfT = cfTt.getText().toString();
                updateProduct();
            }

            private void updateProduct() {
                DatabaseHandler dbr = new DatabaseHandler(
                        getApplicationContext());

                dbr.updateProduct(leakerT, addProT, addStreamT, addCfT);

                Log.d("database", "report Saved");
                dbr.close();

                proTt.setVisibility(View.GONE);
                streamTt.setVisibility(View.GONE);
                cfTt.setVisibility(View.GONE);
                addP.setVisibility(View.GONE);
            }
        });

        clearB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                addEquipDescTxt.setEnabled(true);
                addEquipTypeTxt.setEnabled(true);
                addEquipSizeTxt.setEnabled(true);
                addEquipClassTxt.setEnabled(true);
                addMPositionTxt.setEnabled(true);

                equipDesc.setEnabled(true);
                equipSize.setEnabled(true);
                product.setEnabled(true);
                equipType.setEnabled(true);
                equipClass.setEnabled(true);
                mPosition.setEnabled(true);

                addEquipDesc.setEnabled(true);
                addEquipSize.setEnabled(true);
                addProduct.setEnabled(true);
                addEquipType.setEnabled(true);
                addEquipClass.setEnabled(true);
                addMPosition.setEnabled(true);

                addEquipDesc.setBackgroundResource(R.drawable.edit);
                addEquipSize.setBackgroundResource(R.drawable.edit);
                addProduct.setBackgroundResource(R.drawable.edit);
                addEquipType.setBackgroundResource(R.drawable.edit);
                addEquipClass.setBackgroundResource(R.drawable.edit);
                addMPosition.setBackgroundResource(R.drawable.edit);

                mResult.setText("");
                mRes = 0.0;
                strBackG = "0";
                strMResult = "0";
                strMRes = "0";

                ppm.setVisibility(View.VISIBLE);
                lel.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);

            }
        });

        addProduct.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (proTt.isShown()) {
                    proTt.setVisibility(View.GONE);
                    streamTt.setVisibility(View.GONE);
                    cfTt.setVisibility(View.GONE);
                    addP.setVisibility(View.GONE);
                    pros.setVisibility(View.GONE);
                    streamss.setVisibility(View.GONE);
                    cfs.setVisibility(View.GONE);

                    addProduct.setBackgroundResource(R.drawable.edit);
                    addProduct.setText("ADD");
                    addProduct();
                } else {
                    addProduct.setBackgroundResource(R.drawable.save);
                    addProduct.setText("SAVE");
                    proTt.setVisibility(View.VISIBLE);
                    streamTt.setVisibility(View.VISIBLE);
                    cfTt.setVisibility(View.VISIBLE);
                    addP.setVisibility(View.VISIBLE);
                    pros.setVisibility(View.VISIBLE);
                    streamss.setVisibility(View.VISIBLE);
                    cfs.setVisibility(View.VISIBLE);
                    addEquipSizeTxt.setVisibility(View.GONE);
                    addEquipDescTxt.setVisibility(View.GONE);
                    addEquipClassTxt.setVisibility(View.GONE);
                    addMPositionTxt.setVisibility(View.GONE);
                    addEquipTypeTxt.setVisibility(View.GONE);

                    addEquipDesc.setText("ADD");
                    addEquipSize.setText("ADD");
                    addEquipType.setText("ADD");
                    addEquipClass.setText("ADD");
                    addMPosition.setText("ADD");

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

        product.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                loadCf();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    protected void addHighLeaker() {
        productT = product.getSelectedItem().toString();
        equipmentDescT = equipDesc.getSelectedItem().toString();
        subDescriptionT = subDesc.getSelectedItem().toString();
        equipmentTypeT = equipType.getSelectedItem().toString();
        equipmentSizeT = equipSize.getSelectedItem().toString();
        equipmentClassT = equipClass.getSelectedItem().toString();
        equipmentIdT = equipID.getText().toString();
        mPositionT = mPosition.getSelectedItem().toString();
        strMRes = mResult.getText().toString();
        readingT = reading;
        strBackG = backGr.getText().toString();

        if (busiT.equals("Filling Station")) {
            if (productT.trim().length() > 0
                    && equipmentDescT.trim().length() > 0
                    && subDescriptionT.trim().length() > 0
                    && equipmentTypeT.trim().length() > 0
                    && equipmentSizeT.trim().length() > 0
                    && equipmentClassT.trim().length() > 0
                    && equipmentIdT.trim().length() > 0
                    && mPositionT.trim().length() > 0
                    && strMRes.trim().length() > 0
                    && readingT.trim().length() > 0
                    && strBackG.trim().length() > 0) {

                // database handler
                FHighData db = new FHighData(getApplicationContext());

                // inserting new label into database
                //db.insertFHighLeaker(idsss, busiT, loc, statID, cont, draw,
                //		leakerT, camSerialT, gasSurveyOpT, camOpT,
                //		gasSurveySerialT, productT, equipmentDescT,
                //		subDescriptionT, equipmentTypeT, equipmentSizeT,
                //		equipmentIdT, mPositionT, correctedReading, correctedR,
                //		strMResult, strRecomedation, dateCreateT, cycTt);

                Toast.makeText(getApplicationContext(), "Saved",
                        Toast.LENGTH_SHORT).show();
                db.close();
                Log.d("highLeaker", "highLeaker Filling saved");
            } else {
                Toast.makeText(getApplicationContext(), "NO EMPTY FIELDS",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (busiT.equals("Valve Station")) {
            if (productT.trim().length() > 0
                    && equipmentDescT.trim().length() > 0

                    && subDescriptionT.trim().length() > 0
                    && equipmentTypeT.trim().length() > 0
                    && equipmentSizeT.trim().length() > 0
                    && equipmentClassT.trim().length() > 0
                    && equipmentIdT.trim().length() > 0
                    && mPositionT.trim().length() > 0
                    && strMRes.trim().length() > 0
                    && readingT.trim().length() > 0
                    && strBackG.trim().length() > 0) {

                // database handler
                VHighData db = new VHighData(getApplicationContext());

                // inserting new label into database
                db.insertHighLeaker(idsss, busiT, valve, valveDrawing, leakerT,
                        camSerialT, gasSurveyOpT, camOpT, gasSurveySerialT,
                        productT, equipmentDescT, subDescriptionT,
                        equipmentTypeT, equipmentSizeT, equipmentIdT,
                        mPositionT, correctedReading, correctedR, strMResult,
                        strRecomedation, dateCreateT, cycTt);

                Toast.makeText(getApplicationContext(), "Saved",
                        Toast.LENGTH_SHORT).show();
                db.close();
                Log.d("highLeaker", "highLeaker Valves saved");
            }
        } else if (busiT.equals("Depot")) {
            if (productT.trim().length() > 0
                    && equipmentDescT.trim().length() > 0

                    && subDescriptionT.trim().length() > 0
                    && equipmentTypeT.trim().length() > 0
                    && equipmentSizeT.trim().length() > 0
                    && equipmentClassT.trim().length() > 0
                    && equipmentIdT.trim().length() > 0
                    && mPositionT.trim().length() > 0
                    && strMRes.trim().length() > 0
                    && readingT.trim().length() > 0
                    && strBackG.trim().length() > 0) {

                // database handler
                DHighData db = new DHighData(getApplicationContext());

                // inserting new label into database
                //db.insertHighLeaker(idsss, busiT, depot, depotDrawing, leakerT,
                //		camSerialT, gasSurveyOpT, camOpT, gasSurveySerialT,
                //		productT, equipmentDescT, subDescriptionT,
                //		equipmentTypeT, equipmentSizeT, equipmentIdT,
                //		mPositionT, correctedReading, correctedR, strMResult,
                //		strRecomedation, dateCreateT, cycTt);

                Toast.makeText(getApplicationContext(), "Saved",
                        Toast.LENGTH_SHORT).show();
                db.close();
                Log.d("highLeaker", "highLeaker Depot saved");
            }
        }

    }

    protected void calcID() {
        if (busiT.equals("Filling Station")) {
            String tmpId = "";
            SQLiteDatabase dbrc = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/fillingReports.db", null, 0);

            Cursor crc = dbrc.rawQuery("SELECT * " + "FROM fillingReports",
                    null);

            Integer rowCount = crc.getCount();
            idsss = rowCount++;
            Log.d("rowCount", "" + rowCount + "");
            crc.close();
            dbrc.close();
        } else if (busiT.equals("Valve Station")) {
            String tmpId = "";
            SQLiteDatabase dbrc = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/valveReports.db", null, 0);

            Cursor crc = dbrc.rawQuery("SELECT * " + "FROM valveReports", null);

            Integer rowCount = crc.getCount();
            idsss = rowCount++;
            Log.d("rowCount", "" + rowCount + "");
            crc.close();
            dbrc.close();
        } else if (busiT.equals("Depot")) {
            String tmpId = "";
            SQLiteDatabase dbrc = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/depotReports.db", null, 0);

            Cursor crc = dbrc.rawQuery("SELECT * " + "FROM depotReports", null);

            Integer rowCount = crc.getCount();
            idsss = rowCount++;
            Log.d("rowCount", "" + rowCount + "");
            crc.close();
            dbrc.close();
        }
    }

    protected void calcCycle() {
        if (cycTt.equals("Cycle 1")) {
            cycTt = "cycle1Result";
        } else if (cycTt.equals("Cycle 2")) {
            cycTt = "cycle2Result";
        } else if (cycTt.equals("Cycle 3")) {
            cycTt = "cycle3Result";
        } else if (cycTt.equals("Cycle 4")) {
            cycTt = "cycle4Result";
        } else if (cycTt.equals("Cycle 5")) {
            cycTt = "cycle5Result";
        } else if (cycTt.equals("Cycle 6")) {
            cycTt = "cycle6Result";
        } else if (cycTt.equals("Cycle 7")) {
            cycTt = "cycle7Result";
        } else if (cycTt.equals("Cycle 8")) {
            cycTt = "cycle8Result";
        } else if (cycTt.equals("Cycle 9")) {
            cycTt = "cycle9Result";
        }

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

    private void saveLe() {
        try {
            File myFile = new File("/sdcard/e3softData/Data/localFilling.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(leakerT);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    protected void addReport() {
        if (busiT.equals("Filling Station")) {
            // database handler
            StationReports db = new StationReports(getApplicationContext());

            db.insertReports(busiT, loc, statID, cont, drawingT, leakerT,
                    camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT,
                    productT, equipmentDescT, subDescriptionT, equipmentTypeT,
                    equipmentSizeT, equipmentClassT, equipmentIdT, mPositionT,
                    correctedReading, correctedR, strBackG, strMResult,
                    strRecomedation, dateCreateT, dateModT, cycTt);
            Log.d("database", "image saved");
            Log.d("reading", correctedReading);
            Log.d("loss", strMResult);
        } else if (busiT.equals("Depot")) {
            // database handler
            DepotData db = new DepotData(getApplicationContext());

            db.insertReports(busiT, depot, depotDrawing, leakerT, camOpT,
                    camSerialT, gasSurveyOpT, gasSurveySerialT, productT,
                    equipmentDescT, subDescriptionT, equipmentTypeT,
                    equipmentSizeT, equipmentClassT, equipmentIdT, mPositionT,
                    correctedReading, correctedR, strBackG, strMResult,
                    strRecomedation, dateCreateT, dateModT, cycTt);
            Log.d("database", "image saved");
            Log.d("reading", correctedReading);
            Log.d("loss", strMResult);
        } else if (busiT.equals("Valve Station")) {
            // database handler
            ValveData db = new ValveData(getApplicationContext());

            db.insertReports(busiT, valve, valveDrawing, leakerT, camOpT,
                    camSerialT, gasSurveyOpT, gasSurveySerialT, productT,
                    equipmentDescT, subDescriptionT, equipmentTypeT,
                    equipmentSizeT, equipmentClassT, equipmentIdT, mPositionT,
                    correctedReading, correctedR, strBackG, strMResult,
                    strRecomedation, dateCreateT, dateModT, cycTt);
            Log.d("database", "image saved");
            Log.d("reading", correctedReading);
            Log.d("loss", strMResult);
        }
    }

    protected void loadCf() {
        productT = product.getSelectedItem().toString();
        Log.d("product", productT);
        data2 = "";
        String prod2 = "";

        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/flocalMarketing.sqlite", null, 0);

        Cursor c2 = db.rawQuery(
                "SELECT pr.cf FROM streams pr where pr.product = " + "'"
                        + productT + "'", null);
        int pro2 = c2.getColumnIndex("cf");
        c2.moveToLast();

        // looping through all rows and adding to list
        if (c2 != null) {
            do {
                prod2 = c2.getString(pro2);
                data2 = data2 + prod2;
                cf = Double.parseDouble(data2);
                strCf = cf.toString();
                Log.d("cf", "++++" + strCf);
            } while (c2.moveToNext());
        }

        // closing connection
        c2.close();
        db.close();
    }

    private void receiveData() {

        BroadcastReceiver receiver2 = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent2) {

                camOpT = intent2.getStringExtra("camOplo2");
                camSerialT = intent2.getStringExtra("camSeriallo2");
                gasSurveyOpT = intent2.getStringExtra("gasSurveyOplo2");
                gasSurveySerialT = intent2.getStringExtra("gasSurveySeriallo2");

                seq = intent2.getStringExtra("seq2");
                loc = intent2.getStringExtra("loc2");
                cont = intent2.getStringExtra("cont2");
                statID = intent2.getStringExtra("id2");
                busiT = intent2.getStringExtra("busi2");
                leakerT = intent2.getStringExtra("lilo2");
                drawingT = intent2.getStringExtra("draw2");

                depot = intent2.getStringExtra("depot");
                depotDrawing = intent2.getStringExtra("depotDrawing");

                valve = intent2.getStringExtra("valve");
                valveDrawing = intent2.getStringExtra("valveDrawing");

                cycTt = intent2.getStringExtra("loCycle");
            }
        };

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("com.example.e3soft.receiver");
        registerReceiver(receiver2, filter2);

    }

    private void addProduct() {

        productT = proTt.getText().toString();

        if (productT.trim().length() > 0) {
            String cfT = "0";
            // database handler
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            // inserting new label into database
            // db.insertProduct(productT, cfT, dateCreateT, dateModT);

            // making input filed text to blank
            proTt.setText("");

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();

            // Hiding the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(proTt.getWindowToken(), 0);

            // loading spinner with newly added data
            loadPro();
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Value",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void addPlantSurvey() {
        Log.d("recom", strRecomedation);

        productT = product.getSelectedItem().toString();
        equipmentDescT = equipDesc.getSelectedItem().toString();
        subDescriptionT = subDesc.getSelectedItem().toString();
        equipmentTypeT = equipType.getSelectedItem().toString();
        equipmentSizeT = equipSize.getSelectedItem().toString();
        equipmentClassT = equipClass.getSelectedItem().toString();
        equipmentIdT = equipID.getText().toString();
        mPositionT = mPosition.getSelectedItem().toString();
        strMRes = mResult.getText().toString();
        readingT = reading;
        strBackG = backGr.getText().toString();

        if (productT.trim().length() > 0 && equipmentDescT.trim().length() > 0
                && subDescriptionT.trim().length() > 0
                && equipmentTypeT.trim().length() > 0
                && equipmentSizeT.trim().length() > 0
                && equipmentClassT.trim().length() > 0
                && equipmentIdT.trim().length() > 0
                && mPositionT.trim().length() > 0
                && strMRes.trim().length() > 0 && readingT.trim().length() > 0
                && strBackG.trim().length() > 0) {

            // database handler
            HighData db = new HighData(getApplicationContext());

            // inserting new label into database
            db.insertHighLeaker(idsss, siteT, areaT, unitNumberT, unitT,
                    unitSizeT, drawingT, streamT, leakerT, specificLocT,
                    systemT, equipmentLocT, camOpT, camSerialT, gasSurveyOpT,
                    gasSurveySerialT, productT, equipmentDescT,
                    subDescriptionT, equipmentTypeT, equipmentSizeT,
                    equipmentIdT, mPositionT, correctedReading, correctedR,
                    strMResult, strRecomedation, dateCreateT, cycTt);

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "NO EMPTY FIELDS",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateRecomedation() {

        if (mRes >= 20000) {
            strRecomedation = "DANGER";
        } else if (mRes >= 10000) {
            strRecomedation = "URGENT";
        } else if (mRes >= 5000) {
            strRecomedation = "Maintenance Required";
        } else if (mRes > 1000) {
            strRecomedation = "Consider Maintenance";
        } else if (mRes <= 1000) {
            strRecomedation = "Acceptable";
        }
    }

    private void checkImages() {

        if (updateB.isEnabled()) {
            updateB.setBackgroundResource(R.drawable.submit);
        } else {
            updateB.setBackgroundResource(R.drawable.submit_inactive);
        }
    }

    private void initialiseComponents() {

        currentActivityName = getBaseContext().getApplicationInfo().className;
        activityName = "Inspection.java";

        updateB = (Button) findViewById(R.id.submitBtn);
        backB = (Button) findViewById(R.id.backBtn);
        refreshB = (Button) findViewById(R.id.refreshBtn);
        clearB = (Button) findViewById(R.id.clearBtn);
        addP = (Button) findViewById(R.id.faddPBtn2);

        addEquipDesc = (Button) findViewById(R.id.addEDesc);
        addEquipType = (Button) findViewById(R.id.addEType);
        addEquipSize = (Button) findViewById(R.id.addESize);
        addEquipClass = (Button) findViewById(R.id.addEClass);
        addProduct = (Button) findViewById(R.id.addProduct);
        addMPosition = (Button) findViewById(R.id.addPosition);

        restartB = (Button) findViewById(R.id.restartB2);

        equipDesc = (Spinner) findViewById(R.id.eDescCombo);
        equipType = (Spinner) findViewById(R.id.eTypeCombo);
        equipSize = (Spinner) findViewById(R.id.eSizeCombo);
        equipClass = (Spinner) findViewById(R.id.eClassCombo);
        product = (Spinner) findViewById(R.id.productCombo);
        mPosition = (Spinner) findViewById(R.id.mPositionCombo);

        ppm = (RadioButton) findViewById(R.id.radioPPM);
        lel = (RadioButton) findViewById(R.id.radioLEL);
        gas = (RadioButton) findViewById(R.id.radioGAS);

        equipID = (EditText) findViewById(R.id.eIDTxt);
        mResult = (EditText) findViewById(R.id.lomeasurementTxt);
        subDesc = (Spinner) findViewById(R.id.mainType);
        backGr = (EditText) findViewById(R.id.backgroundTxt);

        addEquipDescTxt = (EditText) findViewById(R.id.addEDescTxt);
        addEquipTypeTxt = (EditText) findViewById(R.id.addETypeTxt);
        addEquipSizeTxt = (EditText) findViewById(R.id.addESizeTxt);
        addEquipClassTxt = (EditText) findViewById(R.id.addEclassTxt);
        addMPositionTxt = (EditText) findViewById(R.id.addIMPositionTxt);
        proTt = (EditText) findViewById(R.id.fproductTt2);
        streamTt = (EditText) findViewById(R.id.fstreamTt2);
        cfTt = (EditText) findViewById(R.id.fcfTt2);

        pros = (TextView) findViewById(R.id.fproductTt3);
        streamss = (TextView) findViewById(R.id.fstreamTt3);
        cfs = (TextView) findViewById(R.id.fcfTt3);

        addEquipDescTxt.setSingleLine();
        addEquipTypeTxt.setSingleLine();
        addEquipSizeTxt.setSingleLine();
        addEquipClassTxt.setSingleLine();
        addMPositionTxt.setSingleLine();
        equipID.setSingleLine();
        mResult.setSingleLine();
        backGr.setSingleLine();

        proTt.setSingleLine();
        streamTt.setSingleLine();
        cfTt.setSingleLine();

        date = (TextView) findViewById(R.id.dateTxt);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        date.setText(currentDate);

        addMPositionTxt.setVisibility(View.GONE);
        addEquipSizeTxt.setVisibility(View.GONE);
        addEquipDescTxt.setVisibility(View.GONE);
        addEquipClassTxt.setVisibility(View.GONE);
        addEquipTypeTxt.setVisibility(View.GONE);

        proTt.setVisibility(View.GONE);
        streamTt.setVisibility(View.GONE);
        cfTt.setVisibility(View.GONE);
        addP.setVisibility(View.GONE);
        pros.setVisibility(View.GONE);
        streamss.setVisibility(View.GONE);
        cfs.setVisibility(View.GONE);

        dateCreateT = currentDate;
        dateModT = currentDate;
        streamT = "Hello";
        ppm.setVisibility(View.VISIBLE);
        lel.setVisibility(View.VISIBLE);
        gas.setVisibility(View.VISIBLE);
        updateB.setBackgroundResource(R.drawable.submit);

    }

    protected void loadSpinnerData() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements

        List<String> pEquipmentDesc = db.getAllEquipmentDescription();
        List<String> pEquipmentType = db.getAllEquipmentType();
        List<String> pEquipmentSize = db.getAllEquipmentSize();
        List<String> pEquipmentClass = db.getAllEquipmentClass();
        List<String> pMPosition = db.getAllMeasurementPositions();
        List<String> mainTypes = new ArrayList<String>();
        mainTypes.add("MECHANICAL");
        mainTypes.add("INSTRUMENT");
        mainTypes.add("ROTATING");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterEquipmentDesc = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentDesc);

        ArrayAdapter<String> dataAdapterEquipmentType = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentType);

        ArrayAdapter<String> dataAdapterEquipmentSize = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentSize);

        ArrayAdapter<String> dataAdapterEquipmentClass = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentClass);

        ArrayAdapter<String> dataAdapterMPosition = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pMPosition);

        ArrayAdapter<String> dataAdaptermTypes = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, mainTypes);

        // Drop down layout style - list view with radio button
        dataAdapterEquipmentDesc
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterEquipmentType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterEquipmentSize
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterEquipmentClass
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterMPosition
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdaptermTypes
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        equipClass.setAdapter(dataAdapterEquipmentClass);
        equipDesc.setAdapter(dataAdapterEquipmentDesc);
        equipSize.setAdapter(dataAdapterEquipmentSize);
        equipType.setAdapter(dataAdapterEquipmentType);
        mPosition.setAdapter(dataAdapterMPosition);
        subDesc.setAdapter(dataAdaptermTypes);
    }

    private void loadPro() {

        StationData db = new StationData(getApplicationContext());
        List<String> pProducts = db.getAllProducts();
        ArrayAdapter<String> dataAdapterProduct = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pProducts);
        dataAdapterProduct
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product.setAdapter(dataAdapterProduct);
    }
}
