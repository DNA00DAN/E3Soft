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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Local_Marketing extends Activity {
    public static String busiTT, seqT, lockT;
    public static String busis, seqs, locks;
    private Button submitB, restartB;
    private String camOpT, camSerialT, gasOpT, gasSerialT, leakerT;
    private String stationID, contact, drawing;
    private String depotT, depotDrawingT;
    private Spinner sequence, loc, depotSp, cycleSp;
    private Spinner busiType;
    private LinearLayout fStation, vStation, depot;
    private TextView fStat, fCont;
    private String dateCreateT, dateModT;
    private EditText depotDrawing, fDraw;
    private EditText valveStation, valveDrawing;
    private String valveStationT, valveDrawingT;
    private String cycT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local__marketing);
        initialiseComponents();
        receiveData();
        loadBusiSpinner();

        submitB.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (busiTT.equals("Filling Station")) {
                    drawing = fDraw.getText().toString();
                    if (drawing.equals("")) {
                        drawing = "Undefined";
                    }
                    cycT = cycleSp.getSelectedItem().toString();
                    lockT = loc.getSelectedItem().toString();
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(2);
                    sendData3();
                } else if (busiTT.equals("Depot")) {
                    depotT = depotSp.getSelectedItem().toString();
                    depotDrawingT = depotDrawing.getText().toString();
                    if (depotDrawingT.equals("")) {
                        depotDrawingT = "Undefined";
                    }
                    cycT = cycleSp.getSelectedItem().toString();
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(2);
                    sendData3();
                } else if (busiTT.equals("Valve Station")) {
                    valveStationT = valveStation.getText().toString();
                    valveDrawingT = valveDrawing.getText().toString();
                    if (valveDrawingT.equals("")) {
                        valveDrawingT = "Undefined";
                    }
                    cycT = cycleSp.getSelectedItem().toString();
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(2);
                    sendData3();
                } else if (busiTT.equals("Choose 1")) {
                    Toast.makeText(getApplicationContext(),
                            "Complete A survey First", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            private void showFReport() {
                // TODO Auto-generated method stub
                Intent i = new Intent(Local_Marketing.this, Freport.class);
                startActivity(i);
            }
        });

        restartB.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                TabActivity tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(0);
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

        sequence.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                seqT = sequence.getSelectedItem().toString();
                SharedPreferences save2 = getSharedPreferences(seqT, 0);
                save2.edit().putString("seq", seqT);


            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    protected void sendData3() {
        Intent dataIntent3 = new Intent();
        dataIntent3.setAction("com.example.e3soft.receiver");

        dataIntent3.putExtra("camOplo2", camOpT);
        dataIntent3.putExtra("camSeriallo2", camSerialT);
        dataIntent3.putExtra("gasSurveyOplo2", gasOpT);
        dataIntent3.putExtra("gasSurveySeriallo2", gasSerialT);
        dataIntent3.putExtra("lilo2", leakerT);

        dataIntent3.putExtra("seq2", seqT);
        dataIntent3.putExtra("loc2", lockT);
        dataIntent3.putExtra("draw2", drawing);
        dataIntent3.putExtra("cont2", contact);
        dataIntent3.putExtra("id2", stationID);
        dataIntent3.putExtra("busi2", busiTT);

        dataIntent3.putExtra("depot", depotT);
        dataIntent3.putExtra("depotDrawing", depotDrawingT);

        dataIntent3.putExtra("valve", valveStationT);
        dataIntent3.putExtra("valveDrawing", valveDrawingT);

        dataIntent3.putExtra("loCycle", cycT);

        sendBroadcast(dataIntent3);
    }

    private void receiveData() {
        // TODO Auto-generated method stub
        BroadcastReceiver receiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                camOpT = intent.getStringExtra("camOplo");
                camSerialT = intent.getStringExtra("camSeriallo");
                gasOpT = intent.getStringExtra("gasSurveyOplo");
                gasSerialT = intent.getStringExtra("gasSurveySeriallo");
                leakerT = intent.getStringExtra("lilo");
                Log.d("leaker", leakerT + "h");
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.e3soft.receiver");
        registerReceiver(receiver, filter);
    }


    public String calcContact() {
        String data = "";
        String prod = "";
        lockT = loc.getSelectedItem().toString();
        lockT = "'" + lockT + "'";
        // TODO Auto-generated method stub
        SQLiteDatabase db2 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/flocalMarketing.sqlite", null, 0);

        Cursor c = db2.rawQuery(
                "SELECT co.contact FROM contacts co where co.siteID= " + lockT,
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

    private void loadDaySpinner() {
        StationData db = new StationData(getApplicationContext());
        // Spinner Drop down elements
        try {
            List<String> pDays = db.getAlldays();

            ArrayAdapter<String> dataAdapterDay = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item, pDays);

            dataAdapterDay
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sequence.setAdapter(dataAdapterDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBusiSpinner() {
        // TODO Auto-generated method stub
        final List<String> busiTypes = new ArrayList<String>();
        busiTypes.add("Choose 1");
        busiTypes.add("Filling Station");
        busiTypes.add("Valve Station");
        busiTypes.add("Depot");

        final List<String> lCycels = new ArrayList<String>();
        lCycels.add("Cycle 1");
        lCycels.add("Cycle 2");
        lCycels.add("Cycle 3");
        lCycels.add("Cycle 4");
        lCycels.add("Cycle 5");
        lCycels.add("Cycle 6");
        lCycels.add("Cycle 7");
        lCycels.add("Cycle 8");
        lCycels.add("Cycle 9");

        ArrayAdapter<String> busiT = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, busiTypes);

        ArrayAdapter<String> cyce = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, lCycels);

        busiType.setAdapter(busiT);
        cycleSp.setAdapter(cyce);
    }

    private void initialiseComponents() {
        // TODO Auto-generated method stub
        submitB = (Button) findViewById(R.id.lsaveB);
        restartB = (Button) findViewById(R.id.restartB1);

        busiType = (Spinner) findViewById(R.id.businessType);

        sequence = (Spinner) findViewById(R.id.sequenceSp);
        loc = (Spinner) findViewById(R.id.locationSp);
        depotSp = (Spinner) findViewById(R.id.loDepotSp);
        cycleSp = (Spinner) findViewById(R.id.loCycleSp1);

        fStation = (LinearLayout) findViewById(R.id.fStationLayout);
        vStation = (LinearLayout) findViewById(R.id.vStationLayout);
        depot = (LinearLayout) findViewById(R.id.depotLayout);

        fStat = (TextView) findViewById(R.id.fStationTxt);
        fCont = (TextView) findViewById(R.id.fContactTxt);
        fDraw = (EditText) findViewById(R.id.fDrawingTxt);

        depotDrawing = (EditText) findViewById(R.id.dDNumber);
        valveStation = (EditText) findViewById(R.id.vLocationResult);
        valveDrawing = (EditText) findViewById(R.id.vDNumber);

        fStation.setVisibility(View.GONE);
        vStation.setVisibility(View.GONE);
        depot.setVisibility(View.GONE);

        valveStation.setSingleLine();
        valveDrawing.setSingleLine();

        depotDrawing.setSingleLine();

        fDraw.setSingleLine();

        dateCreateT = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);
        dateModT = dateCreateT;
        sequence.setVisibility(View.GONE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.local__marketing, menu);
        return true;
    }

}
