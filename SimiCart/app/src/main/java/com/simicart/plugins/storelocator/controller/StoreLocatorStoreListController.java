package com.simicart.plugins.storelocator.controller;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.plugins.storelocator.delegate.StoreLocatorStoreListDelegate;
import com.simicart.plugins.storelocator.entity.SearchObject;
import com.simicart.plugins.storelocator.fragment.StoreLocatorSearchStoreFragment;
import com.simicart.plugins.storelocator.model.ModelLocator;

import java.util.HashMap;

public class StoreLocatorStoreListController extends SimiController {

    protected OnClickListener onSearchClick;
    protected RecyclerView.OnScrollListener onListScroll;
    protected int limit = 10;
    protected int offset = 0;
    protected int itemCount = 0;
    protected String resultNumber;
    protected boolean isOnscroll = true;
    protected SearchObject searchObject;
    protected Location currrentLocation = null;
    protected GoogleApiClient mGoogleApiClient;

    protected StoreLocatorStoreListDelegate mDelegate;

    public void setDelegate(StoreLocatorStoreListDelegate delegate) {
        mDelegate = delegate;
    }

    public void setSearchObject(SearchObject searchObject) {
        this.searchObject = searchObject;
    }

    public RecyclerView.OnScrollListener getOnListScroll() {
        return onListScroll;
    }

    public OnClickListener getOnSearchClick() {
        return onSearchClick;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        requestGetCurrentLocation();

        onListScroll = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    // Scrolling up
                    mDelegate.visibleSearchLayout(true);
                } else {
                    // Scrolling down
                    mDelegate.visibleSearchLayout(false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                itemCount = recyclerView.getChildCount();
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int threshold = offset + limit - 1;
                if (lastPosition == threshold
                        && offset <= itemCount) {
                    if (isOnscroll) {
                        offset += limit;
                        isOnscroll = false;
                        mDelegate.showLoadMore(true);
                        requestGetListStore(false);
                    }
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
                HashMap<String, Object> hmData = new HashMap<String, Object>();
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

    public void requestGetCurrentLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(SimiManager.getIntance().getCurrentActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        currrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (currrentLocation != null) {
                            requestGetListStore(true);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
                resultNumber = ((ModelLocator) mModel).getTotalResult();
                mDelegate.updateView(mModel.getCollection());
                if (itemCount >= offset) {
                    isOnscroll = true;
                }
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
        if (currrentLocation != null) {
            mModel.addBody("lat", "" + currrentLocation.getLatitude());
            mModel.addBody("lng", "" + currrentLocation.getLongitude());
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
