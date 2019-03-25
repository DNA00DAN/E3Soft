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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Reports extends Activity {

    static final int READ_BLOCK_SIZE = 100;
    private Button sendB, refB;
    private Spinner reportsSpin;
    private TableRow tR;
    private ImageView img;
    private Bitmap b;
    private LinearLayout layout;
    private int repNr;
    private TextView site, area, unitNumber, unit, drawing, unitSize,
            equipmentLoc, specificLoc, stream, system, camOp, camSerial,
            gasSurveyOp, gasSurveySerial, product, equipmentDesc,
            equipmentType, mResult, aLoss, reading, equipmentSize,
            equipmentClass, equipmentId, mPosition, dateCreate, report, backG,
            subD, leaker, recom, oldLeaker, cycle;
    private String siteT, areaT, unitNumberT, unitT, unitSizeT, drawingT,
            DATE_TAKEN, equipmentLocT, specificLocT, streamT, systemT, camOpT,
            camSerialT, imageLocT, gasSurveyOpT, gasSurveySerialT, productT,
            equipmentDescT, subDescriptionT, strMRes, equipmentTypeT, mResultT,
            aLossT, readingT, equipmentSizeT, strMResult, strBackG,
            equipmentClassT, equipmentIdT, mPositionT, dateCreateT, dateMod,
            reportT, image, leakerT, leakerID1, recomT, oldLeakerT, cycleT;
    private Integer ii;
    private Double hcValueD = 0.0, nhcValueD = 0.0;
    private String hcValueT, nhcValueT, monthT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initialiseComponents();
        populateTextViews();

        loadImage();
        loadOldLeaker();

        sendB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                try {
                    getValues();
                    saveCSV();
                    savecsv2();
                    savecsv3();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        refB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                loadImage();

            }
        });
        reportsSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                populateTextViews2();
                loadOldLeaker();
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

    protected void savecsv3() throws IOException {
        hcValueT = "0.0";
        nhcValueT = "0.0";
        String st = streamT.substring(0, 2);
        monthT = dateCreateT.substring(7, 14);
        if (st.equals("HC")) {
            hcValueT = aLossT;
            nhcValueT = "0.0";
        } else {
            hcValueT = "0.0";
            nhcValueT = aLossT;
        }
        String direc = "/e3softData/";
        String fileName = "monthlyLoss.csv";
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + direc);
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents
        File file = new File(dir, fileName);
        if (file.exists()) {
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            String data = monthT + "," + hcValueT + "," + nhcValueT + "\n";
            bw.append(data);
            bw.flush();
            bw.close();
        } else {
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            createHead2();
            String data = monthT + "," + hcValueT + "," + nhcValueT + "\n";
            bw.append(data);
            bw.flush();
            bw.close();
        }
    }

    private void createHead2() throws IOException {
        String direc = "/e3softData/";
        String fileName = "monthlyLoss.csv";
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
        String Data2 = "Date,HC,NHC" + "\n";
        bw.write(Data2);
        bw.flush();
        bw.close();
    }

    private void loadOldLeaker() {
        String leakerTs = leaker.getText().toString();
        String data1 = "";
        String prod1 = "";

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/oldLeakers.db", null, 0);
        Cursor c10 = db10.rawQuery("SELECT oldLeakers.oldLeak "
                + "FROM oldLeakers " + "WHERE oldLeakers.leakerID = '"
                + leakerTs + "'", null);

        int pro1 = c10.getColumnIndex("oldLeak");

        c10.moveToFirst();

        // looping through all rows and adding to list
        if (c10.getCount() > 0) {
            do {
                prod1 = c10.getString(pro1);
                data1 = data1 + prod1;
                oldLeaker.setText(data1);
                oldLeakerT = oldLeaker.getText().toString();

            } while (c10.moveToNext());
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c10.close();
        db10.close();
    }

    protected void savecsv2() throws IOException {
        String tmpDate = dateCreateT.substring(7);
        String tmpTime = dateCreateT.substring(0, 6);
        String direc = "/e3softData/";
        String fileName = "e3softRepairOrdersExcel.csv";
        String tmpAre = areaT.substring(4);
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + direc);
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents
        File file = new File(dir, fileName);
        if (file.exists()) {
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            String data = tmpDate + "," + leakerT + "," + tmpAre + ","
                    + unitNumberT + "," + unitT + "," + streamT + ","
                    + productT + "," + specificLocT + "," + equipmentLocT + ","
                    + drawingT + "," + systemT + "," + equipmentDescT + ","
                    + equipmentTypeT + "," + equipmentSizeT + ","
                    + equipmentIdT + "," + mPositionT + "," + subDescriptionT
                    + "," + oldLeakerT + "," + mResultT + "," + cycleT + ","
                    + camOpT + "," + camSerialT + "," + gasSurveyOpT + ","
                    + gasSurveySerialT + "," + aLossT + "," + tmpTime + "\n";
            bw.append(data);
            bw.flush();
            bw.close();
        } else {
            String f = file.toString();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(file, true));
            createHead();
            String data = tmpDate + "," + leakerT + "," + tmpAre + ","
                    + unitNumberT + "," + unitT + "," + streamT + ","
                    + productT + "," + specificLocT + "," + equipmentLocT + ","
                    + drawingT + "," + systemT + "," + equipmentDescT + ","
                    + equipmentTypeT + "," + equipmentSizeT + ","
                    + equipmentIdT + "," + mPositionT + "," + subDescriptionT
                    + "," + oldLeakerT + "," + mResultT + "," + cycleT + ","
                    + camOpT + "," + camSerialT + "," + gasSurveyOpT + ","
                    + gasSurveySerialT + "," + aLossT + "," + tmpTime + "\n";
            bw.append(data);
            bw.flush();
            bw.close();
        }

    }

    private void createHead() throws IOException {
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

        String Data2 = "Date,Report #,Area,Unit #,Unit Name,"
                + "Stream,Product,Specific Information,Equip Location,Drawing,System,"
                + "Equip Desc,Equip Type,"
                + "Equip Size,Equip ID,Leak Source,"
                + "Maintenance Type,"
                + "oldLeakerID,Measurement Result (PPM),Cycle,Camera Operator,Camera Serial #,Gas Surveyor,Sniffer Serial #,Annual Loss (KG),Time"
                + "\n";
        bw.write(Data2);
        bw.flush();
        bw.close();
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

    protected void populateTextViews2() {
        String tmpReport = reportsSpin.getSelectedItem().toString();
        Integer rep = Integer.parseInt(tmpReport);
        layout.setVisibility(View.VISIBLE);
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

        String data30 = "";
        String prod30 = "";

        String data31 = "";
        String prod31 = "";

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/enserve3DatabaseV1.0", null, 0);
        Cursor c10 = db10.rawQuery("SELECT " + "reports.id ,"
                + "reports.site ," + "reports.area ," + "reports.unitNumber ,"
                + "reports.unit ," + "reports.unitSize ," + "reports.drawing ,"
                + "reports.stream ," + "reports.leakerID ,"
                + "reports.specificLocation ," + "reports.systemLocation ,"
                + "reports.equipmentLocation ," + "reports.cameraOperator ,"
                + "reports.cameraSerial ," + "reports.surveyOperator ,"
                + "reports.surveySerial ," + "reports.product ,"
                + "reports.equipmentDescription ," + "reports.subDescription ,"
                + "reports.equipmentType ," + "reports.equipmentSize ,"
                + "reports.equipmentClass ," + "reports.equipmentId ,"
                + "reports.measurementPosition ,"
                + "reports.measurementResult ," + "reports.reading ,"
                + "reports.background ," + "reports.annualLoss ,"
                + "reports.recommendation ," + "reports.image ,"
                + "reports.datecreate " + "FROM reports "
                + "WHERE reports.id = " + rep, null);

        int pro1 = c10.getColumnIndex("id");
        int pro2 = c10.getColumnIndex("site");
        int pro3 = c10.getColumnIndex("area");
        int pro4 = c10.getColumnIndex("unitNumber");
        int pro5 = c10.getColumnIndex("unit");
        int pro6 = c10.getColumnIndex("unitSize");
        int pro7 = c10.getColumnIndex("drawing");
        int pro8 = c10.getColumnIndex("stream");
        int pro31 = c10.getColumnIndex("leakerID");
        int pro9 = c10.getColumnIndex("specificLocation");
        int pro10 = c10.getColumnIndex("systemLocation");
        int pro11 = c10.getColumnIndex("equipmentLocation");

        int pro12 = c10.getColumnIndex("cameraOperator");
        int pro13 = c10.getColumnIndex("cameraSerial");
        int pro14 = c10.getColumnIndex("surveyOperator");
        int pro15 = c10.getColumnIndex("surveySerial");

        int pro16 = c10.getColumnIndex("product");
        int pro17 = c10.getColumnIndex("equipmentDescription");
        int pro18 = c10.getColumnIndex("subDescription");
        int pro19 = c10.getColumnIndex("equipmentType");
        int pro20 = c10.getColumnIndex("equipmentSize");
        int pro21 = c10.getColumnIndex("equipmentClass");
        int pro22 = c10.getColumnIndex("equipmentId");
        int pro23 = c10.getColumnIndex("measurementPosition");
        int pro24 = c10.getColumnIndex("measurementResult");
        int pro25 = c10.getColumnIndex("reading");
        int pro26 = c10.getColumnIndex("background");
        int pro27 = c10.getColumnIndex("annualLoss");
        int pro28 = c10.getColumnIndex("recommendation");
        int pro30 = c10.getColumnIndex("image");
        int pro29 = c10.getColumnIndex("datecreate");

        c10.moveToFirst();

        // looping through all rows and adding to list
        if (c10.getCount() > 0) {
            do {
                prod1 = c10.getString(pro1);
                data1 = data1 + prod1;
                report.setText(data1);

                prod2 = c10.getString(pro2);
                data2 = data2 + prod2;
                site.setText(data2);

                prod3 = c10.getString(pro3);
                data3 = data3 + prod3;
                area.setText(data3);

                prod4 = c10.getString(pro4);
                data4 = data4 + prod4;
                unitNumber.setText(data4);

                prod5 = c10.getString(pro5);
                data5 = data5 + prod5;
                unit.setText(data5);

                prod6 = c10.getString(pro6);
                data6 = data6 + prod6;
                unitSize.setText(data6);

                prod7 = c10.getString(pro7);
                data7 = data7 + prod7;
                drawing.setText(data7);

                prod8 = c10.getString(pro8);
                data8 = data8 + prod8;
                stream.setText(data8);

                prod9 = c10.getString(pro9);
                data9 = data9 + prod9;
                specificLoc.setText(data9);

                prod10 = c10.getString(pro10);
                data10 = data10 + prod10;
                system.setText(data10);

                prod11 = c10.getString(pro11);
                data11 = data11 + prod11;
                equipmentLoc.setText(data11);

                prod12 = c10.getString(pro12);
                data12 = data12 + prod12;
                camOp.setText(data12);

                prod13 = c10.getString(pro13);
                data13 = data13 + prod13;
                camSerial.setText(data13);

                prod14 = c10.getString(pro14);
                data14 = data14 + prod14;
                gasSurveyOp.setText(data14);

                prod15 = c10.getString(pro15);
                data15 = data15 + prod15;
                gasSurveySerial.setText(data15);

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

                prod26 = c10.getString(pro26);
                data26 = data26 + prod26;
                backG.setText(data26);

                prod27 = c10.getString(pro27);
                data27 = data27 + prod27;
                aLoss.setText(data27);

                prod28 = c10.getString(pro28);
                data28 = data28 + prod28;
                recom.setText(data28);

                prod29 = c10.getString(pro29);
                data29 = data29 + prod29;
                dateCreate.setText(data29);

                prod31 = c10.getString(pro31);
                data31 = data31 + prod31;
                leaker.setText(data31);
                loadImage();

            } while (c10.moveToNext());
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // loadImage();
        calcTextViewStyle();
        // closing connection
        c10.close();
        db10.close();
    }

    protected void populateTextViews() {
        layout.setVisibility(View.VISIBLE);

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

        String data30 = "";
        String prod30 = "";

        String data31 = "";
        String prod31 = "";

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/enserve3DatabaseV1.0", null, 0);
        Cursor c10 = db10.rawQuery("SELECT " + "reports.id ,"
                + "reports.site ," + "reports.area ," + "reports.unitNumber ,"
                + "reports.unit ," + "reports.unitSize ," + "reports.drawing ,"
                + "reports.stream ," + "reports.leakerID ,"
                + "reports.specificLocation ," + "reports.systemLocation ,"
                + "reports.equipmentLocation ," + "reports.cameraOperator ,"
                + "reports.cameraSerial ," + "reports.surveyOperator ,"
                + "reports.surveySerial ," + "reports.product ,"
                + "reports.equipmentDescription ," + "reports.subDescription ,"
                + "reports.equipmentType ," + "reports.equipmentSize ,"
                + "reports.equipmentClass ," + "reports.equipmentId ,"
                + "reports.measurementPosition ,"
                + "reports.measurementResult ," + "reports.reading ,"
                + "reports.background ," + "reports.annualLoss ,"
                + "reports.recommendation ," + "reports.cycle ,"
                + "reports.datecreate " + "FROM reports", null);

        int pro1 = c10.getColumnIndex("id");
        int pro2 = c10.getColumnIndex("site");
        int pro3 = c10.getColumnIndex("area");
        int pro4 = c10.getColumnIndex("unitNumber");
        int pro5 = c10.getColumnIndex("unit");
        int pro6 = c10.getColumnIndex("unitSize");
        int pro7 = c10.getColumnIndex("drawing");
        int pro8 = c10.getColumnIndex("stream");
        int pro31 = c10.getColumnIndex("leakerID");
        int pro9 = c10.getColumnIndex("specificLocation");
        int pro10 = c10.getColumnIndex("systemLocation");
        int pro11 = c10.getColumnIndex("equipmentLocation");

        int pro12 = c10.getColumnIndex("cameraOperator");
        int pro13 = c10.getColumnIndex("cameraSerial");
        int pro14 = c10.getColumnIndex("surveyOperator");
        int pro15 = c10.getColumnIndex("surveySerial");

        int pro16 = c10.getColumnIndex("product");
        int pro17 = c10.getColumnIndex("equipmentDescription");
        int pro18 = c10.getColumnIndex("subDescription");
        int pro19 = c10.getColumnIndex("equipmentType");
        int pro20 = c10.getColumnIndex("equipmentSize");
        int pro21 = c10.getColumnIndex("equipmentClass");
        int pro22 = c10.getColumnIndex("equipmentId");
        int pro23 = c10.getColumnIndex("measurementPosition");
        int pro24 = c10.getColumnIndex("measurementResult");
        int pro25 = c10.getColumnIndex("reading");
        int pro26 = c10.getColumnIndex("background");
        int pro27 = c10.getColumnIndex("annualLoss");
        int pro28 = c10.getColumnIndex("recommendation");
        int pro30 = c10.getColumnIndex("cycle");
        int pro29 = c10.getColumnIndex("datecreate");

        c10.moveToLast();

        // looping through all rows and adding to list
        if (c10.getCount() > 0) {
            do {
                prod1 = c10.getString(pro1);
                data1 = data1 + prod1;
                report.setText(data1);

                prod2 = c10.getString(pro2);
                data2 = data2 + prod2;
                site.setText(data2);

                prod3 = c10.getString(pro3);
                data3 = data3 + prod3;
                area.setText(data3);

                prod4 = c10.getString(pro4);
                data4 = data4 + prod4;
                unitNumber.setText(data4);

                prod5 = c10.getString(pro5);
                data5 = data5 + prod5;
                unit.setText(data5);

                prod6 = c10.getString(pro6);
                data6 = data6 + prod6;
                unitSize.setText(data6);

                prod7 = c10.getString(pro7);
                data7 = data7 + prod7;
                drawing.setText(data7);

                prod8 = c10.getString(pro8);
                data8 = data8 + prod8;
                stream.setText(data8);

                prod9 = c10.getString(pro9);
                data9 = data9 + prod9;
                specificLoc.setText(data9);

                prod10 = c10.getString(pro10);
                data10 = data10 + prod10;
                system.setText(data10);

                prod11 = c10.getString(pro11);
                data11 = data11 + prod11;
                equipmentLoc.setText(data11);

                prod12 = c10.getString(pro12);
                data12 = data12 + prod12;
                camOp.setText(data12);

                prod13 = c10.getString(pro13);
                data13 = data13 + prod13;
                camSerial.setText(data13);

                prod14 = c10.getString(pro14);
                data14 = data14 + prod14;
                gasSurveyOp.setText(data14);

                prod15 = c10.getString(pro15);
                data15 = data15 + prod15;
                gasSurveySerial.setText(data15);

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

                prod26 = c10.getString(pro26);
                data26 = data26 + prod26;
                backG.setText(data26);

                prod27 = c10.getString(pro27);
                data27 = data27 + prod27;
                aLoss.setText(data27);

                prod28 = c10.getString(pro28);
                data28 = data28 + prod28;
                recom.setText(data28);

                prod29 = c10.getString(pro29);
                data29 = data29 + prod29;
                dateCreate.setText(data29);

                prod31 = c10.getString(pro31);
                data31 = data31 + prod31;
                leaker.setText(data31);

                prod30 = c10.getString(pro30);
                data30 = data30 + prod30;
                cycle.setText(data30);

                loadImage();

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

    protected void saveCSV() throws IOException {

        String direc = "/e3softData/";
        String fileName = "e3softRepairOrders.csv";
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

        String data = "'" + siteT + "'" + "," + "'" + areaT + "'" + "," + "'"
                + unitNumberT + "'" + "," + "'" + unitT + "'" + "," + "'"
                + unitSizeT + "'" + "," + "'" + drawingT + "'" + "," + "'"
                + streamT + "'" + "," + "'" + specificLocT + "'" + "," + "'"
                + systemT + "'" + "," + "'" + equipmentLocT + "'" + "," + "'"
                + camOpT + "'" + "," + "'" + camSerialT + "'" + "," + "'"
                + gasSurveyOpT + "'" + "," + "'" + gasSurveySerialT + "'" + ","
                + "'" + productT + "'" + "," + "'" + equipmentDescT + "'" + ","
                + "'" + subDescriptionT + "'" + "," + "'" + equipmentTypeT
                + "'" + "," + "'" + equipmentSizeT + "'" + "," + "'"
                + equipmentClassT + "'" + "," + "'" + equipmentIdT + "'" + ","
                + "'" + mPositionT + "'" + "," + "'" + mResultT + "'" + ","
                + "'" + readingT + "'" + "," + "'" + aLossT + "'" + "," + "'"
                + recomT + "'" + "," + "'" + dateCreateT + "'" + "," + "'"
                + leakerT + "'" + ",'" + oldLeakerT + "','" + cycleT + "';"
                + "\n";
        bw.write(data);
        bw.flush();
        bw.close();
    }

    private void loadImage() {
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

    private void getValues() {
        // TODO Auto-generated method stub
        Bundle extras = getIntent().getExtras();

        siteT = site.getText().toString();
        areaT = area.getText().toString();
        unitNumberT = unitNumber.getText().toString();
        unitT = unit.getText().toString();
        unitSizeT = unitSize.getText().toString();

        drawingT = drawing.getText().toString();
        equipmentLocT = equipmentLoc.getText().toString();
        specificLocT = specificLoc.getText().toString();
        streamT = stream.getText().toString();
        systemT = system.getText().toString();

        camOpT = camOp.getText().toString();
        camSerialT = camSerial.getText().toString();
        gasSurveyOpT = gasSurveyOp.getText().toString();
        gasSurveySerialT = gasSurveySerial.getText().toString();

        productT = product.getText().toString();
        equipmentDescT = equipmentDesc.getText().toString();
        subDescriptionT = subD.getText().toString();
        equipmentTypeT = equipmentType.getText().toString();
        readingT = reading.getText().toString();
        equipmentSizeT = equipmentSize.getText().toString();
        equipmentClassT = equipmentClass.getText().toString();
        equipmentIdT = equipmentId.getText().toString();
        mPositionT = mPosition.getText().toString();
        dateCreateT = dateCreate.getText().toString();
        mResultT = mResult.getText().toString();
        aLossT = aLoss.getText().toString();
        recomT = recom.getText().toString();
        leakerT = leaker.getText().toString();
        cycleT = cycle.getText().toString();

    }

    private void initialiseComponents() {
        layout = (LinearLayout) findViewById(R.id.layout1);
        tR = (TableRow) findViewById(R.id.reportSpTb);
        sendB = (Button) findViewById(R.id.sendBtn);
        refB = (Button) findViewById(R.id.refBtn);

        reportsSpin = (Spinner) findViewById(R.id.reportNr);

        site = (TextView) findViewById(R.id.site);
        area = (TextView) findViewById(R.id.area);
        unitNumber = (TextView) findViewById(R.id.unit);
        unit = (TextView) findViewById(R.id.unitName);
        unitSize = (TextView) findViewById(R.id.unitSize);

        drawing = (TextView) findViewById(R.id.drawing);
        equipmentLoc = (TextView) findViewById(R.id.equipLoc);
        specificLoc = (TextView) findViewById(R.id.specificLoc);
        stream = (TextView) findViewById(R.id.stream);
        system = (TextView) findViewById(R.id.system);

        camOp = (TextView) findViewById(R.id.camOperator);
        camSerial = (TextView) findViewById(R.id.camSerial);
        gasSurveyOp = (TextView) findViewById(R.id.gasOperator);
        gasSurveySerial = (TextView) findViewById(R.id.gasSerial);

        report = (TextView) findViewById(R.id.reportTxt);
        product = (TextView) findViewById(R.id.product);
        equipmentDesc = (TextView) findViewById(R.id.equipDesc);
        subD = (TextView) findViewById(R.id.subDesc);
        equipmentType = (TextView) findViewById(R.id.equipType);
        reading = (TextView) findViewById(R.id.reading);
        equipmentSize = (TextView) findViewById(R.id.equipSize);
        equipmentClass = (TextView) findViewById(R.id.equipClass);
        equipmentId = (TextView) findViewById(R.id.equipID);
        mPosition = (TextView) findViewById(R.id.mPosition);
        dateCreate = (TextView) findViewById(R.id.date);
        img = (ImageView) findViewById(R.id.imageTaken);
        mResult = (TextView) findViewById(R.id.mResult);
        aLoss = (TextView) findViewById(R.id.loss);
        backG = (TextView) findViewById(R.id.backGr);
        recom = (TextView) findViewById(R.id.recom);
        leaker = (TextView) findViewById(R.id.leakerTxt);
        cycle = (TextView) findViewById(R.id.cycle);
        oldLeaker = (TextView) findViewById(R.id.oldleakerTxt);

        layout.setVisibility(View.GONE);
        tR.setVisibility(View.GONE);
        // searchB.setVisibility(View.GONE);

    }

    private void loadSpinnerData() {
        // TODO Auto-generated method stub
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        List<String> pReports = db.getAllReports();

        ArrayAdapter<String> dataAdapterReport = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pReports);

        dataAdapterReport
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportsSpin.setAdapter(dataAdapterReport);
    }

    protected void onResumeInstanceState(Bundle savedInstanceState) {
        super.onResume();
        loadImage();
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
        Intent startNewAtivityOpen = new Intent(Reports.this, EditData.class);

        startActivity(startNewAtivityOpen);
    }
}
