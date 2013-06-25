package com.megginson.sloopsql;
import android.database.Cursor;
import java.io.Writer;
import java.io.IOException;

/**
 * Write a cursor's contents out as CSV.
 */
public class CSVCursorSerializer
{

	private Cursor mCursor;

	public CSVCursorSerializer(Cursor cursor)
	{
		mCursor = cursor;
	}

	public void serialize(Writer output) throws IOException
	{
		serialize_headers(output);
		mCursor.moveToPosition(-1);
		while (mCursor.moveToNext())
		{
			serialize_row(output);
		}
	}

	private void serialize_headers(Writer output) throws IOException
	{
		for (int i = 0; i < mCursor.getColumnCount(); i++)
		{
			if (i > 0)
			{
				output.write(',');
			}
			output.write(escape_item(mCursor.getColumnName(i)));
		}
		output.write("\r\n");
	}

	private void serialize_row(Writer output) throws IOException
	{
		for (int i = 0; i < mCursor.getColumnCount(); i++)
		{
			if (i > 0)
			{
				output.write(',');
			}
			output.write(escape_item(mCursor.getString(i)));
		}
		output.write("\r\n");
	}

	private String escape_item(String s)
	{
		if (s.indexOf(',') > -1 || s.indexOf('"') > -1 || s.indexOf('\r') > -1 || s.indexOf('\n') > -1)
		{
			if (s.indexOf('"') > -1)
			{
				s = s.replace("\"", "\"\"");
			}
			return '"' + s + '"';
		}
		else
		{
			return s;
		}
	}

}
