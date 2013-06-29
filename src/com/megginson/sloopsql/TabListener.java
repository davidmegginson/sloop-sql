package com.megginson.sloopsql;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * A single tab-content instance.
 *
 * This class represents both the instance of a single tab's content, and
 * the listeners to respond to tabbar events.
 *
 * Note that the handle currently places the tab instance in the activity root,
 * replacing the activity's previous layout.  I will need to change this once
 * we have a multi-pane layout.
 *
 * Adapted from the example at 
 * http://developer.android.com/guide/topics/ui/actionbar.html
 */
public class TabListener implements ActionBar.TabListener
{
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;

	/** 
	 * Construct a new tab-content instance.
	 *
	 * @param activity  The host Activity, used to instantiate the fragment
	 * @param tag  The identifier tag for the fragment
	 * @param fragmentClass  The fragment's Class, used to instantiate the fragment
	 */
	public TabListener(Activity activity, String tag, Fragment fragment)
	{
		mActivity = activity;
		mTag = tag;
		mFragment = fragment;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		ft.replace(android.R.id.content, mFragment, mTag);
		ft.attach(mFragment);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		if (mFragment != null)
		{
			// Detach the fragment, because another one is being attached
			ft.detach(mFragment);
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		// User selected the already selected tab. Usually do nothing.
	}
}

