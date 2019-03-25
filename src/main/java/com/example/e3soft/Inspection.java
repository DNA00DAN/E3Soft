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

public class Inspection extends Activity {

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
    private Button updateB, backB, refreshB, clearB, loadButton;
    private Button addEquipDesc, addEquipType, addEquipSize, addEquipClass,
            addProduct, addMPosition, addP;
    private Spinner equipDesc, equipType, equipSize, equipClass, product,
            mPosition, subDesc;
    private EditText addEquipDescTxt, addEquipTypeTxt, addEquipSizeTxt,
            addEquipClassTxt, addProductTxt, addMPositionTxt, equipID, mResult,
            backGr, proTt, streamTt, cfTt, oldLeak;
    private TextView date, productsTxt;
    private RadioButton ppm, lel, gas;
    private String currentActivityName, activityName, currentDate, reading,
            correctedReading, correctedR, oldLeakT;
    private String strMResult, strBackG, strCf, strMRes, DATE_TAKEN,
            strRecomedation;
    private String productT, equipmentDescT, subDescriptionT, equipmentTypeT,
            readingT = "PPM", equipmentSizeT, equipmentClassT, equipmentIdT,
            mPositionT, dateCreateT, dateModT;
    private String equipDescPressed, equipTypePressed, equipSizePressed,
            equipClassPressed, productPressed, mPositionPressed, cycTt;
    private Double cf = 0.0, mRes = 0.0, lRa = 0.0, trendF = 0.0, backG = 0.0,
            sv = 0.0, correctedReading1;
    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT;
    private Bitmap bm;
    private Drawable imas;
    private TextView strT, proT, cff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        initialiseComponents();
        receiveData();
        loadSpinnerData();
        loadSpinnerMTypes();
        loadSpinnerClass();
        loadSpinnerSize();
        loadSpinnerMPos();
        loadSpinnerType();

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                strMResult = mResult.getText().toString();
                strMRes = strMResult;

                equipmentDescT = equipDesc.getSelectedItem().toString();
                equipmentTypeT = equipType.getSelectedItem().toString();
                equipmentSizeT = equipSize.getSelectedItem().toString();
                equipmentClassT = equipClass.getSelectedItem().toString();
                equipmentIdT = equipID.getText().toString();
                mPositionT = mPosition.getSelectedItem().toString();

                if (equipmentDescT.equals("Choose 1")
                        || equipmentTypeT.equals("Choose 1")
                        || equipmentSizeT.equals("Choose 1")
                        || equipmentIdT.equals("Choose 1")
                        || mPositionT.equals("Choose 1")) {
                    mRes = 0.0;
                    Toast.makeText(getBaseContext(),
                            "Please Complete all DropDowns", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Log.d("CF VALVUE", cf.toString());
                    calcID();
                    calcCycle();
                    calculateBackG();
                    calculateLoss();
                    addPlantSurvey();
                    addReport();
                    addOldLeak();
                    saveLe();
                    ppm.setVisibility(View.VISIBLE);
                    lel.setVisibility(View.VISIBLE);
                    gas.setVisibility(View.VISIBLE);
                    mResult.setText("");
                    backGr.setText("");
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
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(0);
                }
            }

