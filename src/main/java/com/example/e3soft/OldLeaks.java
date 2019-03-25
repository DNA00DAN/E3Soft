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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class OldLeaks extends Activity {

    public static String refinery, uni;
    private Spinner siteSp;
    private EditText searchEt;
    private Button searchBtn, updateB, clearB;
    private TextView date;
    private TextView site, area, unit, unitName, drawing, equipmentLoc,
            specificLoc, stream, system, product, equipmentDesc, equipmentType,
            equipmentSize, equipmentId, mPosition, leaker;
    private String siteT, areaT, unitT, unitNameT, drawingT, equipmentLocT,
            specificLocT, streamT, systemT, productT, equipmentDescT,
            equipmentTypeT, equipmentSizeT, equipmentIdT, mPositionT,
            leakerT = "1", prevResT;
    private Cursor c10;
    private TextView cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8, cc9, cc10, cc11,
            cc12;
    private TextView ccc1, ccc2, ccc3, ccc4, ccc5, ccc6, ccc7, ccc8, ccc9,
            ccc10, ccc11, ccc12;
    private String cc1T, cc2T, cc3T, cc4T, cc5T, cc6T, cc7T, cc8T, cc9T, cc10T,
            cc11T, cc12T;
    private String ccc1T, ccc2T, ccc3T, ccc4T, ccc5T, ccc6T, ccc7T, ccc8T,
            ccc9T, ccc10T, ccc11T, ccc12T;
    private TableRow tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9, tt10, tt11,
            tt12;
    private String currentDate, strRecomedation, strCf;
    private EditText mResult, backGr;
    private RadioButton ppm, lel, gas;
    private Double mRes = 0.0, backGD = 0.0, lRa = 0.0, sv = 0.0, cf = 0.0;
    private String reading, mResultT, backGrT, correctedReading, lossT,
            maintenanceTypeT;
    private Double correctedReading1;
    private ImageView img;
    private Button loadPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldleaks);

        initialiseComponents();
        populateSites();
        populateGiData();
        populatePrevRes();
        checkPrevRes();
        img.setVisibility(View.GONE);

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leakerT = "";
                leakerT = searchEt.getText().toString().trim();
                if (leakerT.length() > 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = new File(Environment
                            .getExternalStorageDirectory()
                            + "/e3softData/DCIM/");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    String fileName = leakerT + ".jpg";
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

        loadPic.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (loadPic.getText().toString().equals("Show Image")) {
                    img.setVisibility(View.VISIBLE);
                    loadPic.setText("Hide Image");
                    loadImage();
                } else {
                    img.setVisibility(View.GONE);
                    loadPic.setText("Show Image");
                }
            }
        });

        clearB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                lel.setVisibility(View.VISIBLE);
                ppm.setVisibility(View.VISIBLE);
                gas.setVisibility(View.VISIBLE);
            }
        });

        searchBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                leakerT = searchEt.getText().toString();
                populateGiData();
                populatePrevRes();
                checkPrevRes();
                if (img.isShown()) {
                    loadImage();
                }
                if (!leaker.getText().toString().equals(searchEt.getText().toString())) {
                    noteMissingLeak();
                }
            }
        });

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (userDetails.camTech.equals(userDetails.surveyTech)) {
                    noteUnchangedData();
                } else {
                    leakerT = searchEt.getText().toString();
                    populateVariables();
                    calculatePrevRes();
                    calcBackGround();
                    calcCf();
                    calcLoss();

                    updateRes();
                    updateDates();
                    updateLoss();
                    try {
                        saveFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    clearRes();
                }
            }
        });

        ppm.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                reading = "PPM";
                lel.setVisibility(View.GONE);
                gas.setVisibility(View.GONE);

            }

        });

        lel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                reading = "LEL";
                ppm.setVisibility(View.GONE);
                gas.setVisibility(View.GONE);
            }
        });

        gas.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                reading = "GAS %";
                lel.setVisibility(View.GONE);
                ppm.setVisibility(View.GONE);
            }
        });

        siteSp.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                //loadUnitSpinner();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    protected void noteMissingLeak() {
        new AlertDialog.Builder(this)
                .setMessage("Leak does not exist OR Has been replaced with a new ID")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).show();
    }

    protected void noteUnchangedData() {
        new AlertDialog.Builder(this)
                .setMessage("Please Complete Technician Details & Cycle Details")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).show();
    }

    protected void saveFile() throws IOException {
        String direc = "/e3softData/";
        String fileName = "oldLeaksData.csv";
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
        String data = "'" + userDetails.cycleRes + "','" + leakerT + "','" + currentDate
                + "','" + siteT + "','" + areaT + "','" + unitT + "','" + unitNameT
                + "','" + drawingT + "','" + streamT + "','" + productT + "','"
                + specificLocT + "','" + systemT + "','" + equipmentLocT + "','"
                + userDetails.camTech + "','" + userDetails.camSerial + "','"
                + userDetails.surveyTech + "','" + userDetails.surveySerial + "','"
                + equipmentDescT + "','" + maintenanceTypeT + "','"
                + equipmentTypeT + "','" + equipmentSizeT + "','" + equipmentIdT
                + "','" + mPositionT + "','" + correctedReading + "','" + lossT + "','"
                + strRecomedation + "'\n";

        bw.write(data);
        bw.flush();
        bw.close();
    }

    protected void loadImage() {
        try {
            leakerT = searchEt.getText().toString();
            File path = new File("/mnt/extSdCard/e3softData/DCIM");
            if (path.exists()) {
                String fileNames = path + "/" + leakerT + ".jpg";
                Log.d("image path", fileNames);
                Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
                img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 350, 350,
                        false));
            } else {
                File path2 = new File("/sdcard/e3softData/DCIM");
                String fileNames = path2 + "/" + leakerT + ".jpg";
                Log.d("image path", fileNames);
                Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
                img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 350, 350,
                        false));
                Toast.makeText(getApplicationContext(), "NO IMAGE ON SD CARD",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            File path = new File("/sdcard/e3softData/Data");
            String fileNames = path + "/" + "000.png";
            Log.d("image path", fileNames);
            Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
            img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 250, 250,
                    false));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                .setNeutralButton("HOME",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(OldLeaks.this, Home.class);
                                startActivity(i);
                                finish();
                            }
                        }).setNegativeButton("NO", null).show();
    }

    protected void clearRes() {
        lel.setVisibility(View.VISIBLE);
        ppm.setVisibility(View.VISIBLE);
        gas.setVisibility(View.VISIBLE);
        mResult.setText("");
        populatePrevRes();
        checkPrevRes();
        searchEt.requestFocus();
    }

    protected void calcCf() {
        String data2 = "";
        String prod2 = "";

        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/newPros.db", null, 0);

        Cursor c2 = db.rawQuery(
                "SELECT pr.cf FROM nwPros pr where pr.stream = " + "'"
                        + streamT + "'", null);
        int pro2 = c2.getColumnIndex("cf");
        c2.moveToLast();

        // looping through all rows and adding to list
        if (c2 != null) {
            do {
                prod2 = c2.getString(pro2);
                data2 = data2 + prod2;
                cf = Double.parseDouble(data2);
                strCf = cf.toString();
                Log.d("cf", strCf);
            } while (c2.moveToNext());
        }

        // closing connection
        c2.close();
        db.close();
    }

    protected void updateRes() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteSp.getSelectedItem().toString().equals("Mina Al Ahmadi")) {
            db.updateMaaRes(Integer.parseInt(leakerT), userDetails.cycleRes,
                    correctedReading);
        } else if (siteSp.getSelectedItem().toString().equals("Mina Abdulla")) {
            db.updateMabRes(Integer.parseInt(leakerT), userDetails.cycleRes,
                    correctedReading);
        }
        if (siteSp.getSelectedItem().toString().equals("Shuaiba")) {
            db.updateShuRes(Integer.parseInt(leakerT), userDetails.cycleRes,
                    correctedReading);
        }
    }

    protected void updateLoss() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteSp.getSelectedItem().toString().equals("Mina Al Ahmadi")) {
            db.updateMaaLoss(Integer.parseInt(leakerT), userDetails.cycleLoss,
                    lossT);
        } else if (siteSp.getSelectedItem().toString().equals("Mina Abdulla")) {
            db.updateMabLoss(Integer.parseInt(leakerT), userDetails.cycleLoss,
                    lossT);
        }
        if (siteSp.getSelectedItem().toString().equals("Shuaiba")) {
            db.updateShuLoss(Integer.parseInt(leakerT), userDetails.cycleLoss,
                    lossT);
        }
    }

    protected void updateDates() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteSp.getSelectedItem().toString().equals("Mina Al Ahmadi")) {
            db.updateMaaDates(Integer.parseInt(leakerT), userDetails.cycleDate,
                    currentDate);
        } else if (siteSp.getSelectedItem().toString().equals("Mina Abdulla")) {
            db.updateMabDates(Integer.parseInt(leakerT), userDetails.cycleDate,
                    currentDate);
        }
        if (siteSp.getSelectedItem().toString().equals("Shuaiba")) {
            db.updateShuDates(Integer.parseInt(leakerT), userDetails.cycleDate,
                    currentDate);
        }
    }

    protected void calcBackGround() {
        backGrT = backGr.getText().toString();

        if (backGrT == null || backGrT.isEmpty()) {
            backGD = 0.0;
        } else {
            backGD = Double.parseDouble(backGrT);
        }
    }

    protected void calcLoss() {
        mRes = Double.parseDouble(mResult.getText().toString());

        if (reading == "LEL") {
            mRes = ((mRes * 1000) / 2);
        }
        if (reading == "GAS %") {
            mRes = (mRes * 10000);
        }
        correctedReading1 = (cf * mRes);
        calculateRecomedation();
        correctedReading = correctedReading1.toString();
        reading = "PPM";
        sv = ((cf * mRes) - backGD);

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
                lRa = (0.11 * 8760);
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

    protected void calculatePrevRes() {
        if (userDetails.cycleNo.equals("Cycle 1")) {
            prevResT = "NA";
        } else if (userDetails.cycleNo.equals("Cycle 2")) {
            prevResT = cc1.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 3")) {
            prevResT = cc2.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 4")) {
            prevResT = cc3.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 5")) {
            prevResT = cc4.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 6")) {
            prevResT = cc5.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 7")) {
            prevResT = cc6.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 8")) {
            prevResT = cc7.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 9")) {
            prevResT = cc8.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 10")) {
            prevResT = cc9.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 11")) {
            prevResT = cc10.getText().toString();
        } else if (userDetails.cycleNo.equals("Cycle 12")) {
            prevResT = cc11.getText().toString();
        }
    }

    protected void populateVariables() {
        siteT = site.getText().toString();
        areaT = area.getText().toString();
        unitT = unit.getText().toString();
        unitNameT = unitName.getText().toString();
        drawingT = drawing.getText().toString();
        equipmentLocT = equipmentLoc.getText().toString();
        specificLocT = specificLoc.getText().toString();
        streamT = stream.getText().toString();
        systemT = system.getText().toString();
        productT = product.getText().toString();
        equipmentDescT = equipmentDesc.getText().toString();
        equipmentTypeT = equipmentType.getText().toString();
        equipmentSizeT = equipmentSize.getText().toString();
        equipmentIdT = equipmentId.getText().toString();
        mPositionT = mPosition.getText().toString();
        leakerT = leaker.getText().toString();
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

    private void checkPrevRes() {
        if (cc1.getText().toString().equals("NA")
                && ccc1.getText().toString().equals("NA")) {
            tt1.setVisibility(View.GONE);
        } else {
            tt1.setVisibility(View.VISIBLE);
        }
        if (cc2.getText().toString().equals("NA")
                && ccc2.getText().toString().equals("NA")) {
            tt2.setVisibility(View.GONE);
        } else {
            tt2.setVisibility(View.VISIBLE);
        }
        if (cc3.getText().toString().equals("NA")
                && ccc3.getText().toString().equals("NA")) {
            tt3.setVisibility(View.GONE);
        } else {
            tt3.setVisibility(View.VISIBLE);
        }
        if (cc4.getText().toString().equals("NA")
                && ccc4.getText().toString().equals("NA")) {
            tt4.setVisibility(View.GONE);
        } else {
            tt4.setVisibility(View.VISIBLE);
        }
        if (cc5.getText().toString().equals("NA")
                && ccc5.getText().toString().equals("NA")) {
            tt5.setVisibility(View.GONE);
        } else {
            tt5.setVisibility(View.VISIBLE);
        }
        if (cc6.getText().toString().equals("NA")
                && ccc6.getText().toString().equals("NA")) {
            tt6.setVisibility(View.GONE);
        } else {
            tt6.setVisibility(View.VISIBLE);
        }
        if (cc7.getText().toString().equals("NA")
                && ccc7.getText().toString().equals("NA")) {
            tt7.setVisibility(View.GONE);
        } else {
            tt7.setVisibility(View.VISIBLE);
        }
        if (cc8.getText().toString().equals("NA")
                && ccc8.getText().toString().equals("NA")) {
            tt8.setVisibility(View.GONE);
        } else {
            tt8.setVisibility(View.VISIBLE);
        }
        if (cc9.getText().toString().equals("NA")
                && ccc9.getText().toString().equals("NA")) {
            tt9.setVisibility(View.GONE);
        } else {
            tt9.setVisibility(View.VISIBLE);
        }
        if (cc10.getText().toString().equals("NA")
                && ccc10.getText().toString().equals("NA")) {
            tt10.setVisibility(View.GONE);
        } else {
            tt10.setVisibility(View.VISIBLE);
        }
        if (cc11.getText().toString().equals("NA")
                && ccc11.getText().toString().equals("NA")) {
            tt11.setVisibility(View.GONE);
        } else {
            tt11.setVisibility(View.VISIBLE);
        }
        if (cc12.getText().toString().equals("NA")
                && ccc12.getText().toString().equals("NA")) {
            tt12.setVisibility(View.GONE);
        } else {
            tt12.setVisibility(View.VISIBLE);
        }
    }

    private void populateSites() {
        List<String> sites = new ArrayList<String>();
        sites.add("Mina Al Ahmadi");
        sites.add("Mina Abdulla");
        sites.add("Shuaiba");

        ArrayAdapter<String> dataAdapterSite = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sites);
        dataAdapterSite
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        siteSp.setAdapter(dataAdapterSite);
    }

    private void populatePrevRes() {
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

        SQLiteDatabase db10 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/e3softdata.db", null, 0);


        if (siteSp.getSelectedItem().toString().equals("Mina Al Ahmadi")) {
            c10 = db10.rawQuery("SELECT " + "*" + " FROM maaResults res"
                    + " WHERE res.leakerID = '" + leakerT + "'", null);
        } else if (siteSp.getSelectedItem().toString().equals("Mina Abdulla")) {
            c10 = db10.rawQuery("SELECT " + "*" + " FROM mabResults res"
                    + " WHERE res.leakerID = '" + leakerT + "'", null);
        } else if (siteSp.getSelectedItem().toString().equals("Shuaiba")) {
            c10 = db10.rawQuery("SELECT " + "*" + " FROM shuResults res"
                    + " WHERE res.leakerID = '" + leakerT + "'", null);
        }
        int pro1 = c10.getColumnIndex("cycle1Result");
        int pro2 = c10.getColumnIndex("newC1Result");
        int pro3 = c10.getColumnIndex("cycle2Result");
        int pro4 = c10.getColumnIndex("newC2Result");
        int pro5 = c10.getColumnIndex("cycle3Result");
        int pro6 = c10.getColumnIndex("newC3Result");
        int pro7 = c10.getColumnIndex("cycle4Result");
        int pro8 = c10.getColumnIndex("newC4Result");
        int pro9 = c10.getColumnIndex("cycle5Result");
        int pro10 = c10.getColumnIndex("newC5Result");
        int pro11 = c10.getColumnIndex("cycle6Result");
        int pro12 = c10.getColumnIndex("newC6Result");
        int pro13 = c10.getColumnIndex("cycle7Result");
        int pro14 = c10.getColumnIndex("newC7Result");
        int pro15 = c10.getColumnIndex("cycle8Result");
        int pro16 = c10.getColumnIndex("newC8Result");
        int pro17 = c10.getColumnIndex("cycle9Result");
        int pro18 = c10.getColumnIndex("newC9Result");
        int pro19 = c10.getColumnIndex("cycle10Result");
        int pro20 = c10.getColumnIndex("newC10Result");
        int pro21 = c10.getColumnIndex("cycle11Result");
        int pro22 = c10.getColumnIndex("newC11Result");
        int pro23 = c10.getColumnIndex("cycle12Result");
        int pro24 = c10.getColumnIndex("newC12Result");

        c10.moveToLast();

        // looping through all rows and adding to list
        if (c10.getCount() > 0) {
            do {
                prod1 = c10.getString(pro1);
                data1 = data1 + prod1;
                cc1.setText(data1);

                prod2 = c10.getString(pro2);
                data2 = data2 + prod2;
                ccc1.setText(data2);

                prod3 = c10.getString(pro3);
                data3 = data3 + prod3;
                cc2.setText(data3);

                prod4 = c10.getString(pro4);
                data4 = data4 + prod4;
                ccc2.setText(data4);

                prod5 = c10.getString(pro5);
                data5 = data5 + prod5;
                cc3.setText(data5);

                prod6 = c10.getString(pro6);
                data6 = data6 + prod6;
                ccc3.setText(data6);

                prod7 = c10.getString(pro7);
                data7 = data7 + prod7;
                cc4.setText(data7);

                prod8 = c10.getString(pro8);
                data8 = data8 + prod8;
                ccc4.setText(data8);

                prod9 = c10.getString(pro9);
                data9 = data9 + prod9;
                cc5.setText(data9);

                prod10 = c10.getString(pro10);
                data10 = data10 + prod10;
                ccc5.setText(data10);

                prod11 = c10.getString(pro11);
                data11 = data11 + prod11;
                cc6.setText(data11);

                prod12 = c10.getString(pro12);
                data12 = data12 + prod12;
                ccc6.setText(data12);

                prod13 = c10.getString(pro13);
                data13 = data13 + prod13;
                cc7.setText(data13);

                prod14 = c10.getString(pro14);
                data14 = data14 + prod14;
                ccc7.setText(data14);

                prod15 = c10.getString(pro15);
                data15 = data15 + prod15;
                cc8.setText(data15);

                prod16 = c10.getString(pro16);
                data16 = data16 + prod16;
                ccc8.setText(data16);

                prod17 = c10.getString(pro17);
                data17 = data17 + prod17;
                cc9.setText(data17);

                prod18 = c10.getString(pro18);
                data18 = data18 + prod18;
                ccc9.setText(data18);

                prod19 = c10.getString(pro19);
                data19 = data19 + prod19;
                cc10.setText(data19);

                prod20 = c10.getString(pro20);
                data20 = data20 + prod20;
                ccc10.setText(data20);

                prod21 = c10.getString(pro21);
                data21 = data21 + prod21;
                cc11.setText(data21);

                prod22 = c10.getString(pro22);
                data22 = data22 + prod22;
                ccc11.setText(data22);

                prod23 = c10.getString(pro23);
                data23 = data23 + prod23;
                cc12.setText(data23);

                prod24 = c10.getString(pro24);
                data24 = data24 + prod24;
                ccc12.setText(data24);

            } while (c10.moveToNext());
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c10.close();
        db10.close();
    }

    private void populateGiData() {
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

        SQLiteDatabase db11 = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/e3softdata.db", null, 0);


        Cursor c11 = db11.rawQuery("SELECT " + "*" + " FROM generalInfo"
                + " WHERE generalInfo.leakerID = '" + leakerT + "'", null);

        int pro1 = c11.getColumnIndex("leakerID");
        int pro2 = c11.getColumnIndex("site");
        int pro3 = c11.getColumnIndex("area");
        int pro4 = c11.getColumnIndex("unit");
        int pro5 = c11.getColumnIndex("unitName");
        int pro6 = c11.getColumnIndex("drawing");
        int pro7 = c11.getColumnIndex("stream");
        int pro8 = c11.getColumnIndex("specificLoc");
        int pro9 = c11.getColumnIndex("system");
        int pro10 = c11.getColumnIndex("equipmentLoc");
        int pro11 = c11.getColumnIndex("product");
        int pro12 = c11.getColumnIndex("equipmentDesc");
        int pro13 = c11.getColumnIndex("equipmentType");
        int pro14 = c11.getColumnIndex("equipmentSize");
        int pro15 = c11.getColumnIndex("equipmentID");
        int pro16 = c11.getColumnIndex("measurementPosition");
        int pro17 = c11.getColumnIndex("Maintenance type");

        c11.moveToLast();

        // looping through all rows and adding to list
        if (c11.getCount() > 0) {
            do {

                prod1 = c11.getString(pro1);
                data1 = data1 + prod1;
                leaker.setText(data1);
                Log.d("dataLoaded", "leaker ID");

                prod2 = c11.getString(pro2);
                data2 = data2 + prod2;
                site.setText(data2);
                Log.d("dataLoaded", "site");

                prod3 = c11.getString(pro3);
                data3 = data3 + prod3;
                area.setText(data3);
                Log.d("dataLoaded", "area");

                prod4 = c11.getString(pro4);
                data4 = data4 + prod4;
                unit.setText(data4);
                Log.d("dataLoaded", "unit");

                prod5 = c11.getString(pro5);
                data5 = data5 + prod5;
                unitName.setText(data5);
                Log.d("dataLoaded", "unitName");

                prod6 = c11.getString(pro6);
                data6 = data6 + prod6;
                drawing.setText(data6);
                Log.d("dataLoaded", "drawing");

                prod7 = c11.getString(pro7);
                data7 = data7 + prod7;
                stream.setText(data7);
                Log.d("dataLoaded", "stream");

                prod8 = c11.getString(pro8);
                data8 = data8 + prod8;
                specificLoc.setText(data8);
                Log.d("dataLoaded", "specific Loc");

                prod9 = c11.getString(pro9);
                data9 = data9 + prod9;
                system.setText(data9);
                Log.d("dataLoaded", "system");

                prod10 = c11.getString(pro10);
                data10 = data10 + prod10;
                equipmentLoc.setText(data10);
                Log.d("dataLoaded", "equipment Loc");

                prod11 = c11.getString(pro11);
                data11 = data11 + prod11;
                product.setText(data11);
                Log.d("dataLoaded", "product");

                prod12 = c11.getString(pro12);
                data12 = data12 + prod12;
                equipmentDesc.setText(data12);
                Log.d("dataLoaded", "equipment Desc");

                prod13 = c11.getString(pro13);
                data13 = data13 + prod13;
                equipmentType.setText(data13);
                Log.d("dataLoaded", "equipment Type");

                prod14 = c11.getString(pro14);
                data14 = data14 + prod14;
                equipmentSize.setText(data14);
                Log.d("dataLoaded", "equipment Size");

                prod15 = c11.getString(pro15);
                data15 = data15 + prod15;
                equipmentId.setText(data15);
                Log.d("dataLoaded", "equipment ID");

                prod16 = c11.getString(pro16);
                data16 = data16 + prod16;
                mPosition.setText(data16);
                Log.d("dataLoaded", "Measurement Position");

                prod17 = c11.getString(pro17);
                data17 = data17 + prod17;
                maintenanceTypeT = data17;
                Log.d("dataLoaded", "Maintenance Type");

            } while (c11.moveToNext());
            Log.d("dataLoaded", "All GI Data Loaded");
        } else {
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c11.close();
        db11.close();

    }

    private void initialiseComponents() {
        leaker = (TextView) findViewById(R.id.lIDN);
        site = (TextView) findViewById(R.id.mSiteN);
        area = (TextView) findViewById(R.id.mAreaN);
        unit = (TextView) findViewById(R.id.mUnitN);
        unitName = (TextView) findViewById(R.id.mUnitNameN);
        drawing = (TextView) findViewById(R.id.mDrawingN);
        equipmentLoc = (TextView) findViewById(R.id.mEquipLocN);
        specificLoc = (TextView) findViewById(R.id.mSpecificLocN);
        stream = (TextView) findViewById(R.id.mStreamN);
        system = (TextView) findViewById(R.id.mSystemN);
        product = (TextView) findViewById(R.id.mProductN);
        equipmentDesc = (TextView) findViewById(R.id.mEquipDescN);
        equipmentType = (TextView) findViewById(R.id.mEquipTypeN);
        equipmentSize = (TextView) findViewById(R.id.mEquipSizeN);
        equipmentId = (TextView) findViewById(R.id.mEquipIDN);
        mPosition = (TextView) findViewById(R.id.mMPositionN);
        date = (TextView) findViewById(R.id.dateTxt2N);

        siteSp = (Spinner) findViewById(R.id.mSiteSp);

        cc1 = (TextView) findViewById(R.id.c1);
        cc2 = (TextView) findViewById(R.id.c2);
        cc3 = (TextView) findViewById(R.id.c3);
        cc4 = (TextView) findViewById(R.id.c4);
        cc5 = (TextView) findViewById(R.id.c5);
        cc6 = (TextView) findViewById(R.id.c6);
        cc7 = (TextView) findViewById(R.id.c7);
        cc8 = (TextView) findViewById(R.id.c8);
        cc9 = (TextView) findViewById(R.id.c9);
        cc10 = (TextView) findViewById(R.id.c10);
        cc11 = (TextView) findViewById(R.id.cc11R);
        cc12 = (TextView) findViewById(R.id.c12);

        ccc1 = (TextView) findViewById(R.id.cccc11);
        ccc2 = (TextView) findViewById(R.id.c21);
        ccc3 = (TextView) findViewById(R.id.c31);
        ccc4 = (TextView) findViewById(R.id.c41);
        ccc5 = (TextView) findViewById(R.id.c51);
        ccc6 = (TextView) findViewById(R.id.c61);
        ccc7 = (TextView) findViewById(R.id.c71);
        ccc8 = (TextView) findViewById(R.id.c81);
        ccc9 = (TextView) findViewById(R.id.c91);
        ccc10 = (TextView) findViewById(R.id.c101);
        ccc11 = (TextView) findViewById(R.id.c111);
        ccc12 = (TextView) findViewById(R.id.c121);

        tt1 = (TableRow) findViewById(R.id.t1);
        tt2 = (TableRow) findViewById(R.id.t2);
        tt3 = (TableRow) findViewById(R.id.t3);
        tt4 = (TableRow) findViewById(R.id.t4);
        tt5 = (TableRow) findViewById(R.id.t5);
        tt6 = (TableRow) findViewById(R.id.t6);
        tt7 = (TableRow) findViewById(R.id.t7);
        tt8 = (TableRow) findViewById(R.id.t8);
        tt9 = (TableRow) findViewById(R.id.t9);
        tt10 = (TableRow) findViewById(R.id.t10);
        tt11 = (TableRow) findViewById(R.id.t11);
        tt12 = (TableRow) findViewById(R.id.t12);

        searchEt = (EditText) findViewById(R.id.mSearchETN);
        searchBtn = (Button) findViewById(R.id.mSearchBtnN);
        updateB = (Button) findViewById(R.id.submitBtnOldN);
        clearB = (Button) findViewById(R.id.clearBtnOldN);

        currentDate = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE
                        | DateUtils.FORMAT_24HOUR);

        if (currentDate.substring(7, 11).equals("2014")
                || currentDate.substring(7, 11).equals("2015")
                || currentDate.substring(7, 11).equals("2016")
                || currentDate.substring(7, 11).equals("2017")) {

            String tmpdate = currentDate.substring(7, 11) + "-"
                    + currentDate.substring(12, 14) + "-"
                    + currentDate.substring(15, 17);
            date.setText(tmpdate);
            currentDate = date.getText().toString();
        } else {
            String tmpdate = currentDate.substring(13, 17) + "-"
                    + currentDate.substring(10, 12) + "-"
                    + currentDate.substring(7, 9);
            date.setText(tmpdate);
            currentDate = date.getText().toString();
        }

        mResult = (EditText) findViewById(R.id.measurementTxt1N);
        backGr = (EditText) findViewById(R.id.backGTxt1N);
        backGr.setText("" + 0);
        backGr.setFocusable(false);
        ppm = (RadioButton) findViewById(R.id.radioPPM1N);
        lel = (RadioButton) findViewById(R.id.radioLEL1N);
        gas = (RadioButton) findViewById(R.id.radioGAS1N);

        img = (ImageView) findViewById(R.id.imageHL);
        loadPic = (Button) findViewById(R.id.showImageBtn);

    }
}
