package com.example.e3soft;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class Splash extends Activity {

    private static int timeOut = 5000;
    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"};

        int permsRequestCode = 200;

        if (shouldAskPermission()) {
            if (hasPermission(perms[0]) && hasPermission(perms[1])) {

            } else {
                if (!hasPermission(perms[0]) && !hasPermission(perms[1])) {
                    requestPermissions(perms, permsRequestCode);
                }
            }
        }

        File root = new File(Environment
                .getExternalStorageDirectory()
                + "/e3softData/");
        Log.d("path", root.toString());
        root.mkdir();

        File dataF = new File(Environment
                .getExternalStorageDirectory()
                + "/e3softData/Data/");
        dataF.mkdir();

        File mediaF = new File(Environment
                .getExternalStorageDirectory()
                + "/e3softData/DCIM/");
        mediaF.mkdir();

        if (root.exists()) {
            // copy files from assets to sdcard
            File image = new File(Environment
                    .getExternalStorageDirectory()
                    + "/e3softData/DCIM/000.png");

            if (!image.exists()) {
                importDataFiles(dataF, mediaF);
            } else {
                Log.d("DB Status", "Database components already exist");
            }
        } else {
            System.err.println("DB Status:  No Directories found");
        }

        splash = (ImageView) findViewById(R.id.splashScrn);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent in = new Intent(Splash.this, Home.class);
                startActivity(in);
                finish();
            }
        }, timeOut);
    }

    private void importDataFiles(File dataF, File mediaF) {
        try {
            AppUtils.AssetFileCopy(this, mediaF + "/000.png", "000.png", false);
            AppUtils.AssetFileCopy(this, dataF + "/comments.db", "comments.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/dataTypes.db", "dataTypes.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/depotReports.db", "depotReports.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/dHighLeaker.db", "dHighLeaker.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/fHighLeaker.db", "fHighLeaker.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/vHighLeaker.db", "vHighLeaker.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/e3softdata.db", "e3softdata.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/enserve3DatabaseV1.0", "enserve3DatabaseV1.0", false);
            AppUtils.AssetFileCopy(this, dataF + "/fillingReports.db", "fillingReports.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/flocalMarketing.sqlite", "flocalMarketing.sqlite", false);
            AppUtils.AssetFileCopy(this, dataF + "/highLeaker.db", "highLeaker.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/leaker.txt", "leaker.txt", false);
            AppUtils.AssetFileCopy(this, dataF + "/localFilling.txt", "localFilling.txt", false);
            AppUtils.AssetFileCopy(this, dataF + "/newPros.db", "newPros.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/newStreams.db", "newStreams.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/oldLeakers.db", "oldLeakers.db", false);
            AppUtils.AssetFileCopy(this, dataF + "/valveReports.db", "valveReports.db", false);

            Log.d("DB Status", "Database components copied to sd card");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                break;

        }

    }

    private boolean hasPermission(String permission) {

        if (canMakeSnores()) {

            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }

    private boolean canMakeSnores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean shouldAskPermission() {

        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

    }
}