            private void showReport() {

                Intent i = new Intent(Inspection.this, Reports.class);

                i.putExtra("site", siteT);
                i.putExtra("area", areaT);
                i.putExtra("unitNumber", unitNumberT);
                i.putExtra("unitName", unitT);
                i.putExtra("unitSize", unitSizeT);

                i.putExtra("drawing", drawingT);
                i.putExtra("specificLoc", specificLocT);
                i.putExtra("streamLocation", streamT);
                i.putExtra("system", systemT);
                i.putExtra("equipmentLoc", equipmentLocT);

                i.putExtra("camOp", camOpT);
                i.putExtra("camSerial", camSerialT);
                i.putExtra("gasSurveyOp", gasSurveyOpT);
                i.putExtra("gasSurveySerial", gasSurveySerialT);

                i.putExtra("product", productT);
                i.putExtra("equipmentDesc", equipmentDescT);
                i.putExtra("equipmentType", equipmentTypeT);
                i.putExtra("equipmentSize", equipmentSizeT);
                i.putExtra("equipmentClass", equipmentClassT);
                i.putExtra("equipmentId", equipmentIdT);

                i.putExtra("mPosition", mPositionT);
                i.putExtra("mResult", strMRes);
                i.putExtra("background", strBackG);
                i.putExtra("reading", readingT);
                i.putExtra("aLoss", strMResult);
                i.putExtra("recom", strRecomedation);
                i.putExtra("date", dateCreateT);
                i.putExtra("image", bm);

                startActivity(i);
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

                mRes = Double.parseDouble(strMResult);

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

                strMResult = Double.toString(lRa);
            }
        });

        backB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Inspection.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        loadButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    updateB.setEnabled(true);
                    updateB.setBackgroundResource(R.drawable.submit);
                    productsTxt.setText("");
                    // loadPro();
                    loadProduct();
                    loadCf();
                    if (!ppm.isShown() && !lel.isShown() && !gas.isShown()) {
                        ppm.setVisibility(View.VISIBLE);
                        lel.setVisibility(View.VISIBLE);
                        gas.setVisibility(View.VISIBLE);
                    }
                    addP.setVisibility(View.GONE);

                } catch (Exception e) {
                    // proTt.setVisibility(View.VISIBLE);
                    // cfTt.setVisibility(View.VISIBLE);
                    // streamTt.setVisibility(View.VISIBLE);
                    // addP.setVisibility(View.VISIBLE);

                    // streamTt.setText(streamT);
                }
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
                addProductTxt.setEnabled(true);
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
                if (addProductTxt.isShown()) {
                    addProductTxt.setVisibility(View.GONE);
                    addProduct.setBackgroundResource(R.drawable.edit);
                    addProduct.setText("ADD");
                    addProduct();
                } else {
                    addProductTxt.setVisibility(View.VISIBLE);
                    addProduct.setBackgroundResource(R.drawable.save);
                    addProduct.setText("SAVE");

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

    }

    protected void addOldLeak() {
        oldLeakT = oldLeak.getText().toString();
        if (oldLeakT.equals("")) {
            oldLeakT = "NA";
        }
        // database handler
        oldData db = new oldData(getApplicationContext());

        db.insertLeaker(leakerT, oldLeakT);
        Log.d("database", "image saved");
        db.close();
    }

    private void loadSpinnerType() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> pEquipmentType = db.getAllEquipmentType();
        ArrayAdapter<String> dataAdapterEquipmentType = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentType);
        dataAdapterEquipmentType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipType.setAdapter(dataAdapterEquipmentType);
    }

    private void loadSpinnerMPos() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> pMPosition = db.getAllMeasurementPositions();
        ArrayAdapter<String> dataAdapterMPosition = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pMPosition);
        dataAdapterMPosition
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPosition.setAdapter(dataAdapterMPosition);
    }

    private void loadSpinnerSize() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> pEquipmentSize = db.getAllEquipmentSize();
        ArrayAdapter<String> dataAdapterEquipmentSize = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentSize);
        dataAdapterEquipmentSize
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipSize.setAdapter(dataAdapterEquipmentSize);
    }

    private void loadSpinnerClass() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> pEquipmentClass = db.getAllEquipmentClass();
        ArrayAdapter<String> dataAdapterEquipmentClass = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentClass);
        dataAdapterEquipmentClass
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipClass.setAdapter(dataAdapterEquipmentClass);
    }

    private void loadSpinnerMTypes() {
        List<String> mainTypes = new ArrayList<String>();
        mainTypes.add("MECHANICAL");
        mainTypes.add("INSTRUMENT");
        mainTypes.add("ROTATING");
        ArrayAdapter<String> dataAdaptermTypes = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, mainTypes);
        dataAdaptermTypes
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subDesc.setAdapter(dataAdaptermTypes);
    }

    protected void calcID() {
        String tmpId = "";
        SQLiteDatabase dbrc = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/highLeaker.db", null, 0);

        Cursor crc = dbrc.rawQuery("SELECT * " + "FROM highLeaker", null);

        Integer rowCount = crc.getCount();
        idsss = rowCount++;
        Log.d("rowCount", "" + rowCount + "");
        crc.close();
        dbrc.close();
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
            File myFile = new File("/sdcard/e3softData/Data/leaker.txt");
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
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        db.insertReports(siteT, areaT, unitNumberT, unitT, unitSizeT, drawingT,
                streamT, leakerT, specificLocT, systemT, equipmentLocT, camOpT,
                camSerialT, gasSurveyOpT, gasSurveySerialT, productT,
                equipmentDescT, subDescriptionT, equipmentTypeT,
                equipmentSizeT, equipmentClassT, equipmentIdT, mPositionT,
                correctedReading, correctedR, strBackG, strMResult,
                strRecomedation, byteImage, dateCreateT, dateModT, cycTt);
        Log.d("database", "image saved");
        db.close();
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

    private void loadProduct() {
        data = "";
        String prod = "";

        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/NewPros.db", null, 0);

        Cursor c = db.rawQuery(
                "SELECT pr.product FROM nwPros pr where pr.stream = " + "'"
                        + streamT + "'", null);
        int pro = c.getColumnIndex("product");
        c.moveToLast();

        // looping through all rows and adding to list
        if (c != null) {
            do {
                prod = c.getString(pro);
                data = data + prod;
                productsTxt = (TextView) findViewById(R.id.productView);
                productsTxt.setVisibility(View.VISIBLE);
                productsTxt.setText(data);
            } while (c.moveToNext());
        }

        // closing connection
        c.close();
        db.close();
    }

    private void receiveData() {

        BroadcastReceiver receiver2 = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent2) {

                camOpT = intent2.getStringExtra("camOp");
                camSerialT = intent2.getStringExtra("camSerial");
                gasSurveyOpT = intent2.getStringExtra("gasSurveyOp");
                gasSurveySerialT = intent2.getStringExtra("gasSurveySerial");

                siteT = intent2.getStringExtra("site");
                areaT = intent2.getStringExtra("area");
                unitNumberT = intent2.getStringExtra("unitNumber");
                unitT = intent2.getStringExtra("unitName");
                unitSizeT = intent2.getStringExtra("unitSize");

                drawingT = intent2.getStringExtra("drawing");
                streamT = intent2.getStringExtra("streamLocation");
                leakerT = intent2.getStringExtra("leakerID");
                specificLocT = intent2.getStringExtra("specificLoc");

                systemT = intent2.getStringExtra("system");
                equipmentLocT = intent2.getStringExtra("equipmentLoc");
                counter = intent2.getStringExtra("count");
                emerT = intent2.getStringExtra("em");
                cycTt = intent2.getStringExtra("cyc2");

            }
        };

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("com.example.e3soft.receiver");
        registerReceiver(receiver2, filter2);

    }

    private void addProduct() {

        productT = addProductTxt.getText().toString();

        if (productT.trim().length() > 0) {
            String cfT = "0";
            // database handler
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            // inserting new label into database
            // db.insertProduct(productT, cfT, dateCreateT, dateModT);

            // making input filed text to blank
            addProductTxt.setText("");

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
                    .show();

            // Hiding the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(addProductTxt.getWindowToken(), 0);

            // loading spinner with newly added data
            loadSpinnerData();
            loadProduct();
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Value",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void addPlantSurvey() {
        Log.d("recom", strRecomedation);

        productT = productsTxt.getText().toString();
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
        loadButton = (Button) findViewById(R.id.loadProduct);
        addP = (Button) findViewById(R.id.addPBtn2);

        addEquipDesc = (Button) findViewById(R.id.addEDesc);
        addEquipType = (Button) findViewById(R.id.addEType);
        addEquipSize = (Button) findViewById(R.id.addESize);
        addEquipClass = (Button) findViewById(R.id.addEClass);
        addProduct = (Button) findViewById(R.id.addProduct);
        addMPosition = (Button) findViewById(R.id.addPosition);

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
        mResult = (EditText) findViewById(R.id.measurementTxt);
        subDesc = (Spinner) findViewById(R.id.mainType);
        backGr = (EditText) findViewById(R.id.backgroundTxt);

        addEquipDescTxt = (EditText) findViewById(R.id.addEDescTxt);
        addEquipTypeTxt = (EditText) findViewById(R.id.addETypeTxt);
        addEquipSizeTxt = (EditText) findViewById(R.id.addESizeTxt);
        addEquipClassTxt = (EditText) findViewById(R.id.addEclassTxt);
        addProductTxt = (EditText) findViewById(R.id.addProductTxt);
        addMPositionTxt = (EditText) findViewById(R.id.addIMPositionTxt);
        proTt = (EditText) findViewById(R.id.productTt2);
        streamTt = (EditText) findViewById(R.id.streamTt2);
        cfTt = (EditText) findViewById(R.id.cfTt2);
        oldLeak = (EditText) findViewById(R.id.oldIDTxt);
        proT = (TextView) findViewById(R.id.productTt13);
        strT = (TextView) findViewById(R.id.streamTt13);
        cff = (TextView) findViewById(R.id.cfTt13);

        proTt.setVisibility(View.GONE);
        cfTt.setVisibility(View.GONE);
        streamTt.setVisibility(View.GONE);

        proT.setVisibility(View.GONE);
        cff.setVisibility(View.GONE);
        strT.setVisibility(View.GONE);

        addEquipDescTxt.setSingleLine();
        addEquipTypeTxt.setSingleLine();
        addEquipSizeTxt.setSingleLine();
        addEquipClassTxt.setSingleLine();
        addProductTxt.setSingleLine();
        addMPositionTxt.setSingleLine();
        equipID.setSingleLine();
        mResult.setSingleLine();
        backGr.setSingleLine();
        oldLeak.setSingleLine();

        date = (TextView) findViewById(R.id.dateTxt);
        productsTxt = (TextView) findViewById(R.id.productView);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        date.setText(currentDate);

        addMPositionTxt.setVisibility(View.GONE);
        addEquipSizeTxt.setVisibility(View.GONE);
        addEquipDescTxt.setVisibility(View.GONE);
        addProductTxt.setVisibility(View.GONE);
        addEquipClassTxt.setVisibility(View.GONE);
        addEquipTypeTxt.setVisibility(View.GONE);
        productsTxt.setVisibility(View.GONE);
        addProduct.setVisibility(View.GONE);
        product.setVisibility(View.GONE);

        proTt.setVisibility(View.GONE);
        streamTt.setVisibility(View.GONE);
        cfTt.setVisibility(View.GONE);
        addP.setVisibility(View.GONE);

        dateCreateT = currentDate;
        dateModT = currentDate;
        streamT = "Hello";
        ppm.setVisibility(View.VISIBLE);
        lel.setVisibility(View.VISIBLE);
        gas.setVisibility(View.VISIBLE);
        updateB.setEnabled(false);
        updateB.setBackgroundResource(R.drawable.submit_inactive);

    }

    protected void loadSpinnerData() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements

        List<String> pEquipmentDesc = db.getAllEquipmentDescription();
        ArrayAdapter<String> dataAdapterEquipmentDesc = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentDesc);
        dataAdapterEquipmentDesc
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipDesc.setAdapter(dataAdapterEquipmentDesc);

    }

    private void loadPro() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> pProducts = db.getAllProducts();
        ArrayAdapter<String> dataAdapterProduct = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pProducts);
        dataAdapterProduct
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product.setAdapter(dataAdapterProduct);
    }
}
