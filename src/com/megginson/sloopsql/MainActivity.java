package com.megginson.sloopsql;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.megginson.sloopsql.R;

/**
 * Main container activity for the UI.
 *
 * This activity manages the various fragments that make up the app.
 */
public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		final ActionBar actionBar = getActionBar();

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < 3; i++) {
			actionBar.addTab(
                actionBar.newTab()
				.setText("Tab " + (i + 1))
				.setTabListener(new TabListener<QueryFragment>(this, "query" + i, QueryFragment.class)));
		}
	}

}
