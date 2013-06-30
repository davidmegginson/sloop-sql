package com.megginson.sloopsql;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.megginson.sloopsql.R;
import java.util.ArrayList;
import android.widget.Toast;
import java.util.List;

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
		
		restore_tabs(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState)
	{
		save_tabs(savedInstanceState);
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
		ActionBar.Tab tab = add_fragment_tab(tag, new QueryFragment());
		tab.select();
		mQueryCounter++;
	}

	private ActionBar.Tab add_fragment_tab(String tag, Fragment fragment)
	{
		TabListener listener = new TabListener(this, R.id.fragment_container, tag, new QueryFragment());
		ActionBar.Tab tab = mActionBar.newTab()
			.setText("Query " + (mQueryCounter + 1))
			.setTabListener(listener);
		// there's no getTabListener() method, so save a copy here
		tab.setTag(listener);
		mActionBar.addTab(tab);
		refresh_options_menu();
		return tab;
	}

	/**
	 * Close the current tab.
	 */
	private void do_close_current_tab()
	{
		ActionBar.Tab currentTab = mActionBar.getSelectedTab();
		if (currentTab != null)
		{
			mActionBar.removeTab(currentTab);
		}
		refresh_options_menu();
	}
	
	private void restore_tabs(Bundle savedInstanceState)
	{
		
	}
	
	private void save_tabs(Bundle savedInstanceState)
	{
		ArrayList<String> fragmentTags = new ArrayList<String>();
		ArrayList<String> TabTitles = new ArrayList<String>();
		for(int i = 0; i < mActionBar.getTabCount(); i++) {
			ActionBar.Tab tab = mActionBar.getTabAt(i);
			TabListener listener = (TabListener)tab.getTag();
			fragmentTags.add(listener.getTag());
			TabTitles.add(tab.getText().toString());
		}
		savedInstanceState.putInt("queryCounter", mQueryCounter);
		savedInstanceState.putInt("selectedTab", mActionBar.getSelectedNavigationIndex());
		savedInstanceState.putStringArrayList("tabTitles", TabTitles);
		savedInstanceState.putStringArrayList("fragmentTags", fragmentTags);
	}

	/**
	 * Refresh the options menu based on current tab state.
	 */
	private void refresh_options_menu()
	{
		MenuItem closeTabItem = mOptionsMenu.findItem(R.id.item_close_tab);
		if (closeTabItem != null)
		{
			if (mActionBar.getTabCount() > 0)
			{
				closeTabItem.setVisible(true);
			}
			else
			{
				closeTabItem.setVisible(false);
			}
			invalidateOptionsMenu();
		}
	}
}
