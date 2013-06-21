package com.megginson.sloopsql;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;

/**
 * Activity for executing SQL queries.
 */
public class QueryActivity extends Activity
{

 	private DatabaseHandler mDatabase;
	
	private List<String> mQueryHistory = new ArrayList<String>();

    /** 
	 * Called when the activity is first created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);
		mDatabase = new DatabaseHandler(this);
		update_history(null);
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
		AutoCompleteTextView queryView = (AutoCompleteTextView)findViewById(R.id.input_query);
		LinearLayout headerView  = (LinearLayout)findViewById(R.id.layout_header);
		ListView resultsView = (ListView)findViewById(R.id.list_results);
		TextView messageView = (TextView)findViewById(R.id.text_message);

	 	SQLiteDatabase db = mDatabase.getWritableDatabase();

		try
		{
			String queryText = queryView.getText().toString();
			Cursor cursor = db.rawQuery(queryText, null);
			update_history(queryText);
			
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
	
	private void update_history(String entry)
	{
		AutoCompleteTextView queryView = (AutoCompleteTextView)findViewById(R.id.input_query);
		queryView.setThreshold(1);
		
		if (entry != null && !mQueryHistory.contains(entry)) {
			mQueryHistory.add(entry);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mQueryHistory);
		queryView.setAdapter(adapter);
		
	}
}
