package com.megginson.sloopsql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Toast;

public final class Util
{

	/**
	 * Find and inflate a layout.
	 */
	public static View inflate(Context context, int id)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(id, null);
	}
	
	public static void toast(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public static String makeStackTrace(Throwable t)
	{
		StringBuffer s = new StringBuffer();
		for (StackTraceElement e : t.getStackTrace())
		{
			s.append(e.toString());
			s.append('\n');
		}
		return s.toString();
	}

}
