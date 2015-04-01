package com.example.favtwittersearch;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.Arrays;


import android.app.AlertDialog; 
import android.content.Context; 
import android.content.DialogInterface; 
import android.content.Intent; 
import android.content.SharedPreferences; 
import android.net.Uri; 

import android.view.LayoutInflater; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.view.inputmethod.InputMethodManager; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.TableLayout; 
import android.widget.TableRow; 

public class Main extends Activity {

	private SharedPreferences savedSearches; // user's favorite searches 
	private TableLayout queryTableLayout; // shows the search buttons 
	private EditText queryEditText; // where the user enters queries 
	private EditText tagEditText; // where the user enters a query's tag 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		savedSearches= getSharedPreferences("searches",MODE_PRIVATE);

		// get a reference to the queryTableLayout 
		queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout); 
		// get references to the two EditTexts and the Save Button 
		queryEditText = (EditText) findViewById(R.id.queryeditText);
		tagEditText = (EditText) findViewById(R.id.tageditText);

		// register listeners for the Save and Clear Tags Buttons 
		Button saveButton = (Button) findViewById(R.id.savebutton);     
		saveButton.setOnClickListener(saveButtonListener); 
		Button clearTagsButton = (Button) findViewById(R.id.clearTagsbutton);    
		clearTagsButton.setOnClickListener(clearTagsButtonListener);

		refreshButtons(null); // add previously saved searches to GUI


	}//end method onCreate

	// recreate search tag and edit Buttons for all saved searches; 
	// pass null to create all the tag and edit Buttons. 
	private void refreshButtons(String newTag) 
	{
		//store save tags in the tags array
		String tags[]=savedSearches.getAll().keySet().toArray(new String[0]);//Why is the size of String array zero?
		Arrays.sort(tags,String.CASE_INSENSITIVE_ORDER);//sort by tag
		// if a new tag was added, insert in GUI at the appropriate location 
		if (newTag != null)  

		{    
			makeTagGUI(newTag, Arrays.binarySearch(tags, newTag)); 
		} // end if 
		else // display GUI for all tags 
		{         
			// display all saved searches 
			for (int index = 0; index < tags.length; ++index) 
				makeTagGUI(tags[index], index); 
		}//end else
	}//end method refresh buttons

	// add new search to the save file, then refresh all Buttons 
	private void makeTag(String query, String tag) 
	{ 
		// originalQuery will be null if we're adding a new search 
		String originalQuery = savedSearches.getString(tag, null); 
		SharedPreferences.Editor preferencesEditor = savedSearches.edit();
		preferencesEditor.putString(tag, query);//store current search
		preferencesEditor.apply();//store the updated preferences

		// if this is a new query, add its GUI 
		if (originalQuery == null) 
			refreshButtons(tag); // adds a new button for this tag 
	}

	// add a new tag button and corresponding edit button to the GUI 
	private void makeTagGUI(String tag, int index)    
	{

		// get a reference to the LayoutInflater service            
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);    

		// inflate new_tag_view.xml to create new tag and edit Buttons 
		View newTagView = inflater.inflate(R.layout.new_tag_view, null);

		// get newTagButton, set its text and register its listener 
		Button newTagButton =(Button) newTagView.findViewById(R.id.newTagButton); 
		newTagButton.setText(tag); 
		newTagButton.setOnClickListener(queryButtonListener);

		// get newEditButton and register its listener     
		Button newEditButton = (Button) newTagView.findViewById(R.id.newEditButton); 
		newEditButton.setOnClickListener(editButtonListener); 

		//get newDeleteButton and register its listener
		System.out.println("Delete Button");
		Button newDeleteButton = (Button) newTagView.findViewById(R.id.newDeleteButton);
		newDeleteButton.setOnClickListener(deleteButtonListener);

		//add a new tag and edit buttons to queryTableLayout
		queryTableLayout.addView(newTagView,index);
	}//end makeTagGUI

	// remove all saved search Buttons from the app 
	private void clearButtons() 
	{ 
		// remove all saved search Buttons 
		queryTableLayout.removeAllViews(); 
	} // end method clearButtons 

	// create a new Button and add it to the ScrollView 
	public OnClickListener saveButtonListener = new OnClickListener()    
	{
		@Override 
		public void onClick(View v)
		{
			// create tag if both queryEditText and tagEditText are not empty 
			if (queryEditText.getText().length() > 0 && tagEditText.getText().length() > 0) 
			{ 
				makeTag(queryEditText.getText().toString(), 
						tagEditText.getText().toString()); 
				queryEditText.setText(""); // clear queryEditText 
				tagEditText.setText(""); // clear tagEditText 

				//hide the soft keyboard
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tagEditText.getWindowToken(),0);

			}//end if

			else // display message asking user to provide a query and a tag 
			{
				// create a new AlertDialog Builder
				AlertDialog.Builder builder =new AlertDialog.Builder(Main.this); 

				builder.setTitle(R.string.missingTitle); // title bar string  

				// provide an OK button that simply dismisses the dialog    
				builder.setPositiveButton(R.string.OK, null);  

				// set the message to display                               
				builder.setMessage(R.string.missingMessage);                                                                          

				// create AlertDialog from the AlertDialog.Builder          
				AlertDialog errorDialog = builder.create();                 
				errorDialog.show(); // display the Dialog  
			}//end else
		}//end method onClick
	};//end OnClickListener anonymous inner class

	// clears all saved searches 
	public OnClickListener clearTagsButtonListener = new OnClickListener()   
	{
		@Override 
		public void onClick(View v) 
		{ 
			// create a new AlertDialog Builder 
			AlertDialog.Builder builder = new AlertDialog.Builder(Main.this); 
			builder.setTitle(R.string.confirmTitle); // title bar string 
			// provide an OK button that simply dismisses the dialog 
			builder.setPositiveButton(R.string.erase,new DialogInterface.OnClickListener() 
			{
				@Override 
				public void onClick(DialogInterface dialog, int button) 
				{
					clearButtons(); // clear all saved searches from the map 
					// get a SharedPreferences.Editor to clear searches 
					SharedPreferences.Editor preferencesEditor = savedSearches.edit();
					preferencesEditor.clear();//remove all tag/query pairs
					preferencesEditor.apply();//commit the changes
				}//end method onClick
			}/*end anonymous inner class*/);//end call to method setPositiveButton
			builder.setCancelable(true);
			builder.setNegativeButton(R.string.cancel,null);

			//set the message to display
			builder.setMessage(R.string.confirmMessage);

			// create AlertDialog from the AlertDialog.Builder 
			AlertDialog confirmDialog = builder.create(); 
			confirmDialog.show(); // display the Dialog 
		}//end method onClick
	}; // end OnClickListener anonymous inner class

	// load selected search in a web browser 
	public OnClickListener queryButtonListener = new OnClickListener()    
	{
		@Override 
		public void onClick(View v) 
		{ 
			// get the query 
			String buttonText = ((Button)v).getText().toString(); 
			String query = savedSearches.getString(buttonText, null);

			//create the URL corresponding to the touched button's query
			String urlString= getString(R.string.searchURL)+query;

			//create an intent to launch a web browser
			Intent getURL= new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));

			startActivity(getURL);//execute the intent

		}//end method onClick
	};//end OnClickListener anonymous inner class


	// edit selected search 
	public OnClickListener editButtonListener = new OnClickListener()    
	{
		@Override 
		public void onClick(View v) 
		{  // get all necessary GUI components
			TableRow buttonTableRow=(TableRow)v.getParent();
			Button searchButton=(Button)buttonTableRow.findViewById(R.id.newTagButton);


			String tag = searchButton.getText().toString(); 

			// set EditTexts to match the chosen tag and query 
			tagEditText.setText(tag); 
			queryEditText.setText(savedSearches.getString(tag, null));
		}//end method onClick
	}; // end OnClickListener anonymous inner class 

	public OnClickListener deleteButtonListener = new OnClickListener()    
	{
		@Override 
		public void onClick(View v) 
		{  // get all necessary GUI components
			TableRow buttonTableRow=(TableRow)v.getParent();


			Button searchButton=(Button)buttonTableRow.findViewById(R.id.newTagButton);
			String tag = searchButton.getText().toString();
			String query = savedSearches.getString(tag, null); 
			SharedPreferences.Editor preferencesEditor = savedSearches.edit();


			preferencesEditor.remove(tag);
			preferencesEditor.remove(query);
			preferencesEditor.apply();//store the updated preferences

			queryTableLayout.removeView(buttonTableRow);
		}//end method onClick
	}; // end OnClickListener anonymous inner class
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	


}
