package com.example.ind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Controller extends Activity{
	
	@SuppressWarnings("unused")
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        
    	// Button for set Wifi ap's
    	Button setWifiAPs = (Button) findViewById(R.id.welcome_screen_button1);   
    	// Button for creating route graph on map
    	Button route = (Button) findViewById(R.id.welcome_screen_button2);
    	// Button for navigation
    	Button navigate = (Button) findViewById(R.id.welcome_screen_button3);
    	// Button for choosing map file
    	Button uploadMap = (Button) findViewById(R.id.welcome_screen_button4);
    	// Button for showing all networks
    	Button showAllNetworks = (Button) findViewById(R.id.welcome_screenbutton6);
    	// Button for about app
    	Button about = (Button) findViewById(R.id.welcome_screen_button5);
    	preLoadPreferences();
    }
    
    private void preLoadPreferences() {
		
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	String fileName = prefs.getString("FILENAME", "No file name set.");
    	int dbVersion = prefs.getInt("DATABASE_VERSION", -1);
    	Editor edit = prefs.edit();
    	
    	if(fileName.equals("No file name set."))
    	{
    		edit = prefs.edit();
    		edit.putString("FILENAME", "mm2.jpg");
        	edit.commit();
        	Log.d("WN Prefs", "Filename pref was " +  fileName);
    	}
    	if(dbVersion == -1)
    	{
    		edit = prefs.edit();
        	edit.putInt("DATABASE_VERSION", 5);
        	edit.commit();
        	Log.d("WN Prefs", "DB version pref was " + dbVersion);
    	}
		
	}

	// function for handling the button clicks
	public void onClick(View v) {
       
		switch(v.getId())
		{
			case R.id.welcome_screen_button1:	// set Wifi ap's
				Intent lmIntent = new Intent(getApplicationContext(), LoadAPsOnMap.class);
				startActivity(lmIntent);
				break;
				
			case R.id.welcome_screen_button2:
				Intent sgnIntent = new Intent(getApplicationContext(), SetGraphNodesOnMap.class);
				startActivity(sgnIntent);	
				break;
							
			case R.id.welcome_screen_button3:
				Intent navIntent = new Intent(getApplicationContext(), Navigate.class);
				startActivity(navIntent);
				break;
				
			case R.id.welcome_screen_button4:
				Intent fileUpIntent = new Intent(getApplicationContext(), FileDialog.class);
				startActivity(fileUpIntent);
				break;
			
			case R.id.welcome_screenbutton6:
				Intent wnIntent = new Intent(getApplicationContext(), NetworksActivity.class);
				startActivity(wnIntent);
				break;
			
			case R.id.welcome_screen_button7:
				Intent dbOpIntent = new Intent(getApplicationContext(), DBOperations.class);
				startActivity(dbOpIntent);
				break;
				
			case R.id.welcome_screen_button5:
				Intent aboutIntent = new Intent(getApplicationContext(), About.class);
				startActivity(aboutIntent);
				break;
			
			default:
				Toast.makeText(getApplicationContext(), getLocalClassName() + " Error!", Toast.LENGTH_SHORT).show();
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
