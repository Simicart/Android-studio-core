package com.simicart.plugins.storelocator.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.plugins.storelocator.delegate.StoreLocatorSearchStoreDelegate;
import com.simicart.plugins.storelocator.entity.SearchObject;
import com.simicart.plugins.storelocator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.storelocator.fragment.StoreLocatorMainPageTabletFragment;
import com.simicart.plugins.storelocator.model.GetSearchConfigModel;
import com.simicart.plugins.storelocator.model.GetSearchCountryModel;
import com.simicart.plugins.storelocator.model.GetTagSearchModel;

import java.util.HashMap;

public class StoreLocatorSearchStoreController extends SimiController {

    protected OnClickListener onSearchClick;
    protected OnClickListener onClearSearchClick;
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
                if (search != null) {
                    onSearchAction(search);
                    Utils.hideKeyboard(v);
                }
            }
        };

        onClearSearchClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onSearchAction(null);
                Utils.hideKeyboard(v);
            }
        };

    }

    protected void onSearchAction(SearchObject searchObject) {
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.SEARCH_OBJECT, searchObject);
        if (DataLocal.isTablet) {
            StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance(new SimiData(hmData));
            SimiManager.getIntance().replaceFragment(fragment);
        } else {
            StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance(new SimiData(hmData));
            SimiManager.getIntance().replaceFragment(fragment);
        }
        SimiManager.getIntance().removeDialog();
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
                if (isCompleteRequest == 3) {
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
                if (isCompleteRequest == 3) {
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
                if (isCompleteRequest == 3) {
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
        tagModel.addBody("offset", "0");
        tagModel.addBody("limit", "10");
        tagModel.request();
    }

    protected void updateView() {
        mDelegate.dismissLoading();
        mDelegate.updateView(null);
    }

}
