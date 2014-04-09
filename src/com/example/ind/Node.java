package com.example.ind;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Node {
	@SerializedName("ID")
	private long _id;
	
	@SerializedName("Map Name")
	private String mapName;
	
	@SerializedName("X")
	private double Xpos;
	
	@SerializedName("Y")
	private double Ypos;
	
	@SerializedName("Router 1")
	private long r1;
	
	@SerializedName("Router 2")
	private long r2;
	
	@SerializedName("Router 3")
	private long r3;
	
	@SerializedName("Router 4")
	private long r4;
	
	@SerializedName("Distance")
	private double distance;

	public long get_id() {
		return _id;
	}
	public void set_id(long l) {
		_id = l;
	}
	
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mName) {
		mapName = mName;
	}
	
	public double getXpos() {
		return Xpos;
	}
	public void setXpos(double xpos) {
		Xpos = xpos;
	}
	
	public double getYpos() {
		return Ypos;
	}
	public void setYpos(double ypos) {
		Ypos = ypos;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double d) {
		distance = d;
	}

	
	public long getR1() {
		return r1;
	}
	public void setR1(long r1) {
		this.r1 = r1;
	}
	public long getR2() {
		return r2;
	}
	public void setR2(long r2) {
		this.r2 = r2;
	}
	public long getR3() {
		return r3;
	}
	public void setR3(long r3) {
		this.r3 = r3;
	}
	public long getR4() {
		return r4;
	}
	public void setR4(long r4) {
		this.r4 = r4;
	}
	
	@Override
	public String toString() {		
		GsonBuilder builder = new GsonBuilder(); 
		Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	// set which nodes neighbor current node
	public void setNeighborNode(String string, Node prevNode) {
		// TODO Auto-generated method stub
		
	}


}
