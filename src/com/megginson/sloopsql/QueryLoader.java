package com.megginson.sloopsql;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;

/**
 * Execute a SQL query as a background task.
 */
public class QueryLoader extends AsyncTaskLoader<Cursor>
{
 
 	private SQLiteDatabase mDatabase;
	
	private String mQueryString;
	
	private Cursor mCursor;

	public QueryLoader(Context context, SQLiteDatabase database, String queryString)
	{
		super(context);
		mDatabase = database;
		mQueryString = queryString;
	}
	
	@Override
	public Cursor loadInBackground()
	{
		mCursor = mDatabase.rawQuery(mQueryString, null);
		return mCursor;
	}
	
}
