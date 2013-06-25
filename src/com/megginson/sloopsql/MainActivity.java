package com.megginson.sloopsql;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.megginson.sloopsql.R;

public class MainActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Create new fragment and transaction
		Fragment queryFragment = new QueryFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(android.R.id.content, queryFragment);
		transaction.commit();
	}

}
