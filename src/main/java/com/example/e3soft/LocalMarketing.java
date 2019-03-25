package com.example.e3soft;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class LocalMarketing extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_marketing);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, LOperator.class);
        spec = tabHost.newTabSpec("Operator").setIndicator("Operator")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Local_Marketing.class);
        spec = tabHost.newTabSpec("Site").setIndicator("Site")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, FInspection.class);
        spec = tabHost.newTabSpec("Survey").setIndicator("Survey")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

        tabHost.getTabWidget().getChildTabViewAt(0).setEnabled(false);
        tabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);
        tabHost.getTabWidget().getChildTabViewAt(2).setEnabled(false);

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
