package com.simicart.plugins.storelocator.fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.entity.SearchObject;

import java.util.HashMap;

public class StoreLocatorMainPageTabletFragment extends SimiFragment {

	protected SearchObject searchObject;

	public static StoreLocatorMainPageTabletFragment newInstance(SimiData data) {
		StoreLocatorMainPageTabletFragment fragment = new StoreLocatorMainPageTabletFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DATA, data);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_main"), container, false);

		if(mData != null) {
			searchObject = (SearchObject) getValueWithKey(Constants.KeyData.SEARCH_OBJECT);
		}

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
		createView();

		return rootView;
	}

	protected void createView() {
		HashMap<String, Object> hmData = new HashMap<>();
		hmData.put(Constants.KeyData.SEARCH_OBJECT, searchObject);
		StoreLocatorStoreListFragment storeListFragment = StoreLocatorStoreListFragment.newInstance(new SimiData(hmData));
		SimiManager.getIntance().getManager().beginTransaction()
				.replace(Rconfig.getInstance().id("list_store_container"), storeListFragment).commit();
		SimiManager.getIntance().getManager().beginTransaction()
				.replace(Rconfig.getInstance().id("map_container"), StoreLocatorMapFragment.newInstance()).commit();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		createView();
	}

}
