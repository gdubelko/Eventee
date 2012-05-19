package com.Eventee;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tabs extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost appTabs = (TabHost) findViewById (R.id.tabhost);
        appTabs.setup();
        
        TabSpec specs = appTabs.newTabSpec("tag1");
        specs.setContent(R.id.Exchange);
        specs.setIndicator("Exchange");
        appTabs.addTab(specs);
        
        specs = appTabs.newTabSpec("tag2");
        specs.setContent(R.id.Contacts);
        specs.setIndicator("Contacts");
        appTabs.addTab(specs);
        
        specs = appTabs.newTabSpec("tag3");
        specs.setContent(R.id.Me);
        specs.setIndicator("Me");
        appTabs.addTab(specs);
 
    }
}

