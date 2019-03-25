package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Historical_Data extends Activity {

    public static String data, data2, data3, data4, data5;
    private Button updateB, backB, searchB;
    private Spinner searchSP;
    private EditText searchCriteria;
    private TextView ids, positions, results, readings, dates;
    private TableLayout latestResult, prevResult;
    private String currentActivityName, activityName, searchC = "",
            searchItem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical__data);
        initialiseComponents();
        populateSpinners();

        updateB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ids.setText("");
                positions.setText("");
                results.setText("");
                readings.setText("");
                dates.setText("");
                TabActivity tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(1);
                sendData();
            }

            private void sendData() {
                // TODO Auto-generated method stub
                Intent dataIntent = new Intent();
                dataIntent.setAction("com.example.e3soft.receiver");

                dataIntent.putExtra("leakerIDH", data);

                sendBroadcast(dataIntent);
            }
        });

        backB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Historical_Data.this,
                        Home.class);
                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        searchB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                latestResult.setVisibility(View.VISIBLE);
                loadData();
                updateB.setEnabled(true);
            }

        });

        searchSP.setOnItemSelectedListener(new OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                searchC = searchSP.getSelectedItem().toString();

                if (searchC == "Leaker ID") {
                    searchC = searchSP.getSelectedItem().toString();
                    SharedPreferences save = getSharedPreferences(searchC, 0);
                    save.edit().putString("searchC", searchC);
                    Log.d("search", searchC);
                } else {
                    Log.d("search", "EMPTY");
                }
            }


            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void loadData() {
        searchItem = searchCriteria.getText().toString();
        searchItem = "'" + searchItem + "'";
        data = "";
        data2 = "";
        data3 = "";
        data4 = "";
        data5 = "";
        String prod = "";
        String prod3 = "";
        String prod2 = "";
        String prod4 = "";
        String prod5 = "";
        Log.d("search", searchItem);
        // TODO Auto-generated method stub
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/highLeaker.db", null, 0);

        Cursor c = db
                .rawQuery(
                        "SELECT hl.leakerID, hl.measurementPosition, hl.measurementResult, hl.readingUnit, hl.date FROM highLeaker hl where hl.leakerID= '" + searchItem +"'" , null);
        int pro = c.getColumnIndex("leakerID");
        int pro2 = c.getColumnIndex("measurementPosition");
        int pro3 = c.getColumnIndex("measurementResult");
        int pro4 = c.getColumnIndex("readingUnit");
        int pro5 = c.getColumnIndex("date");
        c.moveToLast();

        // looping through all rows and adding to list
        if (c.getCount() > 0) {
            do {
                prod = c.getString(pro);
                prod2 = c.getString(pro2);
                prod3 = c.getString(pro3);
                prod4 = c.getString(pro4);
                prod5 = c.getString(pro5);
                data = data + prod;
                data2 = data2 + prod2;
                data3 = data3 + prod3;
                data4 = data4 + prod4;
                data5 = data5 + prod5;
                ids.setText(data);
                positions.setText(data2);
                results.setText(data3);
                readings.setText(data4);
                dates.setText(data5);
            } while (c.moveToNext());
        } else {
            Log.d("unitName", data + "EMPTY");
        }

        // closing connection
        c.close();
        db.close();

    }

    private void populateSpinners() {
        // TODO Auto-generated method stub
        addSearchItems();
    }

    private void addSearchItems() {
        // TODO Auto-generated method stub
        final List<String> searchItems = new ArrayList<String>();
        searchItems.add("Leaker ID");

        ArrayAdapter<String> gasSerialAd = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, searchItems);
        searchSP.setAdapter(gasSerialAd);
    }

    private void initialiseComponents() {
        // TODO Auto-generated method stub

        currentActivityName = getBaseContext().getApplicationInfo().className;
        activityName = "Historical_Data.java";

        updateB = (Button) findViewById(R.id.submitBtn);
        backB = (Button) findViewById(R.id.backBtn);
        searchB = (Button) findViewById(R.id.searchBtn);

        searchSP = (Spinner) findViewById(R.id.searchCombo);

        searchCriteria = (EditText) findViewById(R.id.searchTxt);

        ids = (TextView) findViewById(R.id.ids);
        positions = (TextView) findViewById(R.id.positions);
        results = (TextView) findViewById(R.id.results);
        readings = (TextView) findViewById(R.id.readings);
        dates = (TextView) findViewById(R.id.dates);

        latestResult = (TableLayout) findViewById(R.id.latestResultTb);

        updateB.setEnabled(false);
        latestResult.setVisibility(View.GONE);

        ids.setText("");
        positions.setText("");
        results.setText("");
        readings.setText("");
        dates.setText("");

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
}
