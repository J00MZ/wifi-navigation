package com.example.ind;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDB extends ListActivity{

	private NodesDBSource datasource;
	private TextView count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.node_db_layout);

	    datasource = new NodesDBSource(getApplicationContext());
	    datasource.open();
	
	    List<Node> values = datasource.getAllNodes();
	
	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Node> adapter = new ArrayAdapter<Node>(this,
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	    count = (TextView) findViewById(R.id.nodes_count);
	    count.setText("DB has " + getListAdapter().getCount() + " Nodes." );
	  }

	  // Will be called via the onClick attribute
	  // of the buttons in node_db_layout.xml
	  public void onClick(View view) {
	    @SuppressWarnings("unchecked")
	    ArrayAdapter<Node> adapter = (ArrayAdapter<Node>) getListAdapter();
	    Node node = new Node();
	    switch (view.getId()) 
	    {
		    case R.id.add:
		    	Toast.makeText(getApplicationContext(), "Cannot be created without map.", Toast.LENGTH_SHORT).show();
		    	break;
		      
		    case R.id.delete:
		      if (getListAdapter().getCount() > 0) {
		    	  node = (Node) getListAdapter().getItem(0);
		    	  datasource.deleteNode(node);
		    	  adapter.remove(node);
		      }
		      break;
	    }
	    adapter.notifyDataSetChanged();
	    count.setText("DB has " + getListAdapter().getCount() + " Nodes." );
	  }

	  @Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
}
