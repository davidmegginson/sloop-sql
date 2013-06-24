package com.megginson.sloopsql;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.megginson.sloopsql.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Activity for executing SQL queries.
 */
public class QueryActivity extends Activity
{

	public final static String QUERY_HISTORY_PROPERTY = "queryHistory";

	public final static String QUERY_TEXT_PROPERTY = "queryText";

	private Context mContext;

 	private DatabaseHandler mDatabaseHandler;

	private Set<String> mQueryHistory = new HashSet<String>();

	private AutoCompleteTextView mQueryView;

	private Button mQueryButton;

    /** 
	 * Called when the activity is first created.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);

		mContext = this;

		mDatabaseHandler = new DatabaseHandler(this);

		setup_ui();		
    }

	/**
	 * Load preferences when the activity resumes.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		// must copy - return value not safe to modify
		Set<String> history = prefs.getStringSet(QUERY_HISTORY_PROPERTY, null);
		if (history == null)
		{
			mQueryHistory = new HashSet<String>();
		}
		else
		{
			mQueryHistory = new HashSet<String>(history);
		}
		update_query_history(null);
	}

	/**
	 * Save preferences when the activity pauses.
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putStringSet(QUERY_HISTORY_PROPERTY, mQueryHistory);
		editor.commit();
	}

	/**
	 * Saved the temporary runtime state
	 */
	@Override
	protected void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		bundle.putString(QUERY_TEXT_PROPERTY, mQueryView.getText().toString());
	}

	/**
	 * Restore the temporary runtime state
	 */
	@Override
	protected void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		mQueryView.setText(bundle.getString(QUERY_TEXT_PROPERTY));
		doExecuteQuery(mQueryView);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.query_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.item_clear_history:
				doClearHistory();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
		}
	}

	/**
	 * Clear the query text field.
	 */
	public void doClearQuery(View view)
	{
		mQueryView.setText("");
	}
	
	public void doClearHistory()
	{
		mQueryHistory = new HashSet<String>();
		update_query_history(null);
		showToast(getString(R.string.message_history_cleared));
	}
	
	/**
	 * Set up the UI components of the activity.
	 */
	private void setup_ui()
	{
		mQueryButton = (Button)findViewById(R.id.button_query);

		mQueryView = (AutoCompleteTextView)findViewById(R.id.input_query);
		mQueryView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView view, int actionId, KeyEvent event)
				{
					if (actionId == EditorInfo.IME_NULL)
					{
						mQueryButton.performClick();
						return true;
					}
					else
					{
						return false;
					}
				}
			});
	}

	/**
	 * Update the query history for autocomplete.
	 *
	 * If the parameter is not null, add it to the history first; otherwise,
	 * just set the history from the mQueryHistory list.
	 */
	private void update_query_history(String entry)
	{
		if (entry != null)
		{
			mQueryHistory.add(entry);
		}
		ArrayAdapter<String> adapter = 
			new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
									 new ArrayList<String>(mQueryHistory));
		mQueryView.setAdapter(adapter);
	}
	
	private void showToast(String message)
	{
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Task for running a database query in the background.
	 */
	private class QueryTask extends AsyncTask<String, Integer, AsyncResult<Cursor>>
	{
		
		private String mQueryText;

		/**
		 * Run the SQL query (called from a background thread)
		 */
		@Override
		protected AsyncResult<Cursor> doInBackground(String ... queries)
		{
			Cursor cursor = null;
			SQLiteDatabase database = mDatabaseHandler.getWritableDatabase();
			try
			{
				for (int i = 0; i < queries.length; i++)
				{
					cursor = database.rawQuery(queries[i], null);
					// save the successful query text
					mQueryText = queries[i];
				} 
				return new AsyncResult<Cursor>(cursor);
			}
			catch (Throwable t)
			{
				return new AsyncResult<Cursor>(t);
			}
		}

		/**
		 * Handle the SQL result (called from the main UI thread)
		 */
		@Override
		protected void onPostExecute(AsyncResult<Cursor> result)
		{
			if (result.isError())
			{
				showToast(result.getThrowable().getMessage());
				return;
			}
			Cursor cursor = result.getResult();
			update_query_history(mQueryText);
			LinearLayout headerView  = (LinearLayout)findViewById(R.id.layout_header);
			ListView resultsView = (ListView)findViewById(R.id.list_results);
			TextView messageView = (TextView)findViewById(R.id.text_message);
			headerView.removeAllViews();
			if (cursor.moveToNext())
			{
				for (int i = 0; i < cursor.getColumnCount(); i++)
				{
					TextView header = (TextView)Util.inflate(headerView.getContext(), R.layout.table_header);
					header.setText(cursor.getColumnName(i));
					headerView.addView(header);
				}	
			}

			messageView.setText("Returned " + cursor.getCount() + " rows");
			resultsView.setAdapter(new QueryResultAdapter(cursor));
		}

	}

}
