package com.megginson.sloopsql;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import android.content.SharedPreferences;

/**
 * Activity for executing SQL queries.
 */
public class QueryActivity extends Activity
{

 	private DatabaseHandler mDatabase;

	private Set<String> mQueryHistory = new HashSet<String>();
	
	private AutoCompleteTextView mQueryView;
	

    /** 
	 * Called when the activity is first created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);
		mDatabase = new DatabaseHandler(this);
		
		SharedPreferences prefs = getPreferences(0);
		mQueryHistory = prefs.getStringSet("queryHistory", mQueryHistory);
		
		mQueryView = (AutoCompleteTextView)findViewById(R.id.input_query);
		
		update_query_history(null);
    }
	
	/**
	 * Save preferences when the activity stops.
	 */
	public void onStop()
	{
		super.onStop();
		
		SharedPreferences prefs = getPreferences(0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putStringSet("queryHistory", mQueryHistory);
		editor.commit();
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		bundle.putString("queryText", mQueryView.getText().toString());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle bundle)
	{
		mQueryView.setText(bundle.getString("queryText"));
		doExecuteQuery(mQueryView);
	}

	/**
	 * Event: execute a SQL query.
	 *
	 * Respond to a button press to execute the query in the query field and 
	 * show the results below.
	 *
	 * @param view The button that triggered the event.
	 */
	public void doExecuteQuery(View view)
	{
		LinearLayout headerView  = (LinearLayout)findViewById(R.id.layout_header);
		ListView resultsView = (ListView)findViewById(R.id.list_results);
		TextView messageView = (TextView)findViewById(R.id.text_message);

	 	SQLiteDatabase db = mDatabase.getWritableDatabase();

		try
		{
			String queryText = mQueryView.getText().toString();
			if (queryText == null || queryText.length() == 0)
			{
				return;
			}
			Cursor cursor = db.rawQuery(queryText, null);
			update_query_history(queryText);

			headerView.removeAllViews();
			if (cursor.moveToNext())
			{
				for (int i = 0; i < cursor.getColumnCount(); i++)
				{
					TextView header = (TextView)Util.inflate(headerView, R.layout.table_header);
					header.setText(cursor.getColumnName(i));
					headerView.addView(header);
				}	
			}

			messageView.setText("Returned " + cursor.getCount() + " rows");
			resultsView.setAdapter(new QueryResultAdapter(cursor));
		}
		catch (Exception e)
		{
			messageView.setText(e.getMessage());
		}
		finally
		{
			db.close();
		}
	}

	/**
	 * Update the query history for autocomplete.
	 *
	 * If the parameter is not null, add it to the history first; otherwise,
	 * just set the history from the mQueryHistory list.
	 */
	private void update_query_history(String entry)
	{
		mQueryView.setThreshold(1);

		if (entry != null)
		{
			mQueryHistory.add(entry);
		}
		ArrayAdapter<String> adapter = 
			new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
			new ArrayList<String>(mQueryHistory));
		mQueryView.setAdapter(adapter);

	}
}
