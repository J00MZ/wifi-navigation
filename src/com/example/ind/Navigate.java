package com.example.ind;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Joe Tavin
 *
 */
public class Navigate extends Activity{
	
	public static Context appContext = null;
    private Navigate currActivity = null;
    private static WifiReceiver receiverWifi = null;
	private static WifiReceiver positionWifi = null;
	private WifiManager wM;
	private WifiManager wMP;
	private Map<String, Integer> wifiMap;
	private ImageView iv;
	private List<Node> nodes;
	protected Node latest_node;	
	private SharedPreferences prefs;
	private String mName;
	private static final String bssid_1 = "f8:d1:11:c0:56:96";  // michlala
	private static final String bssid_2 = "00:1f:1f:68:2b:24";	// cs
	private static final String bssid_3 = "64:70:02:c5:78:32";	// tp-link
	private static final String bssid_4 = "b0:48:7a:c0:8d:ec"; 	// michlala 2
	private NodesDBSource datasource;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigate);
		
		// App members
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mName = prefs.getString("FILENAME", "No file name set.");
		appContext = this.getApplicationContext();
		currActivity = Navigate.this;
		receiverWifi = new WifiReceiver(currActivity, wM);
		positionWifi = new WifiReceiver(currActivity, wMP);
        iv = (ImageView) findViewById(R.id.michlala_map_2);
        datasource = new NodesDBSource(getApplicationContext());
	    datasource.open();
	    nodes = new ArrayList<Node>();
	   // nodes = datasource.getAllNodes();
	}
	 @Override
	  protected void onResume() {
		 
	    datasource.open();
	    super.onResume();
	  }

	 @Override
  	protected void onPause() {
	 //unregisterReceiver(receiverWifi);
	 //unregisterReceiver(positionWifi);
	 datasource.close();
	 super.onPause();
  	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigate_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.load_nodes_but:
			showToast("Entering Load Nodes mode", Toast.LENGTH_SHORT);
			iv.setOnTouchListener(getOnTouchListener(true));
			break;
			
		case R.id.stop_load:
			showToast("Exiting Load Nodes mode", Toast.LENGTH_SHORT);
			iv.setOnTouchListener(getOnTouchListener(false));
			break;
			
		case R.id.show_nodes:
			showAllNodesOnMap();
			break;
			
		case R.id.hide_nodes:
			hideAllNodesOnMap();
			break;
				
		case R.id.show_position_but:
			showToast("Calculating Position...", Toast.LENGTH_LONG);
			calcPosition();
			break;
		case R.id.db_item:
			Intent dbInt = new Intent(getApplicationContext(), ViewDB.class);
			startActivity(dbInt);
			break;
		case R.id.action_settings:
			Intent sInt = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(sInt);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return (super.onOptionsItemSelected(item));
	}
	

	private OnTouchListener getOnTouchListener(boolean which) {
		
		return (which) ?
		 new OnTouchListener() {
			
        	@Override
        	public boolean onTouch(View v, MotionEvent event) 
        	{
        		if (event.getAction() == MotionEvent.ACTION_DOWN) 
        		{
        			Log.d("WiFi Nav", "Stage 001 in add node WiFi signals starting...");
        			int x_last = (int) event.getX();
        			int y_last = (int) event.getY();
        			
        			Bitmap bmp = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(), Config.ARGB_8888);
        	        Canvas canvas = new Canvas(bmp);
        			iv.draw(canvas);

        			Paint circlePaint = new Paint();
        			circlePaint.setColor(Color.RED);
        			canvas.drawCircle((float) x_last, (float) y_last, 8, circlePaint);	
        			
        			iv.setImageBitmap(bmp);
        			
        			SecureRandom r = new SecureRandom();
        			latest_node = new Node();
        			
        			latest_node.set_id(r.nextInt());
        			latest_node.setMapName(mName);
        			latest_node.setXpos(x_last);
                    latest_node.setYpos(y_last);
                    
                    showToast("New node position: [" + x_last + ", " + y_last + "]" + "\nCalculating WiFi Signal...", Toast.LENGTH_LONG);
                    Log.d("WiFi Nav", "Stage 002 before WiFi scan...");
                    Log.d("WiFi Nav", "Stage 003 Node = " + latest_node.toString());
                    receiverWifi.mainWifi.startScan();
                    Log.d("WiFi Nav", "Stage 004 WiFi scan done. going to sleep...");
        			try {
        				Thread.sleep(1000);
        				} catch (InterruptedException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			Log.d("WiFi Nav", "Stage 005 Back from sleep...");
                    wifiMap = receiverWifi.map;
                   
                    if(!wifiMap.isEmpty())
                    {
                    	Log.d("WiFi Nav", "Stage 005.5 MAP IS NOT EMPTY...");
                    	latest_node.setR1(wifiMap.get(bssid_1));
                    	latest_node.setR2(wifiMap.get(bssid_2));
                    	latest_node.setR3(wifiMap.get(bssid_3));
                    	latest_node.setR4(wifiMap.get(bssid_4));
                    }
                    Log.d("WiFi Nav", "Stage 006 Node = " + latest_node.toString());
                    nodes.add(latest_node);
                    datasource.createNode(latest_node);
        		}
        		return true;
        	}
		} 
		: 
		new OnTouchListener() {
			
        	@Override
        	public boolean onTouch(View v, MotionEvent event) 
        	{
        		return true;
        	}
		};
	}

	private void showAllNodesOnMap() {
		
		for(Node aNode : nodes)
		{
			Bitmap bmp = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(bmp);
			iv.draw(canvas);

			Paint circlePaint = new Paint();
			circlePaint.setColor(Color.RED);
			canvas.drawCircle((float) aNode.getXpos(), (float) aNode.getYpos(), 8, circlePaint);	
			
			iv.setImageBitmap(bmp);
		}
	}

	private void hideAllNodesOnMap() {
		//iv = (ImageView) findViewById(R.id.michlala_map_2);
        //iv.setImageResource(R.id.michlala_map_2);
		iv.setBackgroundResource(R.drawable.mm2);
	}

	private void calcPosition() {
		
		positionWifi.mainWifi.startScan();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wifiMap = positionWifi.map;

		if(!wifiMap.isEmpty())
        {
			if(wifiMap.containsKey(bssid_1) 
				&& wifiMap.containsKey(bssid_2)
				&& wifiMap.containsKey(bssid_3))
			{
				showPositionOnMap(wifiMap);
			}
			else
			{
				showToast("Not enough router data.\nTry again.", Toast.LENGTH_SHORT);
			}
        }
		else
		{
			showToast("No router data.\nTry again.", Toast.LENGTH_SHORT);
		}
	}

	private void showPositionOnMap(Map<String, Integer> currRoutVals) {
		double distVec;
		Node minNode = new Node();
		minNode.setDistance(0.0);
		boolean flag = true;
	    nodes = datasource.getAllNodes();
	    
		for(Node aNode : nodes)
		{
			distVec = Math.sqrt(
					  Math.pow((currRoutVals.get(bssid_1) - aNode.getR1()), 2)
					+ Math.pow((currRoutVals.get(bssid_2) - aNode.getR2()), 2)
					+ Math.pow((currRoutVals.get(bssid_3) - aNode.getR3()), 2)
					+ Math.pow((currRoutVals.get(bssid_4) - aNode.getR4()), 2));
			
			aNode.setDistance(distVec);
			datasource.updateNode(aNode);
			
			Log.d("WiFi Nav", "michlala -- pos: " + currRoutVals.get(bssid_1) + ", loop: " + aNode.getR1()); 
			Log.d("WiFi Nav", "michlala -- pos: " + currRoutVals.get(bssid_4) + ", loop: " + aNode.getR4()); 
			Log.d("WiFi Nav", "cs -- pos: " + currRoutVals.get(bssid_2) + ", loop: " + aNode.getR2());
			Log.d("WiFi Nav", "tplink -- pos: " + currRoutVals.get(bssid_3) + ", loop: " + aNode.getR3());
			
			if (flag)	// first run of loop we define minimum node as the first
			{
				minNode = aNode;
				flag = false;
			}
			if (minNode.getDistance() > distVec)
			{
				minNode = aNode;
				Log.d("WiFi Nav", "Min Node ID: " + aNode.get_id());
			}
			Log.d("WiFi Nav", "current = [" + aNode.get_id() + ", " + aNode.getDistance() + "]");
			Log.d("WiFi Nav", "MIN = [" + minNode.get_id() + ", " + minNode.getDistance() + "]");
		}
		
		Bitmap bmp = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
		iv.draw(canvas);

		Paint circlePaint = new Paint();
		circlePaint.setColor(Color.BLUE);
		canvas.drawCircle((float) minNode.getXpos(), (float)  minNode.getYpos(), 10, circlePaint);
		iv.setImageBitmap(bmp);
		
	}
	
	public void showToast(final String toast, final int length)
	{
	    runOnUiThread(new Runnable() {
	        public void run()
	        {
	            Toast.makeText(Navigate.this, toast, length).show();
	        }
	    });
	}

}
