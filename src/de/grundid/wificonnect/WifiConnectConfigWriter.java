package de.grundid.wificonnect;

import org.nfctools.ndef.Ndef;
import org.nfctools.ndef.mime.TextMimeRecord;
import org.nfctools.ndef.wkt.records.Record;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class WifiConnectConfigWriter extends Activity {

	private NfcAdapter nfcAdapter;
	private SimpleWifiInfo simpleWifiInfo;
	private PendingIntent pendingIntent;
	private String[][] techListsArrays;
	private IntentFilter[] intentFiltersArrays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_tag);

		Button cancel = (Button)findViewById(R.id.cancelButton);
		cancel.setOnClickListener(new SimpleActivityFinishHandler(this));

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		createForegroundDispatchFilters();
	}

	@Override
	protected void onResume() {
		super.onResume();
		simpleWifiInfo = getIntent().getParcelableExtra("SIMPLE_WIFI_INFO");
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArrays, techListsArrays);
	}

	protected void createForegroundDispatchFilters() {
		pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		intentFiltersArrays = new IntentFilter[] { tech };

		techListsArrays = new String[][] { new String[] { android.nfc.tech.Ndef.class.getName() }, };
	}

	@Override
	protected void onPause() {
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tagFromIntent != null && simpleWifiInfo != null) {
			String content = SimpleWifiInfoConverter.toString(simpleWifiInfo);
			TextMimeRecord record = new TextMimeRecord("text/x-wifi", content);
			writeNdefTag(tagFromIntent, record);
			finish();
		}
	}

	public void writeNdefTag(Tag tag, Record record) {
		try {
			byte[] recordData = Ndef.getNdefMessageEncoder().encodeSingle(record);

			NdefMessage message = new NdefMessage(recordData);
			NdefFormatable ndefFormatable = NdefFormatable.get(tag);
			if (ndefFormatable != null) {
				ndefFormatable.connect();
				ndefFormatable.format(message);
				Toast.makeText(this, R.string.nfc_write_ok, Toast.LENGTH_SHORT).show();
			}
			else {
				android.nfc.tech.Ndef ndef = android.nfc.tech.Ndef.get(tag);
				if (ndef != null) {
					ndef.connect();

					if (recordData.length < ndef.getMaxSize()) {
						ndef.writeNdefMessage(message);
						Toast.makeText(this, R.string.nfc_write_ok, Toast.LENGTH_SHORT).show();
					}
					else
						Toast.makeText(this, R.string.nfc_not_enough_space, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(this, R.string.nfc_write_not_ok, Toast.LENGTH_SHORT).show();
				}
			}
		}
		catch (Exception e) {
			DialogUtils.buildAndShowInformationDialog(this, R.string.nfc_write_not_ok, new SimpleActivityFinishHandler(
					this));
		}
	}
}
