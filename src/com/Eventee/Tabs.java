package com.Eventee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Corresponds to Main.xml
 * 
 * @author Gregory
 *
 */
public class Tabs extends Activity
{
	Button exchange; 
	int numContacts; //Number of contacts (might be useful later, for expanadblelist, or scrollview)
	String myString = null;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost appTabs = (TabHost) findViewById (R.id.tabhost);
        
        //Exchange button
        exchange = (Button) findViewById(R.id.Button_Exchange);
        exchange.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View view) 
			{
				Intent myIntent = new Intent(view.getContext(), BeamActivity.class);

                startActivityForResult(myIntent, 0);
			}
		});
        
        appTabs.setup();
        //tab1: Exchange
        TabSpec specs = appTabs.newTabSpec("tag1");
        specs.setContent(R.id.Exchange);
        specs.setIndicator("Exchange");
        appTabs.addTab(specs);
        //tab2: Contacts
        specs = appTabs.newTabSpec("tag2");
        specs.setContent(R.id.Contacts);
        specs.setIndicator("Contacts");
        appTabs.addTab(specs);
        //tab3: Me(profile)
        specs = appTabs.newTabSpec("tag3");
        specs.setContent(R.id.Me);
        specs.setIndicator("Me");
        appTabs.addTab(specs);
 
    }

}