package com.example.ind;

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WifiAP {
	
	@SerializedName("File Name")
	private String fName;

	public String fName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}
	@SerializedName("SSID")
	private String SSID;

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String SSID) {
		this.SSID = SSID;
	}

	@SerializedName("BSSID")
	private String BSSID;

	public String getBSSID() {
		return BSSID;
	}

	public void setBSSID(String BSSID) {
		this.BSSID = BSSID;
	}
	@SerializedName("X")
	private int XPos;

	public int getXPos() {
		return XPos;
	}

	public void setXPos(int XPos) {
		this.XPos = XPos;
	}

	@SerializedName("Y")
	private int YPos;
	

	public int getYPos() {
		return YPos;
	}

	public void setYPos(int YPos) {
		this.YPos = YPos;
	}

	@Override
	public String toString() {		
		GsonBuilder builder = new GsonBuilder(); 
		Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}