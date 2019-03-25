package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class LocalNewLeaks extends Activity {

    public static String busiTT, seqT, lockT;
    public static String busis, seqs, locks;
    private Button submitB, restartB;
    private String camOpT, camSerialT, gasOpT, gasSerialT, leakerT;
    private String stationID, contact, drawing;
    private String depotT, depotDrawingT;
    private Spinner sequence, loc, depotSp;
    private Spinner busiType;
    private LinearLayout fStation, vStation, depot;
    private TextView fStat, fCont;
    private String dateCreateT, dateModT;
    private EditText depotDrawing, fDraw;
    private EditText valveStation, valveDrawing;
    private String valveStationT, valveDrawingT;
    private String cycT;
    private Spinner equipDesc, subDesc, equipSize, equipType, mPosition,
            streamLocation;
    private RadioButton ppm, lel, gas;
    private TextView equipID, mResult, backGr;
    private String productT, strCf, leakerID, correctedReading, correctedR;
    private Double cf = 0.0, correctedReading1 = 0.0, lRa = 0.0, sv = 0.0;
    private String reading;
    private EditText addLeakerId;
    private Button newLeak;
    private String busiTypeT, stationIDT, stationNameT, contactT, drawingT;

    private String strMResult, strBackG, strMRes, strRecomedation;

    private String equipmentDescT, subDescriptionT, equipmentTypeT,
            readingT = "PPM", equipmentSizeT, equipmentIdT, mPositionT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_new_leaks);
        initialiseComponents();
        loadBusiSpinner();
        loadEquipmentSpinners();
        loadPro();
        loadL();

        submitB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Double tmpR = Double.parseDouble(mResult.getText().toString());
                if (tmpR < 1.0) {
                    Toast.makeText(getApplicationContext(), "NO ZERO VALUES", Toast.LENGTH_LONG).show();
                } else {
                    busiTypeT = busiType.getSelectedItem().toString();

                    leakerID = "LM_" + addLeakerId.getText().toString();
                    strMResult = mResult.getText().toString();
                    equipmentDescT = equipDesc.getSelectedItem().toString();
                    equipmentTypeT = equipType.getSelectedItem().toString();
                    equipmentSizeT = equipSize.getSelectedItem().toString();
                    productT = streamLocation.getSelectedItem().toString();
                    equipmentIdT = equipID.getText().toString();
                    mPositionT = mPosition.getSelectedItem().toString();
                    subDescriptionT = subDesc.getSelectedItem().toString();

                    if (equipmentDescT.equals("Choose 1")
                            || equipmentTypeT.equals("Choose 1")
                            || equipmentSizeT.equals("Choose 1")
                            || equipmentIdT.equals("Choose 1")
                            || mPositionT.equals("Choose 1")) {
                        Toast.makeText(getBaseContext(),
                                "Please Complete all DropDowns", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        calculateLoss();

                        if (busiTypeT.equals("Filling Station")) {
                            stationIDT = loc.getSelectedItem().toString();
                            stationNameT = fStat.getText().toString();
                            contactT = fCont.getText().toString();
                            drawingT = fDraw.getText().toString();
                            // database handler
                            FHighData db = new FHighData(getApplicationContext());

                            // inserting new label into database
                            db.insertFHighLeaker(leakerID, busiTypeT, stationIDT,
                                    stationNameT, contactT, drawingT,
                                    LocalUserDetails.camTech,
                                    LocalUserDetails.camSerial,
                                    LocalUserDetails.surveyTech,
                                    LocalUserDetails.surveySerial, productT,
                                    equipmentDescT, subDescriptionT,
                                    equipmentTypeT, equipmentSizeT, equipmentIdT,
                                    mPositionT, correctedReading, strMResult,
                                    strRecomedation, dateCreateT,
                                    LocalUserDetails.cycleRes);

                            Toast.makeText(getApplicationContext(), "Saved",
                                    Toast.LENGTH_SHORT).show();
                            db.close();
                            Log.d("highLeaker", "highLeaker Filling saved");

                            insertGIData();
                            insertResData();
                            saveLe();

                            Log.d("Save Complete", "AllDataSaved saved");
                        } else if (busiTypeT.equals("Depot")) {
                            depotT = depotSp.getSelectedItem().toString();
                            depotDrawingT = depotDrawing.getText().toString();
                            // database handler
                            DHighData db = new DHighData(getApplicationContext());

                            // inserting new label into database
                            db.insertHighLeaker(
                                    leakerID,
                                    busiTypeT,
                                    depotT,
                                    depotDrawingT,
                                    LocalUserDetails.camTech,
                                    LocalUserDetails.camSerial,
                                    LocalUserDetails.surveyTech,
                                    LocalUserDetails.surveySerial,
                                    productT,
                                    equipmentDescT,
                                    subDescriptionT,
                                    equipmentTypeT,
                                    equipmentSizeT,
                                    equipmentIdT,
                                    mPositionT,
                                    correctedReading,
                                    strMResult,
                                    strRecomedation,
                                    dateCreateT,
                                    LocalUserDetails.cycleRes);

                            Toast.makeText(getApplicationContext(), "Saved",
                                    Toast.LENGTH_SHORT).show();
                            db.close();
                            Log.d("highLeaker", "highLeaker Filling saved");

                            insertGIData();
                            insertResData();
                            saveLe();
                            Log.d("Save Complete", "AllDataSaved saved");
                        }

                    }
                    newLeak.setEnabled(true);
                    mResult.setText("");
                    ppm.setVisibility(View.VISIBLE);
                    lel.setVisibility(View.VISIBLE);
                    gas.setVisibility(View.VISIBLE);
                    showReport();
                }
            }
        });

        busiType.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                busiTT = busiType.getSelectedItem().toString();
                calcBusiType();
                calcLoc();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        loc.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                lockT = loc.getSelectedItem().toString();

                calcStation();
                calcContact();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        streamLocation.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                loadCf();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

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

        restartB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                reading = "";
                lel.setVisibility(View.VISIBLE);
                ppm.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);
            }
        });

        newLeak.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerID = addLeakerId.getText().toString().trim();
                if (leakerID.length() > 0) {
                    Integer tmpLeaks = Integer.parseInt(leakerID);
                    tmpLeaks++;
                    leakerID = tmpLeaks.toString();
                    addLeakerId.setText(leakerID);
                    leakerID = "LM_" + leakerID;
                    newLeak.setEnabled(false);
                    submitB.setEnabled(true);
                    submitB.setBackgroundResource(R.drawable.submit);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Starting Number", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    protected void insertResData() {
        DataClass db = new DataClass(getApplicationContext());
        if (busiTypeT.equals("Filling Station")) {
            db.insertFSResult("LM_" + addLeakerId.getText().toString(),
                    LocalUserDetails.cycleRes, correctedReading);
        } else if (busiTypeT.equals("Depot")) {
            db.insertDepotResult("LM_" + addLeakerId.getText().toString(),
                    LocalUserDetails.cycleRes, correctedReading);
        }
    }

    protected void insertGIData() {
        if (busiType.getSelectedItem().toString().equals("Filling Station")) {
            DataClass db = new DataClass(getApplicationContext());
            db.insertGIFS(leakerID, stationIDT, stationNameT, drawingT,
                    contactT, productT, equipmentDescT, subDescriptionT,
                    equipmentTypeT, equipmentSizeT, equipmentIdT, mPositionT,
                    userDetails.camTech, userDetails.camSerial,
                    userDetails.surveyTech, userDetails.surveySerial,
                    userDetails.cycleRes);
            Toast.makeText(getApplicationContext(), "General Info Added",
                    Toast.LENGTH_SHORT).show();
        } else if (busiType.getSelectedItem().toString().equals("Depot")) {
            DataClass db = new DataClass(getApplicationContext());
            db.insertGIDE(leakerID, depotT, depotDrawingT,
                    productT, equipmentDescT, subDescriptionT,
                    equipmentTypeT, equipmentSizeT, equipmentIdT, mPositionT,
                    userDetails.camTech, userDetails.camSerial,
                    userDetails.surveyTech, userDetails.surveySerial,
                    userDetails.cycleRes);
            Toast.makeText(getApplicationContext(), "General Info Added",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadL() {
        File dir = Environment.getExternalStorageDirectory();
        // File yourFile = new File(dir,
        // "path/to/the/file/inside/the/sdcard.ext");

        // Get the text file
        File file = new File(dir, "e3softData/Data/localFilling.txt");
        // i have kept text.txt in the sd-card

        if (file.exists()) // check if file exist
        {
            // Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line.substring(3));
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

    protected void loadCf() {
        productT = streamLocation.getSelectedItem().toString();
        Log.d("product", productT);
        String data2 = "";
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

    private void loadPro() {

        StationData db = new StationData(getApplicationContext());
        List<String> pProducts = db.getAllProducts();
        ArrayAdapter<String> dataAdapterProduct = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pProducts);
        dataAdapterProduct
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        streamLocation.setAdapter(dataAdapterProduct);
    }

    private void loadEquipmentSpinners() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        List<String> pEquipmentDesc = db.getAllEquipmentDescription();
        ArrayAdapter<String> dataAdapterEquipmentDesc = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentDesc);
        dataAdapterEquipmentDesc
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipDesc.setAdapter(dataAdapterEquipmentDesc);

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

        List<String> pMPosition = db.getAllMeasurementPositions();
        ArrayAdapter<String> dataAdapterMPosition = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, pMPosition);
        dataAdapterMPosition
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPosition.setAdapter(dataAdapterMPosition);

        List<String> pEquipmentType = db.getAllEquipmentType();
        ArrayAdapter<String> dataAdapterEquipmentType = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                pEquipmentType);
        dataAdapterEquipmentType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipType.setAdapter(dataAdapterEquipmentType);
    }

    public String calcContact() {
        String data = "";
        String prod = "";
        lockT = fStat.getText().toString();
        lockT = "'" + lockT + "'";
        // TODO Auto-generated method stub
        SQLiteDatabase db2 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/flocalMarketing.sqlite", null, 0);

        Cursor c = db2.rawQuery(
                "SELECT co.contact FROM contacts co where co.site = " + lockT,
                null);
        int pro = c.getColumnIndex("contact");
        c.moveToFirst();

        // looping through all rows and adding to list
        if (c.getCount() > 0) {
            do {
                prod = c.getString(pro);
                data = data + prod;
                fCont.setText(data);
                contact = data;
            } while (c.moveToNext());
        } else {
            Log.d("unitName", data + "EMPTY");
        }

        // closing connection
        c.close();
        db2.close();
        return data;
    }

    public String calcStation() {
        String data = "";
        String prod = "";
        lockT = loc.getSelectedItem().toString();
        // TODO Auto-generated method stub
        SQLiteDatabase db2 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/flocalMarketing.sqlite", null, 0);

        Cursor c = db2.rawQuery(
                "SELECT si.site FROM siteIDs si where si.siteID= '" + lockT
                        + "'", null);
        int pro = c.getColumnIndex("site");
        c.moveToFirst();

        // looping through all rows and adding to list
        if (c.getCount() > 0) {
            do {
                prod = c.getString(pro);
                data = data + prod;
                fStat.setText(data);
                stationID = data;
            } while (c.moveToNext());
        } else {
            Log.d("unitName", data + "EMPTY");
        }

        // closing connection
        c.close();
        db2.close();
        return data;
    }

    protected void calcLoc() {
        StationData db = new StationData(getApplicationContext());

        // Spinner Drop down elements
        try {
            List<String> pSeqs = db.getAllSites(busiTT);

            ArrayAdapter<String> dataAdapterSeq = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item, pSeqs);

            dataAdapterSeq
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            loc.setAdapter(dataAdapterSeq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void calcBusiType() {
        // TODO Auto-generated method stub
        if (busiTT.equals("Filling Station")) {
            fStation.setVisibility(View.VISIBLE);
            vStation.setVisibility(View.GONE);
            depot.setVisibility(View.GONE);

        } else if (busiTT.equals("Valve Station")) {
            fStation.setVisibility(View.GONE);
            vStation.setVisibility(View.VISIBLE);
            depot.setVisibility(View.GONE);

        } else if (busiTT.equals("Depot")) {
            fStation.setVisibility(View.GONE);
            vStation.setVisibility(View.GONE);
            depot.setVisibility(View.VISIBLE);
            loadDepotSpinner();
        } else {
            fStation.setVisibility(View.GONE);
            vStation.setVisibility(View.GONE);
            depot.setVisibility(View.GONE);
        }
    }

    private void loadDepotSpinner() {
        StationData db = new StationData(getApplicationContext());
        // Spinner Drop down elements
        try {
            List<String> pDepots = db.getAllDepots();

            ArrayAdapter<String> dataAdapterDepots = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pDepots);

            dataAdapterDepots
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            depotSp.setAdapter(dataAdapterDepots);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialiseComponents() {
        submitB = (Button) findViewById(R.id.lsaveB);
        restartB = (Button) findViewById(R.id.restartB1);
        newLeak = (Button) findViewById(R.id.updateLeakerBtnN);
        busiType = (Spinner) findViewById(R.id.businessType);

        sequence = (Spinner) findViewById(R.id.sequenceSp);
        loc = (Spinner) findViewById(R.id.locationSp);
        depotSp = (Spinner) findViewById(R.id.loDepotSp);

        // Equipment Details
        equipDesc = (Spinner) findViewById(R.id.eDescSp);
        equipType = (Spinner) findViewById(R.id.eTypeSp);
        equipSize = (Spinner) findViewById(R.id.eSizeSp);
        mPosition = (Spinner) findViewById(R.id.mPositionSp);
        subDesc = (Spinner) findViewById(R.id.mainTypeSp);
        streamLocation = (Spinner) findViewById(R.id.streamLocationSp);

        ppm = (RadioButton) findViewById(R.id.radioPPMN);
        lel = (RadioButton) findViewById(R.id.radioLELN);
        gas = (RadioButton) findViewById(R.id.radioGASN);

        equipID = (EditText) findViewById(R.id.eIDTxtN);
        mResult = (EditText) findViewById(R.id.measurementTxtN);
        backGr = (EditText) findViewById(R.id.backgroundTxtN);

        fStation = (LinearLayout) findViewById(R.id.fStationLayout);
        vStation = (LinearLayout) findViewById(R.id.vStationLayout);
        depot = (LinearLayout) findViewById(R.id.depotLayout);

        fStat = (TextView) findViewById(R.id.fStationTxt);
        fCont = (TextView) findViewById(R.id.fContactTxt);
        fDraw = (EditText) findViewById(R.id.fDrawingTxt);

        depotDrawing = (EditText) findViewById(R.id.dDNumber);
        valveStation = (EditText) findViewById(R.id.vLocationResult);
        valveDrawing = (EditText) findViewById(R.id.vDNumber);
        addLeakerId = (EditText) findViewById(R.id.addLeakerIdTxtN);

        fStation.setVisibility(View.GONE);
        vStation.setVisibility(View.GONE);
        depot.setVisibility(View.GONE);

        valveStation.setSingleLine();
        valveDrawing.setSingleLine();
        equipID.setSingleLine();
        depotDrawing.setSingleLine();
        fDraw.setSingleLine();


        String currentDate1 = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR);

        String currentDate2 = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE);

        dateCreateT = currentDate1.substring(8) + "-" + currentDate2.substring(3) + "-" + currentDate2.substring(0, 2);
        Log.d("Date 1",dateCreateT.substring(5,6));
        Log.d("Date 2",dateCreateT.substring(8,9));
        if(!dateCreateT.substring(5,6).equals("-") || !dateCreateT.substring(8,9).equals("-")){
            dateCreateT = DateUtils.formatDateTime(getBaseContext(),
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_NUMERIC_DATE
                            | DateUtils.FORMAT_24HOUR);

            if (dateCreateT.substring(7, 11).equals("2014")
                    || dateCreateT.substring(7, 11).equals("2015")
                    || dateCreateT.substring(7, 11).equals("2016")
                    || dateCreateT.substring(7, 11).equals("2017")) {

                String tmpdate = dateCreateT.substring(7, 11) + "-"
                        + dateCreateT.substring(12, 14) + "-"
                        + dateCreateT.substring(15, 17);
                dateCreateT = tmpdate;
            } else {
                //String tmpdate = dateCreateT.substring(13, 17) + "-"
                //        + dateCreateT.substring(10, 12) + "-"
                //        + dateCreateT.substring(7, 9);

                dateCreateT = dateCreateT;
            }
        }



        sequence.setVisibility(View.GONE);

        addLeakerId.setEnabled(false);
        addLeakerId.setSingleLine();
        submitB.setEnabled(false);
        submitB.setBackgroundResource(R.drawable.submit_inactive);
        backGr.setText("" + 0);
        backGr.setEnabled(false);
        mResult.setSingleLine();

    }

    private void loadBusiSpinner() {
        // TODO Auto-generated method stub
        final List<String> busiTypes = new ArrayList<String>();
        busiTypes.add("Choose 1");
        busiTypes.add("Filling Station");
        busiTypes.add("Valve Station");
        busiTypes.add("Depot");

        ArrayAdapter<String> busiT = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, busiTypes);

        busiType.setAdapter(busiT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.local_new_leaks, menu);
        return true;
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
                                Intent i = new Intent(LocalNewLeaks.this,
                                        Home.class);
                                startActivity(i);
                                finish();
                            }
                        }).setNegativeButton("NO", null).show();
    }

    private void calculateLoss() {

        Double mRes = Double.parseDouble(strMResult);
        sv = (Calculations.calculateSV(mRes,cf,reading));
        correctedReading1 = sv;
        calculateRecomedation();
        correctedReading = correctedReading1.toString();
        correctedR = "PPM";


        strMResult = Calculations.calculateLossBySource(sv,mPositionT);

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

    private void showReport() {
        if (busiTypeT.equals("Filling Station")) {
            Intent i = new Intent(LocalNewLeaks.this, Freport.class);
            startActivity(i);
        } else if (busiTypeT.equals("Depot")) {
            Intent i = new Intent(LocalNewLeaks.this, Dreport.class);
            startActivity(i);
        } else if (busiTypeT.equals("Valve Station")) {
            Intent i = new Intent(LocalNewLeaks.this, Vreport.class);
            startActivity(i);
        }
    }

    private void saveLe() {
        try {
            File myFile = new File("/sdcard/e3softData/Data/localFilling.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(leakerID);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
