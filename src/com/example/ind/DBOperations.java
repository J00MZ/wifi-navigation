package com.example.ind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DBOperations extends Activity{
	
	private NodesDBSource datasource;
	

	@SuppressWarnings("unused")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.db_operations_layout);
	    Button viewDB = (Button) findViewById(R.id.db_op_view);
	    Button deleteDB = (Button) findViewById(R.id.db_op_delete);
	    datasource = new NodesDBSource(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.db_op_view:
				Intent vdbIntent = new Intent(this, ViewDB.class);
				startActivity(vdbIntent);
				Log.d("WN App", "view db reached!");
				break;
			case R.id.db_op_reset:
				
				break;
				
			case R.id.db_op_delete: 
				handleDelete();
			break;
		
		}
	}

	private void handleDelete() {
		
		AlertDialog.Builder ad = new AlertDialog.Builder(
				DBOperations.this);
		 
		// Setting Dialog Title
		ad.setTitle("Confirm Delete...");
		 
		// Setting Dialog Message
		ad.setMessage("Are you sure you want delete the database?");
		 
		// Setting Positive "Yes" Btn
		ad.setPositiveButton("YES",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // DB IS DELETED HERE!!
		            	datasource.delete(); 
		                Toast.makeText(getApplicationContext(),
		                        "You clicked on \"YES\"\nDB will be deleted.", Toast.LENGTH_SHORT)
		                        .show();
		            }
		        });
		// Setting Negative "NO" Btn
		ad.setNegativeButton("NO",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write your code here to execute after dialog
		                Toast.makeText(getApplicationContext(),
		                        "You clicked on \"NO\".\nNothing will be done.", Toast.LENGTH_SHORT)
		                        .show();
		                dialog.cancel();
		            }
		        });
		 
		// Showing Alert Dialog
		ad.show();
		
	}
	
	
}
