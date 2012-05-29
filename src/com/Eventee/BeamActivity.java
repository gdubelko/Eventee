package com.Eventee;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;

public class BeamActivity extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback
{
    NfcAdapter mNfcAdapter;
    static String otherString  = "Greg Dubelko^OSU^greg.dubelko@gmail.com^440-773-2824";
    String myString;
    private static final int MESSAGE_SENT = 1;
    TextView stringTest;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beamview);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null)
        {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback to set NDEF Message and Register callback to listen for message-sent success
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

    
    }//End of onCreate()

    public NdefMessage createNdefMessage(NfcEvent event)
    {
        NdefMessage msg = new NdefMessage(new NdefRecord[]
        {
            createMimeRecord("application/com.Eventee", myString.getBytes())
            /**
             * The Android Application Record (AAR) is commented out. When a device
             * receives a push with an AAR in it, the application specified in the AAR
             * is guaranteed to run. The AAR overrides the tag dispatch system.
             * You can add it back in to guarantee that this
             * activity starts when receiving a beamed message. For now, this code
             * uses the tag dispatch system.
             */
            //,NdefRecord.createApplicationRecord("com.Eventee.BeamActivity")
        });
        return msg;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()))
        {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and sets value to otherString variable
     */
    void processIntent(Intent intent)
    {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // record 0 contains the MIME type, record 1 is the AAR, if present
        String contactDetails = new String(msg.getRecords()[0].getPayload());
        Contact newContact;
        try {
			newContact = new Contact(contactDetails);
			Tabs.m_contacts.add(newContact);
			Tabs.m_adapter.add(newContact);
	        Tabs.m_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e("66", "malformed or incorrect string");
			//don't add it to the listview, but show an error state contact.
			newContact = new Contact();
		} 
		stringTest.setText(newContact.getContactName() + "\n" + newContact.getContactDetails());
    }

    /**
     * Creates a custom MIME type encapsulated in an NDEF record
     */
    public NdefRecord createMimeRecord(String mimeType, byte[] payload)
    {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }
    public void onNdefPushComplete(NfcEvent event)
    {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();

    }
    /** This handler receives a message from onNdefPushComplete */
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case MESSAGE_SENT:
                Toast.makeText(getApplicationContext(), "Contact sent!", Toast.LENGTH_LONG).show();
                break;
            }
        }
    };
}