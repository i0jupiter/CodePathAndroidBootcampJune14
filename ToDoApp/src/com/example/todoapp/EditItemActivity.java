package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends ActionBarActivity {
	
	EditText etEditItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		String itemText = getIntent().getStringExtra("itemText");
		
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(itemText);
		// Set the cursor to the right of the text to be edited.
		etEditItem.setSelection(etEditItem.getText().length());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	// Save the edited item and return to parent activity
	public void onSave(View v) {
		
		// Put the edited item in a new Intent to return to parent activity
		Intent editItemIntent = new Intent();
		editItemIntent.putExtra("editedItemText", etEditItem.getText().toString());
		
		// Get the position of the edited item from the intent that started this activity
		editItemIntent.putExtra("editedItemPosition", 
								getIntent().getIntExtra("itemPosition", 0));
		
		// Set result and close this activity
		setResult(RESULT_OK, editItemIntent);
		finish();
	}

}