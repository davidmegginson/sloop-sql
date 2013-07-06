package com.megginson.sloopsql;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Dialog fragment to display a list of tables.
 */
public class TableListFragment extends DialogFragment
{

	//
	// Internal fragment state
	//

	/**
	 * The fragment's root view. set in {@link #onCreateView}
	 */
 	private ViewGroup mFragmentView;

	/**
	 * The database to query.
	 */
	private SQLiteDatabase mDatabase;

	/**
	 * Any existing query result.
	 */
	private Cursor mCursor;


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
		
		mDatabase = new DatabaseHandler(getActivity()).getReadableDatabase();
	}

	/**
	 * Lifecycle event: fragment destroyed.
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if (mCursor != null)
		{
			mCursor.close();
			mCursor = null;
		}

		if (mDatabase != null)
		{
			mDatabase.close();
			mDatabase = null;
		}
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
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		// Generate a list of tables
		do_list_tables();
	}


	//
	// Actions
	//

	/**
	 * Show a list of tables and views.
	 */
	private void do_list_tables()
	{
		try
		{
			mCursor = mDatabase.rawQuery("select type, name from sqlite_master where type in ('table', 'view')", null);
			get_list_view().setAdapter(new TableListAdapter(mCursor));
		}
		catch (Throwable t)
		{
			Util.toast(getActivity(), t.getMessage());
		}
	}
	
	/**
	 * Select a table.
	 */
	 private void do_select_table_row(int position)
	 {
		 Util.toast(getActivity(), "Select table @ " + position);
		 dismiss();
	 }


	//
	// UI methods
	//

	/**
	 * Set up the UI components of the activity.
	 */
	private void setup_ui()
	{
		getDialog().setTitle(R.string.title_table_list);
		get_list_view().setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				do_select_table_row(position);
			}
		});
	}

	/**
	 * Get the table list view.
	 */
	private ListView get_list_view()
	{
		return (ListView)mFragmentView.findViewById(R.id.table_list);
	}

}
