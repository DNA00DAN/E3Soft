package com.example.e3soft;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

public class userDetails extends TabActivity {

    public static String ste = "";

    public static String camTech = "", camSerial = "", surveyTech = "", surveySerial = "", cycleRes = "", cycleDate = "", cycleLoss = "", cycleNo = "", cycleRep = "";

    private static Spinner SpcamTech, SpcamSerial, SpsurveyTech, SpsurveySerial,
            SpcycleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetails);

        initialiseComponents();
        populateSpinners();

        System.out.println("user Details loaded");

        SpcycleNo.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try{
                    cycleNo = SpcycleNo.getSelectedItem().toString();
                    calculateCycle();
                    Log.d("Cycle No:	", cycleNo);}
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            private void calculateCycle() {
                if (cycleNo.equals("Cycle 1")) {
                    cycleRes = "cycle1Result";
                    cycleRep = "newC1Result";
                    cycleDate = "cycle1Date";
                    cycleLoss = "c1Loss";
                } else if (cycleNo.equals("Cycle 2")) {
                    cycleRes = "cycle2Result";
                    cycleRep = "newC2Result";
                    cycleDate = "cycle2Date";
                    cycleLoss = "c2Loss";
                } else if (cycleNo.equals("Cycle 3")) {
                    cycleRes = "cycle3Result";
                    cycleRep = "newC3Result";
                    cycleDate = "cycle3Date";
                    cycleLoss = "c3Loss";
                } else if (cycleNo.equals("Cycle 4")) {
                    cycleRes = "cycle4Result";
                    cycleRep = "newC4Result";
                    cycleDate = "cycle4Date";
                    cycleLoss = "c4Loss";
                } else if (cycleNo.equals("Cycle 5")) {
                    cycleRes = "cycle5Result";
                    cycleRep = "newC5Result";
                    cycleDate = "cycle5Date";
                    cycleLoss = "c5Loss";
                } else if (cycleNo.equals("Cycle 6")) {
                    cycleRes = "cycle6Result";
                    cycleRep = "newC6Result";
                    cycleDate = "cycle6Date";
                    cycleLoss = "c6Loss";
                } else if (cycleNo.equals("Cycle 7")) {
                    cycleRes = "cycle7Result";
                    cycleRep = "newC7Result";
                    cycleDate = "cycle7Date";
                    cycleLoss = "c7Loss";
                } else if (cycleNo.equals("Cycle 8")) {
                    cycleRes = "cycle8Result";
                    cycleRep = "newC8Result";
                    cycleDate = "cycle8Date";
                    cycleLoss = "c8Loss";
                } else if (cycleNo.equals("Cycle 9")) {
                    cycleRes = "cycle9Result";
                    cycleRep = "newC9Result";
                    cycleDate = "cycle9Date";
                    cycleLoss = "c9Loss";
                } else if (cycleNo.equals("Cycle 10")) {
                    cycleRes = "cycle10Result";
                    cycleRep = "newC10Result";
                    cycleDate = "cycle10Date";
                    cycleLoss = "c10Loss";
                } else if (cycleNo.equals("Cycle 11")) {
                    cycleRes = "cycle11Result";
                    cycleRep = "newC11Result";
                    cycleDate = "cycle11Date";
                    cycleLoss = "c11Loss";
                } else if (cycleNo.equals("Cycle 12")) {
                    cycleRes = "cycle12Result";
                    cycleRep = "newC12Result";
                    cycleDate = "cycle12Date";
                    cycleLoss = "c12Loss";
                }
                System.err.println(cycleRes);
                System.err.println(cycleRep);
                System.err.println(cycleDate);
                System.err.println(cycleLoss);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        SpcamTech.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try{
                camTech = SpcamTech.getSelectedItem().toString();
                Log.d("Cam Tech:	", camTech);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        SpcamSerial.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try{
                camSerial = SpcamSerial.getSelectedItem().toString();
                Log.d("Cam Serial:	", camSerial);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        SpsurveyTech.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try {
                    surveyTech = SpsurveyTech.getSelectedItem().toString();
                    Log.d("Survey Tech:	", surveyTech);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        SpsurveySerial.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try{
                surveySerial = SpsurveySerial.getSelectedItem().toString();
                Log.d("Survey Serial:	", surveySerial);
                    }catch (Exception e){
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, NewLeaks.class);
        spec = tabHost.newTabSpec("New Leaks").setIndicator("New Leaks")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, HLeaker.class);
        spec = tabHost.newTabSpec("Old Leaks").setIndicator("Old Leaks")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Repairs.class);
        spec = tabHost.newTabSpec("Repairs").setIndicator("Repairs")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

    }

    private void populateSpinners() {
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
        cycless.add("Cycle 10");
        cycless.add("Cycle 11");
        cycless.add("Cycle 12");

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

        SpcamTech.setAdapter(dataAdapterCamOps);
        SpcamSerial.setAdapter(dataAdapterCamSerials);
        SpsurveyTech.setAdapter(dataAdapterSurveyOps);
        SpsurveySerial.setAdapter(dataAdapterSurveySerials);
        SpcycleNo.setAdapter(dataAdapterCycles);

        for(int i = 0; i<dataAdapterCamOps.getCount();i++){
            System.out.println(dataAdapterCamOps.getItem(i).toString());
        }
        for(int i = 0; i<dataAdapterCamSerials.getCount();i++){
            System.out.println(dataAdapterCamSerials.getItem(i).toString());
        }
        for(int i = 0; i<dataAdapterSurveyOps.getCount();i++){
            System.out.println(dataAdapterSurveyOps.getItem(i).toString());
        }
        for(int i = 0; i<dataAdapterSurveySerials.getCount();i++){
            System.out.println(dataAdapterSurveySerials.getItem(i).toString());
        }

        db.close();
    }

    private void initialiseComponents() {
        SpcycleNo = (Spinner) findViewById(R.id.cycleSp);
        SpcamTech = (Spinner) findViewById(R.id.camOperatorSp);
        SpcamSerial = (Spinner) findViewById(R.id.camSerialSp);
        SpsurveyTech = (Spinner) findViewById(R.id.gasSurveyOpSp);
        SpsurveySerial = (Spinner) findViewById(R.id.gasSurveySerialSp);


    }

    private void setTabs() {

    }
}
