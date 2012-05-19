package project.Eventee;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventeeActivity extends Activity 
{
    static final private int LOAD_ACCOUNT = 0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
     }
    
    public void buttonClickHandler(View target)
    {
    	Intent appProcess = new Intent(this, project.Eventee.SeekActivity.class);
    	startActivityForResult(appProcess, LOAD_ACCOUNT);
    	
    }
   
}