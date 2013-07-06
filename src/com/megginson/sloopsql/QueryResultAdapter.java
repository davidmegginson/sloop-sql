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
 * Display a row of a query result in a ListView
 */
public class QueryResultAdapter extends BaseAdapter implements ListAdapter
{

	private Cursor mCursor;

	/**
	 * The adapter always wraps a cursor.
	 */
	public QueryResultAdapter(Cursor cursor)
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
			rowView = make_row_view(parent.getContext(), mCursor.getColumnCount());
		}
		
		for (int i = 0; i < mCursor.getColumnCount(); i++)
		{
			TextView cell = (TextView)rowView.getChildAt(i);
			cell.setText(mCursor.getString(i));
		}
		
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

	/**
	 * Create an empty view for a result row.
	 */
	private ViewGroup make_row_view(Context context, int columnCount)
	{
		LinearLayout layout = new LinearLayout(context);
		for (int i = 0; i < mCursor.getColumnCount(); i++)
		{
			layout.addView(Util.inflate(context, R.layout.cell));
		}
		return layout;
	}

}
