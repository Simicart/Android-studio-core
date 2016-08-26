package com.simicart.plugins.locator.fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.adapter.TabAdapter;
import com.simicart.plugins.locator.entity.SearchObject;

import java.util.HashMap;

public class StoreLocatorMainPageFragment extends SimiFragment {
	
	protected ViewPager mPager;
	protected SearchObject searchObject;
	protected TabLayout title_tab;
	TabAdapter adapter;
	
	public static StoreLocatorMainPageFragment newInstance(SimiData data) {
		StoreLocatorMainPageFragment fragment = new StoreLocatorMainPageFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DATA, data);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_mainpage"), container, false);

		if(mData != null) {
			searchObject = (SearchObject) getValueWithKey(Constants.KeyData.SEARCH_OBJECT);
		}
		
		mPager = (ViewPager) rootView.findViewById(Rconfig
				.getInstance().id("pager_store_locator"));

		adapter = new TabAdapter(getChildFragmentManager());
		mPager.setAdapter(adapter);

		title_tab = (TabLayout) rootView
				.findViewById(Rconfig.getInstance().id("tab_store_locator"));
		title_tab.setBackgroundColor(AppColorConfig.getInstance()
				.getSectionColor());

		String[] LOCATION_PERMS={
				Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
		};
		if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
			if (DataLocal.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) == false
					|| DataLocal.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == false) {
				SimiManager.getIntance().getCurrentActivity().requestPermissions(LOCATION_PERMS, KeyData.REQUEST_PERMISSIONS.LOCATION_REQUEST);
				return rootView;
			}
		}
		setupViewPager();
		
		return rootView;
	}
	
	protected void setupViewPager() {
		HashMap<String, Object> hmData = new HashMap<>();
		hmData.put(Constants.KeyData.SEARCH_OBJECT, searchObject);
		StoreLocatorStoreListFragment storeListFragment = StoreLocatorStoreListFragment.newInstance(new SimiData(hmData));
        adapter.addFragment(storeListFragment
        		, SimiTranslator.getInstance().translate("Store List"));
        adapter.addFragment(StoreLocatorMapFragment.newInstance()
        		, SimiTranslator.getInstance().translate("Map"));
		adapter.notifyDataSetChanged();
		title_tab.setupWithViewPager(mPager);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		setupViewPager();
	}
}
