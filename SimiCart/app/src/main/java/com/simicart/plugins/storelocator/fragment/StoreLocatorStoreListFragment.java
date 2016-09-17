package com.simicart.plugins.storelocator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.block.StoreLocatorStoreListBlock;
import com.simicart.plugins.storelocator.controller.StoreLocatorStoreListController;
import com.simicart.plugins.storelocator.entity.SearchObject;

public class StoreLocatorStoreListFragment extends SimiFragment {

    protected StoreLocatorStoreListBlock mBlock;
    protected StoreLocatorStoreListController mController;
    protected SearchObject searchObject;

    public static StoreLocatorStoreListFragment newInstance(SimiData data) {
        StoreLocatorStoreListFragment fragment = new StoreLocatorStoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_store_list"), container, false);

        if (mData != null) {
            searchObject = (SearchObject) getValueWithKey(Constants.KeyData.SEARCH_OBJECT);
        }

        mBlock = new StoreLocatorStoreListBlock(rootView, getActivity());
        mBlock.initView();
        if (mController == null) {
            mController = new StoreLocatorStoreListController();
            mController.setDelegate(mBlock);
            mController.setSearchObject(searchObject);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }
        mBlock.onListStoreScroll(mController.getOnListScroll());
        mBlock.onSearchClick(mController.getOnSearchClick());

        return rootView;
    }

}
