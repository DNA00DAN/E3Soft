package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NewReports extends Activity {

    private Spinner leakIDSp;
    private EditText leakerID;
    private ImageView img;
    private Button searchB, refB, submitBtn, compileBtn;
    private TextView date, cycle;
    private TextView site, area, unit, unitName, drawing, system, stream,
            product, oldID, specificLoc, equipLoc;
    private TextView camTech, camSerial, gasTech, gasSerial;
    private TextView equipDesc, maintenanceType, equipType, equipSize, equipID,
            mPosition, mResult, loss, recom;
    private String leakerIDT;
    private String dateT, cycleT;
    private String siteT, areaT, unitT, unitNameT, drawingT, systemT, streamT,
            productT, oldIDT, specificLocT, equipLocT;
    private String camTechT, camSerialT, gasTechT, gasSerialT;
    private String equipDescT, maintenanceTypeT, equipTypeT, equipSizeT,
            equipIDT, mPositionT, mResultT, lossT, recomT;
    private int rec = 0, totalrec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newreports);
        initialiseComponents();
        populateTmpData();

        searchB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String leaker = leakerID.getText().toString().trim();
                if (leaker.length() > 0) {
                    populateTmpData2();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Starting Number", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leakerIDT = "";
                leakerIDT = leakerID.getText().toString().trim();
                if (leakerIDT.length() > 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = new File(Environment
                            .getExternalStorageDirectory()
                            + "/e3softData/DCIM/");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    String fileName = leakerIDT + ".jpg";
                    File output = new File(dir.getAbsolutePath(), fileName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(output));
                    startActivityForResult(intent, 1);
                    loadImage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Leaker ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                loadImage();

            }
        });

        submitBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                populateVariables();
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        compileBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakIDSp.setVisibility(View.VISIBLE);
                populateSpinner();
                totalrec = leakIDSp.getAdapter().getCount();
                leakIDSp.requestFocus();
                leakIDSp.setSelection(rec);
                for (rec = (int) leakIDSp.getSelectedItemId(); rec < totalrec; rec++) {
                    leakIDSp.setSelection(rec);
                    Log.d("Selected Leaker ID", leakIDSp.getSelectedItem().toString());
                    leakerID.setText(leakIDSp.getSelectedItem().toString());
                    populateTmpData2();
                    populateVariables();
                    try {
                        saveFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                DataClass db = new DataClass(getApplicationContext());
                db.deleteTmpData();
            }
        });
    }

    protected void populateSpinner() {
        DataClass db = new DataClass(getApplicationContext());
        List<String> leaks = db.getAllLeaks();
        ArrayAdapter<String> dataAdapterleaks = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                leaks);
        dataAdapterleaks
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leakIDSp.setAdapter(dataAdapterleaks);
    }

    protected void saveFile() throws IOException {
        String direc = "/e3softData/";
        String fileName = "newLeaksData.csv";
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
        String data = "'" + cycleT + "','" + leakerIDT + "','" + dateT + "','" + siteT
                + "','" + areaT + "','" + unitT + "','" + unitNameT + "','" + drawingT
                + "','" + streamT + "','" + productT + "','" + specificLocT + "','"
                + systemT + "','" + equipLocT + "','" + camTechT + "','" + camSerialT
                + "','" + gasTechT + "','" + gasSerialT + "','" + equipDescT + "','"
                + maintenanceTypeT + "','" + equipTypeT + "','" + equipSizeT + "','"
                + equipIDT + "','" + mPositionT + "','" + mResultT + "','" + lossT
                + "','" + recomT + "'\n";

        bw.write(data);
        bw.flush();
        bw.close();
        Toast.makeText(this, "Leak Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    protected void populateVariables() {
        leakerIDT = leakerID.getText().toString();
        dateT = date.getText().toString();
        cycleT = cycle.getText().toString();

        siteT = site.getText().toString();
        areaT = area.getText().toString();
        unitT = unit.getText().toString();
        unitNameT = unitName.getText().toString();
        drawingT = drawing.getText().toString();
        streamT = stream.getText().toString();
        productT = product.getText().toString();
        specificLocT = specificLoc.getText().toString();
        systemT = system.getText().toString();
        equipLocT = equipLoc.getText().toString();

        camTechT = camTech.getText().toString();
        camSerialT = camSerial.getText().toString();
        gasTechT = gasTech.getText().toString();
        gasSerialT = gasSerial.getText().toString();

        equipDescT = equipDesc.getText().toString();
        maintenanceTypeT = maintenanceType.getText().toString();
        equipTypeT = equipType.getText().toString();
        equipSizeT = equipSize.getText().toString();
        equipIDT = equipID.getText().toString();
        mPositionT = mPosition.getText().toString();
        mResultT = mResult.getText().toString();
        lossT = loss.getText().toString();
        recomT = recom.getText().toString();
        if (unitT.equals("18A")) {
            unitT = "18 Tr-I";
        } else if (unitT.equals("18B")) {
            unitT = "18 Tr-II";
        } else if (unitT.equals("18C")) {
            unitT = "18 Tr-III";
        } else if (unitT.equals("18D")) {
            unitT = "18 Tr-IV";
        }
    }

    private void populateTmpData() {
        String data0 = "";
        String prod0 = "";

        String data1 = "";
        String prod1 = "";

        String data2 = "";
        String prod2 = "";

        String data3 = "";
        String prod3 = "";

        String data4 = "";
        String prod4 = "";

        String data5 = "";
        String prod5 = "";

        String data6 = "";
        String prod6 = "";

        String data7 = "";
        String prod7 = "";

        String data8 = "";
        String prod8 = "";

        String data9 = "";
        String prod9 = "";

        String data10 = "";
        String prod10 = "";

        String data11 = "";
        String prod11 = "";

        String data12 = "";
        String prod12 = "";

        String data13 = "";
        String prod13 = "";

        String data14 = "";
        String prod14 = "";

        String data15 = "";
        String prod15 = "";

        String data16 = "";
        String prod16 = "";

        String data17 = "";
        String prod17 = "";

        String data18 = "";
        String prod18 = "";

        String data19 = "";
        String prod19 = "";

        String data20 = "";
        String prod20 = "";

        String data21 = "";
        String prod21 = "";

        String data22 = "";
        String prod22 = "";

        String data23 = "";
        String prod23 = "";

        String data24 = "";
        String prod24 = "";

        String data25 = "";
        String prod25 = "";

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/e3softData.db", null, 0);
        Cursor c10 = db10.rawQuery("SELECT " + "*" + " FROM TmpData", null);

        int pro1 = c10.getColumnIndex("date");
        int pro2 = c10.getColumnIndex("cycle");
        int pro3 = c10.getColumnIndex("site");
        int pro4 = c10.getColumnIndex("area");
        int pro5 = c10.getColumnIndex("unit");
        int pro6 = c10.getColumnIndex("unitName");
        int pro7 = c10.getColumnIndex("drawing");
        int pro8 = c10.getColumnIndex("stream");
        int pro9 = c10.getColumnIndex("product");
        int pro10 = c10.getColumnIndex("specificLoc");
        int pro11 = c10.getColumnIndex("system");
        int pro12 = c10.getColumnIndex("equipmentLoc");

        int pro13 = c10.getColumnIndex("camOperator");
        int pro14 = c10.getColumnIndex("camSerial");
        int pro15 = c10.getColumnIndex("gasSurveyOperator");
        int pro16 = c10.getColumnIndex("gasSurveySerial");

        int pro17 = c10.getColumnIndex("equipmentDesc");
        int pro18 = c10.getColumnIndex("Maintenance type");
        int pro19 = c10.getColumnIndex("equipmentType");
        int pro20 = c10.getColumnIndex("equipmentSize");
        int pro21 = c10.getColumnIndex("equipmentID");

        int pro22 = c10.getColumnIndex("measurementPosition");
        int pro23 = c10.getColumnIndex("result");
        int pro24 = c10.getColumnIndex("loss");
        int pro25 = c10.getColumnIndex("recom");
        int pro00 = c10.getColumnIndex("leakerID");

        c10.moveToLast();

        if (c10.getCount() > 0) {
            do {

                prod0 = c10.getString(pro00);
                data0 = data1 + prod0;
                leakerID.setText(data0);

                prod1 = c10.getString(pro1);
                data1 = data1 + prod1;
                date.setText(data1);

                prod2 = c10.getString(pro2);
                data2 = data2 + prod2;
                cycle.setText(data2);

                prod3 = c10.getString(pro3);
                data3 = data3 + prod3;
                site.setText(data3);

                prod4 = c10.getString(pro4);
                data4 = data4 + prod4;
                area.setText(data4);

                prod5 = c10.getString(pro5);
                data5 = data5 + prod5;
                unit.setText(data5);

                prod6 = c10.getString(pro6);
                data6 = data6 + prod6;
                unitName.setText(data6);

                prod7 = c10.getString(pro7);
                data7 = data7 + prod7;
                drawing.setText(data7);

                prod8 = c10.getString(pro8);
                data8 = data8 + prod8;
                stream.setText(data8);

                prod9 = c10.getString(pro9);
                data9 = data9 + prod9;
                product.setText(data9);

                prod10 = c10.getString(pro10);
                data10 = data10 + prod10;
                specificLoc.setText(data10);

                prod11 = c10.getString(pro11);
                data11 = data11 + prod11;
                system.setText(data11);

                prod12 = c10.getString(pro12);
                data12 = data12 + prod12;
                equipLoc.setText(data12);

                prod13 = c10.getString(pro13);
                data13 = data13 + prod13;
                camTech.setText(data13);

                prod14 = c10.getString(pro14);
                data14 = data14 + prod14;
                camSerial.setText(data14);

                prod15 = c10.getString(pro15);
                data15 = data15 + prod15;
                gasTech.setText(data15);

                prod16 = c10.getString(pro16);
                data16 = data16 + prod16;
                gasSerial.setText(data16);

                prod17 = c10.getString(pro17);
                data17 = data17 + prod17;
                equipDesc.setText(data17);

                prod18 = c10.getString(pro18);
                data18 = data18 + prod18;
                maintenanceType.setText(data18);

                prod19 = c10.getString(pro19);
                data19 = data19 + prod19;
                equipType.setText(data19);

                prod20 = c10.getString(pro20);
                data20 = data20 + prod20;
                equipSize.setText(data20);

                prod21 = c10.getString(pro21);
                data21 = data21 + prod21;
                equipID.setText(data21);

                prod22 = c10.getString(pro22);
                data22 = data22 + prod22;
                mPosition.setText(data22);

                prod23 = c10.getString(pro23);
                data23 = data23 + prod23;
                mResult.setText(data23);

                prod24 = c10.getString(pro24);
                data24 = data24 + prod24;
                loss.setText(data24);

                prod25 = c10.getString(pro25);
                data25 = data25 + prod25;
                recom.setText(data25);

                loadImage();
            } while (c10.moveToNext());
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c10.close();
        db10.close();
    }

    private void populateTmpData2() {
        String data1 = "";
        String prod1 = "";

        String data2 = "";
        String prod2 = "";

        String data3 = "";
        String prod3 = "";

        String data4 = "";
        String prod4 = "";

        String data5 = "";
        String prod5 = "";

        String data6 = "";
        String prod6 = "";

        String data7 = "";
        String prod7 = "";

        String data8 = "";
        String prod8 = "";

        String data9 = "";
        String prod9 = "";

        String data10 = "";
        String prod10 = "";

        String data11 = "";
        String prod11 = "";

        String data12 = "";
        String prod12 = "";

        String data13 = "";
        String prod13 = "";

        String data14 = "";
        String prod14 = "";

        String data15 = "";
        String prod15 = "";

        String data16 = "";
        String prod16 = "";

        String data17 = "";
        String prod17 = "";

        String data18 = "";
        String prod18 = "";

        String data19 = "";
        String prod19 = "";

        String data20 = "";
        String prod20 = "";

        String data21 = "";
        String prod21 = "";

        String data22 = "";
        String prod22 = "";

        String data23 = "";
        String prod23 = "";

        String data24 = "";
        String prod24 = "";

        String data25 = "";
        String prod25 = "";

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/e3softData.db", null, 0);
        Cursor c10 = db10.rawQuery("SELECT " + "*"
                + " FROM TmpData WHERE TmpData.leakerID = '"
                + leakerID.getText().toString() + "'", null);

        int pro1 = c10.getColumnIndex("date");
        int pro2 = c10.getColumnIndex("cycle");
        int pro3 = c10.getColumnIndex("site");
        int pro4 = c10.getColumnIndex("area");
        int pro5 = c10.getColumnIndex("unit");
        int pro6 = c10.getColumnIndex("unitName");
        int pro7 = c10.getColumnIndex("drawing");
        int pro8 = c10.getColumnIndex("stream");
        int pro9 = c10.getColumnIndex("product");
        int pro10 = c10.getColumnIndex("specificLoc");
        int pro11 = c10.getColumnIndex("system");
        int pro12 = c10.getColumnIndex("equipmentLoc");

        int pro13 = c10.getColumnIndex("camOperator");
        int pro14 = c10.getColumnIndex("camSerial");
        int pro15 = c10.getColumnIndex("gasSurveyOperator");
        int pro16 = c10.getColumnIndex("gasSurveySerial");

        int pro17 = c10.getColumnIndex("equipmentDesc");
        int pro18 = c10.getColumnIndex("Maintenance type");
        int pro19 = c10.getColumnIndex("equipmentType");
        int pro20 = c10.getColumnIndex("equipmentSize");
        int pro21 = c10.getColumnIndex("equipmentID");

        int pro22 = c10.getColumnIndex("measurementPosition");
        int pro23 = c10.getColumnIndex("result");
        int pro24 = c10.getColumnIndex("loss");
        int pro25 = c10.getColumnIndex("recom");

        c10.moveToLast();

        if (c10.getCount() > 0) {
            do {

                prod1 = c10.getString(pro1);
                data1 = data1 + prod1;
                date.setText(data1);

                prod2 = c10.getString(pro2);
                data2 = data2 + prod2;
                cycle.setText(data2);

                prod3 = c10.getString(pro3);
                data3 = data3 + prod3;
                site.setText(data3);

                prod4 = c10.getString(pro4);
                data4 = data4 + prod4;
                area.setText(data4);

                prod5 = c10.getString(pro5);
                data5 = data5 + prod5;
                unit.setText(data5);

                prod6 = c10.getString(pro6);
                data6 = data6 + prod6;
                unitName.setText(data6);

                prod7 = c10.getString(pro7);
                data7 = data7 + prod7;
                drawing.setText(data7);

                prod8 = c10.getString(pro8);
                data8 = data8 + prod8;
                stream.setText(data8);

                prod9 = c10.getString(pro9);
                data9 = data9 + prod9;
                product.setText(data9);

                prod10 = c10.getString(pro10);
                data10 = data10 + prod10;
                specificLoc.setText(data10);

                prod11 = c10.getString(pro11);
                data11 = data11 + prod11;
                system.setText(data11);

                prod12 = c10.getString(pro12);
                data12 = data12 + prod12;
                equipLoc.setText(data12);

                prod13 = c10.getString(pro13);
                data13 = data13 + prod13;
                camTech.setText(data13);

                prod14 = c10.getString(pro14);
                data14 = data14 + prod14;
                camSerial.setText(data14);

                prod15 = c10.getString(pro15);
                data15 = data15 + prod15;
                gasTech.setText(data15);

                prod16 = c10.getString(pro16);
                data16 = data16 + prod16;
                gasSerial.setText(data16);

                prod17 = c10.getString(pro17);
                data17 = data17 + prod17;
                equipDesc.setText(data17);

                prod18 = c10.getString(pro18);
                data18 = data18 + prod18;
                maintenanceType.setText(data18);

                prod19 = c10.getString(pro19);
                data19 = data19 + prod19;
                equipType.setText(data19);

                prod20 = c10.getString(pro20);
                data20 = data20 + prod20;
                equipSize.setText(data20);

                prod21 = c10.getString(pro21);
                data21 = data21 + prod21;
                equipID.setText(data21);

                prod22 = c10.getString(pro22);
                data22 = data22 + prod22;
                mPosition.setText(data22);

                prod23 = c10.getString(pro23);
                data23 = data23 + prod23;
                mResult.setText(data23);

                prod24 = c10.getString(pro24);
                data24 = data24 + prod24;
                loss.setText(data24);

                prod25 = c10.getString(pro25);
                data25 = data25 + prod25;
                recom.setText(data25);

            } while (c10.moveToNext());
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
            noteMissingLeak();
        }
        // closing connection
        c10.close();
        db10.close();
    }

    private void initialiseComponents() {
        leakerID = (EditText) findViewById(R.id.reportNrN);
        searchB = (Button) findViewById(R.id.searchBtnN);
        refB = (Button) findViewById(R.id.refBtnN);

        date = (TextView) findViewById(R.id.dateN);
        cycle = (TextView) findViewById(R.id.cycleN);

        site = (TextView) findViewById(R.id.siteN);
        area = (TextView) findViewById(R.id.areaN);
        unit = (TextView) findViewById(R.id.unitN);
        unitName = (TextView) findViewById(R.id.unitNameN);
        drawing = (TextView) findViewById(R.id.drawingN);
        system = (TextView) findViewById(R.id.systemN);
        stream = (TextView) findViewById(R.id.streamN);
        product = (TextView) findViewById(R.id.productN);
        specificLoc = (TextView) findViewById(R.id.specificLocN);
        equipLoc = (TextView) findViewById(R.id.equipLocN);

        camTech = (TextView) findViewById(R.id.camOperatorN);
        camSerial = (TextView) findViewById(R.id.camSerialN);
        gasTech = (TextView) findViewById(R.id.gasOperatorN);
        gasSerial = (TextView) findViewById(R.id.gasSerialN);

        equipDesc = (TextView) findViewById(R.id.equipDescN);
        maintenanceType = (TextView) findViewById(R.id.subDescN);
        equipType = (TextView) findViewById(R.id.equipTypeN);
        equipSize = (TextView) findViewById(R.id.equipSizeN);
        equipID = (TextView) findViewById(R.id.equipIDN);
        mPosition = (TextView) findViewById(R.id.mPositionN);
        mResult = (TextView) findViewById(R.id.mResultN);
        loss = (TextView) findViewById(R.id.lossN);
        recom = (TextView) findViewById(R.id.recomN);

        img = (ImageView) findViewById(R.id.imageTakenN);
        submitBtn = (Button) findViewById(R.id.sendBtnN);
        compileBtn = (Button) findViewById(R.id.compileBtnN);

        leakIDSp = (Spinner) findViewById(R.id.leakerIDSp);
        leakIDSp.setVisibility(View.GONE);
    }

    private void loadImage() {
        try {
            String leakerT = leakerID.getText().toString();
            File path = new File("/sdcard/e3softData/DCIM");
            String fileNames = path + "/" + leakerT + ".jpg";
            Log.d("image path", fileNames);
            Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
            img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    false));
        } catch (Exception e) {
            File path = new File("/sdcard/e3softData/DCIM");
            String fileNames = path + "/" + "000.png";
            Log.d("image path", fileNames);
            Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
            img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    false));
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

    protected void noteMissingLeak() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "Leak does not exist OR Has been replaced with a new ID")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upRe:
                loadUpdatePage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadUpdatePage() {
        Intent startNewAtivityOpen = new Intent(NewReports.this, EditData.class);
        startNewAtivityOpen.putExtra("leakerId", leakerID.getText().toString());
        startActivity(startNewAtivityOpen);
    }
}
