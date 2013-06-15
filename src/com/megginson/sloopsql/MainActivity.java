package com.megginson.sloopsql;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	/**
	  Event: execute a SQL query.
	 */
	public void doExecuteQuery(View view)
	{
		EditText queryView = (EditText)findViewById(R.id.input_query);
		String queryText = queryView.getText().toString();

		TextView resultsView = (TextView)findViewById(R.id.text_results);
		resultsView.setText(queryText);
	}
}
