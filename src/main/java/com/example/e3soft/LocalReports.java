package com.example.e3soft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LocalReports extends Activity {

    private Button fReps, dReps, vReps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_reports);

        fReps = (Button) findViewById(R.id.fil);
        dReps = (Button) findViewById(R.id.dep);
        vReps = (Button) findViewById(R.id.val);

        fReps.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent startNewActivityOpen = new Intent(LocalReports.this,
                        Freport.class);

                startActivity(startNewActivityOpen);
                finish();

            }
        });

        vReps.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent startNewActivityOpen = new Intent(LocalReports.this,
                        Vreport.class);

                startActivity(startNewActivityOpen);
                finish();

            }
        });

        dReps.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startNewActivityOpen = new Intent(LocalReports.this,
                        Dreport.class);

                startActivity(startNewActivityOpen);
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.local_reports, menu);
        return true;
    }

}
