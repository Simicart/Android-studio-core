package com.simicart.plugins.storelocator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.block.StoreLocatorSearchStoreBlock;
import com.simicart.plugins.storelocator.controller.StoreLocatorSearchStoreController;
import com.simicart.plugins.storelocator.entity.SearchObject;

public class StoreLocatorSearchStoreFragment extends SimiFragment {

	protected StoreLocatorSearchStoreBlock mBlock;
	protected StoreLocatorSearchStoreController mController;
	protected SearchObject search_object;

	public static StoreLocatorSearchStoreFragment newInstance(SimiData data) {
		StoreLocatorSearchStoreFragment fragment = new StoreLocatorSearchStoreFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DATA, data);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_search_store"), container,
				false);

		// data
		if (mData != null) {
			search_object = (SearchObject) getValueWithKey(Constants.KeyData.SEARCH_OBJECT);
		}

		mBlock = new StoreLocatorSearchStoreBlock(rootView, getActivity());
		mBlock.setSearchObject(search_object);
		mBlock.initView();
		if (mController == null) {
			mController = new StoreLocatorSearchStoreController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.onClearAllClick(mController.getOnClearSearchClick());
		mBlock.onSearchClick(mController.getOnSearchClick());

		return rootView;
	}

}
