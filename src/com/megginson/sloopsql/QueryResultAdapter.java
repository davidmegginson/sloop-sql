package com.megginson.sloopsql;

import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QueryResultAdapter extends BaseAdapter implements ListAdapter
{
	
	private Cursor mCursor;

	public QueryResultAdapter(Cursor cursor)
	{
		mCursor = cursor;
	}
	
	@Override
	public Object getItem(int position)
	{
		return mCursor.toString();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout layout = new LinearLayout(parent.getContext());
		mCursor.moveToPosition(position);
		for (int i = 0; i < mCursor.getColumnCount(); i++) {
			TextView cell = new TextView(parent.getContext());
			cell.setText(mCursor.getString(i));
			layout.addView(cell);
		}
		return layout;
	}
	
	@Override
	public long getItemId(int position)
	{
		return mCursor.hashCode();
	}
	
	@Override
	public int getCount()
	{
		return mCursor.getCount();
	}

}
