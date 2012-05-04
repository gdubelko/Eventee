package project.Eventee;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class SeekActivity extends Activity {


public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Resources res = getResources(); // Resource object to get Drawables
    TabHost tabHost = getTabHost();  // The activity TabHost
    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
    Intent intent;  // Reusable Intent for each tab

    // Create an Intent to launch an Activity for the tab (to be reused)
    intent = new Intent().setClass(this, SeekActivity.class);

    // Initialize a TabSpec for each tab and add it to the TabHost
    spec = tabHost.newTabSpec("seek").setIndicator("Seek",
                      res.getDrawable(R.drawable.ic_launcher))
                  .setContent(intent);
    tabHost.addTab(spec);

    // Do the same for the other tabs
    intent = new Intent().setClass(this, ContactsActivity.class);
    spec = tabHost.newTabSpec("contacts").setIndicator("Contacts",
                      res.getDrawable(R.drawable.ic_launcher))
                  .setContent(intent);
    tabHost.addTab(spec);

    intent = new Intent().setClass(this, MeActivity.class);
    spec = tabHost.newTabSpec("me").setIndicator("Me",
                      res.getDrawable(R.drawable.ic_launcher))
                  .setContent(intent);
    tabHost.addTab(spec);

    tabHost.setCurrentTab(2);
}

private TabHost getTabHost() {
	// TODO Auto-generated method stub
	return null;
}
}