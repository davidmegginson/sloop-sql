package com.megginson.sloopsql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

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

}
