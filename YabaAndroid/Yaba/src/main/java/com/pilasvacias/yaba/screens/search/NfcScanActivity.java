package com.pilasvacias.yaba.screens.search;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.util.WToast;

import butterknife.InjectView;
import butterknife.Views;

import static android.nfc.NfcAdapter.CreateNdefMessageCallback;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class NfcScanActivity extends Activity implements CreateNdefMessageCallback {

    // Inject views
    @InjectView(R.id.nfcScan_editText)
    EditText mEditText;
    @InjectView(R.id.nfcScan_textView)
    TextView mTextView;
    // Fields
    private NfcAdapter mNfcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_scan_activity);
        Views.inject(this);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            WToast.showLong(this, R.string.nfc_unavailable);
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("Beam text: " + mEditText.getText().toString() + "\n\nBeam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{
                        NdefRecord.createMime(getString(R.string.nfc_mime_type), text.getBytes())
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
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMessages[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        mTextView.setText(new String(msg.getRecords()[0].getPayload()));
    }
}
