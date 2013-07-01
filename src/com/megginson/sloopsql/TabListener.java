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
	private final Activity mActivity;
	private final int mParentId;
	private final Fragment mFragment;
	private boolean mIsDetached = false;

	/** 
	 * Construct a new tab-content instance.
	 *
	 * @param activity  The host Activity, used to instantiate the fragment
	 * @param fragmentClass  The fragment's Class, used to instantiate the fragment
	 */
	public TabListener(Activity activity, int parentId, Fragment fragment)
	{
		mActivity = activity;
		mParentId = parentId;
		mFragment = fragment;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		if (mIsDetached)
		{
			ft.attach(mFragment);
		}
		else
		{
			ft.replace(mParentId, mFragment);
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		ft.detach(mFragment);
		mIsDetached = true;
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		// User selected the already selected tab. Usually do nothing.
	}

	/**
	 * Return the fragment.
	 */
	public Fragment getFragment()
	{
		return mFragment;
	}

}

