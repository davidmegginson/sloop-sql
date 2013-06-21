package com.megginson.sloopsql;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
		for (int i = 0; i < mCursor.getColumnCount(); i++)
		{
			TextView cell = (TextView)inflate(parent, R.layout.table_cell);
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

	private View inflate(ViewGroup parent, int id)
	{
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		return inflater.inflate(id, null);
	}

}
