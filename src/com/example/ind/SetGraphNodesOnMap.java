package com.example.ind;

import java.security.SecureRandom;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetGraphNodesOnMap extends Activity {
	
	static Context appContext = null;
    private SetGraphNodesOnMap currActivity = null;
	private WifiManager wM;
	private WifiReceiver receiverWifi = null;
	private ImageView iv;
	private OnTouchListener otl = null;
    private int x_last = 0;
	private int y_last = 0;
	private ArrayList<Node> nodes;
	private Node latest_node = null;
	private Button change;
	private SharedPreferences prefs;
	private String mapFileName = "FILENAME";
	private NodesDBSource datasource;
	private static final String bssid_1 = "f8:d1:11:c0:56:96";   //michlala
	private static final String bssid_2 = "00:1f:1f:68:2b:24";	//cs
	private static final String bssid_3 = "64:70:02:c5:78:32";	//tp-link
	private static final String bssid_4 = "b0:48:7a:c0:8d:ec"; 	// michlala 2
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_graph_nodes);
		// App members
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		appContext = this.getApplicationContext();
		currActivity = SetGraphNodesOnMap.this;
        receiverWifi = new WifiReceiver(currActivity, wM);
        iv = (ImageView) findViewById(R.id.michlala_map_2);
//        iv.setImageResource(R.drawable.mm2);
//        iv.setMaxZoom(4f);
        datasource = new NodesDBSource(this);
	    datasource.open();
        nodes = new ArrayList<Node>();
        change = (Button) findViewById(R.id.button_to_change_file);
        change.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				String fileName = prefs.getString(mapFileName, "!!!");
				int fileID = 0;
				
				Toast.makeText(getApplicationContext(), "New File=" + fileName, Toast.LENGTH_LONG).show();
				if(!fileName.equals("!!!"))
				{
					if(fileName.equals("exl1.jpg"))
					{
						fileID = R.drawable.exl1;
					}
					else if(fileName.equals("exl2.jpg"))
					{
						fileID = R.drawable.exl2;
					}
					else if(fileName.equals("huji.JPG"))
					{
						fileID = R.drawable.huji;
					}
					else if (fileName.equals("mm.jpg"))
					{
						fileID = R.drawable.mm;
					}
					else if (fileName.equals("mall1.png"))
					{
						fileID = R.drawable.mall1;
					}
					else if (fileName.equals("mall2.jpg"))
					{
						fileID = R.drawable.mall2;
					}
					else if (fileName.equals("mall3.png"))
					{
						fileID = R.drawable.mall3;
					}
					else
					{
						fileID = R.drawable.mm2;
					}
					
					iv.setImageResource(fileID);
					Toast.makeText(getApplicationContext(), "Map File Changed!", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        otl = new OnTouchListener() {

			@Override
        	public boolean onTouch(View v, MotionEvent event) 
        	{
        		if (event.getAction() == MotionEvent.ACTION_DOWN) 
        		{
        			receiverWifi.mainWifi.startScan();
        			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			latest_node = new Node();
        			
        			x_last = (int) event.getX();
        			y_last = (int) event.getY();
        			
        			SecureRandom r = new SecureRandom();
        			latest_node.set_id(r.nextLong());
        			latest_node.setMapName(prefs.getString(mapFileName, "filename not available."));
                    latest_node.setXpos(x_last);
                    latest_node.setYpos(y_last);
                    
                    if(!receiverWifi.map.isEmpty())
                    {
                    	latest_node.setR1(receiverWifi.map.get(bssid_1));
                    	latest_node.setR2(receiverWifi.map.get(bssid_2));
                    	latest_node.setR3(receiverWifi.map.get(bssid_3));
                    	latest_node.setR3(receiverWifi.map.get(bssid_4));
                    }
                   
//                	if(nodes.size() > 1)
//        			{
//        				setNeighbors(latest_node, nodes.get(nodes.indexOf(latest_node) - 1));
//        			}
                	
                    showNodeOnMap(latest_node);
                    nodes.add(latest_node);
                    datasource.createNode(latest_node);
        		}
        		return true;
        	}
		};
		iv.setOnTouchListener(otl);
		
	}
	
	@Override
	public void onPause()
	{
		unregisterReceiver(receiverWifi);
		super.onPause(); 
	}
	
	@Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.set_graph_nodes_menu, menu);
			return true;
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			
		case R.id.view_graph_id:
			drawGraph();
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
	
		// this function draws the node on the map according to the x, y coordinates.
		
	public void showNodeOnMap(Node node)
	{
		float textSize = 20;
        Bitmap bmp = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
		iv.draw(canvas);

		Paint circlePaint = new Paint();
		circlePaint.setColor(Color.RED);
		canvas.drawCircle((float) node.getXpos(), (float) node.getYpos(), 8, circlePaint);
			
		Paint namePaint = new Paint();
		namePaint.setColor(Color.BLACK);
		namePaint.setTextSize(textSize);
		canvas.drawText( "[" + node.getXpos() + ", " + node.getYpos() + "]",
                (float) node.getXpos(),
                (float) node.getYpos(), namePaint);
		
		iv.setImageBitmap(bmp);
	}
		
		
		// this function connects all nodes to a graph
		
	public void drawGraph(){
		
		//nodes = (ArrayList<Node>) datasource.getAllNodes();
		// draw it on screen
		Bitmap bmp = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
		
        iv.draw(canvas);

        for(int i=1; i< nodes.size();  i++ )
		{
			Paint bluePaint = new Paint();
			bluePaint.setColor(Color.BLUE);
			Paint redPaint = new Paint();
			redPaint.setColor(Color.RED);

			canvas.drawCircle((float) nodes.get(i).getXpos(), (float) nodes.get(i).getYpos(), 8, bluePaint);
			
			canvas.drawLine((float) nodes.get(i-1).getXpos(), (float) nodes.get(i-1).getYpos(),
								(float) nodes.get(i).getXpos(), (float) nodes.get(i).getYpos(), redPaint);			
		}
		iv.setImageBitmap(bmp);
	}
		
		
		public void setNeighbors(Node currNode, Node prevNode)
		{
			currNode.setNeighborNode("n0", prevNode);
			prevNode.setNeighborNode("n1", currNode);	
		}
		
		
		public void autoCalculateNeighbors(){
			
		}
}


