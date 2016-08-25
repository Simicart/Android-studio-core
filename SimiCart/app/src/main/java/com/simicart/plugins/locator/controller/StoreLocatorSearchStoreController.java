package com.simicart.plugins.locator.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.plugins.locator.delegate.StoreLocatorSearchStoreDelegate;
import com.simicart.plugins.locator.entity.SearchObject;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageTabletFragment;
import com.simicart.plugins.locator.model.GetSearchConfigModel;
import com.simicart.plugins.locator.model.GetSearchCountryModel;
import com.simicart.plugins.locator.model.GetTagSearchModel;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class StoreLocatorSearchStoreController extends SimiController {
	
	protected OnClickListener onSearchClick;
	protected OnClickListener onClearSearchClick;
	protected OnItemClickListener onSearchByTag;
	protected StoreLocatorSearchStoreDelegate mDelegate;
	protected GetSearchConfigModel configModel;
	protected GetSearchCountryModel countryModel;
	protected GetTagSearchModel tagModel;
	protected int isCompleteRequest = 0;
	
	public void setDelegate(StoreLocatorSearchStoreDelegate delegate) {
		mDelegate = delegate;
	}
	
	public OnClickListener getOnSearchClick() {
		return onSearchClick;
	}
	
	public OnClickListener getOnClearSearchClick() {
		return onClearSearchClick;
	}
	
	public OnItemClickListener getOnSearchBytag() {
		return onSearchByTag;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		requestGetConfig();
		requestGetCountry();
		requestGetTagSearch();
		
		onSearchClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchObject search = mDelegate.getSearchObject();
				if(search != null) {
					if(DataLocal.isTablet) {
						StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
						fragment.setSearchObject(search);
						SimiManager.getIntance().replaceFragment(fragment);
					} else {
						StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
						fragment.setSearchObject(search);
						SimiManager.getIntance().replaceFragment(fragment);
					}
					SimiManager.getIntance().removeDialog();
					Utils.hideKeyboard(v);
				}
			}
		};
		
		onClearSearchClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(DataLocal.isTablet) {
					StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
					SimiManager.getIntance().replaceFragment(fragment);
				} else {
					StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
					SimiManager.getIntance().replaceFragment(fragment);
				}
				SimiManager.getIntance().removeDialog();
				Utils.hideKeyboard(v);
			}
		};
		
		onSearchByTag = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SearchObject search = new SearchObject();
				search.setTag(position);
				if(DataLocal.isTablet) {
					StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
					fragment.setSearchObject(search);
					SimiManager.getIntance().replaceFragment(fragment);
				} else {
					StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
					fragment.setSearchObject(search);
					SimiManager.getIntance().replaceFragment(fragment);
				}
				SimiManager.getIntance().removeDialog();
			}
		};
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mDelegate.setListConfig(configModel.getConfigs());
		mDelegate.setListCountry(countryModel.getCountries());
		mDelegate.setListTag(tagModel.getTags());
		mDelegate.updateView(mModel.getCollection());
	}
	
	protected void requestGetConfig() {
		mDelegate.showLoading();
		configModel = new GetSearchConfigModel();
		configModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.setListConfig(configModel.getConfigs());
				isCompleteRequest++;
				if(isCompleteRequest == 3) {
					updateView();
				}
			}
		});
		configModel.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				SimiNotify.getInstance().showNotify(error.getMessage());
			}
		});
		configModel.request();
	}
	
	protected void requestGetCountry() {
		countryModel = new GetSearchCountryModel();
		countryModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.setListCountry(countryModel.getCountries());
				isCompleteRequest++;
				if(isCompleteRequest == 3) {
					updateView();
				}
			}
		});
		countryModel.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				SimiNotify.getInstance().showNotify(error.getMessage());
			}
		});
		countryModel.request();
	}
	
	protected void requestGetTagSearch() {
		tagModel = new GetTagSearchModel();
		tagModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.setListTag(tagModel.getTags());
				isCompleteRequest++;
				if(isCompleteRequest == 3) {
					updateView();
				}
			}
		});
		tagModel.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				SimiNotify.getInstance().showNotify(error.getMessage());
			}
		});
		tagModel.request();
	}

	protected void updateView() {
		mDelegate.dismissLoading();
		mDelegate.updateView(null);
	}

}
