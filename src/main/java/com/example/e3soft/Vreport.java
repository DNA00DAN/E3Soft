package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Vreport extends Activity {

    private TextView date, leaker, camOp, camSerial, gasOp, gasSerial, type,
            location, drawing, recom, product, equipmentDesc, equipmentType,
            mResult, aLoss, reading, equipmentSize, equipmentClass,
            equipmentId, mPosition, dateCreate, subD, cyc;

    private String dateT, leakerT, camOpT, camSerialT, gasOpT, gasSerialT,
            typeT, sequenceT, locationT, StationIDT, contatcNrT, drawingT,
            recomT, productT, equipmentDescT, subDescriptionT, strMRes,
            equipmentTypeT, mResultT, aLossT, readingT, equipmentSizeT,
            strMResult, equipmentClassT, equipmentIdT, mPositionT, dateCreateT,
            dateMod, leakerID1, cycT;

    private Button saveBtn, searchB;

    private Spinner reportsSpin;

    private ImageView img;

    private TableRow tR;

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vreport);
        InitialiseComponents();
        loadSpinnerData();
        populateTextFields();
        loadImage();
        saveBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                try {
                    getValues();
                    saveCSV();
                    savecsv2();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        searchB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                tR.setVisibility(View.VISIBLE);
                loadSpinnerData();

            }
        });
        reportsSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                populateTextFields();
                loadImage();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leakerID1 = "";
                leakerID1 = leaker.getText().toString().trim();
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
                    loadImage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Provide Leaker ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void loadImage() {
        try {
            leakerT = leaker.getText().toString();
            File path = new File("/sdcard/e3softData/DCIM");
            if (path.exists()) {
                String fileNames = path + "/" + leakerT + ".jpg";
                Drawable image = Drawable
                        .createFromPath("/sdcard/e3softData/DCIM" + "/"
                                + leakerT + ".jpg");
                Log.d("image path", fileNames);
                Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
                img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 250, 250,
                        false));
            }
        } catch (Exception e) {
            File path = new File("/sdcard/e3softData/DCIM");
            String fileNames = path + "/" + "000.png";
            Drawable image = Drawable
                    .createFromPath("/sdcard/e3softLocalData/DCIM" + "/"
                            + "000.png");
            Log.d("image path", fileNames);
            Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
            img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 250, 250,
                    false));
        }
    }

    protected void loadSpinnerData() {
        ValveData db = new ValveData(getApplicationContext());

        List<String> pReports = db.getAllReports();

        ArrayAdapter<String> dataAdapterReport = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pReports);

        dataAdapterReport
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportsSpin.setAdapter(dataAdapterReport);
    }

    protected void saveCSV() throws IOException {
        // TODO Auto-generated method stub

        String direc = "/e3softData/";
        String fileName = "valveMarketing.csv";
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

        String data = "'" + typeT + "'" + "," + "'" + locationT + "'" + "," + "'"
                + drawingT + "'" + "," + "'"
                + camOpT + "'" + "," + "'" + camSerialT + "'" + "," + "'"
                + gasOpT + "'" + "," + "'" + gasSerialT + "'" + ","
                + "'" + productT + "'" + "," + "'" + equipmentDescT + "'" + ","
                + "'" + subDescriptionT + "'" + "," + "'" + equipmentTypeT
                + "'" + "," + "'" + equipmentSizeT + "'" + "," + "'"
                + equipmentClassT + "'" + "," + "'" + equipmentIdT + "'" + ","
                + "'" + mPositionT + "'" + "," + "'" + mResultT + "'" + ","
                + "'" + readingT + "'" + "," + "'" + aLossT + "'" + "," + "'"
                + recomT + "'" + "," + "'" + dateCreateT + "'" + "," + "'"
                + leakerT + "'," + "'" + cycT + "'" + ";" + "\n";
        bw.write(data);
        bw.flush();
        bw.close();
    }

    protected void savecsv2() throws IOException {
        // TODO Auto-generated method stub
        String direc = "/e3softData/";
        String fileName = "valveMarketingExcel.csv";
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + direc);
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents
        File file = new File(dir, fileName);
        String f = file.toString();
        if (!file.exists()) {
            createHead();
        }
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(file, true));

        String data = "" + typeT + "" + "," + "" + locationT + "" + "," + ""
                + drawingT + "" + "," + ""
                + camOpT + "" + "," + "" + camSerialT + "" + "," + ""
                + gasOpT + "" + "," + "" + gasSerialT + "" + ","
                + "" + productT + "" + "," + "" + equipmentDescT + "" + ","
                + "" + subDescriptionT + "" + "," + "" + equipmentTypeT
                + "" + "," + "" + equipmentSizeT + "" + "," + ""
                + equipmentClassT + "" + "," + "" + equipmentIdT + "" + ","
                + "" + mPositionT + "" + "," + "" + mResultT + "" + ","
                + "" + readingT + "" + "," + "" + aLossT + "" + "," + ""
                + recomT + "" + "," + "" + dateCreateT + "" + "," + ""
                + leakerT + "," + "" + cycT + "" + ";" + "\n";

        bw.append(data);
        bw.flush();
        bw.close();
    }

    private void createHead() throws IOException {
        String direc = "/e3softData/";
        String fileName = "valveMarketingExcel.csv";
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
        String Data2 = "business,valveStation,drawing," +
                "cameraOperator," +
                "cameraSerial,gasSurveyor, gasSurveyorSerial,product," +
                "equipmentDescription,measurementType,equipmentType," +
                "equipmentSize,equipmentClass,equipmentID,measurementPosition," +
                "measuremntResult,readingUnit,potentialLoss,recommendations," +
                "time,date,leakerID,Cycle" + "\n";
        bw.write(Data2);
        bw.flush();
        bw.close();
    }

    protected void getValues() {
        typeT = type.getText().toString();
        locationT = location.getText().toString();

        drawingT = drawing.getText().toString();

        camOpT = camOp.getText().toString();
        camSerialT = camSerial.getText().toString();
        gasOpT = gasOp.getText().toString();
        gasSerialT = gasSerial.getText().toString();

        productT = product.getText().toString();
        equipmentDescT = equipmentDesc.getText().toString();
        subDescriptionT = subD.getText().toString();
        equipmentTypeT = equipmentType.getText().toString();
        readingT = reading.getText().toString();
        equipmentSizeT = equipmentSize.getText().toString();
        equipmentClassT = equipmentClass.getText().toString();
        equipmentIdT = equipmentId.getText().toString();
        mPositionT = mPosition.getText().toString();
        dateCreateT = date.getText().toString();
        mResultT = mResult.getText().toString();
        aLossT = aLoss.getText().toString();
        recomT = recom.getText().toString();
        leakerT = leaker.getText().toString();
        cycT = cyc.getText().toString();
    }

    private void populateTextFields() {
        leakerT = reportsSpin.getSelectedItem().toString();
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

        String data26 = "";
        String prod26 = "";

        String data27 = "";
        String prod27 = "";

        String data28 = "";
        String prod28 = "";

        String data29 = "";
        String prod29 = "";

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/valveReports.db", null, 0);


        Cursor c10 = db10.rawQuery("SELECT *" + " FROM valveReports reports "
                + "WHERE reports.leakerID = '" + leakerT + "' ORDER BY leakerID DESC", null);

        int pro2 = c10.getColumnIndex("date");
        int pro3 = c10.getColumnIndex("leakerID");

        int pro4 = c10.getColumnIndex("camOperator");
        int pro5 = c10.getColumnIndex("camSerial");
        int pro6 = c10.getColumnIndex("gasSurveyOperator");
        int pro7 = c10.getColumnIndex("gasSurveySerial");

        int pro8 = c10.getColumnIndex("business");
        int pro9 = c10.getColumnIndex("valves");
        int pro12 = c10.getColumnIndex("drawing");

        int pro16 = c10.getColumnIndex("product");
        int pro17 = c10.getColumnIndex("equipmentDesc");
        int pro18 = c10.getColumnIndex("subDescription");
        int pro19 = c10.getColumnIndex("equipmentType");
        int pro20 = c10.getColumnIndex("equipmentSize");
        int pro21 = c10.getColumnIndex("equipmentClass");
        int pro22 = c10.getColumnIndex("equipmentID");
        int pro23 = c10.getColumnIndex("measurementPosition");
        int pro24 = c10.getColumnIndex("measurementResult");
        int pro25 = c10.getColumnIndex("readingUnit");
        int pro27 = c10.getColumnIndex("loss");
        int pro28 = c10.getColumnIndex("recommendation");
        int pro29 = c10.getColumnIndex("cycle");

        c10.moveToLast();

        // looping through all rows and adding to list
        if (c10.getCount() > 0) {
            do {
                prod2 = c10.getString(pro2);
                data2 = data2 + prod2;
                date.setText(data2);

                prod3 = c10.getString(pro3);
                data3 = data3 + prod3;
                leaker.setText(data3);

                prod4 = c10.getString(pro4);
                data4 = data4 + prod4;
                camOp.setText(data4);

                prod5 = c10.getString(pro5);
                data5 = data5 + prod5;
                camSerial.setText(data5);

                prod6 = c10.getString(pro6);
                data6 = data6 + prod6;
                gasOp.setText(data6);

                prod7 = c10.getString(pro7);
                data7 = data7 + prod7;
                gasSerial.setText(data7);

                prod8 = c10.getString(pro8);
                data8 = data8 + prod8;
                type.setText(data8);

                prod9 = c10.getString(pro9);
                data9 = data9 + prod9;
                location.setText(data9);

                prod12 = c10.getString(pro12);
                data12 = data12 + prod12;
                drawing.setText(data12);

                prod16 = c10.getString(pro16);
                data16 = data16 + prod16;
                product.setText(data16);

                prod17 = c10.getString(pro17);
                data17 = data17 + prod17;
                equipmentDesc.setText(data17);

                prod18 = c10.getString(pro18);
                data18 = data18 + prod18;
                subD.setText(data18);

                prod19 = c10.getString(pro19);
                data19 = data19 + prod19;
                equipmentType.setText(data19);

                prod20 = c10.getString(pro20);
                data20 = data20 + prod20;
                equipmentSize.setText(data20);

                prod21 = c10.getString(pro21);
                data21 = data21 + prod21;
                equipmentClass.setText(data21);

                prod22 = c10.getString(pro22);
                data22 = data22 + prod22;
                equipmentId.setText(data22);

                prod23 = c10.getString(pro23);
                data23 = data23 + prod23;
                mPosition.setText(data23);

                prod24 = c10.getString(pro24);
                data24 = data24 + prod24;
                mResult.setText(data24);

                prod25 = c10.getString(pro25);
                data25 = data25 + prod25;
                reading.setText(data25);

                prod27 = c10.getString(pro27);
                data27 = data27 + prod27;
                aLoss.setText(data27);

                prod28 = c10.getString(pro28);
                data28 = data28 + prod28;
                recom.setText(data28);

                prod29 = c10.getString(pro29);
                data29 = data29 + prod29;
                cyc.setText(data29);

            } while (c10.moveToNext());
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        calcTextViewStyle();
        // closing connection
        c10.close();
        db10.close();
    }

    private void calcTextViewStyle() {
        String tmpS = recom.getText().toString();
        if (tmpS.equals("Consider Maintenance")) {
            recom.setTextColor(-16777216);
            recom.setBackgroundColor(-2968436);
            recom.setTypeface(null, Typeface.NORMAL);
        } else if (tmpS.equals("Maintenance Required")) {
            recom.setTextColor(-16777216);
            recom.setBackgroundColor(-2304);
            recom.setTypeface(null, Typeface.NORMAL);
        } else if (tmpS.equals("URGENT")) {
            recom.setTextColor(-16777216);
            recom.setBackgroundColor(-224509);
            recom.setTypeface(null, Typeface.NORMAL);
        } else if (tmpS.equals("DANGER")) {
            recom.setBackgroundColor(-65536);
            recom.setTextColor(-1);
            recom.setTypeface(null, Typeface.BOLD);
        } else {
            recom.setTextColor(-16777216);
            recom.setBackgroundColor(-10242783);
            recom.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void InitialiseComponents() {
        saveBtn = (Button) findViewById(R.id.vSendBtn);
        searchB = (Button) findViewById(R.id.vesearchRe);
        reportsSpin = (Spinner) findViewById(R.id.vereportNr);
        date = (TextView) findViewById(R.id.vDate);
        leaker = (TextView) findViewById(R.id.vLeaker);
        camOp = (TextView) findViewById(R.id.vCamOperator);
        camSerial = (TextView) findViewById(R.id.vCamSerial);
        gasOp = (TextView) findViewById(R.id.vGasOperator);
        gasSerial = (TextView) findViewById(R.id.vGasSerial);
        type = (TextView) findViewById(R.id.vType);
        location = (TextView) findViewById(R.id.vLocation);
        drawing = (TextView) findViewById(R.id.vDrawing);

        product = (TextView) findViewById(R.id.vproduct);
        equipmentDesc = (TextView) findViewById(R.id.vequipDesc);
        subD = (TextView) findViewById(R.id.vsubDesc);
        equipmentType = (TextView) findViewById(R.id.vequipType);
        reading = (TextView) findViewById(R.id.vreading);
        equipmentSize = (TextView) findViewById(R.id.vequipSize);
        equipmentClass = (TextView) findViewById(R.id.vequipClass);
        equipmentId = (TextView) findViewById(R.id.vequipID);
        mPosition = (TextView) findViewById(R.id.vmPosition);
        img = (ImageView) findViewById(R.id.vimageTaken);
        mResult = (TextView) findViewById(R.id.vmResult);
        aLoss = (TextView) findViewById(R.id.vloss);
        recom = (TextView) findViewById(R.id.vrecom);
        cyc = (TextView) findViewById(R.id.vcyc);

        layout = (LinearLayout) findViewById(R.id.velayout1);
        tR = (TableRow) findViewById(R.id.vereportSpTb);
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
}
