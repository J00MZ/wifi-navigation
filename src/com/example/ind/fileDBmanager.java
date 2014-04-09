package com.example.ind;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.os.Environment;
import android.util.Log;

public class fileDBmanager{
	
	private static final String TAG = "Error";
	private String fileName;
	//private static final String TAG = parent.class.getName();

	public static String Read(String fName)
	{     
       String ret = "";
      //Find the directory for the SD Card using the API
      File dir = new File( Environment.getExternalStorageDirectory()+ "/WifiNavigation/");
      //Get the text file
      File file = new File(dir, fName);
      if(!file.exists())
      {  
    	  return "";
      }
      //Read text from file
      StringBuilder sb = new StringBuilder();
      try {
          BufferedReader br = new BufferedReader(new FileReader(file));
          String line;

          while ((line = br.readLine()) != null) {
              sb.append(line);
              sb.append('\n');
          }
          br.close();
          ret=sb.toString();
      }
      catch (FileNotFoundException e) {
      	Log.e(TAG, "File not found: " + e.toString());
      } catch (IOException e) {
          Log.e(TAG, "Can not read file: " + e.toString());
      }

      return ret;
	}
	
	public static void Write( String fName, String data){
	    try
	    {
	        File root = new File(Environment.getExternalStorageDirectory() + "/WifiNavigation/"); 
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        FileWriter writer = new FileWriter(new File(root, fName), true);
	        writer.append(data);
	        writer.append("\n");
	        writer.flush();
	        writer.close();
	    }
	    catch(IOException e)
	    {
	    	Log.e(TAG, "File write failed: " + e.toString());
	    }
	   }  
	

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}