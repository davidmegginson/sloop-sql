package com.megginson.sloopsql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class Util
{

	/**
	 * Find and inflate a layout.
	 */
	public static View inflate(ViewGroup parent, int id)
	{
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		return inflater.inflate(id, null);
	}

}
