package com.example.ind;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "nodes.db";
	private static final int DATABASE_VERSION = 5;
	public static final String TABLE_NODES = "nodes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MAP_NAME = "map_name";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_R1 = "router1";
	public static final String COLUMN_R2 = "router2";
	public static final String COLUMN_R3 = "router3";
	public static final String COLUMN_R4 = "router4";
	public static final String COLUMN_DISTANCE = "distance";
	private static final String[] allColumns = { COLUMN_ID, COLUMN_MAP_NAME,
												 COLUMN_X, COLUMN_Y,
												 COLUMN_R1, COLUMN_R2, COLUMN_R3, COLUMN_R4,
												 COLUMN_DISTANCE};
		  

	  // Database creation sql statement
	  // 
	private static final String DATABASE_CREATE = "create table "
	      + TABLE_NODES + "(" + COLUMN_ID
	      + " integer primary key autoincrement, "
	      + COLUMN_MAP_NAME +  " text not null ,"
	      + COLUMN_X 		+ " text not null ,"
	      + COLUMN_Y		+ " text not null ,"
	      + COLUMN_R1		+ " text ,"
	      + COLUMN_R2		+ " text ,"
	      + COLUMN_R3 		+ " text ,"
	      + COLUMN_R4 		+ " text ,"
	      + COLUMN_DISTANCE + " text );";
	
	public DBHelper(Context context)
	{	
	    super(context, DATABASE_NAME, null, DATABASE_VERSION); 	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	    db.execSQL(DATABASE_CREATE);
	    Log.d(DBHelper.class.getName(),
		        "Database was created!");
	  }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.d(DBHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODES);
	    onCreate(db);
	 }
	
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new node
    public void addNode(Node node) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, node.get_id()); // Node ID
        values.put(COLUMN_MAP_NAME, node.getMapName()); // Node MAP
        values.put(COLUMN_X, node.getXpos());
	    values.put(COLUMN_Y, node.getYpos());
	    values.put(COLUMN_R1, node.getR1());
	    values.put(COLUMN_R2, node.getR2());
	    values.put(COLUMN_R3, node.getR3());
	    values.put(COLUMN_R4, node.getR4());
	    values.put(COLUMN_DISTANCE, node.getDistance());
        
        db.insert(TABLE_NODES, null, values);	// Inserting Row
        db.close(); // Closing database connection
    }
 
    // Getting single node by id
    public Node getNode(long _id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_NODES, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(_id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
       
        Node node = new Node();
    	node.set_id(cursor.getLong(0));
    	node.setMapName(cursor.getString(1));
    	node.setXpos(cursor.getDouble(2));
    	node.setYpos(cursor.getDouble(3));
    	node.setR1(cursor.getLong(4));
    	node.setR2(cursor.getLong(5));
    	node.setR3(cursor.getLong(6));
    	node.setR3(cursor.getLong(7));
    	node.setDistance(cursor.getDouble(8)); 
        
        return node;
    }
     
    // Getting All Nodes
    public List<Node> getAllNodes() 
    {
        List<Node> nodeList = new ArrayList<Node>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NODES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Node node = new Node();
            	node.set_id(Integer.parseInt(cursor.getString(0)));
            	
            	node.setMapName(cursor.getString(1));
            	node.setXpos(cursor.getDouble(2));
            	node.setYpos(cursor.getDouble(3));
            	node.setR1(cursor.getLong(4));
            	node.setR2(cursor.getLong(5));
            	node.setR3(cursor.getLong(6));
            	node.setR3(cursor.getLong(7));
            	node.setDistance(cursor.getDouble(8));
            	
                // Adding node to list
            	nodeList.add(node);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return nodeList;
    }
 
    // Updating single node
    public int updateNode(Node node) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_MAP_NAME, node.getMapName());
        values.put(DBHelper.COLUMN_X, node.getXpos());
	    values.put(DBHelper.COLUMN_Y, node.getYpos());
	    values.put(DBHelper.COLUMN_R1, node.getR1());
	    values.put(DBHelper.COLUMN_R2, node.getR2());
	    values.put(DBHelper.COLUMN_R3, node.getR3());
	    values.put(DBHelper.COLUMN_R4, node.getR4());
	    values.put(DBHelper.COLUMN_DISTANCE, node.getDistance());
        // updating row
        return db.update(TABLE_NODES, values, COLUMN_ID + "=?",
                new String[] { String.valueOf(node.get_id()) });
    }
 
    // Deleting single node
    public void deleteNode(long _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NODES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(_id) });
        db.close();
    }
 
 
    // Getting nodes Count
    public int getNodesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NODES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
    public void deleteDB(SQLiteDatabase db){
    	Log.d(DBHelper.class.getName(),
    	        "Deleting database, this will destroy all old data too.");
    	 db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODES);
    }

}
