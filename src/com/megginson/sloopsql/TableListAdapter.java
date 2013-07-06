package com.megginson.sloopsql;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Display a row in a list of database tables.
 */
public class TableListAdapter extends BaseAdapter implements ListAdapter
{

	private Cursor mCursor;

	/**
	 * The adapter always wraps a cursor.
	 */
	public TableListAdapter(Cursor cursor)
	{
		super();
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
		mCursor.moveToPosition(position);

		ViewGroup rowView = (ViewGroup)convertView;
		if (rowView == null)
		{
			rowView = (ViewGroup)Util.inflate(parent.getContext(), R.layout.table_list_row);
		}

		TextView typeView = (TextView)rowView.findViewById(R.id.table_type);
		TextView nameView = (TextView)rowView.findViewById(R.id.table_name);

		typeView.setText(mCursor.getString(mCursor.getColumnIndex("type")));
		nameView.setText(mCursor.getString(mCursor.getColumnIndex("name")));

		return rowView;
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
