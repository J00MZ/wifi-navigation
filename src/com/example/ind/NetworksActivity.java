package com.example.ind;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NetworksActivity extends Activity {
	
	public final int iterations = 5;
	public WifiManager mW;
	WiFiNetworks networks;
	Button scanForAPS;
	Button normalize;
	Button submit;
	EditText editName1;
	EditText editName2;
	EditText editName3;
	List<String> routerNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_networks_info);
		routerNames = new ArrayList<String>();
		scanForAPS = (Button) findViewById(R.id.rescan_button);   		
		networks = new WiFiNetworks(this, mW, iterations, routerNames);
		networks.mainWifi.startScan();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
	    //unregisterReceiver(networks);
		super.onPause();
	}

	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.rescan_button:	
				networks.mainWifi.startScan();
				break;
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			
		case R.id.action_settings:
			Intent sInt = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(sInt);
			break;
		default:
			return super.onOptionsItemSelected(item);

		}
		return (super.onOptionsItemSelected(item));
	}
	
	
}
