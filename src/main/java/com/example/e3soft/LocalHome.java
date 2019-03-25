package com.example.e3soft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LocalHome extends Activity {

    private Button survey, logout, highLeaker, local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_home);

        survey = (Button) findViewById(R.id.surveyBtn2);
        highLeaker = (Button) findViewById(R.id.highLeakerBtn2);
        local = (Button) findViewById(R.id.localBtn2);
        logout = (Button) findViewById(R.id.logoutBtn2);

        survey.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(LocalHome.this,
                        LocalMarketing.class);

                startActivity(startNewAtivityOpen);
                finish();

            }
        });

        local.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                finish();

            }
        });

        highLeaker.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(LocalHome.this,
                        LocalHighLeaker.class);

                startActivity(startNewAtivityOpen);
                finish();

            }
        });

        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(LocalHome.this,
                        LocalReports.class);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.local_home, menu);
        return true;
    }

}
