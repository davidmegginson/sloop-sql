package com.megginson.sloopsql;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHandler extends SQLiteOpenHelper
{

	public DatabaseHandler(Context context)
	{
		super(context, "SloopSQL", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) 
	{
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
	}

}
