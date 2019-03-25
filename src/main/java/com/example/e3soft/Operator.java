package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operator extends Activity {

    private String counter;

    private Button updateB, backB, clearB, cam, newLeakBtn;

    private Button addCamOp, addCamSerial, addGasSurveyOp, addGasSurveySerial;

    private Spinner camOp, camSerial, gasSurveyOp, gasSurveySerial, cycles;

    private String camOpPressed, camSerialPressed, gasSurveyOpPressed,
            gasSurveySerialPressed;

    private String currentActivityName, activityName, DATE_TAKEN, cycTt;

    private EditText camOpTxt, camSerialTxt, gasSurveyOpTxt,
            gasSurveySerialTxt, leakId;

    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT,
            dateCreateT, dateModT, currentDate, leakerID1 = "1", pLeak = "1";

    private String siteT, areaT, unitNumberT, unitT, drawingT, equipmentLocT,
            specificLocT, streamT, systemT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        initialiseComponents();
        populateSpinners();
        loadL();
        checkImages();

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                populateVariables();

                if (camOpT.trim().length() > 0
                        && camSerialT.trim().length() > 0
                        && gasSurveyOpT.trim().length() > 0
                        && gasSurveySerialT.trim().length() > 0
                        && leakerID1.trim().length() > 0) {

                    Toast.makeText(getApplicationContext(), "Saved",
                            Toast.LENGTH_SHORT).show();

                    Toast.makeText(
                            getBaseContext(),
                            "Cam Operator:	" + camOpT + "\n" + "Cam Serial:	"
                                    + camSerialT + "\n"
                                    + "Gas Surveyor Operator:	" + gasSurveyOpT
                                    + "\n" + "Gas Surveyor Serial:	"
                                    + gasSurveySerialT + "\n"
                                    + "Date Created:	" + dateCreateT,
                            Toast.LENGTH_LONG).show();
                    newLeakBtn.setEnabled(true);
                    updateB.setEnabled(false);
                    cam.setEnabled(false);
                    updateB.setBackgroundResource(R.drawable.submit_inactive);
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(1);
                    sendData2();

                } else {
                    Toast.makeText(getApplicationContext(), "No Empty Fields",
                            Toast.LENGTH_LONG).show();
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

                Toast.makeText(getBaseContext(), "Cleared all Data",
                        Toast.LENGTH_LONG).show();

            }
        });

        newLeakBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerID1 = leakId.getText().toString().trim();
                if (leakerID1.length() > 0) {
                    Integer tmpLeaks = Integer.parseInt(leakerID1);
                    tmpLeaks++;
                    leakerID1 = tmpLeaks.toString();
                    leakId.setText(leakerID1);
                    newLeakBtn.setEnabled(false);
                    cam.setEnabled(true);
                    updateB.setEnabled(true);
                    updateB.setBackgroundResource(R.drawable.submit);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Starting Number", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        backB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Operator.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        cam.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerID1 = leakId.getText().toString().trim();
                if (leakerID1.length() > 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = new File(Environment
                            .getExternalStorageDirectory()
                            + "/e3softData/DCIM/");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    String fileName = leakerID1 + ".jpg";
                    File output = new File(dir.getAbsolutePath(), fileName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(output));
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Leaker ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkCompletetion();
    }

    private void loadL() {
        File dir = Environment.getExternalStorageDirectory();
        // File yourFile = new File(dir,
        // "path/to/the/file/inside/the/sdcard.ext");

        // Get the text file
        File file = new File(dir, "e3softData/Data/leaker.txt");
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
            leakId.setText(text);
        } else {
            leakId.setText("Sorry file doesn't exist!!");
        }
    }

    private FileInputStream FileInputStream(File file) {
        // TODO Auto-generated method stub
        return null;
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

    protected void populateVariables() {
        camOpT = camOp.getSelectedItem().toString();
        camSerialT = camSerial.getSelectedItem().toString();
        gasSurveyOpT = gasSurveyOp.getSelectedItem().toString();
        gasSurveySerialT = gasSurveySerial.getSelectedItem().toString();
        leakerID1 = leakId.getText().toString().trim();
        cycTt = cycles.getSelectedItem().toString();
    }

    private void populateSpinners() {
        loadSpinnerData();
    }

    protected void addCamOp() {
        camOpT = camOpTxt.getText().toString();
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
            loadSpinnerData();
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Value",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void checkCompletetion() {
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

        if (updateB.isEnabled()) {
            updateB.setBackgroundResource(R.drawable.submit);
        } else {
            updateB.setBackgroundResource(R.drawable.submit_inactive);
        }
    }

    private void sendData2() {

        // TODO Auto-generated method stub
        Intent dataIntent1 = new Intent();
        dataIntent1.setAction("com.example.e3soft.receiver");

        dataIntent1.putExtra("camOp", camOpT);
        dataIntent1.putExtra("camSerial", camSerialT);
        dataIntent1.putExtra("gasSurveyOp", gasSurveyOpT);
        dataIntent1.putExtra("gasSurveySerial", gasSurveySerialT);
        dataIntent1.putExtra("li", leakerID1);
        dataIntent1.putExtra("cy", cycTt);

        sendBroadcast(dataIntent1);
    }

    private void initialiseComponents() {
        // TODO Auto-generated method stub

        updateB = (Button) findViewById(R.id.submitBtn);
        backB = (Button) findViewById(R.id.backBtn);
        clearB = (Button) findViewById(R.id.clearBtn);
        cam = (Button) findViewById(R.id.addCam);
        newLeakBtn = (Button) findViewById(R.id.newLeakB);

        addCamOp = (Button) findViewById(R.id.addCamOpBtn);
        addCamSerial = (Button) findViewById(R.id.addCamSerialBtn);
        addGasSurveyOp = (Button) findViewById(R.id.addGasOpBtn);
        addGasSurveySerial = (Button) findViewById(R.id.addGasSurveySerialBtn);

        camOp = (Spinner) findViewById(R.id.camOperatorCombo);
        camSerial = (Spinner) findViewById(R.id.camSerialCombo);
        gasSurveyOp = (Spinner) findViewById(R.id.gasSurveyOpCombo);
        gasSurveySerial = (Spinner) findViewById(R.id.gasSurveySerialCombo);
        cycles = (Spinner) findViewById(R.id.cycleCombo);

        camOpTxt = (EditText) findViewById(R.id.addCamOpTxt);
        camSerialTxt = (EditText) findViewById(R.id.addCamSerialTxt);
        gasSurveyOpTxt = (EditText) findViewById(R.id.addSurveyOpTxt);
        gasSurveySerialTxt = (EditText) findViewById(R.id.addSurveySerialTxt);
        leakId = (EditText) findViewById(R.id.leakIdss);

        camOpTxt.setSingleLine();
        camSerialTxt.setSingleLine();
        gasSurveyOpTxt.setSingleLine();
        gasSurveySerialTxt.setSingleLine();
        leakId.setSingleLine();

        camOpTxt.setVisibility(View.GONE);
        camSerialTxt.setVisibility(View.GONE);
        gasSurveyOpTxt.setVisibility(View.GONE);
        gasSurveySerialTxt.setVisibility(View.GONE);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        dateCreateT = currentDate;
        dateModT = currentDate;

        cam.setEnabled(false);
        leakId.setFocusable(false);
        currentActivityName = getBaseContext().getApplicationInfo().className;
        activityName = "Operator.java";
        cam.setVisibility(View.GONE);
    }

    private void loadSpinnerData() {
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
        ArrayAdapter<String> dataAdapterCycles = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                cycless);

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

        camOp.setAdapter(dataAdapterCamOps);
        camSerial.setAdapter(dataAdapterCamSerials);
        gasSurveyOp.setAdapter(dataAdapterSurveyOps);
        gasSurveySerial.setAdapter(dataAdapterSurveySerials);
        cycles.setAdapter(dataAdapterCycles);

        db.close();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        outState.putString("leakerID", leakerID1);
        // outState.putInt("my_int", nMyInt);
        Log.i("onSaveInstanceState", "onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore saved values
        leakerID1 = savedInstanceState.getString("leakerID");
        leakId.setText(leakerID1);
        // nNewMyInt = savedInstanceState.getInt("my_int");
        Log.i("onRestoreInstanceState", "onRestoreInstanceState()");

    }

    protected void onResumeInstanceState(Bundle savedInstanceState) {
        super.onResume();
        // restore values
        leakerID1 = savedInstanceState.getString("leakerID");
        leakId.setText(leakerID1);
        // nNewMyInt = savedInstanceState.getInt("my_int");
        Log.i("onRestoreInstanceState", "onRestoreInstanceState()");

    }
}
