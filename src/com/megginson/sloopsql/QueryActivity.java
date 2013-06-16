package com.megginson.sloopsql;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.SQLException;

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
        setContentView(R.layout.main);
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
		TextView resultsView = (TextView)findViewById(R.id.text_results);

	 	SQLiteDatabase db = mDatabase.getWritableDatabase();
		
		try
		{
			String queryText = queryView.getText().toString();
			Cursor cursor = db.rawQuery(queryText, null);
			resultsView.setText("Returned " + cursor.getCount() + " rows");
		}
		catch (SQLException e)
		{
			resultsView.setText(e.getMessage());
		}
		finally
		{
			db.close();
		}
	}
}
