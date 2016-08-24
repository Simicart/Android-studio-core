package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookBlock;
import com.simicart.core.customer.controller.AddressBookController;

public class AddressBookFragment extends SimiFragment {

	protected int addressBookFor = -1;
	protected AddressBookBlock mBlock;
	protected AddressBookController mController;

	public static AddressBookFragment newInstance(SimiData data) {
		AddressBookFragment fragment = new AddressBookFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DATA, data);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_fragment_address_book"),
				container, false);

		if(mData != null) {
			addressBookFor = (int) getValueWithKey("address_book_for");
		}

		mBlock = new AddressBookBlock(view, getActivity());
		mBlock.setAddressBookFor(addressBookFor);
		mBlock.initView();

		if (null == mController) {
			mController = new AddressBookController();
			mController.setAddressBookFor(addressBookFor);
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setAddressBookFor(addressBookFor);
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setonTouchListener(mController.getListener());

		return view;

	}

}
