package com.megginson.sloopsql;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.graphics.Color;
import android.widget.ListView;
import android.widget.LinearLayout;

/**
 * Activity for executing SQL queries.
 */
public class QueryActivity extends Activity
{

 	private DatabaseHandler mDatabase;

    /** 
	 * Called when the activity is first created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);
		mDatabase = new DatabaseHandler(this);
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
		EditText queryView = (EditText)findViewById(R.id.input_query);
		LinearLayout headerView  = (LinearLayout)findViewById(R.id.layout_header);
		ListView resultsView = (ListView)findViewById(R.id.list_results);
		TextView messageView = (TextView)findViewById(R.id.text_message);

	 	SQLiteDatabase db = mDatabase.getWritableDatabase();

		try
		{
			String queryText = queryView.getText().toString();
			Cursor cursor = db.rawQuery(queryText, null);
			
			headerView.removeAllViews();
			if (cursor.moveToNext())
			{
				for (int i = 0; i < cursor.getColumnCount(); i++)
				{
					TextView header = new TextView(this);
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
	
}
