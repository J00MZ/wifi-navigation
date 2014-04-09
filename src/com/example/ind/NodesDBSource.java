package com.example.ind;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class NodesDBSource{
	
		private Context contxt;
	  private SQLiteDatabase database;
	  private DBHelper dbHelper;
	  private String[] allColumns = { DBHelper.COLUMN_ID, DBHelper.COLUMN_MAP_NAME,
									  DBHelper.COLUMN_X,  DBHelper.COLUMN_Y,
									  DBHelper.COLUMN_R1,  DBHelper.COLUMN_R2,  DBHelper.COLUMN_R3, DBHelper.COLUMN_R4,
									  DBHelper.COLUMN_DISTANCE};
	  									  

	  public NodesDBSource(Context context) // CONSTRUCTOR!
	  {
		// prefs = loadPrefs();
		//, prefs.getInt(dbv, 1)
	    dbHelper = new DBHelper(context);
	  }

	  public void open() throws SQLException 
	  {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void delete() throws SQLException 
	  {
		  database = dbHelper.getWritableDatabase();
		  dbHelper.deleteDB(database);
	  }
	  
	  public void createNode( Node node) 
	  {
	    dbHelper.addNode(node);
	    Log.d("WiFi Nav DB", "Node with id: " + node.get_id() + " was created.\n");
	    Node n = dbHelper.getNode(node.get_id());
	    Log.d("WiFi Nav DB", "NODE DATA: " + n.toString());
	  }

	  public void deleteNode(Node node) 
	  {
	    long id = node.get_id();
	    dbHelper.deleteNode(id);
	    Log.d("WiFi Nav DB", "Node with id: " + id + " was deleted.\n");
	  }

	  public void updateNode(Node node) 
	  {
	    long id = node.get_id();
	    dbHelper.updateNode(node);
	    Log.d("WiFi Nav DB", "Node with id: " + id + " was updated.\n");
	  }
	  
	  public List<Node> getAllNodes() 
	  {
		database = dbHelper.getReadableDatabase();
	    List<Node> nodesList = new ArrayList<Node>();

	    Cursor cursor = database.query(DBHelper.TABLE_NODES,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Node node = cursorToNode(cursor);
	    	nodesList.add(node);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return nodesList;
	  }

	  private Node cursorToNode(Cursor cursor) {
		  
		  Node node = new Node();
		  node.set_id(cursor.getLong(0));
		  
		  node.setMapName(cursor.getString(1));
		  node.setXpos(cursor.getDouble(2));
		  node.setYpos(cursor.getDouble(3));
		  node.setR1(cursor.getLong(4));
		  node.setR2(cursor.getLong(5));
		  node.setR3(cursor.getLong(6));
		  node.setR4(cursor.getLong(7));
		  node.setDistance(cursor.getDouble(8));
		  
		  return node;
	  }

	   @SuppressWarnings("unused")
	   private SharedPreferences loadPrefs() 
	   {
		   contxt = Navigate.appContext;
		   return PreferenceManager.getDefaultSharedPreferences(contxt);
	   }
}
