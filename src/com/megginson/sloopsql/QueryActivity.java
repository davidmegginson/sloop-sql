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

/**
 Activity for executing SQL queries.
 */
public class QueryActivity extends Activity
{

 	private DatabaseHandler mDatabase;

    /** 
	 Called when the activity is first created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);
		mDatabase = new DatabaseHandler(this);
    }

	/**
	 Event: execute a SQL query.

	 Respond to a button press to execute the query in the query field and 
	 show the results below.

	 @param view The button that triggered the event.
	 */
	public void doExecuteQuery(View view)
	{
		EditText queryView = (EditText)findViewById(R.id.input_query);
		TableLayout tableLayout = (TableLayout)findViewById(R.id.table_results);
		TextView resultsView = (TextView)findViewById(R.id.text_message);

	 	SQLiteDatabase db = mDatabase.getWritableDatabase();

		try
		{
			String queryText = queryView.getText().toString();
			Cursor cursor = db.rawQuery(queryText, null);
			resultsView.setText("Returned " + cursor.getCount() + " rows");

			tableLayout.removeAllViews();
			
			// set the headers
			TableRow headers = new TableRow(this);
			for (int i = 0; i < cursor.getColumnCount(); i++)
			{
				TextView header = new TextView(this);
				header.setBackgroundColor(Color.GRAY);
				header.setText(cursor.getColumnName(i));
				headers.addView(header);
			}
			tableLayout.addView(headers);
			
			// fill in the contents
			while (cursor.moveToNext())
			{
				TableRow row = new TableRow(this);
				for (int i = 0; i < cursor.getColumnCount(); i++)
				{
					TextView cell = new TextView(this);
					cell.setText(cursor.getString(i));
					row.addView(cell);
				}
				tableLayout.addView(row);
			}
		}
		catch (Exception e)
		{
			resultsView.setText(e.getMessage());
		}
		finally
		{
			db.close();
		}
	}
}
