package de.grundid.wificonnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WifiConnect extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button)findViewById(R.id.saveTagButton);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ssid = ((TextView)findViewById(R.id.ssid)).getText().toString();
				String key = ((TextView)findViewById(R.id.wpaKey)).getText().toString();
				// TODO add field for the type. Right now we use WPA as default
				SimpleWifiInfo simpleWifiInfo = new SimpleWifiInfo("wpa", ssid, key);
				Intent intent = new Intent(WifiConnect.this, WifiConnectConfigWriter.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("SIMPLE_WIFI_INFO", simpleWifiInfo);
				WifiConnect.this.startActivityForResult(intent, 100);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100)
			finish();
	}
}