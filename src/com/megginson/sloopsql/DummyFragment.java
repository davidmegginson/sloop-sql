package com.megginson.sloopsql;

import android.app.Fragment;
import android.view.View;
import java.util.zip.Inflater;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class DummyFragment extends Fragment

{

public View onCreateView(LayoutInflater inflater, ViewGroup container)
{
	return new TextView(getActivity());
}

}
