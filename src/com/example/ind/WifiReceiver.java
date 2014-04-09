package com.example.ind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {

	private final Activity parent;
	public WifiManager mainWifi;
	public Map<String, Integer> map = new HashMap<String, Integer>();
	List<ScanResult> wifiLevelList;
    Context context;

    // constructor
    public WifiReceiver(Activity parent, WifiManager mWifi) {
        this.parent = parent;
        this.mainWifi = mWifi;
    	mainWifi = (WifiManager) parent.getSystemService(Context.WIFI_SERVICE);
    	parent.registerReceiver(this, new IntentFilter( WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    	this.mainWifi.startScan();
    }

  
	@Override
	public void onReceive(Context c, Intent intent) {
		
		wifiLevelList = mainWifi.getScanResults();
        for (ScanResult awifiLevelList : wifiLevelList) 
        {
			map.put(awifiLevelList.BSSID, awifiLevelList.level);
        }
        
	}

	public Activity getParent() {
		return parent;
	}
	
}
