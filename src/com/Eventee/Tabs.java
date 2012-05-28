package com.Eventee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
/**
 * CSE694 Project
 * @authors Greg Dubelko, Eric Gottschalk, Jason Monroe, Todd Simmons
 * An application that uses NFC to exchange business information
 */
public class Tabs extends ListActivity
{
    private ProgressDialog m_ProgressDialog = null;
    protected static ArrayList<Contact> m_contacts = null;
    protected static ContactAdapter m_adapter;
    private Runnable viewContacts;

    Button saveThem, saveMe;
    EditText name, network, email, phone;
    String myString;
    String otherString = "Greg Dubelko^OSU^greg.dubelko@gmail.com^440-773-2824";
    
    HashMap<String, String> contactMap = new HashMap<String, String>();
    

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);// displays main.xml
        m_contacts = new ArrayList<Contact>();
        TabHost appTabs = (TabHost) findViewById (R.id.tabhost); //tabs container

        //EditTexts
        name = (EditText) findViewById(R.id.Input_Name);
        network = (EditText) findViewById(R.id.Input_Network);
        email = (EditText) findViewById(R.id.Input_Email);
        phone = (EditText) findViewById(R.id.Input_PhoneNumber);

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //BUTTON(SaveMe)::Saves users information into one string::
        saveMe = (Button) findViewById(R.id.Button_SaveMe);
        saveMe.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                myString = name.getText() + "^" + network.getText() + "^" + email.getText() + "^" + phone.getText();
                Toast.makeText(getApplicationContext(), myString, Toast.LENGTH_LONG).show();  //Simple test----Remove Later..maybe
                contactMap.put(myString, "itWorked");
                Contact myself = new Contact();
                myself.setContactName(name.getText()+"");
                myself.setContactDetails(network.getText()+"");
                m_contacts.add(myself);
                m_adapter.add(myself);
                m_adapter.notifyDataSetChanged();
            }
        });
        saveThem = (Button) findViewById(R.id.Button_SaveThem);
        saveThem.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
            	Contact them = new Contact();
            	String [] temp = otherString.split("^");
    			if (temp.length >1)
    			{
    				them.setContactName(temp[0]);
                    them.setContactDetails( temp[1]+"/"+temp[2]+"/"+temp[3]);
    			}
				m_contacts.add(them);
                m_adapter.add(them);
                m_adapter.notifyDataSetChanged();
				
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
        
        
        m_contacts = new ArrayList<Contact>();
        Tabs.m_adapter = new ContactAdapter(this, R.layout.row, m_contacts);
        setListAdapter(Tabs.m_adapter);
        
        appTabs.setOnTabChangedListener(new OnTabChangeListener() 
        {
        	public void onTabChanged(String tabId) 
        	{
        		m_adapter.notifyDataSetChanged();
        	}
        	
        });
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
     
    }//onCreate() end
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
            
            Set<String> keys = contactMap.keySet();
    		Iterator<String> it = keys.iterator();
    		while(it.hasNext())
    		{
    			String key = it.next();
    			Contact c1 = new Contact();
                c1.setContactName(key);
                c1.setContactDetails(contactMap.get(key));

                m_contacts.add(c1);
    		}
            

            Thread.sleep(1000);
            Log.i("ARRAY", ""+ m_contacts.size());
            
        }
        catch (Exception e)
        {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        runOnUiThread(returnRes);
    }
    protected class ContactAdapter extends ArrayAdapter<Contact>
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

