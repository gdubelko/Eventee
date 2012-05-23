package com.Eventee;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
/**
 * CSE694 Project
 * @authors Greg Dubelko, Eric Gottschalk, Jason Monroe, Todd Simmons
 * An application that uses NFC to exchange business information
 */
public class Tabs extends ListActivity
{
    private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Contact> m_contacts = null;
    private ContactAdapter m_adapter;
    private Runnable viewContacts;

    Button exchange, saveMe;
    EditText name, network, email, phone;
    String myString;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);// displays main.xml

        TabHost appTabs = (TabHost) findViewById (R.id.tabhost); //tabs container

        //EditTexts
        name = (EditText) findViewById(R.id.Input_Name);
        network = (EditText) findViewById(R.id.Input_Network);
        email = (EditText) findViewById(R.id.Input_Email);
        phone = (EditText) findViewById(R.id.Input_PhoneNumber);

        //BUTTON(Exchange)::Runs Beam processes::
        exchange = (Button) findViewById(R.id.Button_Exchange);
        exchange.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(view.getContext(), BeamActivity.class);
                myIntent.putExtra("myString", myString);
                startActivityForResult(myIntent, 0);
            }
        });
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //BUTTON(SaveMe)::Saves users information into one string::
        saveMe = (Button) findViewById(R.id.Button_SaveMe);
        saveMe.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                myString = name.getText() + "^" + network.getText() + "^" + email.getText() + "^" + phone.getText();
                Toast.makeText(getApplicationContext(), myString, Toast.LENGTH_LONG).show();  //Simple test----Remove Later..maybe
            }
        });
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //TABS::::::::::::::::::::::::::::::
        appTabs.setup();
        //tab1: Exchange
        TabSpec specs = appTabs.newTabSpec("tag1");
        specs.setContent(R.id.Exchange);
        specs.setIndicator("Exchange");
        appTabs.addTab(specs);
        //tab2: Contacts
        specs = appTabs.newTabSpec("tag2");
        specs.setContent(R.id.Output_Contacts);
        specs.setIndicator("Contacts");
        appTabs.addTab(specs);
        //tab3: Me(profile)
        specs = appTabs.newTabSpec("tag3");
        specs.setContent(R.id.Me);
        specs.setIndicator("Me");
        appTabs.addTab(specs);
        //::::::::::::::::::::::::::::::::::::
        //::Tokenizer In Progress::::::::::::::::::
        //::::::::::::::::::::::::::::::::::::
        StringTokenizer st = new StringTokenizer(BeamActivity.otherString, "^"); 
        /**
        while(st.hasMoreTokens()) 
        { 
        String val = st.nextToken(); 
        } 
        **/
        //::::::::::::::::::::::::::::::::::::
        //::Internal Storage Writer:::::::::::
        //::::::::::::::::::::::::::::::::::::
        String FILENAME = "contactlist_file";
        try
        {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            while(st.hasMoreTokens()) 
            {
            	fos.write(st.nextToken().getBytes());
            	 
            }    
            //fos.write(BeamActivity.otherString.getBytes());
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //:::::::::::::::::::::::::::::::::::
        //::Contacts List::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::
        m_contacts = new ArrayList<Contact>();
        this.m_adapter = new ContactAdapter(this, R.layout.row, m_contacts);
        setListAdapter(this.m_adapter);
        viewContacts = new Runnable()
        {
            public void run()
            {
                getContacts();
            }
        };
        Thread thread =  new Thread(null, viewContacts, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(Tabs.this, "Please wait...", "Retrieving data ...", true);
    }
    private Runnable returnRes = new Runnable()
    {
        public void run()
        {
            if(m_contacts != null && m_contacts.size() > 0)
            {
                m_adapter.notifyDataSetChanged();
                for(int i=0; i<m_contacts.size(); i++) m_adapter.add(m_contacts.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    private void getContacts()
    {
        try
        {
        	//File reader and string splitter would go here*****
            m_contacts = new ArrayList<Contact>();
            
            Contact c = new Contact();
            c.setContactName(BeamActivity.otherString);
            c.setContactDetails("OSU Greg.dubelko@gmail.com 440-7732824");

            m_contacts.add(c);
            
            Contact c2 = new Contact();
            c2.setContactName("Seth MacFarlene");
            c2.setContactDetails("Family Guy SFarlene@gmail.com 215-883-8829");

            m_contacts.add(c2);

            Thread.sleep(5000);
            Log.i("ARRAY", ""+ m_contacts.size());
            /**
        	FileInputStream fis = openFileInput("contactlist_file");
        	m_contacts = new ArrayList<Contact>();
        	while(fis.hasNextLine())
        	{
        	    Contact c = new Contact();
        	    c.setContactName(BeamActivity.otherString);
        	    c.setContactDetails("OSU Greg.dubelko@gmail.com 440-7732824");

        	    m_contacts.add(c);
        	}
        	**/
        }
        catch (Exception e)
        {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        runOnUiThread(returnRes);
    }
    private class ContactAdapter extends ArrayAdapter<Contact>
    {
        private ArrayList<Contact> items;

        public ContactAdapter(Context context, int textViewResourceId, ArrayList<Contact> items)
        {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup group)
        {
            View v = convertView;
            if (v == null)
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            Contact c = items.get(index);
            if (c != null)
            {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                if (tt != null)
                {
                    tt.setText(c.getContactName());
                }
                if(bt != null)
                {
                    bt.setText(c.getContactDetails());
                }
            }
            return v;
        }
    }
}

