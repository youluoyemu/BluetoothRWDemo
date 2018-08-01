package com.example.admin.bluetoothrwdemo.ui.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

	public void runOnUiThread(Runnable runnable) {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(runnable);
	}
}
