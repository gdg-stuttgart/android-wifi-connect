package de.grundid.wificonnect;

import java.util.List;

import org.nfctools.ndef.Ndef;
import org.nfctools.ndef.RecordUtils;
import org.nfctools.ndef.mime.TextMimeRecord;
import org.nfctools.ndef.wkt.records.GenericControlRecord;
import org.nfctools.ndef.wkt.records.Record;
import org.nfctools.ndef.wkt.records.TextRecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class WifiConnectHandler extends Activity {

	private void handleIntent() {
		try {
			NdefMessage[] ndefMessages = getNdefMessages(getIntent());
			for (NdefMessage ndefMessage : ndefMessages) {

				List<Record> records = Ndef.getNdefMessageDecoder().decodeToRecords(ndefMessage.toByteArray());
				for (Record record : records) {

					if (record instanceof GenericControlRecord) {
						GenericControlRecord gcRecord = (GenericControlRecord)record;
						if (RecordUtils.equalsTarget(gcRecord, "WifiManager")) {
							TextRecord ssid = RecordUtils.getRecordByKey(gcRecord.getData(), "S");
							TextRecord type = RecordUtils.getRecordByKey(gcRecord.getData(), "T");
							TextRecord key = RecordUtils.getRecordByKey(gcRecord.getData(), "P");
							SimpleWifiInfo wifiInfo = new SimpleWifiInfo(type.getText(), ssid.getText(), key.getText());
							setNewWifi(wifiInfo);
						}
					}
					else if (record instanceof TextMimeRecord) {

						TextMimeRecord tmRecord = (TextMimeRecord)record;
						String contentType = tmRecord.getContentType();
						if ("text/x-wifi".equalsIgnoreCase(contentType)) {
							setNewWifi(SimpleWifiInfoConverter.fromString(tmRecord.getContent()));
						}
					}
				}
			}
		}
		catch (FormatException e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		handleIntent();
		finish();
	}

	private NdefMessage[] getNdefMessages(Intent intent) throws FormatException {
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage)rawMsgs[i];
				}
			}
			else {
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}
		}
		else {
			Log.e("WifiConnect", "Unknown intent " + intent);
		}
		return msgs;
	}

	protected void setNewWifi(SimpleWifiInfo wifiInfo) {
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);

		boolean foundAKnownNetwork = false;
		List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration wifiConfiguration : configuredNetworks) {
			if (wifiConfiguration.SSID.equals("\"" + wifiInfo.getSsid() + "\"")) {
				foundAKnownNetwork = true;
				boolean result = wifiManager.enableNetwork(wifiConfiguration.networkId, true);
				if (result) {
					showLongToast("Now connected to known network \"" + wifiInfo.getSsid()
							+ "\". If you want to set a new WPA key, please delete the network first.");
				}
				else {
					showLongToast("Connection to a known network failed.");
				}
			}
		}

		if (!foundAKnownNetwork) {
			setupNewNetwork(wifiInfo, wifiManager);
		}
	}

	protected void setupNewNetwork(SimpleWifiInfo wifiInfo, WifiManager wifiManager) {
		WifiConfiguration wc = new WifiConfiguration();
		wc.SSID = "\"" + wifiInfo.getSsid() + "\"";

		if (wifiInfo.isKeyPreHashed())
			wc.preSharedKey = wifiInfo.getKey();
		else
			wc.preSharedKey = "\"" + wifiInfo.getKey() + "\"";

		int networkId = wifiManager.addNetwork(wc);
		boolean result = wifiManager.enableNetwork(networkId, true);

		if (result) {
			showLongToast("Now connected to \"" + wifiInfo.getSsid() + "\"");
			wifiManager.saveConfiguration();
		}
		else {
			showLongToast("Creating connection failed. " + wc);
		}
	}

	private void showLongToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
