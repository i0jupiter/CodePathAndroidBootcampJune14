package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends ActionBarActivity {

	private final int REQUEST_CODE = 100;
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		readItems();
		todoAdapter = new ArrayAdapter<String>(this, 
											   android.R.layout.simple_list_item_1, 
											   todoItems);
		
		lvItems = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(todoAdapter);
		
		// Set up click and long click listeners on the ListView
		setupListClickListener();
		setupListViewListener();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}
	
	// Handle adding an item
	public void onAddedItem(View v) {
		
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		writeItems();
	}
	
	// Handle editing an item
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			String editedItemText = data.getExtras().getString("editedItemText");
		    int editedItemPosition = data.getExtras().getInt("editedItemPosition");
		    String oldItemText = todoItems.get(editedItemPosition);
		    
		    // Re-write only if the item was actually edited. Case-insensitive comparison!
		    if (!oldItemText.equals(editedItemText)) {
			    todoItems.set(editedItemPosition, editedItemText);
			    todoAdapter.notifyDataSetChanged();
				writeItems();
		    }
		}
	}

	/* Listeners on to-do item ListView */
	
	// Clicking an item starts EditItemActivity
	private void setupListClickListener() {
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, 
									View view, 
									int position, 
									long id) {
				
				Intent editItemIntent = new Intent(ToDoActivity.this, EditItemActivity.class);
				editItemIntent.putExtra("itemPosition", position);
				editItemIntent.putExtra("itemText", todoItems.get(position));
				startActivityForResult(editItemIntent, REQUEST_CODE);
			}
		});
	}
	
	// Holding an item deletes it
	private void setupListViewListener() {
		
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, 
										   View view,
										   int position, 
										   long rowId) {
				
				todoItems.remove(position);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
		});
	}
	
	/* Private Methods */
	
	// Read to-do items from a file
	private void readItems() {
		
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException ex) {
			todoItems = new ArrayList<String>();
			ex.printStackTrace();
		}
	}
 	
	// Write to-do items to a file
	private void writeItems() {
		
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}