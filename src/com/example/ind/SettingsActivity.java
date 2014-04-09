package com.example.ind;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{

	private SharedPreferences prefs;
	private Editor edit;
	private EditText et;
	private ListView listview;
	private Button sb;
	private TextView currentFile;
	private String[] routers;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.settings_layout);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        et = (EditText) findViewById(R.id.db_version_num);
        currentFile = (TextView) findViewById(R.id.textView2);
        listview = (ListView) findViewById(R.id.routers_list);
    	sb = (Button) findViewById(R.id.save_button);
    	sb.setOnClickListener(this);
    	showRouterList();
    	loadPrefs();
    }
	
	private void showRouterList() {
		
		routers = new String[] {"Router: michlala, MAC = f8:d1:11:c0:56:96" ,
								"Router: cs, MAC = 00:1f:1f:68:2b:24",
								"Router: TP-LINK_C57832, MAC = 64:70:02:c5:78:32",
								"Router: michlala, MAC = 2b0:48:7a:c0:8d:ec"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    	        android.R.layout.simple_list_item_1, routers);
		
	    listview.setAdapter(adapter);
	}

	private void loadPrefs() {
    	String fileName = prefs.getString("FILENAME", "No file name set.");
    	int dbVersion = prefs.getInt("DATABASE_VERSION", -1);
    	et.setText(String.valueOf(dbVersion));
    	currentFile.setText(fileName);
	}
	// for saving the database version 
	private void savePrefs(String key, int value) {
	
    	edit = prefs.edit();
    	edit.putInt(key, value);
    	edit.commit();
	}
	
	// for saving the filename
	private void savePrefs(String key, String value) {
		
    	edit = prefs.edit();
    	edit.putString(key, value);
    	edit.commit();
	}

	@Override
	public void onClick(View v) {
		
		savePrefs("DATABASE_VERSION", Integer.parseInt(et.getText().toString()));
		savePrefs("FILENAME",  currentFile.getText().toString());
		loadPrefs();
		Toast.makeText(getApplicationContext(), "Your preferences have been saved.", Toast.LENGTH_SHORT).show();
	}	 

	
}
