package com.simicart.plugins.locator.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.plugins.locator.delegate.StoreLocatorStoreListDelegate;
import com.simicart.plugins.locator.entity.SearchObject;
import com.simicart.plugins.locator.fragment.StoreDetailFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorSearchStoreFragment;
import com.simicart.plugins.locator.model.ModelLocator;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.HashMap;

public class StoreLocatorStoreListController extends SimiController {

	protected OnItemClickListener onListItemClick;
	protected OnClickListener onSearchClick;
	protected OnScrollListener onListScroll;
	protected int limit = 10;
	protected int offset = 0;
	protected String resultNumber;
	protected boolean isOnscroll = true;
	protected SearchObject searchObject;

	protected StoreLocatorStoreListDelegate mDelegate;

	public void setDelegate(StoreLocatorStoreListDelegate delegate) {
		mDelegate = delegate;
	}

	public void setSearchObject(SearchObject searchObject) {
		this.searchObject = searchObject;
	}

	public OnScrollListener getOnListScroll() {
		return onListScroll;
	}

	public OnClickListener getOnSearchClick() {
		return onSearchClick;
	}

	public OnItemClickListener getOnListItemClick() {
		return onListItemClick;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		requestGetListStore(true);

		onListScroll = new OnScrollListener() {

			int currentFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				try {
					int threshold = 1;
					int count = view.getCount();
					Log.e("Count :", count + "");
					if (scrollState == SCROLL_STATE_IDLE) {
						if ((view.getLastVisiblePosition() >= count - threshold)
								&& Integer.parseInt(resultNumber) > count) {
							Log.e("ResultNumber :", resultNumber);
							Log.e("IsOnscroll:", isOnscroll + "");
							mDelegate.showLoadMore(true);
							if (isOnscroll == true) {
								isOnscroll = false;
								offset += limit;
								requestGetListStore(false);
							}
						} else {
							mDelegate.showLoadMore(false);
						}
					}

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				try {
					if (currentFirstVisibleItem > firstVisibleItem) {
						mDelegate.visibleSearchLayout(true);
					} else if (currentFirstVisibleItem < firstVisibleItem) {
						mDelegate.visibleSearchLayout(false);
					}
					currentFirstVisibleItem = firstVisibleItem;
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		};

		onListItemClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String,Object> hmData = new HashMap<String, Object>();
				hmData.put(Constants.KeyData.STORE_OBJECT, mDelegate.getListStore().get(position));
				StoreDetailFragment detail = StoreDetailFragment.newInstance(new SimiData(hmData));
				if (DataLocal.isTablet) {
					SimiManager.getIntance().addPopupFragment(detail);
				} else {
					SimiManager.getIntance().replaceFragment(detail);
				}
			}
		};

		onSearchClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (searchObject == null) {
					searchObject = new SearchObject();
				}
				HashMap<String,Object> hmData = new HashMap<String, Object>();
				hmData.put(Constants.KeyData.SEARCH_OBJECT, searchObject);
				StoreLocatorSearchStoreFragment fragment = StoreLocatorSearchStoreFragment.newInstance(new SimiData(hmData));
				SimiManager.getIntance().addPopupFragment(fragment);
			}
		};

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		requestGetListStore(true);
	}

	protected void requestGetListStore(boolean showLoading) {
		if (showLoading == true) {
			mDelegate.showLoading();
		}
		mModel = new ModelLocator();
		mModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.dismissLoading();
				mDelegate.showLoadMore(false);
				resultNumber = ((ModelLocator)mModel).getTotalResult();
				mDelegate.updateView(mModel.getCollection());
				isOnscroll = true;
			}
		});
		mModel.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				mDelegate.dismissLoading();
				SimiTranslator.getInstance()
						.translate(SimiTranslator.getInstance().translate("No store match with your searching"));
			}
		});
		mModel.addBody("limit", "" + limit);
		mModel.addBody("offset", "" + offset);
		Location location = mDelegate.getCurrentLocation();
		if(location != null) {
			mModel.addBody("lat", "" + location.getLatitude());
			mModel.addBody("lng", "" + location.getLongitude());
		}
		if (searchObject != null) {
			String country = searchObject.getName_country();
			if (Utils.validateString(country)) {
				mModel.addBody("country", country);
			}
			String city = searchObject.getCity();
			if (Utils.validateString(city)) {
				mModel.addBody("city", city);
			}
			String state = searchObject.getState();
			if (Utils.validateString(state)) {
				mModel.addBody("state", state);
			}
			String zipcode = searchObject.getZipcode();
			if (Utils.validateString(zipcode)) {
				mModel.addBody("zipcode", zipcode);
			}
			int tag = searchObject.getTag();
			if (tag != -1) {
				mModel.addBody("tag", "" + tag);
			}
		}
		mModel.request();
	}

}
