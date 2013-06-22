package com.megginson.sloopsql;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.os.AsyncTask;

/**
 * Activity for executing SQL queries.
 */
public class QueryActivity extends Activity
{

	public final static String QUERY_HISTORY_PROPERTY = "queryHistory";

	public final static String QUERY_TEXT_PROPERTY = "queryText";


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
		mQueryHistory = prefs.getStringSet(QUERY_HISTORY_PROPERTY, mQueryHistory);

		mQueryView = (AutoCompleteTextView)findViewById(R.id.input_query);

		update_query_history(null);
    }

	/**
	 * Save preferences when the activity stops.
	 */
	@Override
	public void onStop()
	{
		super.onStop();

		SharedPreferences prefs = getPreferences(0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putStringSet(QUERY_HISTORY_PROPERTY, mQueryHistory);
		editor.commit();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		bundle.putString(QUERY_TEXT_PROPERTY, mQueryView.getText().toString());
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle)
	{
		mQueryView.setText(bundle.getString(QUERY_TEXT_PROPERTY));
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
		String queryText = mQueryView.getText().toString();

		if (queryText != null && queryText.length() > 0)
		{
			new QueryTask().execute(queryText);
			update_query_history(queryText);
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

	/**
	 * Task for running a database query in the background.
	 */
	private class QueryTask extends AsyncTask<String, Integer, Cursor>
	{

		@Override
		protected Cursor doInBackground(String ... queries)
		{
			Cursor cursor = null;
			SQLiteDatabase database = mDatabase.getWritableDatabase();
			for (int i = 0; i < queries.length; i++)
			{
				cursor = database.rawQuery(queries[i], null);
			} 
			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor cursor)
		{
			LinearLayout headerView  = (LinearLayout)findViewById(R.id.layout_header);
			ListView resultsView = (ListView)findViewById(R.id.list_results);
			TextView messageView = (TextView)findViewById(R.id.text_message);
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

	}

}
