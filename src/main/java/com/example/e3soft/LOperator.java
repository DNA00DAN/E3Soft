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
import java.util.List;

public class LOperator extends Activity {

    private String counter;

    private Button updateB, backB, clearB, cam, newLeakBtn;

    private Button addCamOp, addCamSerial, addGasSurveyOp, addGasSurveySerial;

    private Spinner camOp, camSerial, gasSurveyOp, gasSurveySerial;

    private String camOpPressed, camSerialPressed, gasSurveyOpPressed,
            gasSurveySerialPressed;

    private String currentActivityName, activityName, DATE_TAKEN;

    private EditText camOpTxt, camSerialTxt, gasSurveyOpTxt,
            gasSurveySerialTxt, leakId;

    private String camOpT, camSerialT, gasSurveyOpT, gasSurveySerialT,
            dateCreateT, dateModT, currentDate, leakerID2 = "1", pLeak = "1";

    private String siteT, areaT, unitNumberT, unitT, drawingT, equipmentLocT,
            specificLocT, streamT, systemT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loperator);
        initialiseComponents();
        populateSpinners();
        loadL();
        checkImages();
        updateB.setEnabled(false);
        updateB.setBackgroundResource(R.drawable.submit_inactive);

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                populateVariables();

                if (camOpT.trim().length() > 0
                        && camSerialT.trim().length() > 0
                        && gasSurveyOpT.trim().length() > 0
                        && gasSurveySerialT.trim().length() > 0
                        && leakerID2.trim().length() > 0) {

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
                    updateB.setEnabled(false);
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

                updateB.setBackgroundResource(R.drawable.submit_inactive);
                Toast.makeText(getBaseContext(), "Cleared all Data",
                        Toast.LENGTH_LONG).show();

            }
        });

        newLeakBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerID2 = leakId.getText().toString().trim();
                if (leakerID2.length() > 0) {
                    Integer tmpLeaks = Integer.parseInt(leakerID2);
                    tmpLeaks++;
                    leakerID2 = tmpLeaks.toString();
                    leakId.setText(leakerID2);
                    leakerID2 = "local" + leakerID2;
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
                Intent startNewAtivityOpen = new Intent(LOperator.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        cam.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerID2 = "local" + leakId.getText().toString().trim();
                if (leakerID2.length() > 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = new File(Environment
                            .getExternalStorageDirectory()
                            + "/e3softData/DCIM/local/");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    String fileName = leakerID2 + ".jpg";
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
        leakerID2 = "LM_" + leakId.getText().toString().trim();
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

        dataIntent1.putExtra("camOplo", camOpT);
        dataIntent1.putExtra("camSeriallo", camSerialT);
        dataIntent1.putExtra("gasSurveyOplo", gasSurveyOpT);
        dataIntent1.putExtra("gasSurveySeriallo", gasSurveySerialT);
        dataIntent1.putExtra("lilo", leakerID2);

        sendBroadcast(dataIntent1);
    }

    private void initialiseComponents() {
        // TODO Auto-generated method stub

        updateB = (Button) findViewById(R.id.LosubmitBtn);
        backB = (Button) findViewById(R.id.LobackBtn);
        clearB = (Button) findViewById(R.id.LoclearBtn);
        cam = (Button) findViewById(R.id.LoaddCam);
        newLeakBtn = (Button) findViewById(R.id.LonewLeakB);

        addCamOp = (Button) findViewById(R.id.LoaddCamOpBtn);
        addCamSerial = (Button) findViewById(R.id.LoaddCamSerialBtn);
        addGasSurveyOp = (Button) findViewById(R.id.LoaddGasOpBtn);
        addGasSurveySerial = (Button) findViewById(R.id.LoaddGasSurveySerialBtn);

        camOp = (Spinner) findViewById(R.id.LocamOperatorCombo);
        camSerial = (Spinner) findViewById(R.id.LocamSerialCombo);
        gasSurveyOp = (Spinner) findViewById(R.id.LogasSurveyOpCombo);
        gasSurveySerial = (Spinner) findViewById(R.id.LogasSurveySerialCombo);

        camOpTxt = (EditText) findViewById(R.id.LoaddCamOpTxt);
        camSerialTxt = (EditText) findViewById(R.id.LoaddCamSerialTxt);
        gasSurveyOpTxt = (EditText) findViewById(R.id.LoaddSurveyOpTxt);
        gasSurveySerialTxt = (EditText) findViewById(R.id.LoaddSurveySerialTxt);
        leakId = (EditText) findViewById(R.id.LoleakIdss);

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
        activityName = "LOperator.java";
        cam.setVisibility(View.GONE);
    }

    private void loadSpinnerData() {
        // TODO Auto-generated method stub
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> lpCamOps = db.getAllCamOps();
        List<String> lpCamSerials = db.getAllCamSerials();
        List<String> lpSurveyOps = db.getAllSurveyOps();
        List<String> lpSurveySerials = db.getAllSurveySerials();

        // Creating adapter for spinner
        ArrayAdapter<String> ldataAdapterCamOps = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, lpCamOps);

        ArrayAdapter<String> ldataAdapterCamSerials = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                lpCamSerials);

        ArrayAdapter<String> ldataAdapterSurveyOps = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                lpSurveyOps);

        ArrayAdapter<String> ldataAdapterSurveySerials = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                lpSurveySerials);

        // Drop down layout style - list view with radio button
        ldataAdapterCamOps
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ldataAdapterCamSerials
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ldataAdapterSurveyOps
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ldataAdapterSurveySerials
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        camOp.setAdapter(ldataAdapterCamOps);
        camSerial.setAdapter(ldataAdapterCamSerials);
        gasSurveyOp.setAdapter(ldataAdapterSurveyOps);
        gasSurveySerial.setAdapter(ldataAdapterSurveySerials);

        db.close();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        outState.putString("leakerID", leakerID2);
        // outState.putInt("my_int", nMyInt);
        Log.i("onSaveInstanceState", "onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore saved values
        leakerID2 = savedInstanceState.getString("leakerID");
        leakId.setText(leakerID2);
        // nNewMyInt = savedInstanceState.getInt("my_int");
        Log.i("onRestoreInstanceState", "onRestoreInstanceState()");

    }

    protected void onResumeInstanceState(Bundle savedInstanceState) {
        super.onResume();
        // restore values
        leakerID2 = savedInstanceState.getString("leakerID");
        leakId.setText(leakerID2);
        // nNewMyInt = savedInstanceState.getInt("my_int");
        Log.i("onRestoreInstanceState", "onRestoreInstanceState()");

    }
}
