package com.example.ind;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/**
 * Created by Joe Tavin on 19/05/13.
 */
public class floorWrapper {

	private Map<String, WifiAP> ap_map;
	private String filename;

	public floorWrapper()
	{
		ap_map = new HashMap<String, WifiAP>();
	}
	
	public void AddAP(WifiAP ap) 
	{
		Log.d("WN", "AP Name: " + ap.getSSID() + "AP = " + ap.toString());
		ap_map.put(ap.getSSID(), ap);
	}
	
	public Map<String, WifiAP> getAllAPs(){
	
		return ap_map;
		
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}