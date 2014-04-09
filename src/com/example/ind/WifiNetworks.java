package com.example.ind;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;

class WiFiNetworks extends BroadcastReceiver
{
	//variables
	NetworksActivity netActivity;
	public WifiManager mainWifi;
	public final int iterations;
	List<ScanResult> wifiList;
	private Map<String, Integer> rawMap = new HashMap<String, Integer>();
	private Map<String, Integer> normalizedMap = new HashMap<String, Integer>();
	private TextView WifiNetworksText;
	private List<String> routerNames;
	private List<Integer> routerSums;
	private int numOfNetworkToMonitor = 3;

	int index1 = 0;
	int index2 = 0;
	int index3 = 0;
	
	// constructor
    public WiFiNetworks(NetworksActivity nact, WifiManager mWifi, int it, List<String> rNames) {
    	this.netActivity = nact;
    	this.mainWifi = mWifi;
    	this.iterations = it;
    	this.routerNames = rNames;
    	routerSums = new ArrayList<Integer>(numOfNetworkToMonitor);
    	mainWifi = (WifiManager) netActivity.getSystemService(Context.WIFI_SERVICE);
    	netActivity.registerReceiver(this, new IntentFilter( WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
    
	@Override
	public void onReceive(Context context, Intent intent) {

		wifiList = mainWifi.getScanResults();
        for (ScanResult aWifiList : wifiList) 
        {
        	rawMap.put(aWifiList.SSID, aWifiList.level);
        	
			if(routerNames.contains(aWifiList.SSID))
    		{
	    		index1 ++;
	    		routerSums.set(0, routerSums.get(0) + aWifiList.level);	// an implementation of the += operator..
				if (index1 == iterations)
				{
					normalizedMap.put(aWifiList.SSID, routerSums.get(0)/iterations);
				}
    		}
				
        }
        
        if(rawMap.isEmpty())
        {
        	WifiNetworksText = (TextView) netActivity.findViewById(R.id.raw_network_data);
        	WifiNetworksText.setText("There are no networks in range to display.\n" +
        							"Try to change your location.");
        }
        else
        {
        	showNetworksLevel(rawMap, false);
        }
        if(normalizedMap.isEmpty())
        {
        	WifiNetworksText = (TextView) netActivity.findViewById(R.id.wifiTextview);
        	WifiNetworksText.setText("The chosen networks are not in range.\n" +
        							"Try to change your location.");
        }
        else
        {
        	showNetworksLevel(normalizedMap, true);
        }
    }

	
	
	public void showNetworksLevel(Map<String, Integer> networks, boolean which) {
	
	
		WifiNetworksText = (which) ? (TextView) netActivity.findViewById(R.id.wifiTextview)
								: (TextView) netActivity.findViewById(R.id.raw_network_data);
		WifiNetworksText.setText("");
		
		for (Map.Entry<String, Integer> entry : networks.entrySet()) 
		{
			String k = entry.getKey();
			Integer v = entry.getValue();
			WifiNetworksText.append("Network: " + k + ", Level: " + v.toString() + "\n");
		}
	}
}
