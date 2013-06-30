package com.megginson.sloopsql;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.megginson.sloopsql.R;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Activity for executing SQL queries.
 */
public class QueryFragment extends Fragment
{

	public final static String QUERY_HISTORY_PROPERTY = "queryHistory";

	public final static String QUERY_TEXT_PROPERTY = "queryText";

	private View mFragmentView;

 	private DatabaseHandler mDatabaseHandler;

	private SQLiteDatabase mDatabase;

	private Cursor mCursor;

	private String mQueryText;

	private Set<String> mQueryHistory = new HashSet<String>();

	private AutoCompleteTextView mQueryView;

	private Button mQueryButton;


	//
	// Lifecycle methods
	//

    /** 
	 * Lifecycle event: fragment first created.
	 *
	 * The argument contains the saved state if Android is restoring
	 * a previously-existing version of this fragment, or null if it's
	 * creating the fragment from scratch.
	 *
	 * @param savedInstance the fragment's saved state, or null. 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		mDatabaseHandler = new DatabaseHandler(getActivity());
		mDatabase = mDatabaseHandler.getWritableDatabase();

		if (savedInstanceState != null)
		{
			mQueryText = savedInstanceState.getString(QUERY_TEXT_PROPERTY);
		}
    }

	/**
	 * Lifecycle event: fragment destroyed.
	 *
	 * Free up any database resources we're using.
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if (mCursor != null)
		{
			mCursor.close();
			mCursor = null;
		}
		if (mDatabase != null)
		{
			mDatabase.close();
			mDatabase = null;
		}
		if (mDatabaseHandler != null)
		{
			mDatabaseHandler.close();
			mDatabaseHandler = null;
		}
	}

	/**
	 * Lifecycle event: parent activity created.
	 *
	 * This is where we have to set our persistent state (for orientation
	 * changes, etc.) using {@link #setRetainInstance(boolean)}. This
	 * setting lets Android preserve some of the fragment's internal
	 * state (but not its UI).
	 *
	 * @param savedInstanceState a bundle for saving any instance-specific
	 * configuration.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);;
		setRetainInstance(true);
	}

	/**
	 * Lifecycle event: fragment needs to (re)draw its view.
	 *
	 * Android will set the savedInstanceState to null if it's calling
	 * this method right after {@link #onCreate(Bundle)}, or if it's
	 * redrawing a fragment that has set itself to persistent using
	 * {@link #setRetainInstance(boolean)}. After e.g. orientation
	 * change, Android can ask the fragment to redraw itself, and if
	 * the saved state is null, it's up to the fragment to repopulate
	 * its UI components from internal state.
	 *
	 * @param inflater The parent activity's inflater, for building
	 * the layout from XML resource files.
	 * @param container The parent activity's {@link ViewGroup} that
	 * will hold the fragment.
	 * @param savedInstance the fragment's saved state, or null. 
	 * @return The root of the fragment's UI.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mFragmentView = inflater.inflate(R.layout.query, container, false);

		setup_ui();

		return mFragmentView;
	}

	/**
	 * Lifecycle event: fragment is resuming (or starting for first time).
	 *
	 * This is where we read our permanent (non-instance-specific)
	 * configuration from saved preferences.
	 */
	@Override
	public void onResume()
	{
		super.onResume();

		do_execute_query();

		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
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
	 * Lifecycle event: fragment is pausing (maybe permanently).
	 *
	 * This is where we save our permanent (non-instance-specific)
	 * configuration in shared preferences.
	 */
	@Override
	public void onPause()
	{
		super.onPause();
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putStringSet(QUERY_HISTORY_PROPERTY, mQueryHistory);
		editor.commit();
	}

	/**
	 * Lifecycle event: Android wants us to save the instance state.
	 *
	 * Android might use the state to create a new copy of the fragment
	 * later.
	 *
	 * @param savedInstanceState a bundle where we can save the
	 * fragment's current temporary (instance-specific) state.
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putString(QUERY_TEXT_PROPERTY, mQueryView.getText().toString());
	}

	/**
	 * Lifecycle event: Android is creating the options menu.
	 *
	 * This is where the fragment has the chance to contribute
	 * extra items.
	 *
	 * @param menu The menu to which we can add items.
	 * @param inflator An inflator for XML-based menu resources.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.query_menu, menu);
	}

	/**
	 * Lifecycle event: the user has selected a menu item.
	 *
	 * We have an opportunity to intercept it.
	 *
	 * @param item The menu item selected.
	 * @return true if we have handled the item; false otherwise.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.item_clear_history:
				do_clear_history();
				return true;
			case R.id.item_share:
				do_share();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	//
	// Callbacks for actions a user has performed in the UI
	//

	/**
	 * Action: execute a SQL query.
	 *
	 * Use the text content of {@link #mQueryView} for the query and assign
	 * to {@link #mQueryText}
	 */
	private void do_execute_query()
	{		
		mQueryText = mQueryView.getText().toString();

		if (mQueryText != null && mQueryText.length() > 0)
		{
			new QueryTask().execute(mQueryText);
		}
	}

	/**
	 * Action: clear the query text field.
	 *
	 * Sets {@link #mQueryText} and the contents of {@link #mQueryView} to
	 * the empty string.
	 */
	private void do_clear_query(View view)
	{
		mQueryText = "";
		mQueryView.setText(mQueryText);

	}

	/**
	 * Action: clear query history.
	 */
	private void do_clear_history()
	{
		mQueryHistory = new HashSet<String>();
		update_query_history(null);
		Util.toast(getActivity(), getString(R.string.message_history_cleared));
	}

	/**
	 * Action: share the current query results
	 *
	 * Offers to share the current results as a text/csv file
	 * named "sloopsql-results.csv".
	 */
	private void do_share()
	{
		try
		{
			// FIXME - make unique
			// FIXME - need to clean up old files
			String filename = "sloopsql-results.csv";
			Writer output = new OutputStreamWriter(getActivity().openFileOutput(filename, Context.MODE_WORLD_READABLE));
			new CSVCursorSerializer(mCursor).serialize(output);
			output.close();

			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getActivity().getFileStreamPath(filename)));
			sendIntent.setType("text/csv");
			startActivity(sendIntent);
		}
		catch (Throwable t)
		{
			Util.toast(getActivity(), t.getMessage());
		}
	}
	
	
	//
	// Internal utility methods
	//

	/**
	 * Set up the UI components of the activity.
	 */
	private void setup_ui()
	{
		mQueryButton = (Button)mFragmentView.findViewById(R.id.button_query);
		mQueryButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view)
				{
					do_execute_query();
				}
			});

		View clearButton = mFragmentView.findViewById(R.id.button_clear);
		clearButton.setOnClickListener(new View.OnClickListener(){
				public void onClick(View view)
				{
					do_clear_query(view);
				}
			});

		mQueryView = (AutoCompleteTextView)mFragmentView.findViewById(R.id.input_query);
		mQueryView.setText(mQueryText);
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
			new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, 
									 new ArrayList<String>(mQueryHistory));
		mQueryView.setAdapter(adapter);
	}

	/**
	 * Set the fragment's database-results cursor.
	 *
	 * Close any existing cursor first, to avoid leaking resources.
	 *
	 * @param cursor The new cursor to set.
	 */
	private void set_cursor(Cursor cursor)
	{
		if (mCursor != null)
		{
			mCursor.close();
		}
		mCursor = cursor;
	}
	
	
	//
	// Internal helper classes
	//

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
			try
			{
				for (int i = 0; i < queries.length; i++)
				{
					cursor = mDatabase.rawQuery(queries[i], null);
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
				Util.toast(getActivity(), result.getThrowable().getMessage());
				return;
			}

			// stores in mCursor after closing any old one
			set_cursor(result.getResult());
			if (mCursor == null)
			{
				return;
			}

			update_query_history(mQueryText);

			LinearLayout headerView  = (LinearLayout)mFragmentView.findViewById(R.id.layout_header);
			ListView resultsView = (ListView)mFragmentView.findViewById(R.id.list_results);
			TextView messageView = (TextView)mFragmentView.findViewById(R.id.text_message);

			headerView.removeAllViews();
			if (mCursor.moveToNext())
			{
				for (int i = 0; i < mCursor.getColumnCount(); i++)
				{
					TextView header = (TextView)Util.inflate(headerView.getContext(), R.layout.table_header);
					header.setText(mCursor.getColumnName(i));
					headerView.addView(header);
				}	
			}

			messageView.setText(String.format(getString(R.string.message_query_result), mCursor.getCount()));
			resultsView.setAdapter(new QueryResultAdapter(mCursor));
		}

	}

}

