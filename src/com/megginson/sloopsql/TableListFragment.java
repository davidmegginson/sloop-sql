package com.megginson.sloopsql;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dialog fragment to display a list of tables.
 */
public class TableListFragment extends DialogFragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
	{
		return inflator.inflate(R.layout.table_list, container, false);
	}

}
