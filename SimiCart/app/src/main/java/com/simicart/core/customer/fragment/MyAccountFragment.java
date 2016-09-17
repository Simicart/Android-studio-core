package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.MyAccountBlock;
import com.simicart.core.customer.controller.MyAccountController;

public class MyAccountFragment extends SimiFragment {
	MyAccountBlock mBlock;
	MyAccountController mController;

	public static MyAccountFragment newInstance() {
		MyAccountFragment fragment = new MyAccountFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setScreenName("My Account Screen");
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_my_account_layout"),
				container, false);
		Context context = getActivity();
		mBlock = new MyAccountBlock(view, context);
		mBlock.initView();
		if(mController == null) {
			mController = new MyAccountController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		return view;
	}
}
