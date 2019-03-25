package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Current_Site extends Activity {

    public static String siteT, areaT, unitNumberT, unitT, drawingT, unitSizeT,
            equipmentLocT, specificLocT, streamT, systemT, dateCreateT,
            dateModT, leaker = "0";
    public static String sites = "", areas = "", units = "", drawings = "",
            streams = "";
    public static String data3 = "", data4 = "", data5 = "", data6 = "",
            data7 = "", data8 = "";
    private static TableRow lRow;
    private String counter;
    private Button updateB, backB, clearB, loadL;
    private EditText addSiteTxt, addDrawingTxt, addAreaTxt, addUnitNumberTxt,
            addSpecificTxt, addStreamTxt, addSystemTxt, addEquipLocTxt,
            addUnitNameTxt, addUnitSizeTxt, addPro1, addcf1, addLeakerId,
            emptyB, heightTxt;
    private Button addSite, addDrawing, addArea, addUnitNumber,
            addStreamLocation;
    private Spinner site, drawing, area, unitNumber, streamLocation, level,
            loc1, loc2;
    private String sitePressed, drawingPressed, areaPressed, unitNumberPressed,
            streamLocationPressed, productTT, cfTT, cycTt, heightT, levelT;
    private String currentActivityName, activityName, currentDate,
            emerT = "not";
    private TextView unitName, unitSize, uName, uSize, uNum, unitN, pro1, cf1,
            exT;
    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT;
    private CheckBox emer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current__site);
        initialiseComponents();
        recieveData();
        populateSpinners();
        checkImages();

        updateB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                populateVariables();

                if (siteT.trim().length() > 0 && areaT.trim().length() > 0
                        && unitNumberT.trim().length() > 0
                        && drawingT.trim().length() > 0
                        && specificLocT.trim().length() > 0
                        && streamT.trim().length() > 0
                        && systemT.trim().length() > 0
                        && equipmentLocT.trim().length() > 0) {

                    Toast.makeText(getApplicationContext(), "Saved",
                            Toast.LENGTH_SHORT).show();

                    Toast.makeText(
                            getBaseContext(),
                            "Site:	" + siteT + "\n" + "Area:	" + areaT + "\n"
                                    + "Unit NR:	" + unitNumberT + "\n"
                                    + "Unit:	" + unitT + "\n" + "Unit Size:	"
                                    + unitSizeT + "\n" + "Drawing:	" + drawingT
                                    + "\n" + "Specific Location:	"
                                    + specificLocT + "\n" + "Stream:	"
                                    + streamT + "\n" + "System:	" + systemT
                                    + "\n" + "Equipment Location:	"
                                    + equipmentLocT + "\n" + "Date Created:	"
                                    + dateCreateT, Toast.LENGTH_LONG).show();
                    loadL.setEnabled(true);
                    updateB.setEnabled(false);
                    updateB.setBackgroundResource(R.drawable.submit_inactive);

                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(2);
                    sendData();

                } else {
                    Toast.makeText(getApplicationContext(), "No Empty Fields",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        clearB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                addSiteTxt.setEnabled(true);
                addAreaTxt.setEnabled(true);
                addUnitNumberTxt.setEnabled(true);
                addDrawingTxt.setEnabled(true);
                addStreamTxt.setEnabled(true);

                site.setEnabled(true);
                drawing.setEnabled(true);
                area.setEnabled(true);
                unitNumber.setEnabled(true);
                streamLocation.setEnabled(true);

                addSite.setEnabled(true);
                addDrawing.setEnabled(true);
                addArea.setEnabled(true);
                addUnitNumber.setEnabled(true);

                addStreamLocation.setEnabled(true);

                addSite.setBackgroundResource(R.drawable.edit);
                addDrawing.setBackgroundResource(R.drawable.edit);
                addArea.setBackgroundResource(R.drawable.edit);
                addUnitNumber.setBackgroundResource(R.drawable.edit);

                addStreamLocation.setBackgroundResource(R.drawable.edit);

                addSiteTxt.setVisibility(View.GONE);
                addAreaTxt.setVisibility(View.GONE);
                addUnitNumberTxt.setVisibility(View.GONE);

                addStreamTxt.setVisibility(View.GONE);

                unitName.setVisibility(View.GONE);
                unitSize.setVisibility(View.GONE);

                updateB.setBackgroundResource(R.drawable.submit_inactive);

            }
        });

        site.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                siteT = site.getSelectedItem().toString();
                SharedPreferences save = getSharedPreferences(siteT, 0);
                save.edit().putString("sites", siteT);

                loadAreaSpinner();
                area.setEnabled(true);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        area.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                areaT = area.getSelectedItem().toString();
                SharedPreferences save1 = getSharedPreferences(areaT, 0);
                save1.edit().putString("areas", areaT);

                loadUnitSpinner();
                unitNumber.setEnabled(true);
                loadStreams();
                streamLocation.setEnabled(true);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        unitNumber.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                unitNumberT = unitNumber.getSelectedItem().toString();
                SharedPreferences save2 = getSharedPreferences(unitNumberT, 0);
                save2.edit().putString("units", unitNumberT);

                calculateUnitName();
                loadUnitSize();
                loadDrawingSpinner();
                drawing.setEnabled(true);
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
                // streamT= streamLocation.getSelectedItem().toString();
                // SharedPreferences save4 = getSharedPreferences(streamT, 0);
                // save4.edit().putString("streams", streamT);

            }

            public void onNothingSelected(AdapterView<?> arg0) {

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

                    addAreaTxt.setVisibility(View.GONE);
                    addSiteTxt.setVisibility(View.GONE);
                    addUnitNumberTxt.setVisibility(View.GONE);

                    addStreamTxt.setVisibility(View.GONE);

                    addSite.setText("ADD");
                    addUnitNumber.setText("ADD");
                    addArea.setText("ADD");

                    addStreamLocation.setText("ADD");

                }
            }

        });

        addStreamLocation.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                productTT = addPro1.getText().toString();
                cfTT = addcf1.getText().toString();
                String tmpAr = area.getSelectedItem().toString();
                streamT = addStreamTxt.getText().toString().toUpperCase();
                if (addStreamTxt.isShown()) {
                    addPro1.setVisibility(View.GONE);
                    addcf1.setVisibility(View.GONE);
                    pro1.setVisibility(View.GONE);
                    cf1.setVisibility(View.GONE);
                    exT.setVisibility(View.GONE);
                    addStreamTxt.setVisibility(View.GONE);
                    addStreamLocation.setBackgroundResource(R.drawable.edit);
                    addStreamLocation.setText("ADD");

                    if (cfTT.trim().length() > 0) {
                        // database handler
                        StreamData db = new StreamData(getApplicationContext());
                        // inserting new label into database
                        db.insertStream(tmpAr, streamT, productTT);

                        insertPro();

                        // making input filed text to blank
                        addStreamTxt.setText("");
                        addPro1.setText("");
                        addcf1.setText("");

                        Toast.makeText(getApplicationContext(), "Saved",
                                Toast.LENGTH_SHORT).show();

                        // Hiding the keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(
                                addStreamTxt.getWindowToken(), 0);

                        // loading spinner with newly added data
                        loadStreams();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter All 3 Values", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    addStreamTxt.setVisibility(View.VISIBLE);
                    addPro1.setVisibility(View.VISIBLE);
                    addcf1.setVisibility(View.VISIBLE);
                    pro1.setVisibility(View.VISIBLE);
                    cf1.setVisibility(View.VISIBLE);
                    exT.setVisibility(View.VISIBLE);
                    addStreamLocation.setBackgroundResource(R.drawable.save);
                    addStreamLocation.setText("SAVE");

                    addAreaTxt.setVisibility(View.GONE);
                    addSiteTxt.setVisibility(View.GONE);
                    addUnitNumberTxt.setVisibility(View.GONE);
                    addDrawingTxt.setVisibility(View.GONE);

                    addSite.setText("ADD");
                    addUnitNumber.setText("ADD");
                    addArea.setText("ADD");
                    addDrawing.setText("ADD");
                }
            }
        });

        backB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Current_Site.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        loadL.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                loadLeaker();
                loadL.setEnabled(false);
                updateB.setEnabled(true);
                updateB.setBackgroundResource(R.drawable.submit);
            }
        });

        emer.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (emer.isChecked()) {
                    emerT = "checked";
                    Toast.makeText(getApplicationContext(), emerT, Toast.LENGTH_SHORT).show();
                } else {
                    emerT = "not";
                }
            }
        });
    }

    protected void insertPro() {
        ProClass db = new ProClass(getApplicationContext());

        db.insertProduct(streamT, productTT, cfTT);
        db.close();
    }

    protected void loadStreams() {
        String ar = area.getSelectedItem().toString();
        StreamData db = new StreamData(getApplicationContext());

        // Spinner Drop down elements
        try {
            List<String> pStreams = db.getAllStreams(ar);
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

    private void createcsv() throws IOException {
        // TODO Auto-generated method stub
        String direc = "/e3softData/";
        String fileName = "e3softRepairOrdersExcel.csv";
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
        String Data2 = "site,area,unitNumber,unitName,unitSize,drawing,"
                + "stream,specificLocation,system,equipmentLocation,cameraOperator,"
                + "cameraSerial,gasSurveyor, gasSurveyorSerial,product,"
                + "equipmentDescription,measurementType,equipmentType,"
                + "equipmentSize,equipmentClass,equipmentID,measurementPosition,"
                + "measuremntResult,readingUnit,potentialLoss,recommendations,"
                + "time,date,leakerID" + "\n";
        bw.write(Data2);
        bw.flush();
        bw.close();
    }

    private void loadLeaker() {
        // TODO Auto-generated method stub
        addLeakerId.setText(leaker);
    }

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

    protected void populateVariables() {
        siteT = site.getSelectedItem().toString();
        areaT = area.getSelectedItem().toString();
        unitT = unitNumber.getSelectedItem().toString();
        drawingT = drawing.getSelectedItem().toString();
        unitT = unitName.getText().toString();
        unitSizeT = unitSize.getText().toString();
        streamT = streamLocation.getSelectedItem().toString();
        specificLocT = addSpecificTxt.getText().toString();
        equipmentLocT = addEquipLocTxt.getText().toString();
        systemT = addSystemTxt.getText().toString();
        leaker = addLeakerId.getText().toString().trim();
        heightT = heightTxt.getText().toString();
        levelT = level.getSelectedItem().toString();
        String loc1ss = loc1.getSelectedItem().toString();
        String loc2ss = loc2.getSelectedItem().toString();

        equipmentLocT = loc1ss + " " + loc2ss + " of " + equipmentLocT + "  H:"
                + heightT + "m " + "LV: " + levelT;
        Toast.makeText(getApplicationContext(), equipmentLocT, Toast.LENGTH_SHORT).show();
        if (emerT.equals("checked")) {
            areaT = areaT + "	(Emergency)";
            Toast.makeText(getApplicationContext(), "Area = " + areaT, Toast.LENGTH_SHORT)
                    .show();
        } else {
            areaT = area.getSelectedItem().toString();
        }
    }

    private void recieveData() {
        BroadcastReceiver receiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                camOpT = intent.getStringExtra("camOp");
                camSerialT = intent.getStringExtra("camSerial");
                gasSurveyOpT = intent.getStringExtra("gasSurveyOp");
                gasSurveySerialT = intent.getStringExtra("gasSurveySerial");
                leaker = intent.getStringExtra("li");
                cycTt = intent.getStringExtra("cy");
                Log.d("leaker", leaker + "h");
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.e3soft.receiver");
        registerReceiver(receiver, filter);
    }

    private void populateSpinners() {
        loadSpinnerData();
    }

    private void checkImages() {

        if (updateB.isEnabled()) {
            updateB.setBackgroundResource(R.drawable.submit);
        } else {
            updateB.setBackgroundResource(R.drawable.submit_inactive);
        }
    }

    private void initialiseComponents() {

        updateB = (Button) findViewById(R.id.submitBtn);
        backB = (Button) findViewById(R.id.backBtn);
        clearB = (Button) findViewById(R.id.clearBtn);
        loadL = (Button) findViewById(R.id.updateLeaker);

        addSite = (Button) findViewById(R.id.addSite);
        addUnitNumber = (Button) findViewById(R.id.addUnitNum);
        addDrawing = (Button) findViewById(R.id.addDrawing);
        addArea = (Button) findViewById(R.id.addArea);
        addStreamLocation = (Button) findViewById(R.id.addStreamLocation);

        site = (Spinner) findViewById(R.id.siteCombo);
        area = (Spinner) findViewById(R.id.areaCombo);
        drawing = (Spinner) findViewById(R.id.drawingCombo);
        unitNumber = (Spinner) findViewById(R.id.unitNumCombo);
        streamLocation = (Spinner) findViewById(R.id.streamLocationCombo);
        level = (Spinner) findViewById(R.id.floorCombo);
        loc1 = (Spinner) findViewById(R.id.locDis);
        loc2 = (Spinner) findViewById(R.id.locDir);

        area.setEnabled(false);
        drawing.setEnabled(false);
        unitNumber.setEnabled(false);
        streamLocation.setEnabled(false);

        addSiteTxt = (EditText) findViewById(R.id.addSiteNameTxt);
        addDrawingTxt = (EditText) findViewById(R.id.addDrawingTxt);
        addAreaTxt = (EditText) findViewById(R.id.addAreaTxt);
        addUnitNumberTxt = (EditText) findViewById(R.id.addUnitNumTxt);
        addUnitNameTxt = (EditText) findViewById(R.id.addUnitTxt);
        addUnitSizeTxt = (EditText) findViewById(R.id.addUnitSizeTxt);

        addSpecificTxt = (EditText) findViewById(R.id.addSpecificTxt);
        addStreamTxt = (EditText) findViewById(R.id.addStreamTxt);
        addSystemTxt = (EditText) findViewById(R.id.addSystemTxt);
        addEquipLocTxt = (EditText) findViewById(R.id.addEquipLocTxt);
        addPro1 = (EditText) findViewById(R.id.addProTxt1);
        addcf1 = (EditText) findViewById(R.id.addCfTxt1);
        addLeakerId = (EditText) findViewById(R.id.addLeakerIdTxt);
        emptyB = (EditText) findViewById(R.id.addSiteNameTxt1);
        heightTxt = (EditText) findViewById(R.id.addHeightTxt);

        unitName = (TextView) findViewById(R.id.unitNamesTxt);
        unitSize = (TextView) findViewById(R.id.unitSizeTxt);
        uNum = (TextView) findViewById(R.id.addUnitNumbV);
        uName = (TextView) findViewById(R.id.addUnitNameV);
        uSize = (TextView) findViewById(R.id.addUnitSizeV);
        unitN = (TextView) findViewById(R.id.unitN);
        pro1 = (TextView) findViewById(R.id.addProTxt2);
        cf1 = (TextView) findViewById(R.id.addCfTxt2);
        exT = (TextView) findViewById(R.id.exTt);

        lRow = (TableRow) findViewById(R.id.leakerRow);

        emer = (CheckBox) findViewById(R.id.emerCh);

        addSiteTxt.setVisibility(View.GONE);
        addAreaTxt.setVisibility(View.GONE);
        addUnitNumberTxt.setVisibility(View.GONE);
        addDrawingTxt.setVisibility(View.GONE);
        addStreamTxt.setVisibility(View.GONE);
        unitName.setVisibility(View.GONE);
        unitSize.setVisibility(View.GONE);

        addUnitNameTxt.setVisibility(View.GONE);
        addUnitSizeTxt.setVisibility(View.GONE);

        addPro1.setVisibility(View.GONE);
        addcf1.setVisibility(View.GONE);
        pro1.setVisibility(View.GONE);
        cf1.setVisibility(View.GONE);
        exT.setVisibility(View.GONE);

        uNum.setVisibility(View.GONE);
        uName.setVisibility(View.GONE);
        uSize.setVisibility(View.GONE);
        emptyB.setVisibility(View.GONE);

        addSiteTxt.setSingleLine();
        addDrawingTxt.setSingleLine();
        addAreaTxt.setSingleLine();
        addUnitNumberTxt.setSingleLine();
        addUnitNameTxt.setSingleLine();
        addUnitSizeTxt.setSingleLine();
        addSpecificTxt.setSingleLine();
        addStreamTxt.setSingleLine();
        addSystemTxt.setSingleLine();
        addEquipLocTxt.setSingleLine();
        addcf1.setSingleLine();
        addLeakerId.setSingleLine();
        emptyB.setSingleLine();
        addPro1.setSingleLine();
        addcf1.setSingleLine();
        heightTxt.setSingleLine();

        sitePressed = "True";
        drawingPressed = "True";
        areaPressed = "True";
        unitNumberPressed = "True";
        streamLocationPressed = "True";

        currentActivityName = getBaseContext().getApplicationInfo().className;
        activityName = "Current_Site.java";
        // updateB.setEnabled(false);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        dateCreateT = currentDate;
        dateModT = currentDate;

        unitName.setText("EMPTY");
        unitSize.setText("EMPTY");

        updateB.setEnabled(false);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addSpecificTxt.getWindowToken(), 0);

        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(addSystemTxt.getWindowToken(), 0);

        InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm3.hideSoftInputFromWindow(addEquipLocTxt.getWindowToken(), 0);

        siteT = "0";
        addLeakerId.setFocusable(false);
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

    protected void loadUnitSize() {
        data4 = "";
        String prod4 = "";
        unitNumberT = unitNumber.getSelectedItem().toString();
        String unitNumberT2 = "'" + unitNumberT + "'";
        SQLiteDatabase db4 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/enserve3DatabaseV1.0", null, 0);

        Cursor c4 = db4
                .rawQuery(
                        "SELECT un.unitSize FROM units un , area ar where un.area = ar.area AND un.unitNumber= "
                                + unitNumberT2, null);
        int pro4 = c4.getColumnIndex("unitSize");
        c4.moveToLast();

        // looping through all rows and adding to list
        if (c4.getCount() > 0) {
            do {
                prod4 = c4.getString(pro4);
                data4 = data4 + prod4;
                unitSize.setVisibility(View.VISIBLE);
                unitSize.setText(data4);
            } while (c4.moveToNext());
        } else {
            unitSize.setVisibility(View.VISIBLE);
            unitSize.setText(data4);
            Log.d("unit Size", data4 + "EMPTY");
        }

        // closing connection
        c4.close();
        db4.close();
    }

    private void sendData() {
        systemT.toUpperCase();
        Intent dataIntent2 = new Intent();
        dataIntent2.setAction("com.example.e3soft.receiver");

        dataIntent2.putExtra("camOp", camOpT);
        dataIntent2.putExtra("camSerial", camSerialT);
        dataIntent2.putExtra("gasSurveyOp", gasSurveyOpT);
        dataIntent2.putExtra("gasSurveySerial", gasSurveySerialT);

        dataIntent2.putExtra("site", siteT);
        dataIntent2.putExtra("area", areaT);
        dataIntent2.putExtra("unitNumber", unitNumberT);
        dataIntent2.putExtra("unitName", unitT);
        dataIntent2.putExtra("unitSize", unitSizeT);
        dataIntent2.putExtra("drawing", drawingT);
        dataIntent2.putExtra("streamLocation", streamT);
        dataIntent2.putExtra("leakerID", leaker);
        dataIntent2.putExtra("specificLoc", specificLocT);
        dataIntent2.putExtra("system", systemT);
        dataIntent2.putExtra("equipmentLoc", equipmentLocT);
        dataIntent2.putExtra("count", counter);
        dataIntent2.putExtra("em", emerT);
        dataIntent2.putExtra("cyc2", cycTt);
        sendBroadcast(dataIntent2);
    }

    private void loadSiteSpinner() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        List<String> pSites = db.getAllPSites();

        ArrayAdapter<String> dataAdapterSite = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pSites);

        dataAdapterSite
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        site.setAdapter(dataAdapterSite);

    }

    private void loadAreaSpinner() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        SharedPreferences load = getSharedPreferences(siteT, 0);
        sites = load.getString("sites", siteT);
        Log.d("WebService ", sites);

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

        SharedPreferences load1 = getSharedPreferences(areaT, 0);
        areas = load1.getString("areas", areaT);
        Log.d("WebService ", areas);

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

        SharedPreferences load2 = getSharedPreferences(unitNumberT, 0);
        units = load2.getString("units", unitNumberT);
        Log.d("WebService ", units);

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
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        SharedPreferences load2 = getSharedPreferences(unitNumberT, 0);
        units = load2.getString("units", unitNumberT);
        Log.d("WebService ", units);

        // Spinner Drop down elements
        try {
            List<String> pStreams = db.viewStreams();
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

    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

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
        loc2s.add("N");
        loc2s.add("NE");
        loc2s.add("NW");
        loc2s.add("S");
        loc2s.add("SE");
        loc2s.add("SW");
        loc2s.add("E");
        loc2s.add("W");

        // Creating adapter for spinner

        ArrayAdapter<String> dataAdapterSite = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pSites);

        ArrayAdapter<String> dataAdapterLevels = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, levels);

        ArrayAdapter<String> dataAdapterLoc1s = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, loc1s);

        ArrayAdapter<String> dataAdapterLoc2s = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, loc2s);

        // Drop down layout style - list view with radio button
        dataAdapterSite
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterLevels
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterLoc1s
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapterLoc2s
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        site.setAdapter(dataAdapterSite);
        level.setAdapter(dataAdapterLevels);
        loc1.setAdapter(dataAdapterLoc1s);
        loc2.setAdapter(dataAdapterLoc2s);

    }
}
