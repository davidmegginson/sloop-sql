package com.megginson.sloopsql;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.megginson.sloopsql.R;

/**
 * Main container activity for the UI.
 *
 * This activity manages the various fragments that make up the app.
 */
public class MainActivity extends Activity
{

 	private ActionBar mActionBar;

	private int mQueryCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mActionBar = getActionBar();

		// Specify that tabs should be displayed in the action bar.
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.item_add_query:
				doAddQueryTab();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Add a new query tab
	 */
	private void doAddQueryTab()
	{
		String tag = "query" + mQueryCounter;
		ActionBar.Tab tab = mActionBar.newTab()
			.setText("Query " + (mQueryCounter + 1))
			.setTabListener(new TabListener<QueryFragment>(this, tag, QueryFragment.class));
		mActionBar.addTab(tab);
		tab.select();
		mQueryCounter++;
	}

}
