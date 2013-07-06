package com.megginson.sloopsql;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;

/**
 * Dialog fragment to display a list of tables.
 */
public class TableListFragment extends DialogFragment
{
 
 	public static  TableListFragment newInstance()
	{
		return new TableListFragment();
	}

	//
	// Internal fragment state
	//

	/**
	 * The fragment's root view. set in {@link #onCreateView}
	 */
 	private ViewGroup mFragmentView;


	//
	// Fragment lifecycle methods
	//

	/**
	 * Lifecycle event: fragment first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
	}

	/**
	 * Lifecycle event: fragment destroyed.
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	/**
	 * Lifecycle event: fragment creates or recreates its view.
	 */
	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
	{
		mFragmentView = (ViewGroup)inflator.inflate(R.layout.table_list, container, false);
		setup_ui();
		return mFragmentView;
	}


	//
	// UI methods
	//

	/**
	 * Set up the UI components of the activity.
	 */
	private void setup_ui()
	{
		String items[] = {
			"a", "b", "c"
		};
		
		ListView tableList = get_list_view();
		tableList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.cell, items));
	}

	/**
	 * Get the table list view.
	 */
	ListView get_list_view()
	{
		return (ListView)mFragmentView.findViewById(R.id.table_list);
	}

}
