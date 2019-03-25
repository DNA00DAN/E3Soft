package com.example.e3soft;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class High_Leaker extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high__leaker);
        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, Historical_Data.class);

        spec = tabHost.newTabSpec("Historical Data")
                .setIndicator("Historical Data").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Maintenance.class);
        spec = tabHost.newTabSpec("Maintenance").setIndicator("Maintenance")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

}
