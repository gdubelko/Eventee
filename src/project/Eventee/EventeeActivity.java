package project.Eventee;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventeeActivity extends Activity 
{
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	setContentView(R.layout.seek);
            }
        });
        
     }
    public void onStart()
    {
    	super.onStart();
    }
    
    public void onStop()
    {
    	super.onStop();
    }
}