package project.Eventee;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class ContactsActivity extends Activity {


public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Resources res = getResources(); 
    TabHost tabHost = getTabHost();  
    TabHost.TabSpec spec;  
    Intent intent;  

    intent = new Intent().setClass(this, SeekActivity.class);

    spec = tabHost.newTabSpec("seek").setIndicator("Seek", res.getDrawable(R.drawable.ic_launcher)).setContent(intent);
    tabHost.addTab(spec);

    intent = new Intent().setClass(this, ContactsActivity.class);
    spec = tabHost.newTabSpec("contacts").setIndicator("Contacts", res.getDrawable(R.drawable.ic_launcher)).setContent(intent);
    tabHost.addTab(spec);

    intent = new Intent().setClass(this, MeActivity.class);
    spec = tabHost.newTabSpec("me").setIndicator("Me", res.getDrawable(R.drawable.ic_launcher)).setContent(intent);
    tabHost.addTab(spec);

    tabHost.setCurrentTab(2);
}

private TabHost getTabHost() {
	// TODO Auto-generated method stub
	return null;
}
}