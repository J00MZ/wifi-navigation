package com.example.ind;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class LoadAPsOnMap extends Activity {

	private static Context appContext = null;
	private floorWrapper fw = null;
	private SharedPreferences prefs;
    private int x_last = 0;
	private int y_last = 0;
    private  ImageView iv;	
	private String _ap_name=null;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_routers_layout);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
		// App members
		appContext = this.getApplicationContext();
		fw = new floorWrapper();
		fw.setFilename(prefs.getString("FILENAME", "No file name set."));
        iv = (ImageView) findViewById(R.id.add_routers_image_view);
        iv.setOnTouchListener(new OnTouchListener() {
			
        	@Override
        	public boolean onTouch(View v, MotionEvent event) 
        	{
        		if (event.getAction() == MotionEvent.ACTION_DOWN) 
        		{

        			x_last = (int) event.getX();
        			y_last = (int) event.getY();
                    final EditText input = new EditText(LoadAPsOnMap.this);
                    new AlertDialog.Builder(LoadAPsOnMap.this)
                            .setTitle("New Wifi Router")
                            .setMessage("Please enter SSID of new Wifi Network Router.")
                            .setView(input)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int whichButton) {
                                            Editable value = input.getText();
                                            if (value == null) throw new AssertionError();
                                            _ap_name = value.toString();

                                            WifiAP ap = new WifiAP();
                                            ap.setSSID(_ap_name);
                                            ap.setfName(prefs.getString("FILENAME", "No file name set."));
                                            //ap.setBSSID(scanresult...)
                                            ap.setXPos(x_last);
                                            ap.setYPos(y_last);
                                           
                                            fw.AddAP(ap);
                                            
                                            Toast.makeText(
                                                    appContext,
                                                    "Starting Wifi AP Position Load of "+ ap.getSSID() +"...\n" 
                                                    + "MAC address: " + ap.getBSSID() + "\nPosition Captured ["
                                                    + x_last + ", " + y_last + "]\n",
                                                    Toast.LENGTH_LONG).show();
                                            printAP(ap);
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int whichButton) {
                                            // Do nothing.
                                        }
                                    }).show();		
        		}
        		return true;
        	}
		} );
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
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
			
		case R.id.all_networks:
			Intent wnIntent = new Intent(getApplicationContext(), NetworksActivity.class);
			startActivity(wnIntent);
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
	
	// this prints the new AP on the Map Image
	
	public void printAP(WifiAP ap)
	{
        Bitmap bmp = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
		iv.draw(canvas);

		Paint circlePaint = new Paint();
		circlePaint.setColor(Color.RED);
		canvas.drawCircle((float) ap.getXPos(), (float) ap.getYPos(), 10, circlePaint);
			
		Paint namePaint = new Paint();
		namePaint.setColor(Color.BLUE);
		namePaint.setTextSize(48);
		canvas.drawText(ap.getSSID(),
                (float) ap.getXPos(),
                (float) ap.getYPos(), namePaint);
		
		iv.setImageBitmap(bmp);
	}
}

