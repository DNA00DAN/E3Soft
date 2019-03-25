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

public class LocalRepairs extends Activity {

    private Spinner siteSp, comment,oldSp;
    private EditText searchEt;
    private Button searchBtn, updateB, clearB;
    private TextView date;
    private TextView site, siteID, drawing, contact,
            product, equipmentDesc, equipmentType,
            equipmentSize, equipmentId, mPosition, leaker;
    private TextView cc1, cc2, cc3, cc4, cc5, cc6,
            cc7, cc8, cc9, cc10, cc11, cc12;
    private TextView ccc1, ccc2, ccc3, ccc4, ccc5, ccc6,
            ccc7, ccc8, ccc9, ccc10, ccc11, ccc12;
    private TableRow tt1, tt2, tt3, tt4, tt5, tt6,
            tt7, tt8, tt9, tt10, tt11, tt12;
    private EditText mResult, backGr;
    private RadioButton ppm, lel, gas, repaired;
    private Double correctedReading1, cf;
    private ImageView img;
    private Button loadPic;
    private Cursor c10;
    private String currentDate, strCf;
    private String leakerT = "LM_4000001", reading = "PPM", prevResT, tmpSs, repT, commentT, outcomeT;
    private String busiTypeT, siteT, siteIDT, contactT, drawingT, productT, equipmentDescT, equipmentTypeT, equipmentSizeT, equipmentIdT, mPositionT;
    private String correctedReading, lossT, strRecomedation;
    private Double sv = 0.0, lRa = 0.0;
    private TableRow stationIDRow, stationContactRow;
    private String depotT, depotDrawingT;
    public static String refinery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_repairs);
        initialiseComponents();
        populateSites();
        populateGiData();
        populatePrevRes();
        populateComments();
        searchEt.setText(leakerT.substring(3));
        loadImage();

        updateB.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (LocalUserDetails.camTech.equals(LocalUserDetails.surveyTech)) {
                    noteUnchangedData();
                } else {
                    leakerT = "LM_" + searchEt.getText().toString();
                    populateVariables();
                    calculatePrevRes();
                    calcCf();
                    calcLoss();
                    calculateOutcome();
                    updateRes();
                    updateDates();
                    //updateLoss();
                    try {
                        saveFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    clearRes();
                }
            }
        });

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leakerT = "";
                leakerT = "LM_" + searchEt.getText().toString().trim();
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
                leakerT = "LM_" + searchEt.getText().toString();
                populateGiData();
                populatePrevRes();
                if (img.isShown()) {
                    loadImage();
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
                populateOldLeaks();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        oldSp.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                leakerT = oldSp.getSelectedItem().toString().substring(4);
                searchEt.setText(oldSp.getSelectedItem().toString().substring(3));
                leakerT = "LM_" + searchEt.getText().toString();
                populateGiData();
                populatePrevRes();

                if (img.isShown()) {
                    loadImage();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void populateComments() {
        CommentData db = new CommentData(getApplicationContext());

        // Spinner Drop down elements
        List<String> pComms = db.getAllComments();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterComms = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pComms);
        // Drop down layout style - list view with radio button
        dataAdapterComms
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comment.setAdapter(dataAdapterComms);
    }

    protected void calculateOutcome() {
        if (LocalUserDetails.cycleNo.equals("Cycle 1")) {
            tmpSs = cc1.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 2")) {
            tmpSs = cc2.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 3")) {
            tmpSs = cc3.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 4")) {
            tmpSs = cc4.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 5")) {
            tmpSs = cc5.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 6")) {
            tmpSs = cc6.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 7")) {
            tmpSs = cc7.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 8")) {
            tmpSs = cc8.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 9")) {
            tmpSs = cc9.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 10")) {
            tmpSs = cc10.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 11")) {
            tmpSs = cc11.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 12")) {
            tmpSs = cc12.getText().toString();
        }

        Double tmpOld = 0.0;
        if (tmpSs.equals("") || tmpSs.equals("null") || tmpSs.equals("NA")) {
            tmpSs = "0";
            tmpOld = 0.0;
        } else {
            tmpOld = Double.parseDouble(tmpSs);
        }
        commentT = comment.getSelectedItem().toString();
        Double tmpNew = Double.parseDouble(correctedReading);
        if (repaired.isChecked()) {
            repT = "Repair Attempted";
        } else {
            repT = "Repair Not Attempted";
        }
        if (repT.equals("Repair Not Attempted") || (commentT.equals("SD"))) {
            outcomeT = "Not repaired due to inaccessibility";
        } else if (tmpNew >= tmpOld) {
            outcomeT = "Tightening not successful";
        } else if (tmpNew > 10000) {
            outcomeT = "Reduced but still significant";
        } else if (tmpNew > 1000) {
            outcomeT = "Reduced but still needs attention";
        } else if (tmpNew <= 1000) {
            outcomeT = "Repaired";
        }
        Double tmpRr = Double.parseDouble(correctedReading);
        if (tmpRr < 10000) {
            commentT = "Repaired";
        }
    }

    protected void clearRes() {
        lel.setVisibility(View.VISIBLE);
        ppm.setVisibility(View.VISIBLE);
        gas.setVisibility(View.VISIBLE);
        mResult.setText("");
        populatePrevRes();
        searchEt.requestFocus();
    }

    protected void saveFile() throws IOException {
        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            String direc = "/e3softData/";
            String fileName = "fillingRepairData.csv";
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
            String data = "'" + LocalUserDetails.cycleRes + "','" + leakerT + "','" + currentDate
                    + "','" + siteT + "','" + siteIDT + "','" + drawingT + "','" + contactT
                    + "','" + productT + "','"
                    + LocalUserDetails.camTech + "','" + LocalUserDetails.camSerial + "','"
                    + LocalUserDetails.surveyTech + "','" + LocalUserDetails.surveySerial + "','"
                    + equipmentDescT + "','"
                    + equipmentTypeT + "','" + equipmentSizeT + "','" + equipmentIdT
                    + "','" + mPositionT + "','" + correctedReading + "','" + lossT + "','"
                    + strRecomedation + "','" + outcomeT + "','" + commentT + "'\n";

            bw.write(data);
            bw.flush();
            bw.close();
        } else if (siteSp.getSelectedItem().toString().equals("Depot")) {
            String direc = "/e3softData/";
            String fileName = "depotRepairData.csv";
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
            String data = "'" + LocalUserDetails.cycleRes + "','" + leakerT + "','" + currentDate
                    + "','" + depotT + "','" + depotDrawingT
                    + "','" + productT + "','"
                    + LocalUserDetails.camTech + "','" + LocalUserDetails.camSerial + "','"
                    + LocalUserDetails.surveyTech + "','" + LocalUserDetails.surveySerial + "','"
                    + equipmentDescT + "','"
                    + equipmentTypeT + "','" + equipmentSizeT + "','" + equipmentIdT
                    + "','" + mPositionT + "','" + correctedReading + "','" + lossT + "','"
                    + strRecomedation + "','" + outcomeT + "','" + commentT + "'\n";

            bw.write(data);
            bw.flush();
            bw.close();
        }
    }

    protected void updateDates() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            db.updateFSDates(leakerT, LocalUserDetails.cycleDate,
                    currentDate);
        } else if (siteSp.getSelectedItem().toString().equals("Depot")) {
            db.updateDepotDates(leakerT, LocalUserDetails.cycleDate,
                    currentDate);
        }
    }

    protected void updateLoss() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            db.updateFSLoss(leakerT, LocalUserDetails.cycleLoss,
                    lossT);
        } else if (siteSp.getSelectedItem().toString().equals("Depot")) {
            db.updateDepotLoss(leakerT, LocalUserDetails.cycleLoss,
                    lossT);
        }
    }

    protected void updateRes() {
        DataClass db = new DataClass(getApplicationContext());
        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            db.updateFSResRep(leakerT, LocalUserDetails.cycleRep,
                    correctedReading);
        } else if (siteSp.getSelectedItem().toString().equals("Depot")) {
            db.updateDepotResRep(leakerT, LocalUserDetails.cycleRep,
                    correctedReading);
        }
    }

    protected void calcLoss() {
        Double mRes = Double.parseDouble(mResult.getText().toString());
        sv = (Calculations.calculateSV(mRes,cf,reading));
        correctedReading1 = sv;
        calculateRecomedation();
        correctedReading = correctedReading1.toString();



        reading = "PPM";
        lossT = Calculations.calculateLossBySource(sv, mPositionT);

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


    protected void calcCf() {
        Log.d("product", productT);
        String data2 = "";
        String prod2 = "";

        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/e3softData/Data/flocalMarketing.sqlite", null, 0);

        Cursor c2 = db.rawQuery(
                "SELECT pr.cf FROM streams pr where pr.product = " + "'"
                        + product.getText().toString() + "'", null);
        int pro2 = c2.getColumnIndex("cf");
        c2.moveToNext();

        prod2 = c2.getString(pro2);
        data2 = data2 + prod2;
        cf = Double.parseDouble(data2);
        strCf = cf.toString();
        Log.d("cf", "++++" + strCf);


        // closing connection
        c2.close();
        db.close();
    }


    protected void calculatePrevRes() {
        if (LocalUserDetails.cycleNo.equals("Cycle 1")) {
            prevResT = "NA";
        } else if (LocalUserDetails.cycleNo.equals("Cycle 2")) {
            prevResT = cc1.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 3")) {
            prevResT = cc2.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 4")) {
            prevResT = cc3.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 5")) {
            prevResT = cc4.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 6")) {
            prevResT = cc5.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 7")) {
            prevResT = cc6.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 8")) {
            prevResT = cc7.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 9")) {
            prevResT = cc8.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 10")) {
            prevResT = cc9.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 11")) {
            prevResT = cc10.getText().toString();
        } else if (LocalUserDetails.cycleNo.equals("Cycle 12")) {
            prevResT = cc11.getText().toString();
        }
    }


    protected void populateVariables() {
        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            busiTypeT = siteSp.getSelectedItem().toString();
            siteT = site.getText().toString();
            siteIDT = siteID.getText().toString();
            contactT = contact.getText().toString();
            drawingT = drawing.getText().toString();
            productT = product.getText().toString();
            equipmentDescT = equipmentDesc.getText().toString();
            equipmentTypeT = equipmentType.getText().toString();
            equipmentSizeT = equipmentSize.getText().toString();
            equipmentIdT = equipmentId.getText().toString();
            mPositionT = mPosition.getText().toString();
            leakerT = "LM_" + searchEt.getText().toString();
        } else if (siteSp.getSelectedItem().toString().equals("Depot")) {
            busiTypeT = siteSp.getSelectedItem().toString();
            depotT = site.getText().toString();
            depotDrawingT = drawing.getText().toString();
            productT = product.getText().toString();
            equipmentDescT = equipmentDesc.getText().toString();
            equipmentTypeT = equipmentType.getText().toString();
            equipmentSizeT = equipmentSize.getText().toString();
            equipmentIdT = equipmentId.getText().toString();
            mPositionT = mPosition.getText().toString();
            leakerT = "LM_" + searchEt.getText().toString();
        }
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
                                Intent i = new Intent(LocalRepairs.this, Home.class);
                                startActivity(i);
                                finish();
                            }
                        }).setNegativeButton("NO", null).show();
    }

    protected void loadImage() {
        try {
            leakerT = "LM_" + searchEt.getText().toString();
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
            File path = new File("/sdcard/e3softData/DCIM");
            String fileNames = path + "/" + "000.png";
            Log.d("image path", fileNames);
            Bitmap mBitmap = BitmapFactory.decodeFile(fileNames);
            img.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 250, 250,
                    false));
        }
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



        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            c10 = db10.rawQuery("SELECT " + "*" + " FROM fillingResults res"
                    + " WHERE res.leakerID = '" + leakerT + "'", null);
        } else if (siteSp.getSelectedItem().toString().equals("Depot")) {
            c10 = db10.rawQuery("SELECT " + "*" + " FROM depotResults res"
                    + " WHERE res.leakerID = '" + leakerT + "'", null);
        } else if (siteSp.getSelectedItem().toString().equals("Valve Station")) {
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
        if (siteSp.getSelectedItem().toString().equals("Filling Station")) {
            populateFS();
        }
        else
        if (siteSp.getSelectedItem().toString().equals("Depot")) {
            populateDepot();
        }
        else{
            notifyWrongLeak();
        }
    }

    private void populateDepot() {
        leakerT = "LM_" + searchEt.getText().toString();
        stationIDRow.setVisibility(View.GONE);
        stationContactRow.setVisibility(View.GONE);
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

        Cursor c11 = db11.rawQuery("SELECT " + "*" + " FROM depotGeneralInfo"
                + " WHERE depotGeneralInfo.leakerID = '" + leakerT + "'", null);

        int pro1 = c11.getColumnIndex("leakerID");
        int pro2 = c11.getColumnIndex("siteName");
        int pro6 = c11.getColumnIndex("drawing");
        int pro11 = c11.getColumnIndex("product");
        int pro12 = c11.getColumnIndex("equipmentDescription");
        int pro13 = c11.getColumnIndex("equipmentType");
        int pro14 = c11.getColumnIndex("equipmentSize");
        int pro15 = c11.getColumnIndex("equipmentID");
        int pro16 = c11.getColumnIndex("measurementPosition");
        int pro17 = c11.getColumnIndex("Type");

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


                prod6 = c11.getString(pro6);
                data6 = data6 + prod6;
                drawing.setText(data6);
                Log.d("dataLoaded", "drawing");

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





            } while (c11.moveToNext());
            Log.d("dataLoaded", "All GI Data Loaded");
        } else {
            notifyWrongLeak();
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c11.close();
        db11.close();
    }

    public void populateFS() {
        stationIDRow.setVisibility(View.VISIBLE);
        stationContactRow.setVisibility(View.VISIBLE);
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

        Cursor c11 = db11.rawQuery("SELECT " + "*" + " FROM fillingGeneralInfo"
                + " WHERE fillingGeneralInfo.leakerID = '" + leakerT + "'", null);

        int pro1 = c11.getColumnIndex("leakerID");
        int pro2 = c11.getColumnIndex("site");
        int pro3 = c11.getColumnIndex("siteID");
        int pro4 = c11.getColumnIndex("contact");
        int pro6 = c11.getColumnIndex("drawing");
        int pro11 = c11.getColumnIndex("product");
        int pro12 = c11.getColumnIndex("equipmentDescription");
        int pro13 = c11.getColumnIndex("equipmentType");
        int pro14 = c11.getColumnIndex("equipmentSize");
        int pro15 = c11.getColumnIndex("equipmentID");
        int pro16 = c11.getColumnIndex("measurementPosition");
        int pro17 = c11.getColumnIndex("Type");

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
                siteID.setText(data3);
                Log.d("dataLoaded", "site ID");

                prod6 = c11.getString(pro6);
                data6 = data6 + prod6;
                drawing.setText(data6);
                Log.d("dataLoaded", "drawing");

                prod7 = c11.getString(pro4);
                data7 = data7 + prod7;
                contact.setText(data7);
                Log.d("dataLoaded", "Contact");

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




            } while (c11.moveToNext());
            Log.d("dataLoaded", "All GI Data Loaded");
        } else {
            notifyWrongLeak();
            Log.d("Empty Report", data1 + "EMPTY");
        }
        // closing connection
        c11.close();
        db11.close();
    }

    public void notifyWrongLeak() {
        new AlertDialog.Builder(this)
                .setTitle("DATA ERROR")
                .setMessage("Leak Captured in different Unit" + "\n"+
                        "Enter a valid Leak ID")
                .setCancelable(false)
                .setNegativeButton("OK", null).show();
    }


    private void populateSites() {
        List<String> sites = new ArrayList<String>();
        sites.add("Filling Station");
        sites.add("Depot");

        ArrayAdapter<String> dataAdapterSite = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sites);
        dataAdapterSite
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        siteSp.setAdapter(dataAdapterSite);

    }

    private void initialiseComponents() {
        leaker = (TextView) findViewById(R.id.lIDN);
        site = (TextView) findViewById(R.id.mSiteN);
        siteID = (TextView) findViewById(R.id.SiteIDN);
        drawing = (TextView) findViewById(R.id.mDrawingN);
        contact = (TextView) findViewById(R.id.mContactN);

        product = (TextView) findViewById(R.id.mProductN);
        equipmentDesc = (TextView) findViewById(R.id.mEquipDescN);
        equipmentType = (TextView) findViewById(R.id.mEquipTypeN);
        equipmentSize = (TextView) findViewById(R.id.mEquipSizeN);
        equipmentId = (TextView) findViewById(R.id.mEquipIDN);
        mPosition = (TextView) findViewById(R.id.mMPositionN);
        date = (TextView) findViewById(R.id.dateTxt2N);

        siteSp = (Spinner) findViewById(R.id.mSiteSpR);
        oldSp = (Spinner) findViewById(R.id.oldLeaksLMSpR);
        comment = (Spinner) findViewById(R.id.commentComboRep);
        repaired = (RadioButton) findViewById(R.id.radioRepairRep);

        stationIDRow = (TableRow) findViewById(R.id.mSiteIDN);
        stationContactRow = (TableRow) findViewById(R.id.stationCon);

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

        String currentDate1 = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR);

        String currentDate2 = DateUtils.formatDateTime(getBaseContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NUMERIC_DATE);

        currentDate = currentDate1.substring(8) + "-" + currentDate2.substring(3) + "-" + currentDate2.substring(0, 2);
        Log.d("Date 1",currentDate.substring(5,6));
        Log.d("Date 2",currentDate.substring(8,9));
        if(!currentDate.substring(5,6).equals("-") || !currentDate.substring(8,9).equals("-")){
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
//                String tmpdate = currentDate.substring(13, 17) + "-"
//                        + currentDate.substring(10, 12) + "-"
//                        + currentDate.substring(7, 9);
                date.setText(currentDate);
                currentDate = date.getText().toString();
            }
        }
        date.setText(currentDate);

        mResult = (EditText) findViewById(R.id.measurementTxt1N);
        backGr = (EditText) findViewById(R.id.backGTxt1N);
        backGr.setText("" + 0);
        backGr.setFocusable(false);
        ppm = (RadioButton) findViewById(R.id.radioPPM1N);
        lel = (RadioButton) findViewById(R.id.radioLEL1N);
        gas = (RadioButton) findViewById(R.id.radioGAS1N);

        img = (ImageView) findViewById(R.id.imageHL);
        loadPic = (Button) findViewById(R.id.showImageBtn);
        loadPic.setText("Show Image");
        img.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.local_old_leaks, menu);
        return true;
    }

    private void populateOldLeaks() {
        refinery = siteSp.getSelectedItem().toString();
        List<String> pUnit;
        DataClass db = new DataClass(getApplicationContext());
        if(siteSp.getSelectedItem().toString().equals("Filling Station")) {
            pUnit = db.viewStations();
        }else{
            pUnit = db.viewDepots();
        }
        // Spinner Drop down elements
        try {
            ArrayAdapter<String> dataAdapterUnit = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_dropdown_item, pUnit);
            dataAdapterUnit
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            oldSp.setAdapter(dataAdapterUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
