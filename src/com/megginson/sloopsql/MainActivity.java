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
	
	private Menu mOptionsMenu;

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
		mOptionsMenu = menu;
		refresh_options_menu();
		return true;		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.item_add_query:
				do_add_query_tab();
				return true;
			case R.id.item_close_tab:
				do_close_current_tab();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Add a new query tab
	 */
	private void do_add_query_tab()
	{
		String tag = "query" + mQueryCounter;
		ActionBar.Tab tab = mActionBar.newTab()
			.setText("Query " + (mQueryCounter + 1))
			.setTabListener(new TabListener(this, tag, new QueryFragment()));
		mActionBar.addTab(tab);
		tab.select();
		refresh_options_menu();
		mQueryCounter++;
	}

	/**
	* Close the current tab.
	*/
	private void do_close_current_tab()
	{
		ActionBar.Tab currentTab = mActionBar.getSelectedTab();
		if (currentTab != null) {
			mActionBar.removeTab(currentTab);
		}
		refresh_options_menu();
	}
	
	/**
	* Refresh the options menu based on current tab state.
	*/
	private void refresh_options_menu()
	{
		MenuItem closeTabItem = mOptionsMenu.findItem(R.id.item_close_tab);
		if (closeTabItem != null)
		{
			if (mActionBar.getTabCount() > 0) {
				closeTabItem.setVisible(true);
			} else {
				closeTabItem.setVisible(false);
			}
			invalidateOptionsMenu();
		}
	}
}
