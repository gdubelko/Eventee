package com.Eventee;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.nio.charset.Charset;


public class BeamActivity extends Activity implements CreateNdefMessageCallback 
{
    NfcAdapter mNfcAdapter;
    String otherString = null;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beamview);
        
        Button next = (Button) findViewById(R.id.Button_Ok);
        next.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

        });
        
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) 
        {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    public NdefMessage createNdefMessage(NfcEvent event) 
    {
        String text = ("Beam me up, Android!\n\n" +"Beam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(new NdefRecord[] 
        { 
        		createMimeRecord("application/com.example.android.beam", text.getBytes())
         /**
          * The Android Application Record (AAR) is commented out. When a device
          * receives a push with an AAR in it, the application specified in the AAR
          * is guaranteed to run. The AAR overrides the tag dispatch system.
          * You can add it back in to guarantee that this
          * activity starts when receiving a beamed message. For now, this code
          * uses the tag dispatch system.
          */
          //,NdefRecord.createApplicationRecord("com.example.android.beam")
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
        otherString = new String(msg.getRecords()[0].getPayload());
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
}