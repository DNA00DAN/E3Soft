package com.example.e3soft;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditData extends Activity {

    private String leakerT, item, newDataT;

    private String streamTt, productTt, cfTt, readingTt, mPositionT, correctedReading, correctedR, strRecomedation, lossT;

    private Spinner itemS, stream, reading,leaker;

    private EditText neweData;

    private Button saveB, clearB, exitB;

    private TableRow streamRow;

    private Double mRes = 0.0, correctedReading1 = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        initialiseComponents();
        populateSpinner();
        setSpinnerValue();

        saveB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (itemS.getSelectedItem().toString().equals("stream")) {
                    loadProductName();
                    loadMPosition();
                    populateVariables2();
                    calcLoss();
                    updateRecord2();
                    clearFields();
                } else {
                    populateVariables();
                    updateRecord();
                    clearFields();
                }

            }
        });

        itemS.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (itemS.getSelectedItem().toString().equals("stream")) {
                    reading.setVisibility(View.VISIBLE);
                    streamRow.setVisibility(View.VISIBLE);
                    loadStreams();
                } else {
                    reading.setVisibility(View.GONE);
                    streamRow.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        reading.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (reading.getSelectedItem().toString().equals("PPM")) {
                    readingTt = "PPM";
                } else if (reading.getSelectedItem().toString().equals("LEL")) {
                    readingTt = "LEL";
                } else if (reading.getSelectedItem().toString().equals("GAS")) {
                    readingTt = "GAS";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        clearB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                clearFields();
            }
        });

        exitB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setSpinnerValue() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("leakerId");
            leaker.setSelection(getIndex(leaker, value));

    }
    }

    private int getIndex(Spinner spinner, String value) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++) {
           if (spinner.getItemAtPosition(i).toString().equals(value))  {
               index = i;
               break;
           }
        }
        return index;
    }

    protected void calcLoss() {
        mRes = Double.parseDouble(neweData.getText().toString());
        Double cf = Double.parseDouble(cfTt);
        Double sv = 0.0;
        Double lRa = 0.0;

        if (readingTt == "LEL") {
            mRes = ((mRes * 1000) / 2);
        }
        if (readingTt == "GAS") {
            mRes = (mRes * 10000);
        }
        correctedReading1 = (cf * mRes);
        calculateRecomedation();
        correctedReading = correctedReading1.toString();
        correctedR = "PPM";
        sv = ((cf * mRes) - 0.0);

        if (sv > 100000) {
            if (mPositionT.equals("Valve Stem")
                    || mPositionT.equals("Valve Gland")
                    || mPositionT.equals("Valve Bonnet")) {
                lRa = (0.14 * 8760);
            } else if (mPositionT.equals("Union")) {
                lRa = (0.084 * 8760);
            } else if (mPositionT.equals("Flange")) {
                lRa = (0.03 * 8760);
            } else if (mPositionT.equals("Open End")) {
                lRa = (0.079 * 8760);
            } else if (mPositionT.equals("Pump Seal")) {
                lRa = (0.16 * 8760);
            } else if (mPositionT.equals("Compressor Seal")
                    || mPositionT.equals("Cap")
                    || mPositionT.equals("Relief Valve")
                    || mPositionT.equals("Cylinder Cover")
                    || mPositionT.equals("Plug")) {
                lRa = (0.11 * 8760);
            } else {
                lRa = 10000000.1990;
            }
        } else {
            if (mPositionT.equals("Valve Stem")
                    || mPositionT.equals("Valve Gland")
                    || mPositionT.equals("Valve Bonnet")) {
                Double tmplRa = Math.pow(sv, 0.746);
                lRa = 0.00000461 * 8760 * tmplRa;
            } else if (mPositionT.equals("Flange")) {
                Double tmplRa = Math.pow(sv, 0.703);
                lRa = 0.00000461 * 8760 * tmplRa;
            } else if (mPositionT.equals("Union") || mPositionT.equals("Cap")
                    || mPositionT.equals("Plug")) {
                Double tmplRa = Math.pow(sv, 0.735);
                lRa = 0.00000153 * 8760 * tmplRa;
            } else if (mPositionT.equals("Open End")) {
                Double tmplRa = Math.pow(sv, 0.704);
                lRa = 0.0000022 * 8760 * tmplRa;
            } else if (mPositionT.equals("Pump Seal")) {
                Double tmplRa = Math.pow(sv, 0.61);
                lRa = 0.0000503 * 8760 * tmplRa;
            } else {
                Double tmplRa = Math.pow(sv, 0.589);
                lRa = 0.0000229 * 8760 * tmplRa;
            }
        }
        if (lRa == 10000000.1990) {
            lossT = "Not Defined";
        } else {
            lossT = Double.toString(lRa);
        }
        lossT = Double.toString(lRa);

        Log.d("Calculate Loss: ", correctedReading);
        Log.d("Calculate Loss: ", strRecomedation);
        Log.d("Calculate Loss: ", lossT);
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

    protected void loadMPosition() {
        String data4 = "";
        String prod4 = "";

        SQLiteDatabase db4 = null;

        db4 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/e3softData.db", null, 0);

        Cursor c4 = db4.rawQuery(
                "SELECT * FROM TmpData pr where pr.leakerID = '" + leaker.getSelectedItem().toString() + "'",
                null);
        int pro4 = c4.getColumnIndex("measurementPosition");
        c4.moveToFirst();

        // looping through all rows and adding to list
        if (c4.getCount() > 0) {
            do {
                prod4 = c4.getString(pro4);
                mPositionT = data4 + prod4;


                Log.d("Leak Source: ", mPositionT + "- Loaded");
            } while (c4.moveToNext());
        } else {
            Log.d("Leak Source: ", data4 + "EMPTY");
        }

        // closing connection
        c4.close();
        db4.close();
    }

    protected void loadStreams() {
        ProData db = new ProData(getApplicationContext());
        try {
            List<String> pStreams = db.getAllStreams2();
            ArrayAdapter<String> dataAdapterStream = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item,
                    pStreams);
            dataAdapterStream
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stream.setAdapter(dataAdapterStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadProductName() {
        streamTt = stream.getSelectedItem().toString();
        String data4 = "";
        String prod4 = "";
        String data5 = "";
        String prod5 = "";

        String stre = "'" + streamTt + "'";
        SQLiteDatabase db4 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/newPros.db", null, 0);

        Cursor c4 = db4.rawQuery(
                "SELECT * FROM nwPros pr where pr.stream = " + stre,
                null);
        int pro4 = c4.getColumnIndex("product");
        int pro5 = c4.getColumnIndex("cf");
        c4.moveToFirst();

        // looping through all rows and adding to list
        if (c4.getCount() > 0) {
            do {
                prod4 = c4.getString(pro4);
                productTt = data4 + prod4;

                prod5 = c4.getString(pro5);
                cfTt = data5 + prod5;

                Log.d("Product: ", productTt + "- Loaded");
                Log.d("Product: ", cfTt + "- Loaded");
            } while (c4.moveToNext());
        } else {
            Log.d("Product: ", data4 + "EMPTY");
        }

        // closing connection
        c4.close();
        db4.close();
    }

    private void populateSpinner() {
        // Spinner Drop down elements
        List<String> pSites = new ArrayList<String>();

        pSites.add("Choose 1");
        pSites.add("drawing");
        pSites.add("stream");
        pSites.add("specificLoc");
        pSites.add("system");
        pSites.add("equipmentLoc");
        pSites.add("equipmentDesc");
        pSites.add("Maintenance type");
        pSites.add("equipmentType");
        pSites.add("equipmentSize");
        pSites.add("equipmentID");
        pSites.add("measurementPosition");

        List<String> units = new ArrayList<String>();

        units.add("Choose 1");
        units.add("PPM");
        units.add("LEL");
        units.add("GAS");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterLevels = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pSites);

        // Drop down layout style - list view with radio button
        dataAdapterLevels
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterUnits = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, units);

        // Drop down layout style - list view with radio button
        dataAdapterUnits
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        itemS.setAdapter(dataAdapterLevels);
        reading.setAdapter(dataAdapterUnits);

        DataClass db = new DataClass(getApplicationContext());
        List<String> leaks = db.getAllLeaks2();

// Creating adapter for spinner
        ArrayAdapter<String> dataAdapterLeaks = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, leaks);

        // Drop down layout style - list view with radio button
        dataAdapterLeaks
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        leaker.setAdapter(dataAdapterLeaks);

    }

    protected void clearFields() {
        neweData.setText("");
    }

    protected void updateRecord() {
        DataClass dbur = new DataClass(getApplicationContext());
        try {
            dbur.updateReport2(leakerT, item, newDataT);
            dbur.close();
            Log.d("update", "success");
            Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "UPDATE ERROR, CHECK DATA",
                    Toast.LENGTH_LONG).show();
            dbur.close();
        }
    }

    protected void updateRecord2() {
        DataClass dbur = new DataClass(getApplicationContext());
        try {
            dbur.updateReport3(leakerT, itemS.getSelectedItem().toString(), streamTt, correctedReading, lossT, strRecomedation, productTt);
            dbur.close();
            Log.d("update", "success");
            Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "UPDATE ERROR, CHECK DATA",
                    Toast.LENGTH_LONG).show();
            dbur.close();
        }
    }

    protected void populateVariables() {
        leakerT = leaker.getSelectedItem().toString();
        item = itemS.getSelectedItem().toString();
        newDataT = neweData.getText().toString();
    }

    protected void populateVariables2() {
        leakerT = leaker.getSelectedItem().toString();
        item = itemS.getSelectedItem().toString();
        newDataT = neweData.getText().toString();
        readingTt = reading.getSelectedItem().toString();
    }

    private void initialiseComponents() {
        saveB = (Button) findViewById(R.id.edSaveB);
        clearB = (Button) findViewById(R.id.edclearB);
        exitB = (Button) findViewById(R.id.edexitB);

        itemS = (Spinner) findViewById(R.id.edDataType);
        stream = (Spinner) findViewById(R.id.streamLocationSp);
        reading = (Spinner) findViewById(R.id.readingUnitSp);

        streamRow = (TableRow) findViewById(R.id.tableRow7);

        leaker = (Spinner) findViewById(R.id.editLeakSp);
        neweData = (EditText) findViewById(R.id.newDataEt);
        neweData.setSingleLine();
    }

}
