package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;

public class Home extends Activity {

    private Button survey, highLeaker, reports;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        survey = (Button) findViewById(R.id.surveyBtn);
        highLeaker = (Button) findViewById(R.id.highLeakerBtn);
        reports = (Button) findViewById(R.id.reportBtn);


        survey.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Home.this,
                        userDetails.class);

                startActivity(startNewAtivityOpen);
                finish();

            }
        });

        highLeaker.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Home.this,
                        LocalUserDetails.class);

                startActivity(startNewAtivityOpen);
                finish();

            }
        });

        reports.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Home.this,
                        NewReports.class);

                startActivity(startNewAtivityOpen);
                finish();

            }
        });
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
