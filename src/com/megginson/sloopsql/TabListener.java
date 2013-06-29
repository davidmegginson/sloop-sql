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
public class TabListener<T extends Fragment> implements ActionBar.TabListener
{
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	/** 
	 * Construct a new tab-content instance.
	 *
	 * @param activity  The host Activity, used to instantiate the fragment
	 * @param tag  The identifier tag for the fragment
	 * @param fragmentClass  The fragment's Class, used to instantiate the fragment
	 */
	public TabListener(Activity activity, String tag, Class<T> fragmentClass)
	{
		mActivity = activity;
		mTag = tag;
		mClass = fragmentClass;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
	{
		// Check if the fragment is already initialized
		if (mFragment == null)
		{
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			ft.add(android.R.id.content, mFragment, mTag);
		}
		else
		{
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
		}
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

